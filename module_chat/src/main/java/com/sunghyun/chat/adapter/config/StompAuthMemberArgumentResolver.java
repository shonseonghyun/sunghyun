package com.sunghyun.chat.adapter.config;

import com.sunghyun.annotation.AuthMember;
import com.sunghyun.dto.AuthMemberInfo;
import com.sunghyun.dto.UserPrincipal;
import org.springframework.core.MethodParameter;
import org.springframework.messaging.Message;
import org.springframework.messaging.handler.invocation.HandlerMethodArgumentResolver;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
public class StompAuthMemberArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(AuthMember.class)
                && parameter.getParameterType().equals(AuthMemberInfo.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, Message<?> message) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (accessor != null) {
            UserPrincipal userPrincipal= (UserPrincipal) accessor.getUser();
            return userPrincipal.getAuthMemberInfo();
        }
        return null;
    }
}
