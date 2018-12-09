package com.xuyu.springboot.mapper;

import com.xuyu.springboot.bean.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {


    public List<User> checkLogin(User employee);

    public User getEmpById(Integer id);

    public  void  insertEmp(User employee);

    public void updateEmp(User employee);
}
