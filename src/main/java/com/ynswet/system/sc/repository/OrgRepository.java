/**       
 * @Title: OrgRepository.java
 * @Package com.ynswet.homecloud.common.repository
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:15:16
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.Org;

/**
 * 类功能说明：组织管理
 * <p>Title: OrgRepository.java</p>
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:15:16
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface OrgRepository extends JpaRepository<Org, Integer>{
	public List<Org> findByOrgidIn(Collection<Integer> orgIds);
	public List<Org> findByStatusAndOrgidIn(String status,Collection<Integer> orgIds);
	public List<Org> findByParentId(int parentId);
	@Query(value="select org,station from Org org ,Station station where org.parentId = ?1 and org.orgid=station.orgid")
	public Object[] findOrgAndStationByOrgid(int parentId);
	
	public List<Org> findByParentIdAndStatus(int parentId,String status);
	@Query(value="SELECT org FROM Org org WHERE org.parentId = ?1 AND org.stationFlag = 'Y' AND org.orgid NOT IN (SELECT s.orgid FROM Station s)")
	public List<Org> findByParentIdNoStation(int parentId);
	@Query(value="select org from Org org where org.orgCode like concat('%',?1,'%') or org.orgCode like concat('%',?2,'%') ")
	public Page<Org> findByOrgCodeLike(String orgCode,Pageable pageable);
	@Query(value="select org from Org org where org.orgName like concat('%',?1,'%')")
	public Page<Org> findByOrgNameLike(String orgName,Pageable pageable);
	@Query(value="select org from Org org where org.orgType like concat(?1,'%')")
	public Page<Org> findByOrgTypeLike(String orgType,Pageable pageable);
	public Org findByOrgCode(String orgCode);
	@Query(value="select org.parentId from Org org where org.orgid = ?1")
	public Integer findByOrgidToParentId(Integer orgid);
	public Org findByOrgid(Integer orgid);
	@Query(value="select org.orgName from Org org where org.orgid = ?1")
	public String findNameByOrgid(Integer orgid);
	
	@Query(value="select org from Org org where org.orgCode like concat('%',?1,'%') or org.orgName like concat('%',?2,'%') ")
	public List<Org> findByOrgCodeOrorgNameLike(String orgCode,String orgName);
}
