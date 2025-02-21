package com.zxr.bsoj.model.dto.aiquestion;

import lombok.Data;

import java.io.Serializable;

@Data
public class AIGenerateQuestionRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    // 题目标题
    private String title;
    // 题目类型
    private String[] type;
    // 题目难度
    private String difficulty;
}
