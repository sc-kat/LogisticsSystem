package com.example.project.support;
import lombok.Data;
import org.springframework.stereotype.Component;
import java.time.LocalDate;

@Component
@Data
public class CurrentDateAndProfitService {
    LocalDate currentDate;
    Integer profit;

    public CurrentDateAndProfitService() {
        this.currentDate = LocalDate.of(2021,12,14);
        this.profit = 0;
    }

    public void incrementDate() {
        this.currentDate = this.currentDate.plusDays(1);
    }

    public synchronized void addProfit(Integer profitPerDay) {
        this.profit += profitPerDay;
    }

}
