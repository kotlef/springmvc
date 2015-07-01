package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ynswet.system.sc.domain.Res;

/**
 * 
 * 类功能说明
 * <p>Title: ResRepository.java</p>
 * @author 原勇
 * @date 2015年5月9日 下午3:03:08
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */

public interface ResRepository extends JpaRepository<Res, Integer> {
	
	@Modifying
	@Query(value="select res from Res res,OrgRes orgres where res.resId=orgres.id.resId and orgres.id.orgid in ?1")
	public List<Res> findByInOrgids(Collection<Integer> orgIds);

}
