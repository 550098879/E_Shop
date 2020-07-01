package org.zyx.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/** Swagger 接口文档框架 配置类
 *
 */

@EnableSwagger2
@Configuration
public class SwaggerConfig {

    @Bean
    public Docket customDocket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage("org.zyx.controller")) //需要生成接口的包所在位置
                .build();
    }

    public ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                //文档说明
                .title("E-Shop接口文档")
                .version("1.0.0")
                .description("E-Shop接口文档")
                .license("Apache 2.0")
                .build()
                ;
    }


}
