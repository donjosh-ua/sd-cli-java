package distributed.systems.sd_cli_java.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distributed.systems.sd_cli_java.model.entity.Expense;
import distributed.systems.sd_cli_java.model.entity.Plan;
import distributed.systems.sd_cli_java.model.entity.User;
import distributed.systems.sd_cli_java.service.PlanService;
import distributed.systems.sd_cli_java.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<Plan> createPlan(@RequestBody Plan plan) {
        Plan createdPlan = planService.createPlan(plan);
        return new ResponseEntity<>(createdPlan, HttpStatus.CREATED);
    }

    @PutMapping("/update")
    public ResponseEntity<Plan> updatePlan(@RequestBody Plan plan) {
        return planService.findById(plan.getPlanId())
                .map(existingPlan -> {
                    return new ResponseEntity<>(planService.updatePlan(plan), HttpStatus.OK);
                })
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Plan> getPlanById(@PathVariable Long id) {
        return planService.findById(id)
                .map(plan -> new ResponseEntity<>(plan, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping
    public ResponseEntity<List<Plan>> getAllPlans() {
        List<Plan> plans = planService.findAllPlans();
        return new ResponseEntity<>(plans, HttpStatus.OK);
    }

    @PostMapping("/delete")
    public ResponseEntity<Void> deletePlan(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");
        if (planService.findById(id).isPresent()) {
            planService.deletePlan(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping("/add-user")
    public ResponseEntity<Plan> addUserToPlan(@RequestBody Map<String, Long> payload) {
        Long planId = payload.get("planId");
        Long userId = payload.get("userId");
        return planService.findById(planId)
                .flatMap(plan -> userService.findById(userId)
                        .map(user -> new ResponseEntity<>(planService.addUserToPlan(plan, user), HttpStatus.OK)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/remove-user")
    public ResponseEntity<Plan> removeUserFromPlan(@RequestBody Map<String, Long> payload) {
        Long planId = payload.get("planId");
        Long userId = payload.get("userId");
        return planService.findById(planId)
                .flatMap(plan -> userService.findById(userId)
                        .map(user -> new ResponseEntity<>(planService.removeUserFromPlan(plan, user), HttpStatus.OK)))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/add-expense")
    public ResponseEntity<Plan> addExpenseToPlan(@RequestBody Map<String, Object> payload) {
        Long planId = ((Number) payload.get("planId")).longValue();
        Expense expense = new Expense();
        expense.setName((String) payload.get("name"));
        expense.setAmount(((Number) payload.get("amount")).floatValue());

        @SuppressWarnings("unchecked")
        Map<String, Object> userMap = (Map<String, Object>) payload.get("user");
        User user = new User();
        user.setUserId(((Number) userMap.get("id")).longValue());
        expense.setUser(user);

        return planService.findById(planId)
                .map(plan -> new ResponseEntity<>(planService.addExpenseToPlan(plan, expense), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Plan>> getPlansByUser(@PathVariable Long userId) {
        return userService.findById(userId)
                .map(user -> new ResponseEntity<>(planService.findPlansByUser(user), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/by-name/{name}")
    public ResponseEntity<Plan> getPlanByName(@PathVariable String name) {
        return planService.findByName(name)
                .map(plan -> new ResponseEntity<>(plan, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}
