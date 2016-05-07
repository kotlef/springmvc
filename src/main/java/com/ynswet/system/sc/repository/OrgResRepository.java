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


import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.OrgRes;

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

public interface OrgResRepository extends JpaRepository<OrgRes, Integer> {

	
	@Modifying
	@Query(value="delete from OrgRes x where x.id.orgid = ?1 and x.id.resId = ?2")
	public void deleteByOidAndRid(Integer orgid, Integer resid);
	
	@Query(value="select orgRes from OrgRes orgRes, Res res where orgRes.id.resId=res.resId and res.itemId=?1 and res.resType=?2 ")
	public List<OrgRes> findByItemIdAndResType(Integer itemId,String resType);
	
	@Query(value="Select x from Res x,OrgRes y where x.resId=y.id.resId and y.id.orgid=?1")
	public List<OrgRes> findOrgresByOrgId(Integer hid);
	
	@Query(value="Select x,y from Res x,OrgRes y where x.resId=y.id.resId and y.id.orgid=?1")
	public List<Object[]> findOrgresAndResByOrgId(Integer orgid);
	
	@Query(value="select x.itemId from Res x,OrgRes y where x.resId=y.id.resId and y.id.orgid=?1 and x.resType='O'")
	public List<Integer> findOrgresToOrgid(Integer ogrId);
	
	@Query(value="select mo from OrgRes mo where mo.id.orgid = ?1")
	public List<OrgRes> findOrgResByOrgid(Integer orgId);
	
	@Query(value="select mo.id.resId from OrgRes mo where mo.id.orgid = ?1")
	public Collection<Integer> findByOrgid(Integer orgId);
	
	@Query(value="select mor from OrgRes mor where mor.id.resId = ?1")
	public List<OrgRes> findByResIdAll(Integer resId);
	
	@Query(value="select mor.id.orgid from OrgRes mor where mor.id.resId = ?1")
	public List<Integer> findByResId(Integer resId);
	
	@Query(value="select orgRes from OrgRes orgRes ,Res res where res.resId = orgRes.id.resId and orgRes.id.orgid = ?1 and res.itemId = ?2")
	public OrgRes findByOrgidAndItemId(Integer orgid,Integer itemId);
	
	@Query(value="select orgres from OrgRes orgres where orgres.id.resId = ?1 and orgres.id.orgid = ?2 ")
	public OrgRes findByResIdAndOrgid(Integer resId,Integer orgid);
}
