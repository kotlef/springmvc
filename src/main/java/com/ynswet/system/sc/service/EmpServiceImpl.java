package com.ynswet.system.sc.service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.common.domain.ListJsonStructure;
import com.ynswet.common.rest.BaseRest;
import com.ynswet.common.util.BeanUtils;
import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.Emp;
import com.ynswet.system.sc.domain.Post;
import com.ynswet.system.sc.domain.PostId;
import com.ynswet.system.sc.domain.Role;
import com.ynswet.system.sc.domain.User;
import com.ynswet.system.sc.domain.Userlogin;
import com.ynswet.system.sc.realm.UserManager;
import com.ynswet.system.sc.repository.EmpRepository;
import com.ynswet.system.sc.repository.OrgRepository;
import com.ynswet.system.sc.repository.OrgResRepository;
import com.ynswet.system.sc.repository.PostRepository;
import com.ynswet.system.sc.repository.RoleRepository;
import com.ynswet.system.sc.repository.UserRepository;
import com.ynswet.system.sc.repository.UserloginRepository;
import com.ynswet.system.sc.util.PasswordHelper;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;


/**
 *
 * 类功能说明：员工管理
 * <p>Title: EmpServiceImpl.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午11:28:58
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class EmpServiceImpl implements EmpService {

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private EmpRepository empRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private OrgResRepository orgResRepository;
	@Autowired
	private UserManager userManager;
	@Autowired
	private UserloginRepository userloginRepository;
	@Autowired
	private PasswordHelper passwordHelper;
	@Autowired
	private OrgRepository orgRepository;
	@Autowired
	private OrgService orgService;
	@Autowired
	private RoleRepository roleRepository;
	
	@Autowired
	private EhCacheManager ehCacheManager;
	/*
	 * (non-Javadoc)
	 * @see com.ynswet.homecloud.common.service.EmpService#deleteEmp(java.lang.Integer)
	 */
	@Transactional
	public void deleteEmp(Integer uid) {
		List<Post> p = postRepository.findByUid(uid);
		postRepository.delete(p);
		postRepository.flush();
		userRepository.delete(uid);
		userRepository.flush();
		empRepository.delete(uid);
		empRepository.flush();
	}
	
	@Transactional
	public List<Map<String,Object>> searchEmpInOrgids(Integer[] orgids) {
		List<User> results = empRepository.findEmpInORgids(orgids);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(User user : results){
			Map<String,Object> map = new HashMap<String,Object>();
			map.put("empUid", user.getUid());
			map.put("empName", user.getUserName());
			list.add(map);
		}
		return list;
	}
	@Transactional
	public List<Integer> oregresUids(Integer orgid) {
		List<Integer> listUids = postRepository.findByOrgidToEmp(orgid);
		return listUids;
	}

	@Transactional
	public List<Map<String,Object>> empDetil(Pageable pageable) {
		Page<Object[]> page=empRepository.findAllOrderBycreatTime(pageable);
		List<Object[]> result = page.getContent();
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for(Object[] rt:result){
			User u = (User)rt[0];
			Emp e = (Emp)rt[1];
			Map<String,Object> map = BeanUtils.toMap(u);
			map.put("empCode", e.getEmpCode());
			map.put("empPassword", e.getEmpPassword());
			map.put("empType", e.getEmpType());
			map.put("entryDate", e.getEntryDate());
			map.put("departDate", e.getDepartDate());
			map.put("postRank", e.getPostRank());
			map.put("resume", e.getResume());
			map.put("parentId", e.getParentId());
			list.add(map);
		}
		return list;
	}


	@Transactional
	public void saveOperate(String userName, String cell, String userType,
			String idType, String idNo, String email, String cell2,String status,
			String marriage, String blood, String gender, String education,
			String empCode,String empPassword, String postRank, String roleIds,Integer orgid, String note) {

		User user = new User();
		Timestamp createTime = new Timestamp(System.currentTimeMillis());
		Date modifyTime = DateTimeUtils.getSystemCurrentTimeMillis();
		user.setUserType(userType);
		user.setCreateTime(createTime);
		user.setModifyTime(modifyTime);
		user.setIdType(idType);
		user.setIdNo(idNo);
		user.setEmail(email);
		user.setCell(cell);
		user.setCell2(cell2);
		user.setUserName(userName);
		user.setMarriage(marriage);
		user.setBlood(blood);
		user.setGender(gender);
		user.setEducation(education);
		user.setStatus(status);
		user.setSecPassword("123456");
		user.setBindPassword("654321");
		user.setNote(note);
		User u = userRepository.saveAndFlush(user);
		
		Emp emp = new Emp();
		emp.setParentId(0);
		emp.setUid(u.getUid());
		emp.setCreateTime(createTime);
		emp.setModifyTime(modifyTime);
		emp.setEmpType(userType);
		emp.setPostRank(postRank);
		emp.setStatus(status);
		emp.setEmpCode(empCode);
		emp.setEmpPassword(empPassword);
		emp.setNote(note);
		empRepository.saveAndFlush(emp);
		String [] ids = roleIds.split(",");
		for(String roleId:ids){
			Post post = new Post();
			PostId id = new PostId();
			Role role = roleRepository.findByroleId(Integer.valueOf(roleId));
			id.setOrgid(orgid);
			id.setRoleId(role.getRoleId());
			id.setUid(u.getUid());
			post.setId(id);
			post.setStatus(status);
			post.setNote(note);
			post.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
			post.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
			postRepository.saveAndFlush(post); 
		}
		List<String> loginStringList=new ArrayList<String>();
		loginStringList.add(u.getCell());
		List<Userlogin> listlogin = new ArrayList<Userlogin>();
		for(String loginString:loginStringList){
			Userlogin userlogin = new Userlogin();
			userlogin.setPassword(BaseRest.DEFAULT_PASSWORD);
			userlogin.setUid(u.getUid());
			userlogin.setStatus(u.getStatus());
			userlogin.setLoginString(loginString);
			userlogin = passwordHelper.encryptPassword(userlogin);
			listlogin.add(userlogin);
		}
		userloginRepository.save(listlogin);
		userloginRepository.flush();	
	}

	@Transactional
	public void updateOperate(Integer uid,String userName,  String status,
			String roleIds, Integer orgid, String note,String empCode,String empPassword){
		User user = userRepository.findByUid(uid);
		user.setUserName(userName);
		user.setStatus(status);
		user.setNote(note);
		userRepository.saveAndFlush(user);
		List<Post> posts = postRepository.findByUid(uid);
		if(posts.size() >0){
			postRepository.delete(posts);
			postRepository.flush();
		}
		String [] ids = roleIds.split(",");
		for(String roleId:ids){
			Post post = new Post();
			PostId id = new PostId();
			post.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
			post.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
			id.setOrgid(orgid);
			id.setRoleId(Integer.valueOf(roleId));
			id.setUid(uid);
			post.setId(id);
			post.setNote(note);
			post.setStatus(status);
			postRepository.saveAndFlush(post);
		}
		
		Emp emp=empRepository.findByUid(uid);
		emp.setEmpCode(empCode);
		emp.setEmpPassword(empPassword);
		emp.setStatus(status);
		emp.setNote(note);
		empRepository.saveAndFlush(emp);
	}

	@Transactional
	public boolean delete(Integer[] uids) {
		boolean msg=false;
		userRepository.delete(uids);
		userRepository.flush();
		empRepository.delete(uids);
		empRepository.flush();
		postRepository.delete(uids);
		postRepository.flush();
		List<Userlogin> userlogins= userloginRepository.findByUidIn(uids);
//		List<Post> psots= postRepository.findByUidIn(uids);
		CacheManager cm=ehCacheManager.getCacheManager();
		Cache cache=cm.getCache("shiroAuthenticationCache");
//		postRepository.delete(psots);
//		postRepository.flush();
		for(Userlogin ul:userlogins){
			userloginRepository.delete(ul);
			userloginRepository.flush();
			cache.remove(ul.getLoginString());
		}
		msg=true;
		return msg;
	}

	@Transactional
	public ListJsonStructure<Map<String, Object>> searchByValue(String value,Pageable pageable) {
		Page<Object[]> pages=empRepository.findByValue(value, pageable);
		List<Object[]> list = pages.getContent();
		List<Map<String,Object>> lt = new ArrayList<Map<String,Object>>();
		for(Object[] result : list){
			User u = (User)result[0];
			Emp e = (Emp)result[1];
			Map<String,Object> map = BeanUtils.toMap(u);
			map.put("empCode", e.getEmpCode());
			map.put("empPassword", e.getEmpPassword());
			map.put("empType", e.getEmpType());
			map.put("entryDate", e.getEntryDate());
			map.put("departDate", e.getDepartDate());
			map.put("postRank", e.getPostRank());
			map.put("resume", e.getResume());
			map.put("parentId", e.getParentId());
			lt.add(map);
		}
		ListJsonStructure<Map<String, Object>> json = new ListJsonStructure<Map<String, Object>>();
		json.setRows(lt);
		json.setTotal((int)pages.getTotalPages());
		return json;
	}
}
