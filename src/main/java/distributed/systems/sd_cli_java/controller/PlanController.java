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

    @GetMapping("/all")
    public ResponseEntity<?> getAllPlans() {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @GetMapping("/search-by-id/{planId}")
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

    @DeleteMapping("/delete")
    public ResponseEntity<?> deletePlan(@RequestBody Map<String, Long> fields) {
        throw new IllegalArgumentException("Not implemented yet");
    }

}
