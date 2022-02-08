package com.code.zxs.auth.config;

import com.code.zxs.auth.util.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.temporal.ChronoUnit;

@Component
@Data
@Slf4j
@ConfigurationProperties(prefix = "global.config.jwt")
public class JwtConfig {
    private String pubKeyPath;
    private String priKeyPath;
    private String secret;
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private Integer expire = 120;
    private ChronoUnit unit = ChronoUnit.MINUTES;
    private String cookieName = "my-token";
    private Integer cookieMaxAge = 120;

    @PostConstruct
    public void init() {
        try {
            File pubKeyFile = new File(pubKeyPath);
            File priKeyFile = new File(priKeyPath);

            if (!pubKeyFile.exists() || !priKeyFile.exists())
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);

            publicKey = RsaUtils.getPublicKey(pubKeyPath);
            privateKey = RsaUtils.getPrivateKey(priKeyPath);
        } catch (Exception e) {
            log.error("初始化jwt密钥对失败",e);
        }
    }
}
