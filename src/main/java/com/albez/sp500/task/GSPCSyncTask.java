package com.albez.sp500.task;

import com.albez.sp500.service.SP500Service;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
@Component
@RequiredArgsConstructor
public class GSPCSyncTask {
    private final SP500Service sp500Service;
    private final AtomicBoolean running = new AtomicBoolean();

    @EventListener(ApplicationReadyEvent.class)
    @Scheduled(cron = "${finance.sync.cron:0 0 8 * * *}", zone = "Asia/Shanghai")
    public void syncGSPC() {
        if (!running.compareAndSet(false, true)) {
            log.info("GSPC sync is already running, skipping this execution");
            return;
        }
        try {
            int inserted = sp500Service.syncGSPC();
            log.info("GSPC sync completed, inserted {} records", inserted);
        } catch (Exception exception) {
            log.error("GSPC sync failed", exception);
        } finally {
            running.set(false);
        }
    }
}
