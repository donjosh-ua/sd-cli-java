package distributed.systems.sd_cli_java.service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import distributed.systems.sd_cli_java.mapper.ExpenseMapper;
import distributed.systems.sd_cli_java.mapper.UserMapper;
import distributed.systems.sd_cli_java.model.dto.expense.ExpenseRequestDTO;
import distributed.systems.sd_cli_java.model.dto.user.UserResponseDTO;
import distributed.systems.sd_cli_java.model.dto.expense.ExpenseDTO;
import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.repository.ExpenseRepository;
import distributed.systems.sd_cli_java.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final UserRepository userRepository;
    private final ExpenseMapper expenseMapper;
    private final UserMapper userMapper;

    private List<String> extractParticipants(Object participantsObj) {

        List<String> participants = new ArrayList<>();

        if (participantsObj instanceof List<?>)
            for (Object o : (List<?>) participantsObj)
                if (o instanceof String s)
                    participants.add(s);

        return participants;
    }

    private void updateStringField(Map<String, Object> expense, String key, Consumer<String> setter) {
        if (expense.get(key) != null)
            setter.accept((String) expense.get(key));
    }

    public ExpenseDTO createExpense(ExpenseRequestDTO expense) {

        if (expense.getAmount() <= 0)
            throw new IllegalArgumentException("Expense amount must be greater than zero");

        if (expense.getPlanId() == null)
            throw new IllegalArgumentException("Expense must be associated with a plan");

        Expense expenseEntity = expenseMapper.toEntity(expense);

        expenseEntity.setParticipants(new ArrayList<>());
        expenseEntity.setDate(LocalDateTime.now());
        expenseEntity.setName(expense.getName());
        expenseEntity.setAmount(expense.getAmount());
        expenseEntity.setType(expense.getType());

        for (String participantEmail : expense.getParticipants())
            userRepository.findByEmail(participantEmail).ifPresent(u -> expenseEntity.getParticipants().add(u));

        expenseRepository.save(expenseEntity);

        return expenseMapper.toDto(expenseEntity);
    }

    public ExpenseDTO updateExpense(Map<String, Object> expense) {

        Expense existingExpense = expenseRepository.findById(((Number) expense.get("expenseId")).longValue())
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        updateStringField(expense, "name", existingExpense::setName);
        updateStringField(expense, "type", existingExpense::setType);
        updateStringField(expense, "icon", existingExpense::setIcon);

        if (expense.get("amount") != null) {
            Float amount = Float.parseFloat(expense.get("amount").toString());
            if (amount <= 0)
                throw new IllegalArgumentException("Expense amount must be greater than zero");
            existingExpense.setAmount(amount);
        }

        if (expense.get("participants") != null) {

            List<String> participants = extractParticipants(expense.get("participants"));

            existingExpense.getParticipants().clear();

            for (String participantEmail : participants)
                userRepository.findByEmail(participantEmail)
                        .ifPresent(user -> existingExpense.getParticipants().add(user));
        }

        return expenseMapper.toDto(expenseRepository.save(existingExpense));
    }

    public Optional<ExpenseDTO> findById(Long id) {

        if (!expenseRepository.existsById(id))
            throw new IllegalArgumentException("Expense not found");

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

        List<User> participants = expenseRepository.findById(expenseId)
                .map(Expense::getParticipants)
                .orElseThrow(() -> new IllegalArgumentException("No participants found for this expense"));

        return userMapper.toResponseDTOList(participants);
    }

    public Object joinExpense(Map<String, Object> fields) {

        Long expenseId = ((Number) fields.get("expenseId")).longValue();
        String userEmail = (String) fields.get("email");

        if (expenseId == null || userEmail == null)
            throw new IllegalArgumentException("Expense ID and User Email are required");

        Expense expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new IllegalArgumentException("Expense not found"));

        var user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (expense.getParticipants().stream().anyMatch(u -> u.getEmail().equals(user.getEmail())))
            throw new IllegalArgumentException("User is already a participant of this expense");

        expense.getParticipants().add(user);
        expenseRepository.save(expense);

        return expenseMapper.toDto(expense);
    }

    public Object quitExpense(Map<String, Object> fields) {

        Long expenseId = ((Number) fields.get("expenseId")).longValue();
        String userEmail = (String) fields.get("email");

        if (expenseId == null || userEmail == null)
            throw new IllegalArgumentException("Expense ID and User Email are required");

        Expense expense = expenseRepository.findById(expenseId).orElseThrow(
                () -> new IllegalArgumentException("Expense not found"));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (expense.getParticipants().stream().noneMatch(p -> p.getEmail().equals(user.getEmail())))
            throw new IllegalArgumentException("User is not a participant of this expense");

        expense.getParticipants().remove(user);
        expenseRepository.save(expense);

        return expenseMapper.toDto(expense);
    }

}
