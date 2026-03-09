package com.gmail.phrolovich.mapper;

import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.Placement;
import com.gmail.phrolovich.api.dto.ServerInstance;
import com.gmail.phrolovich.integration.AWSInstanceData;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class DtoMapperTest {

    @Autowired
    private DtoMapper dtoMapper;

    @Test
    public void shouldTransformFromAwsInstance() {
        AWSInstanceData source = new AWSInstanceData();
        source.setAvailabilityZone("east-1");
        source.setInstanceId("123123");
        source.setInstanceType("t2.micro");
        source.setName("name");
        source.setPrivateIpAddress("192.168.1.1");
        source.setPublicIpAddress("192.168.1.2");
        source.setState("running");
        ServerInstance target = dtoMapper.fromAwsInstance(source);
        assertNotNull(target);

        assertEquals(source.getAvailabilityZone(), target.getAvailabilityZone());
        assertEquals(source.getInstanceId(), target.getInstanceId());
        assertEquals(source.getInstanceType(), target.getInstanceType());
        assertEquals(source.getName(), target.getName());
        assertEquals(source.getPrivateIpAddress(), target.getPrivateIpAddress());
        assertEquals(source.getPublicIpAddress(), target.getPublicIpAddress());
        assertEquals(source.getState(), target.getState());
    }

    @Test
    public void shouldTransformFromInstance() {
        Instance source = new Instance();
        Placement placement = new Placement();
        placement.setAvailabilityZone("east-1a");
        source.setPlacement(placement);
        source.setInstanceId("123123");
        source.setInstanceType("t2.micro");
        source.setPrivateIpAddress("192.168.1.1");
        source.setPublicIpAddress("192.168.1.2");
        source.setPublicDnsName("my.cool.server.com");
        InstanceState state = new InstanceState();
        state.setName("running");
        source.setState(state);
        AWSInstanceData target = dtoMapper.fromEC2Instance(source);
        assertNotNull(target);

        assertEquals("east-1a", target.getAvailabilityZone());
        assertEquals(source.getInstanceId(), target.getInstanceId());
        assertEquals(source.getInstanceType(), target.getInstanceType());
        assertEquals("my.cool.server.com", target.getName());
        assertEquals(source.getPrivateIpAddress(), target.getPrivateIpAddress());
        assertEquals(source.getPublicIpAddress(), target.getPublicIpAddress());
        assertEquals("running", target.getState());
    }
}
