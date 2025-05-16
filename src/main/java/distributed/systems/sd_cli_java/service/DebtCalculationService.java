package distributed.systems.sd_cli_java.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.model.entity.Debt;
import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.repository.DebtRepository;
import distributed.systems.sd_cli_java.repository.ExpenseRepository;
import distributed.systems.sd_cli_java.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DebtCalculationService {

    private final DebtRepository debtRepository;
    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;

    public List<Debt> calculateDebtsForExpense(Expense expense) {

        // Do not create debts for invitation expenses
        if ("invitation".equals(expense.getType())) {
            return new ArrayList<>();
        }

        Plan plan = expense.getPlan();
        User payer = expense.getUser();
        List<User> planUsers = userRepository.findByPlan(plan);
        float amountPerPerson = expense.getAmount() / planUsers.size();

        List<Debt> debts = new ArrayList<>();

        for (User user : planUsers) {
            if (!user.equals(payer)) {
                Debt debt = new Debt();
                debt.setAmount(amountPerPerson);
                debt.setExpense(expense);
                debt.setLender(payer);
                debt.setBorrower(user);
                debts.add(debtRepository.save(debt));
            }
        }

        return debts;
    }

    public Map<User, Map<User, Float>> optimizeDebts(Plan plan) {

        List<User> users = userRepository.findByPlan(plan);
        Map<User, Float> balances = calculateNetBalances(plan);
        Map<User, Map<User, Float>> optimizedTransactions = new HashMap<>();

        // Initialize the transactions map for each user
        for (User user : users) {
            optimizedTransactions.put(user, new HashMap<>());
        }

        // Sort users by balance (decreasing order)
        List<User> sortedUsers = users.stream()
                .sorted((u1, u2) -> Float.compare(balances.get(u2), balances.get(u1)))
                .collect(Collectors.toList());

        int i = 0;
        int j = sortedUsers.size() - 1;

        // Keep settling debts between the user who is owed the most and the user who
        // owes the most
        while (i < j) {

            User lender = sortedUsers.get(i);
            User borrower = sortedUsers.get(j);

            float lenderBalance = balances.get(lender);
            float borrowerBalance = balances.get(borrower);

            // If the lender balance is positive and borrower balance is negative
            if (lenderBalance > 0 && borrowerBalance < 0) {

                float amount = Math.min(lenderBalance, -borrowerBalance);

                // Update the optimized transactions
                Map<User, Float> borrowerTransactions = optimizedTransactions.get(borrower);
                borrowerTransactions.put(lender, borrowerTransactions.getOrDefault(lender, 0.0f) + amount);

                // Update balances
                balances.put(lender, lenderBalance - amount);
                balances.put(borrower, borrowerBalance + amount);

                // Move pointers if balance is settled
                if (Math.abs(balances.get(lender)) < 0.01f) {
                    i++;
                }
                if (Math.abs(balances.get(borrower)) < 0.01f) {
                    j--;
                }
            } else {
                // If one of the balances is already settled, move the pointer
                if (Math.abs(lenderBalance) < 0.01f) {
                    i++;
                }
                if (Math.abs(borrowerBalance) < 0.01f) {
                    j--;
                }
            }
        }

        return optimizedTransactions;
    }

    public Map<User, Float> calculateNetBalances(Plan plan) {
        List<User> users = userRepository.findByPlan(plan);
        List<Expense> planExpenses = expenseRepository.findByPlan(plan);
        Map<User, Float> balances = new HashMap<>();

        // Initialize balances to zero
        for (User user : users) {
            balances.put(user, 0.0f);
        }

        // Process all expenses
        for (Expense expense : planExpenses) {
            if ("invitation".equals(expense.getType())) {
                // Skip invitation expenses for debt calculation
                continue;
            }

            User payer = expense.getUser();
            float amount = expense.getAmount();
            float splitAmount = amount / users.size();

            // Update payer's balance - they paid for everyone so are owed money
            balances.put(payer, balances.get(payer) + amount - splitAmount);

            // Update everyone else's balance - they owe money
            for (User user : users) {
                if (!user.equals(payer)) {
                    balances.put(user, balances.get(user) - splitAmount);
                }
            }
        }

        return balances;
    }

    public Map<User, Map<String, Object>> generateDebtSummary(Plan plan) {
        // Get optimized transactions
        Map<User, Map<User, Float>> optimizedTransactions = optimizeDebts(plan);
        Map<User, Map<String, Object>> summary = new HashMap<>();

        // For each user, create a summary of their debts and the expenses that
        // contributed to them
        for (User user : optimizedTransactions.keySet()) {
            Map<String, Object> userSummary = new HashMap<>();

            // Get all expenses this user participated in
            List<Expense> userExpenses = expenseRepository.findByUserAndPlan(user, plan);
            float totalPaid = userExpenses.stream().map(Expense::getAmount).reduce(0.0f, Float::sum);

            // Get all loans this user made
            List<Debt> loansGiven = debtRepository.findByLender(user);
            float totalLoaned = loansGiven.stream().map(Debt::getAmount).reduce(0.0f, Float::sum);

            // Get all debts this user owes
            List<Debt> debtsOwed = debtRepository.findByBorrower(user);
            float totalOwed = debtsOwed.stream().map(Debt::getAmount).reduce(0.0f, Float::sum);

            // Calculate net balance
            float netBalance = totalPaid + totalLoaned - totalOwed;

            // Get the transactions this user needs to make
            Map<User, Float> transactions = optimizedTransactions.get(user);

            userSummary.put("totalPaid", totalPaid);
            userSummary.put("totalLoaned", totalLoaned);
            userSummary.put("totalOwed", totalOwed);
            userSummary.put("netBalance", netBalance);
            userSummary.put("transactions", transactions);
            userSummary.put("expenses", userExpenses);

            summary.put(user, userSummary);
        }

        return summary;
    }

}
