package com.example.service;

import com.example.dto.RegisterUserResponseDTO;

public interface GreetingService {
    RegisterUserResponseDTO fetchGreeting(String username);
}
