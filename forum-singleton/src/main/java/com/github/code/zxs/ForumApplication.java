package com.github.code.zxs;

import com.alicp.jetcache.anno.config.EnableCreateCacheAnnotation;
import com.alicp.jetcache.anno.config.EnableMethodCache;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@MapperScan(basePackages = "com.github.code.zxs.*.mapper")
@EnableMethodCache(basePackages = "com.github.code.zxs")
@EnableCreateCacheAnnotation
@EnableScheduling
public class ForumApplication {
    public static void main(String[] args) {
        SpringApplication.run(ForumApplication.class);
    }
}
