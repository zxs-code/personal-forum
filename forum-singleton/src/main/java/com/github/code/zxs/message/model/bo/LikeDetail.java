package com.github.code.zxs.message.model.bo;

import com.github.code.zxs.core.component.RangeResult;
import com.github.code.zxs.message.model.entity.Remind;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class LikeDetail extends RangeResult<LikeItem> {
    private Remind remind;
}
