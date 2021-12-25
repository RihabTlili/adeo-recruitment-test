package adeo.leroymerlin.cdp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * Spring boot Application class
 */
@SpringBootApplication
@EnableTransactionManagement
public class AdeoLeroyMerlinCDPRecruitmentApplication {

	/**
	 * Main function to launch Spring Boot Application
	 * @param args command line args
	 */
	public static void main(String[] args) {
		SpringApplication.run(AdeoLeroyMerlinCDPRecruitmentApplication.class, args);
	}
}
