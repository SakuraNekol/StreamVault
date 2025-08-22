package com.flower.spirit.dao;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.flower.spirit.entity.VideoMixEntity;

@Repository
@Transactional
public interface VideoMixDao extends JpaRepository<VideoMixEntity, Integer>, JpaSpecificationExecutor<VideoMixEntity>{

	
	public List<VideoMixEntity> findAll();

	

}
