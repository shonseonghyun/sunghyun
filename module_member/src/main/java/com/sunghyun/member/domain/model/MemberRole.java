package com.sunghyun.member.domain.model;

import com.sunghyun.member.domain.enums.Role;
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
public class MemberRole {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_role_no")
    private Long memberRoleNo;

    private Role role;
}
