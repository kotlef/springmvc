/**       
 * @Title: StationServiceImpl.java
 * @Package com.ynswet.homecloud.common.service
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月15日 上午9:58:06
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.system.sc.repository.StationRepository;


/**
 * 类功能说明:服务中心管理
 * <p>Title: StationServiceImpl.java</p>
 * @author 李玉鹏
 * @date 2015年5月15日 上午9:58:06
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class StationServiceImpl implements StationService{
	
	@Autowired
	private StationRepository stationRepository;

	/* (non-Javadoc)    
	 * @see com.ynswet.homecloud.common.service.StationService#deleteStation(java.lang.Integer)    
	 */
	@Transactional
	public void deleteStation(Integer id) {
		// TODO Auto-generated method stub
		stationRepository.delete(id);
		stationRepository.flush();
	}

	@Transactional
	public List<Integer> resOrgids() {
		
		return null;
	}

}
