package com.biyao.config;

import com.google.common.collect.Lists;
import lombok.Generated;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.List;

/**
 * swagger配置
 *
 * @author: hxs
 * @create: 2020/4/10
 */
@Configuration
@ConditionalOnProperty(prefix = "spring.swagger", name = "enable")
@EnableSwagger2
@Generated
public class SwaggerConfig {

    @Bean
    public Docket createRestApi() {
        ParameterBuilder aParameterBuilder = new ParameterBuilder();
        aParameterBuilder
                .name("tenant_code")
                .description("租户ID")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .defaultValue("default")
                .required(false).build();
        List<Parameter> aParameters = Lists.newArrayList();
        aParameters.add(aParameterBuilder.build());
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(apiInfo())
                .globalOperationParameters(aParameters)
                .select()
                .apis(RequestHandlerSelectors.basePackage("com.biyao"))
                .paths(PathSelectors.any()).build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfoBuilder()
                .title("ema平台RESTful APIs")
                .description("ema平台")
                .version("1.0").build();
    }
}
