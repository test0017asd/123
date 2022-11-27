package com.example.dh.controller;

import com.example.dh.repository.Users;
import com.example.dh.service.UserService;
import com.example.dh.vo.UserVo;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.util.Map;

@Controller
public class MainController {

    @Autowired
    UserService uService;
    @Autowired
    HttpSession ss;

    @RequestMapping(value = {"/", "/index"})
    public String main() {
        return "/index.html";
    }

    @RequestMapping("/joinForm")
    public String joinForm() {
        return "/joinForm.html";
    }

    @ResponseBody
    @RequestMapping("/idChk")
    public Users idChk(@RequestBody String id) {
        System.out.println(uService.idChk(id).toString());
        return uService.idChk(id);
    }

    @RequestMapping("/joinOk")
    public String joinOk(@ModelAttribute UserVo uVo) {
        uService.join(uVo);
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login() {
        return "/login.html";
    }

    @RequestMapping("/loginOk")
    public String loginOk(@ModelAttribute UserVo uVo) {
        Users user = uService.loginOk(uVo);
        if (user != null) {
            ss.setAttribute("authUser", user);
            return "redirect:/";
        } else {
            return "redirect:/login?result=fail";
        }

    }

    @RequestMapping("/logOut")
    public String logOut() {
        ss.removeAttribute("authUser");
        ss.invalidate();
        return "redirect:/";
    }

    @RequestMapping("/search")
    public String search(String search, int page, Model model) throws IOException {
        Users authUser = (Users) ss.getAttribute("authUser");
        Map<String, Object> bMap = uService.search(search.strip(), page, authUser);
        model.addAttribute("infoList", bMap);
        return "/search.html";
    }
    @RequestMapping("/title")
    public String searchDeep(String title, Model model) throws IOException {
        model.addAttribute("info", uService.searchDeep(title));
        return "/searchDeep.html";
    }
}
