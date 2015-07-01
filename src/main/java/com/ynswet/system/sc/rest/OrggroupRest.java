/**       
 * @Title: OrggroupRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月13日 上午11:48:57
 * @version V1.0  
 * <p>Copy 
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.rest;

import java.sql.Timestamp;
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
import com.ynswet.system.sc.domain.Orggroup;
import com.ynswet.system.sc.repository.OrggroupRepository;
import com.ynswet.system.sc.service.OrggroupService;

/**
 * 
 * 类功能说明：机构管理
 * <p>Title: OrggroupRest.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午11:48:57
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/orggroup")
public class OrggroupRest extends BaseRest{
	
	@Autowired
	private OrggroupService orggroupService;
	
	@Autowired
	private OrggroupRepository orggroupRepository;
	
	/**
	 * url：/ynlxcloud/orggroup
	 * 函数功能说明:保存机构
	 * @author 李玉鹏 
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orggroup
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure save(@ModelAttribute Orggroup orggroup){
		SingleJsonStructure json = new SingleJsonStructure();
		orggroup.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
		orggroupRepository.saveAndFlush(orggroup);
		json.setMsg("保存成功！");
		return json;
	}
	/**
	 * url：/ynlxcloud/orggroup/{orggroupId}
	 * 函数功能说明:修改机构
	 * @author 李玉鹏 
	 * @date 2015年5月25日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orggroupId
	 * @param @param orggroup
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{orggroupId}",method = RequestMethod.POST)
	public SingleJsonStructure updateOrggroup(@PathVariable Integer orggroupId,@ModelAttribute Orggroup orggroup) {
		// TODO Auto-generated method stub
		Timestamp modifyTime = new Timestamp(System.currentTimeMillis());
		Orggroup oldOrggroup = orggroupRepository.findOne(orggroupId);
		orggroup.setCreateTime(oldOrggroup.getCreateTime());
		orggroup.setModifyTime(modifyTime);
		orggroupRepository.saveAndFlush(orggroup);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("更新成功!");
		return json;

	}
	/**
	 * url：/ynlxcloud/orggroup/{orggroupIds}
	 * 函数功能说明:删除机构
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orggroupIds
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{orggroupIds}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteOrggroup(@PathVariable String orggroupIds) {
		if(!orggroupIds.isEmpty()){
			String [] arrOrggroupIds = orggroupIds.split(",");
			for(String orggroupId : arrOrggroupIds){
				orggroupService.deleteOrggroup(Integer.valueOf(orggroupId));
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("删除成功！");
		return json;
	}
	/**
	 * url：/ynlxcloud/orggroup
	 * 函数功能说明:查询所有的机构
	 * @author 李玉鹏 
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return ListJsonStructure<Orggroup>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ListJsonStructure<Orggroup> findAll(){
		ListJsonStructure<Orggroup> json = new ListJsonStructure<Orggroup>();
		List<Orggroup> orggroupList= orggroupRepository.findAll();
		json.setRows(orggroupList);
		json.setMsg("查询成功！");
		return json;
	}
	
	/**
	 * url：/ynlxcloud/orggroup/toPage
	 * 函数功能说明:分页查询所有的机构
	 * @author 李玉鹏 
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param rows
	 * @param @return    
	 * @return ListJsonStructure<Orggroup>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<Orggroup> findAlltoPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		Pageable pageable = new PageRequest(page-1, size);
		Page<Orggroup> pages = orggroupRepository.findAll(pageable);
		ListJsonStructure<Orggroup> json = new ListJsonStructure<Orggroup>();
		json.setMsg("查询成功！");
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}
}
