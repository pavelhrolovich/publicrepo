package com.gmail.phrolovich.controller;

import com.gmail.phrolovich.api.ServerDashboardApi;
import com.gmail.phrolovich.api.dto.Direction;
import com.gmail.phrolovich.api.dto.ServerInstance;
import com.gmail.phrolovich.mapper.DtoMapper;
import com.gmail.phrolovich.integration.AWSInstanceData;
import com.gmail.phrolovich.integration.AWSServiceGateway;
import com.gmail.phrolovich.service.PagingSortingService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ServerDashboardController implements ServerDashboardApi {
    private static final int ITEMS_PER_PAGE = 10;

    private final AWSServiceGateway awsServiceGateway;
    private final PagingSortingService pagingSortingService;
    private final DtoMapper mapper;

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(Direction.class, new DirectionPropertyEditor());
    }

    public List<ServerInstance> loadPage(int page, String soring, Direction direction, String region) {
        List<AWSInstanceData> instanceData = awsServiceGateway.describeInstances(region);
        List<AWSInstanceData> itemsOnPage = pagingSortingService.sortAndPage(page, ITEMS_PER_PAGE, soring,direction, instanceData);
        return itemsOnPage.stream().map(mapper::fromAwsInstance).collect(Collectors.toList());
    }

}
