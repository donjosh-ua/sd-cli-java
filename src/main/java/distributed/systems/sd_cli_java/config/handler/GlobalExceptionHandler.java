package distributed.systems.sd_cli_java.config.handler;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import distributed.systems.sd_cli_java.model.dto.common.ResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ResponseDTO> handleException(Exception e) {
        ResponseDTO response = ResponseDTO.builder()
                .success(false)
                .data(Map.of("error", e.getMessage()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ResponseDTO> handleIllegalArgumentException(IllegalArgumentException e) {
        ResponseDTO response = ResponseDTO.builder()
                .success(false)
                .data(Map.of("warn", e.getMessage()))
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

}
