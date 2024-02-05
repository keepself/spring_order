package com.encore.ordering.member.controller;

import com.encore.ordering.common.CommonResponse;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.dto.request.CreateMemberRequest;
import com.encore.ordering.member.dto.request.LoginRequest;
import com.encore.ordering.member.dto.response.MemberResponse;
import com.encore.ordering.member.service.MemberService;
import com.encore.ordering.order.dto.OrderResponse;
import com.encore.ordering.order.service.OrderService;
import com.encore.ordering.securities.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class MemberController {
    private final MemberService memberService;
    private final OrderService orderService;
    private final JwtProvider jwtProvider;

    @Autowired
    public MemberController(MemberService memberService, OrderService orderService, JwtProvider jwtProvider){
        this.memberService = memberService;
        this.orderService = orderService;
        this.jwtProvider = jwtProvider;
    }

    @PostMapping("/member/create")
    public ResponseEntity<CommonResponse> create(@Valid @RequestBody CreateMemberRequest createMemberRequest){
        Member member = memberService.create(createMemberRequest);
        return new ResponseEntity<>(new CommonResponse(HttpStatus.CREATED,
                "Member Successfully Created", member.getId()), HttpStatus.CREATED);
    }

    // 특정 회원의 주문 내역 조회 (관리자 권한)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/member/{id}/orders")
    public List<OrderResponse> findMemberOrders(@PathVariable (value = "id") Long id){
        return orderService.findByMember(id);

    }

    @GetMapping("/member/myorders")
    public List<OrderResponse> findMyOrders(){

        return orderService.findMyOrders();
    }
//
//    // 내 주문 내역 조회
//    @GetMapping("/member/myorders")




    @PostMapping("/doLogin")
    public ResponseEntity<CommonResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        Member member = memberService.login(loginRequest);
        // 토큰 생성
        String jwt = jwtProvider.createToken(member.getEmail(), member.getRole().toString());
        Map<String, Object> member_info = new HashMap<>();
        member_info.put("id", member.getId());
        member_info.put("token", jwt);

        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
                "Member Successfully Login", member_info), HttpStatus.OK);
    }

//    @GetMapping("/members")
//    public ResponseEntity<ResponseDto> members(){
//        List<MemberResponse> memberResponses = memberService.findAll();
//        return new ResponseEntity<>(new ResponseDto(HttpStatus.OK,
//                "Get Member List", memberResponses), HttpStatus.OK);
//    }

    @GetMapping("/member/myInfo")
    public ResponseEntity<CommonResponse> findMyInfo(){
        MemberResponse memberResponse = memberService.findMyInfo();
        return new ResponseEntity<>(new CommonResponse(HttpStatus.OK,
                "Member Successfuly Found", memberResponse), HttpStatus.OK);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/members")
    public List<MemberResponse> memebers(){
        return memberService.findAll();
    }

}
