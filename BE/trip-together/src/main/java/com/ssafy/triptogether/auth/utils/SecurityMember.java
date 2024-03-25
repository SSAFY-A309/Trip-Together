package com.ssafy.triptogether.auth.utils;

import com.ssafy.triptogether.member.domain.Member;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

@Getter
@NoArgsConstructor
public class SecurityMember implements UserDetails {

    private Long id;
    private String uuid;
    private String username;

    public SecurityMember(Member member) {
        this.id = member.getId();
        this.uuid = member.getUuid();
        this.username = member.getUsername();
    }

    public SecurityMember(Long id, String uuid, String username) {
        this.id = id;
        this.uuid = uuid;
        this.username = username;
    }

    public SecurityMember(Authentication authentication) {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 사용자가 "AUTHORITY" 권한을 가지고 있음.
        return Collections.singleton(new SimpleGrantedAuthority("AUTHORITY"));
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() { // false 반환시 : 계정 만료
        return true;
    }

    @Override
    public boolean isAccountNonLocked() { // false 반환 시 : 계정 잠김
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() { // false 반환 시 : 인증 정보 만료
        return true;
    }

    @Override
    public boolean isEnabled() { // false 반환 시 : 계정 비활성화
        return true;
    }
}
