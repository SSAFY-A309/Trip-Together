package com.ssafy.triptogether.syncaccount.service;

import com.ssafy.triptogether.auth.data.request.PinVerifyRequest;
import com.ssafy.triptogether.global.exception.exceptions.category.ForbiddenException;
import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.infra.twinklebank.TwinkleBankClient;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleAccountSyncRequest;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleBankAccountsLoadRequest;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleAccountSyncResponse;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleBankAccountsDetail;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleBankAccountsLoadResponse;
import com.ssafy.triptogether.member.domain.Gender;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.repository.MemberRepository;
import com.ssafy.triptogether.syncaccount.data.request.MainSyncAccountUpdateRequest;
import com.ssafy.triptogether.syncaccount.data.request.SyncAccountDeleteRequest;
import com.ssafy.triptogether.syncaccount.data.request.SyncAccountSaveRequest;
import com.ssafy.triptogether.syncaccount.data.response.BankAccountsLoadResponse;
import com.ssafy.triptogether.syncaccount.data.response.SyncAccountsDetail;
import com.ssafy.triptogether.syncaccount.data.response.SyncAccountsLoadResponse;
import com.ssafy.triptogether.syncaccount.domain.SyncAccount;
import com.ssafy.triptogether.syncaccount.repository.SyncAccountRepository;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

@ExtendWith(MockitoExtension.class)
class SyncAccountServiceImplTest {
    @InjectMocks
    SyncAccountServiceImpl syncAccountService;
    @Mock
    SyncAccountRepository syncAccountRepository;
    @Mock
    TwinkleBankClient twinkleBankClient;
    @Mock
    MemberRepository memberRepository;

    @Nested
    @DisplayName("연동 계좌 목록 조회")
    class SyncAccountsLoadTest {
        List<SyncAccountsDetail> syncAccounts;

        @BeforeEach
        void setUp() {
            SyncAccountsDetail syncAccount1 = SyncAccountsDetail.builder()
                    .uuid("test1")
                    .name("테스트 계좌")
                    .accountNum("123-123")
                    .isMain(true)
                    .build();
            SyncAccountsDetail syncAccount2 = SyncAccountsDetail.builder()
                    .uuid("test1")
                    .name("테스트 계좌")
                    .accountNum("123-123")
                    .isMain(true)
                    .build();
            syncAccounts = Arrays.asList(syncAccount1, syncAccount2);
        }

        @Test
        void syncAccountsLoad() {
            // given
            given(syncAccountRepository.memberSyncAccountsLoad(anyLong())).willReturn(syncAccounts);
            // when
            SyncAccountsLoadResponse response = syncAccountService.syncAccountsLoad(1L);
            //then
            assertAll(
                    () -> assertEquals(2, response.syncAccountsDetail().size(), "연동 계좌 목록의 크기가 예상과 다릅니다."),
                    () -> assertEquals("test1", response.syncAccountsDetail().get(0).uuid(), "첫 번쨰 연동 계좌가 예상과 다릅니다.")
            );
            verify(syncAccountRepository, times(1)).memberSyncAccountsLoad(1L);
        }
    }

    @Nested
    @MockitoSettings(strictness = Strictness.LENIENT)
    @DisplayName("사용자의 연동 계좌의 주계좌 설정 변경")
    class MainSyncAccountUpdateTest {
        Long memberId = 1L;
        Member member;
        SyncAccount currentMainSyncAccount, newMainSyncAccount;
        MainSyncAccountUpdateRequest mainSyncAccountUpdateRequest;

        @BeforeEach
        void setUp() {
            member = Member.builder()
                    .gender(Gender.MALE)
                    .nickname("TestUser")
                    .uuid("TestUser")
                    .birth(LocalDate.now())
                    .build();
            ReflectionTestUtils.setField(member, "id", memberId);
            currentMainSyncAccount = SyncAccount.builder()
                    .uuid("currentMain")
                    .num("123-123")
                    .name("currentMain")
                    .isMain(true)
                    .member(member)
                    .build();
            newMainSyncAccount = SyncAccount.builder()
                    .uuid("newMain")
                    .num("456-456")
                    .name("newMain")
                    .isMain(false)
                    .member(member)
                    .build();
            mainSyncAccountUpdateRequest = MainSyncAccountUpdateRequest.builder()
                    .uuid("newMain")
                    .pinNum("1234")
                    .build();
        }

        @Test
        @DisplayName("주계좌 변경 성공")
        void mainSyncAccountUpdateSuccess() {
            // given
            given(syncAccountRepository.findByMemberIdAndIsMain(anyLong(), eq(true)))
                    .willReturn(Optional.of(currentMainSyncAccount));
            given(syncAccountRepository.findByUuid(anyString()))
                    .willReturn(Optional.of(newMainSyncAccount));
            // when`
            syncAccountService.mainSyncAccountUpdate(memberId, mainSyncAccountUpdateRequest);
            // then
            assertAll(
                    () -> assertFalse(currentMainSyncAccount.getIsMain(), "이전 주계좌가 비활성화 되지 않았습니다."),
                    () -> assertTrue(newMainSyncAccount.getIsMain(), "새로운 계좌가 주계좌로 설정되지 않았습니다.")
            );
            verify(syncAccountRepository, times(1)).findByMemberIdAndIsMain(memberId, true);
            verify(syncAccountRepository, times(1)).findByUuid(mainSyncAccountUpdateRequest.uuid());
        }

        @Test
        @DisplayName("주계좌 변경을 위한 요청 계좌가 없는 경우")
        void newMainSyncAccountEmpty() {
            // given
            given(syncAccountRepository.findByMemberIdAndIsMain(anyLong(), eq(true)))
                    .willReturn(Optional.of(currentMainSyncAccount));
            given(syncAccountRepository.findByUuid(anyString()))
                    .willReturn(Optional.empty());
            // when`& then
            assertThrows(NotFoundException.class, () -> {
                syncAccountService.mainSyncAccountUpdate(memberId, mainSyncAccountUpdateRequest);
            });
            verify(syncAccountRepository, times(1)).findByMemberIdAndIsMain(memberId, true);
            verify(syncAccountRepository, times(1)).findByUuid(mainSyncAccountUpdateRequest.uuid());
        }

        @Test
        @DisplayName("주계좌 설정 권한이 없는 경우")
        void newMainSyncAccountForbidden() {
            // given
            given(syncAccountRepository.findByMemberIdAndIsMain(anyLong(), eq(true)))
                    .willReturn(Optional.of(currentMainSyncAccount));
            given(syncAccountRepository.findByUuid(anyString()))
                    .willReturn(Optional.of(newMainSyncAccount));
            // when`& then
            assertThrows(ForbiddenException.class, () -> {
                syncAccountService.mainSyncAccountUpdate(2L, mainSyncAccountUpdateRequest);
            });
            verify(syncAccountRepository, times(1)).findByMemberIdAndIsMain(2L, true);
            verify(syncAccountRepository, times(1)).findByUuid(mainSyncAccountUpdateRequest.uuid());
        }
    }

    @Nested
    @DisplayName("사용자 은행 계좌 목록 조회")
    class BankAccountLoadTest {
        Member member;
        TwinkleBankAccountsLoadResponse twinkleBankAccountsLoadResponse;

        @BeforeEach
        void setUp() {
            member = Member.builder()
                    .gender(Gender.MALE)
                    .nickname("TestUser")
                    .uuid("TestUser")
                    .birth(LocalDate.now())
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
        }

        @Test
        void bankAccountLoad() {
            // given
            given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
            given(twinkleBankClient.bankAccountsLoad(any(TwinkleBankAccountsLoadRequest.class))).willReturn(twinkleBankAccountsLoadResponse);
            // when
            BankAccountsLoadResponse response = syncAccountService.bankAccountsLoad(1L);
            // then
            assertEquals(2, response.bankAccountsDetails().size());
            assertEquals("TestAccount1", response.bankAccountsDetails().get(0).uuid());
            assertEquals("TestAccount2", response.bankAccountsDetails().get(1).uuid());
        }
    }

    @Nested
    @DisplayName("연동 계좌 등록")
    class AccountSyncTest {
        PinVerifyRequest pinVerifyRequest;
        SyncAccountSaveRequest syncAccountSaveRequest;
        TwinkleAccountSyncResponse twinkleAccountSyncResponse;
        Member member;

        @BeforeEach
        void setUp() {
            pinVerifyRequest = PinVerifyRequest.builder()
                    .pinNum("1234")
                    .build();
            syncAccountSaveRequest = SyncAccountSaveRequest.builder()
                    .pinNum("test")
                    .bankAccountUuid("test")
                    .isMain(false)
                    .build();
            twinkleAccountSyncResponse = TwinkleAccountSyncResponse.builder()
                    .accountName("test")
                    .accountNum("test")
                    .accountUuid("test")
                    .build();
            member = Member.builder()
                    .gender(Gender.MALE)
                    .nickname("TestUser")
                    .uuid("TestUser")
                    .birth(LocalDate.now())
                    .build();
        }

        @Test
        @DisplayName("첫 계좌 연동인 경우")
        void accountSyncInit() {
            // given
            given(twinkleBankClient.bankAccountsSync(any(TwinkleAccountSyncRequest.class))).willReturn(twinkleAccountSyncResponse);
            given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
            given(syncAccountRepository.memberSyncAccountExist(anyLong())).willReturn(true);
            // when
            syncAccountService.syncAccountSave(1L, pinVerifyRequest, syncAccountSaveRequest);
            // then
            verify(syncAccountRepository, times(1)).save(any(SyncAccount.class));
        }

        @Test
        @DisplayName("이미 연동 계좌가 있는 경우")
        void accountSync() {
            // given
            given(twinkleBankClient.bankAccountsSync(any(TwinkleAccountSyncRequest.class))).willReturn(twinkleAccountSyncResponse);
            given(memberRepository.findById(anyLong())).willReturn(Optional.ofNullable(member));
            given(syncAccountRepository.memberSyncAccountExist(anyLong())).willReturn(false);
            // when
            syncAccountService.syncAccountSave(1L, pinVerifyRequest, syncAccountSaveRequest);
            // then
            verify(syncAccountRepository, times(1)).save(any(SyncAccount.class));
        }
    }

    @Nested
    @DisplayName("연동 계좌 해지")
    class AccountSyncDeleteTest {
        long memberId = 1L;
        PinVerifyRequest pinVerifyRequest;
        SyncAccountDeleteRequest syncAccountDeleteRequest;
        Member member;
        SyncAccount syncAccount;

        @BeforeEach
        void setUp() {
            pinVerifyRequest = PinVerifyRequest.builder()
                    .pinNum("1234")
                    .build();
            syncAccountDeleteRequest = SyncAccountDeleteRequest.builder()
                    .pinNum("test")
                    .bankAccountUuid("test")
                    .build();
            member = Member.builder()
                    .gender(Gender.MALE)
                    .nickname("TestUser")
                    .uuid("TestUser")
                    .birth(LocalDate.now())
                    .build();
            syncAccount = SyncAccount.builder()
                    .uuid("newMain")
                    .num("456-456")
                    .name("newMain")
                    .isMain(false)
                    .member(member)
                    .build();
            ReflectionTestUtils.setField(member, "id", memberId);
        }

        @Test
        @DisplayName("연동 해지 성공")
        void accountSyncDeleteSuccess() {
            // given
            given(syncAccountRepository.findByUuid(anyString())).willReturn(Optional.ofNullable(syncAccount));
            // when
            syncAccountService.syncAccountDelete(memberId, pinVerifyRequest, syncAccountDeleteRequest);
            // then
            verify(syncAccountRepository, times(1)).delete(any(SyncAccount.class));
        }

        @Test
        @DisplayName("연동 해지 요청 계좌가 없는 경우")
        void accountSyncDeleteNotFound() {
            // given
            given(syncAccountRepository.findByUuid(anyString())).willReturn(Optional.empty());
            // when & then
            assertThrows(NotFoundException.class, () -> {
                syncAccountService.syncAccountDelete(memberId, pinVerifyRequest, syncAccountDeleteRequest);
            });
        }

        @Test
        @DisplayName("연동 해지 권한이 없는 경우")
        void accountSyncDeleteForbidden() {
            // given
            given(syncAccountRepository.findByUuid(anyString())).willReturn(Optional.ofNullable(syncAccount));
            // when & then
            assertThrows(ForbiddenException.class, () -> {
                syncAccountService.syncAccountDelete(2L, pinVerifyRequest, syncAccountDeleteRequest);
            });
        }
    }
}
