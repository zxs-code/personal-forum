package com.github.code.zxs.resource.model.bo;


import com.github.code.zxs.core.component.Cursor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReplyDataBO {
    private Cursor cursor;
    private List<ReplyBO> replies;
}
