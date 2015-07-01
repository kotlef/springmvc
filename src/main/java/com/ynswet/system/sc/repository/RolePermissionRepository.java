/**       
 * @Title: RolePermissionRepository.java
 * @Package com.ynswet.system.sc.repository
 * @Description: TODO
 * @author 原勇
 * @date 2015年5月9日 下午3:29:00
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynswet.system.sc.domain.RolePermission;
import com.ynswet.system.sc.domain.RolePermissionId;

/**
 * 类功能说明
 * <p>Title: RolePermissionRepository.java</p>
 * @author 原勇
 * @date 2015年5月9日 下午3:29:00
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */

public interface RolePermissionRepository extends JpaRepository<RolePermission, RolePermissionId> {

	

}
