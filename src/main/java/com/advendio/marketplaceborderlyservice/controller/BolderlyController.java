/* (C)2022 */
package com.advendio.marketplaceborderlyservice.controller;

import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.CreateClientRequest;
import com.advendio.marketplaceborderlyservice.model.response.EncryptedData;
import com.advendio.marketplaceborderlyservice.service.BolderService;
import com.advendio.marketplaceborderlyservice.service.CognitoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

@RestController
@Api(tags = "Bolderly")
@Slf4j
public class BolderlyController {
    private final BolderService bolderService;
    private final CognitoService cognitoService;

    public BolderlyController(BolderService bolderService, CognitoService clientRequest) {
        this.bolderService = bolderService;
        this.cognitoService = clientRequest;
    }

    @GetMapping
    @ApiImplicitParams({
        @ApiImplicitParam(
                name = "Authorization",
                value = "Authorization",
                example = "Bearer <access_token>",
                paramType = "header",
                required = true)
    })
    public ResponseEntity<?> getData() {
        return ResponseEntity.ok("GET DATA");
    }

    @PostMapping
    @ApiImplicitParams({
        @ApiImplicitParam(
                name = "Authorization",
                value = "Authorization",
                example = "Bearer <access_token>",
                paramType = "header",
                required = true)
    })
    public ResponseEntity<?> createData() {
        return ResponseEntity.ok("CREATE DATA");
    }

    @GetMapping(value = "/token")
    public ResponseEntity<TokenDto> getToken(@RequestParam String clientId) {
        return ResponseEntity.ok(cognitoService.getToken(clientId));
    }

    @GetMapping(value = "/genKey")
    public void generateKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        bolderService.genKey();
    }

    @PostMapping(value = "/encryptData")
    public ResponseEntity<EncryptedData> encryptData(@RequestBody Object request) {
        return ResponseEntity.ok(bolderService.encryptData(request));
    }

    @PostMapping(value = "/client")
    public ResponseEntity<TokenDto> createClient(@RequestBody EncryptedData encryptedData) {
        CreateClientRequest createClientRequest = bolderService.decryptCreateData(encryptedData);
        return ResponseEntity.ok(cognitoService.createPoolClientAndGetToken(createClientRequest));
    }
}
