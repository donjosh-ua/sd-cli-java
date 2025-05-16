package distributed.systems.sd_cli_java;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class SdCliJavaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SdCliJavaApplication.class, args);
	}

	@GetMapping("/")
	public String home() {
		return "Si no saco 10, me suicido";
	}

}
