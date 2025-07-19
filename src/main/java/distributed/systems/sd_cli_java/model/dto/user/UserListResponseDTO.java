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
public class UserListResponseDTO {

    private List<UserResponseDTO> users;
    private long total;
    private int limit;
    private int offset;
}
