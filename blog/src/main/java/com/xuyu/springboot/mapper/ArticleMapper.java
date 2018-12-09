package com.xuyu.springboot.mapper;

import com.xuyu.springboot.bean.ArticleInfo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;
import java.util.Map;

@Mapper
public interface ArticleMapper {


    List<ArticleInfo> list(Map<String,Object> param);
}
