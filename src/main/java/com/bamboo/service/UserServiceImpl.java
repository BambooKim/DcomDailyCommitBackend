package com.bamboo.service;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserVO;
import com.bamboo.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Log4j2
@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserMapper mapper;

    @Override
    public void registerUser(UserDTO user) {
        log.info("registerUser....." + user);

        mapper.insertUser(user);
    }

    @Override
    public List<UserVO> getUserList() {
        log.info("getUserList.....");

        return mapper.getUserList();
    }

    @Override
    public void updateDB() {
        /*
        ArrayList<UserVO> list = (ArrayList<UserVO>) mapper.getUserList();

        list.forEach(elem -> {




        });

         */


        String githubId = "codeisneverodd"; //elem.getId();


        try {
            String url = "https://github.com/" + githubId;
            Connection connection = Jsoup.connect(url);

            Document document = connection.get();
            Elements avatarElem = document.select("div.js-profile-editable-replace img").first().getElementsByAttribute("src"); //("avatar avatar-user width-full border color-bg-default");
            String avatarUrl = avatarElem.get(0).attr("src");

            log.info(avatarUrl);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startDateString = "2022-02-01";  // 나중에 스터디 시작일로 바꿔야 함.
            Date startDate = simpleDateFormat.parse(startDateString);

            Elements rects = document.select("div.js-calendar-graph rect");

            rects.forEach(tags -> {
                String data_date = tags.attr("data-date");

                if (!data_date.isEmpty()) {
                    try {
                        Date rectDate = simpleDateFormat.parse(data_date);

                        // 스터디 시작일과 각 잔디의 날짜를 비교함. 시작일 이후의 잔디들만 선택.
                        if (rectDate.getTime() >= startDate.getTime()) {
                            int commitCountofDay = Integer.parseInt(tags.attr("data-count"));

                            log.info(data_date + " " + commitCountofDay);
                        }
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
            });

            // List<String> dateList = rects.eachAttr("data-date");

            //log.info(dateList);
        } catch (Exception e) {
            log.error(e);
        }
    }
}
