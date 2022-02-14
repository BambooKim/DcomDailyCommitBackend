package com.bamboo.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    // 회원 등록 및 삭제 때 사용하는 도메인 객체

    private String githubId;
    private String korName;
}
