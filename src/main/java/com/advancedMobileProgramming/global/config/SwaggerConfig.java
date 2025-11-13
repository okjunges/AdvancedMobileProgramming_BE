package com.advancedMobileProgramming.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import java.util.ArrayList;

@Configuration
public class SwaggerConfig {

    public SwaggerConfig(MappingJackson2HttpMessageConverter converter) {
        var supportedMediaTypes = new ArrayList<>(converter.getSupportedMediaTypes());
        supportedMediaTypes.add(new MediaType("application", "octet-stream"));
        converter.setSupportedMediaTypes(supportedMediaTypes);
    }

    @Bean
    public OpenAPI openAPI(){
        // API 기본 설정
        Info info = new Info()
                .title("Advanced Mobile Programming API Document")
                .version("1.0")
                .description(
                        "환영합니다! 고급모바일프로그래밍 수업에서 진행한 프로젝트\n실시간 기자재 인식 및 정보 제공 기능을 통한 기자재 관리 시스템 서비스입니다\n"
                );

        // JWT 인증 방식 설정
        String jwtSchemeName = "jwtAuth";
        String refreshSchemeName = "refreshAuth";
        SecurityRequirement securityRequirement = new SecurityRequirement().addList(jwtSchemeName);
        Components components = new Components()
                .addSecuritySchemes(jwtSchemeName, new SecurityScheme()
                        .name("Authorization")
                        .type(SecurityScheme.Type.HTTP)
                        .scheme("bearer")
                        .bearerFormat("JWT")
                        .in(SecurityScheme.In.HEADER))
                .addSecuritySchemes(refreshSchemeName, new SecurityScheme()
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