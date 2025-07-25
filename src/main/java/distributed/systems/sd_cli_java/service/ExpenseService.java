package distributed.systems.sd_cli_java.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.mapper.ExpenseMapper;
import distributed.systems.sd_cli_java.mapper.UserMapper;
import distributed.systems.sd_cli_java.model.dto.expense.ExpenseRequestDTO;
import distributed.systems.sd_cli_java.model.dto.user.UserResponseDTO;
import distributed.systems.sd_cli_java.model.dto.expense.ExpenseDTO;
import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.repository.ExpenseRepository;
import distributed.systems.sd_cli_java.repository.UserRepository;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseMapper expenseMapper;
    private final UserMapper userMapper;

    public ExpenseDTO createExpense(ExpenseRequestDTO expense) {
        if (expense.getAmount() <= 0) {
            throw new IllegalArgumentException("Expense amount must be greater than zero");
        }

        if (expense.getPlanId() == null) {
            throw new IllegalArgumentException("Expense must be associated with a plan");
        }

        Expense expenseEntity = expenseMapper.toEntity(expense);

        expenseEntity.setParticipants(new ArrayList<>());
        expenseEntity.setDate(LocalDateTime.now());
        expenseEntity.setName(expense.getName());
        expenseEntity.setAmount(expense.getAmount());
        expenseEntity.setType(expense.getType());

        for (String participantEmail : expense.getParticipants()) {
            userRepository.findByEmail(participantEmail).ifPresent(
                    user -> expenseEntity.getParticipants().add(user));
        }

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

    public List<UserResponseDTO> findParticipantsByExpenseId(Long expenseId) {

        if (!expenseRepository.existsById(expenseId))
            throw new IllegalArgumentException("Expense not found");

        return expenseRepository.findById(expenseId)
                .map(Expense::getParticipants)
                .orElseThrow(() -> new IllegalArgumentException("No participants found for this expense"))
                .stream()
                .map(userMapper::toResponseDTO)
                .toList();
    }

}
