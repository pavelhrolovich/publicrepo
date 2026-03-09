package com.gmail.phrolovich.mapper;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.gmail.phrolovich.api.dto.ServerInstance;
import com.gmail.phrolovich.integration.AWSInstanceData;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;

import java.util.Optional;

@Mapper(componentModel = "spring")
public abstract class DtoMapper {

    public abstract ServerInstance fromAwsInstance(AWSInstanceData source);

    @Mappings(
        value = {
            @Mapping(source = "placement.availabilityZone", target = "availabilityZone"),
            @Mapping(source = "publicDnsName", target = "name")
        }
    )
    public abstract AWSInstanceData fromEC2Instance(Instance source);

    String map(InstanceState instance) {
        return Optional.ofNullable(instance)
            .map(InstanceState::getName)
            .orElse(null);
    }

}
