package com.zamrad.configuration.aws;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnMissingBean(AmazonS3Template.class)
@EnableConfigurationProperties(AmazonProperties.class)
public class AwsConiguration {
    @Autowired
    private AmazonProperties amazonProperties;

    @Bean
    AmazonS3Template amazonS3Template() {
        return new AmazonS3Template(amazonProperties.getS3().getDefaultBucket());
    }
}
