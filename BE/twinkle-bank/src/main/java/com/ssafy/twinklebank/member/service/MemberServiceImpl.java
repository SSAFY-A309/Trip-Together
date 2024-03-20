package com.ssafy.twinklebank.member.service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.ssafy.twinklebank.global.exception.exceptions.category.NotFoundException;
import com.ssafy.twinklebank.member.data.AuthInfoFindResponse;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ssafy.twinklebank.member.data.request.MemberJoinRequest;
import com.ssafy.twinklebank.member.domain.Member;
import com.ssafy.twinklebank.member.repository.MemberRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import static com.ssafy.twinklebank.global.exception.response.ErrorCode.UNDEFINED_MEMBER;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberSaveService, MemberLoadService {
	private final MemberRepository memberRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional
	public Map<String, String> join(MemberJoinRequest request) {
		// TODO: 중복 아이디 체킹

		String encodedPassword = passwordEncoder.encode(request.password());
		String uuid = UUID.randomUUID().toString();

		Member member = Member.builder()
			.name(request.name())
			.uuid(uuid)
			.gender(request.gender())
			.birth(request.birth())
			.password(encodedPassword)
			.username(request.username())
			.build();

		member = memberRepository.save(member);

		Map<String, String> response = new HashMap<>();
		response.put("username", member.getUsername());
		response.put("name", member.getName());

		return response;
	}

    @Override
    public AuthInfoFindResponse findAuthInfo(String memberUuid) {
        // find auth info & return
        return memberRepository.findAuthInfoById(memberUuid)
                .orElseThrow(() -> new NotFoundException("AuthInfoFind", UNDEFINED_MEMBER, memberUuid));
    }
}
