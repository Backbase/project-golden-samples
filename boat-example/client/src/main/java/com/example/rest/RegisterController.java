package com.example.rest;

import com.example.dto.RegisterUserRequestDTO;
import com.example.dto.RegisterUserResponseDTO;
import com.example.service.GreetingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Slf4j
@RestController
@RequiredArgsConstructor
public class RegisterController {

    private final GreetingService greetingService;

    @PostMapping("/client-api/register")
    public ResponseEntity<RegisterUserResponseDTO> registerUser(@RequestBody @Valid RegisterUserRequestDTO registerUserRequestDTO) {
        log.info("registering user");
        //Do register business logic here
        return ResponseEntity.ok(greetingService.fetchGreeting(registerUserRequestDTO.getUsername()));
    }

}
