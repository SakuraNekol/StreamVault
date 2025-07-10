package com.flower.spirit.dao;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.flower.spirit.entity.GraphicContentEntity;

public interface GraphicContentDao extends PagingAndSortingRepository<GraphicContentEntity, Integer>, JpaSpecificationExecutor<GraphicContentEntity>  {

	Optional<GraphicContentEntity> findById(Integer id);

	void deleteById(Integer id);

}
