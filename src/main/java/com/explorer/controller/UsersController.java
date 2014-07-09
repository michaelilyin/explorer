package com.explorer.controller;

import com.explorer.domain.User;
import com.explorer.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.Md5PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * Created by Michael on 08.07.2014.
 */
@Controller
public class UsersController {

    @Autowired
    private UserService userService;

    @RequestMapping(value = "/signup", method = RequestMethod.GET)
    public String signUp(ModelMap model) {
        model.addAttribute(new User());
        return "../../register";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "../../login";
    }

    @RequestMapping(value = "/signup", method = RequestMethod.POST)
    public String register(@Valid User newUser, BindingResult bindingResult, ModelMap model) {
        if (bindingResult.hasErrors()) {
            model.put("errors", bindingResult.getAllErrors());
            return "../../register";
        }
        User user = userService.getUser(newUser.getUsername());
        if (user != null) {
            model.put("errorCode", "inputError.loginExists");
            return "../../register";
        }
        Md5PasswordEncoder encoder = new Md5PasswordEncoder();
        System.out.println(newUser.getUsername() + " " + newUser.getPassword());
        newUser.setPassword(encoder.encodePassword(newUser.getPassword(), newUser.getUsername()));
        System.out.println(newUser.getUsername() + " " + newUser.getPassword());
        userService.saveUser(newUser);
        return "redirect:/files";
    }
}