package com.encore.ordering.member.controller;

import com.encore.ordering.common.ResponseDto;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.request.CreateMemberRequest;
import com.encore.ordering.member.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class MemberController {
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }

    @PostMapping("/member/create")
    public ResponseEntity<ResponseDto> create(@Valid @RequestBody CreateMemberRequest createMemberRequest){
        Member member = memberService.create(createMemberRequest);
        return new ResponseEntity<>(new ResponseDto(HttpStatus.CREATED,
                "Member Successfully Created", member.getId()), HttpStatus.CREATED);
    }
}
