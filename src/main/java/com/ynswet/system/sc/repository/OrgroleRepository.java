package com.ynswet.system.sc.repository;



import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.Orgrole;


public interface OrgroleRepository extends JpaRepository<Orgrole, Integer> {
	//public Collection<Integer> findByOrgid(Integer orgid);
	
	@Query(value="Select myOrgrole from Orgrole myOrgrole where myOrgrole.id.orgid=?1 and myOrgrole.id.roleId=?2")
	public Orgrole findByOrgidAndRoleId(Integer orgid,Integer roleId);
	
	@Query(value="select myOrgrole.id.roleId from Orgrole myOrgrole where myOrgrole.id.orgid = ?1")
	public List<Integer> findByOrgid(Integer ogrid);
	
	@Query(value="select myOrgrole from Orgrole myOrgrole where myOrgrole.id.orgid = ?1")
	public List<Orgrole> findByOgrid(Integer orgid);
	
	@Query(value="select mor.id.orgid from Orgrole mor where mor.id.roleId = ?1")
	public List<Integer> findByRoleId(Integer roleId);
	
	@Query(value="select mor from Orgrole mor where mor.id.roleId = ?1")
	public List<Orgrole> findByRoleIdAll(Integer roleId);
}
