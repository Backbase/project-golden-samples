package com.backbase.openbanking.mockserver.api.balances;

import com.backbase.openbanking.backend.api.accounts.mock.BalancesApi;
import com.backbase.openbanking.backend.model.accounts.mock.OBError1;
import com.backbase.openbanking.backend.model.accounts.mock.OBErrorResponse1;
import com.backbase.openbanking.backend.model.accounts.mock.OBReadBalance1;
import com.backbase.openbanking.mockserver.common.TokenDecoder;
import com.backbase.openbanking.mockserver.common.web.AbstractMockController;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@Log4j2
@RestController
@RequiredArgsConstructor
public class OpenBankingBalancesApiController extends AbstractMockController implements BalancesApi {

    @Override
    protected MockDataPathConfiguration defineMockDataPathConfiguration() {
        return new MockDataPathConfiguration("balances", "balances");
    }

    @Override
    public ResponseEntity<OBReadBalance1> getBalances(String authorization, String xFapiAuthDate, String xFapiCustomerIpAddress, String xFapiInteractionId, String xCustomerUserAgent) {
        TokenDecoder decodedToken = TokenDecoder.getDecoded(authorization);
        return mockDataResponse(decodedToken.getCWUser());
    }

    @Override
    protected ResponseEntity<OBErrorResponse1> handleError(Exception e) {
        log.error("Internal error in Accounts Mock Service: ", e);

        OBErrorResponse1 errorResponseDetail = new OBErrorResponse1()
                .errors(List.of(new OBError1()))
                .message(e.getMessage())
                .code(UUID.randomUUID().toString());

        return new ResponseEntity<>(errorResponseDetail, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
