package com.example.project;
import com.example.project.exception.DataNotFound;
import com.example.project.support.UpdateDbFromCsvFile;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;


@SpringBootApplication
@EnableAsync
public class LogisticsSystemApplication {

	public static void main(String[] args) throws DataNotFound {
		ApplicationContext run = SpringApplication.run(LogisticsSystemApplication.class, args);
		UpdateDbFromCsvFile updateDbFromCsvFile = run.getBean(UpdateDbFromCsvFile.class);

		updateDbFromCsvFile.uploadCsvFiles();
	}

}
