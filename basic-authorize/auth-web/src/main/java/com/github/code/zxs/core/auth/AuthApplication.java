package com.github.code.zxs.core.auth;









@SpringBootApplication(exclude = {SecurityAutoConfiguration.class})
/**
 * 扫描核心包下的组件
 */
@ComponentScan(basePackages = {
        "com.github.code.zxs.auth",
        "com.github.code.zxs.core"
})
@MapperScan("com.github.code.zxs.auth.mapper")
@ServletComponentScan
@EnableFeignClients
public class AuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(AuthApplication.class);
    }
}
