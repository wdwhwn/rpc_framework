package com.baizhi;

import com.baizhi.service.HelloService;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ConsumerRpcService {

    public static void main(String[] args) {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext-consumer.xml");
        HelloService helloService = (HelloService) applicationContext.getBean("proxy");
        String sayHello = helloService.sayHello("zs");
        System.out.println(sayHello);
    }
}
