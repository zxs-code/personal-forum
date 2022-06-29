package com.github.code.zxs.resource.model.bo;

import com.github.code.zxs.core.component.CommentCursor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDataBO {
    private CommentCursor cursor;
    private List<CommentBO> comments;
}
