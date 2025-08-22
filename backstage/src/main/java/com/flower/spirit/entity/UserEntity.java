package com.flower.spirit.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;


/**
 * 系统用户
 * @author flower
 *
 */
@Entity
@Table(name = "biz_user")
public class UserEntity implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -36170159459919881L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE,generator="user_info_seq")
	@TableGenerator(name = "user_info_seq", allocationSize = 1, table = "seq_common", pkColumnName = "seq_id", valueColumnName = "seq_count")
    private Integer id;
		
	private String username;
	
	private String password;
	
	private String lasttime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getLasttime() {
		return lasttime;
	}

	public void setLasttime(String lasttime) {
		this.lasttime = lasttime;
	}
	
	
	

	
	
}
