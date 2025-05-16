package distributed.systems.sd_cli_java.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distributed.systems.sd_cli_java.model.entity.Debt;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.service.DebtCalculationService;
import distributed.systems.sd_cli_java.service.DebtService;
import distributed.systems.sd_cli_java.service.PlanService;
import distributed.systems.sd_cli_java.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/debts")
@RequiredArgsConstructor
public class DebtController {

    private final DebtService debtService;
    private final DebtCalculationService debtCalculationService;
    private final UserService userService;
    private final PlanService planService;

    @PostMapping("/create")
    public ResponseEntity<Debt> createDebt(@RequestBody Debt debt) {
        Debt createdDebt = debtService.createDebt(debt);
        return new ResponseEntity<>(createdDebt, HttpStatus.CREATED);
    }

    @PostMapping("/update")
    public ResponseEntity<Debt> updateDebt(@RequestBody Debt debt) {
        return debtService.findById(debt.getId())
                .map(existingDebt -> {
                    return new ResponseEntity<>(debtService.updateDebt(debt), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Debt> getDebtById(@PathVariable Long id) {
        return debtService.findById(id)
                .map(debt -> new ResponseEntity<>(debt, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Debt>> getAllDebts() {
        List<Debt> debts = debtService.findAllDebts();
        return new ResponseEntity<>(debts, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deleteDebt(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");
        if (debtService.findById(id).isPresent()) {
            debtService.deleteDebt(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @GetMapping("/by-borrower/{borrowerId}")
    public ResponseEntity<List<Debt>> getDebtsByBorrower(@PathVariable Long borrowerId) {
        return userService.findById(borrowerId)
                .map(borrower -> new ResponseEntity<>(debtService.findDebtsByBorrower(borrower), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-lender/{lenderId}")
    public ResponseEntity<List<Debt>> getDebtsByLender(@PathVariable Long lenderId) {
        return userService.findById(lenderId)
                .map(lender -> new ResponseEntity<>(debtService.findDebtsByLender(lender), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-expense/{expenseId}")
    public ResponseEntity<List<Debt>> getDebtsByExpense(@PathVariable Long expenseId) {
        List<Debt> debts = debtService.findDebtsByExpense(expenseId);
        return new ResponseEntity<>(debts, HttpStatus.OK);
    }

    @PostMapping("/between-users")
    public ResponseEntity<List<Debt>> getDebtsBetweenUsers(@RequestBody Map<String, Long> payload) {
        Long lenderId = payload.get("lenderId");
        Long borrowerId = payload.get("borrowerId");

        return userService.findById(lenderId)
                .flatMap(lender -> userService.findById(borrowerId)
                        .map(borrower -> new ResponseEntity<>(
                                debtService.findDebtsBetweenUsers(lender, borrower),
                                HttpStatus.OK)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/total-by-borrower/{borrowerId}")
    public ResponseEntity<Float> getTotalDebtForBorrower(@PathVariable Long borrowerId) {
        return userService.findById(borrowerId)
                .map(borrower -> new ResponseEntity<>(
                        debtService.calculateTotalDebtForBorrower(borrower),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/summary-by-plan")
    public ResponseEntity<Map<User, Float>> getDebtSummaryForPlan(@RequestBody Map<String, Long> payload) {
        Long planId = payload.get("planId");
        return planService.findById(planId)
                .map(plan -> new ResponseEntity<>(
                        debtService.calculateDebtSummaryForPlan(plan),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/optimize-by-plan")
    public ResponseEntity<Map<User, Map<User, Float>>> getOptimizedDebts(@RequestBody Map<String, Long> payload) {
        Long planId = payload.get("planId");
        return planService.findById(planId)
                .map(plan -> new ResponseEntity<>(
                        debtCalculationService.optimizeDebts(plan),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
