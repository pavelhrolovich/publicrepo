package com.gmail.phrolovich.mapper;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.gmail.phrolovich.api.dto.ServerInstance;
import com.gmail.phrolovich.integration.AWSInstanceData;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DtoMapper {

    public ServerInstance fromAwsInstance(AWSInstanceData source) {
        if (source == null) {
            return null;
        }

        ServerInstance target = new ServerInstance();
        target.setInstanceId(source.getInstanceId());
        target.setName(source.getName());
        target.setInstanceType(source.getInstanceType());
        target.setState(source.getState());
        target.setAvailabilityZone(source.getAvailabilityZone());
        target.setPrivateIpAddress(source.getPrivateIpAddress());
        target.setPublicIpAddress(source.getPublicIpAddress());
        return target;
    }

    public AWSInstanceData fromEC2Instance(Instance source) {
        if (source == null) {
            return null;
        }

        return AWSInstanceData.builder()
            .instanceId(source.getInstanceId())
            .name(source.getPublicDnsName())
            .instanceType(source.getInstanceType())
            .state(map(source.getState()))
            .availabilityZone(
                Optional.ofNullable(source.getPlacement())
                    .map(placement -> placement.getAvailabilityZone())
                    .orElse(null)
            )
            .privateIpAddress(source.getPrivateIpAddress())
            .publicIpAddress(source.getPublicIpAddress())
            .build();
    }

    String map(InstanceState instance) {
        return Optional.ofNullable(instance)
            .map(InstanceState::getName)
            .orElse(null);
    }

}
