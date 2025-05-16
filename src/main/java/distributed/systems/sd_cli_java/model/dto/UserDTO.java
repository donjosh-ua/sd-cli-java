package distributed.systems.sd_cli_java.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long id;
    private String username;

    @Builder.Default
    private List<Long> expenseIds = new ArrayList<>();

    @Builder.Default
    private List<Long> debtIds = new ArrayList<>();

    @Builder.Default
    private List<Long> loanIds = new ArrayList<>();

    @Builder.Default
    private List<Long> planIds = new ArrayList<>();

}