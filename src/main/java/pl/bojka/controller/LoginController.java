package pl.bojka.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import pl.bojka.form.LoginForm;

@Controller
public class LoginController {

    @Autowired
    BuoyDataController buoyDataController;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String getLoginForm(){
        return "login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@ModelAttribute(name = "loginForm")LoginForm loginForm, Model model){
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();
        if ("admin".equals(username) && "bojka".equals(password))
            return buoyDataController.test(model);
        model.addAttribute("invalidCredentials", true);
        return "login";
    }
}
