package com.github.code.zxs.auth.util;

import com.github.code.zxs.core.util.JsonUtils;
import com.github.code.zxs.core.util.TimeUtils;
import io.jsonwebtoken.*;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class JwtUtils {

    public static final String DEFAULT_CLAIM_NAME = "info";


    /**
     * @param info      token中存储的信息
     * @param issueTime token发布时间
     * @param encodeKey 加密密钥
     * @param expire    token有效时长
     * @param unit      token有效时长的单位
     * @return
     * @throws Exception
     */
    public static String generateToken(Object info, Date issueTime, int expire, TimeUnit unit, SignatureAlgorithm algorithm, Key encodeKey) {
        Date expiration = TimeUtils.plus(issueTime, expire, unit);
        return generateToken(info, issueTime, expiration, algorithm, encodeKey);
    }


    /**
     * @param info       token中存储的信息
     * @param issueTime  token发布时间
     * @param encodeKey  加密密钥
     * @param expiration token到期时间
     * @return
     * @throws Exception
     */
    public static String generateToken(Object info, Date issueTime, Date expiration, SignatureAlgorithm algorithm, Key encodeKey) {
        return Jwts.builder()
                .claim(DEFAULT_CLAIM_NAME, JsonUtils.serialize(info))
                .setIssuedAt(issueTime)
                .setExpiration(expiration)
                .signWith(algorithm, encodeKey)
                .compact();
    }


    /**
     * @param claimMap  token中存储的键值对信息
     * @param issueTime token发布时间
     * @param encodeKey 加密密钥
     * @param expire    token有效时长
     * @param unit      token有效时长的单位
     * @return
     * @throws Exception
     */
    public static String generateToken(Map<String, Object> claimMap, Date issueTime, int expire, TimeUnit unit, SignatureAlgorithm algorithm, Key encodeKey) {
        Date expiration = TimeUtils.plus(issueTime, expire, unit);
        return generateToken(claimMap, issueTime, expiration, algorithm, encodeKey);
    }

    /**
     * @param claimMap   token中存储的键值对信息
     * @param issueTime  token发布时间
     * @param algorithm  加密算法
     * @param encodeKey  加密密钥
     * @param expiration token到期时间
     * @return
     * @throws Exception
     */
    public static String generateToken(Map<String, Object> claimMap, Date issueTime, Date expiration, SignatureAlgorithm algorithm, Key encodeKey) {
        //jjwt无法自动转换复杂类型，手动转成jsonx形式
        //JJWT only converts simple String, Date, Long, Integer, Short and Byte types automatically
        for (Map.Entry<String, Object> entry : claimMap.entrySet()) {
            entry.setValue(JsonUtils.serialize(entry.getValue()));
        }
        return Jwts.builder()
                .addClaims(claimMap)
                .setIssuedAt(issueTime)
                .setExpiration(expiration)
                .signWith(algorithm, encodeKey)
                .compact();
    }


    /**
     * 密钥解析token
     *
     * @param token     用户请求中的token
     * @param decodeKey 解密密钥
     * @return
     * @throws Exception
     */
    public static Jws<Claims> parserToken(String token, Key decodeKey) {
        return Jwts.parser().setSigningKey(decodeKey).parseClaimsJws(token);
    }


    /**
     * 获取token中存储的信息，token过期会抛出 {@link ExpiredJwtException}
     *
     * @param token     用户请求中的token
     * @param decodeKey 解密密钥
     * @param type      token中存储信息的类型
     * @return
     */
    public static <T> T getInfoFromToken(String token, Key decodeKey, Class<T> type) {
        return getInfoFromToken(token, DEFAULT_CLAIM_NAME, decodeKey, type);
    }


    /**
     * 获取token中存储的信息，token过期会抛出 {@link ExpiredJwtException}
     *
     * @param token     用户请求中的token
     * @param claimName 信息的key
     * @param decodeKey 解密密钥
     * @param type      token中存储信息的类型
     * @return
     */
    public static <T> T getInfoFromToken(String token, String claimName, Key decodeKey, Class<T> type) {
        Jws<Claims> claimsJws = parserToken(token, decodeKey);
        Claims body = claimsJws.getBody();
        String info = body.get(claimName, String.class);
        return JsonUtils.parse(info, type);
    }

    /**
     * 获取token的过期时间，token过期会抛出 {@link ExpiredJwtException}
     *
     * @param token
     * @param decodeKey
     * @return
     */
    public static Date getExpirationFromToken(String token, Key decodeKey) {
        Jws<Claims> claimsJws = parserToken(token, decodeKey);
        Claims body = claimsJws.getBody();
        return body.getExpiration();
    }
}