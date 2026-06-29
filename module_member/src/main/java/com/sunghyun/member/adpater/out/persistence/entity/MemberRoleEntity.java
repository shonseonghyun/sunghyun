package com.sunghyun.member.adpater.out.persistence.entity;

import com.sunghyun.config.authorize.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Table(name = "member_role")
@AllArgsConstructor
@Getter
@Builder
@NoArgsConstructor
public class MemberRoleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_role_no")
    private Long memberRoleNo;

    @Enumerated(EnumType.STRING)
    private Role role;
}
