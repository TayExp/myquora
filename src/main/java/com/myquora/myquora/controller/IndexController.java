package com.myquora.myquora.controller;


import java.util.*;

import org.assertj.core.util.Arrays;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.*;

import com.myquora.myquora.model.User;

@Controller
public class IndexController {

	@RequestMapping(path= {"/hello"})
	@ResponseBody
	public String index() {
		return "Hello myquora.";
	}
	
	@RequestMapping(value="/profile/{group}/{userId}")
	@ResponseBody
	public String profile(@PathVariable("group")String group,
					@PathVariable("userId")int userId,
					@RequestParam(value = "type", defaultValue="1",required=false) int type,
					@RequestParam(value="key", defaultValue="myquora")String key) {
		return String.format("Profile page of %s,%d,type=%d,key=%s", group, userId, type, key);
	}
	
	@RequestMapping(path= {"/ftl"}, method= {RequestMethod.GET})
	public String template(Model model) { //model传递数据
		model.addAttribute("value1","v1");
		
		List<String> colors = new ArrayList<>();
		colors.add("RED");
		colors.add("GREEN");
		colors.add("BLUE");
		model.addAttribute("colors", colors);
		Map<String, String> map = new HashMap<>();
        for (int i = 0; i < 4; ++i) {
            map.put(String.valueOf(i), String.valueOf(i * i));
        }
        model.addAttribute("map", map);
        model.addAttribute("user",new User("LEE"));
		return "home"; //返回一个home的模板
	}
}
