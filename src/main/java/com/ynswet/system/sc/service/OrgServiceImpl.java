/**       
 * @Title: OrgServiceImpl.java
 * @Package com.ynswet.homecloud.common.service
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:05:21
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.common.util.BeanUtils;
import com.ynswet.system.sc.domain.Org;
import com.ynswet.system.sc.domain.OrgRes;
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.domain.Station;
import com.ynswet.system.sc.repository.OrgRepository;
import com.ynswet.system.sc.repository.OrgResRepository;
import com.ynswet.system.sc.repository.ResRepository;
import com.ynswet.system.sc.repository.StationRepository;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

/**
 * 类功能说明：组织管理
 * <p>Title: OrgServiceImpl.java</p>
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:05:21
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class OrgServiceImpl implements OrgService{

	@Autowired
	private OrgRepository orgRepository;
	
	@Autowired
	private ResRepository resRepository;
	
	@Autowired
	private OrgResRepository orgresRepository;
	
	@Autowired
	private StationRepository  stationRepository;
	
	private EhCacheManager ehCacheManager;

	/* (non-Javadoc)    
	 * @see com.ynswet.homecloud.common.service.OrgService#deleteOrg(com.ynswet.homecloud.common.domain.Org)    
	 */
	@Transactional
	public void deleteOrg(Integer orgid) {
		int parentId;
		List<Org> orgAll = orgRepository.findAll();
		Org org = orgRepository.findOne(orgid);
		if(org != null){
			parentId = org.getParentId();
			List<Integer> orgids = getChildNodes(orgAll,orgid);
			for(Integer id:orgids){
				deleteOrgresAndRes(id);
				orgRepository.delete(id);
				orgRepository.flush();
			}
			deleteOrgresAndRes(orgid);
			orgRepository.delete(org);
			orgRepository.flush();
			Org parentOrg = orgRepository.findByOrgid(parentId);
			//判断是否有节点
			List<Org> childs = orgRepository.findByParentId(parentId);
			if(childs.size() < 1){
				parentOrg.setState("open");
				orgRepository.saveAndFlush(parentOrg);
			}
		}
	}
	//删除组织资源和资源
	private void deleteOrgresAndRes(int id){
		CacheManager cm=ehCacheManager.getCacheManager();
		Cache cache=cm.getCache("shiroAuthenticationCache");
		List<OrgRes> orgRess = orgresRepository.findOrgResByOrgid(id);
		Res res = resRepository.findByitemIdAndResType(id, "S");
		if(orgRess.size() > 0){
			orgresRepository.delete(orgRess);
			orgresRepository.flush();
			cache.removeAll();
		}
		if(null != res){
			resRepository.delete(res);
			resRepository.flush();
			cache.removeAll();
		}
	}
	@Transactional
	public List<Integer> getChildNodes(List<Org> list, Integer typeId) {
		returnList.clear();
		if(list != null&& typeId != null){
	        for (Iterator<Org> iterator = list.iterator(); iterator.hasNext();) {
	        	Org org = (Org) iterator.next();
	            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
	            if (org.getState().equals("closed") && typeId.equals(org.getOrgid())) {
	                recursionFn(list,org);
	            }
	        }
		}
        return returnList;
	}
	private List<Integer> returnList = new ArrayList<Integer>();
	private void recursionFn(List<Org> list, Org org) {
        List<Org> childList = getChildList(list, org);// 得到子节点列表
        Iterator<Org> it = childList.iterator();
        while (it.hasNext()) {
        	Org o = (Org) it.next();
        	returnList.add(o.getOrgid());
        	if(o.getState().equals("closed")){
        		 recursionFn(list, o);
        	}
        }
	}
    // 得到子节点列表
    private List<Org> getChildList(List<Org> list, Org org) {
        List<Org> orgList = new ArrayList<Org>();
        Iterator<Org> it = list.iterator();
        while (it.hasNext()) {
        	Org n = (Org) it.next();
            if (n.getParentId().equals(org.getOrgid())) {
            	orgList.add(n);
            }
        }
        return orgList;
    }
    
	@Transactional
	public boolean saveStationAndRes() {
		return false;
	}
	
	@Transactional
	public List<Map<String, Object>> searchOrgAndStationByParentId(
			int parentId) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		List<Org> listOrg = orgRepository.findByParentId(parentId);
		for(Org org : listOrg){
			Map<String, Object> map = BeanUtils.toMap(org);
			map.put("text", org.getOrgName());
			map.put("id", org.getOrgid());
			if(org.getStationFlag().equals("Y")){
				Station station = stationRepository.findOne(org.getOrgid());
				if(station != null){
					map.put("stationType", station.getStationType());
					map.put("stationDesc", station.getStationDesc());
					map.put("stationAdd", station.getStationAdd());
					map.put("stationTel", station.getStationTel());
					map.put("stationLeader", station.getStationLeader());
					map.put("leaderCell", station.getStationLeader());
					map.put("stationLevel", station.getStationLevel());
					map.put("stationArea", station.getStationArea());
					map.put("stationPCount", station.getStationPCount());
					map.put("stationSCount", station.getStationSCount());
					map.put("longitude", station.getLongitude());
					map.put("latitude", station.getLatitude());
				}
			}
			list.add(map);
		}
		return list;
	}
}
