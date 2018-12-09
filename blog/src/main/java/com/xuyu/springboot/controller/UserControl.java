package com.xuyu.springboot.controller;


import com.xuyu.springboot.bean.User;
import com.xuyu.springboot.bean.Result;
import com.xuyu.springboot.bean.TypeInfo;
import com.xuyu.springboot.mapper.UserMapper;

import com.xuyu.springboot.mapper.TypeMapper;
import com.xuyu.springboot.service.EmployeeService;
import com.xuyu.springboot.utils.TestMD5;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Controller
public class UserControl {

    @Autowired
    EmployeeService employeeService;

    @Autowired
    UserMapper userMapper;

    @Autowired
    JavaMailSenderImpl mailSender;

    @Autowired
    TypeMapper typeMapper;





    @RequestMapping("/background")
    public String tobg(){return "background";}


    //登录页面
   @RequestMapping("/index")
   public String toLogin(HttpServletRequest request){
       request.setAttribute("APP_PATH",request.getContextPath());
        return "login";
   }

   @RequestMapping("/emp/{ id}")
   @ResponseBody
   public User getemp(@PathVariable("id") Integer id){
       User employee= employeeService.getEmp(id);
        return employee;
   }

//---------------------------------------------------------------------------------------------------------------
    /**
     * 获取验证码
     *
     * @param response
     * @param session
     */
    @RequestMapping("/getVerifyCode.action")
    public void generate(HttpServletResponse response, HttpSession session) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        String verifyCodeValue = drawImg(output);

        session.setAttribute("verifyCodeValue", verifyCodeValue);

        try {
            ServletOutputStream out = response.getOutputStream();
            output.writeTo(out);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 绘画验证码
     *
     * @param output
     * @return
     */
    private String drawImg(ByteArrayOutputStream output) {
        String code = "";
        // 随机产生4个字符
        for (int i = 0; i < 4; i++) {
            code += randomChar();
        }
        int width = 70;
        int height = 25;
        BufferedImage bi = new BufferedImage(width, height,
                BufferedImage.TYPE_3BYTE_BGR);
        Font font = new Font("Times New Roman", Font.PLAIN, 20);
        // 调用Graphics2D绘画验证码
        Graphics2D g = bi.createGraphics();
        g.setFont(font);
        Color color = new Color(66, 2, 82);
        g.setColor(color);
        g.setBackground(new Color(226, 226, 240));
        g.clearRect(0, 0, width, height);
        FontRenderContext context = g.getFontRenderContext();
        Rectangle2D bounds = font.getStringBounds(code, context);
        double x = (width - bounds.getWidth()) / 2;
        double y = (height - bounds.getHeight()) / 2;
        double ascent = bounds.getY();
        double baseY = y - ascent;
        g.drawString(code, (int) x, (int) baseY);
        g.dispose();
        try {
            ImageIO.write(bi, "jpg", output);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return code;
    }

    /**
     * 随机参数一个字符
     *
     * @return
     */
    private char randomChar() {
        Random r = new Random();
        String s = "ABCDEFGHJKLMNPRSTUVWXYZ0123456789";
        return s.charAt(r.nextInt(s.length()));
    }

//------------------------------------------------------------------------------------------------------
   //登录逻辑的开始
    @RequestMapping("/user")
    public String login(User user, Map<String,Object> map, HttpSession session, HttpServletRequest request,ModelMap modelMap){
       String checkcode= (String) session.getAttribute("verifyCodeValue");
       String usercode=request.getParameter("check");
       //把用户输入的密码进行加密与数据库的进行比对
        String newpassword=TestMD5.encryPassword(user.getPassword());
        user.setPassword(newpassword);
        //查询数据库
       List<User> employees = userMapper.checkLogin(user);
       if(employees.size()==0){
           map.put("err","您输入的账号或密码存在错误，请重试");
           return "login";
       }else if(employees.size()==1){
          if (checkcode.equals(usercode.toUpperCase())){
              //数据库中的正是用户，提供给以后页面的访问
              User user1=employees.get(0);
              session.setAttribute("user",user1);
              modelMap.put("userinfo",user1);
              List<TypeInfo> typeLists=typeMapper.selectAll(null);
              request.setAttribute("tlists",typeLists);
              return "background";
          }else {
              map.put("err","您输入的验证码有误");
              return "login";
          }
       }
       return null;
    }



    //前往注册页逻辑
    @RequestMapping("/topageregis")
    public String topageregis(){
        return "regis";
    }

    //向目标邮箱发送验证码逻辑
    @ResponseBody
    @RequestMapping("/mailre")
    public Result mailreP(User employee, HttpSession session){
       // System.out.println(employee);
        session.setAttribute("emp",employee);
        SimpleMailMessage message = new SimpleMailMessage();
        message.setSubject("欢迎使用邮件注册");
        String a;
        a= String.valueOf((int)((Math.random()*(9999-1000+1))+1000));
        session.setAttribute("code",a);
        message.setText(a);
        if(employee.getEmail()!=null&&employee.getEmail()!="") {
            message.setTo(employee.getEmail());
            message.setFrom("282075823@qq.com");
            mailSender.send(message);
            return Result.success().add("Msg","请前往邮箱查询收到的验证码并输入完成最后的确认~！");
        }else {
            return Result.error("请输入邮箱");
        }

       // employeeMapper.insertEmp(employee);

    }

   //邮箱中确认验证码，成功后MD5盐值加密存入数据库
    @RequestMapping("/icode")
    public String check(HttpServletRequest request,HttpSession session){
       String mycode=request.getParameter("mcode");
       if(mycode.equals(session.getAttribute("code"))){
             User employee= (User) session.getAttribute("emp");
             //在注册时对于密码进行MD5加密存储
           String newPassword=TestMD5.encryPassword(employee.getPassword());
           employee.setPassword(newPassword);
              userMapper.insertEmp(employee);
           return "login";
       }else {
           request.setAttribute("error","您的验证码存在错误");
           return "regis";}

    }

}
