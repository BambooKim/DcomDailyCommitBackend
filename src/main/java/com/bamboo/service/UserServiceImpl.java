package com.bamboo.service;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserDataRecord;
import com.bamboo.domain.UserVO;
import com.bamboo.exception.*;
import com.bamboo.mapper.UserMapper;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Log4j2
@Service
//@AllArgsConstructor
@PropertySource("classpath:server.properties")
public class UserServiceImpl implements UserService {

    @Autowired
    ApplicationContext ctx;

    private UserMapper mapper;

    public UserServiceImpl(UserMapper mapper) {
        //TimeZone.setDefault(TimeZone.getTimeZone("KST"));

        this.mapper = mapper;
    }

    @Override
    public void registerUser(UserDTO user) throws NoGithubIdFoundException, WrongAccessCodeException, UserAlreadyExistsException, BlankArgumentException {
        log.info("registerUser....." + user);

        // 0. request body의 각 field가 비어있는지 검사.
        if (user.getGithubId().equals("") || user.getKorName() == null
                || user.getAccessCode() == null || user.getUserCode() == null) {
            throw new BlankArgumentException("Blank argument");
        }

        // 1. AccessCode의 유효성 검사.
        String inputAccessCode = user.getAccessCode();
        if (!inputAccessCode.equals(ctx.getEnvironment().getProperty("ACCESS_CODE"))) {
            throw new WrongAccessCodeException("Wrong Access Code");
        }

        // 2. githubId가 Github에 존재하는지 확인한다.
        int statusCode = 0;
        try {
            URL url = new URL("https://api.github.com/users/" + user.getGithubId());
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            statusCode = connection.getResponseCode();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        log.debug("Code: " + statusCode);
        if (statusCode == 404 || statusCode == 400 || statusCode == 406) {
            throw new NoGithubIdFoundException("No ID found");
        }

        // 3. 이미 디비에 아이디가 존재하는 경우.
        UserDTO checkUserObject = mapper.getUserForCheck(user);
        if (checkUserObject != null) {
            throw new UserAlreadyExistsException("User already exists");
        }

        // 4. 예외가 없다면 디비에 저장.
        mapper.insertUser(user);
    }

    @Override
    public List<UserVO> getUserListforResponse() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        log.info("getUserListforUpdate.....");

        Calendar yesterDayCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        yesterDayCal.add(Calendar.DATE, -1);

        List<UserVO> list = mapper.getUserDataforResponse();
        for (int i = 0; i < list.size(); i++) {
            list.get(i).setRank(i+1);

            try {
                // manipulate yesterday
                int yesterdayYear = yesterDayCal.get(Calendar.YEAR);
                int yesterdayMonth = yesterDayCal.get(Calendar.MONTH);
                int yesterdayDate = yesterDayCal.get(Calendar.DATE);

                Date yesterDayInDate = simpleDateFormat.parse(yesterdayYear + "-" + yesterdayMonth + "-" + yesterdayDate);

                // manipulate startday
                Calendar startCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                startCal.setTime(list.get(i).getStartedAt());

                int startYear = startCal.get(Calendar.YEAR);
                int startMonth = startCal.get(Calendar.MONTH);
                int startDate = startCal.get(Calendar.DATE);

                Date startDayInDate = simpleDateFormat.parse(startYear + "-" + startMonth + "-" + startDate);

                log.debug("yesterday: " + yesterDayInDate);
                log.debug("startday: " + startDayInDate);

                // subtract dates
                int elapsedTimeDay = (int) Math.round(((yesterDayInDate.getTime() - startDayInDate.getTime()) / (1000.0 * 24.0 * 60.0 * 60.0))) + 1;

                log.debug("elapsed: " + elapsedTimeDay);

                list.get(i).setUnpaidFine((elapsedTimeDay - list.get(i).getCommitDayCount()) * 500 - list.get(i).getPaidFine());
                list.get(i).setElapsedDay(elapsedTimeDay + 1);
            } catch (ParseException e) {

            }
        }

        return list;
    }

    @Override
    public void updateDB() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        Date today = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        final Date yesterday = calendar.getTime();

        // DB의 각 레코드들이 리스트 형태로 반환된다.
        List<UserDataRecord> list = mapper.getUserDataforUpdate();

        // 리스트의 각 레코드들, 즉 각 유저들마다 반복하여 크롤링한다.
        list.forEach(elem -> {
            String githubId = elem.getId();
            Date startedAt = elem.getStartedAt();
            Date lastUpdate = elem.getLastUpdate();

            try {
                Calendar lastCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
                lastCal.setTime(elem.getLastUpdate());

                Calendar todayCal = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);

                log.info("================");
                log.info(lastCal.get(Calendar.DATE));
                log.info(todayCal.get(Calendar.DATE));

                if (lastCal.get(Calendar.DATE) != todayCal.get(Calendar.DATE)) {
                    log.info("inside if state =====================");

                    //mapper.setFalseIsCommitToday(elem);
                    elem.setIsCommitToday(0);
                }

                // Github 프로필 이미지 링크 크롤링
                String url = "https://github.com/" + githubId;
                Connection connection = getJsoupConnection(url);

                Document document = connection.get();
                Elements avatarElem = document.select("div.js-profile-editable-replace img").first()
                        .getElementsByAttribute("src");     // may producee nullpointerexception.
                String avatarUrl = avatarElem.get(0).attr("src");

                log.debug("Github Id: " + githubId);
                log.debug(avatarUrl);

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

/*                            if (githubId.equals("bambookim")) {

                            }*/
                            //log.debug("tag: " + tags);

                            // 스터디 시작일과 각 잔디의 날짜를 비교함. 시작일 이후의 어제까지의 잔디들만 선택.
                            if (calendar.getTime().getTime() <= rectDate.getTime()
                                    && rectDate.getTime() <= today.getTime()) {
                                int commitCountofDay = Integer.parseInt(tags.attr("data-count"));
                                elem.setTotalCommits(elem.getTotalCommits() + commitCountofDay);

                                log.debug(rectDate + " " + commitCountofDay);

                                if (commitCountofDay != 0) {
                                    // 어떤 날에 커밋을 1개 이상 했다면...

                                    elem.setCommitDayCount(elem.getCommitDayCount() + 1);

                                    elem.setCommitsInARow(elem.getCommitsInARow() + 1);
                                } else {
                                    // 어떤 날에 커밋이 아예 없다면...

                                    elem.setCommitsInARow(0);
                                }

                                log.debug(data_date + " " + commitCountofDay);
                            }

                            if (rectDate.after(today)) {
                                int commitCountofDay = Integer.parseInt(tags.attr("data-count"));

                                log.debug("&&&& Into the else-if state &&&&");
                                log.debug(rectDate + " - " + commitCountofDay);

                                if (commitCountofDay > 0) {
                                    elem.setIsCommitToday(1);
                                }
                            }
                        } catch (Exception e) {
                            log.error(e);
                        }
                    }
                });

                elem.setRankPower(elem.getCommitsInARow() * 10 + elem.getCommitDayCount() * 5
                                    + elem.getTotalCommits() - (elem.getUnpaidFine() / 50));
                elem.setUserImg(avatarUrl);

                // 이후 업데이트 sql 매퍼 호출.
                mapper.updateUserData(elem);
            } catch (Exception e) {
                log.error(e);
            }
        });
    }

    @Override
    public void deleteUser(UserDTO user) throws NoUserExistsException, WrongUserCodeException, BlankArgumentException {
        log.info("Delete User....." + user);

        UserDTO savedUserIdAndCode = mapper.getUserForCheck(user);
        log.debug(savedUserIdAndCode);

        // 0. request body의 각 field가 비어있는지 검사.
        if (user.getGithubId().equals("") || user.getUserCode() == null) {
            throw new BlankArgumentException("Blank argument");
        }

        // 1. 입력한 user의 github id가 존재하는지 확인한다.
        if (savedUserIdAndCode == null) {
            throw new NoUserExistsException("user not found");
        }
        // 2. 입력한 userCode와 저장된 userCode가 일치하는지 확인한다.
        if (!savedUserIdAndCode.getUserCode().equals(user.getUserCode())) {
            throw new WrongUserCodeException("wrong user code");
        }

        mapper.deleteUser(user);
    }

    @Override
    public UserVO initAndGetUser(UserDTO user) {
        log.info("initAndGetUser....." + user);

        UserVO initialUser = new UserVO();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.KOREA);

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Asia/Seoul"), Locale.KOREA);
        Date today = calendar.getTime();
        calendar.add(Calendar.DATE, -1);
        Date yesterday = calendar.getTime();

        UserDataRecord record = mapper.getUser(user); // 레코드 하나만 디비에서 불러옴

        String githubId = record.getId();
        Date startedAt = record.getStartedAt();
        Date lastUpdate = record.getLastUpdate();

        try {
            // Github 프로필 이미지 링크 크롤링
            String url = "https://github.com/" + githubId;
            Connection connection = getJsoupConnection(url);

            Document document = connection.get();
            Elements avatarElem = document.select("div.js-profile-editable-replace img").first()
                    .getElementsByAttribute("src");     // may producee nullpointerexception.
            String avatarUrl = avatarElem.get(0).attr("src");

            log.debug("Github Id: " + githubId);
            log.debug(avatarUrl);

            calendar.setTime(startedAt);
            calendar.add(Calendar.DATE, -1);

            Elements rects = document.select("div.js-calendar-graph rect");

            rects.forEach(tags -> {
                String data_date = tags.attr("data-date");

                if (!data_date.isEmpty()) {
                    try {
                        //log.debug("tag: " + tags);
                        Date rectDate = simpleDateFormat.parse(data_date);

                        if (rectDate.after(calendar.getTime())) {
                            int commitCountofDay = Integer.parseInt(tags.attr("data-count"));

                            log.debug("rectDate: " + rectDate);
                            log.debug("Initial Day Commit: " + commitCountofDay);

                            if (commitCountofDay > 0) {
                                record.setIsCommitToday(1);
                            }
                        }
                    } catch (Exception e) {
                        log.error(e);
                    }
                }
            });

            record.setRankPower(record.getCommitsInARow() * 10 + record.getCommitDayCount() * 5
                    + record.getTotalCommits() - (record.getUnpaidFine() / 50));
            record.setUserImg(avatarUrl);

            record.setElapsedDay(1);

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
        initialUser.setElapsedDay(record.getElapsedDay());
        initialUser.setRankPower(record.getRankPower());
        initialUser.setRank(-1);
        initialUser.setUserImg(record.getUserImg());
        initialUser.setCommitDayCount(record.getCommitDayCount());
        initialUser.setIsCommitToday(record.getIsCommitToday());

        return initialUser;
    }

    Connection getJsoupConnection(String url) {
        return Jsoup.connect(url)
                .cookie("tz", "Asia%2FSeoul")
                .cookie("logged_in", "yes")
                .cookie("_octo", Objects.requireNonNull(env.getProperty("_octo")))
                .cookie("user_session", Objects.requireNonNull(env.getProperty("userSession")))
                .userAgent("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/98.0.4758.109 Safari/537.36")
                .header("accept-language", "ko-KR,ko;q=0.9,en-US;q=0.8,en;q=0.7")
                .header("acept-encoding", "gzip, deflate, br")
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
    }
}
