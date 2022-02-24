package com.bamboo.service;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserVO;
import com.bamboo.exception.*;

import java.util.List;

public interface UserService {
    void registerUser(UserDTO user) throws NoGithubIdFoundException, WrongAccessCodeException, UserAlreadyExistsException, BlankArgumentException;
    List<UserVO> getUserListforResponse();
    void updateDB();
    void deleteUser(UserDTO user) throws NoUserExistsException, WrongUserCodeException, BlankArgumentException;
    UserVO initAndGetUser(UserDTO user);
}
