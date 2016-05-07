/**       
 * @Title: OrgresService.java
 * @Package com.ynswet.system.sc.service
 * @Description: TODO
 * @author 孙越
 * @date 2015年8月10日 下午2:11:25
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import java.util.Date;

import com.ynswet.system.sc.domain.OrgRes;
import com.ynswet.system.sc.domain.OrgResId;
import com.ynswet.system.sc.domain.Res;

/**
 * 类功能说明
 * <p>Title: OrgresService.java</p>
 * @author 孙越
 * @date 2015年8月10日 下午2:11:25
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface OrgresService {
	
	public void deleteByOidAndRid(int oid, int rid);
	
	public boolean saveOrUpdateRes(Integer ajag,Res res, OrgRes orgRes, OrgResId orgResId, String orgids);
	
	public boolean saveOrgresAndRes(int orgid,String note,Date createTime,String resType,int itemId);
	public boolean updateOrgresAndRes (int oldOrgid,int orgid,int communityId,String note);
}
