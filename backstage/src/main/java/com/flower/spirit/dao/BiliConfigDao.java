package com.flower.spirit.dao;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.flower.spirit.entity.BiliConfigEntity;




@Repository
@Transactional
public interface BiliConfigDao extends JpaRepository<BiliConfigEntity, Integer>, JpaSpecificationExecutor<BiliConfigEntity>{

	
	public List<BiliConfigEntity> findAll();

	

}
