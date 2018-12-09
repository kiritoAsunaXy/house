package com.xuyu.springboot;

import com.xuyu.springboot.bean.User;
import com.xuyu.springboot.mapper.UserMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class Myspringbootweb01ApplicationTests {


    @Autowired
    UserMapper employeeMapper;


    @Autowired
    RedisTemplate<Object,User> empRedisTemplate;

    @Autowired
    StringRedisTemplate stringRedisTemplate;//操作字符串

    @Autowired
    RedisTemplate redisTemplate;//k，v都是对象的

    @Test
    public void contextLoads() {

    }



    /*
    * String字符串，list列表，set集合。hash散列，Zset有序集合
    *   stringRedisTemplate.opsForValue()来简化操作字符串
    *   stringRedisTemplate.opsForHash()操作Hsah以此类推
    * */
    @Test
    public void mytestredis01(){
     //   stringRedisTemplate.opsForValue().set("xuyu","123456");
       // String xuyu = stringRedisTemplate.opsForValue().get("xuyu");
        //System.out.println(xuyu);
        stringRedisTemplate.opsForList().leftPush("list01","1");
        stringRedisTemplate.opsForList().leftPush("list01","2");
    }

    @Test
    public void test02(){
        User employee=employeeMapper.getEmpById(1);
       //默认保存对象使用的是jdk序列化机制，序列化后的数据存在redis里
        //redisTemplate.opsForValue().set("emp01",employee);
        //1.将数据以json的方式保存
        //（1）自己将对象转为Json
        //（2）自己默认的序列化规则
    empRedisTemplate.opsForValue().set("emp-01",employee);
    }

}
