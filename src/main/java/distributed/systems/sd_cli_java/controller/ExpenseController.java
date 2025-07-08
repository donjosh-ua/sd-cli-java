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

import distributed.systems.sd_cli_java.model.dto.ExpenseDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/expenses")
public class ExpenseController {

    @GetMapping
    public ResponseEntity<?> getAllExpenses() {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @GetMapping("/{expenseId}")
    public ResponseEntity<?> getExpenseById(@PathVariable Long expenseId) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PostMapping
    public ResponseEntity<?> createExpense(@RequestBody ExpenseDTO expense) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PutMapping
    public ResponseEntity<?> updateExpense(@RequestBody ExpenseDTO expense) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @DeleteMapping
    public ResponseEntity<?> deleteExpense(@RequestBody Map<String, Long> fields) {
        throw new IllegalArgumentException("Not implemented yet");
    }

}
