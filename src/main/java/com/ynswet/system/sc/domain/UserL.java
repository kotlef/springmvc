package com.ynswet.system.sc.domain;
// Generated 2015-6-12 13:37:56 by Hibernate Tools 4.3.1

import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import static javax.persistence.GenerationType.IDENTITY;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * UserL generated by hbm2java
 */
@Entity
@Table(name = "user_l", catalog = "fd_d1")
public class UserL implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 日志内码(自增序号).
	 */
	private Integer logId;

	/**
	 * 用户内码.
	 */
	private Integer uid;

	/**
	 * 用户日志类型（C=新建, S=停用, R=重新启用, P=密码变更, M=档案修改).
	 */
	private String userLogType;

	/**
	 * 修改者用户内码.
	 */
	private Integer modifyUid;

	/**
	 * 日志描述.
	 */
	private String logDesc;

	/**
	 * 记录建立时间.
	 */
	private Date createTime;

	public UserL() {
	}

	public UserL(Integer uid, Integer modifyUid, Date createTime) {
		this.uid = uid;
		this.modifyUid = modifyUid;
		this.createTime = createTime;
	}
	public UserL(Integer uid, String userLogType, Integer modifyUid, String logDesc,
			Date createTime) {
		this.uid = uid;
		this.userLogType = userLogType;
		this.modifyUid = modifyUid;
		this.logDesc = logDesc;
		this.createTime = createTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "LogID", unique = true, nullable = false)
	public Integer getLogId() {
		return this.logId;
	}

	public void setLogId(Integer logId) {
		this.logId = logId;
	}

	@Column(name = "UID", nullable = false)
	public Integer getUid() {
		return this.uid;
	}

	public void setUid(Integer uid) {
		this.uid = uid;
	}

	@Column(name = "UserLog_Type", length = 1)
	public String getUserLogType() {
		return this.userLogType;
	}

	public void setUserLogType(String userLogType) {
		this.userLogType = userLogType;
	}

	@Column(name = "Modify_UID", nullable = false)
	public Integer getModifyUid() {
		return this.modifyUid;
	}

	public void setModifyUid(Integer modifyUid) {
		this.modifyUid = modifyUid;
	}

	@Column(name = "Log_DESC", length = 65535)
	public String getLogDesc() {
		return this.logDesc;
	}

	public void setLogDesc(String logDesc) {
		this.logDesc = logDesc;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Create_Time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@").append(" [");
		buffer.append("logId").append("='").append(getLogId()).append("' ");
		buffer.append("uid").append("='").append(getUid()).append("' ");
		buffer.append("userLogType").append("='").append(getUserLogType())
				.append("' ");
		buffer.append("modifyUid").append("='").append(getModifyUid())
				.append("' ");
		buffer.append("logDesc").append("='").append(getLogDesc()).append("' ");
		buffer.append("createTime").append("='").append(getCreateTime())
				.append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}