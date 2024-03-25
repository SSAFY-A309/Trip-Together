package com.ssafy.triptogether.auth.validator.pin;

import com.ssafy.triptogether.auth.data.request.PinVerifyRequest;
import com.ssafy.triptogether.member.domain.Gender;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.repository.MemberRepository;
import org.aspectj.lang.JoinPoint;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
class PinVerifyAspectTest {
    @InjectMocks
    PinVerifyAspect pinVerifyAspect;
    @Mock
    MemberRepository memberRepository;
    @Mock
    JoinPoint joinPoint;

    @Nested
    @DisplayName("사용자 PIN 인증")
    class PinVerifyTest {
        Long memberId = 1L;
        PinVerifyRequest pinVerifyRequest;
        Member member = Member.builder()
                .uuid("test")
                .nickname("test")
                .gender(Gender.MALE)
                .birth(LocalDate.now())
                .build();

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);
            pinVerifyRequest = PinVerifyRequest.builder()
                    .pinNum("1234")
                    .build();
            member.savePin("1234");
        }

    }
}