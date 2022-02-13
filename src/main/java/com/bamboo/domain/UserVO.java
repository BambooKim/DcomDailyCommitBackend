package com.bamboo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
public class UserVO {
    // db : refresh-user 말고 response시 크롤링 필요없이 바로 전달해야 할 것들 (계산은 가능한것 제외)
    private String id;                  // db
    private String name;                // db
    private int paidFine;               // db
    private Date startedAt;             // db
    private int unpaidFine;             // (참여일 - 커밋일) * 500 - paidFine. service
    private int commitsInARow;          // db
    private int totalCommits;           // db
    private String participationRate;   // 참여일 / (어제날짜 - startedAt). service
    private int rankPower;              // commitsInARow * 10 + 커밋일 * 5 + totalCommits - (unpaidFine / 50)
    private int rank;                   // sort. response 직전 부여. service
    private String userImg;             // db
    private Date lastUpdate;            // db
}
