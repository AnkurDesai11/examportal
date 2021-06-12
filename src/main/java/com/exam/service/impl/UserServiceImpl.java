package com.exam.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import com.exam.helper.UserFoundException;
import com.exam.helper.UserNotFoundException;
import com.exam.model.User;
import com.exam.model.UserRole;
import com.exam.repo.RoleRepository;
import com.exam.repo.UserRepository;
import com.exam.service.UserService;

@Service
public class UserServiceImpl implements UserService{

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	//creating user
	@Override
	public User createUser(User user, Set<UserRole> userRoles) throws UserFoundException{
		// TODO Auto-generated method stub
		//return null;
		
		User localUser = this.userRepository.findByUsername(user.getUsername());
		User localUsermail = this.userRepository.findByEmail(user.getEmail());
		
		if(localUser!=null) {
			System.out.println("User with username exists in database");
			//throw new Exception("User with username exists");
			throw new UserFoundException("User with username already exists in database");
		}
		else if(localUsermail!=null) {
			System.out.println("User with email exists in database");
			throw new UserFoundException("User with email already exists in database");
		}
		else {
			//create user
			for(UserRole urole: userRoles ) {
				roleRepository.save(urole.getRole());//adding roles to database
			}
			user.getUserRoles().addAll(userRoles);//assigning roles to user
			localUser = this.userRepository.save(user);
		}
		return localUser;
	}
	
	//getting user by username
	@Override
	public User getUser(String username) {
		return this.userRepository.findByUsername(username);
	}
	
	//delete user by userId
	@Override
	public void deleteUser(Long userId) {
		this.userRepository.deleteById(userId);
	}

	//update user by username
	@Override
	public User updateUser(User user) throws UserNotFoundException {
		// TODO Auto-generated method stub
		
		User localUser = this.userRepository.findByUsername(user.getUsername());
		
		if(localUser==null) {
			System.out.println("User does not exist in database");
			throw new UserNotFoundException();
		}
		else {
			user.setId(localUser.getId());
		}
		return this.userRepository.save(user);
	}
	
}
