package com.encore.ordering.member.domain;

import lombok.*;

import javax.persistence.Embeddable;

@Embeddable // 이 객체를 어딘가의 객체에 중간에 삽입시킨다.
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address {
    private String city;
    private String street;
    private String zipcode;
}
