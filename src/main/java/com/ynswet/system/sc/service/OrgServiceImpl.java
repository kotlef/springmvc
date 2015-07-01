/**       
 * @Title: OrgServiceImpl.java
 * @Package com.ynswet.homecloud.common.service
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:05:21
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.system.sc.repository.OrgRepository;

/**
 * 类功能说明：组织管理
 * <p>Title: OrgServiceImpl.java</p>
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:05:21
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class OrgServiceImpl implements OrgService{

	@Autowired
	private OrgRepository orgRepository;

	/* (non-Javadoc)    
	 * @see com.ynswet.homecloud.common.service.OrgService#deleteOrg(com.ynswet.homecloud.common.domain.Org)    
	 */
	@Transactional
	public void deleteOrg(Integer orgid) {
		// TODO Auto-generated method stub
		orgRepository.delete(orgid);
		orgRepository.flush();
	}
	
	

}
