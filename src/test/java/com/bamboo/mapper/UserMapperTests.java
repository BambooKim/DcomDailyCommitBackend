package com.bamboo.mapper;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserDataRecord;
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
public class UserMapperTests {

    @Setter(onMethod_ = @Autowired)
    private UserMapper mapper;

    @Test
    public void testInsertUser() {
        UserDTO user = new UserDTO();
        user.setGithubId("testId");
        user.setKorName("시험용");

        mapper.insertUser(user);

        log.info(user);
    }

    @Test
    public void testSetFalseIsCommitToday() {
        UserDataRecord user = new UserDataRecord();
        user.setId("codeisneverodd");

        mapper.setFalseIsCommitToday(user);
    }
}
