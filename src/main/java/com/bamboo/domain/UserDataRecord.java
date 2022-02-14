package com.bamboo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class UserDataRecord {
    // 크롤링 때 사용하는 도메인 객체

    private String id;
    private String name;
    private int paidFine;
    private Date startedAt;
    private int unpaidFine;             // (참여일 - 커밋일) * 500 - paidFine. service
    private int commitsInARow;
    private int totalCommits;
    private String participationRate;   // 커밋일 /  참여일. service
    private int rankPower;              // commitsInARow * 10 + 커밋일 * 5 + totalCommits - (unpaidFine / 50)
    private String userImg;
    private Date lastUpdate;
    private int commitDayCount;
}
