package ru.romster.accounts;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by n.romanov
 */
@Controller
public class HomeController {
    @RequestMapping("/")
    public String home() {
        return "redirect:swagger-ui.html";
    }
}