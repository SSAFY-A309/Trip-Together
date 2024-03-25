package com.ssafy.triptogether.global.exception;

import com.ssafy.triptogether.global.exception.exceptions.WrongPasswordException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/exception")
public class exceptionTestController {
    @GetMapping("/badrequest")
    public void throwBadRequest() {
        throw new WrongPasswordException("테스트컨트롤러", new Object[]{"파라미터"});
    }
}
