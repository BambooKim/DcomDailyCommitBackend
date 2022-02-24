package com.bamboo.persistence;

import lombok.Setter;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:web/WEB-INF/applicationContext.xml")
public class MyBatisTest {

    @Setter(onMethod_ = { @Autowired})
    private SqlSessionFactory sqlFactory;

    @Test
    public void testFactory(){
        System.out.println("\n >>>>>>>>>> sqlFactory 출력 : "+sqlFactory);
    }

    @Test
    public void testSession() throws Exception{

        try(SqlSession session = sqlFactory.openSession()){

            System.out.println(" >>>>>>>>>> session 출력 : "+session+"\n");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}