/**
 * @Title: EmpRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月13日 上午11:45:36
 * @version V1.0  
 * <p>Copy
 * <p>Company:广州合光信息科技有限公司</p>
 */
package com.ynswet.system.sc.rest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
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
import com.ynswet.system.sc.domain.Emp;
import com.ynswet.system.sc.domain.User;
import com.ynswet.system.sc.domain.Userlogin;
import com.ynswet.system.sc.model.CommonVariableModel;
import com.ynswet.system.sc.repository.EmpRepository;
import com.ynswet.system.sc.repository.UserRepository;
import com.ynswet.system.sc.repository.UserloginRepository;
import com.ynswet.system.sc.service.EmpService;
import com.ynswet.system.sc.util.PasswordHelper;
import com.ynswet.system.sc.util.SystemVariableUtils;

/**
 *
 * 类功能说明：员工管理
 * <p>Title: EmpRest.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午11:45:36
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/emp")
public class EmpRest extends BaseRest{

	@Autowired
	private EmpService empService;

	@Autowired
	private EmpRepository empRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserloginRepository userloginRepository;
	
	@Autowired
	private PasswordHelper passwordHelper;
	
	@Autowired
	private EhCacheManager ehCacheManager;
	

	/**
	 * url:/ynlxmainarchive/emp
	 * 函数功能说明:保存员工信息
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param Emp
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure save(@ModelAttribute Emp emp,@ModelAttribute User user){
		Timestamp createTime = new Timestamp(System.currentTimeMillis());
		Date modifyTime = DateTimeUtils.getSystemCurrentTimeMillis();
		SingleJsonStructure json = new SingleJsonStructure();
		User checkUser = userRepository.checkUserUniqueness(user.getCell(), user.getIdNo());
		if(null!=checkUser){
			json.setSuccess(false);
			json.setMsg(BaseRest.SAVE_FAILURE);
		}else{
			user.setUserType(emp.getEmpType());
			user.setCreateTime(createTime);
			user.setSecPassword("123456");
			user.setBindPassword("654321");
			user.setModifyTime(modifyTime);
			User u = userRepository.saveAndFlush(user);
			List<String> loginStringList=new ArrayList<String>();
			loginStringList.add(u.getCell());
			loginStringList.add(u.getIdNo());
			List<Userlogin> listlogin = new ArrayList<Userlogin>();
			for(String loginString:loginStringList){
				Userlogin userlogin = new Userlogin();
				userlogin.setPassword(BaseRest.DEFAULT_PASSWORD);
				userlogin.setUid(u.getUid());
				userlogin.setStatus(u.getStatus());
				userlogin.setLoginString(loginString);
				userlogin = passwordHelper.encryptPassword(userlogin);
				listlogin.add(userlogin);
			}
			userloginRepository.save(listlogin);
			userloginRepository.flush();
			if(emp.getEmpPassword().isEmpty()){
				//后期修改
				emp.setEmpPassword("123456");
			}
			emp.setParentId(0);
			emp.setUid(u.getUid());
			emp.setCreateTime(createTime);
			emp.setModifyTime(modifyTime);
			empRepository.saveAndFlush(emp);
			json.setRows(u.getUid());
			json.setMsg(BaseRest.SAVE_SUCCESS);
		}
		return json;
	}
	
	/**
	 * url：/springmvc/emp/{uid}
	 * 函数功能说明:更新员工信息
	 * @author 李玉鹏
	 * @date 2015年5月23日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uid
	 * @param @param emp
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{uid}",method = RequestMethod.POST)
	public SingleJsonStructure updateEmp(@PathVariable Integer uid,@ModelAttribute Emp emp,@ModelAttribute User user) {
		SingleJsonStructure json = new SingleJsonStructure();
		Emp oldEmp = empRepository.findOne(uid);
		User oldUser = userRepository.findOne(uid);
		emp.setCreateTime(oldEmp.getCreateTime());
		emp.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		empRepository.saveAndFlush(emp);
		user.setUserType(emp.getEmpType());
		user.setSecPassword("123456");
		user.setBindPassword("654321");
		user.setCreateTime(oldUser.getCreateTime());
		user.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		userRepository.saveAndFlush(user);
		if(emp.getStatus().equals("0")|| emp.getStatus().equals("S")){
			List<Userlogin> userlogin= userloginRepository.findByUid(uid);
			CacheManager cm=ehCacheManager.getCacheManager();
			Cache cache=cm.getCache("shiroAuthenticationCache");
			for(Userlogin ul:userlogin){
				ul.setStatus(emp.getStatus());
				userloginRepository.saveAndFlush(ul);
				cache.remove(ul.getLoginString());
			}
		}
		json.setMsg(BaseRest.UPDATE_SUCCESS);
		return json;

	}

	/**
	 * url：/ynlxmainarchive/emp/{uids}
	 * 函数功能说明：删除操作
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
	public SingleJsonStructure deleteEmps(@PathVariable Integer[] uids) {
		boolean msg = empService.delete(uids);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("删除成功！");
		json.setSuccess(msg);
		return json;
	}
	/**
	 * url：/ynlxmainarchive/emp
	 * 函数功能说明:查询所有的员工
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return   
	 * @return ListJsonStructure<Emp>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ListJsonStructure<Emp> findAll(){
		ListJsonStructure<Emp> json = new ListJsonStructure<Emp>();
		List<Emp> EmpList= empRepository.findAll();
		json.setRows(EmpList);
		json.setMsg("查询成功！");
		return json;
	}

	/**
	 * url：/springmvc/emp/toPage
	 * 函数功能说明:分页查询员工
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param start
	 * @param @param limit
	 * @param @return   
	 * @return ListJsonStructure<Emp>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> findAlltoPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size){
		Pageable pageable = new PageRequest(page-1, size);
		List<Map<String,Object>> list =  empService.empDetil(pageable);
		ListJsonStructure<Map<String,Object>> json = new ListJsonStructure<Map<String,Object>>();
		json.setRows(list);
		json.setTotal(list.size());
		json.setMsg(BaseRest.FIND_SUCCESS);
		return json;
	}
	/**
	 * url:/springmvc/emp/searchBykeyValue/toPage
	 * 函数功能说明:条件查询实现分页
	 * @author 张毕思 
	 * @date 2015年7月15日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key
	 * @param @param value
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return ListJsonStructure<Emp>   
	 * @throws
	 */
	@RequestMapping(value="/searchBykeyValue/toPage",method=RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> searchaByKeyValue(
			@RequestParam("value")String value,
			@RequestParam("page")int page,
			@RequestParam("rows")int size){
		Pageable pageable = new PageRequest(page-1, size);
		ListJsonStructure<Map<String,Object>> json = empService.searchByValue(value, pageable);
		return json;
	}
	/**
	 * url::/springmvc/emp/searchEmpInOrgids
	 * 函数功能说明:查询组织下的所有员工
	 * @author 张毕思 
	 * @date 2016年1月18日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgids
	 * @param @return    
	 * @return ListJsonStructure<Map<String,Object>>   
	 * @throws
	 */
	@RequestMapping(value="/searchEmpInOrgids",method=RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> searchEmpInOrgids(@RequestParam(value="orgids[]")Integer[] orgids){
		List<Map<String,Object>> list = empService.searchEmpInOrgids(orgids);
		ListJsonStructure<Map<String,Object>> json = new ListJsonStructure<Map<String,Object>>();
		json.setRows(list);
		return json;
	}
	
	/**
	 * url:/emp/saveOperate
	 * 函数功能说明:保存人员
	 * @author 张毕思 
	 * @date 2015年10月23日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/saveOperate",method=RequestMethod.POST)
	public SingleJsonStructure saveOperate(
			@ModelAttribute User user,
			@RequestParam("roleIds")String roleIds,
			@RequestParam("status")String status,
			@RequestParam("empCode")String empCode,
			@RequestParam("empPassword")String empPassword,
			@RequestParam("orgid")Integer orgid
			){
		String userType = "0";
		String idType = "0";
		String idNo = user.getCell()+"0000000";
		String email = user.getCell() + "@sewt.com";
		String cell2 = user.getCell();
		String marriage = "0";
		String blood = "0";
		String gender = "0";
		String education = "0";
		String postRank = "0";
		if(empPassword.isEmpty()){
			//后期修改
			empPassword = "123456";
		}
		empService.saveOperate(user.getUserName(), user.getCell(), userType, idType, idNo, email,
				cell2, marriage, blood, gender, status, education, empCode,empPassword, postRank,
				roleIds, orgid, user.getNote());
		SingleJsonStructure json = new SingleJsonStructure ();
		json.setMsg(BaseRest.SAVE_SUCCESS);
		return json;
	}
	/**
	 * 
	 * 函数功能说明:emp/updateOperate
	 * @author 张毕思 
	 * @date 2015年10月23日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uid
	 * @param @param userName
	 * @param @param cell
	 * @param @param status
	 * @param @param roleCode
	 * @param @param orgid
	 * @param @param note
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/updateOperate/{uid}" ,method=RequestMethod.POST)
	public SingleJsonStructure updateOperate(
			@PathVariable Integer uid,
			@ModelAttribute User user,
			@RequestParam("empCode")String empCode,
			@RequestParam("empPassword")String empPassword,
			@RequestParam("roleIds")String roleIds,
			@RequestParam("orgid")Integer orgid
			){
		empService.updateOperate(uid, user.getUserName(), user.getStatus(), roleIds, orgid, user.getNote(),empCode,empPassword);
		if(user.getStatus().equals("0")|| user.getStatus().equals("S")){
			List<Userlogin> userlogin= userloginRepository.findByUid(uid);
			CacheManager cm=ehCacheManager.getCacheManager();
			Cache cache=cm.getCache("shiroAuthenticationCache");
			for(Userlogin ul:userlogin){
				ul.setStatus(user.getStatus());
				userloginRepository.saveAndFlush(ul);
				cache.remove(ul.getLoginString());
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.UPDATE_SUCCESS);
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月5日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uid
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/searchByUid/{uid}",method=RequestMethod.GET)
	public SingleJsonStructure searchByUid(@PathVariable("uid")Integer uid){
		Emp emp = empRepository.findByUid(uid);
		SingleJsonStructure json = new SingleJsonStructure();
		if(emp!=null){
			json.setRows(emp);
			json.setMsg(BaseRest.FIND_SUCCESS);
			return json;
		}else{
			json.setSuccess(false);
			json.setMsg(BaseRest.FIND_FAILURE);
			return json;
		}
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月26日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/searchEmpInfo",method=RequestMethod.GET)
	public SingleJsonStructure searchEmpInfo(){
		CommonVariableModel cm=SystemVariableUtils.getCommonVariableModel();
		Userlogin userlogin =cm.getUserlogin();
		Integer uid = userlogin.getUid();
		Emp emp = empRepository.findOne(uid);
		SingleJsonStructure json = new SingleJsonStructure();
		if(emp != null){
			String empCode = emp.getEmpCode();
			String empPass = emp.getEmpPassword();
			json.setRows("-"+empCode+"-"+empPass+"-");
			json.setMsg(BaseRest.FIND_SUCCESS);
		}else{
			json.setRows("---");
			json.setMsg(BaseRest.FIND_FAILURE);
		}
		return json;
	}
	/**
	 * url:emp/updateStatus
	 * 函数功能说明
	 * @author 张毕思 
	 * @date 2016年2月24日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uid
	 * @param @param status
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/updateStatus/{uid}",method=RequestMethod.POST)
	public SingleJsonStructure updateStatus(@PathVariable Integer uid,@RequestParam("status")String status){
		User user = userRepository.findByUid(uid);
		if(user !=null){
			user.setStatus(status);
			userRepository.saveAndFlush(user);
		}
		Emp emp=empRepository.findByUid(uid);
		if(emp != null){
			emp.setStatus(status);
			empRepository.saveAndFlush(emp);
		}
		CacheManager cm=ehCacheManager.getCacheManager();
		Cache cache=cm.getCache("shiroAuthenticationCache");
		List<Userlogin> userlogin= userloginRepository.findByUid(uid);
		for(Userlogin ul:userlogin){
			ul.setStatus(status);
			userloginRepository.saveAndFlush(ul);
			cache.remove(ul.getLoginString());
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.UPDATE_SUCCESS);
		return json;
	}
}
