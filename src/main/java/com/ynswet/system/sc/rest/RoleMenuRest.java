/**
 * @Title: RoleRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 孟话然
 * @date 2015年7月31日 上午11:30:29
 * @version V1.0  
 * <p>Copy
 * <p>Company:广州合光信息科技有限公司</p>
 */
package com.ynswet.system.sc.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynswet.common.domain.SingleJsonStructure;
import com.ynswet.common.rest.BaseRest;
import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.RoleMenu;
import com.ynswet.system.sc.domain.RoleMenuId;
import com.ynswet.system.sc.repository.RoleMenuRepository;


/**
*
* 类功能说明：角色菜单管理
* <p>Title: RoleMenuRest.java</p>
* @author 孟话然
* @date 2015年7月31日 上午11:30:29
* 类修改者	修改日期
* 修改说明
* @version V1.0
* <p>Description:立翔云</p>
* <p>Copyright: Copyright (c) 2015</p>
* <p>Company:广州合光信息科技有限公司</p>
*/
@RestController
@RequestMapping("/roleMenu")
public class RoleMenuRest extends BaseRest{
	
	@Autowired
	private RoleMenuRepository roleMenuRepository;
	
	
	/**
	 *
	 * 查找具有的菜单功能
	 * @author 孟话然
	 * @date 2015年7月24日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param roleId
	 * @param @return   
	 * @return ListJsonStructure<Integer>   
	 * @throws
	 */
	@RequestMapping(value="/searchMenuIdByRoleId",method = RequestMethod.GET)
	public List<Integer> searchMenuIdByRoleId(
			@RequestParam("roleId")Integer roleId
			) {
		List<Integer> roleMenus=roleMenuRepository.findMenuIdByRoleId(roleId);
		return roleMenus;
	}

	/**
	 *
	 * 修改菜单功能
	 * @author 孟话然
	 * @date 2015年7月24日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key value
	 * @param @return   
	 * @return ListJsonStructure<RoleMenu>   
	 * @throws
	 */
	@RequestMapping(value="/updataRoleMenu",method = RequestMethod.POST)
	public SingleJsonStructure updateRoleMenu(
			@ModelAttribute RoleMenuId rmId,
			@ModelAttribute RoleMenu rm,
			@RequestParam("roleId")Integer roleId,
			@RequestParam("menuIds")String menuIds){
		SingleJsonStructure json = new SingleJsonStructure();
		rmId.setRoleId(roleId);
		rm.setId(rmId);
		//删除角色菜单
		List<RoleMenu> roleMenus = roleMenuRepository.findByRoleId(roleId);
		if(!roleMenus.isEmpty()){
			for(int b=0;b<roleMenus.size();b++){
				roleMenuRepository.delete(roleMenus.get(b));
			}
		}
		//添加橘色菜单
		if(!menuIds.isEmpty()){
			String[] arrIds = menuIds.split(",");
			for(int a=0;a<arrIds.length;a++){
				rmId.setMenuId(Integer.valueOf(arrIds[a]));
				rm.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
				roleMenuRepository.saveAndFlush(rm);
			}
		}
		json.setMsg("修改成功");
		return json;
	}
	
	
}