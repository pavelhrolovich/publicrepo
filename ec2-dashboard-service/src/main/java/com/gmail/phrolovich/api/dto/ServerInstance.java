package com.gmail.phrolovich.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(name = "ServerInstance", description = "Server instance model")
public class ServerInstance {
    @Schema(description = "Server instance ID")
    private String instanceId;
    @Schema(description = "Server instance name")
    private String name;
    @Schema(description = "Server instance type")
    private String instanceType;
    @Schema(description = "Server instance state")
    private String state;
    @Schema(description = "Server instance availability zone")
    private String availabilityZone;
    @Schema(description = "Server instance private IP address")
    private String privateIpAddress;
    @Schema(description = "Server instance public IP address")
    private String publicIpAddress;
}
