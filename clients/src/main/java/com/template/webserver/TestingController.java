package com.template.webserver;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class TestingController {


    @GetMapping("/anotherpage")
    public String getAnotherPage() {
        return "anotherPage";
    }

    @GetMapping("/testpage")
    public String getIndex() {
        return "testpage";
    }
}
