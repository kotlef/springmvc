/**       
 * @Title: ResServiceImpl.java
 * @Package com.ynswet.system.sc.service
 * @Description: TODO
 * @author 孙越
 * @date 2015年8月5日 下午6:32:23
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.system.sc.repository.ResRepository;



/**
 * 类功能说明:资源管理
 * <p>Title: ResServiceImpl.java</p>
 * @author 孙越
 * @date 2015年8月5日 下午6:32:23
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */


@Service
public class ResServiceImpl implements ResService{

@Autowired
private ResRepository resRepository;
	/* (non-Javadoc)    
	 * @see com.ynswet.system.sc.service.ResService#deleteRes(java.lang.Integer)    
	 */
@Transactional
	public void deleteRes(Integer resId) {
		resRepository.delete(resId);
		resRepository.flush();
	}

}
