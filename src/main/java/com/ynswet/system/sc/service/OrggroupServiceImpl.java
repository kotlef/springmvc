package com.ynswet.system.sc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.system.sc.repository.OrggroupRepository;

/**
 * 
 * 类功能说明：机构管理
 * <p>Title: UserServiceImpl.java</p>
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
public class OrggroupServiceImpl implements OrggroupService {
	
	
	@Autowired
	private OrggroupRepository orggroupRepository;

	/*
	 * (non-Javadoc)    
	 * @see com.ynswet.homecloud.common.service.OrggroupService#deleteOrggroup(java.lang.Integer)
	 */
	@Transactional
	public void deleteOrggroup(Integer orggroupId) {
		// TODO Auto-generated method stub
		orggroupRepository.delete(orggroupId);
		orggroupRepository.flush();
	}

}
