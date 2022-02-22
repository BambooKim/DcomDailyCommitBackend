package com.bamboo.service;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserVO;
import com.bamboo.exception.*;

import java.util.List;

public interface UserService {
    public void registerUser(UserDTO user) throws NoGithubIdFoundException, WrongAccessCodeException, UserAlreadyExistsException, BlankArgumentException;
    public List<UserVO> getUserListforResponse();
    public void updateDB();
    public void deleteUser(UserDTO user) throws NoUserExistsException, WrongUserCodeException, BlankArgumentException;
    public UserVO initAndGetUser(UserDTO user);
}
