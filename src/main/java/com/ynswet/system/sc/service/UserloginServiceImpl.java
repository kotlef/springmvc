/**       
 * @Title: UserloginServiceImpl.java
 * @Package com.ynswet.system.sc.service
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月20日 上午11:09:07
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ynswet.system.sc.domain.Userlogin;
import com.ynswet.system.sc.repository.UserloginRepository;

/**
 * 类功能说明
 * <p>Title: UserloginServiceImpl.java</p>
 * @author 李玉鹏
 * @date 2015年5月20日 上午11:09:07
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class UserloginServiceImpl implements UserloginService{

	@Autowired
	private UserloginRepository userloginRepository;
	/* (non-Javadoc)    
	 * @see com.ynswet.system.sc.service.UserloginService#saveUserlogin(java.util.List)    
	 */
	@Transactional
	public void saveUserlogin(List<Userlogin> userlogins) {
		// TODO Auto-generated method stub
		userloginRepository.save(userlogins);
		userloginRepository.flush();
	}

}
