package com.xuyu.springboot.service;


import com.xuyu.springboot.bean.ArticleInfo;
import com.xuyu.springboot.mapper.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ArticleInfoService {

   @Autowired
    private ArticleMapper articleMapper;

    public List<ArticleInfo> list(Map<String,Object> param) {
        return articleMapper.list(param);
    }
}
