package com.github.code.zxs.resource.support.result;

import com.github.code.zxs.resource.model.entity.Like;
import com.github.code.zxs.resource.model.enums.LikeStateEnum;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LikeResult {
    private Boolean success;
    private LikeStateEnum oldState;
    private LikeStateEnum newState;
    private Like like;

    public boolean isChange() {
        return oldState != newState;
    }
}
