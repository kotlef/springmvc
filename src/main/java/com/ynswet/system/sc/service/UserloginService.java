/**       
 * @Title: UserloginService.java
 * @Package com.ynswet.system.sc.service
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月20日 上午11:09:41
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import java.util.List;

import com.ynswet.system.sc.domain.Userlogin;

/**
 * 类功能说明
 * <p>Title: UserloginService.java</p>
 * @author 李玉鹏
 * @date 2015年5月20日 上午11:09:41
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface UserloginService {
	
	public void saveUserlogin(List<Userlogin> userlogins);
	
}
