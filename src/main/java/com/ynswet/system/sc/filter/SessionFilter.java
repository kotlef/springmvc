package com.ynswet.system.sc.filter;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.shiro.web.filter.authc.UserFilter;
import org.apache.shiro.web.util.WebUtils;

import com.ynswet.common.util.JacksonUtil;


/**
 *
 * @author elfmatian
 *
 */
public class SessionFilter extends UserFilter {

	@Override
    protected boolean onAccessDenied(ServletRequest request,
        ServletResponse response) throws Exception {

        // 判断session里是否有用户信息
		if (isAjax(request, response)) {
			String login="http://" + request.getLocalAddr()+":"+request.getLocalPort()+ "/springmvc/login";
			Map<String, Object> result = new HashMap<String, Object>();
			result.put("success", false);
			result.put("msg", "SessionException");
			result.put("result", login);
			response.setContentType("application/json;charset="
					+ request.getCharacterEncoding());
			JacksonUtil.writerJson(response.getWriter(), result);

			return false;
		}


        return super.onAccessDenied(request, response);
    }



	public boolean isAjax(ServletRequest request, ServletResponse response) {
		String jsonHeader = WebUtils.toHttp(request).getHeader("accept");
		String xmlHeader = WebUtils.toHttp(request).getHeader(
				"X-Requested-With");
		return (jsonHeader != null && jsonHeader.indexOf("application/json") > -1)
				|| (xmlHeader != null && xmlHeader.indexOf("XMLHttpRequest") > -1);
	}



}
