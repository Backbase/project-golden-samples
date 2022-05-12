package com.example.rest;

import com.backbase.greeting.api.service.v1.GreetingApi;
import com.backbase.greeting.api.service.v1.model.GreetingGetResponse;
import com.backbase.greeting.api.service.v1.model.GreetingPostRequest;
import com.backbase.greeting.api.service.v1.model.GreetingPostResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class GreetingController implements GreetingApi {

    private static final String GREETING_MESSAGE_FORMAT = "Hello %s!";
    private static final String GENERIC_GREETING_MESSAGE = "Hello there!";

    @Override
    public ResponseEntity<GreetingGetResponse> getGreeting() {
        GreetingGetResponse response = new GreetingGetResponse().message(GENERIC_GREETING_MESSAGE);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<GreetingPostResponse> postGreeting(@Valid GreetingPostRequest greetingPostRequest) {
        GreetingPostResponse response = new GreetingPostResponse().message(String.format(GREETING_MESSAGE_FORMAT, greetingPostRequest.getUsername()));
        return ResponseEntity.ok(response);
    }

}
