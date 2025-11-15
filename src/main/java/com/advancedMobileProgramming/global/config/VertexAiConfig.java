package com.advancedMobileProgramming.global.config;

import com.google.auth.oauth2.GoogleCredentials;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

@Configuration
public class VertexAiConfig {
    @Value("${vertex-ai.credentials-path}")
    private String serviceAccountPath;

    @Bean
    public GoogleCredentials vertexCredentials() throws IOException {
        return GoogleCredentials.fromStream(new FileInputStream(serviceAccountPath))
                .createScoped(List.of("https://www.googleapis.com/auth/cloud-platform"));
    }

    // 실제 HTTP 요청에 사용할 Bearer Token 을 뽑아오는 메서드로서 Vertex AI 호출할 때 무조건 이걸 써서 Bearer token 넣어야 한다
    public String getAccessToken(GoogleCredentials credentials) {
        try {
            credentials.refreshIfExpired();
            return credentials.getAccessToken().getTokenValue();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // Vertex AI REST API 를 호출하는 데 사용할 HTTP 클라이언트
    @Bean
    public RestTemplate vertexRestTemplate(GoogleCredentials vertexCredentials) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(3000);
        factory.setReadTimeout(20000);

        RestTemplate rt = new RestTemplate(factory);

        // 인터셉터 추가
        rt.getInterceptors().add((req, body, execution) -> {
            // Bearer 토큰 자동 주입
            req.getHeaders().setBearerAuth(getAccessToken(vertexCredentials));
            return execution.execute(req, body);
        });

        return rt;
    }
}