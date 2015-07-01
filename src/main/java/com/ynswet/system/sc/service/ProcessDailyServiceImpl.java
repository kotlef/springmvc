/**       
 * @Title: ProcessDailyServiceImpl.java
 * @Package com.ynswet.homecloud.common.service
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月14日 下午3:32:59
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.system.sc.repository.ProcessDailyRepository;

/**
 * 类功能说明：日结进程表
 * <p>Title: ProcessDailyServiceImpl.java</p>
 * @author 李玉鹏
 * @date 2015年5月14日 下午3:32:59
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class ProcessDailyServiceImpl implements ProcessDailyService{

	@Autowired
	private ProcessDailyRepository processDailyRepository;

	/* (non-Javadoc)    
	 * @see com.ynswet.homecloud.common.service.ProcessDailyService#deleteProcessDaily(java.lang.Integer)    
	 */
	@Transactional
	public void deleteProcessDaily(Integer executeId) {
		// TODO Auto-generated method stub
		processDailyRepository.delete(executeId);
		processDailyRepository.flush();
	}
}
