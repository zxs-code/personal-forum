package com.github.code.zxs.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.temporal.ChronoUnit;

@Component
@ConfigurationProperties(prefix = "global.config.jwt")
public class JwtConfig {
    private String publicKeyPath;
    private String privateKeyPath;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private Integer expire = 30;
    private ChronoUnit unit = ChronoUnit.MINUTES;
}
