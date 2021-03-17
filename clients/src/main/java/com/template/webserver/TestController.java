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
        return "anotherPage";
    }

    @GetMapping("/doctorPage")
    private String doctorPage(){
        return "doctorPage";
    }


}
