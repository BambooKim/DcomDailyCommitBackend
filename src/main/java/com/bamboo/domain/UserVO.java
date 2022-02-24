package com.bamboo.domain;

import lombok.Data;

import java.util.Date;

@Data
public class UserVO {
    // 보여줄 내용 전달 시 사용하는 도메인 객체

    private String id;
    private String name;
    private int paidFine;
    private Date startedAt;
    private int unpaidFine;             // (참여일 - 커밋일) * 500 - paidFine. service
    private int commitsInARow;
    private int totalCommits;
    private int elapsedDay;
    private int rankPower;              // commitsInARow * 10 + 커밋일 * 5 + totalCommits - (unpaidFine / 50)
    private int rank;                   // sort. response 직전 부여. service
    private String userImg;
    private int commitDayCount;
    private int isCommitToday;
}
