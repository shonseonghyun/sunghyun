package com.sunghyun.config;

import com.sunghyun.dto.SecurityMemberDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;


@Getter
@AllArgsConstructor
public class SecurityUserDetails implements UserDetails {
    private SecurityMemberDto securityMemberDto;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return securityMemberDto.getRoles()
                .stream().map(
                        role->new SimpleGrantedAuthority("ROLE_"+role))
                .toList()
                ;
    }

    public Long getMemberNo(){ return securityMemberDto.getMemberNo(); }
    public String getName(){ return securityMemberDto.getName(); }

    @Override
    public String getPassword() {
        return securityMemberDto.getPwd();
    }

    @Override
    public String getUsername() {
        return securityMemberDto.getId();
    }

    //나머지 이스파이어드 관련 메서드들 전부 return true; 설정
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    @Override public boolean isEnabled() { return true; }
}
