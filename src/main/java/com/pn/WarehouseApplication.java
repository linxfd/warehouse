package com.pn;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//定义mapper接口的扫描器，指明mapper接口所在的包，然后会自动为mapper接口创建代理并且加入到Spring的ioc容器中
@MapperScan(basePackages = {"com.pn.mapper","com.pn.utils"})
@SpringBootApplication
public class WarehouseApplication {
    public static void main(String[] args) {
        SpringApplication.run(WarehouseApplication.class, args);
    }
}
