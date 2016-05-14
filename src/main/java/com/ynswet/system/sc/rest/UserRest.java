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

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ynswet.common.rest.BaseRest;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.ynswet.common.domain.ListJsonStructure;
import com.ynswet.common.domain.SingleJsonStructure;
import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.domain.User;
import com.ynswet.system.sc.domain.Userlogin;
import com.ynswet.system.sc.model.CommonVariableModel;
import com.ynswet.system.sc.realm.UserManager;
import com.ynswet.system.sc.repository.UserRepository;
import com.ynswet.system.sc.repository.UserloginRepository;
import com.ynswet.system.sc.service.UserService;
import com.ynswet.system.sc.util.PasswordHelper;
import com.ynswet.system.sc.util.SystemVariableUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * 
 * 类功能说明
 * <p>Title: UserRest.java</p>
 * @author 孙越
 * @date 2015年8月5日 下午4:21:59
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/user")
public class UserRest extends BaseRest {

	@Autowired
	private UserService userService;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private UserloginRepository userloginRepository;

	@Autowired
	private PasswordHelper passwordHelper;
	
	@Autowired
	private UserManager userManager;
	
	@Autowired
	private EhCacheManager ehCacheManager;


	/**
	 * 当前用户修改密码
	 *
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 *
	 * @return String
	 */

	@RequestMapping(value="/changePassword",method = RequestMethod.POST)
	@ResponseBody
	public SingleJsonStructure changePassword(
			@RequestParam("uid") Integer uid,
			@RequestParam("openid") String openid,
			@RequestParam("oldPassword") String oldPassword,
			@RequestParam("newPassword") String newPassword) {
		SingleJsonStructure json = new SingleJsonStructure();
		User user = userRepository.findByUidAndOpenid(uid,openid);
		if(user!=null){
			Userlogin userlogin = userloginRepository.findByLoginString(user.getCell());
			String pwd = passwordHelper.getPassword(userlogin, oldPassword);
			if (pwd.equals(userlogin.getPassword())) {
				List<Userlogin> userloginList = userloginRepository.findByUid(userlogin.getUid());
				for (Userlogin ul : userloginList) {
					ul.setPassword(newPassword);
					passwordHelper.encryptPassword(ul);
					userloginRepository.save(ul);
				}
				userloginRepository.flush();
				json.setSuccess(true);
				json.setMsg("密码修改成功");
			}else{
				json.setSuccess(false);
				json.setMsg("原密码错误");
			}
		}else{
			json.setSuccess(false);
			json.setMsg("用户不存在");
		}
		return json;
	}



	/**
	 * url：/ynlxhealth/user
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
		User checkUser = userRepository.checkUserUniqueness(user.getCell(), user.getIdNo());
		if(null!=checkUser){
			json.setSuccess(false);
			json.setMsg(BaseRest.SAVE_FAILURE);
		}else{
			user.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
			user.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
			user.setStatus("0");
			user.setSecPassword("123456");
			user.setBindPassword("654321");
			User saveUser = userRepository.saveAndFlush(user);
			List<String> loginStringList=new ArrayList<String>();
			loginStringList.add(saveUser.getCell());
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
	 * url:/ynlxhealth/user/{uids}
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
			CacheManager cm= ehCacheManager.getCacheManager();
			Cache cache=cm.getCache("shiroAuthenticationCache");
			for(String uid : arrUids){
				userRepository.delete(Integer.valueOf(uid));
				userRepository.flush();
				List<Userlogin> list= userloginRepository.findByUid(Integer.valueOf(uid));//需要清除缓存的uid
				for(Userlogin ul:list){
					cache.remove(ul.getLoginString());//登录账号全清空
				}
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.DELTE_SUCCESS);
		return json;
	}

	/**
	 * url:/ynlxhealth/user
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
	 * 
	 * 函数功能说明
	 * @author 刘培琪 
	 * @date 2015年8月3日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return ListJsonStructure<User>   
	 * @throws
	 */
	@RequestMapping(value="userType",method = RequestMethod.GET)
	public ListJsonStructure<User> findAllEmp() {
		List<User> listUsers = userRepository.findAllEmp();
		ListJsonStructure<User> json = new ListJsonStructure<User>();
		json.setRows(listUsers);
		return json;

	}
	
	/**
	 * url:/ynlxhealth/user/toPage
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
	 * url:/ynlxhealth/user/items/toPage
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
			@RequestParam("value")String value) {
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
		Pageable pageable = new PageRequest(page-1, rows);
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
	 * url:/ynlxhealth/user/checkEmail
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
	 * url:/ynlxhealth/user/checkCell
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
	 * url:/ynlxhealth/user/checkIdNo
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
		User user = userRepository.findByIdNo(idNo);
		if(null!=user){
			return false;
		}else{
			return true;
		}
	}

	/**
	 *
	 * 获取当前用户资源（小区）
	 * @author 张明坤
	 * @date 2015年6月22日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return   
	 * @return ListJsonStructure<Res>   
	 * @throws
	 */
	@RequestMapping(value="/searchCurCommunity",method = RequestMethod.GET)
	public ListJsonStructure<Res> searchCurCommunity() {
		ListJsonStructure<Res> json = new ListJsonStructure<Res>();
		CommonVariableModel cm = SystemVariableUtils.getCommonVariableModel();
		Userlogin userlogin =cm.getUserlogin();
		Integer uid=userlogin.getUid();
		List<Res> resList=userManager.getRessByUserId(uid);
		List<Res> comList=new ArrayList<Res>();
		for(Res res:resList){
			if(res.getResType().equals("C")){
				comList.add(res);
			}
		}
		json.setRows(comList);
		json.setTotal(comList.size());
		return json;
	}

	/**
	 * url:/ynlxhealth/user/{uid}
	 * 函数功能说明:更新用户
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uid
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{uid}",method = RequestMethod.GET)
	public SingleJsonStructure updateUser(@PathVariable Integer uid) {
		// TODO Auto-generated method stub
		User user = userRepository.findOne(uid);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.UPDATE_SUCCESS);
		json.setRows(user);
		json.setSuccess(true);
		return json;
	}
	
	/**
	 * url：/ynlxhealth/user/addUser/{}
	 * 函数功能说明:为客户添加成员
	 * @author 孙越
	 * @date 2015年7月29日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param user   
	 * @return void   
	 * @throws
	 */
	@RequestMapping(value="/addUser/{obj}",method = RequestMethod.POST)
	public Integer saveCustomerUser(@ModelAttribute User user,@PathVariable String obj){
		System.out.println("获取到了？"+obj);
		if(!obj.isEmpty()){
			String [] arrIds = obj.split(",");
			user.setUserName(arrIds[1]);
			user.setGender(arrIds[2]);
			user.setCell(arrIds[3]);
			user.setEmail(arrIds[5]);
			user.setIdNo(arrIds[4]);
		}
		
		Timestamp createTime = new Timestamp(System.currentTimeMillis());
		user.setCreateTime(createTime);
		user.setIdType("0");
		user.setUserType("c");
		user.setEducation("0");
		user.setCell2("0");
		user.setMarriage("0");
		user.setBlood("0");
		user.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		user.setStatus("0");
		user.setNote("客户成员");
		User users=userRepository.saveAndFlush(user);
		Integer uid=users.getUid();
		return uid;
	}
	/**
	 * url:ynlxhealth/user/
	 * 函数功能说明：为房屋添加住户
	 * @author 孙越 
	 * @date 2015年8月22日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param user
	 * @param @param obj
	 * @param @return    
	 * @return Integer   
	 * @throws
	 */
	@RequestMapping(value="/addUser",method = RequestMethod.POST)
	public SingleJsonStructure saveHouseUser(@ModelAttribute User user){
		Timestamp createTime = new Timestamp(System.currentTimeMillis());
		user.setCreateTime(createTime);
		User users=userRepository.saveAndFlush(user);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.SAVE_SUCCESS);
		json.setRows(users.getUid());
		return json;
	}
	/**
	 * url：/ynlxhealth/user
	 * 函数功能说明:查询房屋下的用户
	 * @author 孙越
	 * @date 2015年7月30日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param user   
	 * @return void   
	 * @throws
	 */
	@RequestMapping(value="/searchUserByUid",method = RequestMethod.GET)
	public List<User> searchUserByOrgidAndRoleId(@RequestParam  String UID) {
		List<User> list=new ArrayList<User>();
		if(!UID.isEmpty()){
			String [] arrIds = UID.split(",");
			for(int i = 0;i<arrIds.length;i++){
				User user=new User();
				user=userRepository.findUserByUid(Integer.valueOf(arrIds[i]));
				list.add(user);
			}
		}
		return list;
	}
	/**
	 * url:/business/user/updateCUser
	 * 函数功能说明:修改客户成员信息
	 * @author 孙越
	 * @date 2015年8月4日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param Product
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/updateCUser/{obj}",method = RequestMethod.POST)
	public SingleJsonStructure save(@RequestParam String obj){
		String [] arrIds =obj.split(",");
		Integer uid=Integer.parseInt(arrIds[0]);
		String userName=arrIds[1];
		String gender=arrIds[2];
		String cell=arrIds[3];
		User user = userRepository.findOne(uid);
		user.setUserName(userName);
		user.setGender(gender);
		user.setCell(cell);
		userRepository.saveAndFlush(user);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.SAVE_SUCCESS);
		return json;
	}
	/**
	 * url:/user/searchByorgids
	 * 函数功能说明:查当前用户组织下的用户
	 * @author 张毕思 
	 * @date 2015年10月26日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return ListJsonStructure<User>   
	 * @throws
	 */
	@RequestMapping(value="/searchByorgids",method = RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> searchByorgids(
			@RequestParam("orgids")String orgids,
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		Pageable pageable = new PageRequest(page-1, size);
		ListJsonStructure<Map<String,Object>> json =null;
		List<Integer> list = new ArrayList<Integer>();
		if(!orgids.isEmpty()){
			String [] arrOrgids = orgids.split(",");
			for(int i=0;i<arrOrgids.length;i++){
				list.add(Integer.valueOf(arrOrgids[i]));
			}
			json=userService.findInOrgids(list, pageable);
		}else{
			json=new ListJsonStructure<Map<String,Object>>();
		}
		json.setMsg(BaseRest.FIND_SUCCESS);
		return json;
	}
	
	/**
	 * url:/user/searchByOrgidAndValue
	 * 函数功能说明:条件查询当前用户组织下的用户
	 * @author 张毕思 
	 * @date 2015年10月26日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return ListJsonStructure<User>   
	 * @throws
	 */
	@RequestMapping(value="/searchByOrgidAndValue",method = RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> searchByOrgidAndValue(
			@RequestParam("orgids")String orgids,
			@RequestParam("value")String value,
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		List<Integer> list = new ArrayList<Integer>();
		Pageable pageable = new PageRequest(page-1, size);
		ListJsonStructure<Map<String,Object>> json=null;
		if(!orgids.isEmpty()){
			String [] arrOrgids = orgids.split(",");
			for(int i=0;i<arrOrgids.length;i++){
				list.add(Integer.valueOf(arrOrgids[i]));
			}
			json = userService.searchByValue(list,value, pageable);
		}else{
			json = new ListJsonStructure<Map<String,Object>>();
		}
		json.setMsg(BaseRest.FIND_SUCCESS);
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
	@RequestMapping(value="/searchByCell",method = RequestMethod.GET)
	public SingleJsonStructure searchByCellAndUidNotInCustomer(
			@RequestParam("cell")String cell) {
		SingleJsonStructure json = new SingleJsonStructure();
		//User user = userRepository.searchByCellAndUidNotInCustomer(cell);
		User user = userRepository.findByCell(cell);
		if(user==null){
			json.setSuccess(false);
		}
		json.setRows(user);
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明:根据用户手机号或身份证或邮箱查找用户
	 * @author 刘培琪 
	 * @date 2015年12月30日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param value（可以是手机号、身份证、邮箱）
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/findByItem",method = RequestMethod.GET)
	public SingleJsonStructure findByItem(@RequestParam(value="value")String value){
		SingleJsonStructure json = new SingleJsonStructure();
		User user = userRepository.findByItem(value);
		if(null == user){
			json.setSuccess(false);
		}
		json.setRows(user);
		return json;
	}

	/**
	 * url:/ynlxhealth/user/update/{uid}
	 * 函数功能说明:业务办理更新用户
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param user
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/update/info",method = RequestMethod.POST)
	public SingleJsonStructure updateUserInfo(@ModelAttribute User user) {
		SingleJsonStructure json = new SingleJsonStructure();
		User oldUsr = userRepository.findByCell(user.getCell());
		user.setUid(oldUsr.getUid());
		user.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		user.setCreateTime(oldUsr.getCreateTime());
		user.setSecPassword(oldUsr.getSecPassword());
		user.setBindPassword(oldUsr.getBindPassword());
		//
		user.setHeight(oldUsr.getHeight());
		user.setWeight(oldUsr.getWeight());
		user.setGradSchool(oldUsr.getGradSchool());
		user.setMajor(oldUsr.getMajor());
		user.setGraduationDate(oldUsr.getGraduationDate());
		user.setOccupation(oldUsr.getOccupation());
		user.setJobTitle(oldUsr.getJobTitle());
		user.setZip(oldUsr.getZip());
		//
		if(user.getStatus()==null){
			user.setStatus("0");
		}
		User updateUser = userRepository.saveAndFlush(user);
		json.setMsg(BaseRest.UPDATE_SUCCESS);
		json.setRows(updateUser.getUid());
		System.out.println("修改房屋住户成员------------------"+json.getRows());
		return json;
	}

	/**
	 * url:/ynlxhealth/user/update/{uid}
	 * 函数功能说明:业务办理更新用户
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param user
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/update/info/{uid}",method = RequestMethod.POST)
	public SingleJsonStructure updateUserInfo(@PathVariable Integer uid,@ModelAttribute User user) {
		SingleJsonStructure json = new SingleJsonStructure();
		User oldUsr = userRepository.findOne(uid);
		user.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		user.setCreateTime(oldUsr.getCreateTime());
		user.setSecPassword(oldUsr.getSecPassword());
		user.setBindPassword(oldUsr.getBindPassword());
		if(user.getStatus()==null){
			user.setStatus("0");
		}
		User updateUser = userRepository.saveAndFlush(user);
		json.setMsg(BaseRest.UPDATE_SUCCESS);
		json.setRows(updateUser.getUid());
		System.out.println("修改房屋住户成员------------------"+json.getRows());
		return json;
	}

	/**
	 * url:/ynlxhealth/user/update/{uid}
	 * 函数功能说明:业务办理更新用户
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param user
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/findByCell",method = RequestMethod.GET)
	public SingleJsonStructure findByCell(@RequestParam("cell")String cell) {
		SingleJsonStructure json = new SingleJsonStructure();
		User user = userRepository.findByCell(cell);
		if(user==null){
			json.setSuccess(true);
			return json;
		}
		json.setRows(user);
		json.setMsg(BaseRest.FIND_SUCCESS);
		json.setSuccess(false);
		return json;
	}

	/**
	 * url:/ynlxhealth/user/update/{uid}
	 * 函数功能说明:业务办理更新用户
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param user
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/findByIdNo",method = RequestMethod.GET)
	public SingleJsonStructure findByIdNo(@RequestParam("idNo")String idNo) {
		SingleJsonStructure json = new SingleJsonStructure();
		User user = userRepository.findByIdNo(idNo);
		if(user==null){
			json.setSuccess(true);
			return json;
		}
		json.setRows(user);
		json.setMsg(BaseRest.FIND_SUCCESS);
		json.setSuccess(false);
		return json;
	}
	
}
