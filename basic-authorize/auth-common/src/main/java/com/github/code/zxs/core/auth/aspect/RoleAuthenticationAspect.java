package com.github.code.zxs.core.auth.aspect;

















@Slf4j
@Aspect
@Component
public class RoleAuthenticationAspect {

    @Around(value = "@annotation(role)")
    public Object around(ProceedingJoinPoint point, HasRole role) throws Throwable {
        if (role != null) {
            String[] needRoles = role.name();
            RolesStrategy strategy = role.strategy();
            BaseRole[] baseRoles = Optional.ofNullable(UserContext.getRole()).orElse(new BaseRole[0]);
            String[] userRoles = Arrays.stream(baseRoles).map(BaseRole::getName).toArray(String[]::new);

            boolean flag = strategy == RolesStrategy.ANY
                    ? ArrayUtils.containsAny(userRoles, needRoles)
                    : ArrayUtils.containsAll(userRoles, needRoles);
            if (!flag) {
                log.info("无权限访问，用户角色：{}，所需角色：{}", Arrays.toString(userRoles), Arrays.toString(needRoles));
                throw new RoleAuthenticationException(ResponseStatus.FORBIDDEN);
            }
        }
        return point.proceed();
    }
}