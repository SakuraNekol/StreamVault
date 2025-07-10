package com.flower.spirit.service;

import java.io.File;
import java.nio.file.Paths;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.flower.spirit.common.AjaxEntity;
import com.flower.spirit.config.Global;
import com.flower.spirit.dao.GraphicContentDao;
import com.flower.spirit.entity.GraphicContentEntity;
import com.flower.spirit.entity.VideoDataEntity;
import com.flower.spirit.utils.CommandUtil;
import com.flower.spirit.utils.StringUtil;

@Service
public class GraphicContentService {
	
	
	@Autowired
	private GraphicContentDao graphicContentDao;
	
	
	
	@SuppressWarnings("serial")
	public AjaxEntity findPage(GraphicContentEntity res) {
		PageRequest of = PageRequest.of(res.getPageNo(), res.getPageSize());
		Specification<GraphicContentEntity> specification = new Specification<GraphicContentEntity>() {

			@Override
			public Predicate toPredicate(Root<GraphicContentEntity> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				Predicate predicate = criteriaBuilder.conjunction();
				GraphicContentEntity seachDate = (GraphicContentEntity) res;
				if (seachDate != null && StringUtil.isString(seachDate.getTitle())
						&& StringUtil.isString(seachDate.getContent())) {
					// 同时传入 videoname 和 videodesc，使用 OR 查询
					Predicate orPredicate = criteriaBuilder.or(
							criteriaBuilder.like(root.get("title"), "%" + seachDate.getTitle() + "%"),
							criteriaBuilder.like(root.get("content"), "%" + seachDate.getContent() + "%"));
					predicate.getExpressions().add(orPredicate);
				} else if (seachDate != null && StringUtil.isString(seachDate.getTitle())) {
					predicate.getExpressions()
							.add(criteriaBuilder.like(root.get("title"), "%" + seachDate.getTitle() + "%"));
				} else if (seachDate != null && StringUtil.isString(seachDate.getContent())) {
					predicate.getExpressions()
							.add(criteriaBuilder.like(root.get("content"), "%" + seachDate.getContent() + "%"));
				}
				if (seachDate != null && StringUtil.isString(seachDate.getPlatform())) {
					predicate.getExpressions().add(
							criteriaBuilder.like(root.get("platform"), "%" + seachDate.getPlatform() + "%"));
				}
				query.orderBy(criteriaBuilder.desc(root.get("id")));
				return predicate;
			}
		};

		Page<GraphicContentEntity> findAll = graphicContentDao.findAll(specification, of);
		return new AjaxEntity(Global.ajax_success, "数据获取成功", findAll);
	}



	public AjaxEntity deleteGraphicContent(String id) {
		Optional<GraphicContentEntity> findById = graphicContentDao.findById(Integer.valueOf(id));
		if (findById.isPresent()) {
			GraphicContentEntity graphicContentEntity = findById.get();
			CommandUtil.deleteDirectory(Paths.get(graphicContentEntity.getMarkroute()).normalize().toString());
			graphicContentDao.deleteById(Integer.valueOf(id));
		}
		return new AjaxEntity(Global.ajax_success, "操作成功", null);
	}

}
