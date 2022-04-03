package com.bamboo.service;

import com.bamboo.mapper.AdminMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Log4j2
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminMapper adminMapper;

    @Override
    public void updateFineAndLogTable(String id, int fine) {

        // 0. id에 해당하는 레코드를 찾아 기존 paidFine 찾기
        Map<String, Object> user = new HashMap<>();
        user.put("id", id);
        int userPaidFine = adminMapper.getUserPaidFine(user);

        // 1. id에 해당하는 레코드를 찾아 paidFine에 더하여 update.
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("fine", userPaidFine + fine);

        adminMapper.updateUserFine(map);

        // 2. 로그 기록 테이블에 납부 내역 insert.

    }
}
