package com.sunghyun.config.authorize;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SecurityMatcherChain {
    private List<SecurityRequestMatcher> matchers = new ArrayList<>();

    public void add(SecurityRequestMatcher requestMatcher){
        matchers.add(requestMatcher);
    }

    public void addAll(SecurityRequestMatcher... matcher) {
        matchers.addAll(List.of(matcher));
    }
}
