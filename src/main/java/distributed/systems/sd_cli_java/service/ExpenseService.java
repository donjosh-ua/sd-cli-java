package distributed.systems.sd_cli_java.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final KafkaProducerService kafkaProducerService;

    public Expense createExpense(Expense expense) {

        Expense savedExpense = expenseRepository.save(expense);

        if (savedExpense.getPlan() != null) {
            kafkaProducerService.sendExpenseCreatedNotification(savedExpense);
        }

        return savedExpense;
    }

    public Expense updateExpense(Expense expense) {

        Expense updatedExpense = expenseRepository.save(expense);

        if (updatedExpense.getPlan() != null) {
            kafkaProducerService.sendExpenseCreatedNotification(updatedExpense);
        }

        return updatedExpense;
    }

    public Optional<Expense> findById(Long id) {
        return expenseRepository.findById(id);
    }

    public List<Expense> findAllExpenses() {
        return expenseRepository.findAll();
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

    public List<Expense> findExpensesByUser(User user) {
        return expenseRepository.findByUser(user);
    }

    public List<Expense> findByPlan(Plan plan) {
        return expenseRepository.findByPlan(plan);
    }

    public List<Expense> findExpensesByType(String type) {
        return expenseRepository.findByType(type);
    }

    public List<Expense> findExpensesByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return expenseRepository.findByDateBetween(startDate, endDate);
    }

    public List<Expense> findExpensesByPlanAndType(Plan plan, String type) {
        return expenseRepository.findByPlanAndType(plan, type);
    }

    public List<Expense> findExpensesByUserAndPlan(User user, Plan plan) {
        return expenseRepository.findByUserAndPlan(user, plan);
    }

    public Float calculateTotalExpensesForPlan(Plan plan) {
        List<Expense> expenses = expenseRepository.findByPlan(plan);
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(0.0f, Float::sum);
    }

    public Float calculateTotalExpensesForUser(User user) {
        List<Expense> expenses = expenseRepository.findByUser(user);
        return expenses.stream()
                .map(Expense::getAmount)
                .reduce(0.0f, Float::sum);
    }

}
