/**
 * @Title: MenuRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:41:29
 * @version V1.0  
 * <p>Copy
 * <p>Company:广州合光信息科技有限公司</p>
 */
package com.ynswet.system.sc.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.ynswet.common.domain.SingleJsonStructure;
import com.ynswet.common.rest.BaseRest;
import com.ynswet.common.util.BeanUtils;
import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import com.ynswet.common.domain.ListJsonStructure;
import com.ynswet.system.sc.domain.Homepage;
import com.ynswet.system.sc.repository.HomepageRepository;

/**
 *
 * 菜单Restful
 * <p>
 * Title: MenuRest.java
 * </p>
 *
 * @author 张明坤
 * @date 2015年5月22日 下午10:40:51 类修改者 修改日期 修改说明
 * @version V1.0
 *          <p>
 *          Description:立翔云
 *          </p>
 *          <p>
 *          Copyright: Copyright (c) 2015
 *          </p>
 *          <p>
 *          Company:广州合光信息科技有限公司
 *          </p>
 */
@RestController
@RequestMapping("/homepage")
public class HomepageRest {

	@Autowired
	private HomepageRepository homepageRepository;

	/**
	 *
	 * 函数功能说明
	 *
	 * @author 原勇
	 * @date 2015年6月18日 修改者名字 修改日期 修改内容
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Homepage> findAll() {

		List<Homepage> homepageList = homepageRepository.findAll();

		return homepageList;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Homepage findById(@PathVariable Integer id) {

		return homepageRepository.getOne(id);
	}

	/**
	 *
	 * 函数功能说明
	 *
	 * @author 原勇
	 * @date 2015年6月18日 修改者名字 修改日期 修改内容
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<Homepage> findAllToPage(
			@RequestParam("homepageName")String homepageName,
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		Pageable pageable = new PageRequest(page-1, size);
		ListJsonStructure<Homepage> json = new ListJsonStructure<Homepage>();
		Page<Homepage> homepageList;
		if(homepageName.equals("")){
			homepageList = homepageRepository.findAllOrderByCreateTimeDesc(pageable);
		}else{
			homepageList = homepageRepository.findByHomepageNameLike(homepageName,pageable);
		}

		json.setRows(homepageList.getContent());
		json.setMsg(BaseRest.FIND_SUCCESS);
		json.setTotal((int) homepageList.getTotalElements());
		return json;
	}

	/**
	 *
	 * 函数功能说明：保存主页菜单
	 *
	 * @author 李玉鹏
	 * @date 2015年11月09日 修改者名字 修改日期 修改内容
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure save(@ModelAttribute Homepage homepage){
		Integer homepageId = homepage.getHomepageId();
		if(homepageId!=null){
			Homepage homepage1 = homepageRepository.findOne(homepageId);
			homepage.setCreateTime(homepage1.getCreateTime());
		}else{
			homepage.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
		}
		Homepage homepageOld = homepageRepository.saveAndFlush(homepage);
		SingleJsonStructure json = new SingleJsonStructure();
		if(homepageOld==null){
			json.setMsg(BaseRest.SAVE_SUCCESS);
			return json;
		}
		return json;
	}
	/**
	 *
	 * 函数功能说明：删除主页菜单
	 *
	 * @author 李玉鹏
	 * @date 2015年11月09日 修改者名字 修改日期 修改内容
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */

	@RequestMapping(value="/{homepageIds}",method = RequestMethod.DELETE)
	public SingleJsonStructure delete (@PathVariable String homepageIds) {
		if(!homepageIds.isEmpty()){
			String[] arrHomepageIds = homepageIds.split(",");
			for(String homepageId : arrHomepageIds){
				homepageRepository.delete(Integer.valueOf(homepageId));
				homepageRepository.flush();
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.DELTE_SUCCESS);
		return json;
	}

	/**
	 *
	 * 函数功能说明：删除主页菜单
	 *
	 * @author 李玉鹏
	 * @date 2015年11月09日 修改者名字 修改日期 修改内容
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */

	@RequestMapping(value="/findByRoles",method = RequestMethod.GET)
	public List<Map<String,Object>> findByRoles (@RequestParam("roles[]") Integer[] roles) {
		List<Object[]> results = homepageRepository.findByRoles(roles);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Object[] result : results) {
			Role role = (Role) result[0];
			String homepageName = (String) result[1];
			Map<String, Object> roleMap = BeanUtils.toMap(role);
			roleMap.put("homepageName", homepageName);
			list.add(roleMap);
		}
		return list;
	}
}
