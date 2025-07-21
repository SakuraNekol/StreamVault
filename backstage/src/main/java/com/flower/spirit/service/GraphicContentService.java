package com.flower.spirit.service;

import java.io.File;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
				if (seachDate != null && StringUtil.isString(seachDate.getAuthor())) {
					predicate.getExpressions().add(
							criteriaBuilder.like(root.get("author"), "%" + seachDate.getAuthor() + "%"));
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



	public Map<String, Long> countByPlatformGroupBy() {
		List<Object[]> graphicPlatformStats = graphicContentDao.countByPlatformGroupBy();
		Map<String, Long> graphicPlatformMap = new HashMap<>();
		for (Object[] stat : graphicPlatformStats) {
			String platform = (String) stat[0];
			Long count = (Long) stat[1];
			graphicPlatformMap.put(platform != null ? platform : "未知", count);
		}
		return graphicPlatformMap;
	}

	/**
	 * 统计今日新增图文内容数量
	 * 
	 * @return 今日新增图文内容数量
	 */
	public Long countTodayAdded() {
		// 获取今日开始时间（00:00:00）
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Date startDate = calendar.getTime();
		// 获取明日开始时间（作为今日结束时间）
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		Date endDate = calendar.getTime();
		return graphicContentDao.countTodayAdded(startDate, endDate);
	}

}
