package com.github.code.zxs.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RedisCellResult {
    /**
     * 是否允许该操作
     */
    private Boolean allow;
    /**
     * 最大瞬时令牌
     */
    private Long maxBurst;
    /**
     * 剩余的令牌数量
     */
    private Long remain;
    /**
     * 若请求被拒绝，等待多少秒后可以重试
     */
    private Long waitToRetry;
    /**
     * 等待多少秒后，令牌桶的令牌达到上限
     */
    private Long waitToLimit;

}
