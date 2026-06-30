package com.sunghyun.member.adpater.out.persistence.entity;

import com.sunghyun.annotation.UpdateAble;
import com.sunghyun.config.authorize.Role;
import com.sunghyun.member.domain.enums.Gender;
import com.sunghyun.member.domain.model.Member;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "members")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberEntity {
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
                    CascadeType.PERSIST, //Member save 시 저장 확인 완료,
                    CascadeType.REMOVE
            }
//            orphanRemoval = true
    )
    @JoinColumn(name="member_no",nullable = false)
    private List<MemberRoleEntity> roles;

    public Member toDomain() {
        List<Role> domainRoles = this.roles == null ? new ArrayList<>() :
                this.roles.stream()
                        .map(MemberRoleEntity::getRole)
                        .toList();

        return Member.builder()
                .memberNo(this.memberNo)
                .id(this.id)
                .pwd(this.pwd)
                .name(this.name)
                .email(this.email)
                .tel(this.tel)
                .birthDt(this.birthDt)
                .gender(this.gender)
                .roles(domainRoles)
                .build();
    }

    public static MemberEntity fromDomain(final Member member) {
        if (member == null) return null;

        List<MemberRoleEntity> entityRoles = member.getRoles() == null ? new ArrayList<>() :
                member.getRoles().stream()
                        .map(role -> MemberRoleEntity.builder().role(role).build())
                        .toList();

        return MemberEntity.builder()
                .memberNo(member.getMemberNo())
                .id(member.getId())
                .pwd(member.getPwd())
                .name(member.getName())
                .email(member.getEmail())
                .tel(member.getTel())
                .birthDt(member.getBirthDt())
                .gender(member.getGender())
                .roles(entityRoles)
                .build();
    }
}
