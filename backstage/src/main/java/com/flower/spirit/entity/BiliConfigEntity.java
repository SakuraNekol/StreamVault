package com.flower.spirit.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

@Entity
@Table(name = "biz_bili_config")
public class BiliConfigEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3010593573446198051L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE,generator="biz_bili_config")
	@TableGenerator(name = "biz_bili_config", allocationSize = 1, table = "seq_common", pkColumnName = "seq_id", valueColumnName = "seq_count")
    private Integer id;
	
	private String bilicookies;
	
	private String bigmember;
	
	private String bitstream;
	
	private String refreshtoken;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getBilicookies() {
		return bilicookies;
	}

	public void setBilicookies(String bilicookies) {
		this.bilicookies = bilicookies;
	}

	public String getBigmember() {
		return bigmember;
	}

	public void setBigmember(String bigmember) {
		this.bigmember = bigmember;
	}

	public String getBitstream() {
		return bitstream;
	}

	public void setBitstream(String bitstream) {
		this.bitstream = bitstream;
	}

	public String getRefreshtoken() {
		return refreshtoken;
	}

	public void setRefreshtoken(String refreshtoken) {
		this.refreshtoken = refreshtoken;
	}
	
	

}
