package com.github.code.zxs.core.util;

import com.github.code.zxs.core.dto.TokenDTO;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class JwtUtils {

    public static final String DEFAULT_CLAIM_NAME = "info";

    /**
     *
     * @param info  token中存储的信息
     * @param privateKey 加密所使用的私钥
     * @param expire token有效时长
     * @param unit token有效时长的单位
     * @return
     * @throws Exception
     */
    public static String generateToken(Object info, PrivateKey privateKey, int expire,ChronoUnit unit) throws Exception {
        ZonedDateTime now = ZonedDateTime.now();
        Date expiration = TimeUtils.ZonedDateTimeToDate(now.plus(expire, unit));
        return generateToken(info,privateKey,expiration);
    }


    /**
     *
     * @param info  token中存储的信息
     * @param privateKey 加密所使用的私钥
     * @param expiration token到期时间
     * @return
     * @throws Exception
     */
    public static String generateToken(Object info, PrivateKey privateKey, Date expiration) throws Exception {
        Date issueTime = new Date();

        return Jwts.builder()
                .claim(DEFAULT_CLAIM_NAME, info)
                .setIssuedAt(issueTime)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }


    /**
     *
     * @param info  token中存储的信息
     * @param privateKey 加密所使用的私钥字节数组
     * @param expire token有效时长
     * @param unit token有效时长的单位
     * @return
     * @throws Exception
     */
    public static String generateToken(Object info, byte[] privateKey, int expire,ChronoUnit unit) throws Exception {
        return generateToken(info,RsaUtils.getPrivateKey(privateKey),expire,unit);
    }

    /**
     *
     * @param info  token中存储的信息
     * @param privateKey 加密所使用的私钥字节数组
     * @param expiration token到期时间
     * @return
     * @throws Exception
     */
    public static String generateToken(Object info, byte[] privateKey, Date expiration) throws Exception {
        return generateToken(info,RsaUtils.getPrivateKey(privateKey),expiration);
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, PublicKey publicKey) {
        return Jwts.parser().setSigningKey(publicKey).parseClaimsJws(token);
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥字节数组
     * @return
     * @throws Exception
     */
    private static Jws<Claims> parserToken(String token, byte[] publicKey) throws Exception {
        return parserToken(token,RsaUtils.getPublicKey(publicKey));
    }

    public static <T> TokenDTO<T> getInfoFromToken(String token, PublicKey publicKey,Class<T> type) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        T info = body.get(DEFAULT_CLAIM_NAME, type);
        Date issueTime = body.getIssuedAt();
        Date expiration = body.getExpiration();
        return new TokenDTO<>(info,issueTime,expiration);
    }


    public static <T> TokenDTO<T> getInfoFromToken(String token, byte[] publicKey,Class<T> type) throws Exception {
        return getInfoFromToken(token,RsaUtils.getPublicKey(publicKey),type);
    }

    public static Date getExpirationFromToken(String token, PublicKey publicKey) throws Exception {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return body.getExpiration();
    }

    public static void main(String[] args) throws Exception {
        String s = generateToken(new Date(), RsaUtils.getPrivateKey("D:\\11\\privateKey"), 5, ChronoUnit.DAYS);
        System.out.println(s);
        System.out.println(getInfoFromToken(s,RsaUtils.getPublicKey("D:\\11\\publicKey"),Date.class));

    }
}