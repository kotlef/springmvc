/**       
 * @Title: StationService.java
 * @Package com.ynswet.homecloud.common.service
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月15日 上午9:55:28
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import java.util.List;


/**
 * 类功能说明:服务中心管理
 * <p>Title: StationService.java</p>
 * @author 李玉鹏
 * @date 2015年5月15日 上午9:55:28
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface StationService {
	
	public void deleteStation(Integer id);
	
	public List<Integer> resOrgids();
}
