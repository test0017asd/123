package com.example.dh.controller;

import com.example.dh.repository.Users;
import com.example.dh.service.SearchService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.util.Map;

@Controller
public class SearchController {

    @Autowired
    SearchService sService;
    @Autowired
    HttpSession ss;

    @RequestMapping("/search")
    public String search(String search, int page, Model model) throws IOException {
        Users authUser = (Users) ss.getAttribute("authUser");
        Map<String, Object> bMap = sService.search(search.strip(), page, authUser);
        if(bMap == null) {
            return "error";
        }
        model.addAttribute("infoList", bMap);
        return "search";
    }

    @RequestMapping("/title")
    public String searchDeep(String title, Model model) {
        model.addAttribute("info", sService.searchDeep(title));
        return "searchDeep";
    }
}
