package distributed.systems.sd_cli_java.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.mapper.ExpenseMapper;
import distributed.systems.sd_cli_java.model.dto.expense.ExpenseDTO;
import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.repository.ExpenseRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final ExpenseMapper expenseMapper;

    public ExpenseDTO createExpense(ExpenseDTO expense) {
        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero");
        }

        if (expense.getPlanId() == null) {
            throw new IllegalArgumentException("Expense must be associated with a plan");
        }

        Expense expenseEntity = expenseMapper.toEntity(expense);
        expenseEntity.setDate(LocalDateTime.now());

        expenseRepository.save(expenseEntity);

        return expenseMapper.toDto(expenseEntity);
    }

    public ExpenseDTO updateExpense(ExpenseDTO expense) {

        if (!expenseRepository.existsById(expense.getExpenseId())) {
            throw new IllegalArgumentException("Expense not found");
        }

        return expenseMapper.toDto(expenseRepository.save(expenseMapper.toEntity(expense)));
    }

    public Optional<ExpenseDTO> findById(Long id) {

        if (!expenseRepository.existsById(id)) {
            throw new IllegalArgumentException("Expense not found");
        }

        return expenseRepository.findById(id).map(expenseMapper::toDto);
    }

    public List<ExpenseDTO> findAllExpenses() {
        return expenseMapper.toDtoList(expenseRepository.findAll());
    }

    public void deleteExpense(Long id) {
        expenseRepository.deleteById(id);
    }

}
