package com.github.code.zxs.core.generator;

import com.github.code.zxs.auth.context.UserContext;
import com.github.code.zxs.core.generator.base.KeyGenerator;
import org.aspectj.lang.Signature;
import org.springframework.stereotype.Component;

@Component
public class UserIdKeyGenerator implements KeyGenerator {
    @Override
    public String get(Class c, Signature signature, Object[] args) {
        return UserContext.getId().toString();
    }
}
