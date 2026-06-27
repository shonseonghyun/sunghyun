package com.sunghyun.member.domain.model;

import com.sunghyun.annotation.UpdateAble;
import com.sunghyun.member.domain.enums.Gender;
import com.sunghyun.member.domain.exception.PasswordMismatchException;
import com.sunghyun.member.domain.service.PasswordService;
import com.sunghyun.web.ErrorCode;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity(name ="members") //member는 mysql 예약어이므로 s 붙임
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberNo;

    @Column
    private String id;

    @Column
//    @UpdateAble
    private String pwd;

    @Column
    @UpdateAble
    private String email;

    @Column
    @UpdateAble
    private String name;

    @Column
    @UpdateAble(ignoreNull=false)
    private String tel;

    @Column
    @UpdateAble
    private String birthDt;

    @Column
    @UpdateAble
    private Gender gender;

    //사진
//    private String photo;

    @OneToMany(
            cascade = {
                    CascadeType.PERSIST, //Member save 시 저장 확인 완료
            }
    )
    @JoinColumn(name="member_no")
    private List<MemberRole> roles;

    public void encryptPassword(final PasswordService passwordService) {
        this.pwd = passwordService.encode(this.pwd);
    }

    public void checkPassword(final String inputPassword, final PasswordService passwordService) {
        // 암호화 인터페이스의 matches 메서드를 활용해 검증
        if (!passwordService.matches(inputPassword, this.pwd)) {
            throw new PasswordMismatchException(ErrorCode.M005);
        }
    }

}
