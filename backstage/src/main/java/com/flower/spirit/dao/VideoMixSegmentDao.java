package com.flower.spirit.dao;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.flower.spirit.entity.VideoMixSegmentEntity;




@Repository
@Transactional
public interface VideoMixSegmentDao extends JpaRepository<VideoMixSegmentEntity, Integer>, JpaSpecificationExecutor<VideoMixSegmentEntity>{

	
	public List<VideoMixSegmentEntity> findAll();

	public List<VideoMixSegmentEntity> findByVideomixidOrderBySegmentNoAsc(Integer id);

	public void deleteByVideomixid(Integer id);


	

}
