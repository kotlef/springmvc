package com.ynswet.system.sc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.Orgrole;
import com.ynswet.system.sc.domain.OrgroleId;
import com.ynswet.system.sc.domain.Role;
import com.ynswet.system.sc.domain.RoleMenu;
import com.ynswet.system.sc.domain.RoleMenuId;
import com.ynswet.system.sc.repository.OrgroleRepository;
import com.ynswet.system.sc.repository.RoleMenuRepository;
import com.ynswet.system.sc.repository.RoleRepository;

/**
 * 
 * 类功能说明：角色管理
 * <p>Title: RoleServiceImpl.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:32:05
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class RoleServiceImpl implements RoleService {
	
	
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private OrgroleRepository orgRoleRepository;
	
	@Autowired
	private RoleMenuRepository roleMenuRepository;
	
	

	/*
	 * (non-Javadoc)    
	 * @see com.ynswet.homecloud.common.service.RoleService#deleteRole(java.lang.Integer)
	 */
	@Transactional
	public void deleteRole(Integer roleId) {
		// TODO Auto-generated method stub
		roleRepository.delete(roleId);
		roleRepository.flush();
	}
	
	@Transactional
	public boolean saveOrUpdateRole(
			Integer ajag,
			Role role,RoleMenu roleMenu,RoleMenuId roleMenuId,
			Orgrole orgRole,OrgroleId orgRoleId,
			String menuids,String orgids){
		Role r;
		if(ajag==1){
			role.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
			role.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
			r = roleRepository.saveAndFlush(role);
		}else{
			Integer roleId = role.getRoleId();
			Role oldr = roleRepository.findOne(roleId);
			role.setRoleId(oldr.getRoleId());
			role.setCreateTime(oldr.getCreateTime());
			role.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
			r = roleRepository.saveAndFlush(role);
			List<RoleMenu> list1 = roleMenuRepository.findByRoleId(roleId);
			for(int c=0;c<list1.size();c++){
				roleMenuRepository.delete(list1.get(c));
			}
			List<Orgrole> list2 = orgRoleRepository.findByRoleIdAll(roleId);
			for(int d=0;d<list2.size();d++){
				orgRoleRepository.delete(list2.get(d));
			}
		}
		if(!menuids.isEmpty()){
			String[] menuIds = menuids.split(",");
			for(int a=0;a<menuIds.length;a++){
				roleMenuId.setRoleId(r.getRoleId());
				roleMenuId.setMenuId(Integer.valueOf(menuIds[a]));
				roleMenu.setId(roleMenuId);
				roleMenu.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
				roleMenuRepository.saveAndFlush(roleMenu);
			}
		}
		if(!orgids.isEmpty()){
			String[] orgIds = orgids.split(",");
			for(int b=0;b<orgIds.length;b++){
				orgRoleId.setRoleId(r.getRoleId());
				orgRoleId.setOrgid(Integer.valueOf(orgIds[b]));
				orgRole.setId(orgRoleId);
				orgRole.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
				orgRole.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
				orgRole.setStatus("0");
				orgRoleRepository.saveAndFlush(orgRole);
			}
		}
		return true;
	}

}
