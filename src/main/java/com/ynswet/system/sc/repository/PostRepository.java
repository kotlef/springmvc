/**
 * @Title: PostRepository.java
 * @Package com.ynswet.homecloud.common.repository
 * @Description: TODO
 * @author 原勇
 * @date 2015年5月10日 上午11:33:14
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.Org;
import com.ynswet.system.sc.domain.Post;
import com.ynswet.system.sc.domain.User;

/**
 * 类功能说明:岗位管理
 * <p>Title: PostRepository.java</p>
 * @author 原勇
 * @date 2015年5月10日 上午11:33:14
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:云南立翔开发平台</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface PostRepository extends JpaRepository<Post, Integer> {

	@Modifying
	@Query(value="select p.id.orgid from Post p where p.id.uid=?1  order by p.id.orgid")
	public Collection<Integer> findUserOrgIdsByUid(Integer uid);
	@Modifying
	@Query(value="select p.id.roleId from Post p where p.id.uid=?1 order by p.id.roleId")
	public Collection<Integer> findUserRoleIdsByUid(Integer uid);
	
	@Query(value="Select myUser from User myUser,Post myPost where myUser.uid=myPost.id.uid and myPost.id.orgid=?1 and myPost.id.roleId="
			+ "(select myRole.roleId from Role myRole where myRole.roleCode=?2)")
	public List<User> findUserByOrgidAndRoleCode(Integer orgid,String roleCode);
	
	@Query(value="Select myUser from User myUser,Post myPost where myUser.uid=myPost.id.uid  and myPost.id.orgid=?1 and myPost.id.roleId=?2")
	public List<User> findUserByOrgidAndRoleId(Integer orgid,Integer roleId);
	
	@Query(value="select p from Post p where p.id.orgid=?1 and p.id.roleId = ?2 and p.id.uid=?3")
	public Post findByPostId(Integer orgid,Integer roleId,Integer uid);
	
	@Query(value="select p from Post p where p.id.uid=?1  order by p.createTime desc")
	public List<Post> findByUid(Integer uid);
	
	@Query(value="select org from Post p ,Org org where p.id.orgid =org.orgid and p.id.uid=?1  order by p.createTime desc")
	public List<Org> findByUidToOrg(Integer uid);
	
	@Query(value="select u.uid from User u, Post p where u.uid=p.id.uid and p.id.orgid = ?1")
	public List<Integer> findByOrgid(Integer ogrid);
	
	@Query(value="select e.uid from Emp e, Post p where e.uid=p.id.uid and p.id.orgid = ?1")
	public List<Integer> findByOrgidToEmp(Integer ogrid);
	
	@Query(value="select distinct  p.id.uid from Post p where p.id.orgid in ?1 ")
	public List<Integer> findByOrgidIn(Collection<Integer> orgid);
	
	@Query(value="select p.id.orgid from Post p where p.id.uid=?1 group by p.id.orgid")
	public List<Integer> findOrgidByUid(Integer uid);  
	
	@Query(value="select p.id.roleId from Post p where p.id.uid=?1 AND p.id.orgid=?2")
	public List<Integer> findRoleidByUidOrgid(Integer uid,Integer orgid); 
	
	@Query(value="select p from Post p where p.id.uid in ?1")
	public List<Post> findByUidIn(Integer[] uids);
	@Modifying
	@Query(value="delete from Post p where p.id.uid in ?1")
	public void delete(Integer[] uids);
}
