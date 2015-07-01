package com.ynswet.system.sc.rest;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ynswet.system.sc.domain.Homepage;
import com.ynswet.system.sc.domain.Menu;
import com.ynswet.system.sc.domain.Org;
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.domain.Role;
import com.ynswet.system.sc.domain.User;
import com.ynswet.system.sc.encryption.EncryptionBean;
import com.ynswet.system.sc.encryption.PublicKeyMap;
import com.ynswet.system.sc.model.CommonVariableModel;
import com.ynswet.system.sc.realm.UserManager;
import com.ynswet.system.sc.repository.HomepageRepository;
import com.ynswet.system.sc.util.SystemVariableUtils;

/**
 * 系统安全控制器
 *
 * @author elfmatian
 *
 */
@Controller
public class SystemCommonController {

	@Autowired
	private UserManager userManager;

	@Autowired
	private EncryptionBean eb;

	@Autowired
	private HomepageRepository homepageRepository;


	/**
	 * 管理平台登录C，返回登录页面。当C发现当前用户已经登录名且认真后，会自动跳转到index页面
	 *
	 * @return String
	 */
	@RequestMapping(value = "/login")
	public String loginAction() {

		if (!SystemVariableUtils.isAuthenticated()) {
			return "login";
		}
		return "redirect:/index";
	}

	/**
	 * 首页C,在request用翻入当前用户的菜单集合给页面循环
	 *
	 * @return String
	 */
	@RequestMapping("/nav")
	public String nav(
			Model model,
			@CookieValue(value = "homepageId", defaultValue = "0") String homepageId) {
		CommonVariableModel cm=SystemVariableUtils.getCommonVariableModel();
		List<Homepage> homepageList = cm.getHomepageList();
		List<Menu> menuList = cm.getMenusList();
		User user =cm.getUser();
		model.addAttribute("homepageList", homepageList);
		model.addAttribute("menuList", menuList);
		model.addAttribute("user", user);
		if (null != homepageList && homepageList.size() > 0) {
			if (homepageId.equals("0")) {
				model.addAttribute("homepageId", homepageList.get(0)
						.getHomepageId());
			} else
				model.addAttribute("homepageId", homepageId);
		}
		return "nav";

	}

	@RequestMapping("/index")
	public String index(Model model,@CookieValue(value = "homepageId", defaultValue = "0") String homepageId) {
		List<Homepage> homepageList = SystemVariableUtils.getCommonVariableModel().getHomepageList();
		Homepage homepage;
		if (homepageId.equals("0")) {
			homepage=homepageList.get(0);
		}else{
			homepage = homepageRepository.getOne(Integer.valueOf(homepageId));
		}
		model.addAttribute("homepage", homepage.getHomepageUrl());
		return "homepage";
	}

	/**
	 *
	 * 函数功能说明
	 * @author 原勇
	 * @date 2015年6月23日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return   
	 * @return Map<String,Object> 
	 * JSON数据格式： {role:[],org:[],res:[],user:{}}
	 * @throws
	 */
	@RequestMapping(value="/currentUserInfo",method=RequestMethod.GET)
	@ResponseBody
	public Map<String,Object> currentUserInfo() {
		CommonVariableModel cm = SystemVariableUtils.getCommonVariableModel();
		User user =cm.getUser();
		Map<String,Object> currentUserInfo=new HashMap<String,Object>();
		currentUserInfo.put("user", user);
		 List<Org> orgList=cm.getOrgList();
		 List<Role> roleList=cm.getRoleList();
		 List<Res> resList=cm.getResList();
		currentUserInfo.put("org", orgList);
		currentUserInfo.put("role", roleList);
		currentUserInfo.put("res", resList);
		return currentUserInfo;
	}

	@RequestMapping(value = "/security")
	public void getRsaPublicKey(HttpServletRequest request,
			HttpServletResponse response) {
		Cookie cookie = new SimpleCookie("rId");
		KeyPair keyPair = eb.generateKeyPair();
		PublicKeyMap pkm = eb.getHexPublicKeyMap((RSAPublicKey) keyPair
				.getPublic());
		String cookieValue = pkm.getModulus();
		cookie.setValue(cookieValue);
		cookie.setHttpOnly(false);
		cookie.saveTo((HttpServletRequest) request, response);
		HttpSession session = request.getSession();
		session.setAttribute(cookieValue, keyPair);
	}


	@RequestMapping("/mobileLogout")
	@ResponseBody
	public String mobileLogout() {
		Subject  subject=SecurityUtils.getSubject();
	     subject.logout();
		return "true";
	}


	/**
	 * 当前用户修改密码C.修改成功返回"true"否则返回"false"
	 *
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 *
	 * @return String
	 */

	@RequestMapping("/changePassword")
	@ResponseBody
	@CacheEvict(value = "shiroAuthenticationCache", allEntries = true)
	public String changePassword(
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword) {

		if (userManager.updateUserPassword(oldPassword, newPassword)) {
			return "true";
		}

		return "false";

	}

}
