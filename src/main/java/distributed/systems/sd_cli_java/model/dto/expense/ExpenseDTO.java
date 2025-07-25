package distributed.systems.sd_cli_java.model.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseDTO {

    private Long expenseId;
    private String name;
    private Float amount;
    private LocalDateTime date;
    private String type;
    private Long planId;

}