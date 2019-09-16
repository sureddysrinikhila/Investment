package com.dandi.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.dandi.api.model.Login;
import com.dandi.api.model.LoginResponse;
import com.dandi.api.model.Prediction;


@RestController
@RequestMapping(value="/LoginService")
public class LoginService {

	@Autowired
	private UserRepository userRepository;
	
	@RequestMapping(method = RequestMethod.POST,value="/validateLogin")
	public LoginResponse validateLogin(@RequestBody Login login){
		
		User user = userRepository.findByUserNameAndPassword(login.getUserName(), login.getPassword());
		
		LoginResponse response = new LoginResponse();
		
		if(user == null)
		{
			response.setUserName("Invalid");
			return response;
		}
		
		response.setUserName(user.getUserName());

		response.setRecentPredictions(new ArrayList<Prediction>());
		
		return response;
	}
}
