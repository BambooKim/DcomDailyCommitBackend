package com.bamboo.service;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserDataRecord;
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
import java.util.*;

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
    public List<UserVO> getUserListforResponse() {
        log.info("getUserListforUpdate.....");

        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        List<UserVO> list = mapper.getUserDataforResponse();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i+1);

            long elapsedTimeSec = (yesterday.getTime() - list.get(i).getStartedAt().getTime()) / 1000;
            int elapsedTimeDay = (int) (elapsedTimeSec / (24 * 60 * 60));
            
            list.get(i).setUnpaidFine((elapsedTimeDay - list.get(i).getCommitDayCount()) * 500 - list.get(i).getPaidFine());
            
            list.get(i).setParticipationRate(list.get(i).getCommitDayCount() + " / " + elapsedTimeDay);
        }

        return list;
    }

    @Override
    public void updateDB() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = new GregorianCalendar();
        Date today = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        List<UserDataRecord> list = mapper.getUserDataforUpdate();

        list.forEach(elem -> {
            String githubId = elem.getId();
            Date startedAt = elem.getStartedAt();
            Date lastUpdate = elem.getLastUpdate();

            try {
                // Github 프로필 이미지 링크 크롤링
                String url = "https://github.com/" + githubId;
                Connection connection = Jsoup.connect(url);

                Document document = connection.get();
                Elements avatarElem = document.select("div.js-profile-editable-replace img").first()
                        .getElementsByAttribute("src");     // may producee nullpointerexception.
                String avatarUrl = avatarElem.get(0).attr("src");

                log.info("\n\n");
                log.info(githubId);
                log.info(avatarUrl);

                // 오늘 날짜, 스터디 시작 날짜, 마지막 디비 업데이트 날짜 비교
                Date crawlingStart;
                if (lastUpdate == null) {
                    // 등록 후 한번도 반영하지 않았다면... 크롤링 시작은 스터디 시작일!
                    crawlingStart = startedAt;
                } else {
                    // 한번이라도 반영한 적 있다면... 크롤링 시작은 마지막 업데이트 날짜일!
                    crawlingStart = lastUpdate;
                }

                calendar.setTime(crawlingStart);
                calendar.add(Calendar.DATE, -1);

                Elements rects = document.select("div.js-calendar-graph rect");

                rects.forEach(tags -> {
                    String data_date = tags.attr("data-date");

                    if (!data_date.isEmpty()) {
                        try {
                            Date rectDate = simpleDateFormat.parse(data_date);

                            // 스터디 시작일과 각 잔디의 날짜를 비교함. 시작일 이후의 어제까지의 잔디들만 선택.
                            if (calendar.getTime().getTime() <= rectDate.getTime()
                                    && rectDate.getTime() <= yesterday.getTime()) {
                                int commitCountofDay = Integer.parseInt(tags.attr("data-count"));
                                elem.setTotalCommits(elem.getTotalCommits() + commitCountofDay);

                                if (commitCountofDay != 0) {
                                    // 어떤 날에 커밋을 1개 이상 했다면...

                                    elem.setCommitDayCount(elem.getCommitDayCount() + 1);

                                    elem.setCommitsInARow(elem.getCommitsInARow() + 1);
                                } else {
                                    // 어떤 날에 커밋이 아예 없다면...

                                    elem.setCommitsInARow(0);
                                }

                                log.info(data_date + " " + commitCountofDay);
                            }
                        } catch (Exception e) {
                            log.error(e);
                        }
                    }
                });

                elem.setRankPower(elem.getCommitsInARow() * 10 + elem.getCommitDayCount() * 5
                                    + elem.getTotalCommits() - (elem.getUnpaidFine() / 50));
                elem.setUserImg(avatarUrl);
                elem.setLastUpdate(today);

                // 이후 업데이트 sql 매퍼 호출.
                mapper.updateUserData(elem);
            } catch (Exception e) {
                log.error(e);
            }
        });
    }

    @Override
    public void deleteUser(UserDTO user) {
        log.info("Delete User....." + user);

        mapper.deleteUser(user);
    }

    @Override
    public UserVO initAndGetUser(UserDTO user) {
        log.info("initAndGetUser....." + user);

        UserVO initialUser = new UserVO();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");

        Calendar calendar = new GregorianCalendar();
        Date today = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        UserDataRecord record = mapper.getUser(user); // 레코드 하나만 디비에서 불러옴 = mapper.getUserDataforUpdate();

        String githubId = record.getId();
        Date startedAt = record.getStartedAt();
        Date lastUpdate = record.getLastUpdate();

        try {
            // Github 프로필 이미지 링크 크롤링
            String url = "https://github.com/" + githubId;
            Connection connection = Jsoup.connect(url);

            Document document = connection.get();
            Elements avatarElem = document.select("div.js-profile-editable-replace img").first()
                    .getElementsByAttribute("src");     // may producee nullpointerexception.
            String avatarUrl = avatarElem.get(0).attr("src");

            log.info("\n\n");
            log.info(githubId);
            log.info(avatarUrl);

            // 오늘 날짜, 스터디 시작 날짜, 마지막 디비 업데이트 날짜 비교
            Date crawlingStart;
            if (lastUpdate == null) {
                // 등록 후 한번도 반영하지 않았다면... 크롤링 시작은 스터디 시작일!
                crawlingStart = startedAt;
            } else {
                // 한번이라도 반영한 적 있다면... 크롤링 시작은 마지막 업데이트 날짜일!
                crawlingStart = lastUpdate;
            }

            calendar.setTime(crawlingStart);
            calendar.add(Calendar.DATE, -1);

            Elements rects = document.select("div.js-calendar-graph rect");

            rects.forEach(tags -> {
                String data_date = tags.attr("data-date");

                if (!data_date.isEmpty()) {
                    try {
                        Date rectDate = simpleDateFormat.parse(data_date);

                        // 스터디 시작일과 각 잔디의 날짜를 비교함. 시작일 이후의 어제까지의 잔디들만 선택.
                        if (calendar.getTime().getTime() <= rectDate.getTime()
                                && rectDate.getTime() <= yesterday.getTime()) {
                            int commitCountofDay = Integer.parseInt(tags.attr("data-count"));
                            record.setTotalCommits(record.getTotalCommits() + commitCountofDay);

                            if (commitCountofDay != 0) {
                                // 어떤 날에 커밋을 1개 이상 했다면...

                                record.setCommitDayCount(record.getCommitDayCount() + 1);

                                record.setCommitsInARow(record.getCommitsInARow() + 1);
                            } else {
                                // 어떤 날에 커밋이 아예 없다면...

                                record.setCommitsInARow(0);
                            }

                            log.info(data_date + " " + commitCountofDay);
                        }
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
            });

            record.setRankPower(record.getCommitsInARow() * 10 + record.getCommitDayCount() * 5
                    + record.getTotalCommits() - (record.getUnpaidFine() / 50));
            record.setUserImg(avatarUrl);
            record.setLastUpdate(today);

            // 이후 업데이트 sql 매퍼 호출.
            mapper.updateUserData(record);
        } catch (Exception e) {
            log.error(e);
        }

        initialUser.setId(record.getId());
        initialUser.setName(record.getName());
        initialUser.setPaidFine(0);
        initialUser.setStartedAt(record.getStartedAt());
        initialUser.setUnpaidFine(record.getUnpaidFine());
        initialUser.setCommitsInARow(record.getCommitsInARow());
        initialUser.setTotalCommits(record.getTotalCommits());
        initialUser.setParticipationRate(record.getParticipationRate());
        initialUser.setRankPower(record.getRankPower());
        initialUser.setRank(-1);
        initialUser.setUserImg(record.getUserImg());
        initialUser.setCommitDayCount(record.getCommitDayCount());

        return initialUser;
    }
}
