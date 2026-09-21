package com.example.demo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.User;
import com.example.demo.type.AuthProviderType;

public interface UserRepository extends JpaRepository<User, Long>{  
	
	 Optional<User> findByUserName(String username);


	 Optional<User> findByProviderIdAndAuthProviderType(
		        String providerId,
		        AuthProviderType authProviderType
		);
}
