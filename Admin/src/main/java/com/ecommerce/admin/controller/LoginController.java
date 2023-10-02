package com.ecommerce.admin.controller;

import com.ecommerce.library.dto.AdminDto;
import com.ecommerce.library.model.Admin;
import com.ecommerce.library.service.impl.AdminServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class LoginController {
    @Autowired
    private AdminServiceImpl adminService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @RequestMapping("/index")
    public String home(Model model) {
        model.addAttribute("title", "登入首頁");
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication == null || authentication instanceof AnonymousAuthenticationToken){
            return "redirect:/login";
        }

        return "index";
    }

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("title", "會員登入");
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("title", "會員註冊");
        model.addAttribute("adminDto", new AdminDto());
        return "register";
    }

    @GetMapping("/forgot-password")
    public String forgotPassword(Model model) {
        model.addAttribute("title", "忘記密碼");
        return "forgot-password";
    }

    @PostMapping("/register-new")
    public String addNewAdmin(Model model,
                              @Valid @ModelAttribute("adminDto") AdminDto adminDto,
                              BindingResult result,
                              HttpSession session) {
        try {
            session.removeAttribute("message");
            if (result.hasErrors()) {
                model.addAttribute("adminDto", adminDto);
                result.toString();
                return "register";
            }

            String username = adminDto.getUsername();
            Admin admin = adminService.findByUsername(username);

            if (admin != null) {
                model.addAttribute("adminDto", adminDto);
//                session.setAttribute("message", "您的信箱已被註冊!");
                model.addAttribute("mailError", "您的信箱已被註冊!");
                System.out.println("您的信箱已被註冊!");

                return "register";
            }

            if (adminDto.getPassword().equals(adminDto.getRepeatPassword())) {
                adminDto.setPassword(passwordEncoder.encode(adminDto.getPassword()));
                adminService.save(adminDto);
                model.addAttribute("adminDto", adminDto);
//                session.setAttribute("message", "會員註冊成功!");
                model.addAttribute("success", "會員註冊成功!");
                System.out.println("會員註冊成功!");

            } else {
                model.addAttribute("adminDto", adminDto);
//                session.setAttribute("message", "您輸入的密碼不一致!");
                model.addAttribute("passwordError", "您輸入的密碼不一致!");
                System.out.println("您輸入的密碼不一致!");

                return "register";
            }

        } catch (Exception e) {
            e.printStackTrace();
//            session.setAttribute("message", "伺服器錯誤!!");
            model.addAttribute("errors", "伺服器錯誤!");
        }

        return "register";
    }
}
