package com.trading.app.Controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController 
public class HomeController {
	
	@GetMapping
	public String home()
	{
		return "Well Come To Trading Platform";
	}
	
	
	@GetMapping("/api")
	public String secure()
	{
		return "Well Come To Trading Platform secure";
	}
}
