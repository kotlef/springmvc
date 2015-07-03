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

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.ynswet.common.domain.ListJsonStructure;
import com.ynswet.common.domain.SingleJsonStructure;
import com.ynswet.common.rest.BaseRest;
import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.Role;
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
public class RoleRest extends BaseRest{

	@Autowired
	private RoleService roleService;

	@Autowired
	private RoleRepository roleRepository;

	/**
	 * url:/springmvc/role
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

		role.setHomepageId(3);

		role.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());

		role.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());

		role.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());

		role.setStatus("0");

		roleRepository.saveAndFlush(role);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("保存成功！");
		return json;
	}
	/**
	 * url:/springmvc/role/{roleId}
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
	@RequestMapping(value="/{roleId}",method = RequestMethod.PUT)
	public SingleJsonStructure updateRole(@PathVariable Integer roleId,@ModelAttribute Role role) {
		// TODO Auto-generated method stub
		Role oldRole = roleRepository.findOne(roleId);
		role.setCreateTime(oldRole.getCreateTime());
		role.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		SingleJsonStructure json = new SingleJsonStructure();
		roleRepository.saveAndFlush(role);
		json.setMsg("更新成功!");
		return json;
	}
	/**
	 * url:/springmvc/role/{roleIds}
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
		json.setMsg("删除成功！");
		return json;
	}


	@RequestMapping(method = RequestMethod.PUT)
	public SingleJsonStructure updateRole(@ModelAttribute("roleId") int roleId,@ModelAttribute("role") Role role ) {
		Role oldRole=roleRepository.findOne(roleId);
		System.out.println(oldRole.toString());
		System.out.println(role.toString());
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("删除成功！");
		return json;
	}


	/**
	 * url:/springmvc/role
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
		json.setMsg("查询成功！");
		return json;
	}

	/**
	 * url:/springmvc/role/toPage
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
		Pageable pageable = new PageRequest(page-1, size);
		Page<Role> pages = roleRepository.findAll(pageable);
		ListJsonStructure<Role> json = new ListJsonStructure<Role>();
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}

}
