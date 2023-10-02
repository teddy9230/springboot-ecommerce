package com.ecommerce.customer.controller;

import com.ecommerce.library.dto.CustomerDto;
import com.ecommerce.library.model.Customer;
import com.ecommerce.library.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
public class AuthController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {

        model.addAttribute("customerDto", new CustomerDto());

        return "register";
    }

    @PostMapping("/do-register")
    public String processRegister(@Valid @ModelAttribute("customerDto") CustomerDto customerDto,
                                  BindingResult result,
                                  Model model) {
        try {

            if (result.hasErrors()) {
                model.addAttribute("customerDto", customerDto);
                return "register";
            }

            Customer customer = customerService.findByUsername(customerDto.getUsername());

            if (customer != null) {
                model.addAttribute("username", "使用者名稱已被註冊!");
                model.addAttribute("customerDto", customerDto);
                return "register";
            }

            if (customerDto.getPassword().equals(customerDto.getRepeatPassword())) {
                customerDto.setPassword(passwordEncoder.encode(customerDto.getPassword()));
                customerService.save(customerDto);
                model.addAttribute("success", "註冊成功!");
                return "register";
            } else {
                model.addAttribute("password", "輸入密碼不一致!");
                model.addAttribute("customerDto", customerDto);
                return "register";
            }

        } catch (Exception e) {
            model.addAttribute("error", "伺服器運行出現一些問題!");
            model.addAttribute("customerDto", customerDto);
        }
        return "register";
    }
}
