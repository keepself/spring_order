package com.encore.ordering.member.dto.request;

import com.encore.ordering.member.domain.Address;
import com.encore.ordering.member.domain.Member;
import com.encore.ordering.member.domain.Role;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Data
public class CreateMemberRequest {
    @NotEmpty(message = "Name Is Essential")
    private String name;
    @NotEmpty(message = "Email Is Essential")
    @Email(message = "Email Is Not Valid")
    private String email;
    @NotEmpty(message = "Password Is Essential")
    @Size(min = 4, message = "Minimum Length Is 4")
    private String password;
    private String city;
    private String street;
    private String zipcode;

    public Member toEntity(Address address){
        return Member.builder()
                .name(name)
                .email(email)
                .password(password)
                .address(address)
                .role(Role.ROLE_USER)
                .build();
    }
}
