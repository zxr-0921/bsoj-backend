package com.zxr.bsoj.service;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zxr.bsoj.model.dto.posts.QueryPostRequest;
import com.zxr.bsoj.model.entity.Post;
import com.zxr.bsoj.model.entity.Posts;
import com.baomidou.mybatisplus.extension.service.IService;
import com.zxr.bsoj.model.vo.PostVO;
import com.zxr.bsoj.model.vo.PostsVO;

import javax.servlet.http.HttpServletRequest;

/**
* @author zxr0921
* @description 针对表【posts(帖子表，用于存储用户发布的帖子信息)】的数据库操作Service
* @createDate 2025-02-12 10:19:59
*/
public interface PostsService extends IService<Posts> {
    /**
     * 校验
     *
     * @param post
     * @param add
     */
    void validPost(Posts post, boolean add);

    QueryWrapper<Posts> getQueryWrapper(QueryPostRequest queryPostRequest);

    Page<PostsVO> getPostVOPage(Page<Posts> page, HttpServletRequest request);

    PostsVO getPostVO(Posts post, HttpServletRequest request);
}
