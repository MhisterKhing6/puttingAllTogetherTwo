package com.example.picturegaller.utils;

import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ssm.SsmClient;
import software.amazon.awssdk.services.ssm.model.GetParameterRequest;
import software.amazon.awssdk.services.ssm.model.GetParameterResponse;

@Component
public class ParameterStoreUtil {

    private final SsmClient ssmClient;

    public ParameterStoreUtil() {


        this.ssmClient = SsmClient.builder()
                .region(Region.of("eu-west-1"))
                .build();
    }

    public String getParameter(String name, boolean isSecure) {
        GetParameterRequest request = GetParameterRequest.builder()
                .name(name)
                .withDecryption(isSecure)  // Decrypt SecureString values
                .build();

        GetParameterResponse response = ssmClient.getParameter(request);
        return response.parameter().value();
    }
}
