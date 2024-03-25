package com.ssafy.triptogether.member.service;

import com.ssafy.triptogether.auth.data.request.PinVerifyRequest;
import com.ssafy.triptogether.auth.provider.JwtTokenProvider;
import com.ssafy.triptogether.auth.utils.SecurityMember;
import com.ssafy.triptogether.auth.validator.pin.PinVerify;
import com.ssafy.triptogether.global.exception.exceptions.category.NotFoundException;
import com.ssafy.triptogether.global.exception.exceptions.category.ValidationException;
import com.ssafy.triptogether.infra.twinklebank.data.response.TwinkleMemberInfoResponse;
import com.ssafy.triptogether.infra.twinklebank.TwinkleBankClient;
import com.ssafy.triptogether.infra.twinklebank.data.request.TwinkleBankLogoutRequest;
import com.ssafy.triptogether.member.data.PinSaveRequest;
import com.ssafy.triptogether.member.data.PinUpdateRequest;
import com.ssafy.triptogether.member.data.ProfileFindResponse;
import com.ssafy.triptogether.member.data.ProfileUpdateRequest;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.repository.MemberRepository;
import com.ssafy.triptogether.member.utils.MemberUtils;

import lombok.RequiredArgsConstructor;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

import static com.ssafy.triptogether.global.exception.response.ErrorCode.*;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class MemberServiceImpl implements MemberSaveService, MemberLoadService {

	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;
	private final StringRedisTemplate redisTemplate;
	private final JwtTokenProvider jwtTokenProvider;
	private final TwinkleBankClient twinkleBankClient;

	@Transactional
	@Override
	public void updateProfile(long memberId, ProfileUpdateRequest profileUpdateRequest) {
		// find member
		Member member = MemberUtils.findByMemberId(memberRepository, memberId);

		// update member
		member.update(profileUpdateRequest);
	}

	@Transactional
	@Override
	public void savePin(long memberId, PinSaveRequest pinSaveRequest) {
		// validate request
		if (!pinSaveRequest.pinNum().equals(pinSaveRequest.pinNumCheck())) {
			throw new ValidationException("PinSave", PIN_CHECK_MISS_MATCH, pinSaveRequest.pinNum(),
				pinSaveRequest.pinNumCheck());
		}

		// find member
		Member member = MemberUtils.findByMemberId(memberRepository, memberId);

		// validate member
		if (member.getPinNum() != null) {
			throw new ValidationException("PinSave", PIN_ALREADY_EXISTS, memberId);
		}

		// save pin
		String encodedPinNum = passwordEncoder.encode(pinSaveRequest.pinNum());
		member.savePin(encodedPinNum);
	}

	@PinVerify
	@Transactional
	@Override
	public void updatePin(long memberId, PinVerifyRequest pinVerifyRequest, PinUpdateRequest pinUpdateRequest) {
		// validate request if miss match
		if (!pinUpdateRequest.newPinNum().equals(pinUpdateRequest.newPinNumCheck())) {
			throw new ValidationException("PinUpdate", PIN_CHECK_MISS_MATCH, pinUpdateRequest.newPinNum(),
				pinUpdateRequest.newPinNumCheck());
		}

		// find member
		Member member = MemberUtils.findByMemberId(memberRepository, memberId);

		// validate member
		if (member.getPinNum() == null) {
			throw new NotFoundException("PinUpdate", PIN_NOT_EXISTS, memberId);
		}

		// update pin
		String encodedPinNum = passwordEncoder.encode(pinUpdateRequest.newPinNum());
		member.savePin(encodedPinNum);
	}

	@Override
	public void logout(SecurityMember securityMember, String accessToken) {
		// logout from twinkle bank
		TwinkleBankLogoutRequest twinkleBankLogoutRequest = TwinkleBankLogoutRequest.builder()
			.memberUuid(securityMember.getUuid())
			.build();
		twinkleBankClient.bankLogout(twinkleBankLogoutRequest);

		// delete refresh token
		if (redisTemplate.opsForValue().get("refresh:" + securityMember.getId()) != null) {
			redisTemplate.delete("refresh:" + securityMember.getId());
		}

		// add access token to a blacklist
		redisTemplate.opsForValue().set(
			"blacklist:" + accessToken, accessToken,
			jwtTokenProvider.getACCESS_TOKEN_EXPIRE_TIME(),
			TimeUnit.MILLISECONDS
		);
	}

	@Transactional
	@Override
	public Member saveMember(TwinkleMemberInfoResponse twinkleMemberInfoResponse) {

		Member member = Member.builder()
			.username(twinkleMemberInfoResponse.name())
			.uuid(twinkleMemberInfoResponse.memberUuid())
			.nickname("")
			.gender(twinkleMemberInfoResponse.gender())
			.birth(twinkleMemberInfoResponse.birth())
			.build();

		member = memberRepository.save(member);

		return member;
	}

	@Override
	public ProfileFindResponse findProfile(long memberId) {
		// find member & return
		return memberRepository.findProfileByMemberId(memberId)
			.orElseThrow(() -> new NotFoundException("ProfileFind", UNDEFINED_MEMBER, memberId));
	}
}
