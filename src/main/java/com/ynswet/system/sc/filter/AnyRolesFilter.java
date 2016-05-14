package com.ynswet.system.sc.filter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;

public class AnyRolesFilter extends AuthorizationFilter {

	protected boolean isAccessAllowed(ServletRequest request,
			ServletResponse response, Object mappedValue) throws Exception {
		String[] roles = (String[]) mappedValue;
		Subject subject = getSubject(request, response);
		if (roles == null) {
			return true;// 如果没有设置角色参数，默认成功
		}
		for (String role : roles) {
			if (subject.hasRole(role)) {
				return true;
			}
		}
		return false;// 跳到onAccessDenied处理
	}

}
