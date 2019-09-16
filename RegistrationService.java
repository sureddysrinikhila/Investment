package com.dandi.api;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dandi.api.model.RegistrationResponse;

@RestController
@RequestMapping(value="/RegistrationService")
public class RegistrationService {

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(method = RequestMethod.POST,value="/registerUser")
	public RegistrationResponse registerUser(@RequestBody com.dandi.api.model.User user){
		
		RegistrationResponse response = new RegistrationResponse();

		User existingUser = userRepository.findByUserName(user.getUserName());
		
		if(existingUser != null) {
	
			response.setStatus("Registraion failed");
			response.setStatusDescription("User already exists");
			
			return response;
		}
		
		User newUser = new User();
		
		newUser.setEmail(user.getEmail());
		newUser.setUserName(user.getUserName());
		newUser.setStatus("Active");
		newUser.setName(user.getName());
		newUser.setLastName(user.getLastName());
		newUser.setPassword(user.getPassword());
		
		User createdUser = userRepository.save(newUser);
		
		response.setStatus("User Registered Successfully");
		response.setStatusDescription(createdUser.toString());
		
		return response;
	}

}
