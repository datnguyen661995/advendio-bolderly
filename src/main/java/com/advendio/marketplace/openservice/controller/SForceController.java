/* (C)2022 */
package com.advendio.marketplace.openservice.controller;

import com.advendio.marketplace.openservice.constants.SFConstant;
import com.advendio.marketplace.openservice.model.dto.OAuthConnectionDto;
import com.advendio.marketplace.openservice.model.request.DataRequest;
import com.advendio.marketplace.openservice.service.SForceService;
import com.sforce.async.AsyncApiException;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(tags = "SaleForce - Connection")
@Slf4j
@RequestMapping(value = "/sForce")
public class SForceController {
    @Autowired private SForceService sForceService;

    public SForceController(SForceService sForceService) {
        this.sForceService = sForceService;
    }

    @PostMapping(value = "/v1/connection")
    public ResponseEntity<?> getConnection(@RequestBody DataRequest dataRequest)
            throws AsyncApiException {
        OAuthConnectionDto connectionDto =
                sForceService.getAllConnection(
                        dataRequest.getUsername(),
                        dataRequest.getEndpoint(),
                        SFConstant.BULK_CONNECTION);
        log.info("##Connected to MarketPlace");
        if (!ObjectUtils.isEmpty(connectionDto)
                && StringUtils.isNotBlank(
                        connectionDto.getBulkConnection().getConfig().getSessionId())
                && StringUtils.isNotBlank(connectionDto.getConfig().getSessionId())) {
            return ResponseEntity.ok("Connection: ok");
        }
        return ResponseEntity.ok("Connection: failed");
    }
}
