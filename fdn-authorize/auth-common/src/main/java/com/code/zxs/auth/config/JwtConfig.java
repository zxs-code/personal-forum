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
    private String pubKeyPath = "pubKey";           //公钥文件路径
    private String priKeyPath = "priKey";           //私钥文件路径
    private String secret;                          //secret 作为 随机数的种子
    private PublicKey publicKey;
    private PrivateKey privateKey;
    private Integer tokenExpire = 120;                   //jwt有效时长
    private ChronoUnit unit = ChronoUnit.MINUTES;   //jwt有效时长单位
    private String cookieName = "my-token";
    private Integer cookieMaxAge = 60 * 24 * 30;    //默认cookie保存时间为30天

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
            log.error("初始化jwt密钥对失败", e);
        }
    }
}
