package com.feedback.service;

import com.feedback.entity.Register;

public interface IRegisterService {
	
	public Register register(Register register);
	//public boolean login(String username, String password);
	public Register getByUsername(String username);
}
