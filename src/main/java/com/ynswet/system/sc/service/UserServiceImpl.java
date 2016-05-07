package com.ynswet.system.sc.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.common.domain.ListJsonStructure;
import com.ynswet.common.util.BeanUtils;
import com.ynswet.system.sc.domain.User;
import com.ynswet.system.sc.repository.OrgResRepository;
import com.ynswet.system.sc.repository.PostRepository;
import com.ynswet.system.sc.repository.UserRepository;

/**
 * 
 * 类功能说明：用户管理
 * <p>Title: UserServiceImpl.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:32:05
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class UserServiceImpl implements UserService {
	
	
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private OrgResRepository orgResRepository;

	/*
	 * (non-Javadoc)    
	 * @see com.ynswet.homecloud.common.service.UserService#deleteUser(java.lang.Integer)
	 */
	@Transactional
	public void deleteUser(Integer uid) {
		// TODO Auto-generated method stub
		userRepository.delete(uid);
		userRepository.flush();
	}
	@Transactional
	public ListJsonStructure<Map<String,Object>> findInOrgids(Collection<Integer> orgids,Pageable pageable) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Integer> listUids = postRepository.findByOrgidIn(orgids);
		Page<Object[]> result = userRepository.findByUidIn(listUids, pageable);
		for(Object[] rs:result){
			User u = (User)rs[0];
			String empPassword = (String)rs[1];
			String empCode = (String)rs[2];
			Map<String,Object> map = BeanUtils.toMap(u);
			map.put("empCode", empCode);
			map.put("empPassword", empPassword);
			list.add(map);
		}
		ListJsonStructure<Map<String,Object>> json = new ListJsonStructure<Map<String,Object>>();
		json.setTotal((int)result.getTotalElements());
		json.setRows(list);
		return json;
	}
	@Transactional
	public ListJsonStructure<Map<String, Object>> searchByValue(
			Collection<Integer> orgids, String value, Pageable pageable) {
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		List<Integer> listUids = postRepository.findByOrgidIn(orgids);
		Page<Object[]> result = userRepository.findByValueAndUidIn(listUids, value, pageable);
		for(Object[] rs:result){
			User u = (User)rs[0];
			String empPassword = (String)rs[1];
			String empCode = (String)rs[2];
			Map<String,Object> map = BeanUtils.toMap(u);
			map.put("empCode", empCode);
			map.put("empPassword", empPassword);
			list.add(map);
		}
		ListJsonStructure<Map<String,Object>> json = new ListJsonStructure<Map<String,Object>>();
		json.setTotal((int)result.getTotalElements());
		json.setRows(list);
		return json;
	}
}
