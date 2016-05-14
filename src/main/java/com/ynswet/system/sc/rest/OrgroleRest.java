package com.ynswet.system.sc.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.ynswet.common.rest.BaseRest;
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
import com.ynswet.common.util.BeanUtils;
import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.Homepage;
import com.ynswet.system.sc.domain.Orgrole;
import com.ynswet.system.sc.domain.OrgroleId;
import com.ynswet.system.sc.domain.Role;
import com.ynswet.system.sc.repository.HomepageRepository;
import com.ynswet.system.sc.repository.OrgroleRepository;
import com.ynswet.system.sc.repository.RoleRepository;


@RestController
@RequestMapping(value="/orgrole")
public class OrgroleRest extends BaseRest {
	@Autowired
	private OrgroleRepository orgroleRepository;
	@Autowired
	private RoleRepository roleRepository;
	@Autowired
	private HomepageRepository homepageRepository;
	/**
	 * 
	 * 函数功能说明:添加组织角色
	 * @author 张毕思 
	 * @date 2015年12月28日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgid
	 * @param @param roleIds
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method=RequestMethod.POST)
	public SingleJsonStructure save(
				@RequestParam("orgid")Integer orgid,
				@RequestParam("roleIds")String roleIds
			){
		String status = "0";
		Date createTime = DateTimeUtils.getSystemCurrentTimeMillis();
		Date modifyTime = DateTimeUtils.getSystemCurrentTimeMillis();
		if(!roleIds.isEmpty()){
			String [] arrRoleIds = roleIds.split(",");
			for(String roleId : arrRoleIds){
				Orgrole or = orgroleRepository.findByOrgidAndRoleId(orgid, Integer.valueOf(roleId));
				if(or == null){
					Orgrole orgrole = new Orgrole();
					OrgroleId id = new OrgroleId();
					id.setOrgid(orgid);
					id.setRoleId(Integer.valueOf(roleId));
					orgrole.setCreateTime(createTime);
					orgrole.setId(id);
					orgrole.setStatus(status);
					orgrole.setModifyTime(modifyTime);
					orgroleRepository.saveAndFlush(orgrole);
				}
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.SAVE_SUCCESS);
		return json;
	}
	/**
	 * url:
	 * 函数功能说明:修改组织角色
	 * @author 张毕思 
	 * @date 2016年1月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgrole
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/update",method = RequestMethod.POST)
	public SingleJsonStructure update(@ModelAttribute Orgrole orgrole, @ModelAttribute OrgroleId id){
System.out.println(id.getOrgid());
System.out.println(id.getRoleId());
		Orgrole oldOrgrole=orgroleRepository.findByOrgidAndRoleId(id.getOrgid(), id.getRoleId());
	System.out.println(oldOrgrole);
		orgrole.setId(id);
		orgrole.setCreateTime(oldOrgrole.getCreateTime());
		orgrole.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		orgroleRepository.saveAndFlush(orgrole);
		SingleJsonStructure json = new SingleJsonStructure();
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明:根据uid查询出Role
	 * @author 张毕思 
	 * @date 2015年8月5日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgid
	 * @param @return    
	 * @return List<Role>   
	 * @throws
	 */
	@RequestMapping(value="/findByOrgidToRole/{orgid}", method = RequestMethod.GET)
	public List<Role> searchByUid(@PathVariable("orgid")Integer orgid){
		Collection<Integer> roleIds = orgroleRepository.findByOrgid(orgid);
		List<Role> listRoles = roleRepository.findByroleIdIn(roleIds);
		return listRoles;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月6日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param roleId
	 * @param @return    
	 * @return List<Integer>   
	 * @throws
	 */
	@RequestMapping(value="/findByRoleIdToOrg/{roleId}",method = RequestMethod.GET)
	public ListJsonStructure<Integer> findByRoleIdToOrg(@PathVariable("roleId")Integer roleId){
		List<Integer> orgids = orgroleRepository.findByRoleId(roleId);
		ListJsonStructure<Integer> json = new ListJsonStructure<Integer>();		
		json.setRows(orgids);
		return json;
	}
	
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月3日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgid
	 * @param @return    
	 * @return ListJsonStructure<Role>   
	 * @throws
	 */
	@RequestMapping(value="/searchByOrgIdAll/{orgid}", method = RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> searchByOrgidAll(@PathVariable("orgid")Integer orgid){
		Collection<Integer> roleIds = orgroleRepository.findByOrgid(orgid);
		List<Role> listRoles = roleRepository.findByroleIdIn(roleIds);
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		for(Role result : listRoles){
			Homepage homepage = homepageRepository.findByHomePageId(result.getHomepageId());
			Map<String, Object> map = BeanUtils.toMap(result);
			if(null!=homepage){
				map.put("homePageName",homepage.getHomepageName() );
				map.put("homepageUrl",homepage.getHomepageUrl() );
			}
			listmap.add(map);
		}
		ListJsonStructure<Map<String,Object>> json = new ListJsonStructure<Map<String,Object>>();
		json.setRows(listmap);
		json.setTotal(listmap.size());
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明:根据orgid查找出所拥有的角色
	 * @author 张毕思 
	 * @date 2015年8月5日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgid
	 * @param @return    
	 * @return Collection<Integer>   
	 * @throws
	 */
	@RequestMapping(value="/searchByorgidTopage/{orgid}", method = RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> searchByorgidTopage(@PathVariable("orgid")Integer orgid,
			@RequestParam("page")int page,
			@RequestParam("rows")int size){
		Pageable pageable = new PageRequest(page-1, size);
		Collection<Integer> roleIds = orgroleRepository.findByOrgid(orgid);
		Page<Role> pageRoles = roleRepository.findByroleIdIn(roleIds,pageable);
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		for(Role result : pageRoles.getContent()){
			Homepage homepage = homepageRepository.findByHomePageId(result.getHomepageId());
			Map<String, Object> map = BeanUtils.toMap(result);
			if(null!=homepage){
				map.put("homePageName",homepage.getHomepageName() );
				map.put("homepageUrl",homepage.getHomepageUrl() );
			}
			listmap.add(map);
		}
		ListJsonStructure<Map<String,Object>> json = new ListJsonStructure<Map<String,Object>>();
		json.setRows(listmap);
		json.setTotal((int)pageRoles.getTotalElements());
		return json;
	}
	/**
	 * 
	 * 函数功能说明:删除组织角色
	 * @author 张毕思 
	 * @date 2015年12月28日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgid
	 * @param @param roleIds
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{ids}",method=RequestMethod.DELETE)
	public SingleJsonStructure delete(@PathVariable String ids){
		if(!ids.isEmpty()){
			String [] arrRoleIds = ids.split(",");
			for(int i=1;i<arrRoleIds.length;i++){
				Orgrole orgrole = orgroleRepository.findByOrgidAndRoleId(Integer.valueOf(arrRoleIds[0]), Integer.valueOf(arrRoleIds[i]));
				if(null != orgrole){
					orgroleRepository.delete(orgrole);
					orgroleRepository.flush();
				}
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		return json;
	}
	
}
