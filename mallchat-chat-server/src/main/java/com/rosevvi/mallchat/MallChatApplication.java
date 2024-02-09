package com.rosevvi.mallchat;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author: rosevvi
 * @date: 2024/2/8 22:49
 * @version: 1.0
 * @description:
 */
@SpringBootApplication(scanBasePackages = {"com.rosevvi.mallchat"})
public class MallChatApplication {
    public static void main(String args[]){
        SpringApplication.run(MallChatApplication.class);
    }
}
