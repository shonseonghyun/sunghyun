package com.sunghyun.member.application.dto.req;

import com.sunghyun.annotation.DtoOnly;
import com.sunghyun.annotation.NotBlankWithMsg;
import com.sunghyun.member.domain.enums.Gender;
import com.sunghyun.member.domain.model.Member;
import jakarta.validation.constraints.*;
import lombok.*;

@Builder
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MemberRegisterReqDto {

    /* --- Entity Fields --- */
    @NotBlankWithMsg
    @Size(min = 6, message = "{member.id.size}")
    private String id;

    @NotBlankWithMsg
    @Size(min = 8, message = "{member.pwd.size}")
    private String pwd;

    @NotBlankWithMsg
    @Email(message = "{member.email.format}")
    private String email;

    @NotBlankWithMsg
    private String name;

    @Pattern(regexp = "^[0-9]*$", message = "{member.tel.pattern}")
    private String tel;

    @Pattern(regexp = "^[0-9]{6}$", message = "{member.birth.pattern}")
    private String birthDt;

    @NotNull(message = "{common.notnull}")
    private Gender gender;

    /* --- Business/Logic Fields (Not in Entity) --- */
    @DtoOnly
    @NotBlank(message = "{common.notblank}")
    private String pendingToken;

//    private String photo;

    public Member toDomain() {
        return Member.builder()
                .id(this.id)
                .pwd(this.pwd) // 나중에 암호화 로직(passwordEncoder.encode)이 들어갈 자리입니다.
                .email(this.email)
                .name(this.name)
                .tel(this.tel)
                .birthDt(this.birthDt)
                .gender(this.gender)
                .build();
    }
}
