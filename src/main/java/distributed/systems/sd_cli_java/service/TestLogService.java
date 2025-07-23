package distributed.systems.sd_cli_java.service;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class TestLogService {

    @Scheduled(fixedRate = 5000)
    public void logMessage() {
        log.info("This is a test log message from TestLogService");
    }

}