package com.github.code.zxs.core.auth.provider;





@Component
public class AuthUserProvider implements UserProvider {
    @Override
    public Object getUserIdentify() {
        return UserContext.getId();
    }
}
