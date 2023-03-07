package com.example.project;
import com.example.project.support.UpdateDbFromCsvFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;



@SpringBootApplication
public class LogisticsSystemApplication {

	public static void main(String[] args) {
		ApplicationContext run = SpringApplication.run(LogisticsSystemApplication.class, args);
		UpdateDbFromCsvFile updateDbFromCsvFile = run.getBean(UpdateDbFromCsvFile.class);

		updateDbFromCsvFile.uploadCsvFiles();


	}

}
