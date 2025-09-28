package com.flower.spirit.entity;

import java.io.Serializable;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;

/**
 * 通知类 config 配置
 */

@Entity
@Table(name = "biz_notify_config")
public class NotifyConfigEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2064265274339620132L;

	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "biz_notify_config")
	@TableGenerator(name = "biz_notify_config", allocationSize = 1, table = "seq_common", pkColumnName = "seq_id", valueColumnName = "seq_count")
	private Integer id;

	private String notifychannel;

	private String qywxkey; // 企业微信key

	private String dingdingKey; // 钉钉机器人key

	private String dingdingSecret; // 钉钉机器人密钥

	private String feishuKey; // 飞书机器人key
	
	private String sctkey;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNotifychannel() {
		return notifychannel;
	}

	public void setNotifychannel(String notifychannel) {
		this.notifychannel = notifychannel;
	}

	public String getQywxkey() {
		return qywxkey;
	}

	public void setQywxkey(String qywxkey) {
		this.qywxkey = qywxkey;
	}

	public String getDingdingKey() {
		return dingdingKey;
	}

	public void setDingdingKey(String dingdingKey) {
		this.dingdingKey = dingdingKey;
	}

	public String getDingdingSecret() {
		return dingdingSecret;
	}

	public void setDingdingSecret(String dingdingSecret) {
		this.dingdingSecret = dingdingSecret;
	}

	public String getFeishuKey() {
		return feishuKey;
	}

	public void setFeishuKey(String feishuKey) {
		this.feishuKey = feishuKey;
	}

	public String getSctkey() {
		return sctkey;
	}

	public void setSctkey(String sctkey) {
		this.sctkey = sctkey;
	}
	
}
