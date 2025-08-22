package com.flower.spirit.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.flower.spirit.common.AjaxEntity;
import com.flower.spirit.config.Global;
import com.flower.spirit.dao.ProcessHistoryDao;
import com.flower.spirit.entity.ProcessHistoryEntity;
import com.flower.spirit.utils.DateUtils;
import com.flower.spirit.utils.StringUtil;

@Service
public class ProcessHistoryService {
	
	@Autowired
	private ProcessHistoryDao processHistoryDao;
	
	public ProcessHistoryEntity saveProcess(Integer id,String originaladdress,String videoplatform) {
		if(Global.openprocesshistory) {
			Integer ids = id==null?null:id;
			String status =id==null?"已提交未执行":"执行完毕";
			ProcessHistoryEntity processHistoryEntity = new ProcessHistoryEntity(ids, originaladdress, videoplatform,status);
			if(id != null) {
				processHistoryEntity.setCreatetime(DateUtils.formatDateTime(new Date()));
			}
			return processHistoryDao.save(processHistoryEntity);
		}
		return new ProcessHistoryEntity();
	}

	 
	public AjaxEntity findPage(ProcessHistoryEntity res) {
	    PageRequest pageRequest = PageRequest.of(res.getPageNo(), res.getPageSize());

	    Specification<ProcessHistoryEntity> specification = (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();

	        if (res != null) {
	            if (StringUtil.isString(res.getOriginaladdress())) {
	                predicates.add(cb.like(root.get("originaladdress"), "%" + res.getOriginaladdress() + "%"));
	            }
	            if (StringUtil.isString(res.getVideoplatform())) {
	                predicates.add(cb.like(root.get("videoplatform"), "%" + res.getVideoplatform() + "%"));
	            }
	            if (StringUtil.isString(res.getStatus())) {
	                predicates.add(cb.like(root.get("status"), "%" + res.getStatus() + "%"));
	            }
	        }

	        query.orderBy(cb.desc(root.get("id")));
	        return cb.and(predicates.toArray(new Predicate[0]));
	    };

	    Page<ProcessHistoryEntity> findAll = processHistoryDao.findAll(specification, pageRequest);
	    return new AjaxEntity(Global.ajax_success, "数据获取成功", findAll);
	}


	public AjaxEntity deleteProcessHistoryData(ProcessHistoryEntity processHistoryEntity) {
		processHistoryDao.deleteById(processHistoryEntity.getId());
		return new AjaxEntity(Global.ajax_success, "操作成功", null);
	}
	
	
}
