package com.gmail.phrolovich.controller;

import com.gmail.phrolovich.api.dto.Direction;
import com.gmail.phrolovich.api.dto.ServerInstance;
import com.gmail.phrolovich.integration.AWSInstanceData;
import com.gmail.phrolovich.integration.AWSServiceGateway;
import com.gmail.phrolovich.mapper.DtoMapper;
import com.gmail.phrolovich.service.PagingSortingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedList;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class ServerDashboardControllerTest {
    @Mock
    private AWSServiceGateway awsServiceGateway;
    @Mock
    private PagingSortingService pagingSortingService;
    @Mock
    private DtoMapper mapper;
    @InjectMocks
    private ServerDashboardController controller;

    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        mockMvc = org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup(controller)
            .setControllerAdvice(new ErrorHandler())
            .build();
    }

    @Test

    public void shouldLoadDashboard() throws Exception {
        List<AWSInstanceData> listItems = new LinkedList<>();
        for (int i = 0; i < 20; i++) {
            listItems.add(createAwsInstance(i));
            when(mapper.fromAwsInstance(createAwsInstance(i))).thenReturn(createInstance(i));
        }

        when(awsServiceGateway.describeInstances("eu-central-1")).thenReturn(listItems);
        when(pagingSortingService.sortAndPage(1, 10, "name", Direction.DESC, listItems)).thenReturn(listItems.subList(0, 10));


        mockMvc.perform(
            get("/api/servers/v1")
                .accept(MediaType.APPLICATION_JSON)
                .param("region", "eu-central-1")
                .param("direction", "desc")
        ).andExpect(status().isOk())
            .andExpect(content().string("[{\"instanceId\":\"instance0\",\"name\":\"name0\",\"instanceType\":\"t2.nano\",\"state\":\"running\",\"availabilityZone\":\"zonea\",\"privateIpAddress\":\"192.168.0.0\",\"publicIpAddress\":\"192.168.1.0\"},{\"instanceId\":\"instance1\",\"name\":\"name1\",\"instanceType\":\"t2.nano\",\"state\":\"running\",\"availabilityZone\":\"zonea\",\"privateIpAddress\":\"192.168.0.1\",\"publicIpAddress\":\"192.168.1.1\"},{\"instanceId\":\"instance2\",\"name\":\"name2\",\"instanceType\":\"t2.nano\",\"state\":\"running\",\"availabilityZone\":\"zonea\",\"privateIpAddress\":\"192.168.0.2\",\"publicIpAddress\":\"192.168.1.2\"},{\"instanceId\":\"instance3\",\"name\":\"name3\",\"instanceType\":\"t2.nano\",\"state\":\"running\",\"availabilityZone\":\"zonea\",\"privateIpAddress\":\"192.168.0.3\",\"publicIpAddress\":\"192.168.1.3\"},{\"instanceId\":\"instance4\",\"name\":\"name4\",\"instanceType\":\"t2.nano\",\"state\":\"running\",\"availabilityZone\":\"zonea\",\"privateIpAddress\":\"192.168.0.4\",\"publicIpAddress\":\"192.168.1.4\"},{\"instanceId\":\"instance5\",\"name\":\"name5\",\"instanceType\":\"t2.nano\",\"state\":\"running\",\"availabilityZone\":\"zonea\",\"privateIpAddress\":\"192.168.0.5\",\"publicIpAddress\":\"192.168.1.5\"},{\"instanceId\":\"instance6\",\"name\":\"name6\",\"instanceType\":\"t2.nano\",\"state\":\"running\",\"availabilityZone\":\"zonea\",\"privateIpAddress\":\"192.168.0.6\",\"publicIpAddress\":\"192.168.1.6\"},{\"instanceId\":\"instance7\",\"name\":\"name7\",\"instanceType\":\"t2.nano\",\"state\":\"running\",\"availabilityZone\":\"zonea\",\"privateIpAddress\":\"192.168.0.7\",\"publicIpAddress\":\"192.168.1.7\"},{\"instanceId\":\"instance8\",\"name\":\"name8\",\"instanceType\":\"t2.nano\",\"state\":\"running\",\"availabilityZone\":\"zonea\",\"privateIpAddress\":\"192.168.0.8\",\"publicIpAddress\":\"192.168.1.8\"},{\"instanceId\":\"instance9\",\"name\":\"name9\",\"instanceType\":\"t2.nano\",\"state\":\"running\",\"availabilityZone\":\"zonea\",\"privateIpAddress\":\"192.168.0.9\",\"publicIpAddress\":\"192.168.1.9\"}]"));
    }

    @Test
    public void shouldReturnEmptyResponse() throws Exception {
        List<AWSInstanceData> listItems = new LinkedList<>();

        when(awsServiceGateway.describeInstances("eu-central-1")).thenReturn(listItems);
        when(pagingSortingService.sortAndPage(1, 10, "name", Direction.DESC, listItems)).thenReturn(listItems);

        mockMvc.perform(
            get("/api/servers/v1")
                .accept(MediaType.APPLICATION_JSON)
                .param("region", "eu-central-1")
                .param("direction", "desc")
        ).andExpect(status().isOk())
            .andExpect(content().string("[]"));
    }

    @Test
    public void shouldReturnBadRequestForInvalidRegionCode() throws Exception {
        when(awsServiceGateway.describeInstances("invalid"))
            .thenThrow(new IllegalArgumentException("Invalid region"));

        mockMvc.perform(
            get("/api/servers/v1")
                .accept(MediaType.APPLICATION_JSON)
                .param("region", "invalid")
                .param("direction", "desc")
        ).andExpect(status().isBadRequest());
    }

    @Test
    public void shouldReturnBadRequestForInvalidDirection() throws Exception {
        mockMvc.perform(
            get("/api/servers/v1")
                .accept(MediaType.APPLICATION_JSON)
                .param("region", "invalid")
                .param("direction", "de")
                .param("sorting", "invalidField")
        ).andExpect(status().isBadRequest());
    }

    private ServerInstance createInstance(int i) {
        ServerInstance serverInstance = new ServerInstance();
        serverInstance.setName("name" + i);
        serverInstance.setInstanceId("instance" + i);
        serverInstance.setState("running");
        serverInstance.setInstanceType("t2.nano");
        serverInstance.setAvailabilityZone("zonea");
        serverInstance.setPrivateIpAddress("192.168.0." + i);
        serverInstance.setPublicIpAddress("192.168.1." + i);
        return serverInstance;
    }

    AWSInstanceData createAwsInstance(int index) {
        AWSInstanceData awsInstanceData = new AWSInstanceData();
        awsInstanceData.setName("AWSInstanceData" + index);
        awsInstanceData.setState("running");
        awsInstanceData.setInstanceId("instance" + index);
        awsInstanceData.setInstanceType("t2.nano");
        awsInstanceData.setAvailabilityZone("zonea");
        awsInstanceData.setPrivateIpAddress("192.168.0." + index);
        awsInstanceData.setPublicIpAddress("192.168.1." + index);
        return awsInstanceData;
    }
}
