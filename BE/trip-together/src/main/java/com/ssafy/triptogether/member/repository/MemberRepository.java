package com.ssafy.triptogether.member.repository;

import java.util.Optional;

import com.ssafy.triptogether.member.domain.Member;
import com.ssafy.triptogether.member.repository.query.MemberRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Long>, MemberRepositoryCustom {
	Optional<Member> findMemberByUuid(String uuid);
}
