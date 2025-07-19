package distributed.systems.sd_cli_java.model.dto.expense;

import java.time.LocalDateTime;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExpenseNotificationDTO {

    private Long expenseId;
    private String expenseName;
    private Float amount;
    private LocalDateTime date;
    private String type;
    private Long userId;
    private String nickname;
    private Long planId;
    private String planName;
    private Float totalPlanExpense;
    private List<Long> affectedUserIds;

}