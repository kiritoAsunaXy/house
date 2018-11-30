package com.xuyu.house_common;

import com.xuyu.house_common.result.ResultMsg;

public class Testasurl {
    public static void main(String args[]){
        System.out.println(ResultMsg.successMsg("你好啊").asUrlParams());

    }
}
