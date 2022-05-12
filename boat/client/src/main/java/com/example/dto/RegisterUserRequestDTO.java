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
public class RegisterUserRequestDTO {
    @NotEmpty
    private String username;
    private String email;
}
