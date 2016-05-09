/**       
 * @Title: Orgres.java
 * @Package com.ynswet.system.sc.rest
 * @Description: TODO
 * @author 孙越
 * @date 2015年8月10日 下午12:03:00
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynswet.common.domain.ListJsonStructure;
import com.ynswet.common.domain.SingleJsonStructure;
import com.ynswet.common.rest.BaseRest;
import com.ynswet.common.util.BeanUtils;
import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.OrgRes;
import com.ynswet.system.sc.domain.OrgResId;
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.realm.UserManager;
import com.ynswet.system.sc.repository.OrgRepository;
import com.ynswet.system.sc.repository.OrgResRepository;
import com.ynswet.system.sc.repository.ResRepository;
import com.ynswet.system.sc.service.OrgresService;

/**
 * 类功能说明
 * <p>Title: Orgres.java</p>
 * @author 孙越
 * @date 2015年8月10日 下午12:03:00
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/orgres")
public class OrgresRest extends BaseRest{
	@Autowired
	private OrgresService orgresService;
	
	@Autowired
	private OrgResRepository orgresRepository;
	@Autowired
	private UserManager userManager;
	@Autowired
	private ResRepository resRepository;
	@Autowired
	private OrgRepository orgRepository;
	
	/**
	 * url:/springmvc/orgres/
	 * 函数功能说明:为组织添加资源后保存到中间表orgres
	 * @author 孙越 
	 * @date 2015年8月10日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param houseId
	 * @param @param uid
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure saveOregres(@RequestParam (value="orgid")Integer orgid,@RequestParam(value="resid") Integer resid) {
		OrgResId oid=new OrgResId();
		oid.setOrgid(orgid);
		oid.setResId(resid);
		OrgRes orgres=new OrgRes();
		orgres.setId(oid);
		orgres.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
		orgresRepository.saveAndFlush(orgres);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("保存成功！");
		return json;
	}
	/**
	 * url:/springmvc/orgres/searchResByOrgId/{}
	 * 函数功能说明:查询组织下的资源
	 * @author 孙越 
	 * @date 2015年8月10日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgid
	 * @param @return    
	 * @return List<OrgRes>   
	 * @throws
	 */
	@RequestMapping(value="/searchResByOrgId/{orgid}",method = RequestMethod.GET)
	public List<OrgRes> searchResByOrgId(@PathVariable Integer orgid) {
		List<OrgRes> list = orgresRepository.findOrgresByOrgId(orgid);
		return list;
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
	 * @return ListJsonStructure<Res>   
	 * @throws
	 */
	@RequestMapping(value="/searchByOrgIdAll/{orgid}",method = RequestMethod.GET)
	public ListJsonStructure<Map<String,Object>> searchByOrgidAll(@PathVariable("orgid")Integer orgid) {
		Collection<Integer> resIds = orgresRepository.findByOrgid(orgid);
		List<Res> listRess = resRepository.findByresIdIn(resIds);
		List<Map<String,Object>> listmap = new ArrayList<Map<String,Object>>();
		for(Res result : listRess){
			Map<String, Object> map = BeanUtils.toMap(result);
			String resName = orgRepository.findNameByOrgid(result.getItemId());
			map.put("resName",resName );
			listmap.add(map);
		}
		ListJsonStructure<Map<String,Object>> json = new ListJsonStructure<Map<String,Object>>();
		json.setRows(listmap);
		return json;
	}
	
	/**
	 * url:/springmvc/orgres/deleteResById
	 * 函数功能说明:删除组织下的资源
	 * @author 孙越 
	 * @date 2015年8月10日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uids
	 * @param @param houseIds
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/deleteResById/{orgResIds}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteResById(@PathVariable String orgResIds) {
		if(!orgResIds.isEmpty()){
System.out.println(orgResIds);			
			String [] arrIds = orgResIds.split(",");
			int orgid = Integer.valueOf(arrIds[0]);
			for(int i=1;i<arrIds.length;i++){
System.out.println(arrIds[i]);
				orgresService.deleteByOidAndRid(orgid,Integer.valueOf(arrIds[i]));
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.DELTE_SUCCESS);
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月8日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param res
	 * @param @param orgRes
	 * @param @param orgResId
	 * @param @param orgids
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/saveAll",method = RequestMethod.POST)
	public SingleJsonStructure saveAll(
			@ModelAttribute("res") Res res,
			@ModelAttribute("orgRes") OrgRes orgRes,
			@ModelAttribute("orgResId") OrgResId orgResId,
			@RequestParam("oIdr")String orgids){
		boolean b = orgresService.saveOrUpdateRes(1, res, orgRes, orgResId, orgids);
		SingleJsonStructure json = new SingleJsonStructure();
		if(b==true){
			json.setMsg(SAVE_SUCCESS);
		}else{
			json.setMsg(BaseRest.SAVE_FAILURE);
		}
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月8日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param res
	 * @param @param orgRes
	 * @param @param orgResId
	 * @param @param orgids
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/updateAll",method = RequestMethod.POST)
	public SingleJsonStructure updataAll(
			@ModelAttribute("res") Res res,
			@ModelAttribute("orgRes") OrgRes orgRes,
			@ModelAttribute("orgResId") OrgResId orgResId,
			@RequestParam("oIdr")String orgids){
		boolean b = orgresService.saveOrUpdateRes(2, res, orgRes, orgResId, orgids);
		SingleJsonStructure json = new SingleJsonStructure();
		if(b==true){
			json.setMsg(BaseRest.UPDATE_SUCCESS);
		}else{
			json.setMsg(BaseRest.UPDATE_FAILURE);
		}
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月8日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param resId
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="deleteAll/{resId}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteAll(@PathVariable String resId){
		resRepository.delete(Integer.valueOf(resId));
		resRepository.flush();
		List<OrgRes> list = orgresRepository.findByResIdAll(Integer.valueOf(resId));
		for(int d=0;d<list.size();d++){
			orgresRepository.delete(list.get(d));
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(DELTE_SUCCESS);
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 孟话然 
	 * @date 2015年11月8日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param resId
	 * @param @return    
	 * @return ListJsonStructure<Integer>   
	 * @throws
	 */
	@RequestMapping(value="/findByResIdToOrg/{resId}",method = RequestMethod.GET)
	public ListJsonStructure<Integer> findByResIdToOrg(@PathVariable("resId")Integer resId){
		List<Integer> orgids = orgresRepository.findByResId(resId);
		ListJsonStructure<Integer> json = new ListJsonStructure<Integer>();		
		json.setRows(orgids);
		return json;
	}

	/**
	 * url：url：ynlxcloud/orgres/saveMore
	 * 函数功能说明:保存多个组织资源
	 * @author 张毕思 
	 * @date 2015年12月17日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param resids
	 * @param @param orgid
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value = "/saveMore",method = RequestMethod.POST)
	public SingleJsonStructure saveMore(@RequestParam("resids")String resids,@RequestParam("orgid") Integer orgid) {
		if(!resids.isEmpty()){
			String [] arrIds = resids.split(",");
			for(int i=0;i<arrIds.length;i++){
				OrgRes orgres=orgresRepository.findByResIdAndOrgid(Integer.valueOf(arrIds[i]), orgid);
				if(null==orgres){
					Res res = resRepository.findOne(Integer.valueOf(arrIds[i]));
					OrgRes or = new OrgRes();
					OrgResId id = new OrgResId();
					id.setOrgid(orgid);
					id.setResId(Integer.valueOf(arrIds[i]));
					or.setId(id);
					or.setNote(res.getNote());
					or.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
					orgresRepository.saveAndFlush(or);
				}
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.SAVE_SUCCESS);
		return json;
	}
	
}
