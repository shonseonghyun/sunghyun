package com.sunghyun.member.application.dto.req;

import com.sunghyun.annotation.NotNullWithMsg;
import com.sunghyun.member.domain.enums.Gender;
import com.sunghyun.member.adpater.out.persistence.entity.MemberEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MemberModifyReqDto {
    @NotNullWithMsg
    private Long memberNo;

    @Size(min = 8, message = "{member.pwd.size}")
    private String currentPwd;

    @Size(min = 8, message = "{member.newpwd.size}")
    private String newPwd;

    @Email(message = "{member.email.format}")
    private String email;

    @Pattern(regexp = "^[0-9]*$", message = "{member.tel.pattern}")
    private String tel;

    private Gender gender;

//    private String photo;

    public MemberEntity toDomain() {
        //upateable 등록된 필드들로만 도메인 만들도록 자동화 못하나?
        //왜냐면 매번 수정dto생길때마다 이렇게 길게 써야할텐데..
        return MemberEntity.builder()
                .memberNo(this.memberNo)
                .pwd(this.newPwd) // DTO의 newPwd를 엔티티의 pwd 필드로 매핑
                .email(this.email)
                .tel(this.tel)
                .gender(this.gender)
                .build();
    }
}
