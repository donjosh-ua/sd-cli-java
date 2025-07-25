package distributed.systems.sd_cli_java.model.dto.plan;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import distributed.systems.sd_cli_java.model.dto.expense.ExpenseDTO;
import distributed.systems.sd_cli_java.model.dto.user.UserDTO;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PlanDTO {

    private Long id;
    private String name;
    private LocalDateTime date;
    private Boolean status;

    @Builder.Default
    private List<UserDTO> users = new ArrayList<>();

    @Builder.Default
    private List<ExpenseDTO> expenses = new ArrayList<>();

}