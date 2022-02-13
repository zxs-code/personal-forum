package com.code.zxs.auth.util;

import com.code.zxs.auth.config.JwtConfig;
import io.jsonwebtoken.*;

import java.security.Key;
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
                .claim(DEFAULT_CLAIM_NAME, JsonUtils.serialize(info))
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

        //jjwt无法自动转换复杂类型，手动转成jsonx形式
        //JJWT only converts simple String, Date, Long, Integer, Short and Byte types automatically
        for (Map.Entry<String, Object> entry : claimMap.entrySet()) {
            entry.setValue(JsonUtils.serialize(entry.getValue()));
        }
        return Jwts.builder()
                .addClaims(claimMap)
                .setIssuedAt(issueTime)
                .setExpiration(expiration)
                .signWith(SignatureAlgorithm.RS256, privateKey)
                .compact();
    }


    /**
     * 密钥解析token
     *
     * @param token 用户请求中的token
     * @param key   密钥
     * @return
     * @throws Exception
     */
    public static Jws<Claims> parserToken(String token, Key key) {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token);
    }

    /**
     * 公钥解析token
     *
     * @param token     用户请求中的token
     * @param publicKey 公钥
     * @return
     * @throws Exception
     */
    public static Jws<Claims> parserToken(String token, PublicKey publicKey) {
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
    public static Jws<Claims> parserToken(String token, byte[] publicKey) throws Exception {
        return parserToken(token, RsaUtils.getPublicKey(publicKey));
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

        return getInfoFromToken(token, DEFAULT_CLAIM_NAME, publicKey, type);
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
        return getInfoFromToken(token, DEFAULT_CLAIM_NAME, publicKey, type);
    }


    /**
     * 获取token中存储的信息，token过期会抛出 {@link ExpiredJwtException}
     *
     * @param token     用户请求中的token
     * @param claimName 信息的key
     * @param publicKey 公钥字节数组
     * @param type      token中存储信息的类型
     * @return
     */
    public static <T> T getInfoFromToken(String token, String claimName, byte[] publicKey, Class<T> type) throws Exception {
        return getInfoFromToken(token, claimName, RsaUtils.getPublicKey(publicKey), type);
    }

    /**
     * 获取token中存储的信息，token过期会抛出 {@link ExpiredJwtException}
     *
     * @param token     用户请求中的token
     * @param claimName 信息的key
     * @param publicKey 公钥
     * @param type      token中存储信息的类型
     * @return
     */
    public static <T> T getInfoFromToken(String token, String claimName, PublicKey publicKey, Class<T> type) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        String info = body.get(claimName, String.class);
        return JsonUtils.parse(info, type);
    }


    public static <T> T getInfoFromBody(Claims body, String claimName, Class<T> type) {
        String info = body.get(claimName, String.class);
        return JsonUtils.parse(info, type);
    }


    /**
     * 获取token的过期时间，token过期会抛出 {@link ExpiredJwtException}
     *
     * @param token
     * @param publicKey
     * @return
     */
    public static Date getExpirationFromToken(String token, PublicKey publicKey) {
        Jws<Claims> claimsJws = parserToken(token, publicKey);
        Claims body = claimsJws.getBody();
        return body.getExpiration();
    }

    public static void main(String[] args) {
        JwtConfig jwtConfig = new JwtConfig();
        jwtConfig.init();
        String token = JwtUtils.generateToken("123", jwtConfig.getPrivateKey(), 100, jwtConfig.getUnit());
        Jws<Claims> claimsJws = JwtUtils.parserToken(token, jwtConfig.getPublicKey());
        System.out.println(claimsJws.getBody().getIssuedAt());
    }

}