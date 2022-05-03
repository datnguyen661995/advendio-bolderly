/* (C)2022 */
package com.advendio.marketplaceborderlyservice.controller;

import com.advendio.marketplaceborderlyservice.model.dto.TokenDto;
import com.advendio.marketplaceborderlyservice.model.request.ClientRequest;
import com.advendio.marketplaceborderlyservice.model.response.EncryptedData;
import com.advendio.marketplaceborderlyservice.service.BolderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "Bolderly")
@Slf4j
public class BolderlyController {
    private final BolderService bolderService;

    public BolderlyController(BolderService bolderService) {
        this.bolderService = bolderService;
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

    @PostMapping(value = "/oauth/token")
    public ResponseEntity<TokenDto> getToken(@RequestBody EncryptedData encryptedData)
            throws Exception {
        return ResponseEntity.ok(bolderService.getToken(encryptedData));
    }

    @GetMapping(value = "/genKey")
    public void generateKey() throws NoSuchAlgorithmException, NoSuchProviderException {
        bolderService.genKey();
    }

    @PostMapping(value = "/encryptData")
    public ResponseEntity<EncryptedData> encryptData(@RequestBody ClientRequest clientRequest)
            throws Exception {

        return ResponseEntity.ok(bolderService.encryptData(clientRequest));
    }
}
