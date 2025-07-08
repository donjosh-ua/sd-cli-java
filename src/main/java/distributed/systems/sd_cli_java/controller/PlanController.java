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

import distributed.systems.sd_cli_java.model.dto.PlanDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/plans")
@RequiredArgsConstructor
public class PlanController {
    
    @GetMapping
    public ResponseEntity<?> getAllPlans() {
        List<Plan> plans = planService.findAllPlans();
        return new ResponseEntity<>(plans, HttpStatus.OK);
    }

    @GetMapping("/{planId}")
    public ResponseEntity<?> getPlanById(@PathVariable Long planId) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createPlan(@RequestBody PlanDTO plan) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PutMapping("/update")
    public ResponseEntity<?> updatePlan(@RequestBody PlanDTO plan) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PostMapping("/delete")
    public ResponseEntity<?> deletePlan(@RequestBody Map<String, Long> payload) {
        Long id = payload.get("id");
        if (planService.findById(id).isPresent()) {
            planService.deletePlan(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
