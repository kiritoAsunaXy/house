package com.xuyu.house_biz.mapper;

import com.xuyu.house_common.model.User;
import org.apache.ibatis.annotations.Mapper;


import java.util.List;

@Mapper
public interface UserMapper {
    public List<User> selectUsers();


    public int insert(User account);

    public int  delete(String email);

    public int  update(User updateUser);

    public List<User> selectUsersByQuery(User user);
}
