package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

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

	@Modifying
	@Query(value="select res from Res res where res.resType= ?1")
	public List<Res> findByRtype(String resType);

	public Res findByitemId(Integer itemId);
	
	public Page<Res> findByResTypeLike(String resType, Pageable pageable);
	
	public List<Res> findByresIdIn (Collection<Integer> resIds);
	
	@Modifying
	@Query(value="SELECT r FROM Res r WHERE r.resId in ?1")
	public List<Object[]> findInfoByresIdIn (Collection<Integer> resIds);

	@Modifying
	@Query(value="SELECT r FROM Res r WHERE r.resType= ?1 and r.itemId= ?2")
	public List<Res> findOnlyOne(String resType, int itemId);
	
	public Res findByitemIdAndResType(int itemId ,String resType);

}
