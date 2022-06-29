package com.github.code.zxs.core.generator;

import com.github.code.zxs.core.context.HttpContext;
import com.github.code.zxs.core.generator.base.KeyGenerator;
import org.aspectj.lang.Signature;
import org.springframework.stereotype.Component;

@Component
public class IPKeyGenerator implements KeyGenerator {
    @Override
    public String get(Class c, Signature signature, Object[] args) {
        return HttpContext.getClientIP();
    }
}
