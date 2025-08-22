package com.flower.spirit.dao;

import java.util.List;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.flower.spirit.entity.CollectDataDetailEntity;




@Repository
@Transactional
public interface CollectdDataDetailDao extends JpaRepository<CollectDataDetailEntity, Integer>, JpaSpecificationExecutor<CollectDataDetailEntity>{

	
	public List<CollectDataDetailEntity> findAll();

	public void deleteByDataid(Integer dataid);

	public List<CollectDataDetailEntity> findByVideoidAndDataid(String videoid, Integer dataid);

	public long countByDataid(Integer dataid);

	

}
