package distributed.systems.sd_cli_java.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseCreatedNotificationDTO {

    private Long id;
    private String name;
    private Float amount;
    private LocalDateTime date;
    private String type;
    private Long planId;
}