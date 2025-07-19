package distributed.systems.sd_cli_java.model.dto.user;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserSearchResponseDTO {

    private List<UserSearchItemDTO> users;
    private long total;
    private int limit;
    private int offset;
}
