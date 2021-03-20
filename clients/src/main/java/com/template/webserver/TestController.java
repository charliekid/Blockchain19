package com.template.webserver;
import net.corda.core.messaging.CordaRPCOps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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

//    @GetMapping("/patientPage")
//    private String patientPage(Model model){
//        model.addAttribute("userinfo",new userInfo());
//        return "patientPage";
//    }
    @PostMapping("/patientPage")
    private String loginPost(@ModelAttribute("userinfo") userInfo userinfo, Model model){
        System.out.println("Username entered is: " + userinfo.getUsername());
        System.out.println("Password entered is: " + userinfo.getPassword());

        return "patientPage";
    }

    @GetMapping("/homePage")
    private String homePage(){
        return "homePage";
    }

    @GetMapping("/")
    private String login(Model model){
        model.addAttribute("userinfo", new userInfo());
        return "index";
    }

    @GetMapping("/signUp")
    private String signUp () {
        return "signup";
    }

    public class userInfo {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }


}
