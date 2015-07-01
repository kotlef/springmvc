package com.ynswet.system.sc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

}
