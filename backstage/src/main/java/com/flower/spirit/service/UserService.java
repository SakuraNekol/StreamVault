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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.flower.spirit.common.AjaxEntity;
import com.flower.spirit.common.RequestEntity;
import com.flower.spirit.config.Global;
import com.flower.spirit.dao.UserDao;
import com.flower.spirit.entity.UserEntity;
import com.flower.spirit.utils.MD5Util;
import com.flower.spirit.utils.StringUtil;

@Service
public class UserService {
	
//	private Logger logger = LoggerFactory.getLogger(UserService.class);
		
	@Autowired
	private UserDao userDao;

	/**  
	
	 * <p>Title: findUserList</p>  
	
	 * <p>Description:分页获取管理员列表 </p>  
	
	 * @param res
	 * @return  
	
	 */  
	@SuppressWarnings("serial")
	public AjaxEntity findUserList(RequestEntity res) {
	    int page = res.getPage() == null ? 0 : res.getPage();
	    int limit = res.getLimit() == null ? 15 : res.getLimit();
	    Pageable pageable = PageRequest.of(page, limit, Sort.by(Sort.Direction.ASC, "lasttime"));

	    Specification<UserEntity> specification = (root, query, cb) -> {
	        List<Predicate> predicates = new ArrayList<>();
	        return cb.and(predicates.toArray(new Predicate[0]));
	    };

	    Page<UserEntity> findAll = userDao.findAll(specification, pageable);
	    return new AjaxEntity(Global.ajax_success, "数据获取成功", findAll);
	}

	/**  
	
	 * <p>Title: addUser</p>  
	
	 * <p>Description: 添加用户</p>  
	
	 * @param userEntity
	 * @return  
	
	 */  
	public AjaxEntity addUser(UserEntity userEntity) {
		if(!StringUtil.isString(userEntity.getUsername()) || !StringUtil.isString(userEntity.getPassword())) {
			return new AjaxEntity(Global.ajax_uri_error,Global.ajax_uri_error_message,null);
		}
		UserEntity findByUsername = userDao.findByUsername(userEntity.getUsername());
		if(findByUsername != null) {
			return new AjaxEntity(Global.ajax_add_user_err,Global.ajax_add_user_err_message,null);
		}
		Date date = new Date();
		userEntity.setLasttime(Long.toString(date.getTime()));
		userEntity.setPassword(MD5Util.MD5(userEntity.getPassword()));
		userDao.save(userEntity);
		return new AjaxEntity(Global.ajax_success, Global.ajax_add_user_success_message, null);
	}

	public AjaxEntity delUser(UserEntity userEntity) {
		userDao.deleteById(userEntity.getId());
		return new AjaxEntity(Global.ajax_success, Global.ajax_option_success, null);
	}

}