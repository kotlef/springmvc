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

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.ynswet.system.sc.domain.OrgRes;
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.domain.Station;
import com.ynswet.system.sc.repository.OrgRepository;
import com.ynswet.system.sc.repository.OrgResRepository;
import com.ynswet.system.sc.repository.ResRepository;
import com.ynswet.system.sc.repository.StationRepository;
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
	
	@Autowired
	private ResRepository resRepository;
	
	@Autowired
	private StationRepository  stationRepository;
	
	@Autowired
	private OrgResRepository orgResRepository;
	
	/**
	 * url:/springmvc/org
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
	public SingleJsonStructure saveOrg(@ModelAttribute Org org,@ModelAttribute Station station) {
		Date createTime= DateTimeUtils.getSystemCurrentTimeMillis();
		Date modifyTime=DateTimeUtils.getSystemCurrentTimeMillis();
		org.setCreateTime(createTime);
		org.setModifyTime(modifyTime);
		org.setState("open");
		if(org.getParentId() == null){
			org.setParentId(0);
			org.setTreeLevel(0);
		}else{
			Org parentOrg = orgRepository.findOne(org.getParentId());
			parentOrg.setState("closed");
			orgRepository.saveAndFlush(parentOrg);
			org.setTreeLevel(parentOrg.getTreeLevel() + 1);
		}
		Org o = orgRepository.saveAndFlush(org);
		//保存服务站和资源
		if(org.getStationFlag().equals("Y")){
			station.setCreateTime(createTime);
			station.setModifyTime(modifyTime);
			station.setOrgid(o.getOrgid());
			station.setStationName(org.getOrgName());
			station.setStationNo(org.getOrgCode());
			stationRepository.saveAndFlush(station);
			Res rs = resRepository.findByitemIdAndResType(o.getOrgid(), "S");
			if(null == rs){
				Res res = new Res();
				res.setItemId(o.getOrgid());
				res.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
				res.setNote(org.getOrgName());
				res.setResType("S");
				res.setNote(org.getOrgName());
				resRepository.saveAndFlush(res);
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.SAVE_SUCCESS);
		json.setRows(o.getParentId());
		return json;
	}
	
	/**
	 * url:/springmvc/org/{orgid}
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
	public SingleJsonStructure updateOrg(@PathVariable Integer orgid,@RequestParam("oldParentId") Integer oldParentId,@ModelAttribute Org org,@ModelAttribute Station station) {
		Org oldOrg = orgRepository.findOne(orgid);
		org.setOrgid(orgid);
		org.setCreateTime(oldOrg.getCreateTime());
		org.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		Org newOrg =orgRepository.saveAndFlush(org);
		if(!oldParentId.equals(newOrg.getParentId())){
			Org newParent = orgRepository.findByOrgid(newOrg.getParentId());
			newParent.setState("closed");
			orgRepository.saveAndFlush(newParent);
			Org oldParent = orgRepository.findByOrgid(oldParentId);
			List<Org> childs = orgRepository.findByParentId(oldParentId);
			if(childs.size()<1 && null!=oldParent){
				oldParent.setState("open");
				orgRepository.saveAndFlush(oldParent);
			}
		}
		if(org.getStationFlag().equals("Y")){
			station.setOrgid(orgid);
			station.setStationName(org.getOrgName());
			station.setStationNo(org.getOrgCode());
			stationRepository.saveAndFlush(station);
			Res r=resRepository.findByitemId(orgid);
			if(null==r){
				Res res = new Res();
				res.setItemId(orgid);
				res.setResType("S");
				res.setNote(org.getOrgName());
				res.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
				resRepository.saveAndFlush(res);
			}else{
				r.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
				r.setNote(org.getOrgName());
				resRepository.saveAndFlush(r);
			}
		}
		if(org.getStationFlag().equals("N")){
			Station sat = stationRepository.findOne(orgid);
			if(null !=sat){
				stationRepository.delete(sat);
				stationRepository.flush();
			}
			Res res = resRepository.findByitemIdAndResType(orgid, "S");
			if(null!=res){
				List<OrgRes> orgRes = orgResRepository.findByResIdAll(res.getResId());
				if(orgRes.size() >0){
					orgResRepository.delete(orgRes);
					orgResRepository.flush();
				}
				resRepository.delete(res);
				resRepository.flush();
			}
			List<OrgRes> orgRess = orgResRepository.findByItemIdAndResType(orgid, "S");
System.out.println(orgRess);
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.FIND_SUCCESS);
		return json;
	}
	
	/**
	 * url:/springmvc/org/{orgids}
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
	 * url:/springmvc/org
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
	 * url:/springmvc/org/toPage
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
	/**
	 * url:/springmvc/org/serchByParentId
	 * 函数功能说明:根据prentId查询组织
	 * @author 张毕思 
	 * @date 2015年7月15日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param id
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return List<Org>   
	 * @throws
	 */
	@RequestMapping(value="/serchByParentId",method = RequestMethod.GET)
	public List<Map<String,Object>> serchByParentId(@ModelAttribute("id") String id) {
		int parentId=0;
		if(null!=id&&!id.isEmpty()){
			parentId=Integer.parseInt(id);
		}
		List<Map<String,Object>> list=orgService.searchOrgAndStationByParentId(parentId);
		return list;
	}
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年12月9日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param id
	 * @param @return    
	 * @return List<Org>   
	 * @throws
	 */
	@RequestMapping(value="/serchOrgByParentIdNoSation",method=RequestMethod.GET)
	public List<Org> serchOrgByParentIdNoSation(@ModelAttribute("id") String id){
		int parentId=0;
		if(null!=id&&!id.isEmpty()){
			parentId=Integer.parseInt(id);
		}
		ListJsonStructure<Org> json = new ListJsonStructure<Org>();
		List<Org> orgList=orgRepository.findByParentIdNoStation(parentId);
		json.setRows(orgList);
		json.setTotal(orgList.size());
		return orgList;
	}
	/**
	 * url:/springmvc/org/searchaByValue
	 * 函数功能说明:条件查询
	 * @author 张毕思 
	 * @date 2015年7月15日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key
	 * @param @param value
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return ListJsonStructure<Org>   
	 * @throws
	 */
	@RequestMapping(value="/searchaByValue",method=RequestMethod.GET)
	public ListJsonStructure<Org> searchByKeyValueLikTopage(
			@RequestParam("value")String value){
		List<Org> List = orgRepository.findByOrgCodeOrorgNameLike(value,value);
		ListJsonStructure<Org> json = new ListJsonStructure<Org>();
		json.setRows(List);
		return json;
	}
	
	/**
	 * url：/springmvc/org/findOrgByParentIds/{parentIds}
	 * 函数功能说明:查询当前用户所在组织下的组织
	 * @author 张毕思 
	 * @date 2015年10月26日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param parentIds
	 * @param @return    
	 * @return List<Org>   
	 * @throws
	 */
	@RequestMapping(value="/searchOrgByIds",method=RequestMethod.GET)
	public List<Org> searchOrgByIds(@ModelAttribute("ids")String ids,
			@ModelAttribute("id")String id){
		List<Org> orgList = null;
		if(!ids.isEmpty()&& id.equals("")){
			String[] arrId= ids.split(",");
			List<Integer> listIds = new ArrayList<Integer>();
			for(String aid:arrId){
				listIds.add(Integer.parseInt(aid));
			}
			orgList=orgRepository.findByStatusAndOrgidIn("0", listIds);
		}else if(!id.equals("")){
			orgList=orgRepository.findByParentIdAndStatus(Integer.parseInt(id), "0");
		}
		return orgList;
	}
	/**
	 * rul:/springmvc/org/findNoteId
	 * 函数功能说明:查询父节点下的所有子节点
	 * @author 张毕思 
	 * @date 2015年10月26日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgids
	 * @param @return    
	 * @return List<Integer>   
	 * @throws
	 */
	@RequestMapping(value="/findNoteId",method=RequestMethod.GET)
	public Set<Integer> findNoteId(@RequestParam("parentIds")String parentIds){
		List<Org> listOrg = orgRepository.findAll();
		Set<Integer> set = new HashSet<Integer>();  
		if(!parentIds.isEmpty()){
			String [] arrOrgids = parentIds.split(",");
			for(String orgid:arrOrgids){
				List<Integer> listOgrid = orgService.getChildNodes(listOrg, Integer.valueOf(orgid));
				set.addAll(listOgrid);
			}
		}
		return set;
	}
	/**
	 * url:/springmvc/org/searchByParentIdAndStatus
	 * 函数功能说明:根据状态查询组织
	 * @author 张毕思 
	 * @date 2015年12月22日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param id
	 * @param @param status
	 * @param @return    
	 * @return List<Org>   
	 * @throws
	 */
	@RequestMapping(value="/searchByParentIdAndStatus",method = RequestMethod.GET)
	public List<Org> searchByParentIdAndStatus(@ModelAttribute("id") String id,@RequestParam("status")String status) {
		int parentId=0;
		if(null!=id&&!id.isEmpty()){
			parentId=Integer.parseInt(id);
		}
		ListJsonStructure<Org> json = new ListJsonStructure<Org>();
		List<Org> orgList=orgRepository.findByParentIdAndStatus(parentId, status);
		json.setRows(orgList);
		json.setTotal(orgList.size());
		return orgList;
	}
}
