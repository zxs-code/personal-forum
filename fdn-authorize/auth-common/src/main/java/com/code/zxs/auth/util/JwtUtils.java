package com.code.zxs.auth.util;

import io.jsonwebtoken.*;

import java.security.PrivateKey;
import java.security.PublicKey;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

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
    public static String generateToken(Object info, byte[] privateKey, Date expiration) throws Exception {
        return generateToken(info, RsaUtils.getPrivateKey(privateKey), expiration);
    }

    /**
     * @param claimMap   token中存储的键值对信息
     * @param privateKey 加密所使用的私钥
     * @param expire     token有效时长
     * @param unit       token有效时长的单位
     * @return
     * @throws Exception
     */
    public static String generateToken(Map<String, Object> claimMap, PrivateKey privateKey, int expire, ChronoUnit unit) {
        ZonedDateTime now = ZonedDateTime.now();
        Date expiration = TimeUtils.ZonedDateTimeToDate(now.plus(expire, unit));
        return generateToken(claimMap, privateKey, expiration);
    }

    /**
     * @param claimMap   token中存储的键值对信息
     * @param privateKey 加密所使用的私钥
     * @param expiration token到期时间
     * @return
     * @throws Exception
     */
    public static String generateToken(Map<String, Object> claimMap, PrivateKey privateKey, Date expiration) {
        Date issueTime = new Date();

        return Jwts.builder()
                .addClaims(claimMap)
                .setIssuedAt(issueTime)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
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

    /**
     * 获取token中存储的信息，token过期会抛出 {@link ExpiredJwtException}
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @param type      token中存储信息的类型
     * @return
     */
    public static <T> T getInfoFromToken(String token, PublicKey publicKey, Class<T> type) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        T info = body.get(DEFAULT_CLAIM_NAME, type);
        return info;
    }

    /**
     * 获取token中存储的信息，token过期会抛出 {@link ExpiredJwtException}
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥字节数组
     * @param type      token中存储信息的类型
     * @return
     */
    public static <T> T getInfoFromToken(String token, byte[] publicKey, Class<T> type) throws Exception {
        return getInfoFromToken(token, RsaUtils.getPublicKey(publicKey), type);
    }

    /**
     * 获取过期的token中存储的信息
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @param type      token中存储信息的类型
     * @return
     */
    public static <T> T getInfoFromExpiredToken(String token, byte[] publicKey, Class<T> type) throws Exception {
        return getInfoFromExpiredToken(token, RsaUtils.getPublicKey(publicKey), type);
    }

    /**
     * 获取过期的token中存储的信息
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @param type      token中存储信息的类型
     * @return
     */
    public static <T> T getInfoFromExpiredToken(String token, PublicKey publicKey, Class<T> type) {
        return getInfoFromExpiredToken(token, DEFAULT_CLAIM_NAME, publicKey, type);
    }

    /**
     * 获取过期的token中存储的信息
     *
     * @param token     用户请求中的token
     * @param claimName 信息的key
     * @param publicKey 公钥
     * @param type      token中存储信息的类型
     * @return
     */
    public static <T> T getInfoFromExpiredToken(String token, String claimName, byte[] publicKey, Class<T> type) throws Exception {
        return getInfoFromExpiredToken(token, claimName, RsaUtils.getPublicKey(publicKey), type);
    }

    /**
     * 获取过期的token中存储的信息
     *
     * @param token     用户请求中的token
     * @param claimName 信息的key
     * @param publicKey 公钥
     * @param type      token中存储信息的类型
     * @return
     */
    public static <T> T getInfoFromExpiredToken(String token, String claimName, PublicKey publicKey, Class<T> type) {
        try {
            Jws<Claims> claimsJws = parserToken(token, publicKey);
            Claims body = claimsJws.getBody();
            T info = body.get(claimName, type);
            return info;
        } catch (ExpiredJwtException e) {
            return e.getClaims().get(DEFAULT_CLAIM_NAME, type);
        }
    }


    /**
     * 获取token的过期时间，token过期会抛出 {@link ExpiredJwtException}
     * @param token
     * @param publicKey
     * @return
     */
    public static Date getExpirationFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return body.getExpiration();
    }

    /**
     * 获取过期token的过期时间
     * @param token
     * @param publicKey
     * @return
     */
    public static Date getExpirationFromExpiredToken(String token, PublicKey publicKey) {
        try {
            Jws<Claims> claimsJws = parserToken(token, publicKey);
            Claims body = claimsJws.getBody();
            return body.getExpiration();
        } catch (ExpiredJwtException e) {
            return e.getClaims().getExpiration();
        }
    }
}