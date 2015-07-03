/**       
 * @Title: PostRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月19日 下午1:36:56
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynswet.common.domain.ListJsonStructure;
import com.ynswet.common.domain.SingleJsonStructure;
import com.ynswet.common.rest.BaseRest;
import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.Post;
import com.ynswet.system.sc.repository.PostRepository;
import com.ynswet.system.sc.service.PostService;

/**
 * 类功能说明:岗位管理
 * <p>Title: PostRest.java</p>
 * @author 李玉鹏
 * @date 2015年5月19日 下午1:36:56
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/post")
public class PostRest extends BaseRest{
	
	@Autowired
	private PostService postService;
	
	@Autowired
	private PostRepository postRepository;
	
	/**
	 * url:/springmvc/post
	 * 函数功能说明:保存岗位信息
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param post
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure savepost(@ModelAttribute Post post) {
		post.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
		postRepository.saveAndFlush(post);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("保存成功！");
		return json;
	}
	
	/**
	 * url:/springmvc/post/{postid}
	 * 函数功能说明:修改岗位记录
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param post
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{postid}",method = RequestMethod.POST)
	public SingleJsonStructure updatepost(@PathVariable Integer postid,@ModelAttribute Post post) {
		Post oldpost = postRepository.findOne(postid);
		post.setCreateTime(oldpost.getCreateTime());
		post.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		postRepository.saveAndFlush(post);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("更新成功！");
		return json;
	}
	
	/**
	 * url:/springmvc/post/{postids}
	 * 函数功能说明:删除岗位记录
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param postids
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{postids}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteposts(@PathVariable String postids) {
		if(!postids.isEmpty()){
			String [] arrpostids = postids.split(",");
			for(String postid : arrpostids){
				postService.deletePost(Integer.valueOf(postid));
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("删除成功！");
		return json;
	}
	
	/**
	 * url:/springmvc/post
	 * 函数功能说明:查询所有的岗位记录
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return ListJsonStructure<post>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ListJsonStructure<Post> findAll() {
		ListJsonStructure<Post> json = new ListJsonStructure<Post>();
		List<Post> list = postRepository.findAll();
		json.setRows(list);
		return json;
	}	
	
	/**
	 * url:/springmvc/post/toPage
	 * 函数功能说明:分页查询所有的岗位记录
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param rows
	 * @param @return    
	 * @return ListJsonStructure<post>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<Post> findAlltoPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		Pageable pageable = new PageRequest(page-1, size);
		Page<Post> pages = postRepository.findAll(pageable);
		ListJsonStructure<Post> json = new ListJsonStructure<Post>();
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}
	
	
}
