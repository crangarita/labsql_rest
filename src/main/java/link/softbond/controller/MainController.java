package link.softbond.controller;

import link.softbond.service.EmailService;
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
	@Autowired
	private EmailService emailService;

	@GetMapping({"/status"})
	public String status(){
		return passwordEncoder.encode("1234");
	}
	
	@GetMapping({"/status2"})
	public String status2(){
		return "ok";
	}

	@GetMapping({"/status3"})
	public String status3(){
		emailService.sendListEmail("juanpablocorreatarazona@gmail.com", "TEST");
		return "ok";
	}
	
}