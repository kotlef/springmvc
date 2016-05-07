package com.ynswet.system.sc.service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.system.sc.domain.Orggroup;
import com.ynswet.system.sc.repository.OrggroupRepository;

/**
 * 
 * 类功能说明：机构管理
 * <p>Title: UserServiceImpl.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:32:05
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class OrggroupServiceImpl implements OrggroupService {
	
	
	@Autowired
	private OrggroupRepository orggroupRepository;

	/*
	 * (non-Javadoc)    
	 * @see com.ynswet.homecloud.common.service.OrggroupService#deleteOrggroup(java.lang.Integer)
	 */
	@Transactional
	public void deleteOrggroup(Integer orggroupId) {
		int parentId;
		List<Orggroup> list = orggroupRepository.findAll();
		Orggroup orggroup = orggroupRepository.findByorggroupId(orggroupId);
		if(orggroup!=null){
			parentId =orggroup.getParentId();
			List<Integer> childs = getChildNodes(list,orggroupId);
			childs.add(orggroupId);
			orggroupRepository.delete(childs);
			orggroupRepository.flush();
			
/*			for(Integer id:childs){
				orggroupRepository.delete(id);
				orggroupRepository.flush();
			}
			orggroupRepository.delete(orggroup);
			orggroupRepository.flush();*/
			List<Orggroup> nodeList = orggroupRepository.findByParentId(parentId);
			if(nodeList.size()==0){
				Orggroup parentOrggroup = orggroupRepository.findByorggroupId(parentId);
				parentOrggroup.setState("open");
				orggroupRepository.saveAndFlush(parentOrggroup);
			}
		}
	}
	
	private List<Integer> returnList = new ArrayList<Integer>();
	private List<Integer> getChildNodes(List<Orggroup> list, Integer typeId) {
		returnList.clear();
		if(list != null&& typeId != null){
	        for (Iterator<Orggroup> iterator = list.iterator(); iterator.hasNext();) {
	        	Orggroup orggroup = (Orggroup) iterator.next();
	            // 一、根据传入的某个父节点ID,遍历该父节点下的所有子节点
	            if (orggroup.getState().equals("closed") && orggroup.getOrggroupId().equals(typeId)) {
	                recursionFn(list,orggroup);
	            }
	        }
		}
        return returnList;
	}
	private void recursionFn(List<Orggroup> list, Orggroup orggroup) {
	        List<Orggroup> childList = getChildList(list, orggroup);// 得到子节点列表
	        for(int i = 0; i < childList.size();i++){
	        	returnList.add(childList.get(i).getOrggroupId());
	        	if(childList.get(i).getState().equals("closed")){
	        		recursionFn(list,childList.get(i));
	        	}
	        }
	    }
	    // 得到子节点列表
	    private List<Orggroup> getChildList(List<Orggroup> list, Orggroup orggroup) {
	    	List<Orggroup> orgList = new ArrayList<Orggroup>();
	        Iterator<Orggroup> it = list.iterator();
	        while (it.hasNext()) {
	        	Orggroup n = (Orggroup) it.next();
	            if (n.getParentId().equals(orggroup.getOrggroupId())) {
	            	orgList.add(n);
	            }
	        }
	        return orgList;
	    }
}
