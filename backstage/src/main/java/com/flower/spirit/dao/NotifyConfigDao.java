package com.flower.spirit.dao;

import java.util.List;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import com.flower.spirit.entity.NotifyConfigEntity;

@Repository
@Transactional
public interface NotifyConfigDao extends JpaRepository<NotifyConfigEntity, Integer>, JpaSpecificationExecutor<NotifyConfigEntity>  {

	public List<NotifyConfigEntity> findAll();
}
