package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.Homepage;
import com.ynswet.system.sc.domain.Menu;

/**
 *
 * 类功能说明
 * <p>Title: HomepageRepository.java</p>
 * @author 原勇
 * @date 2015年6月4日 下午3:20:15
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface HomepageRepository extends JpaRepository<Homepage, Integer> {

	@Modifying
	@Query(value="Select h from Homepage h,Role r where h.homepageId=r.homepageId and r.roleId in ?1")
	public List<Homepage> findByUserRoleIds(Collection<Integer> roleIds);
}
