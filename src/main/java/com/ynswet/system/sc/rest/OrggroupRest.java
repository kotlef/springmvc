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
		orggroup.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		if(orggroup.getParentId() == null){
			orggroup.setParentId(0);
			orggroup.setTreeLevel(0);
		}else{
			Orggroup parentOrggour=orggroupRepository.findByorggroupId(orggroup.getParentId());
			parentOrggour.setState("closed");
			orggroupRepository.saveAndFlush(parentOrggour);
			orggroup.setTreeLevel(parentOrggour.getTreeLevel()+1);
		}
		Orggroup group= orggroupRepository.saveAndFlush(orggroup);
		json.setMsg(BaseRest.SAVE_SUCCESS);
		json.setRows(group.getParentId());
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
	public SingleJsonStructure updateOrggroup(@PathVariable Integer orggroupId,@RequestParam("oldParentId")Integer oldParentId,@ModelAttribute Orggroup orggroup) {
		SingleJsonStructure json = new SingleJsonStructure();
		Orggroup checkOrggroupName= orggroupRepository.findByOrggroupNameAndorggroupIdNot(orggroup.getOrggroupName(), orggroupId);
		if(checkOrggroupName != null){
			json.setMsg(BaseRest.UPDATE_FAILURE);
			json.setRows("orggroupName");
			json.setSuccess(false);
			return json;
		}
		Timestamp modifyTime = new Timestamp(System.currentTimeMillis());
		Orggroup oldOrggroup = orggroupRepository.findByorggroupId(orggroupId);
		orggroup.setCreateTime(oldOrggroup.getCreateTime());
		orggroup.setModifyTime(modifyTime);
		orggroupRepository.saveAndFlush(orggroup);
		if(!oldParentId.equals(orggroup.getParentId())){
			Orggroup newParent=orggroupRepository.findOne(orggroup.getParentId());
			if(newParent != null){
				newParent.setState("closed");
				newParent.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
				orggroupRepository.saveAndFlush(newParent);
			}
			List<Orggroup> childs = orggroupRepository.findByParentId(oldParentId);
			Orggroup oldParent = orggroupRepository.findByorggroupId(oldParentId);
			if(childs.size()<1 && null!=oldParent){
				oldParent.setState("open");
				orggroupRepository.saveAndFlush(oldParent);
			}
		}
		json.setMsg(BaseRest.UPDATE_SUCCESS);
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
	/**
	 * 根据父节点ID查询子节点
	 * @author 张明坤
	 * @date 2015年5月26日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param parentId
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(value="/serchByParentId",method = RequestMethod.POST)
	public List<Orggroup> findOrggroupByParentId(@ModelAttribute("id") String id) {
		int parentId=0;
		if(null!=id&&!id.isEmpty()){
			parentId=Integer.parseInt(id);
		}
		ListJsonStructure<Orggroup> json = new ListJsonStructure<Orggroup>();
		List<Orggroup> orggroupList=orggroupRepository.findByParentId(parentId);
		json.setRows(orggroupList);
		json.setTotal(orggroupList.size());
		return orggroupList;
	}
	/**
	 * url:/ynlxcloud/orggroup/searchBykeyValue/toPage
	 * 函数功能说明:条件查询实现分页
	 * @author 张毕思 
	 * @date 2015年7月15日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key
	 * @param @param value
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return ListJsonStructure<Emp>   
	 * @throws
	 */
	@RequestMapping(value="searchBykeyValue")
	public ListJsonStructure<Orggroup> searchaByKeyValue(
			@RequestParam("value")String value){
		List<Orggroup> list=orggroupRepository.findByOrggroupNameLike(value);;
		ListJsonStructure<Orggroup> json = new ListJsonStructure<Orggroup>();
		json.setRows(list);
		return json;
	}
	
	/**
	 * url:/ynlxcloud/orggroup/find/parentId
	 * 函数功能说明:根据父节点ID查询
	 * @author 李玉鹏 
	 * @date 2015年7月16日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param id
	 * @param @return    
	 * @return List<Orggroup>   
	 * @throws
	 */
    @RequestMapping( value = "/find/parentId", method = RequestMethod.POST )
    public List<Orggroup> listLocalOrggroup(@ModelAttribute("id") String id)
    {
    	int parentId=0;
		if(null!=id&&!id.isEmpty()){
			parentId=Integer.parseInt(id);
		}
    	List<Orggroup> lists = orggroupRepository.findByParentId( parentId );
        return lists;
    }
    
    /**
	 * url:/ynlxcloud/orggroup/find/parentId
	 * 函数功能说明:根据父节点ID查询（带复选框）
	 * @author 李玉鹏 
	 * @date 2015年7月16日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param id
	 * @param @return    
	 * @return List<Orggroup>   
	 * @throws
	 */
    @RequestMapping( value = "/find/parentId/checked", method = RequestMethod.POST )
    public List<Orggroup> listCategorys(@ModelAttribute("id") String id)
    {
    	int parentId=0;
		if(null!=id&&!id.isEmpty()){
			parentId=Integer.parseInt(id);
		}
    	List<Orggroup> list = orggroupRepository.findByParentId( parentId );
        return list;
    }
    /**
     * url:/ynlxcloud/orggroup/checkOrggroupId
     * 函数功能说明:验证机构编码的唯一性
     * @author 张毕思 
     * @date 2015年12月12日
     * 修改者名字 修改日期
     * 修改内容
     * @param @param orggroupId
     * @param @return    
     * @return boolean   
     * @throws
     */
    @RequestMapping(value="/checkOrggroupId",method=RequestMethod.POST)
    public boolean checkOrggroupId(@RequestParam("id") int id){
    	Orggroup orggroup = orggroupRepository.findByorggroupId(id);
		if(null!=orggroup){
			return false;
		}else{
			return true;
		}
    }
    /**
     * /ynlxcloud/orggroup/checkorggroupName
     * 函数功能说明:验证组织名称的唯一
     * @author 张毕思 
     * @date 2015年12月14日
     * 修改者名字 修改日期
     * 修改内容
     * @param @param orggroupName
     * @param @return    
     * @return boolean   
     * @throws
     */
	@RequestMapping(value="/checkorggroupName",method = RequestMethod.POST)
	public boolean checkorggroupName(@RequestParam("text") String orggroupName){
    	Orggroup orggroup = orggroupRepository.findByorggroupName(orggroupName);
		if(null!=orggroup){
			return false;
		}else{
			return true;
		}
	}
	/**
	 * url:/ynlxcloud/orggroup/serchByParentIdAndStatus
	 * 函数功能说明:根据父节点id和状态查询子节点
	 * @author 张毕思 
	 * @date 2015年12月21日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param id
	 * @param @return    
	 * @return List<Orggroup>   
	 * @throws
	 */
	@RequestMapping(value="/serchByParentIdAndStatus",method = RequestMethod.POST)
	public List<Orggroup> findOrggroupByParentIdAndStatus(@ModelAttribute("id") String id ,@RequestParam("status") String status) {
		int parentId=0;
		if(null!=id&&!id.isEmpty()){
			parentId=Integer.parseInt(id);
		}
		ListJsonStructure<Orggroup> json = new ListJsonStructure<Orggroup>();
		List<Orggroup> orggroupList=orggroupRepository.findByParentIdAndStatus(parentId, status);
		json.setRows(orggroupList);
		json.setTotal(orggroupList.size());
		return orggroupList;
	}
}
