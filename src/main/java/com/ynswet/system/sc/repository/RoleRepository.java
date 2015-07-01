/**       
 * @Title: RoleRepository.java
 * @Package com.ynswet.system.sc.repository
 * @Description: TODO
 * @author 原勇
 * @date 2015年5月9日 下午3:01:48
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ynswet.system.sc.domain.Role;

/**
 * 类功能说明：角色管理
 * <p>Title: RoleRepository.java</p>
 * @author 原勇
 * @date 2015年5月9日 下午3:01:48
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
	
	@Modifying
	@Query(value="select r.roleCode from Role r where r.roleId in ?1")
	public List<String> findRoleCodeByRoleId(Collection<Integer> roleIds);
	

	public List<Role> findByroleIdIn (Collection<Integer> roleIds);
	
}
