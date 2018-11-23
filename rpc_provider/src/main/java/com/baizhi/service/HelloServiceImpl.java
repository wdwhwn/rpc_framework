package com.baizhi.service;

public class HelloServiceImpl implements HelloService {

    public String sayHello(String name)
    {
        System.out.println("=====================");
        return "Hello: "+name;
    }
}
