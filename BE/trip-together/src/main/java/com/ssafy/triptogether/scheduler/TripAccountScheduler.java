package com.ssafy.triptogether.scheduler;

import com.ssafy.triptogether.tripaccount.service.TripAccountSaveService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TripAccountScheduler {
    // Service
    private final TripAccountSaveService tripAccountSaveService;

    @Scheduled(cron = "0 0 5 * * *")
    public void currencyRateUpdate() {
        tripAccountSaveService.currencyRateUpdate();
    }
}
