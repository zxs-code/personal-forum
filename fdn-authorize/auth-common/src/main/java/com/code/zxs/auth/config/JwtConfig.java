package com.code.zxs.auth.config;

import com.code.zxs.auth.util.RsaUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.temporal.ChronoUnit;

@Configuration
@Data
@Slf4j
@ConfigurationProperties(prefix = "global.config.jwt")
public class JwtConfig {
    public static final String USER_INFO_KEY = "userInfoKey";
    public static final String TOKEN_TYPE_KEY = "tokenTypeKey";
    /**
     * 公钥文件路径
     */
    private String pubKeyPath = "pubKey";
    /**
     * 私钥文件路径
     */
    private String priKeyPath = "priKey";
    /**
     * secret 作为 随机数的种子，用来创建公钥私钥文件（文件存在时，不会再创建）
     */
    private String secret;
    /**
     * 公钥
     */
    private PublicKey publicKey;
    /**
     * 私钥
     */
    private PrivateKey privateKey;
    /**
     * jwt有效时长
     */
    private Integer tokenExpire = 120;
    /**
     * jwt有效时长单位
     */
    private ChronoUnit unit = ChronoUnit.MINUTES;
    /**
     * jwt在cookie中的名字
     */
    private String cookieName = "my-token";
    /**
     * jwt保存时长
     */
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
            log.info("完成jwt密钥对初始化");
        } catch (Exception e) {
            log.error("初始化jwt密钥对失败", e);
        }
    }

}
