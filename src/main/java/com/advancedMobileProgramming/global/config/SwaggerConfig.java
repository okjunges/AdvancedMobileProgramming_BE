package com.advancedMobileProgramming.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI(){
        // API 기본 설정
        Info info = new Info()
                .title("rightMark API Document")
                .version("1.0")
                .description(
                        "환영합니다! [rightMark]는 공모 기반 창작 아이디어 디지털 증명 플랫폼입니다.\n"
                );

        // JWT 인증 방식 설정
        String jwtSchemeName = "jwtAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER));

        return new OpenAPI()
                .addServersItem(new Server().url("http://localhost:8080")) // 추가적인 서버 URL 설정 가능
                .info(info)
                .addSecurityItem(securityRequirement)
                .components(components);
    }
}