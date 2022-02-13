package com.bamboo.mapper;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserVO;

import java.util.List;

public interface UserMapper {
    public void insertUser(UserDTO user);
    public List<UserVO> getUserList();
}
