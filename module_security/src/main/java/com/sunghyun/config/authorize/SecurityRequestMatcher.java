package com.sunghyun.config.authorize;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

import java.util.Collections;
import java.util.List;

@Setter(AccessLevel.PRIVATE)
public class SecurityRequestMatcher {
    @Getter
    private RequestHookType requestHookType;

    @Getter
    private HttpMethod httpMethod;

    @Getter
    private String[] patterns;
    private List<Role> roles;

    @Builder(access = AccessLevel.PRIVATE)
    public SecurityRequestMatcher(RequestHookType requestHookType, HttpMethod httpMethod, String[] patterns,List<Role> roles) {
        this.requestHookType = requestHookType;
        this.httpMethod = httpMethod;
        this.patterns = patterns;
        this.roles= roles;
    }

    public String getRole() {
        return roles.get(0).name();
    }


    public boolean isAnyRequest(){
        return httpMethod==null && patterns==null;
    }

    public static SecurityRequestMatcher patterns(String ...patterns){
        return SecurityRequestMatcher.builder()
                .patterns(patterns)
                .build()
                ;
    }

    public static SecurityRequestMatcher patterns(HttpMethod httpMethod, String ...patterns){
        return SecurityRequestMatcher.builder()
                .httpMethod(httpMethod)
                .patterns(patterns)
                .build()
                ;
    }

    public static SecurityRequestMatcher any(){
        return SecurityRequestMatcher.builder()
                .build()
                ;
    }

    public SecurityRequestMatcher hasRole(Role role, String  ...patterns){
        this.setRequestHookType(RequestHookType.HAS_ROLE);
        this.setRoles(Collections.singletonList(role));

        return this;
    }

    public SecurityRequestMatcher authenticated() {
        this.setRequestHookType(RequestHookType.AUTHENTICATED);

        return this;
    }

    public SecurityRequestMatcher denyAll(){
        this.setRequestHookType(RequestHookType.DENY_ALL);

        return this;
    }

    public SecurityRequestMatcher permitAll(){
        this.setRequestHookType(RequestHookType.PERMIT_ALL);

        return this;
    }

    public boolean isUrlOnly() {
        return httpMethod == null && patterns != null;
    }


    enum RequestHookType {
        AUTHENTICATED,
        HAS_ROLE,
//        HAS_ANY_ROLE,
        PERMIT_ALL,
        DENY_ALL
    }
}
