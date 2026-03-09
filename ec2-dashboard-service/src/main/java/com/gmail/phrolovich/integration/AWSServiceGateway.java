package com.gmail.phrolovich.integration;

import com.alicp.jetcache.anno.CacheType;
import com.alicp.jetcache.anno.Cached;
import com.amazonaws.auth.AWSSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.gmail.phrolovich.mapper.DtoMapper;
import com.gmail.phrolovich.security.STSAuthentication;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@Slf4j
@AllArgsConstructor
public class AWSServiceGateway {
    private final DtoMapper dtoMapper;
    private final AWSServicesFactory awsClientBuilder;

    @Cached(name = "aws-describe-instances", key = "#region", expire = 60, cacheType = CacheType.LOCAL)
    public List<AWSInstanceData> describeInstances(String region) {
        SecurityContext context = SecurityContextHolder.getContext();
        OAuth2Authentication authentication = (OAuth2Authentication) context.getAuthentication();
        STSAuthentication credentialsCredentials = (STSAuthentication) authentication.getUserAuthentication();

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        Regions awsRegionFromName = Regions.fromName(region);
        AmazonEC2 amazonEC2 = awsClientBuilder.awsClient(awsRegionFromName, (AWSSessionCredentials) credentialsCredentials.getCredentials());

        List<AWSInstanceData> result = new LinkedList<>();
        boolean done = false;
        DescribeInstancesResult response = null;
        while (!done) {
            response = amazonEC2.describeInstances(request);
            response.getReservations().stream()
                .flatMap(reservation -> reservation.getInstances().stream())
                .map(dtoMapper::fromEC2Instance)
                .collect(Collectors.toCollection(() -> result));

            request.setNextToken(response.getNextToken());
            done = response.getNextToken() == null;
        }

        return result;
    }


}
