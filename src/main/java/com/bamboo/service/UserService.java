package com.bamboo.service;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserVO;

import java.util.List;

public interface UserService {
    public void registerUser(UserDTO user);
    public List<UserVO> getUserList();
    public void updateDB();
}