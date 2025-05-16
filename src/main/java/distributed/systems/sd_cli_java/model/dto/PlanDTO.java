package distributed.systems.sd_cli_java.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {

    private Long id;
    private String name;
    private LocalDateTime date;

    @Builder.Default
    private List<Long> userIds = new ArrayList<>();

    @Builder.Default
    private List<Long> expenseIds = new ArrayList<>();

}