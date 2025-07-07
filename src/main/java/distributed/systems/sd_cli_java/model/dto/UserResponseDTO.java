package distributed.systems.sd_cli_java.model.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserResponseDTO {

    private String email;
    private String nickname;
    private String photoUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
