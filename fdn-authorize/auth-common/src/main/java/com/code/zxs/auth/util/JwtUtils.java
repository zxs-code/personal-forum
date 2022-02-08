package com.code.zxs.auth.util;

import com.code.zxs.auth.entity.User;
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
     * @param info       token中存储的信息
     * @param privateKey 加密所使用的私钥
     * @param expire     token有效时长
     * @param unit       token有效时长的单位
     * @return
     * @throws Exception
     */
    public static String generateToken(Object info, PrivateKey privateKey, int expire, ChronoUnit unit) {
        ZonedDateTime now = ZonedDateTime.now();
        Date expiration = TimeUtils.ZonedDateTimeToDate(now.plus(expire, unit));
        return generateToken(info, privateKey, expiration);
    }


    /**
     * @param info       token中存储的信息
     * @param privateKey 加密所使用的私钥
     * @param expiration token到期时间
     * @return
     * @throws Exception
     */
    public static String generateToken(Object info, PrivateKey privateKey, Date expiration) {
        Date issueTime = new Date();

        return Jwts.builder()
                .claim(DEFAULT_CLAIM_NAME, info)
                .setIssuedAt(issueTime)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }


    /**
     * @param info       token中存储的信息
     * @param privateKey 加密所使用的私钥字节数组
     * @param expire     token有效时长
     * @param unit       token有效时长的单位
     * @return
     * @throws Exception
     */
    public static String generateToken(Object info, byte[] privateKey, int expire, ChronoUnit unit) throws Exception {
        return generateToken(info, RsaUtils.getPrivateKey(privateKey), expire, unit);
    }

    /**
     * @param info       token中存储的信息
     * @param privateKey 加密所使用的私钥字节数组
     * @param expiration token到期时间
     * @return
     * @throws Exception
     */
    public static String generateToken(Object info, byte[] privateKey, Date expiration) throws Exception{
        return generateToken(info, RsaUtils.getPrivateKey(privateKey), expiration);
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
        return parserToken(token, RsaUtils.getPublicKey(publicKey));
    }

    public static User getInfoFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        User info = body.get(DEFAULT_CLAIM_NAME, User.class);
        return info;
    }


    public static User getInfoFromToken(String token, byte[] publicKey) throws Exception {
        return getInfoFromToken(token, RsaUtils.getPublicKey(publicKey));
    }

    public static Date getExpirationFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return body.getExpiration();
    }
}