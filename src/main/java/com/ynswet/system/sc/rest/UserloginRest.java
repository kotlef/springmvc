/**
 * @Title: UserRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月12日 下午4:11:00
 * @version V1.0  
 * <p>Copy
 * <p>Company:广州合光信息科技有限公司</p>
 */
package com.ynswet.system.sc.rest;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynswet.common.domain.SingleJsonStructure;
import com.ynswet.common.rest.BaseRest;
import com.ynswet.system.sc.domain.Userlogin;
import com.ynswet.system.sc.repository.UserloginRepository;
import com.ynswet.system.sc.util.PasswordHelper;
import com.ynswet.system.sc.util.SystemVariableUtils;

/**
 *
 * 类功能说明
 * <p>
 * Title: UserloginRest.java
 * </p>
 *
 * @author 原勇
 * @date 2015年5月26日 下午12:13:33 类修改者 修改日期 修改说明
 * @version V1.0
 *          <p>
 *          Description:立翔云
 *          </p>
 *          <p>
 *          Copyright: Copyright (c) 2015
 *          </p>
 *          <p>
 *          Company:广州合光信息科技有限公司
 *          </p>
 */
@RestController
@RequestMapping("/userlogin")
public class UserloginRest extends BaseRest {

	@Autowired
	private UserloginRepository userloginRepository;

	@Autowired
	private PasswordHelper passwordHelper;

	/**
	 * url:/userlogin/changePassword/{uid}
	 * 函数功能说明
	 * @author 原勇
	 * @date 2015年5月26日 修改者名字 修改日期 修改内容
	 * @param @param uid
	 * @param @param password
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value = "/changePassword/{uid}", method = RequestMethod.POST)
	@Transactional
	public SingleJsonStructure updatePassword(@PathVariable Integer uid,
			@RequestParam String password) {
		SingleJsonStructure json = new SingleJsonStructure();
		List<Userlogin> userloginList = userloginRepository.findByUid(uid);
		for (Userlogin ul : userloginList) {
			ul.setPassword(password);
			passwordHelper.encryptPassword(ul);
			userloginRepository.save(ul);
		}
		userloginRepository.flush();
		json.setSuccess(true);
		json.setMsg(BaseRest.SAVE_SUCCESS);
		return json;
	}

	/**
	 * url:/userlogin/checkPassword
	 * 函数功能说明
	 * @author 李玉鹏
	 * @date 2015年8月13日 修改者名字 修改日期 修改内容
	 * @param @param oldPassword
	 * @param @return   
	 * @return Boolean   
	 * @throws
	 */
	@RequestMapping(value = "/checkPassword", method = RequestMethod.POST)
	@Transactional
	public boolean updatePassword(@RequestParam("oldPassword") String oldPassword) {
		Userlogin userlogin = SystemVariableUtils.getCommonVariableModel().getUserlogin();
		String pwd = passwordHelper.getPassword(userlogin, oldPassword);
		if (pwd.equals(userlogin.getPassword())) {
			return true;
		}else{
			return false;
		}
	}
}
