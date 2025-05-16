package distributed.systems.sd_cli_java.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRegistrationDTO {

    private Long planId;
    private String username;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ExpenseData {
        private Long id;
        private String name;
        private Float amount;
        private LocalDateTime date;

        @JsonProperty("plan_id")
        private Long planId;

        private String type;
    }

    private ExpenseData expense;
}