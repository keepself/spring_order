package com.encore.ordering.member.dto.response;

import com.encore.ordering.member.domain.Address;
import com.encore.ordering.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MemberResponse {
    private Long id;
    private String name;
    private String email;
    private String city;
    private String street;
    private String zipcode;
    private int orderCount;

    public static MemberResponse from(Member member){
        // 분기 처리할 때 일반적으로 많이 사용
        MemberResponseBuilder builder = MemberResponse.builder();
        builder.id(member.getId());
        builder.name(member.getName());
        builder.email(member.getEmail());
        builder.orderCount(member.getOrderings().size());
        Address address = member.getAddress();
        if (address != null){
            builder.city(member.getAddress().getCity());
            builder.street(member.getAddress().getStreet());
            builder.zipcode(member.getAddress().getZipcode());
        }
        return builder.build();
    }
}
