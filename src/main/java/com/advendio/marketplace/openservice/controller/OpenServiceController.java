/* (C)2022 */
package com.advendio.marketplace.openservice.controller;

import com.advendio.marketplace.openservice.model.dto.TokenDto;
import com.advendio.marketplace.openservice.model.request.CreateClientRequest;
import com.advendio.marketplace.openservice.model.response.EncryptedData;
import com.advendio.marketplace.openservice.service.CognitoService;
import com.advendio.marketplace.openservice.service.EncryptionService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Marketplace OpenService")
@Slf4j
public class OpenServiceController {
    private final EncryptionService encryptionService;
    private final CognitoService cognitoService;

    public OpenServiceController(
            EncryptionService encryptionService, CognitoService clientRequest) {
        this.encryptionService = encryptionService;
        this.cognitoService = clientRequest;
    }

    @GetMapping(value = "/v1/test")
    @ApiImplicitParams({
        @ApiImplicitParam(
                name = "Authorization",
                value = "Authorization",
                example = "Bearer <access_token>",
                paramType = "header",
                required = true)
    })
    public ResponseEntity<String> getData() {
        return ResponseEntity.ok("GET DATA");
    }

    @PostMapping(value = "/v1/test")
    @ApiImplicitParams({
        @ApiImplicitParam(
                name = "Authorization",
                value = "Authorization",
                example = "Bearer <access_token>",
                paramType = "header",
                required = true)
    })
    public ResponseEntity<String> createData() {
        return ResponseEntity.ok("CREATE DATA");
    }

    @GetMapping(value = "/v1/token")
    public ResponseEntity<TokenDto> getToken(@RequestParam String clientId) {
        return ResponseEntity.ok(cognitoService.getToken(clientId));
    }

    @PostMapping(value = "/v1/admin/encrypt")
    public ResponseEntity<EncryptedData> encryptData(@RequestBody Object request) {
        return ResponseEntity.ok(encryptionService.encryptData(request));
    }

    @PostMapping(value = "/v1/admin/client")
    public ResponseEntity<TokenDto> createClient(@RequestBody EncryptedData encryptedData) {
        CreateClientRequest createClientRequest =
                encryptionService.decryptCreateData(encryptedData);
        return ResponseEntity.ok(cognitoService.createPoolClientAndGetToken(createClientRequest));
    }
}
