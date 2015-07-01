package com.ynswet.system.sc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
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
	
	@Query(value="select myUser from User myUser where myUser.cell = ?1 or myUser.idNo = ?2 or myUser.email = ?3")
	public User checkUserUniqueness(String cell,String idNo,String email);
	
	public User findByIdNo(String idNo);
	
	public User findByCell(String cell);
	
	public User findByEmail(String email);
	
	public User findByUserName(String userName);
	
	public Page<User> findByIdNoLike(String idNo,Pageable pageable);
	
	public Page<User> findByCellLike(String cell,Pageable pageable);
	
	public Page<User> findByEmailLike(String email,Pageable pageable);
	
	public Page<User> findByUserNameLike(String userName,Pageable pageable);
	
	
}
