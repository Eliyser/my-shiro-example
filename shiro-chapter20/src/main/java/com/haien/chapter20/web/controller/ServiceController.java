package com.haien.chapter20.web.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ServiceController {
    @RequestMapping("/hello")
    public String hello(String[] param1,String param2){
        return "hello"+param1[0]+param2;
    }
}
