package com.ssafy.triptogether.infra.twinklebank;

import com.ssafy.triptogether.global.data.response.ApiResponse;
import com.ssafy.triptogether.global.exception.exceptions.category.ExternalServerException;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleAccountSyncRequest;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleBankAccountsLoadRequest;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleAccountSyncResponse;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleBankAccountsDetail;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleBankAccountsLoadResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class TwinkleBankClientImplTest {
    @InjectMocks
    TwinkleBankClientImpl twinkleBankClient;
    @Mock
    RestTemplate restTemplate;

    @Nested
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("반짝 은행 계좌 목록 조회")
    class BankAccountsLoadTest {
        TwinkleBankAccountsLoadRequest twinkleBankAccountsLoadRequest;
        ApiResponse apiResponse;
        TwinkleBankAccountsLoadResponse twinkleBankAccountsLoadResponse;

        @BeforeEach
        void setUp() {
            twinkleBankAccountsLoadRequest = TwinkleBankAccountsLoadRequest.builder()
                    .uuid("test")
                    .build();
            TwinkleBankAccountsDetail bankAccount1 = TwinkleBankAccountsDetail.builder()
                    .uuid("TestAccount1")
                    .balance(3.0)
                    .name("TestAccount1")
                    .num("123-123")
                    .build();
            TwinkleBankAccountsDetail bankAccount2 = TwinkleBankAccountsDetail.builder()
                    .uuid("TestAccount2")
                    .balance(3.0)
                    .name("TestAccount2")
                    .num("456-456")
                    .build();
            twinkleBankAccountsLoadResponse = TwinkleBankAccountsLoadResponse.builder()
                    .twinkleBankAccountsDetails(List.of(bankAccount1, bankAccount2))
                    .build();
            apiResponse = ApiResponse.builder()
                    .status(1)
                    .message("test")
                    .data(twinkleBankAccountsLoadResponse)
                    .build();
        }

        @Test
        @DisplayName("반짝 은행 계좌 목록 조회 성공")
        void bankAccountsLoadSuccess() {
            // given
            given(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(ApiResponse.class)
            )).willReturn(new ResponseEntity<>(apiResponse, HttpStatus.OK));
            // when
            TwinkleBankAccountsLoadResponse response = twinkleBankClient.bankAccountsLoad(
                    twinkleBankAccountsLoadRequest);
            // then
            assertAll(
                    () -> assertNotNull(response),
                    () -> assertEquals(twinkleBankAccountsLoadResponse, response)
            );
        }

        @Test
        @DisplayName("반짝 은행 계좌 목록 조회 실패")
        void bankAccountsLoadFail() {
            // given
            given(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.GET),
                    any(HttpEntity.class),
                    eq(ApiResponse.class)
            )).willReturn(new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST));
            // when & then
            assertThrows(ExternalServerException.class, () -> {
                twinkleBankClient.bankAccountsLoad(twinkleBankAccountsLoadRequest);
            });
        }
    }

    @Nested
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("반짝 은행 계좌 연동 요청")
    class BankAccountSyncTest {
        TwinkleAccountSyncRequest twinkleAccountSyncRequest;
        ApiResponse apiResponse;
        TwinkleAccountSyncResponse twinkleAccountSyncResponse;

        @BeforeEach
        void setUp() {
            twinkleAccountSyncRequest = TwinkleAccountSyncRequest.builder()
                    .accountUuid("test")
                    .build();
            twinkleAccountSyncResponse = TwinkleAccountSyncResponse.builder()
                    .accountUuid("test")
                    .accountName("test")
                    .accountNum("test")
                    .build();
            apiResponse = ApiResponse.builder()
                    .status(1)
                    .message("test")
                    .data(twinkleAccountSyncResponse)
                    .build();
        }

        @Test
        @DisplayName("반짝 은행 계좌 연동 성공")
        void bankAccountsSyncSuccess() {
            // given
            given(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.POST),
                    any(HttpEntity.class),
                    eq(ApiResponse.class)
            )).willReturn(new ResponseEntity<>(apiResponse, HttpStatus.OK));
            // when
            TwinkleAccountSyncResponse response = twinkleBankClient.bankAccountsSync(
                    twinkleAccountSyncRequest);
            // then
            assertAll(
                    () -> assertNotNull(response),
                    () -> assertEquals(twinkleAccountSyncResponse, response)
            );
        }

        @Test
        @DisplayName("반짝 은행 계좌 연동 실패")
        void bankAccountsSyncFail() {
            // given
            given(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.POST),
                    any(HttpEntity.class),
                    eq(ApiResponse.class)
            )).willReturn(new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST));
            // when & then
            assertThrows(ExternalServerException.class, () -> {
                twinkleBankClient.bankAccountsSync(twinkleAccountSyncRequest);
            });
        }
    }

    @Nested
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("반짝 은행 계좌 연동 해지 요청")
    class BankAccountSyncDeleteTest {
        TwinkleAccountSyncRequest twinkleAccountSyncRequest;
        ApiResponse apiResponse;

        @BeforeEach
        void setUp() {
            twinkleAccountSyncRequest = TwinkleAccountSyncRequest.builder()
                    .accountUuid("test")
                    .build();
            apiResponse = ApiResponse.builder()
                    .status(1)
                    .message("test")
                    .data(null)
                    .build();
        }

        @Test
        @DisplayName("반짝 은행 계좌 연동 해지 실패")
        void bankAccountsLoadFail() {
            // given
            given(restTemplate.exchange(
                    anyString(),
                    eq(HttpMethod.DELETE),
                    any(HttpEntity.class),
                    eq(ApiResponse.class)
            )).willReturn(new ResponseEntity<>(apiResponse, HttpStatus.BAD_REQUEST));
            // when & then
            assertThrows(ExternalServerException.class, () -> {
                twinkleBankClient.bankAccountSyncDelete(twinkleAccountSyncRequest);
            });
        }
    }
}