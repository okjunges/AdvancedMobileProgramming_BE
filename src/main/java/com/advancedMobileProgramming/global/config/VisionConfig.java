package com.advancedMobileProgramming.global.config;

import com.advancedMobileProgramming.global.util.vision.VisionProperties;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.cloud.visionai.v1.WarehouseClient;
import com.google.cloud.visionai.v1.WarehouseSettings;
import com.google.cloud.vision.v1.ProductSearchClient;
import com.google.cloud.vision.v1.ProductSearchSettings;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.FileInputStream;
import java.io.IOException;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(VisionProperties.class)
public class VisionConfig {
    private final VisionProperties visionProperties;

    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(visionProperties.getCredentialsPath()));
        StorageOptions options = StorageOptions.newBuilder()
                .setCredentials(credentials)
                .setProjectId(visionProperties.getProjectId())
                .build();
        return options.getService();
    }

    @Bean
    public WarehouseClient warehouseClient() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(visionProperties.getCredentialsPath()));

        WarehouseSettings settings = WarehouseSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();

        return WarehouseClient.create(settings);
    }

    /* Google Cloud Vision Product Search 용으로 지금은 유지보수로 인해 사용 불가능...
    @Bean
    public Storage storage() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(visionProperties.getCredentialsPath()));
        StorageOptions options = StorageOptions.newBuilder().setCredentials(credentials).build();
        return options.getService();
    }

    @Bean
    public ProductSearchClient productSearchClient() throws IOException {
        GoogleCredentials credentials = GoogleCredentials.fromStream(new FileInputStream(visionProperties.getCredentialsPath()));
        ProductSearchSettings productSearchSettings = ProductSearchSettings.newBuilder()
                .setCredentialsProvider(FixedCredentialsProvider.create(credentials))
                .build();
        return ProductSearchClient.create(productSearchSettings);
    }
    */
}