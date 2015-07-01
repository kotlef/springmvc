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
 * Org generated by hbm2java
 */
@Entity
@Table(name = "org", catalog = "fd_d1")
public class Org implements java.io.Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 组织内码(自增序列).
	 */
	private Integer orgid;

	/**
	 * 组织编码(人工编写).
	 */
	private String orgCode;

	/**
	 * 组织名称.
	 */
	private String orgName;

	/**
	 * 组织类型(*, 见表头定义).
	 */
	private String orgType;

	/**
	 * 父记录ID.
	 */
	private Integer parentId;

	/**
	 * 组织级别(0=缺省的最高, 记录从第1级开始, 1=1级).
	 */
	private Integer treeLevel;

	/**
	 * 是否有子节点的标志(*, open=无子节点, closed=有子节点).
	 */
	private String state;

	/**
	 * 是否服务站(*, Y=服务站, N=非服务站).
	 */
	private String stationFlag;

	/**
	 * 所属机构内码.
	 */
	private Integer orggroupId;

	/**
	 * 启用日期(临时组织必填).
	 */
	private Date openDate;

	/**
	 * 停用日期(临时组织必填).
	 */
	private Date closeDate;

	/**
	 * 状态(*, 0=正常, S=失效).
	 */
	private String status;

	/**
	 * 备注.
	 */
	private String note;

	/**
	 * 记录建立时间.
	 */
	private Date createTime;

	/**
	 * 最后修改时间.
	 */
	private Date modifyTime;

	public Org() {
	}

	public Org(String orgCode, String orgName, String orgType, Integer parentId,
			Integer treeLevel, String state, String stationFlag, Integer orggroupId,
			String status, Date createTime, Date modifyTime) {
		this.orgCode = orgCode;
		this.orgName = orgName;
		this.orgType = orgType;
		this.parentId = parentId;
		this.treeLevel = treeLevel;
		this.state = state;
		this.stationFlag = stationFlag;
		this.orggroupId = orggroupId;
		this.status = status;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}
	public Org(String orgCode, String orgName, String orgType, Integer parentId,
			Integer treeLevel, String state, String stationFlag, Integer orggroupId,
			Date openDate, Date closeDate, String status, String note,
			Date createTime, Date modifyTime) {
		this.orgCode = orgCode;
		this.orgName = orgName;
		this.orgType = orgType;
		this.parentId = parentId;
		this.treeLevel = treeLevel;
		this.state = state;
		this.stationFlag = stationFlag;
		this.orggroupId = orggroupId;
		this.openDate = openDate;
		this.closeDate = closeDate;
		this.status = status;
		this.note = note;
		this.createTime = createTime;
		this.modifyTime = modifyTime;
	}

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "ORGID", unique = true, nullable = false)
	public Integer getOrgid() {
		return this.orgid;
	}

	public void setOrgid(Integer orgid) {
		this.orgid = orgid;
	}

	@Column(name = "ORG_Code", nullable = false, length = 30)
	public String getOrgCode() {
		return this.orgCode;
	}

	public void setOrgCode(String orgCode) {
		this.orgCode = orgCode;
	}

	@Column(name = "ORG_Name", nullable = false, length = 100)
	public String getOrgName() {
		return this.orgName;
	}

	public void setOrgName(String orgName) {
		this.orgName = orgName;
	}

	@Column(name = "ORG_Type", nullable = false, length = 1)
	public String getOrgType() {
		return this.orgType;
	}

	public void setOrgType(String orgType) {
		this.orgType = orgType;
	}

	@Column(name = "ParentID", nullable = false)
	public Integer getParentId() {
		return this.parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Column(name = "Tree_Level", nullable = false)
	public Integer getTreeLevel() {
		return this.treeLevel;
	}

	public void setTreeLevel(Integer treeLevel) {
		this.treeLevel = treeLevel;
	}

	@Column(name = "State", nullable = false, length = 30)
	public String getState() {
		return this.state;
	}

	public void setState(String state) {
		this.state = state;
	}

	@Column(name = "Station_Flag", nullable = false, length = 1)
	public String getStationFlag() {
		return this.stationFlag;
	}

	public void setStationFlag(String stationFlag) {
		this.stationFlag = stationFlag;
	}

	@Column(name = "ORGGroupID", nullable = false)
	public Integer getOrggroupId() {
		return this.orggroupId;
	}

	public void setOrggroupId(Integer orggroupId) {
		this.orggroupId = orggroupId;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "Open_Date", length = 10)
	public Date getOpenDate() {
		return this.openDate;
	}

	public void setOpenDate(Date openDate) {
		this.openDate = openDate;
	}

	@Temporal(TemporalType.DATE)
	@Column(name = "Close_Date", length = 10)
	public Date getCloseDate() {
		return this.closeDate;
	}

	public void setCloseDate(Date closeDate) {
		this.closeDate = closeDate;
	}

	@Column(name = "Status", nullable = false, length = 1)
	public String getStatus() {
		return this.status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Column(name = "Note", length = 65535)
	public String getNote() {
		return this.note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Create_Time", nullable = false, length = 19)
	public Date getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "Modify_Time", nullable = false, length = 19)
	public Date getModifyTime() {
		return this.modifyTime;
	}

	public void setModifyTime(Date modifyTime) {
		this.modifyTime = modifyTime;
	}

	public String toString() {
		StringBuffer buffer = new StringBuffer();

		buffer.append(getClass().getName()).append("@").append(" [");
		buffer.append("orgid").append("='").append(getOrgid()).append("' ");
		buffer.append("orgCode").append("='").append(getOrgCode()).append("' ");
		buffer.append("orgName").append("='").append(getOrgName()).append("' ");
		buffer.append("orgType").append("='").append(getOrgType()).append("' ");
		buffer.append("parentId").append("='").append(getParentId())
				.append("' ");
		buffer.append("treeLevel").append("='").append(getTreeLevel())
				.append("' ");
		buffer.append("state").append("='").append(getState()).append("' ");
		buffer.append("stationFlag").append("='").append(getStationFlag())
				.append("' ");
		buffer.append("orggroupId").append("='").append(getOrggroupId())
				.append("' ");
		buffer.append("openDate").append("='").append(getOpenDate())
				.append("' ");
		buffer.append("closeDate").append("='").append(getCloseDate())
				.append("' ");
		buffer.append("status").append("='").append(getStatus()).append("' ");
		buffer.append("note").append("='").append(getNote()).append("' ");
		buffer.append("createTime").append("='").append(getCreateTime())
				.append("' ");
		buffer.append("modifyTime").append("='").append(getModifyTime())
				.append("' ");
		buffer.append("]");

		return buffer.toString();
	}

}