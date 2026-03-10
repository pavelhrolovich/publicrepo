package com.gmail.phrolovich.api;

import com.gmail.phrolovich.api.dto.Direction;
import com.gmail.phrolovich.api.dto.ServerInstance;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.Authorization;
import io.swagger.annotations.AuthorizationScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/servers/v1")
@Api(value = "EC2 Server Dashboard API", description = "Define API for EC2 Server Dashboard")
public interface ServerDashboardApi {

    @ApiOperation(value = "View list of running EC2 instances in the specific region",
            authorizations = {
                    @Authorization(value = "OAuth2", scopes = {
                            @AuthorizationScope(scope = "read", description = "Read AWS EC2 instances")
                    })
            }
    )
    @GetMapping
    @ResponseBody
    List<ServerInstance> loadPage(@ApiParam(value = "Page to view, each page has 10 elements", example = "1") @RequestParam(value = "page", defaultValue = "1") @Min(value = 1) int page,
                                  @ApiParam(value = "Field to sort by", example = "name") @RequestParam(value = "sorting", defaultValue = "name") String sorting,
                                  @ApiParam(value = "Direction for sorting, either DESC or ASC", example = "desc") @RequestParam(value = "direction", defaultValue = "asc") Direction direction,
                                  @ApiParam(value = "AWS Region to load instances for", example = "eu-central-1") @RequestParam(value = "region") String region);

}
