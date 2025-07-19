package distributed.systems.sd_cli_java.model.dto.debt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DebtDTO {

    private Long id;
    private Float amount;
    private Long expenseId;
    private Long lenderId;
    private Long borrowerId;

}