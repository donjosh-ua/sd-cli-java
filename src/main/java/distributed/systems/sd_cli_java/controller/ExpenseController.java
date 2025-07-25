package distributed.systems.sd_cli_java.controller;

import java.util.Map;

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
import distributed.systems.sd_cli_java.model.dto.expense.ExpenseDTO;
import distributed.systems.sd_cli_java.service.ExpenseService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    @GetMapping("/{expenseId}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long expenseId) {
        return ResponseEntity.ok(expenseService.findById(expenseId));
    }

    @GetMapping("/participants/{expenseId}")
    public ResponseEntity<?> getParticipants(@PathVariable Long expenseId) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PostMapping
    public ResponseEntity<?> createExpense(@RequestBody ExpenseRequestDTO expense) {
        return ResponseEntity.ok(expenseService.createExpense(expense));
    }

    @PutMapping
    public ResponseEntity<?> updateExpense(@RequestBody ExpenseDTO expense) {
        return ResponseEntity.ok(expenseService.updateExpense(expense));
    }

    @DeleteMapping
    public ResponseEntity<?> deleteExpense(@RequestBody Map<String, Long> fields) {

        Long expenseId = fields.get("id");

        if (expenseId == null) {
            return ResponseEntity.badRequest().body("Expense ID is required");
        }

        expenseService.deleteExpense(expenseId);

        return ResponseEntity.ok("Expense deleted successfully");
    }

}
