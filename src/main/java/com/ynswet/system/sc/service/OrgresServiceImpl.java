/**       
 * @Title: OrgresServiceImpl.java
 * @Package com.ynswet.system.sc.service
 * @Description: TODO
 * @author 孙越
 * @date 2015年8月10日 下午2:12:36
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.service;


import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ynswet.common.util.DateTimeUtils;
import com.ynswet.system.sc.domain.OrgRes;
import com.ynswet.system.sc.domain.OrgResId;
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.realm.UserManager;
import com.ynswet.system.sc.repository.OrgResRepository;
import com.ynswet.system.sc.repository.PostRepository;
import com.ynswet.system.sc.repository.ResRepository;

/**
 * 类功能说明
 * <p>Title: OrgresServiceImpl.java</p>
 * @author 孙越
 * @date 2015年8月10日 下午2:12:36
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class OrgresServiceImpl implements OrgresService{
	@Autowired
	private OrgResRepository orgresRepository;
	@Autowired
	private PostRepository postRepository;
	@Autowired
	private UserManager userManager;
	@Autowired
	private ResRepository resRepository;
	

	/* (non-Javadoc)    
	 * @see com.ynswet.system.sc.service.OrgresService#deleteByOidAndRid(int, int)    
	 */
	@org.springframework.transaction.annotation.Transactional
	public void deleteByOidAndRid(int oid, int rid) {
		// TODO Auto-generated method stub
		orgresRepository.deleteByOidAndRid(oid, rid);
	}
	
	@Transactional
	public boolean saveOrUpdateRes(
			Integer ajag,
			Res res, OrgRes orgRes, OrgResId orgResId, String orgids){
		Res r;
		if(ajag==1){
			res.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
			r = resRepository.saveAndFlush(res);
		}else{
			Integer resId = res.getResId();
			Res oldr = resRepository.findOne(resId);
			res.setResId(oldr.getResId());
			res.setCreateTime(oldr.getCreateTime());
			r = resRepository.saveAndFlush(res);
			List<OrgRes> list = orgresRepository.findByResIdAll(resId);
			for(int d=0;d<list.size();d++){
				orgresRepository.delete(list.get(d));
			}
		}
		if(!orgids.isEmpty()){
			String[] orgIds = orgids.split(",");
			for(int b=0;b<orgIds.length;b++){
				orgResId.setResId(r.getResId());
				orgResId.setOrgid(Integer.valueOf(orgIds[b]));
				orgRes.setId(orgResId);
				orgRes.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
				orgresRepository.saveAndFlush(orgRes);
			}
		}
		return true;
	}

	@Transactional
	public boolean saveOrgresAndRes(int orgid, String note,
			Date createTime, String resType,int itemId) {
		boolean msg = false;
		int resId;
		Res res=resRepository.findByitemIdAndResType(itemId, "C");
		if(null == res){
			Res r = new Res();
			r.setCreateTime(createTime);
			r.setItemId(itemId);
			r.setNote(note);
			r.setResType(resType);
			Res rs = resRepository.saveAndFlush(r);
			resId=rs.getResId();
		}else{
			resId=res.getResId();
		}
		OrgRes ors = orgresRepository.findByOrgidAndItemId(orgid, itemId);
		if(null == ors){
			OrgRes orgres = new OrgRes();
			OrgResId id = new OrgResId();
			id.setOrgid(orgid);
			id.setResId(resId);
			orgres.setId(id);
			orgres.setNote(note);
			orgres.setCreateTime(createTime);
			orgresRepository.saveAndFlush(orgres);
		}
		msg = true;
		return msg;
	}

	@Transactional
	public boolean updateOrgresAndRes(int oldOrgid, int orgid, int communityId,String note) {
		boolean msg = false;

		return msg;
	}
}
