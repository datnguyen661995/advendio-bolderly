/* (C)2022 */
package com.advendio.marketplace.openservice.controller;

import com.advendio.marketplace.openservice.constants.SFConstant;
import com.advendio.marketplace.openservice.model.request.DataRequest;
import com.advendio.marketplace.openservice.service.SyncAccountService;
import com.sforce.async.AsyncApiException;
import com.sforce.soap.enterprise.sobject.Account;
import io.swagger.annotations.Api;
import java.io.IOException;
import java.util.List;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "SaleForce - Account")
@Slf4j
public class SyncAccountController {
    @Autowired private SyncAccountService accountService;

    @PostMapping(value = "/v1/sForce/accounts")
    public ResponseEntity<?> runSample(@RequestBody DataRequest dataRequest)
            throws IOException, AsyncApiException {
        accountService.createAccounts(
                dataRequest.getDatatype(),
                dataRequest.getUsername(),
                dataRequest.getEndpoint(),
                SFConstant.BULK_CONNECTION);
        return ResponseEntity.ok("Create Account: ok");
    }

    @PostMapping(value = "/v1/sforce/accounts")
    public ResponseEntity<?> getAccount(@Valid DataRequest dataRequest) {
        List<Account> result = accountService.getAccounts(dataRequest, SFConstant.BULK_CONNECTION);
        return ResponseEntity.ok(result);
    }
}
