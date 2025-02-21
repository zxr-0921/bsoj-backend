package com.zxr.bsoj.model.dto.aiquestion;

import lombok.Data;

import java.io.Serializable;

@Data
public class AIQuestionAnalysisRequest implements Serializable {
    private static final long serialVersionUID = 1L;
    private String title;
    private String content;
}
