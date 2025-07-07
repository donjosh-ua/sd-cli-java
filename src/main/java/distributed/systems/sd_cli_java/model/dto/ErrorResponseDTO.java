package distributed.systems.sd_cli_java.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponseDTO {

    private String error;
    private String message;
    private int code;
}
