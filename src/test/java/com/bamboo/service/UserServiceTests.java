package com.bamboo.service;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

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
}
