package link.softbond.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class MainController {
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@GetMapping({"/status"})
	public String status(){
		return "ok";
	}
	
	@GetMapping({"/status2"})
	public String status2(){
		return "ok";
	}
	
}