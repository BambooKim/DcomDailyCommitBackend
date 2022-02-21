package com.bamboo.mapper;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserDataRecord;
import com.bamboo.domain.UserVO;

import java.util.List;

public interface UserMapper {
    // 회원 등록.
    public void insertUser(UserDTO user);

    // response할 것만 뽑아다 주기
    public List<UserVO> getUserDataforResponse();

    // 크롤링 전 기존 자료 가져다 쓰기
    public List<UserDataRecord> getUserDataforUpdate();

    // 크롤링 뒤 DB update.
    public void updateUserData(UserDataRecord user);

    // 회원 삭제.
    public void deleteUser(UserDTO user);

    // 등록된 회원 정보 가져오기
    public UserDataRecord getUser(UserDTO user);

    // 등록된 회원 (githubId, userCode) 가져오기
    public UserDTO getUserForCheck(UserDTO user);
}
