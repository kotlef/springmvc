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
import java.util.Map;

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
import com.ynswet.system.sc.domain.Org;
import com.ynswet.system.sc.domain.Post;
import com.ynswet.system.sc.domain.PostId;
import com.ynswet.system.sc.domain.User;
import com.ynswet.system.sc.repository.OrgRepository;
import com.ynswet.system.sc.repository.PostRepository;
import com.ynswet.system.sc.repository.RoleRepository;
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
	
	@Autowired
	private OrgRepository orgRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
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
	public SingleJsonStructure savepost(@ModelAttribute Post post,@ModelAttribute PostId postid) {
		post.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
		post.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		post.setStatus("O");
		post.setId(postid);
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
	
	/**
	 * url:/springmvc/post/list/searchUserByOrgidAndRoleCode
	 * 函数功能说明
	 * @author 张明坤 
	 * @date 2015年6月18日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgid
	 * @param @param type
	 * @param @return    
	 * @return List<Emp>   
	 * @throws
	 */
	@RequestMapping(value="/list/searchUserByOrgidAndRoleCode",method = RequestMethod.GET)
	public List<User> searchUserByOrgidAndRoleCode(@RequestParam Integer orgid,@RequestParam String roleCode) {
		List<User> list = postRepository.findUserByOrgidAndRoleCode(orgid, roleCode);
		return list;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 张明坤 
	 * @date 2015年11月3日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgid
	 * @param @param roleId
	 * @param @return    
	 * @return List<User>   
	 * @throws
	 */
	@RequestMapping(value="/list/searchUserByOrgidAndRoleId",method = RequestMethod.GET)
	public List<User> searchUserByOrgidAndRoleId(@RequestParam Integer orgid,@RequestParam Integer roleId) {
		List<User> list = postRepository.findUserByOrgidAndRoleId(orgid, roleId);
		return list;
	}
	
	/**
	 * url:/springmvc/post/list/searchUserByOrgCodeAndRoleCode
	 * 函数功能说明
	 * @author 李玉鹏
	 * @date 2015年7月20日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgCode
	 * @param @param roleId
	 * @param @return    
	 * @return List<Org>   
	 * @throws
	 */
	@RequestMapping(value="/list/searchUserByOrgCodeAndRoleCode",method = RequestMethod.GET)
	public List<User> searchUserByOrgCodeAndRoleCode(
			@RequestParam("orgCode") String orgCode,
			@RequestParam("roleCode") String roleCode) {
		Org org = orgRepository.findByOrgCode(orgCode);
		List<User> list = postRepository.findUserByOrgidAndRoleCode(org.getOrgid(), roleCode);
		return list;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 张明坤 
	 * @date 2015年11月3日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgCode
	 * @param @param roleId
	 * @param @return    
	 * @return List<User>   
	 * @throws
	 */
	@RequestMapping(value="/list/searchUserByOrgCodeAndRoleId",method = RequestMethod.GET)
	public List<User> searchUserByOrgCodeAndRoleId(
			@RequestParam("orgCode") String orgCode,
			@RequestParam("roleId") Integer roleId) {
		Org org = orgRepository.findByOrgCode(orgCode);
		List<User> list = postRepository.findUserByOrgidAndRoleId(org.getOrgid(), roleId);
		return list;
	}
	/**
	 * 
	 * 函数功能说明:根据uid查询出已分配的组织角色
	 * @author 张毕思 
	 * @date 2015年8月4日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uid
	 * @param @return    
	 * @return List<Role>   
	 * @throws
	 */
	@RequestMapping(value="/searchByUid/{uid}",method=RequestMethod.GET)
	public List<Map<String,Object>> searchByUid(@PathVariable("uid")Integer uid){
		List<Map<String,Object>> list = postService.findPost(uid);
		return list;
	}
	/**
	 * 
	 * 函数功能说明:删除岗位
	 * @author 张毕思 
	 * @date 2015年8月6日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param postids
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/deletePost/{postids}", method=RequestMethod.DELETE)
	public SingleJsonStructure deletePost(@PathVariable("postids")String postids){
		if(!postids.isEmpty()){
			String [] arrpostids = postids.split(",");
			postService.deltePost(Integer.valueOf(arrpostids[1]), Integer.valueOf(arrpostids[2]), Integer.valueOf(arrpostids[0]));
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("删除成功！");
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月4日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param post
	 * @param @param postId
	 * @param @param uid
	 * @param @param orgRoles
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/saveAll",method=RequestMethod.POST)
	public SingleJsonStructure saveAll(
			@ModelAttribute Post post,
			@ModelAttribute PostId postId,
			@RequestParam("uid")Integer uid,
			@RequestParam("orgRoles")String orgRoles){
		List<Post> posts = postRepository.findByUid(uid);
		//先循环删除该用户的所有岗位
		if(posts!=null){
			for(int b=0;b < posts.size();b++){
				postRepository.delete(posts.get(b));
				postRepository.flush();
			}
		}
		//循环添加该用户的所有岗位
		if(!orgRoles.isEmpty()){
			String[] arrIds = orgRoles.split(",");
			for(int a=0;a<arrIds.length;a++){
				if(arrIds[a].split("-").length!=0){
					String orgidroleid = (String)arrIds[a];
					String orgid = (String)orgidroleid.split("-")[0];
					String roleid = (String)orgidroleid.split("-")[1];
					postId.setOrgid(Integer.valueOf(orgid));
					postId.setRoleId(Integer.valueOf(roleid));
					postId.setUid(uid);
					post.setId(postId);
					post.setStatus("0");
					post.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
					postRepository.saveAndFlush(post);
				}
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.SAVE_SUCCESS);
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月4日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uid
	 * @param @return    
	 * @return String   
	 * @throws
	 */
	@RequestMapping(value="/findOrgidByUid/{uid}",method=RequestMethod.GET)
	public ListJsonStructure<Integer> findOrgidByUid(@PathVariable("uid")Integer uid){
		List<Integer> orgids = postRepository.findOrgidByUid(uid);
		ListJsonStructure<Integer> json = new ListJsonStructure<Integer>();
		json.setRows(orgids);
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月5日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uid
	 * @param @param orgid
	 * @param @return    
	 * @return List<Integer>   
	 * @throws
	 */
	@RequestMapping(value="/findRoleidByUidOrgid/{uid}/{orgid}",method=RequestMethod.GET)
	public ListJsonStructure<Integer> findRoleidByUidOrgid(@PathVariable("uid")Integer uid,@PathVariable("orgid")Integer orgid){
		List<Integer> roleids = postRepository.findRoleidByUidOrgid(uid,orgid);
		ListJsonStructure<Integer> json = new ListJsonStructure<Integer>();
		json.setRows(roleids);
		return json;
	}
}
