package com.bamboo.mapper;

import java.util.Map;

public interface AdminMapper {

    // user의 paidFine 조회
    int getUserPaidFine(Map map);

    // user의 fine 업데이트
    void updateUserFine(Map map);

    // fine log 테이블에 insert
    void insertIntoFineLog(Map map);
}
