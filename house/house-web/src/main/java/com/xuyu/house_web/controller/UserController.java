package com.xuyu.house_web.controller;

import com.xuyu.house_biz.service.UserService;
import com.xuyu.house_common.constants.CommonConstants;
import com.xuyu.house_common.model.User;
import com.xuyu.house_common.result.ResultMsg;
import com.xuyu.house_common.utils.HashUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @RequestMapping("user")
    public List<User> getUsers(){
        return userService.getUser();
    }

//    @RequestMapping("index")
//    public String index(){
//        return "homepage/index";
//    }
    /*
    * 1.注册验证 2.发送邮件 3.注册验证失败就重定向到注册页面
    *
    * 注册页面获取:根据account对象为依据判断是否注册页获取请求
    * */
    @RequestMapping("/accounts/register")
    public String accountsRegister(User account, ModelMap modelMap){
       if(account==null||account.getName()==null){
           return "user/accounts/register";
       }
       //用户验证
        ResultMsg resultMsg=UserHelper.validate(account);
       if(resultMsg.isSuccess()&&userService.addAccount(account)){
           modelMap.put("email", account.getEmail());
           return "user/accounts/registerSubmit";
       }else {
           return "redirect:/accounts/register?"+resultMsg.asUrlParams();
       }

    }

    @RequestMapping("accounts/verify")
    public String verify(String key){
        boolean result=userService.enable(key);
        if(result){
            return "redirect:/index?"+ResultMsg.successMsg("激活成功").asUrlParams();
        }else {
            return "redirect:/accounts/register?"+ResultMsg.errorMsg("激活失败，请确认链接是否过期");
        }
    }


    //==============================================================================================================
    /*
    * 登录流程
    * */

    @RequestMapping("/accounts/signin")
    public String signin(HttpServletRequest req){
        String username=req.getParameter("username");
        String password=req.getParameter("password");
        String target=req.getParameter("target");
        if(username==null||password==null){
            req.setAttribute("tatget",target);
            return "user/accounts/signin";
        }
       User user= userService.auth(username,password);
        if(user==null){
            return "redirect:/account/signin?"+"target="+target+"&username="+username+"&"+ResultMsg.errorMsg("用户名或密码错误").asUrlParams();
        }else {
            HttpSession session=req.getSession(true);
            session.setAttribute(CommonConstants.USER_ATTRIBUTE,user);
            session.setAttribute(CommonConstants.PLAIN_USER_ATTRIBUTE,user);
            return StringUtils.isNoneBlank(target)? "redirect:"+target:"redirect:/index";
        }
    }


    /*
    * 登出，删除session里的内容
    *
    *
    * */

    @RequestMapping("accounts/logout")
    public  String logout(HttpServletRequest request){
        HttpSession session=request.getSession();
        session.invalidate();
        return "redirect:/index";
    }



    //====================================================================================================
    /*
    *
    *
    * 个人信息页面的展示
    *
    * 1.展示
    * 2.更新信息
    * */

    @RequestMapping("accounts/profile")
    public String profile(User updateUser,ModelMap modelMap,HttpServletRequest request){

        if(updateUser.getEmail()==null){
            return "user/accounts/profile";
        }
        userService.updateUser(updateUser,updateUser.getEmail());
        User query=new User();
        query.setEmail(updateUser.getEmail());
        List<User> users=userService.getUserByQuery(query);
        request.getSession(true).setAttribute(CommonConstants.USER_ATTRIBUTE,users.get(0));
        return "redirect:/accounts/profile?"+ResultMsg.successMsg("更新成功").asUrlParams();
    }

    /**
     * 修改密码操作
     *
     * @param email
     * @param password
     * @param newPassword
     * @param confirmPassword
     * @param mode
     * @return
     */
    @RequestMapping("accounts/changePassword")
    public String changePassword(String email, String password, String newPassword,
                                 String confirmPassword, ModelMap mode) {
        User user = userService.auth(email, password);
        if (user == null || !confirmPassword.equals(newPassword)) {
            return "redirct:/accounts/profile?" + ResultMsg.errorMsg("密码错误").asUrlParams();
        }
        User updateUser = new User();
        updateUser.setPasswd(HashUtils.encryPassword(newPassword));
        userService.updateUser(updateUser, email);
        return "redirect:/accounts/profile?" + ResultMsg.successMsg("更新成功").asUrlParams();
    }




}
