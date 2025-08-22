package com.flower.spirit.dao;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.flower.spirit.entity.UserEntity;


@Repository
@Transactional
public interface UserDao extends JpaRepository<UserEntity, Integer>, JpaSpecificationExecutor<UserEntity>{

	UserEntity findByUsername(String username);
	
	

}
