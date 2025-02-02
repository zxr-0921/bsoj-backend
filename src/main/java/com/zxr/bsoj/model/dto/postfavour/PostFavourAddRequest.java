package com.zxr.bsoj.model.dto.postfavour;

import lombok.Data;

import java.io.Serializable;

/**
 * 帖子收藏 / 取消收藏请求
 */
@Data
public class PostFavourAddRequest implements Serializable {

    private static final long serialVersionUID = 1L;
    /**
     * 帖子 id
     */
    private Long postId;
}