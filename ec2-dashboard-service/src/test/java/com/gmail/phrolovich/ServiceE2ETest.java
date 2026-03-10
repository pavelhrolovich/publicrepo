package com.gmail.phrolovich;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.phrolovich.integration.AWSInstanceData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebAppConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class ServiceE2ETest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @BeforeEach
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void shouldListEC2Instances() throws Exception {
        String username = System.getProperty("aws.test.username");
        String password = System.getProperty("aws.test.password");
        if (username == null || password == null) {
            Assertions.fail("E2E server test require to have AWS credentials set aws.test.username and aws.test.password as system variables in order to connect to AWS. Please see the code above.");
        }

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("region", "eu-central-1");

        ResultActions result = mockMvc.perform(get("/api/servers/v1")
                .params(params)
                .with(httpBasic(username, password))
                .accept("application/json;charset=UTF-8"))

                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        AWSInstanceData[] list = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsByteArray(), AWSInstanceData[].class);
        Assertions.assertNotNull(list);
        Assumptions.assumeTrue(list.length == 1);
    }

    @Test
    public void shouldNotAllowNonAuthenticatedAccess() throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("region", "eu-central-1");

        ResultActions result = mockMvc.perform(get("/api/servers/v1")
                .params(params)
                .accept("application/json;charset=UTF-8"))

                .andExpect(status().isUnauthorized());
    }
}
