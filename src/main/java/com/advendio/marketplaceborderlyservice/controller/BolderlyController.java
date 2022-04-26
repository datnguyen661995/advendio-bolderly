package com.advendio.marketplaceborderlyservice.controller;

import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import com.advendio.marketplaceborderlyservice.service.BolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Bolderly")
@Slf4j
public class BolderlyController {
    @Autowired
    private BolderService bolderService;

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

    @PostMapping(value = "/oauth/token")
    public ResponseEntity<?> getToken(@RequestBody ClientRequest clientRequest) {
        return ResponseEntity.ok(bolderService.getToken(clientRequest));
    }

}
