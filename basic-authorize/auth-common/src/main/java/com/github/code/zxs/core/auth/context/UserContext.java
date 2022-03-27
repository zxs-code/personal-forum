package com.github.code.zxs.core.auth.context;


















@Component
public class UserContext {
    private static TokenGlobalConfig tokenConfig;

    private static TokenResolver tokenResolver;

    @Autowired
    private UserContext(TokenGlobalConfig config, TokenResolver resolver) {
        tokenConfig = config;
        tokenResolver = resolver;
    }

    /**
     * 用户id
     */
    private static final ThreadLocal<Object> idHolder = new ThreadLocal<>();
    /**
     * 用户名
     */
    private static final ThreadLocal<String> userNameHolder = new ThreadLocal<>();
    /**
     * 用户注册时间
     */
    private static final ThreadLocal<Date> createTimeHolder = new ThreadLocal<>();
    /**
     * 用户角色
     */
    private static final ThreadLocal<Set<BaseRole>> roleHolder = new ThreadLocal<>();

    public static Object getId() {
        return idHolder.get();
    }

    public static void setId(Object id) {
        idHolder.set(id);
    }

    public static String getUsername() {
        return userNameHolder.get();
    }

    public static void setUsername(String username) {
        userNameHolder.set(username);
    }

    public static Date getCreateTime() {
        return createTimeHolder.get();
    }

    public static void setCreateTime(Date createTime) {
        createTimeHolder.set(createTime);
    }

    public static Set<BaseRole> getRole() {
        return roleHolder.get();
    }

    public static void setRole(Set<BaseRole> roles) {
        roleHolder.set(roles);
    }

    public static void clear() {
        idHolder.remove();
        userNameHolder.remove();
        createTimeHolder.remove();
        roleHolder.remove();
    }

    public static void initUserInfo(String accessToken) {
        TokenUserDTO userDTO = Optional.ofNullable(tokenResolver.resolve(accessToken)).map(TokenInfoDTO::getUserDTO).orElse(new TokenUserDTO());
        UserContext.setId(userDTO.getId());
        UserContext.setUsername(userDTO.getUsername());
        UserContext.setCreateTime(userDTO.getCreateTime());
        UserContext.setRole(userDTO.getRoles());
    }

    public static void login(LoginInfoDTO loginInfoDTO, boolean rememberMe) {
        Cookie access = HttpUtils.getCookie(tokenConfig.getAccessCookieName(), loginInfoDTO.getAccessToken(), tokenConfig.getAccessCookieDomain(), tokenConfig.getAccessCookiePath(),
                rememberMe ? tokenConfig.getAccessCookieMaxAge().intValue() : -1);
        Cookie refresh = HttpUtils.getCookie(tokenConfig.getRefreshCookieName(), loginInfoDTO.getRefreshToken(), tokenConfig.getRefreshCookieDomain(), tokenConfig.getRefreshCookiePath(),
                rememberMe ? tokenConfig.getRefreshCookieMaxAge().intValue() : -1);
        HttpServletResponse response = HttpContext.getHttpServletResponse();
        response.addCookie(access);
        response.addCookie(refresh);
    }
}
