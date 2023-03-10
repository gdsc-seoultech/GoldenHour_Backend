package com.gdsc.goldenhour.user.controller;

import com.gdsc.goldenhour.common.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {

    @PostMapping("")
    public ResponseEntity<ResponseDto<?>> login() {
        return new ResponseEntity<>(ResponseDto.success("로그인 완료"), HttpStatus.OK);
    }

}
