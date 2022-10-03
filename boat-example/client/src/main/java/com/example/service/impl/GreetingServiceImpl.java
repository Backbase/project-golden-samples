package com.example.service.impl;

import com.backbase.greeting.api.service.v1.GreetingApi;
import com.backbase.greeting.api.service.v1.model.GreetingPostRequest;
import com.example.dto.RegisterUserResponseDTO;
import com.example.service.GreetingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class GreetingServiceImpl implements GreetingService {

    private final GreetingApi greetingApi;

    @Override
    public RegisterUserResponseDTO fetchGreeting(String username) {
        String message = greetingApi.postGreeting(new GreetingPostRequest().username(username)).getMessage();
        RegisterUserResponseDTO registerUserResponseDTO = new RegisterUserResponseDTO();
        registerUserResponseDTO.setMessage(message);
        return registerUserResponseDTO;
    }
}
