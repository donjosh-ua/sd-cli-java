package distributed.systems.sd_cli_java.model.dto.expense;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseRequestDTO {

    private String name;
    private Float amount;
    private String type;
    private Long planId;
    private String icon;
    private List<String> participants;

}
