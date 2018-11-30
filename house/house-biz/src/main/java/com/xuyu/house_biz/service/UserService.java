package com.xuyu.house_biz.service;


import com.google.common.collect.Lists;
import com.xuyu.house_biz.mapper.UserMapper;
import com.xuyu.house_common.model.User;


import com.xuyu.house_common.utils.BeanHelper;
import com.xuyu.house_common.utils.HashUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;


@Service
public class UserService {



    @Autowired
  private UserMapper userMapper;

    @Autowired
    private FileService fileService;

    @Autowired
    private MailService mailService;

    @Value("${domain.name}")
    private String domainName;

    @Value("${file.prefix}")
    private String imgPrefix;

    public List<User> getUser() {
        return userMapper.selectUsers();
    }

    /*
    * 1.插入数据库，非激活，密码盐值加密，保存头像到本地
    * 2.生成key，绑定邮箱
    * 3.发送邮件给用户
    * */
    @Transactional(rollbackFor = Exception.class)
    public boolean addAccount(User account) {
    account.setPasswd(HashUtils.encryPassword(account.getPasswd()));
    List<String> imgList=fileService.getImgPaths(Lists.newArrayList(account.getAvatarFile()));
    if(!imgList.isEmpty()){
        account.setAvatar(imgList.get(0));
    }
        BeanHelper.setDefaultProp(account,User.class);
        BeanHelper.onInsert(account);
        account.setEnable(0);
        userMapper.insert(account);
        System.out.println("account:"+account.toString());
        mailService.registerNotify(account.getEmail());
        return true;

    }


    public boolean enable(String key) {
        return mailService.enable(key);

    }
/*
*
* 用户名密码验证操作
* */
    public User auth(String username, String password) {
        User user=new User();
        user.setEmail(username);
        user.setPasswd(HashUtils.encryPassword(password));
        user.setEnable(1);
        List<User> list=getUserByQuery(user);
        if (!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

    public List<User> getUserByQuery(User user) {
       List<User> list=userMapper.selectUsersByQuery(user);
        list.forEach(u -> {
            u.setAvatar(imgPrefix + u.getAvatar());
        });
       return list;
    }

    public void updateUser(User updateUser, String email) {
        updateUser.setEmail(email);
        BeanHelper.onUpdate(updateUser);
        userMapper.update(updateUser);
    }
}
