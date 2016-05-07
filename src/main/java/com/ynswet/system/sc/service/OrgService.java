/**       
 * @Title: OrgService.java

 * @Package com.ynswet.homecloud.common.service
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:03:53
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import java.util.List;
import java.util.Map;

import com.ynswet.system.sc.domain.Org;


/**
 * 类功能说明：组织管理
 * <p>Title: OrgService.java</p>
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:03:53
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface OrgService {
	
	public void deleteOrg(Integer orgid);
	/**
	 * 
	 * 函数功能说明:获取子节点
	 * @author 张毕思 
	 * @date 2015年10月26日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param list
	 * @param @param typeId
	 * @param @return    
	 * @return String   
	 * @throws
	 */
	public List<Integer> getChildNodes(List<Org> list, Integer typeId);

	public List<Map<String,Object>> searchOrgAndStationByParentId(int parentId);
}
