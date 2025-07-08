package com.feedback.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.feedback.entity.Register;
import com.feedback.repository.IRegisterRepository;

@Service
public class RegisterServiceImpl implements IRegisterService {

    @Autowired
    private IRegisterRepository registerRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    public Register register(Register register) {
        // Encrypt password before saving
        register.setPassword(passwordEncoder.encode(register.getPassword()));
        return registerRepository.save(register);
    }

	/*@Override
	public boolean login(String username, String password) {
	    return false;
	}*/

    @Override
    public Register getByUsername(String username) {
        return registerRepository.findByUsername(username);
    }
}
