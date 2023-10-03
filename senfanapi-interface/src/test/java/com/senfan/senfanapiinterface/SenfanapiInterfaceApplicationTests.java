package com.senfan.senfanapiinterface;

import com.senfan.senfanapiclientsdk.client.SenfanAPIClient;
import com.senfan.senfanapiclientsdk.model.User;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
class SenfanapiInterfaceApplicationTests {
    @Resource
    private SenfanAPIClient senfanAPIClient;
    @Test
    void contextLoads() {
    }

}
