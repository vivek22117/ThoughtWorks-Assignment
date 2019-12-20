package com.ddsolutions.iot.equipment.data.config;

import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.InstanceProfileCredentialsProvider;
import com.amazonaws.auth.profile.ProfileCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBMapper;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWSClientConfiguration {

    private static final Logger LOGGER = LoggerFactory.getLogger(AWSCredentialsProvider.class);
    private static AWSCredentialsProvider awsCredentialsProvider;

    @Value("${isRunningInLocal}")
    private boolean isRunningInLocal;

    @Bean
    public DynamoDBMapper getDynamoDBMapper() {
        try {
            awsCredentialsProvider = getAWSCredentials();
            return new DynamoDBMapper(AmazonDynamoDBClientBuilder.standard()
                    .withCredentials(awsCredentialsProvider)
                    .withRegion(Regions.US_EAST_1).build());

        } catch (Exception ex) {
            LOGGER.error("Unable to create DynamoDB mapper...");
            throw ex;
        }
    }

    @Bean
    public Gson getGson() {
        return new Gson();
    }

    private AWSCredentialsProvider getAWSCredentials() {
        if (awsCredentialsProvider == null) {
            if (isRunningInLocal) {
                awsCredentialsProvider = new ProfileCredentialsProvider("doubledigit");
            } else {
                awsCredentialsProvider = new InstanceProfileCredentialsProvider(true);
            }
        }
        return awsCredentialsProvider;
    }

}
