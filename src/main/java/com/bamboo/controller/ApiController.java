package com.bamboo.controller;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserVO;
import com.bamboo.exception.*;
import com.bamboo.service.UserService;
import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*", allowCredentials = "true")
@Log4j2
public class ApiController {

    private UserService service;
    private RateLimiter limiter;

    public ApiController(UserService service) {
        this.service = service;
        limiter = RateLimiter.create(0.002);
    }

    // 유저 등록 후 등록된 유저 정보 넘김
    // Postman 테스트 시 x-www-form-urlencoded
    @PostMapping(value = "/register-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public UserVO registerUser(UserDTO user)
            throws NoGithubIdFoundException, WrongAccessCodeException, UserAlreadyExistsException, BlankArgumentException {
        log.info("Register User: " + user);

        // DB에 UserDTO의 유저 등록
        service.registerUser(user);
        // 크롤링 후 UserVO 등록 및 리턴
        UserVO init = service.initAndGetUser(user);

        return init;
    }

    // refresh 하지 않고 단순히 디비에서 꺼내와서 보내줌
    @GetMapping(value = "/fetch-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserVO> fetchUser() {
        List<UserVO> list = service.getUserListforResponse();

        log.info(list);

        return list;
    }

    // 새로 크롤링 해서 디비 업데이트 후 꺼내와서 보내줌
    @GetMapping(value = "/refresh-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserVO> refreshUser() {

        if (limiter.tryAcquire()) {
            // Service를 통해 디비 업데이트
            service.updateDB();

            log.debug("%%%%%%%%%%%% IF %%%%%%%%%%%%%");
        } else {
            log.debug("%%%%%%%%%%%% ELSE %%%%%%%%%%%%%");
        }

        return fetchUser();
    }

    // 유저 삭제
    // Postman 테스트 시 x-www-form-urlencoded
    @PostMapping("/delete-user")
    public String deleteUser(UserDTO user)
            throws NoUserExistsException, WrongUserCodeException, BlankArgumentException {
        log.info("Delete User: " + user);

        service.deleteUser(user);

        return user.getGithubId();
    }
}
