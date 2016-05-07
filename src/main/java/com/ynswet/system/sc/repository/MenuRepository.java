package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.Menu;

/**
 * 
 * 类功能说明
 * <p>Title: MenuRepository.java</p>
 * @author 原勇
 * @date 2015年5月9日 下午3:00:46
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */

public interface MenuRepository extends JpaRepository<Menu, Integer> {
	
	@Modifying
	@Query(value="Select m from Menu m,RoleMenu rm where m.menuId=rm.id.menuId and rm.id.roleId in ?1")
	public List<Menu> findByUserRoleIds(Collection<Integer> roleIds);
	
	public List<Menu> findByParentId(Integer parentId);

	public Menu findByMenuId(Integer menuId);
	
	@Query(value="SELECT m FROM Menu m WHERE m.menuName LIKE concat('%',?1,'%')")
	public List<Menu> findByMenuNameLike(String menuName);

	@Query(value="select m from Menu m,RoleMenu rm where m.menuId=rm.id.menuId and rm.id.roleId = ?1")
	public List<Menu> findMenuByRoleId(Integer roleId);

	@Query(value="select distinct m from Menu m,RoleMenu rm where m.menuId=rm.id.menuId and rm.id.roleId in ?1 and m.menuLevel = ?2 order by m.menuId asc")
	public List<Menu> findMenuByRoleIds(Integer[] roleIds,String menuLevel);

	@Query(value="select distinct m from Menu m,RoleMenu rm where m.menuId=rm.id.menuId and m.parentId = ?1 and rm.id.roleId in ?2 order by m.menuId asc")
	public List<Menu> findByParentIdAndRoleIds(Integer parentId,Integer[] roleIds);
}
