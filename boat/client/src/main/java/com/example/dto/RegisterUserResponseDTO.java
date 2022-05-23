package com.example.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotEmpty;

@NoArgsConstructor
@ToString
@Getter
@Setter
public class RegisterUserResponseDTO {
    @NotEmpty
    private String message;
}
