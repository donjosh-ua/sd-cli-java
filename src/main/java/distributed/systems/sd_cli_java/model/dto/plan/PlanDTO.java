package distributed.systems.sd_cli_java.model.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {

    private Long planId;
    private String code;
    private String name;
    private String owner;
    private LocalDateTime date;
    private Boolean status;
    private String description;
    private String category;
    private Float totalAmount;

}