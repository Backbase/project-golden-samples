package com.example;

import com.backbase.buildingblocks.test.http.TestRestTemplateConfiguration;
import com.backbase.greeting.api.service.v1.GreetingApi;
import com.backbase.greeting.api.service.v1.model.GreetingPostResponse;
import com.example.dto.RegisterUserRequestDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest(classes = BoatExampleClientApplication.class)
@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
@AutoConfigureMockMvc
@ActiveProfiles("it")
class RegisterControllerIT {

    private static final String HELLO_USERNAME = "Hello username!";
    @Autowired
    private MockMvc mvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private GreetingApi greetingApi;

    @Test
    void exampleTest() throws Exception {
        //mock server's response
        GreetingPostResponse greetingPostResponse = new GreetingPostResponse();
        greetingPostResponse.setMessage(HELLO_USERNAME);
        when(greetingApi.postGreeting(any())).thenReturn(greetingPostResponse);
        //create test request
        RegisterUserRequestDTO registerUserRequestDTO = new RegisterUserRequestDTO();
        registerUserRequestDTO.setUsername("username");
        registerUserRequestDTO.setEmail("test@test.com");
        String requestAsString = objectMapper.writeValueAsString(registerUserRequestDTO);

        mvc.perform(
                        post("/client-api/register")
                                .header("Authorization", TestRestTemplateConfiguration.TEST_SERVICE_TOKEN)
                                .content(requestAsString)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.message").value(equalTo(HELLO_USERNAME)));
    }
}
