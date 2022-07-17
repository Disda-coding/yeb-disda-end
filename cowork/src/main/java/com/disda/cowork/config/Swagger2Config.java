package com.disda.cowork.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.*;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

/**

/**
 * @program: cowork-back
 * @description: Swagger2配置类
 * @author: Disda
 * @create: 2022-01-24 16:22
 */
@Configuration
@EnableSwagger2
public class Swagger2Config {
    @Bean
    public Docket createRestApi(){
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.disda.cowork.controller"))
                .paths(PathSelectors.any())
                .build()
                .securityContexts(securityContexts())
                .securitySchemes(securitySchemes());
    }

    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title("协同办公")
                .description("协同办公文档")
                .contact(new Contact("disda","http:localhost:8081/doc.html","497457669@qq.com"))
                .version("1.0")
                .build();
    }
    private List<ApiKey> securitySchemes(){
        //设置请求头消息
        List<ApiKey> result = new ArrayList<>();
        ApiKey apiKey = new ApiKey("Authorization", "Authorization", "Header");
        result.add(apiKey);
        return result;
    }

    private List<SecurityContext> securityContexts(){
        //设置需要登录认证的路径
        List<SecurityContext> result = new ArrayList<>();
        result.add(getContextByPath("/hello/.*"));
        return result;
    }
    private SecurityContext getContextByPath(String path) {
        return SecurityContext.builder()
                //默认授权
                .securityReferences(defaultAuth())
                .forPaths(PathSelectors.regex(path))
                .build();
    }
    //默认授权
    private List<SecurityReference> defaultAuth() {
        List<SecurityReference> result = new ArrayList<>();
        //授权范围(全局)
        AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything");
        AuthorizationScope[] authorizationScopes = new AuthorizationScope[1];
        authorizationScopes[0] = authorizationScope;
        result.add(new SecurityReference("Authorizeation",authorizationScopes));
        return result;
    }
}