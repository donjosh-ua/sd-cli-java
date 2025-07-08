package distributed.systems.sd_cli_java.controller;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import distributed.systems.sd_cli_java.model.dto.DebtDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/debts")
public class DebtController {

    @GetMapping("/all")
    public ResponseEntity<?> getAllDebts() {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @GetMapping("/search-by-id/{debtId}")
    public ResponseEntity<?> getDebtById(@PathVariable Long debtId) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PostMapping("/create")
    public ResponseEntity<?> createDebt(@RequestBody DebtDTO debt) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateDebt(@RequestBody DebtDTO debt) {
        throw new IllegalArgumentException("Not implemented yet");
    }

    @DeleteMapping("/delete")
    public ResponseEntity<?> deleteDebt(@RequestBody Map<String, Long> fields) {
        throw new IllegalArgumentException("Not implemented yet");
    }

}
