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

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ynswet.system.sc.repository.PostRepository;

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

	/* (non-Javadoc)    
	 * @see com.ynswet.homecloud.common.service.PostService#deletePost(java.lang.Integer)    
	 */
	@Transactional
	public void deletePost(Integer postId) {
		// TODO Auto-generated method stub
		postRepository.delete(postId);
		postRepository.flush();
	}

}
