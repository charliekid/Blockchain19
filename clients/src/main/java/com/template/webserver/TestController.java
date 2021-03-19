package com.template.webserver;
import net.corda.core.messaging.CordaRPCOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class TestController {
    @GetMapping("/secondPage")
    private String secondPage() {
        return "redirect:homPage";
    }

    @GetMapping("/doctorPage")
    private String doctorPage(){
        return "doctorPage";
    }

    @GetMapping("/patientPage")
    private String patientPage(){
        return "patientPage";
    }

    @GetMapping("/homePage")
    private String homePage(){
        return "homePage";
    }

    @GetMapping("/")
    private String login(){
        return "index";
    }

    @GetMapping("/signUp")
    private String signUp () {
        return "signup";
    }


}
