package distributed.systems.sd_cli_java.model.dto.common;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDTO {

    private boolean success;
    private Map<String, Object> data;

}
