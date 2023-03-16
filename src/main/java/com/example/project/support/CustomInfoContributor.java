package com.example.project.support;


import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class CustomInfoContributor implements InfoContributor {
    final CurrentDateAndProfitService currentDateAndProfitService;

    public CustomInfoContributor(CurrentDateAndProfitService currentDateAndProfitService) {
        this.currentDateAndProfitService = currentDateAndProfitService;
    }

    @Override
    public void contribute(Info.Builder builder) {
        builder.withDetail("current-date", currentDateAndProfitService.getCurrentDate());  //TODO de verificat daca e ok sa injectej metoda de currentdate si profit\

        builder.withDetail("overall-profit", currentDateAndProfitService.getProfit());
    }
}
