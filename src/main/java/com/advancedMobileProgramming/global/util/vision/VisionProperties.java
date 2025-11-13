package com.advancedMobileProgramming.global.util.vision;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "vision")
public class VisionProperties {
    private String role;
    private String projectId;
    private String location;
    private String projectNumber;
    private String corpusId;
    private String indexEndpointId;
    private String bucket;
    private String credentialsPath;
}