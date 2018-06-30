package com.nowcoder;

import com.nowcoder.model.*;
import com.nowcoder.util.JiedisAdapterTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class jedisTests {

   @Autowired
    JiedisAdapterTest jiedisAdapterTest;

    @Test
    public void initData() {
//        User user = new User();
//        user.setName("张三");
//        jiedisAdapterTest.setObject("张三",user);

//        System.out.println(new Jedis().get("张三"));




    }
}
