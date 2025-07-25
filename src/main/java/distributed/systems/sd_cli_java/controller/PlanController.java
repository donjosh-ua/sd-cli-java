package distributed.systems.sd_cli_java.controller;

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
        throw new IllegalArgumentException("Not implemented yet");
    }

    @GetMapping("/expenses/{planId}")
    public ResponseEntity<?> getExpenses(@PathVariable Long planId) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @GetMapping("/{planId}")
    public ResponseEntity<?> getPlanById(@PathVariable Long planId) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getPlansByUserId(@PathVariable Long userId) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PostMapping
    public ResponseEntity<?> createPlan(@RequestBody PlanDTO plan) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PutMapping
    public ResponseEntity<?> updatePlan(@RequestBody PlanDTO plan) {
        throw new IllegalArgumentException("Not implemented yet");
    }

}
