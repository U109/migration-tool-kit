package com.zzz.migration.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.builders.RequestParameterBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.ParameterType;
import springfox.documentation.service.RequestParameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhangzhongzhen
 */
@Configuration
public class SwaggerConfig {

    public static final String API_PREFIX_MIGRATION = "/migration";
    private static final String API_DEFAULT_PACKAGE = "com.zzz.migration.controller";

    private ApiInfo createApiInfo() {
        return new ApiInfoBuilder()
                .title("服务API文档")
                .description("在线API文档")
                .version("1.0")
                .contact(new Contact("zhangzhongzhen", "", "1098598203@qq.com"))
                .build();
    }

    @Bean(value = "publicApi")
    public Docket publicApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(createApiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(API_DEFAULT_PACKAGE))
                .paths(PathSelectors.any())
                .build()
                .ignoredParameterTypes(HttpServletResponse.class, HttpServletRequest.class);
    }
}