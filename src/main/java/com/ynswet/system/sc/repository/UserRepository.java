package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.User;

/**
 * 
 * 类功能说明：用户管理
 * <p>Title: UserRepository.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:36:04
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface UserRepository extends JpaRepository<User, Integer> {
	
	@Query(value="select myUser from User myUser where myUser.userName = ?1")
	public Page<User> findByUserName(String userName,Pageable pageable);
	
	@Query(value="select myUser from User myUser where myUser.cell = ?1 or myUser.idNo = ?2")
	public User checkUserUniqueness(String cell,String idNo);
	
	public User findByUid(Integer uid);
	
	public User findByIdNo(String idNo);
	
	public User findByCell(String cell);
	
	public User findByEmail(String email);
	
	public User findByEmailAndUidNot(String email,Integer uid);
	
	public User findByIdNoAndUidNot(String idNo,Integer uid);
	
	public User findByCellAndUidNot(String cell,Integer uid);
	
	public User findByUserName(String userName);
	
	@Query(value="select e from User e where e.idNo like concat('%',?1,'%')")
	public Page<User> findByIdNoLike(String idNo,Pageable pageable);
	
	@Query(value="select e from User e where e.cell like concat('%',?1,'%')")
	public Page<User> findByCellLike(String cell,Pageable pageable);
	
	@Query(value="select e from User e where e.email like concat('%',?1,'%')")
	public Page<User> findByEmailLike(String email,Pageable pageable);
	
	@Query(value="select e from User e where e.userName like concat('%',?1,'%')")
	public Page<User> findByUserNameLike(String userName,Pageable pageable);

	@Query(value="select emp from User emp where emp.userType='0'")
	public List<User> findAllEmp();
	
	public User findUserByUid(Integer uid);
	
	@Query(value="select user, emp.empPassword,emp.empCode from User user,Emp emp where user.uid = emp.uid and user.uid in ?1 ")
	public Page<Object[]> findByUidIn(Collection<Integer> uid,Pageable pageable);
	
	@Query(value="select user, emp.empPassword,emp.empCode from User user,Emp emp where user.uid = emp.uid and user.uid in ?1 and (user.userName like concat ('%',?2,'%') or user.cell like concat('%',?2,'%') or emp.empCode like concat('%',?2,'%')) ")
	public Page<Object[]> findByValueAndUidIn(Collection<Integer> uid,String value,Pageable pageable);

	public Page<User> findByIdNoLikeAndUidIn(String idNo,Collection<Integer> uids,Pageable pageable);

	public Page<User> findByCellLikeAndUidIn(String cell,Collection<Integer> uids,Pageable pageable);

	public Page<User> findByEmailLikeAndUidIn(String email,Collection<Integer> uids,Pageable pageable);

	public Page<User> findByUserNameLikeAndUidIn(String userName,Collection<Integer> uids,Pageable pageable);

	@Query(value = "select u from User u where u.cell = ?1 and u.uid not in (select c.uid from Customer c )")
	User searchByCellAndUidNotInCustomer(String cell);
	
	@Query(value="select u from User u where (u.cell=?1 or u.idNo = ?1)")
	public User findByItem(String value);
	@Modifying
	@Query(value="delete from User u where u.uid in ?1")
	public void delete(Integer[] uids);
}
