/**       
 * @Title: StationRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月21日 下午10:52:31
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.rest;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
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
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.domain.Station;
import com.ynswet.system.sc.repository.StationRepository;
import com.ynswet.system.sc.service.StationService;

/**
 * 类功能说明:服务中心管理
 * <p>Title: StationRest.java</p>
 * @author 李玉鹏
 * @date 2015年5月21日 下午10:52:31
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/station")
public class StationRest extends BaseRest{
	
	@Autowired
	private StationService stationService;
	
	@Autowired
	private StationRepository stationRepository;
	
	/**
	 * url:/ynlxmainarchive/station
	 * 函数功能说明:保存服务中心
	 * @author 李玉鹏 
	 * @date 2015年5月21日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param station
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure saveStation(@ModelAttribute Station station) {
		station.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
		station.setModifyTime(DateTimeUtils.getSystemCurrentTimeMillis());
		stationRepository.saveAndFlush(station);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("保存成功！");
		return json;
	}
	/**
	 * url:/ynlxmainarchive/station/{stationId}
	 * 函数功能说明：修改服务中心信息
	 * @author 李玉鹏 
	 * @date 2015年5月21日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param station
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{stationId}",method = RequestMethod.POST)
	public SingleJsonStructure updateStation(@PathVariable Integer stationId,@ModelAttribute Station station) {
		Station oldStation = stationRepository.findOne(stationId);
		Timestamp modifyTime = new Timestamp(System.currentTimeMillis());
		station.setModifyTime(modifyTime);
		station.setCreateTime(oldStation.getCreateTime());
		stationRepository.saveAndFlush(station);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("更新成功！");
		return json;
	}
	/**
	 * url:/ynlxmainarchive/station/{stationIds}
	 * 函数功能说明:删除服务中心
	 * @author 李玉鹏 
	 * @date 2015年5月21日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param stationIds
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{stationIds}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteStation(@PathVariable String stationIds) {
		if(!stationIds.isEmpty()){
			String [] arrStationIds = stationIds.split(",");
			for(String stationId : arrStationIds){
				stationService.deleteStation(Integer.valueOf(stationId));
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("删除成功！");
		return json;
	}
	/**
	 * url:/ynlxmainarchive/station
	 * 函数功能说明:查询所有的服务中心
	 * @author 李玉鹏 
	 * @date 2015年5月21日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return ListJsonStructure<Station>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Station> findAll() {
		ListJsonStructure<Station> json = new ListJsonStructure<Station>();
		List<Station> list = stationRepository.findAll();
		json.setMsg("查询成功！");
		json.setRows(list);
		return list;
	}
	/**
	 *url:/ynlxmainarchive/station/searchByStatus
	 * 函数功能说明:根据状态查询所有的服务中心
	 * @author 张毕思 
	 * @date 2015年12月18日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return List<Station>   
	 * @throws
	 */
	@RequestMapping(value="/searchByStatus",method = RequestMethod.GET)
	public List<Station> searchByStatus(@RequestParam("status")String status) {
		ListJsonStructure<Station> json = new ListJsonStructure<Station>();
		List<Station> list = stationRepository.findByStatus(status);
		json.setMsg("查询成功！");
		json.setRows(list);
		return list;
	}
	
	/**
	 * url:/ynlxmainarchive/station/toPage
	 * 函数功能说明：分页查询服务中心记录
	 * @author 李玉鹏 
	 * @date 2015年5月21日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return ListJsonStructure<Station>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<Station> findAlltoPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		Sort sort = new Sort(Direction.DESC,"createTime");
		Pageable pageable = new PageRequest(page-1, size,sort);
		Page<Station> pages = stationRepository.findAll(pageable);
		ListJsonStructure<Station> json = new ListJsonStructure<Station>();
		json.setMsg("查询成功！");
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}
	/**
	 * url:/ynlxcloud/role/searchByKeyValueLike/params
	 * 搜索服务站功能
	 * @author 孟话然
	 * @date 2015年8月4日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key value
	 * @param @return   
	 * @return ListJsonStructure<Station>   
	 * @throws
	 */
	@RequestMapping(value="/searchByKeyValueLike/params",method = RequestMethod.GET)
	public ListJsonStructure<Station> searchByKeyValueLikeToPage(
			@RequestParam("value")String value,
			@RequestParam("page")int page,
			@RequestParam("rows")int rows
			) {
		Page<Station> pages = null ;
		Sort sort = new Sort(Direction.DESC,"createTime");
		Pageable pageable = new PageRequest(page-1, rows,sort);
		pages = stationRepository.findByStationInfoLike(value,pageable);
		ListJsonStructure<Station> json = new ListJsonStructure<Station>();
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}

	/**
	 * url:/ynlxmainarchive/community/findOrgResByResType
	 * 函数功能说明：我的资料界面，根据组织id和资源类型查询资源
	 * @author 孙越
	 * @date 2015年10月10日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param districtId
	 * @param @return   
	 * @return List<Community>   
	 * @throws
	 */
	@RequestMapping(value="/findOrgResByResType",method = RequestMethod.GET)
	public List<Map<String, Object>> findOrgResByResType(@RequestParam("resIds[]") Integer[] resIds) {
		List<Object[]> results = stationRepository.findByStationIdAndResId(resIds);
		List<Map<String,Object>> list = new ArrayList<Map<String,Object>>();
		for (Object[] result : results) {
			Res res = (Res) result[0];
			Station station = (Station) result[1];
			Map<String, Object> resMap = BeanUtils.toMap(res);
			resMap.put("resName", station.getStationName());
			list.add(resMap);
		}
		return list;
	}
}
