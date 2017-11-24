package com.jiujichina;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.jiujichina.model.TbUser;

@RestController
public class UserController {
	
	@GetMapping("/{id}")
	public TbUser findById(@PathVariable String id){
		TbUser user = new TbUser();
		user.setName("李益");
		user.setAge(12);
		return user;
	}
	
}
