/**
 * @Title: PostRepository.java
 * @Package com.ynswet.homecloud.common.repository
 * @Description: TODO
 * @author 原勇
 * @date 2015年5月10日 上午11:33:14
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
package com.ynswet.system.sc.repository;

import java.util.Collection;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.Post;

/**
 * 类功能说明:岗位管理
 * <p>Title: PostRepository.java</p>
 * @author 原勇
 * @date 2015年5月10日 上午11:33:14
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:云南立翔开发平台</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface PostRepository extends JpaRepository<Post, Integer> {

	@Modifying
	@Query(value="select p.id.orgid from Post p where p.id.uid=?1  order by p.id.orgid")
	public Collection<Integer> findUserOrgIdsByUid(Integer uid);

	@Modifying
	@Query(value="select p.id.roleId from Post p where p.id.uid=?1 order by p.id.roleId")
	public Collection<Integer> findUserRoleIdsByUid(Integer uid);
}
