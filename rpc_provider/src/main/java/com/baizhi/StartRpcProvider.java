package com.baizhi;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

public class StartRpcProvider {

    public static void main(String[] args) throws IOException {
        ClassPathXmlApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:applicationContext-provider.xml");

        applicationContext.start();

        System.in.read();
    }
}
