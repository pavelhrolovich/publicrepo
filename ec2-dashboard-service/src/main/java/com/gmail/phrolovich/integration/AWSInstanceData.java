package com.gmail.phrolovich.integration;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AWSInstanceData {
    private String instanceId;
    private String name;
    private String instanceType;
    private String state;
    private String availabilityZone;
    private String privateIpAddress;
    private String publicIpAddress;
}
