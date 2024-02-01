package com.encore.ordering.member.service;

import com.encore.ordering.member.domain.Address;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.request.CreateMemberRequest;
import com.encore.ordering.member.dto.request.LoginRequest;
import com.encore.ordering.member.dto.response.MemberResponse;
import com.encore.ordering.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(MemberRepository memberRepository, PasswordEncoder passwordEncoder){
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Member create(CreateMemberRequest createMemberRequest) throws IllegalArgumentException{
        if (memberRepository.findByEmail(createMemberRequest.getEmail()).isPresent()){
            throw new IllegalArgumentException("이미 가입되어 있는 이메일입니다.");
        }
        createMemberRequest.setPassword(passwordEncoder.encode(createMemberRequest.getPassword()));
        Member member = Member.toEntity(createMemberRequest);
        return memberRepository.save(member);
    }

    public MemberResponse findMyInfo(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Member findMember = memberRepository.findByEmail(authentication.getName())
                .orElseThrow(EntityNotFoundException::new);
        return MemberResponse.from(findMember);
    }

    public List<MemberResponse> findAll(){
        List<Member> members = memberRepository.findAll();
        return members.stream().map(MemberResponse::from).collect(Collectors.toList());
    }

    public Member login(LoginRequest loginRequest) throws IllegalArgumentException{
        // Email 존재 여부 Check
        Member findMember = memberRepository.findByEmail(loginRequest.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이메일입니다."));

        // Password 일치 여부 Check
        if (!passwordEncoder.matches(loginRequest.getPassword(), findMember.getPassword())){
            throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
        }
        return findMember;
    }
}
