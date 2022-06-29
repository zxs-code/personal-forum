package com.github.code.zxs.auth.config;

import com.github.code.zxs.auth.util.RsaUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.io.File;
import java.security.PrivateKey;
import java.security.PublicKey;

@Configuration
@Slf4j
@Data
@ConfigurationProperties(prefix = "bbs.auth.jwt")
public class JwtConfig {
    /**
     * 加密算法
     */
    private SignatureAlgorithm algorithm = SignatureAlgorithm.RS256;
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
    private String secret = "12345";
    /**
     * 公钥
     */
    private PublicKey publicKey;
    /**
     * 私钥
     */
    private PrivateKey privateKey;

    @PostConstruct
    public void init() {
        try {
            File pubKeyFile = new File(pubKeyPath);
            File priKeyFile = new File(priKeyPath);

            if (!pubKeyFile.exists() || !priKeyFile.exists())
                RsaUtils.generateKey(pubKeyPath, priKeyPath, secret);

            publicKey = RsaUtils.getPublicKey(pubKeyPath);
            privateKey = RsaUtils.getPrivateKey(priKeyPath);
            log.info("jwt配置加载完毕");
        } catch (Exception e) {
            log.error("初始化jwt密钥对失败", e);
        }
    }
}
