package com.zxr.bsoj.judge.codesandbox.impl;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.zxr.bsoj.common.ErrorCode;
import com.zxr.bsoj.exception.BusinessException;
import com.zxr.bsoj.judge.codesandbox.CodeSandbox;
import com.zxr.bsoj.judge.codesandbox.model.ExecuteCodeRequest;
import com.zxr.bsoj.judge.codesandbox.model.ExecuteCodeResponse;
import org.apache.commons.lang3.StringUtils;

/**
 * 远程代码沙箱（实际调用接口的沙箱）
 */
public class RemoteCodeSandbox implements CodeSandbox {

    // 定义鉴权请求头和密钥
    private static final String AUTHREQUESTHEADER = "7D6507E83D4EFFD42BCDC88753402B06";     // 请求密钥鉴权常量
    private static final String AUTHREQUESTSECRET = "1BE6C9847772AF22C041AA082F228D44";

    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        String url = "http://119.29.238.15:8090/executeCode";
        String json = JSONUtil.toJsonStr(executeCodeRequest);
        String responseStr = HttpUtil.createPost(url)
                .header(AUTHREQUESTHEADER, AUTHREQUESTSECRET)
                .body(json)
                .execute()
                .body();
        if (StringUtils.isBlank(responseStr)) {
            throw new BusinessException(ErrorCode.API_REQUEST_ERROR, "executeCode remoteSandbox error, message = " + responseStr);
        }
        return JSONUtil.toBean(responseStr, ExecuteCodeResponse.class);
    }
}
