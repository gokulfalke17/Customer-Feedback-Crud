package com.feedback.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.feedback.security.CustomUserDetailsService;

@Configuration
public class SecurityConfig {

	@Autowired
	private BCryptPasswordEncoder encoder;

	@Autowired
	private CustomUserDetailsService uds;

	@Bean
	public DaoAuthenticationProvider authProvider() {
		DaoAuthenticationProvider dap = new DaoAuthenticationProvider();
		dap.setPasswordEncoder(encoder);
		dap.setUserDetailsService(uds);
		return dap;
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

		http.authenticationProvider(authProvider());

		http.authorizeHttpRequests(
				auth -> auth.requestMatchers("/", "/login", "/register", "/css/**", "/js/**", "/images/**").permitAll()
						.anyRequest().authenticated())
				.formLogin(form -> form.loginPage("/login").loginProcessingUrl("/login")
						.defaultSuccessUrl("/dashboard", true).permitAll())
				.logout(log -> log.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
						.logoutSuccessUrl("/login?logout").invalidateHttpSession(true).deleteCookies("JSESSIONID")
						.permitAll())
				.headers(h -> h.cacheControl(Customizer.withDefaults())).csrf(Customizer.withDefaults());

		return http.build();
	}
}
