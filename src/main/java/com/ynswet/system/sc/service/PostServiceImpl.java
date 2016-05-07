/**       
 * @Title: PostServiceImpl.java
 * @Package com.ynswet.homecloud.common.service
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:05:21
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ynswet.common.util.BeanUtils;
import com.ynswet.system.sc.domain.Org;
import com.ynswet.system.sc.domain.Post;
import com.ynswet.system.sc.domain.Role;
import com.ynswet.system.sc.repository.OrgRepository;
import com.ynswet.system.sc.repository.PostRepository;
import com.ynswet.system.sc.repository.RoleRepository;

/**
 * 类功能说明
 * <p>Title: PostServiceImpl.java</p>
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:05:21
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class PostServiceImpl implements PostService{

	@Autowired
	private PostRepository postRepository;
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private RoleRepository roleRepository;

	/* (non-Javadoc)    
	 * @see com.ynswet.homecloud.common.service.PostService#deletePost(java.lang.Integer)    
	 */
	@Transactional
	public void deletePost(Integer postId) {
		// TODO Auto-generated method stub
		postRepository.delete(postId);
		postRepository.flush();
	}

	@Transactional
	public void deltePost(Integer orgid,Integer roleId,Integer uid) {
		// TODO Auto-generated method stub
		Post post = postRepository.findByPostId(orgid, roleId, uid);
		postRepository.delete(post);
		postRepository.flush();
	}

	@Transactional
	public List<Map<String,Object>> findPost(Integer uid) {
		List<Post> listPost = postRepository.findByUid(uid);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Post post:listPost){
			Map<String,Object> map = BeanUtils.toMap(post);
			Org org = orgRepository.findByOrgid(post.getId().getOrgid());
			Role r=roleRepository.findByroleId(post.getId().getRoleId());
			if(org != null){
				map.put("text", org.getOrgName());
			}
			if(r != null){
				map.put("roleName", r.getRoleName());
			}
			list.add(map);
		}
		return list;
	}

	@Transactional
	public List<Integer> findUids(Collection<Integer> orgids) {
		List<Integer> uids = postRepository.findByOrgidIn(orgids);
		return uids;
	}
}
