package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.Orggroup;

/**
 * 
 * 类功能说明：机构管理
 * <p>Title: OrggroupRepository.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:55:34
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface OrggroupRepository extends JpaRepository <Orggroup, Integer> {
	public Orggroup findByorggroupId(int orggroupId);
	@Modifying
	@Query(value="delete from Orggroup orggroup where orggroup.orggroupId in ?1 ")
	public void delete(Collection<Integer> orggroupIds);
	
	@Query(value="select orggroup from Orggroup orggroup where orggroup.orggroupName = ?1 and orggroup.orggroupId <> ?2 ")
	public Orggroup findByOrggroupNameAndorggroupIdNot(String orggroupName,Integer orggroupId);
	public List<Orggroup> findByParentId(int parentId);
	public List<Orggroup> findByParentIdAndStatus(int parentId,String status);
	public Orggroup findByorggroupName(String orggroupName);
	
	@Query(value="select orggroup from Orggroup orggroup where orggroup.orggroupName like concat('%',?1,'%')")
	public List<Orggroup> findByOrggroupNameLike(String orggroupName);
	public Page<Orggroup> findByOpenDate(Date openDate,Pageable pageable);

	/**
	 * 函数功能说明
	 * @author 李玉鹏 
	 * @date 2015年7月15日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param parentId
	 * @param @return    
	 * @return List<Orggroup>   
	 * @throws
	 */
	public List<Orggroup> findByParentId(Integer parentId);
}

