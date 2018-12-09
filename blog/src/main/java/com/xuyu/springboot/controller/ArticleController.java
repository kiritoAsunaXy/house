package com.xuyu.springboot.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.xuyu.springboot.bean.ArticleInfo;
import com.xuyu.springboot.mapper.TypeMapper;
import com.xuyu.springboot.service.ArticleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class ArticleController {

 @Autowired
 private ArticleInfoService articleInfoService;

 @Autowired
 private TypeMapper typeMapper;

    //去文章列表展示页面
    //查询所有文章（正常） 分页查询
    @RequestMapping("/toArticleEdit")
    public String list(ModelMap map,
                       @RequestParam(required = false, value = "typeId") String typeId,
                       @RequestParam(required = false, value = "startDate") String startDate,
                       @RequestParam(required = false, value = "endDate") String endDate,
                       @RequestParam(required = false, value = "keyWord") String keyWord,
                       @RequestParam(value="pageNum", defaultValue="1") int pageNum,
                       @RequestParam(value="pageSize", defaultValue="5") int pageSize) {

        Map<String, Object> param=new HashMap<>();
        param.put("typeId", typeId);
        param.put("startDate", startDate);
        param.put("endDate", endDate);
        param.put("keyWord", keyWord);
        param.put("status",1);

        //PageHelper插件
        PageHelper.startPage(pageNum, pageSize);
        List<ArticleInfo> list= articleInfoService.list(param);
        PageInfo<ArticleInfo> pageInfo = new PageInfo<ArticleInfo>(list);
        map.put("pageInfo", pageInfo);
        //查询所有的文章分类
        map.put("typeList", typeMapper.selectAll(null));

        return "articleEdit";
    }



}
