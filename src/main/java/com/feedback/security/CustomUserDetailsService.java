package com.feedback.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.feedback.entity.Register;
import com.feedback.repository.IRegisterRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

	@Autowired
	private IRegisterRepository registerRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Register user = registerRepository.findByUsername(username);
		return new CustomUserDetails(user);
	}
}
