package com.encore.ordering.member.service;

import com.encore.ordering.member.domain.Address;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.request.CreateMemberRequest;
import com.encore.ordering.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class MemberService {
    private final MemberRepository memberRepository;

    @Autowired
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    public Member create(CreateMemberRequest createMemberRequest){
        Address address = Address.builder()
                .city(createMemberRequest.getCity())
                .street(createMemberRequest.getStreet())
                .zipcode(createMemberRequest.getZipcode())
                .build();
        Member member = createMemberRequest.toEntity(address);
        return memberRepository.save(member);
    }
}
