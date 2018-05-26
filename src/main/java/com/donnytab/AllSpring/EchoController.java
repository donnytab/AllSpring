package com.donnytab.AllSpring;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EchoController {

    @RequestMapping("/")
    public String index() {
        return "Echo Hello!";
    }
}
