package com.backbase.openbanking.mockserver.api.auth;


import com.backbase.openbanking.backend.api.auth.mock.AuthenticationApi;
import com.backbase.openbanking.backend.model.auth.mock.ErrorResponse;
import com.backbase.openbanking.backend.model.auth.mock.Request;
import com.backbase.openbanking.backend.model.auth.mock.Response;
import com.backbase.openbanking.mockserver.common.web.AbstractMockController;
import io.swagger.annotations.ApiParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class AuthenticationApiController extends AbstractMockController implements AuthenticationApi {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationApiController.class);

    @Override
    protected AbstractMockController.MockDataPathConfiguration defineMockDataPathConfiguration() {
        return new MockDataPathConfiguration("authentication", "authentication");
    }

    @Override
    protected ResponseEntity handleError(Exception e) {
        logger.error("Internal error in Authentication Mock Service", e);

        ErrorResponse error = new ErrorResponse()
                .errorCode(HttpStatus.INTERNAL_SERVER_ERROR.toString())
                .errorDescription(e.getMessage());
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    @SuppressWarnings("unchecked")
    public ResponseEntity<Response> authenticate(@ApiParam(value = "Application invoking the API", required = true) @RequestHeader(value = "apiRequestOriginator", required = true) String apiRequestOriginator,
                                                 @ApiParam(value = "Unique request Id for tracking purposes", required = true) @RequestHeader(value = "requestID", required = true) String requestID,
                                                 @ApiParam(value = "Authenticate request body", required = true) @Valid @RequestBody Request body) {

        final String customerNumber = validateField(body::getCustomerNumber, "Request: customerNumber");
        logger.debug("Authenticating user: Customer Number {}, Surname {}", customerNumber, body.getSurname());
        return mockDataResponse(customerNumber);
    }
}