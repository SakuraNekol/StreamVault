package com.flower.spirit.dao;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.flower.spirit.entity.TikTokConfigEntity;




@Repository
@Transactional
public interface TikTokConfigDao extends JpaRepository<TikTokConfigEntity, Integer>, JpaSpecificationExecutor<TikTokConfigEntity>{

	
	public List<TikTokConfigEntity> findAll();

	

}
