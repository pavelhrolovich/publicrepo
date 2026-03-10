package com.gmail.phrolovich.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("Server instance model")
public class ServerInstance {
    @ApiModelProperty("Server instance ID")
    private String instanceId;
    @ApiModelProperty("Server instance name")
    private String name;
    @ApiModelProperty("Server instance type")
    private String instanceType;
    @ApiModelProperty("Server instance state")
    private String state;
    @ApiModelProperty("Server instance availability zone")
    private String availabilityZone;
    @ApiModelProperty("Server instance private IP address")
    private String privateIpAddress;
    @ApiModelProperty("Server instance public IP address")
    private String publicIpAddress;
}
