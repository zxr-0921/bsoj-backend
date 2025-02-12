package com.zxr.bsoj.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.zxr.bsoj.model.entity.Posts;
import com.zxr.bsoj.service.PostsService;
import com.zxr.bsoj.mapper.PostsMapper;
import org.springframework.stereotype.Service;

/**
* @author zxr0921
* @description 针对表【posts(帖子表，用于存储用户发布的帖子信息)】的数据库操作Service实现
* @createDate 2025-02-12 10:19:59
*/
@Service
public class PostsServiceImpl extends ServiceImpl<PostsMapper, Posts>
    implements PostsService{

}




