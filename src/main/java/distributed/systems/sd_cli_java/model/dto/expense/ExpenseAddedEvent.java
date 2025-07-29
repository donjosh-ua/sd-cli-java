package distributed.systems.sd_cli_java.model.dto.expense;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseAddedEvent {

    private Long planId;
    private ExpenseDTO expense;

}
