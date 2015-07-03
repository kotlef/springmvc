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

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynswet.common.domain.ListJsonStructure;
import com.ynswet.common.domain.SingleJsonStructure;
import com.ynswet.common.rest.BaseRest;
import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.User;
import com.ynswet.system.sc.domain.Userlogin;
import com.ynswet.system.sc.repository.UserRepository;
import com.ynswet.system.sc.repository.UserloginRepository;
import com.ynswet.system.sc.service.UserService;
import com.ynswet.system.sc.util.PasswordHelper;

/**
 *
 * 类功能说明：用户管理
 * <p>Title: UserRest.java</p>
 * @author 李玉鹏
 * @date 2015年5月12日 下午4:11:00
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/user")
public class UserRest extends BaseRest{

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserloginRepository userloginRepository;
	
	@Autowired
	private PasswordHelper passwordHelper;

	/**
	 * url：/springmvc/user
	 * 函数功能说明:添加用户
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param user   
	 * @return void   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure saveUser(@ModelAttribute User user){
		SingleJsonStructure json = new SingleJsonStructure();
		User checkUser = userRepository.checkUserUniqueness(user.getCell(), user.getIdNo(), user.getEmail());
		if(null!=checkUser){
			json.setSuccess(false);
			json.setMsg(BaseRest.FAILURE);
		}else{
			user.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
			user.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
			user.setStatus("0");
			User saveUser = userRepository.saveAndFlush(user);
			List<String> loginStringList=new ArrayList<String>();
			loginStringList.add(saveUser.getCell());
			loginStringList.add(saveUser.getEmail());
			loginStringList.add(saveUser.getIdNo());
			List<Userlogin> listlogin = new ArrayList<Userlogin>();
			for(String loginString:loginStringList){
				Userlogin userlogin = new Userlogin();
				userlogin.setPassword(BaseRest.DEFAULT_PASSWORD);
				userlogin.setUid(saveUser.getUid());
				userlogin.setStatus(saveUser.getStatus());
				userlogin.setLoginString(loginString);
				userlogin = passwordHelper.encryptPassword(userlogin);
				listlogin.add(userlogin);
			}
			userloginRepository.save(listlogin);
			userloginRepository.flush();
			json.setRows(saveUser.getUid());
			json.setMsg(BaseRest.SAVE_SUCCESS);
		}
		return json;
	}

	/**
	 * url:/springmvc/user/{uid}
	 * 函数功能说明:更新用户
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param user
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{uid}",method = RequestMethod.POST)
	public SingleJsonStructure updateUser(@PathVariable Integer uid,@ModelAttribute User user) {
		// TODO Auto-generated method stub
		User oldUsr = userRepository.findOne(uid);
		user.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		user.setCreateTime(oldUsr.getCreateTime());
		userRepository.saveAndFlush(user);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.UPDATE_SUCCESS);
		return json;

	}
	/**
	 * url:/springmvc/user/{uids}
	 * 函数功能说明:删除用户
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uids
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{uids}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteUsers (@PathVariable String uids) {
		if(!uids.isEmpty()){
			String[] arrUids = uids.split(",");
			for(String uid : arrUids){
				userRepository.delete(Integer.valueOf(uid));
				userRepository.flush();
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.DELTE_SUCCESS);
		return json;
	}

	/**
	 * url:/springmvc/user
	 * 函数功能说明:查询所有的用户
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return   
	 * @return ListJsonStructure<User>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ListJsonStructure<User> findAlls() {
		List<User> listUsers = userRepository.findAll();
		ListJsonStructure<User> json = new ListJsonStructure<User>();
		json.setRows(listUsers);
		return json;

	}
	/**
	 * url:/springmvc/user/toPage
	 * 函数功能说明:分页查询用户
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param rows
	 * @param @return   
	 * @return ListJsonStructure<User>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<User> findAlltoPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		Pageable pager = new PageRequest(page-1, size);
		Page<User> list = userRepository.findAll(pager);
		ListJsonStructure<User> json = new ListJsonStructure<User>();
		json.setRows(list.getContent());
		json.setTotal((int) list.getTotalElements());
		return json;
	}

	/**
	 * url:/springmvc/user/items/toPage
	 * 函数功能说明:根据条件分页查询用户
	 * @author 李玉鹏
	 * @date 2015年5月25日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param userName
	 * @param @param phone
	 * @param @param pageSize
	 * @param @param rows
	 * @param @return   
	 * @return ListJsonStructure<User>   
	 * @throws
	 */
	@RequestMapping(value="/searchByItems",method = RequestMethod.GET)
	public ListJsonStructure<User> findByUserName(
			@RequestParam("userName")String userName,
			@RequestParam("phone")String phone,
			@RequestParam("page")int page,
			@RequestParam("rows")int rows
			) {
		Pageable pageable = new PageRequest(rows, page);
		Page<User> pages = userRepository.findByUserName(userName, pageable);
		ListJsonStructure<User> json = new ListJsonStructure<User>();
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 李玉鹏 
	 * @date 2015年5月28日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key
	 * @param @param value
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/searchByKeyValue",method = RequestMethod.GET)
	public SingleJsonStructure searchByKeyValue(
			@RequestParam("key")String key,
			@RequestParam("value")String value
			) {
		User user = null ;
		switch (key) {
		case "cell":
			user = userRepository.findByCell(value);
			break;
		case "email":
			user = userRepository.findByEmail(value);
			break;
		case "idNo":
			user = userRepository.findByIdNo(value);
			break;
		case "userName":
			user = userRepository.findByUserName(value);
			break;

		default:
			break;
		}
		SingleJsonStructure json = new SingleJsonStructure();
		if(user==null){
			json.setSuccess(false);
		}
		json.setRows(user);
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 李玉鹏 
	 * @date 2015年5月28日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key
	 * @param @param value
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/searchByKeyValueLike/toPage",method = RequestMethod.GET)
	public ListJsonStructure<User> searchByKeyValueLikeToPage(
			@RequestParam("key")String key,
			@RequestParam("value")String value,
			@RequestParam("page")int page,
			@RequestParam("rows")int rows
			) {
		Page<User> pages = null ;
		Pageable pageable = new PageRequest(rows, page);
		switch (key) {
		case "cell":
			pages = userRepository.findByCellLike(value,pageable);
			break;
		case "email":
			pages = userRepository.findByEmailLike(value,pageable);
			break;
		case "idNo":
			pages = userRepository.findByIdNoLike(value,pageable);
			break;
		case "userName":
			pages = userRepository.findByUserNameLike(value,pageable);
			break;

		default:
			break;
		}
		ListJsonStructure<User> json = new ListJsonStructure<User>();
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}
	
	/**
	 * url:/springmvc/user/checkEmail
	 * 函数功能说明:检查用户邮箱唯一性
	 * @author 李玉鹏 
	 * @date 2015年5月27日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param email
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/checkEmail",method = RequestMethod.POST)
	public boolean checkByEmail(@RequestParam("email")String email) {
		User user = userRepository.findByEmail(email);
		if(null!=user){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * url:/springmvc/user/checkCell
	 * 函数功能说明:检查用户联系手机唯一性
	 * @author 李玉鹏 
	 * @date 2015年5月27日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param cell
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/checkCell",method = RequestMethod.POST)
	public boolean checkByCell(@RequestParam("cell")String cell) {
		User user = userRepository.findByCell(cell);
		if(null!=user){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * url:/springmvc/user/checkIdNo
	 * 函数功能说明:检查用户证件号唯一性
	 * @author 李玉鹏 
	 * @date 2015年5月27日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param idNo
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/checkIdNo",method = RequestMethod.POST)
	public boolean checkByIdNo(@RequestParam("idNo")String idNo) {
		User user = userRepository.findByEmail(idNo);
		if(null!=user){
			return false;
		}else{
			return true;
		}
	}
}
