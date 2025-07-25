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
    private UserDTO owner;
    private LocalDateTime date;
    private Boolean status;
    private String description;
    private String category;
    private Float totalAmount;

    @Builder.Default
    private List<UserDTO> participants = new ArrayList<>();

    @Builder.Default
    private List<ExpenseDTO> expenses = new ArrayList<>();

}