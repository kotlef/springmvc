package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.Emp;
import com.ynswet.system.sc.domain.User;



/**
 *
 * 类功能说明：员工管理
 * <p>Title: EmpRepository.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:48:46
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface EmpRepository extends JpaRepository <Emp, Integer> {
	public Emp findByUid(Integer uid);
	@Query(value="select emp from Emp emp where emp.empCode like concat(?1,'%')")
	public Page<Emp> findByEmpCodeLike(String empCode,Pageable pageable);

	public Page<Emp> findByEntryDate(Date entryDate,Pageable pageable);
	@Query(value="select emp from Emp emp where emp.empType like concat(?1,'%')")
	public Page<Emp> findByEmpTypeLike(String empType,Pageable pageable);
	
	
	@Query(value="select u from User u,Post p,Emp e where p.id.orgid in ?1 and p.id.roleId = 9 and e.uid=p.id.uid and"
			+ " e.status = '0' and u.uid = e.uid")
	public List<User> findEmpInORgids(Integer[] orgids);
	
	@Query(value = "select u,e from User u,Emp e where u.uid = e.uid order by e.modifyTime desc")
	public Page<Object[]> findAllOrderBycreatTime(Pageable pageable);
	
	@Query(value = "select u from User u,Emp e where u.uid = e.uid and e.postRank in ?1 ")
	public List<User> findByPostRank(String[] postRank);
	
	@Query(value="select u,e from Emp e,User u where e.uid=u.uid and e.uid in ?1 ")
	public Page<Object[]> findByUidIn(Collection<Integer> uid,Pageable pageable);
	@Query(value="select u,e from Emp e,User u where e.uid=u.uid and e.empCode like concat(?1,'%') and e.uid in ?2")
	public Page<Object[]> findByEmpCodeLikeAndInUid(String empCode,Collection<Integer> uids,Pageable pageable);
	@Query(value="select e,u from Emp e,User u where e.uid = u.uid and e.empType like concat(?1,'%') and e.uid in ?2 ")
	public Page<Object[]> findByEmpTypeLikeAndInUid(String empType,Collection<Integer> uids,Pageable pageable);

	@Query(value="select u,e from User u,Emp e where u.uid = e.uid and (u.userName like concat ('%',?1,'%') or u.cell like concat('%',?1,'%') or e.empCode like concat('%',?1,'%')) " )
	public Page<Object[]> findByValue(String value, Pageable pageable);
	
	public Emp findByEmpCode(String empCode);
	@Modifying
	@Query(value="delete from Emp emp where emp.uid in ?1")
	public void delete(Integer[] uids );
}

