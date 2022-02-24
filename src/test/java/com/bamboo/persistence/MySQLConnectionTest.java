package com.bamboo.persistence;

import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.sql.DataSource;
import java.sql.Connection;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:web/WEB-INF/applicationContext.xml")
@Log4j2
public class MySQLConnectionTest {

    @Setter(onMethod_ = { @Autowired })
    private DataSource ds;

    @Test
    public void testConnection() throws Exception {

        try (Connection con = ds.getConnection()) {

            System.out.println("\n >>>>>>>>>> Connection 출력 : " + con + "\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}