/**
 * @Title: RoleRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:41:29
 * @version V1.0  
 * <p>Copy
 * <p>Company:广州合光信息科技有限公司</p>
 */
package com.ynswet.system.sc.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ynswet.common.rest.BaseRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynswet.common.domain.ListJsonStructure;
import com.ynswet.common.domain.SingleJsonStructure;
import com.ynswet.common.util.BeanUtils;
import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.Homepage;
import com.ynswet.system.sc.domain.Orgrole;
import com.ynswet.system.sc.domain.OrgroleId;
import com.ynswet.system.sc.domain.Role;
import com.ynswet.system.sc.domain.RoleMenu;
import com.ynswet.system.sc.domain.RoleMenuId;
import com.ynswet.system.sc.repository.HomepageRepository;
import com.ynswet.system.sc.repository.OrgroleRepository;
import com.ynswet.system.sc.repository.RoleMenuRepository;
import com.ynswet.system.sc.repository.RoleRepository;
import com.ynswet.system.sc.service.RoleService;

/**
 *
 * 类功能说明：角色管理
 * <p>Title: RoleRest.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:41:29
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/role")
public class RoleRest extends BaseRest {

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private RoleMenuRepository roleMenuRepository;
	
	@Autowired
	private OrgroleRepository orgRoleRepository;
	
	@Autowired
	private HomepageRepository homepageRepository;
	
	/**
	 * url:/ynlxhealth/role
	 * 函数功能说明:保存角色
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param Role
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure save(@ModelAttribute("role") Role role){
		role.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		role.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
		role.setStatus("0");
		roleRepository.saveAndFlush(role);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(SAVE_SUCCESS);
		return json;
	}
	
	/**
	 * url:/ynlxhealth/role/{roleId}
	 * 函数功能说明:修改角色
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param roleId
	 * @param @param role
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{roleId}",method = RequestMethod.POST)
	public SingleJsonStructure updateRole(@PathVariable Integer roleId,@ModelAttribute Role role) {
		Role oldRole = roleRepository.findOne(roleId);
		role.setCreateTime(oldRole.getCreateTime());
		role.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		SingleJsonStructure json = new SingleJsonStructure();
		roleRepository.saveAndFlush(role);
		json.setMsg(UPDATE_SUCCESS);
		return json;
	}
	
	/**
	 * url:/ynlxhealth/role/{roleIds}
	 * 函数功能说明:删除角色
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param roleIds
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{roleIds}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteRoles(@PathVariable String roleIds) {
		if(!roleIds.isEmpty()){
			String [] arrRoleIds = roleIds.split(",");
			for(String roleId : arrRoleIds){
				roleRepository.delete(Integer.valueOf(roleId));
				roleRepository.flush();
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(DELTE_SUCCESS);
		return json;
	}


	@RequestMapping(method = RequestMethod.PUT)
	public SingleJsonStructure updateRole(@ModelAttribute("roleId") int roleId,@ModelAttribute("role") Role role ) {
		Role oldRole=roleRepository.findOne(roleId);
		System.out.println(oldRole.toString());
		System.out.println(role.toString());
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.UPDATE_SUCCESS);
		return json;
	}


	/**
	 * url:/ynlxhealth/role
	 * 函数功能说明:查询所有的角色
	 * @author 李玉鹏
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return   
	 * @return ListJsonStructure<Role>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ListJsonStructure<Role> findAll(){
		ListJsonStructure<Role> json = new ListJsonStructure<Role>();
		List<Role> roleList= roleRepository.findAll();
		json.setRows(roleList);
		json.setMsg(BaseRest.FIND_SUCCESS);
		return json;
	}

	/**
	 * url:/ynlxhealth/role/toPage
	 * 函数功能说明:分页查询角色
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param size
	 * @param @return   
	 * @return ListJsonStructure<Role>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<Role> findAlltoPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		Sort sort = new Sort(Direction.DESC,"createTime");
		Pageable pageable = new PageRequest(page-1, size,sort);
		Page<Role> pages = roleRepository.findAll(pageable);
		ListJsonStructure<Role> json = new ListJsonStructure<Role>();
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年12月4日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return ListJsonStructure<Map<String,Object>>   
	 * @throws
	 */
	@RequestMapping(value="/findAllAndHpage/ToPage",method = RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> findAllAndHpageToPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size){
		Sort sort = new Sort(Direction.DESC,"createTime");
		Pageable pageable = new PageRequest(page-1, size,sort);
		Page<Role> pages = roleRepository.findAll(pageable);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Role role : pages.getContent()){
			Homepage homepage = homepageRepository.findByHomePageId(role.getHomepageId());
			Map<String, Object> map = BeanUtils.toMap(role);
			if(homepage != null){
				map.put("homePageName", homepage.getHomepageName());
			}
			list.add(map);
		}
		ListJsonStructure<Map<String,Object>> json = new ListJsonStructure<Map<String,Object>>();
		json.setRows(list);
		json.setTotal((int)pages.getTotalElements());
		json.setMsg(BaseRest.FIND_SUCCESS);
		return json;
	}
	/**
	 * url:/ynlxhealth/role/searchByStatus/ToPage
	 * 函数功能说明:查询正常的角色
	 * @author 张毕思 
	 * @date 2015年12月25日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return ListJsonStructure<Map<String,Object>>   
	 * @throws
	 */
	@RequestMapping(value="/searchRoleAndHpageByStatus/ToPage",method = RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> searchRoleAndHpageByStatus(
			@RequestParam("status")String status,
			@RequestParam("page")int page,
			@RequestParam("rows")int size){
		Sort sort = new Sort(Direction.DESC,"createTime");
		Pageable pageable = new PageRequest(page-1, size,sort);
		Page<Role> pages = roleRepository.findByStatus(status, pageable);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Role role : pages.getContent()){
			Map<String, Object> map = BeanUtils.toMap(role);
			Homepage homepage = homepageRepository.findByHomePageId(role.getHomepageId());
			if(homepage != null){
				map.put("homePageName",homepage.getHomepageName());
				map.put("homepageUrl", homepage.getHomepageUrl());
			}
			list.add(map);
		}
		ListJsonStructure<Map<String,Object>> json = new ListJsonStructure<Map<String,Object>>();
		json.setRows(list);
		json.setTotal((int)pages.getTotalElements());
		json.setMsg(BaseRest.FIND_SUCCESS);
		return json;
	}
	

	/**
	 * url:/ynlxhealth/role/searchByKeyValueLike/params
	 * 搜索角色功能
	 * @author 孟话然
	 * @date 2015年7月21日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key value
	 * @param @return   
	 * @return ListJsonStructure<Role>   
	 * @throws
	 */
	@RequestMapping(value="/searchByKeyValueLike/params",method = RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> searchByKeyValueLikeToPage(
			@RequestParam("value")String value,
			@RequestParam("page")int page,
			@RequestParam("rows")int rows){
		Page<Role> pages = null ;
		Sort sort = new Sort(Direction.DESC,"createTime");
		Pageable pageable = new PageRequest(page-1, rows,sort);
		pages = roleRepository.findByRoleInfoLike(value,pageable);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Role role : pages.getContent()){
			Map<String, Object> map = BeanUtils.toMap(role);
			Homepage homepage = homepageRepository.findByHomePageId(role.getHomepageId());
			map.put("homePageName", homepage.getHomepageName());
			list.add(map);
		}
		ListJsonStructure<Map<String,Object>> json = new ListJsonStructure<Map<String,Object>>();
		json.setRows(list);
		json.setTotal((int) pages.getTotalElements());
		json.setMsg(BaseRest.FIND_SUCCESS);
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月6日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param role
	 * @param @param roleMenu
	 * @param @param roleMenuId
	 * @param @param orgRole
	 * @param @param orgRoleId
	 * @param @param orgids
	 * @param @param menuids
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/saveAll",method = RequestMethod.POST)
	public SingleJsonStructure saveAll(
			@ModelAttribute("role") Role role,
			@ModelAttribute("roleMenu") RoleMenu roleMenu,
			@ModelAttribute("roleMenuId") RoleMenuId roleMenuId,
			@ModelAttribute("orgRole") Orgrole orgRole,
			@ModelAttribute("orgRoleId") OrgroleId orgRoleId,
			@RequestParam("oId")String orgids,
			@RequestParam("meId")String menuids){
		boolean b = roleService.saveOrUpdateRole(1, role, roleMenu, roleMenuId, orgRole, orgRoleId, menuids, orgids);
		SingleJsonStructure json = new SingleJsonStructure();
		if(b==true){
			json.setMsg(SAVE_SUCCESS);
		}else{
			json.setMsg(BaseRest.SAVE_FAILURE);
		}
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月6日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param role
	 * @param @param roleMenu
	 * @param @param roleMenuId
	 * @param @param orgRole
	 * @param @param orgRoleId
	 * @param @param orgids
	 * @param @param menuids
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/updateAll",method = RequestMethod.POST)
	public SingleJsonStructure updataAll(
			@ModelAttribute("role") Role role,
			@ModelAttribute("roleMenu") RoleMenu roleMenu,
			@ModelAttribute("roleMenuId") RoleMenuId roleMenuId,
			@ModelAttribute("orgRole") Orgrole orgRole,
			@ModelAttribute("orgRoleId") OrgroleId orgRoleId,
			@RequestParam("oId")String orgids,
			@RequestParam("meId")String menuids){
		boolean b = roleService.saveOrUpdateRole(2, role, roleMenu, roleMenuId, orgRole, orgRoleId, menuids, orgids);
		SingleJsonStructure json = new SingleJsonStructure();
		if(b==true){
			json.setMsg(BaseRest.UPDATE_SUCCESS);
		}else{
			json.setMsg(BaseRest.UPDATE_FAILURE);
		}
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月6日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param roleId
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="deleteAll/{roleId}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteAll(@PathVariable String roleId){
		roleRepository.delete(Integer.valueOf(roleId));
		roleRepository.flush();
		List<RoleMenu> list1 = roleMenuRepository.findByRoleId(Integer.valueOf(roleId));
		for(int c=0;c<list1.size();c++){
			roleMenuRepository.delete(list1.get(c));
		}
		List<Orgrole> list2 = orgRoleRepository.findByRoleIdAll(Integer.valueOf(roleId));
		for(int d=0;d<list2.size();d++){
			orgRoleRepository.delete(list2.get(d));
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(DELTE_SUCCESS);
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明:远程查询角色code是否重复
	 * @author 孟话然 
	 * @date 2015年11月27日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param roleCode
	 * @param @return    
	 * @return boolean   
	 * @throws
	 */
	@RequestMapping(value="/checkRoleCode",method = RequestMethod.POST)
	public boolean checkByIdNo(@RequestParam("roleCode")String roleCode) {
		Role role = roleRepository.findByRoleCode(roleCode);
		if(null!=role){
			return false;
		}else{
			return true;
		}
	}
	
}
