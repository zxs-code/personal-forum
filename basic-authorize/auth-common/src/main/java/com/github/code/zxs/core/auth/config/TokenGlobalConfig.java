package com.github.code.zxs.core.auth.config;










@Getter
@Setter
@Configuration
@Slf4j
@ConfigurationProperties(prefix = "global.config.auth.token")
public class TokenGlobalConfig {
    /**
     * 使用的令牌模型，默认为 uuid
     */
    private TokenModel tokenModel = TokenModel.UUID;
    /**
     * 是否开启友好过期，将refreshToken过期设置在当天凌晨3点
     */
    private boolean friendlyExpire = false;
    /**
     * accessToken有效时长
     */
    private Integer accessTokenExpire = 120;
    /**
     * refreshToken有效时长
     */
    private Integer refreshTokenExpire = 30;
    /**
     * access_token有效时长单位
     */
    private TimeUnit accessUnit = TimeUnit.MINUTES;
    /**
     * refresh_token有效时长单位
     */
    private TimeUnit refreshUnit = TimeUnit.DAYS;
    /**
     * access_token在cookie中的名字
     */
    private String accessCookieName = "access_token";
    /**
     * refresh_token在cookie中的名字
     */
    private String refreshCookieName = "refresh_token";
    /**
     * access_token在cookie中的名字
     */
    private String accessCookieDomain = "localhost";
    /**
     * refresh_token在cookie中的名字
     */
    private String refreshCookieDomain = "localhost";
    /**
     * access_token cookie Path,Path及子路径会携带该cookie
     */
    private String accessCookiePath = "/";
    /**
     * refresh_token cookie Path,Path及子路径会携带该cookie
     */
    private String refreshCookiePath = "/basic-authorize/auth/refresh";

    /**
     * access_token cookie有效时长,单位：秒。
     * 与token默认过期时间匹配，不随accessTokenExpire改变而改变(配置文件属性注入在初始化之后)。
     */
    private Long accessCookieMaxAge = accessUnit.toSeconds(accessTokenExpire);    //默认cookie保存时间为2小时

    /**
     * refresh_token cookie有效时长,单位：秒
     * 与token默认过期时间匹配，不随refreshTokenExpire改变而改变(配置文件属性注入在初始化之后)。
     */
    private Long refreshCookieMaxAge = refreshUnit.toSeconds(refreshTokenExpire);    //默认cookie保存时间为30天

}
