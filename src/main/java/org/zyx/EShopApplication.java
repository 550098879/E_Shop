package org.zyx;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("org.zyx.mapper")
public class EShopApplication {

    public static void main(String[] args) {
        SpringApplication.run(EShopApplication.class,args);
    }

}
