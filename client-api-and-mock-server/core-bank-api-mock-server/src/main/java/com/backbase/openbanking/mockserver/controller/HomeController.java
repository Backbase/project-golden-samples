package com.backbase.openbanking.mockserver.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * Home redirection to OpenAPI api documentation
 */
@Controller
public class HomeController {

    @GetMapping("/")
    public String index() {
        return "redirect:swagger-ui.html";
    }


}
