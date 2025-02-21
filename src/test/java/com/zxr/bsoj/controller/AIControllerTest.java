package com.zxr.bsoj.controller;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class AIControllerTest {
    @Resource
    private AIController aiController;

    @Test
    void getAnswer() {
    }

    @Test
    void codeOptimize() {
//        String s = aiController.codeOptimize("import java.io.*; import java.util.*;  public class Main {     public static void main(String args[]) throws Exception {         Scanner cin = new Scanner(System.in);         int a = cin.nextInt(), b = cin.nextInt();         System.out.println(a + b);     } }");
//        System.out.println("优化后的代码" + s);
    }

//    @Test
//    void judge() {
//        BaseResponse<JSONObject> judge = aiController.judge("import java.io.*; import java.util.*;  public class Main {     public static void main(String args[]) throws Exception {         Scanner cin = new Scanner(System.in);         int a = cin.nextInt(), b = cin.nextInt();         System.out.println(a + b);     } }",
//                "50000000 4000000");
//        System.out.println("程序运行的结果" + judge);
//    }
}