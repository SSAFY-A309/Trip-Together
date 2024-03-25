package com.ssafy.triptogether.auth.service;

import com.ssafy.triptogether.auth.utils.SecurityMember;
import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.repository.MemberRepository;
import com.ssafy.triptogether.member.utils.MemberUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String id) { // TODO : throws UsernameNotFoundException 확인해보기
        Member member = MemberUtils.findByMemberId(memberRepository, Long.parseLong(id));
        return new SecurityMember(member);
    }
}
