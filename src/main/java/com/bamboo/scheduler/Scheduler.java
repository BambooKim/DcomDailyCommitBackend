package com.bamboo.scheduler;

import com.bamboo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Calendar;

@Component
@Log4j2
@AllArgsConstructor
public class Scheduler {

    UserService service;

    @Scheduled(fixedDelay = 300000)
    public void updateEveryFiveMinutes() {
        service.updateDB();

        log.debug("**** Scheduled Method updateEveryFiveMinutes() called ****");
        log.debug(Calendar.getInstance().getTime());
    }
}
