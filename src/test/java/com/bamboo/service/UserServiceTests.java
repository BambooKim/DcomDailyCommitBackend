package com.bamboo.service;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:web/WEB-INF/applicationContext.xml")
@Log4j2
public class UserServiceTests {

    @Setter(onMethod_ = @Autowired)
    private UserService service;

    @Test
    public void testUpdateDb() {
        service.updateDB();
    }

    @Test
    public void testCalendar() {
        try {
            SimpleDateFormat sdformat = new
                    SimpleDateFormat("yyyy-MM-dd");
            Date date1 = sdformat.parse("2022-02-23");
            Date date2 = sdformat.parse("2022-02-25");

            int _date1 = (int) (date1.getTime() / (1000 * 24 * 60 * 60));
            int _date2 = (int) (date2.getTime() / (1000 * 24 * 60 * 60));

            log.info(_date2 - _date1);

            Date asdf = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA).getTime();
            log.info(asdf);
        } catch (ParseException ex) {

        }
    }
}
