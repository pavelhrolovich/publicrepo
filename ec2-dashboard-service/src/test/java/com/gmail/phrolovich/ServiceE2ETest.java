package com.gmail.phrolovich;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gmail.phrolovich.integration.AWSInstanceData;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.http.SecurityHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;

import static org.junit.Assert.fail;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebAppConfiguration
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@Category(E2ETest.class)
public class ServiceE2ETest {
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    private FilterChainProxy springSecurityFilterChain;

    private MockMvc mockMvc;

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac)
                .addFilter(springSecurityFilterChain).build();
    }

    @Test
    public void shouldListEC2Instances() throws Exception {
        String username = System.getProperty("aws.test.username");
        String password = System.getProperty("aws.test.password");
        if (username == null || password == null) {
            fail("E2E server test require to have AWS credentials set aws.test.username and aws.test.password as system variables in order to connect to AWS. Please see the code above.");
        }
        String token = obtainAccessToken(username, password);
        Assert.assertNotNull(token);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("region", "eu-central-1");

        ResultActions result = mockMvc.perform(get("/api/servers/v1")
                .params(params)
                .header("Authorization", "Bearer " + token)
                .accept("application/json;charset=UTF-8"))

                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        AWSInstanceData[] list = new ObjectMapper().readValue(result.andReturn().getResponse().getContentAsByteArray(), AWSInstanceData[].class);
        Assert.assertNotNull(list);
        Assume.assumeTrue(list.length == 1);
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


    private String obtainAccessToken(String username, String password) throws Exception {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "password");
        params.add("username", username);
        params.add("password", password);

        ResultActions result
                = mockMvc.perform(post("/oauth/token")
                .contentType("application/x-www-form-urlencoded")
                .params(params)
                .with(httpBasic("aws-dashboard-client", "aws-dashboard-secret"))
                .accept("application/json;charset=UTF-8"))

                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json;charset=UTF-8"));

        String resultString = result.andReturn().getResponse().getContentAsString();

        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get("access_token").toString();
    }

}
