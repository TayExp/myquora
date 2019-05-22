package com.myquora.myquora.controller;

import java.util.Map;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HelloController {

	@RequestMapping("/hellofm")
	public String hello(Model model) {
		 model.addAttribute("name", "FreeMarker");
		return "hello";
	}
}
