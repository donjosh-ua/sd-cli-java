package distributed.systems.sd_cli_java.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distributed.systems.sd_cli_java.model.dto.plan.PlanDTO;
import distributed.systems.sd_cli_java.service.PlanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {

    private final PlanService planService;

    @GetMapping
    public ResponseEntity<?> getAllPlans() {
        return new ResponseEntity<>(planService.findAllPlans(), HttpStatus.OK);
    }

    @GetMapping("/participants/{planId}")
    public ResponseEntity<?> getParticipants(@PathVariable Long planId) {
        return new ResponseEntity<>(planService.findParticipantsByPlanId(planId), HttpStatus.OK);
    }

    @GetMapping("/expenses/{planId}")
    public ResponseEntity<?> getExpenses(@PathVariable Long planId) {
        return new ResponseEntity<>(planService.findExpensesByPlanId(planId), HttpStatus.OK);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<?> getPlanById(@PathVariable Long planId) {
        return new ResponseEntity<>(planService.findById(planId), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPlansByParticipantId(@PathVariable String userId) {
        return new ResponseEntity<>(planService.findPlansByParticipantId(userId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<?> createPlan(@RequestBody PlanDTO plan) {
        return new ResponseEntity<>(planService.createPlan(plan), HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<?> updatePlan(@RequestBody PlanDTO plan) {
        return new ResponseEntity<>(planService.updatePlan(plan), HttpStatus.OK);
    }

}
