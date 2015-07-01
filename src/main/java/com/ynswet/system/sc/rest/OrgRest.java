/**       
 * @Title: OrgRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:40:13
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
import com.ynswet.system.sc.domain.Org;
import com.ynswet.system.sc.repository.OrgRepository;
import com.ynswet.system.sc.service.OrgService;

/**
 * 类功能说明:组织管理
 * <p>Title: OrgRest.java</p>
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:40:13
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/org")
public class OrgRest extends BaseRest{
	
	@Autowired
	private OrgRepository orgRepository;
	
	@Autowired
	private OrgService orgService;
	
	/**
	 * url:/ynlxcloud/org
	 * 函数功能说明:保存组织
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param org
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure saveOrg(@ModelAttribute Org org) {
		org.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
		orgRepository.saveAndFlush(org);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("保存成功！");
		return json;
	}
	
	/**
	 * url:/ynlxcloud/org/{orgid}
	 * 函数功能说明
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param org
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{orgid}",method = RequestMethod.POST)
	public SingleJsonStructure updateOrg(@PathVariable Integer orgid,@ModelAttribute Org org) {
		Org oldOrg = orgRepository.findOne(orgid);
		org.setCreateTime(oldOrg.getCreateTime());
		org.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		orgRepository.saveAndFlush(org);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("更新成功！");
		return json;
	}
	
	/**
	 * url:/ynlxcloud/org/{orgids}
	 * 函数功能说明:删除组织
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgids
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{orgids}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteOrgs(@PathVariable String orgids) {
		if(!orgids.isEmpty()){
			String [] arrOrgids = orgids.split(",");
			for(String orgid : arrOrgids){
				orgService.deleteOrg(Integer.valueOf(orgid));
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("删除成功！");
		return json;
	}
	
	/**
	 * url:/ynlxcloud/org
	 * 函数功能说明:查询所有的组织
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return ListJsonStructure<Org>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ListJsonStructure<Org> findAll() {
		ListJsonStructure<Org> json = new ListJsonStructure<Org>();
		List<Org> list = orgRepository.findAll();
		json.setRows(list);
		return json;
	}	
	
	/**
	 * url:/ynlxcloud/org/toPage
	 * 函数功能说明:分页查询所有的组织
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param rows
	 * @param @return    
	 * @return ListJsonStructure<Org>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<Org> findAlltoPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		Pageable pageable = new PageRequest(page-1, size);
		Page<Org> pages = orgRepository.findAll(pageable);
		ListJsonStructure<Org> json = new ListJsonStructure<Org>();
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}
}
