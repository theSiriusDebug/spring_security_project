package net.javaguides.springboot.web;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
	private static final Logger logger = Logger.getLogger(net.javaguides.springboot.web.Controller.class);
	@GetMapping("/login")
	public String login() {
		logger.info("login");
		return "login";
	}
	
	@GetMapping("/")
	public String home() {
		logger.info("index");
		return "index";
	}
}
