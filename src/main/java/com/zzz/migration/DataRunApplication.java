package com.zzz.migration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * @author zhangzhongzhen
 */
@ComponentScan("com.zzz")
@EnableSwagger2
@SpringBootApplication(exclude= {DataSourceAutoConfiguration.class})
public class DataRunApplication {

    public static void main(String[] args) {
        SpringApplication.run(DataRunApplication.class, args);
    }

}
