package com.ynswet.system.sc.model;


import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ynswet.system.sc.domain.Homepage;
import com.ynswet.system.sc.domain.Menu;
import com.ynswet.system.sc.domain.Org;
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.domain.Role;
import com.ynswet.system.sc.domain.User;
import com.ynswet.system.sc.domain.Userlogin;

/**
 *
 * 系统常用变量模型实体
 *
 * @author elfmatian
 *
 */
public class CommonVariableModel implements Serializable {


	/**
	 *
	 */
	private static final long serialVersionUID = -5447925015417711180L;

	// 当前用户详细信息
	private User user;

	// 当前用户登录信息
	private Userlogin userlogin;

	// 当前用户所在的组织
	private List<Org> orgList;

	// 当前用户所拥有的角色
	private List<Role> roleList;

	// 当前用户所拥有的数据权限
	private List<Res> resList;

	// 当前用户的授权资源集合
	private UserAuthorizationInfo authorizationInfo;

	//当前用户的主页
	private List<Homepage> homepageList;

	// 当前用户的菜单集合
	private List<Menu> menusList;

	public CommonVariableModel() {

	}


	public CommonVariableModel(Userlogin userlogin) {
		super();
		this.userlogin = userlogin;
	}

	public User getUser() {
		return user;
	}


	public void setUser(User user) {
		this.user = user;
	}


	public Userlogin getUserlogin() {
		return userlogin;
	}


	public void setUserlogin(Userlogin userlogin) {
		this.userlogin = userlogin;
	}


	public List<Org> getOrgList() {
		return orgList;
	}


	public void setOrgList(List<Org> orgList) {
		this.orgList = orgList;
	}


	public List<Role> getRoleList() {
		return roleList;
	}


	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}


	public List<Res> getResList() {
		return resList;
	}


	public void setResList(List<Res> resList) {
		this.resList = resList;
	}


	public UserAuthorizationInfo getAuthorizationInfo() {
		return authorizationInfo;
	}


	public void setAuthorizationInfo(UserAuthorizationInfo authorizationInfo) {
		this.authorizationInfo = authorizationInfo;
	}


	public List<Menu> getMenusList() {
		return menusList;
	}


	public void setMenusList(List<Menu> menusList) {
		this.menusList = menusList;
	}


	public List<Homepage> getHomepageList() {
		return homepageList;
	}


	public void setHomepageList(List<Homepage> homepageList) {
		this.homepageList = homepageList;
	}





}
