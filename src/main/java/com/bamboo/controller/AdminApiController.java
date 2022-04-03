package com.bamboo.controller;

import com.bamboo.domain.UserVO;
import com.bamboo.service.AdminService;
import com.bamboo.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin/api")
@CrossOrigin(origins = "*", allowCredentials = "true")
@Log4j2
@RequiredArgsConstructor
public class AdminApiController {

    private final AdminService adminService;
    private final UserService userService;

    @PostMapping(value = "/update-user-fine", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<UserVO> updateUserFine(String id, int fine) {

        adminService.updateFineAndLogTable(id, fine);

        return userService.getUserListforResponse();
    }
}
