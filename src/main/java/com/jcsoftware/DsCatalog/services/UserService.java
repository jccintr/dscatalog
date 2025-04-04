package com.jcsoftware.DsCatalog.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.jcsoftware.DsCatalog.dtos.UserDTO;
import com.jcsoftware.DsCatalog.dtos.UserInsertDTO;
import com.jcsoftware.DsCatalog.dtos.UserUpdateDTO;
import com.jcsoftware.DsCatalog.entities.User;
import com.jcsoftware.DsCatalog.repositories.RoleRepository;
import com.jcsoftware.DsCatalog.repositories.UserRepository;
import com.jcsoftware.DsCatalog.services.exceptions.IntegrityViolationException;
import com.jcsoftware.DsCatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;

@Service
public class UserService implements UserDetailsService {
	
	//@Autowired
	//private PasswordEncoder passwordEncoder;
	
	@Autowired
	private UserRepository repository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public Page<UserDTO> findAllPaged(Pageable pageable) {

	   Page<User> users = repository.findAll(pageable);
			
	   return users.map(x -> new UserDTO(x));
	   
	}
	
	
	@Transactional
	public UserDTO insert(UserInsertDTO dto) {

		User newUser = new User();
		copyDtoToEntity(dto, newUser);
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		newUser.setPassword(passwordEncoder.encode(dto.getPassword()));
		newUser = repository.save(newUser);

		return new UserDTO(newUser);
	}
	
	
	@Transactional(readOnly = true)
	public UserDTO findById(Long id) {

		Optional<User> userO = repository.findById(id);
		User user = userO.orElseThrow(() -> new ResourceNotFoundException(id));
		
		UserDTO dto = new UserDTO(user);
		
		return dto;

	}
	
	
	@Transactional
	public UserDTO update(Long id, UserUpdateDTO dto) {

		try {
			User user = repository.getReferenceById(id);
			copyDtoToEntity(dto, user);
			user = repository.save(user);
			return new UserDTO(user);
		} catch (EntityNotFoundException e) {
			throw (new ResourceNotFoundException(id));
		}

	}
	
	
	@Transactional(propagation=Propagation.SUPPORTS)
	public void delete(Long id) {

		try {

			if (repository.existsById(id)) {
				repository.deleteById(id);
			} else {
				throw (new ResourceNotFoundException(id));
			}
		}

		catch (DataIntegrityViolationException e) {
			throw (new IntegrityViolationException(id));
		}

	}
	
	
	private void copyDtoToEntity(UserDTO dto, User entity) {

		entity.setFirstName(dto.getFirstName());
		entity.setLastName(dto.getLastName());
		entity.setEmail(dto.getEmail());
		entity.getRoles().clear();
		
		//dto.getRoles().forEach(roleDTO->entity.getRoles().add(new Role(roleDTO.getId(),roleDTO.getAuthority())));
		dto.getRoles().forEach(roleDTO->entity.getRoles().add(roleRepository.getReferenceById(roleDTO.getId())));
		
	}


	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		User user = repository.findByEmail(username);
		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}
		return user;
	}
}
