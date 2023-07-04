package com.zzz.migrationtoolkit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.zzz")
public class MigrationToolkitApplication {

    public static void main(String[] args) {
        SpringApplication.run(MigrationToolkitApplication.class, args);
    }

}
