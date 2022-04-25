package com.advendio.marketplaceborderlyservice.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Bolderly")
@Slf4j
public class BolderlyController {

    @GetMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value = "Authorization", example = "Bearer <access_token>", paramType = "header", required = true)
    })
    public ResponseEntity<?> getData() {
        return ResponseEntity.ok("GET DATA");
    }

    @PostMapping
    @ApiImplicitParams({
            @ApiImplicitParam(name="Authorization", value = "Authorization", example = "Bearer <access_token>", paramType = "header", required = true)
    })
    public ResponseEntity<?> createData() {
        return ResponseEntity.ok("CREATE DATA");
    }

}
