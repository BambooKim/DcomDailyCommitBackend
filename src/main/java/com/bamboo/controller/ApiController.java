package com.bamboo.controller;

import com.bamboo.domain.UserDTO;
import com.bamboo.domain.UserVO;
import com.bamboo.service.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api")
@Log4j2
@AllArgsConstructor
public class ApiController {

    private UserService service;

    @PostMapping("/register-user")
    public String registerUser(UserDTO user) {
        log.info("Register User: " + user);

        service.registerUser(user);

        return "Registered";
    }

    // refresh 하지 않고 단순히 디비에서 꺼내와서 보내줌
    @GetMapping(value = "/fetch-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<UserVO> fetchUser() {
        ArrayList<UserVO> list = new ArrayList<>();
        list = (ArrayList<UserVO>) service.getUserList();

        return list;
    }

    // 새로 크롤링 해서 디비 업데이트 후 꺼내와서 보내줌
    @GetMapping(value = "/refresh-user", produces = MediaType.APPLICATION_JSON_VALUE)
    public ArrayList<UserVO> refreshUser() {

        return null;
    }

    @PostMapping("/delete-user")
    public String deleteUser(String githubId) {
        log.info("Delete User: " + githubId);

        return "Deleted";
    }
}
