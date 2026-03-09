package com.gmail.phrolovich.integration;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.ec2.AmazonEC2;
import com.amazonaws.services.ec2.model.DescribeInstancesRequest;
import com.amazonaws.services.ec2.model.DescribeInstancesResult;
import com.amazonaws.services.ec2.model.Instance;
import com.amazonaws.services.ec2.model.InstanceState;
import com.amazonaws.services.ec2.model.Placement;
import com.amazonaws.services.ec2.model.Reservation;
import com.gmail.phrolovich.mapper.DtoMapper;
import com.gmail.phrolovich.security.STSAuthentication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AWSServiceGatewayTest {
    @MockBean
    private DtoMapper dtoMapper;
    @MockBean
    private AWSServicesFactory awsClientBuilder;

    @Autowired
    private AWSServiceGateway gateway;

    @Test
    public void shouldLoadAwsDataSinglePage() {
        OAuth2Authentication oAuth2Authentication = Mockito.mock(OAuth2Authentication.class);
        Mockito.when(oAuth2Authentication.getUserAuthentication()).thenReturn(new STSAuthentication(new BasicSessionCredentials("A", "B", "C"),
                Collections.singletonList(new SimpleGrantedAuthority("read"))));
        SecurityContext mock = Mockito.mock(SecurityContext.class);
        Mockito.when(mock.getAuthentication()).thenReturn(oAuth2Authentication);
        SecurityContextHolder.setContext(mock);

        AmazonEC2 amazonEC2 = Mockito.mock(AmazonEC2.class);
        DescribeInstancesResult result = createResult(createList(10), null);
        Mockito.when(amazonEC2.describeInstances(new DescribeInstancesRequest())).thenReturn(result);
        Mockito.when(awsClientBuilder.awsClient(ArgumentMatchers.eq(Regions.EU_CENTRAL_1), ArgumentMatchers.any())).thenReturn(amazonEC2);

        Mockito.when(dtoMapper.fromEC2Instance(new Instance())).thenReturn(new AWSInstanceData());

        List<AWSInstanceData> awsInstanceData = gateway.describeInstances("eu-central-1");
        assertNotNull(awsInstanceData);
        assertEquals(10, awsInstanceData.size());
        Mockito.verify(amazonEC2).describeInstances(ArgumentMatchers.any());
        Mockito.verifyNoMoreInteractions(amazonEC2);
    }

    @Test
    public void shouldLoadAwsDataMultiPage() {
        OAuth2Authentication oAuth2Authentication = Mockito.mock(OAuth2Authentication.class);
        Mockito.when(oAuth2Authentication.getUserAuthentication()).thenReturn(new STSAuthentication(new BasicSessionCredentials("A", "B", "C"),
                Collections.singletonList(new SimpleGrantedAuthority("read"))));
        SecurityContext mock = Mockito.mock(SecurityContext.class);
        Mockito.when(mock.getAuthentication()).thenReturn(oAuth2Authentication);
        SecurityContextHolder.setContext(mock);

        AmazonEC2 amazonEC2 = Mockito.mock(AmazonEC2.class);

        Mockito.when(amazonEC2.describeInstances(new DescribeInstancesRequest())).thenReturn(createResult(createList(10), "AAAA"));

        DescribeInstancesRequest request = new DescribeInstancesRequest();
        request.setNextToken("AAAA");
        Mockito.when(amazonEC2.describeInstances(request)).thenReturn(createResult(createList(15), null));

        Mockito.when(awsClientBuilder.awsClient(ArgumentMatchers.eq(Regions.EU_CENTRAL_1), ArgumentMatchers.any())).thenReturn(amazonEC2);

        Mockito.when(dtoMapper.fromEC2Instance(new Instance())).thenReturn(new AWSInstanceData());

        List<AWSInstanceData> awsInstanceData = gateway.describeInstances("eu-central-1");
        assertNotNull(awsInstanceData);
        assertEquals(25, awsInstanceData.size());
    }

    private DescribeInstancesResult createResult(List<Instance> list, String nextToken) {
        DescribeInstancesResult result = new DescribeInstancesResult();
        Reservation reservation = new Reservation();
        reservation.setInstances(list);
        result.setReservations(Arrays.asList(reservation));
        result.setNextToken(nextToken);
        return result;
    }

    private List<Instance> createList(int count) {
        return IntStream.range(0, count)
                .mapToObj(this::createAwsInstance)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Instance createAwsInstance(int index) {
        Instance source = new Instance();
        Placement placement = new Placement();
        placement.setAvailabilityZone("east-1a");
        source.setPlacement(placement);
        source.setInstanceId("123123");
        source.setInstanceType("t2.micro");
        source.setPrivateIpAddress("192.168.1.1");
        source.setPublicIpAddress("192.168.1.2");
        source.setPublicDnsName("instance" + index);
        InstanceState state = new InstanceState();
        state.setName("running");
        source.setState(state);

        return source;
    }

    private Instance createInstance() {
        return new Instance();
    }
}
