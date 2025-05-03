package com.jcsoftware.DsCatalog.services;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.DsCatalog.dtos.EmailDTO;
import com.jcsoftware.DsCatalog.dtos.NewPasswordDTO;
import com.jcsoftware.DsCatalog.entities.PasswordRecover;
import com.jcsoftware.DsCatalog.entities.User;
import com.jcsoftware.DsCatalog.repositories.PasswordRecoverRepository;
import com.jcsoftware.DsCatalog.repositories.UserRepository;
import com.jcsoftware.DsCatalog.services.exceptions.ResourceNotFoundException;

@Service
public class AuthService {
	
	@Value("${email.password-recover.token.minutes}")
	private Long tokenMinutes;
	
	@Value("${email.password-recover.uri}")
	private String recoverUri;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private PasswordRecoverRepository passwordRecoverRepository;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Transactional
	public void createRecoveryToken(EmailDTO dto) {
		
		User user = userRepository.findByEmail(dto.getEmail());
		
		if(user == null) {
			throw new ResourceNotFoundException("email não encontrado");
		}
		
		PasswordRecover pr = new PasswordRecover();
		pr.setEmail(dto.getEmail());
		pr.setToken(UUID.randomUUID().toString());
		pr.setExpiration(Instant.now().plusSeconds(tokenMinutes*60L));
		pr = passwordRecoverRepository.save(pr);
		
		String body = "Acesse o link para definir uma nova senha. \n\n"
				+ recoverUri + pr.getToken();
		
		emailService.sendEmail(dto.getEmail(),"Recuperação de Senha", body);
	}

	@Transactional
	public void saveNewPassword(NewPasswordDTO dto) {
		
		List<PasswordRecover> result = passwordRecoverRepository.searchValidTokens(dto.getToken(), Instant.now());
		if(result.size() == 0) {
			throw new ResourceNotFoundException("token não encontrado");
		}
		User user = userRepository.findByEmail(result.get(0).getEmail());
		user.setPassword(passwordEncoder.encode(dto.getPassword()));
		user = userRepository.save(user);
	}
	
	protected User authenticated() {
		  try {
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    Jwt jwtPrincipal = (Jwt) authentication.getPrincipal();
		    String username = jwtPrincipal.getClaim("username");
		    return userRepository.findByEmail(username);
		  }
		  catch (Exception e) {
		    throw new UsernameNotFoundException("Invalid user");
		  }
		}


}
