
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
import com.ynswet.system.sc.domain.Station;
import com.ynswet.system.sc.domain.StationR;
import com.ynswet.system.sc.domain.StationRId;
import com.ynswet.system.sc.repository.StationRRepository;


/**
 * 
 * 类功能说明
 * <p>Title: StationRRest.java</p>
 * @author 张明坤
 * @date 2015年6月17日 上午11:51:15
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/stationR")
public class StationRRest extends BaseRest{
	
	
	@Autowired
	private StationRRepository stationRRepository;
	
	/**
	 * url:/ynlxmainarchive/stationR
	 * 函数功能说明:保存操作
	 * @author 张明坤 
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param stationR
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure saveStationR(
			@ModelAttribute StationR stationR,
			@ModelAttribute StationRId stationRId,
			@RequestParam("orgid")Integer orgid,
			@RequestParam("rOrgids")String rOrgids){
		SingleJsonStructure json = new SingleJsonStructure();
		stationRId.setOrgid(orgid);
		stationR.setStationType("0");
		stationR.setId(stationRId);
		//删除关联服务站
		List<StationR> stationRs = stationRRepository.findByOrgId(orgid);
		if(!stationRs.isEmpty()){
			for(int b=0;b<stationRs.size();b++){
				stationRRepository.delete(stationRs.get(b));
			}
		}	
		//添加服务站
		if(!rOrgids.isEmpty()){
			String[] arrIds = rOrgids.split(",");
			for(int a=0;a<arrIds.length;a++){
				stationRId.setROrgid(Integer.valueOf(arrIds[a]));
				stationR.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
				stationRRepository.saveAndFlush(stationR);
			}
		}
		json.setMsg("更新成功！");
		return json;
	}

	/**
	 * url:/ynlxmainarchive/stationR
	 * 函数功能说明：查询所有记录
	 * @author 张明坤 
	 * @date 2015年5月13日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return ListJsonStructure<StationR>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ListJsonStructure<StationR> findAll(){
		ListJsonStructure<StationR> json = new ListJsonStructure<StationR>();
		List<StationR> stationRList= stationRRepository.findAll();
		json.setRows(stationRList);
		json.setMsg("查询成功！");
		return json;
	}
	
	/**
	 * url:/ynlxmainarchive/stationR/toPage
	 * 函数功能说明:分页查询
	 * @author 张明坤 
	 * @date 2015年5月23日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return ListJsonStructure<StationR>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<StationR> findAlltoPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		Pageable pageable = new PageRequest(page-1, size);
		Page<StationR> pages = stationRRepository.findAll(pageable);
		ListJsonStructure<StationR> json = new ListJsonStructure<StationR>();
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}
	
	/**
	 * 
	 * 函数功能说明
	 * @author 张明坤 
	 * @date 2015年6月17日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgid
	 * @param @return    
	 * @return List<Station>   
	 * @throws
	 */
	@RequestMapping(value="/searchByOrgid/{orgid}",method = RequestMethod.GET)
	public List<Station> findByDId(@PathVariable Integer orgid) {
		List<Station> list= stationRRepository.findStationByOrgid(orgid);
		return list;
	}
}
