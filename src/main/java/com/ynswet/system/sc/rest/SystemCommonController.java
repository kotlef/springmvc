package com.ynswet.system.sc.rest;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.security.KeyPair;
import java.security.interfaces.RSAPublicKey;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.web.servlet.Cookie;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.code.kaptcha.Producer;
import com.ynswet.common.rest.BaseRest;
import com.ynswet.system.sc.domain.Homepage;
import com.ynswet.system.sc.domain.Menu;
import com.ynswet.system.sc.domain.Org;
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.domain.Role;
import com.ynswet.system.sc.domain.User;
import com.ynswet.system.sc.domain.Userlogin;
import com.ynswet.system.sc.encryption.EncryptionBean;
import com.ynswet.system.sc.encryption.PublicKeyMap;
import com.ynswet.system.sc.model.CommonVariableModel;
import com.ynswet.system.sc.realm.UserManager;
import com.ynswet.system.sc.repository.HomepageRepository;
import com.ynswet.system.sc.repository.UserloginRepository;
import com.ynswet.system.sc.util.SystemVariableUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * 系统安全控制器
 *
 * @author elfmatian
 *
 */
@Controller
public class SystemCommonController extends BaseRest {

	@Autowired
	private UserManager userManager;

	@Autowired
	private EncryptionBean eb;

	@Autowired
	private HomepageRepository homepageRepository;
	
	@Autowired
	private EhCacheManager ehCacheManager;
	
	@Autowired
	private UserloginRepository  userloginRepository;


	/**
	 * 管理平台登录C，返回登录页面。当C发现当前用户已经登录名且认真后，会自动跳转到index页面
	 *
	 * @return String
	 */
	@RequestMapping(value = "/login")
	public String loginAction() {

		if (!SystemVariableUtils.isAuthenticated()) {
			return "loginPage";
		}
		return "redirect:/index";
	}
	
	
	@RequestMapping(value = "/unauthorized")
	public String unauthorized() {
			return "loginPage";
	}
	
	@RequestMapping(value = "/date")
	@ResponseBody
	public String unauthorized(@RequestParam Date test) {
			System.out.println(test.toString());
			return test.toString();
	}


	/**
	 * 首页C,在request用翻入当前用户的菜单集合给页面循环
	 *
	 * @return String
	 */
	@RequestMapping(value="/nav",method=RequestMethod.GET)
	public String nav(
			Model model,HttpServletRequest request){

		CommonVariableModel cm=SystemVariableUtils.getCommonVariableModel();
		Userlogin userlogin =cm.getUserlogin();
		Integer uid=userlogin.getUid();
		String homepageId=getHomepageId(request, uid);
		List<Homepage> homepageList = userManager.getHomepageByUserId(uid);
		List<Menu> menuList = userManager.getMenusByUserId(uid);
		User user =userManager.getUserByUserId(uid);
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

	private String getHomepageId(HttpServletRequest request,Integer uid){
		
		String homepageId="0";
		javax.servlet.http.Cookie[] cookies = request.getCookies();
		if (cookies != null) {
			for (javax.servlet.http.Cookie cookie : cookies) {
				if (cookie.getName().equals(String.valueOf(uid))) {
					homepageId=cookie.getValue();
				}
			}
		}
		return homepageId;
	}
	@RequestMapping( value="/index")
	public String index(Model model,HttpServletRequest request) {
		CommonVariableModel cm=SystemVariableUtils.getCommonVariableModel();
		Userlogin userlogin =cm.getUserlogin();
		Integer uid=userlogin.getUid();
		String homepageId=getHomepageId(request, uid);
		List<Homepage> homepageList= userManager.getHomepageByUserId(uid);
		Homepage homepage;
		if(null!=homepageList && homepageList.size()>0 ){
			if (homepageId.equals("0")) {
				homepage=homepageList.get(0);
			}else{
				homepage = homepageRepository.getOne(Integer.valueOf(homepageId));
			}
			model.addAttribute("homepage", homepage.getHomepageUrl());
		}else
			model.addAttribute("homepage","-1");
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
		Userlogin userlogin =cm.getUserlogin();
		Integer uid=userlogin.getUid();
		Map<String,Object> currentUserInfo=new HashMap<String,Object>();
		currentUserInfo.put("user", userManager.getUserByUserId(uid));
		 List<Org> orgList=userManager.getOrgsByUserId(uid);
		 List<Role> roleList=userManager.getRolesByUserId(uid);
		 List<Res> resList=userManager.getRessByUserId(uid);
		currentUserInfo.put("org", orgList);
		currentUserInfo.put("role", roleList);
		currentUserInfo.put("res", resList);
		return currentUserInfo;
	}

	@RequestMapping(value = "/security",method=RequestMethod.GET)
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

	@RequestMapping(value="/changePassword",method=RequestMethod.POST)
	@ResponseBody
	public String changePassword(
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword) {
		CacheManager cm=ehCacheManager.getCacheManager();
		Cache cache=cm.getCache("shiroAuthenticationCache");
		CommonVariableModel cvm=SystemVariableUtils.getCommonVariableModel();
		Userlogin userlogin =cvm.getUserlogin();
		if (userManager.updateUserPassword(oldPassword, newPassword)) {
			Integer uid=userlogin.getUid();
			List<Userlogin> userlogins=userloginRepository.findByUid(uid);
			for(Userlogin ul:userlogins){
				cache.remove(ul.getLoginString());
			}
			return "true";
		}
		return "false";
	}

	private Producer captchaProducer;

	@Autowired
	public void setCaptchaProducer(Producer captchaProducer) {
		this.captchaProducer = captchaProducer;
	}

	@RequestMapping( value="/getCaptcha",method=RequestMethod.GET)
	public void getCaptcha(HttpServletRequest request,
			HttpServletResponse response, HttpSession session)
			throws IOException {

		response.setDateHeader("Expires", 0);
		// Set standard HTTP/1.1 no-cache headers.
		response.setHeader("Cache-Control",
				"no-store, no-cache, must-revalidate");
		// Set IE extended HTTP/1.1 no-cache headers (use addHeader).
		response.addHeader("Cache-Control", "post-check=0, pre-check=0");
		// Set standard HTTP/1.0 no-cache header.
		response.setHeader("Pragma", "no-cache");
		// return a jpeg
		response.setContentType("image/jpeg");
		// create the text for the image
		String capText = captchaProducer.createText();
		// store the text in the session
		session.setAttribute("captcha", capText);
		Cookie cookie = new SimpleCookie("captcha");
		cookie.setValue(capText);
		cookie.setHttpOnly(false);
		cookie.saveTo((HttpServletRequest) request, response);
		// create the image with the text
		BufferedImage bi = captchaProducer.createImage(capText);

		ServletOutputStream out = response.getOutputStream();
		// write the data out
		ImageIO.write(bi, "jpg", out);
		try {
			out.flush();
		} finally {
			out.close();
		}

	}

}
