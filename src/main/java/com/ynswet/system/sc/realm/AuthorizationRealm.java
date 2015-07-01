package com.ynswet.system.sc.realm;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.Permission;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.google.common.collect.Lists;
import com.ynswet.common.util.CollectionUtils;
import com.ynswet.system.sc.model.CommonVariableModel;
import com.ynswet.system.sc.model.UserAuthorizationInfo;

/**
 * apache shiro 的公用授权类
 * 
 * @author elfmatian
 */
public abstract class AuthorizationRealm extends AuthorizingRealm {

	@Autowired
	private UserManager userManager;

	private List<Permission> defaultPermission = Lists.newArrayList();

	private List<String> defaultRole = Lists.newArrayList();

	/**
	 * 设置默认permission
	 * 
	 * @param defaultPermissionString
	 *            permission 如果存在多个值，使用逗号","使用逗号分割
	 */
	public void setDefaultPermissionString(String defaultPermissionString) {
		String[] perms = StringUtils.split(defaultPermissionString, ",");

		if (null != perms && perms.length > 0) {

			for (String perm : perms) {
				WildcardPermission domainPermission = new WildcardPermission(perm);
				defaultPermission.add(domainPermission);
			}
		}

	}

	/**
	 * 设置默认role
	 * 
	 * @param defaultRoleString
	 *            role 如果存在多个值，使用逗号","使用逗号分割
	 */
	public void setDefaultRoleString(String defaultRoleString) {
		String[] roles = StringUtils.split(defaultRoleString, ",");
		CollectionUtils.addAll(defaultRole, roles);
	}

	/**
	 * 设置默认permission
	 * 
	 * @param defaultPermissions
	 *            permission
	 */
	public void setDefaultPermission(List<Permission> defaultPermission) {
		this.defaultPermission = defaultPermission;
	}

	/**
	 * 设置默认role
	 * 
	 * @param defaultRoles
	 *            role
	 */
	public void setDefaultRole(List<String> defaultRole) {
		this.defaultRole = defaultRole;
	}

	/**
	 * 当用户进行访问链接时的授权方法
	 */
	@Transactional
	protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
	
		SimpleAuthorizationInfo info = new SimpleAuthorizationInfo();

		CommonVariableModel model = (CommonVariableModel) principals.getPrimaryPrincipal();

		Assert.notNull(model, "找不到principals中的CommonVariableModel");

		// 加载用户的组信息和资源信息
		UserAuthorizationInfo authorizationInfo = model.getAuthorizationInfo();		

		// 添加用户拥有的permission
		addPermissions(info, authorizationInfo);
		// 添加用户拥有的role
		addRoles(info, authorizationInfo);

		return info;
	}

	/**
	 * 通过组集合，将集合中的role字段内容解析后添加到SimpleAuthorizationInfo授权信息中
	 * 
	 * @param info
	 *            SimpleAuthorizationInfo
	 * @param groupsList
	 *            组集合
	 */
	@Transactional
	public void addRoles(SimpleAuthorizationInfo info, UserAuthorizationInfo authorizationInfo) {

		// 查询当前用户拥有的角色

		List<String> roles = authorizationInfo.getRoles();
		//System.out.println(roles.toString());
		// 添加默认的roles到roels
		if (CollectionUtils.isNotEmpty(defaultRole)) {
			CollectionUtils.addAll(roles, defaultRole.iterator());
		}

		// 将当前用户拥有的roles设置到SimpleAuthorizationInfo中
		info.addRoles(roles);

	}

	/**
	 * 通过资源集合，将集合中的permission字段内容解析后添加到SimpleAuthorizationInfo授权信息中
	 * 
	 * @param info
	 *            SimpleAuthorizationInfo
	 * @param authorizationInfo
	 *            资源集合
	 */
	private void addPermissions(SimpleAuthorizationInfo info, UserAuthorizationInfo authorizationInfo) {
		// 解析当前用户资源中的permissions
		List<String> temp = authorizationInfo.getPerms();
		List<Permission> permissions = new ArrayList<Permission>();
		if (null != temp && temp.size()>0){
			for (String perm : temp) {
				WildcardPermission domainPermission = new WildcardPermission(perm);
				permissions.add(domainPermission);

			}
		}

		// List<Permission> permissions = getValue(temp, "perms\\[(.*?)\\]");

		// 添加默认的permissions到permissions
		if (CollectionUtils.isNotEmpty(defaultPermission)) {
			CollectionUtils.addAll(permissions, defaultPermission.iterator());
		}

		// 将当前用户拥有的permissions设置到SimpleAuthorizationInfo中

		info.addObjectPermissions(permissions);

	}

	/**
	 * 通过正则表达式获取字符串集合的值
	 * 
	 * @param obj
	 *            字符串集合
	 * @param regex
	 *            表达式
	 * @return List
	 */
	private List<Permission> getValue(List<String> obj, String regex) {

		List<Permission> result = new ArrayList<Permission>();

		if (CollectionUtils.isEmpty(obj)) {
			return result;
		}

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(StringUtils.join(obj, ","));

		while (matcher.find()) {

			String permission = matcher.group(1);

			WildcardPermission domainPermission = new WildcardPermission(permission);

			result.add(domainPermission);
		}

		return result;
	}

	@Override
	public void clearCachedAuthorizationInfo(PrincipalCollection principals) {
		super.clearCachedAuthorizationInfo(principals);
	}

	@Override
	public void clearCachedAuthenticationInfo(PrincipalCollection principals) {
		super.clearCachedAuthenticationInfo(principals);
	}

	@Override
	public void clearCache(PrincipalCollection principals) {
		super.clearCache(principals);
	}

	public void clearAllCachedAuthorizationInfo() {
		getAuthorizationCache().clear();
	}

	public void clearAllCachedAuthenticationInfo() {
		getAuthenticationCache().clear();
	}

	public void clearAllCache() {
		clearAllCachedAuthenticationInfo();
		clearAllCachedAuthorizationInfo();
	}
}
