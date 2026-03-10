package com.gmail.phrolovich.api;

import com.gmail.phrolovich.api.dto.Direction;
import com.gmail.phrolovich.api.dto.ServerInstance;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.constraints.Min;
import java.util.List;

@RestController
@RequestMapping("/api/servers/v1")
@Tag(name = "EC2 Server Dashboard API", description = "Define API for EC2 Server Dashboard")
public interface ServerDashboardApi {

    @Operation(summary = "View list of running EC2 instances in the specific region")
    @GetMapping
    @ResponseBody
    List<ServerInstance> loadPage(@Parameter(description = "Page to view, each page has 10 elements", example = "1") @RequestParam(value = "page", defaultValue = "1") @Min(value = 1) int page,
                                  @Parameter(description = "Field to sort by", example = "name") @RequestParam(value = "sorting", defaultValue = "name") String sorting,
                                  @Parameter(description = "Direction for sorting, either DESC or ASC", example = "desc") @RequestParam(value = "direction", defaultValue = "asc") Direction direction,
                                  @Parameter(description = "AWS Region to load instances for", example = "eu-central-1") @RequestParam(value = "region") String region);

}
