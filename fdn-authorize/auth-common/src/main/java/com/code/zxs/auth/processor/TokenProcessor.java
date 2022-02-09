package com.code.zxs.auth.processor;

import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;

public interface TokenProcessor {
    /**
     * 构建token
     * @param info 需要存储在token中的信息
     * @return
     */
    String generateToken(Object info);

    /**
     * 构建token
     * @param info 需要存储在token中的信息
     * @param  expiration token 到期时间
     * @return
     */
    String generateToken(Object info, Date expiration);

    /**
     * 构建token
     * @param info 需要存储在token中的信息
     * @param expire token过期时长
     * @param unit token过期时长单位
     * @return
     */
    String generateToken(Object info, Integer expire, ChronoUnit unit);

    /**
     * 构建token
     * @param infoMap 需要存储在token中的信息键值对
     * @return
     */
    String generateToken(Map<String,Object> infoMap);

    /**
     * 构建token
     * @param infoMap 需要存储在token中的信息键值对
     * @param expiration token到期时间
     * @return
     */
    String generateToken(Map<String,Object> infoMap, Date expiration);

    /**
     * 构建token
     * @param infoMap 需要存储在token中的信息键值对
     * @param expire token过期时长
     * @param unit token过期时长单位
     * @return
     */
    String generateToken(Map<String,Object> infoMap, Integer expire, ChronoUnit unit);

    <T> T getInfoFromToken(String token,Class<T> type);

    <T> T getInfoFromToken(String token,String infoKey,Class<T> type);

    /**
     * 获取token的过期时间
     * @param token
     * @return
     */
    Date getTokenExpiration(String token);
}
