package com.xuyu.springboot.utils;


import com.google.common.hash.HashCode;
import com.google.common.hash.HashFunction;
import com.google.common.hash.Hashing;

import java.nio.charset.Charset;
//这是一个对密码实现MD5加密的工具类
public class TestMD5 {

    private static final HashFunction function=Hashing.md5();
    private static final String SALT = "xuyu"; //盐值加密

    public static String encryPassword(String password){
        HashCode hashCode =	function.hashString(password+SALT, Charset.forName("UTF-8"));
        return hashCode.toString();
    }

    public static void main(String[] args) {

    System.out.println(TestMD5.encryPassword("1234"));
    }
}



