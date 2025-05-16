package distributed.systems.sd_cli_java.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.model.entity.Debt;
import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.repository.DebtRepository;
import distributed.systems.sd_cli_java.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class DebtService {

    private final DebtRepository debtRepository;
    private final UserRepository userRepository;

    public Debt createDebt(Debt debt) {
        return debtRepository.save(debt);
    }

    public Debt updateDebt(Debt debt) {
        return debtRepository.save(debt);
    }

    public Optional<Debt> findById(Long id) {
        return debtRepository.findById(id);
    }

    public List<Debt> findAllDebts() {
        return debtRepository.findAll();
    }

    public void deleteDebt(Long id) {
        debtRepository.deleteById(id);
    }

    public List<Debt> findDebtsByBorrower(User borrower) {
        return debtRepository.findByBorrower(borrower);
    }

    public List<Debt> findDebtsByLender(User lender) {
        return debtRepository.findByLender(lender);
    }

    public List<Debt> findDebtsByExpense(Long expenseId) {
        return debtRepository.findByExpenseExpenseId(expenseId);
    }

    public List<Debt> findDebtsBetweenUsers(User lender, User borrower) {
        return debtRepository.findByLenderAndBorrower(lender, borrower);
    }

    public Float calculateTotalDebtForBorrower(User borrower) {
        List<Debt> debts = debtRepository.findByBorrower(borrower);
        return debts.stream()
                .map(Debt::getAmount)
                .reduce(0.0f, Float::sum);
    }

    public Float calculateTotalLoanForLender(User lender) {
        List<Debt> loans = debtRepository.findByLender(lender);
        return loans.stream()
                .map(Debt::getAmount)
                .reduce(0.0f, Float::sum);
    }

    public List<Debt> createDebtsForSharedExpense(Expense expense, List<User> planUsers) {
        User payer = expense.getUser();
        Float amountPerUser = expense.getAmount() / planUsers.size();

        return planUsers.stream()
                .filter(user -> !user.equals(payer))
                .map(borrower -> {
                    Debt debt = new Debt();
                    debt.setAmount(amountPerUser);
                    debt.setExpense(expense);
                    debt.setLender(payer);
                    debt.setBorrower(borrower);
                    return debtRepository.save(debt);
                })
                .collect(Collectors.toList());
    }

    public Debt createLoanDebt(User lender, User borrower, Float amount, Expense expense) {
        Debt debt = new Debt();
        debt.setAmount(amount);
        debt.setExpense(expense);
        debt.setLender(lender);
        debt.setBorrower(borrower);
        return debtRepository.save(debt);
    }

    public Map<User, Float> calculateDebtSummaryForPlan(Plan plan) {
        List<User> users = userRepository.findByPlan(plan);

        return users.stream()
                .collect(Collectors.toMap(
                        user -> user,
                        user -> calculateTotalLoanForLender(user) - calculateTotalDebtForBorrower(user)));
    }

}
