/* (C)2022 */
package com.advendio.marketplaceborderlyservice.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Value("${app.swagger.host-name}")
    private String swaggerHostName;

    @Bean
    public Docket productApi() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .apis(
                        RequestHandlerSelectors.basePackage(
                                "com.advendio.marketplaceborderlyservice.controller"))
                .paths(PathSelectors.any())
                .build()
                .host(StringUtils.defaultIfBlank(swaggerHostName, null))
                .apiInfo(metaData())
                .useDefaultResponseMessages(false);
    }

    private ApiInfo metaData() {
        return new ApiInfoBuilder()
                .title("SWAGGER Bolderly")
                .description("SWAGGER API Integrate with Sale-force")
                .version("1.0")
                .build();
    }
}
