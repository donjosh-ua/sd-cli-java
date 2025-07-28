package distributed.systems.sd_cli_java.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distributed.systems.sd_cli_java.model.dto.expense.ExpenseRequestDTO;
import distributed.systems.sd_cli_java.service.ExpenseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping("/{expenseId}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long expenseId) {
        return new ResponseEntity<>(expenseService.findById(expenseId), HttpStatus.OK);
    }

    @GetMapping("/participants/{expenseId}")
    public ResponseEntity<?> getParticipants(@PathVariable Long expenseId) {
        return new ResponseEntity<>(expenseService.findParticipantsByExpenseId(expenseId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createExpense(@RequestBody ExpenseRequestDTO expense) {
        return new ResponseEntity<>(expenseService.createExpense(expense), HttpStatus.CREATED);
    }

    @PostMapping("/join")
    public ResponseEntity<?> joinExpense(@RequestBody Map<String, Object> fields) {
        return new ResponseEntity<>(expenseService.joinExpense(fields), HttpStatus.OK);
    }

    @PostMapping("/quit")
    public ResponseEntity<?> quitExpense(@RequestBody Map<String, Object> fields) {
        return new ResponseEntity<>(expenseService.quitExpense(fields), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<?> updateExpense(@RequestBody Map<String, Object> fields) {
        return new ResponseEntity<>(expenseService.updateExpense(fields), HttpStatus.OK);
    }

    @DeleteMapping
    public ResponseEntity<?> deleteExpense(@RequestBody Map<String, Long> fields) {
        // TODO: Review this method

        Long expenseId = fields.get("id");

        if (expenseId == null) {
            return new ResponseEntity<>("Expense ID is required", HttpStatus.BAD_REQUEST);
        }

        expenseService.deleteExpense(expenseId);

        return new ResponseEntity<>("Expense deleted successfully", HttpStatus.OK);
    }

}
