package distributed.systems.sd_cli_java.controller;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.service.ExpenseService;
import distributed.systems.sd_cli_java.service.PlanService;
import distributed.systems.sd_cli_java.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/expenses")
@RequiredArgsConstructor
public class ExpenseController {

    private final ExpenseService expenseService;
    private final UserService userService;
    private final PlanService planService;

    @PostMapping("/create")
    public ResponseEntity<Expense> createExpense(@RequestBody Expense expenseRequest) {
        // Extract IDs
        Long userId = expenseRequest.getUser().getUserId();
        Long planId = expenseRequest.getPlan() != null ? expenseRequest.getPlan().getPlanId() : null;

        // Look up the actual entities
        User user = userService.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));

        Plan plan = null;
        if (planId != null) {
            plan = planService.findById(planId)
                    .orElseThrow(() -> new IllegalArgumentException("Plan not found with ID: " + planId));
        }

        // Create a new expense with the actual entities
        Expense expense = Expense.builder()
                .name(expenseRequest.getName())
                .amount(expenseRequest.getAmount())
                .date(expenseRequest.getDate())
                .type(expenseRequest.getType())
                .user(user)
                .plan(plan)
                .build();

        Expense createdExpense = expenseService.createExpense(expense);
        return new ResponseEntity<>(createdExpense, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Expense> updateExpense(@RequestBody Expense expense) {
        return expenseService.findById(expense.getExpenseId())
                .map(existingExpense -> {
                    return new ResponseEntity<>(expenseService.updateExpense(expense), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return expenseService.findById(id)
                .map(expense -> new ResponseEntity<>(expense, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Expense>> getAllExpenses() {
        List<Expense> expenses = expenseService.findAllExpenses();
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteExpense(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");
        if (expenseService.findById(id).isPresent()) {
            expenseService.deleteExpense(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/by-user/{userId}")
    public ResponseEntity<List<Expense>> getExpensesByUser(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(user -> new ResponseEntity<>(expenseService.findExpensesByUser(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-plan/{planId}")
    public ResponseEntity<List<Expense>> getExpensesByPlan(@PathVariable Long planId) {
        return planService.findById(planId)
                .map(plan -> new ResponseEntity<>(expenseService.findExpensesByPlan(plan), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-type/{type}")
    public ResponseEntity<List<Expense>> getExpensesByType(@PathVariable String type) {
        List<Expense> expenses = expenseService.findExpensesByType(type);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @GetMapping("/by-date-range")
    public ResponseEntity<List<Expense>> getExpensesByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<Expense> expenses = expenseService.findExpensesByDateRange(startDate, endDate);
        return new ResponseEntity<>(expenses, HttpStatus.OK);
    }

    @PostMapping("/by-plan-and-type")
    public ResponseEntity<List<Expense>> getExpensesByPlanAndType(@RequestBody Map<String, Object> payload) {
        Long planId = ((Number) payload.get("planId")).longValue();
        String type = (String) payload.get("type");

        return planService.findById(planId)
                .map(plan -> new ResponseEntity<>(expenseService.findExpensesByPlanAndType(plan, type), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/by-user-and-plan")
    public ResponseEntity<List<Expense>> getExpensesByUserAndPlan(@RequestBody Map<String, Long> payload) {
        Long userId = payload.get("userId");
        Long planId = payload.get("planId");

        return userService.findById(userId)
                .flatMap(user -> planService.findById(planId)
                        .map(plan -> new ResponseEntity<>(expenseService.findExpensesByUserAndPlan(user, plan),
                                HttpStatus.OK)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/total-by-plan/{planId}")
    public ResponseEntity<Float> getTotalExpensesForPlan(@PathVariable Long planId) {
        return planService.findById(planId)
                .map(plan -> new ResponseEntity<>(expenseService.calculateTotalExpensesForPlan(plan), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/total-by-user/{userId}")
    public ResponseEntity<Float> getTotalExpensesForUser(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(user -> new ResponseEntity<>(expenseService.calculateTotalExpensesForUser(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
