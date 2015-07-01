/**       
 * @Title: OrgResRepository.java
 * @Package com.ynswet.system.sc.repository
 * @Description: TODO
 * @author chenguang 
 * @date 2015年5月9日 下午2:52:04
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:云南立翔科技有限公司</p>      
 */
package com.ynswet.system.sc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynswet.system.sc.domain.OrgRes;
import com.ynswet.system.sc.domain.OrgResId;

/**
 * 
 * 类功能说明
 * <p>Title: OrgResRepository.java</p>
 * @author 原勇
 * @date 2015年5月9日 下午3:00:26
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */

public interface OrgResRepository extends JpaRepository<OrgRes, OrgResId> {

}
