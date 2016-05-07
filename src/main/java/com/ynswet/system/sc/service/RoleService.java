package com.ynswet.system.sc.service;

import com.ynswet.system.sc.domain.Orgrole;
import com.ynswet.system.sc.domain.OrgroleId;
import com.ynswet.system.sc.domain.Role;
import com.ynswet.system.sc.domain.RoleMenu;
import com.ynswet.system.sc.domain.RoleMenuId;


/**
 * 
 * 类功能说明：角色管理
 * <p>Title: RoleService.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:32:25
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface RoleService {
	
	public void deleteRole(Integer roleId);
	
	public boolean saveOrUpdateRole(Integer ajag,Role role,RoleMenu roleMenu,RoleMenuId roleMenuId,Orgrole orgRole,OrgroleId orgRoleId,String menuids,String orgids);
	
}
