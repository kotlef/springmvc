/**       
 * @Title: ResRest.java
 * @Package com.ynswet.system.sc.rest
 * @Description: TODO
 * @author 孙越
 * @date 2015年8月5日 下午4:57:40
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
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.repository.ResRepository;
import com.ynswet.system.sc.service.ResService;

/**
 * 类功能说明:资源管理
 * <p>Title: ResRest.java</p>
 * @author 孙越
 * @date 2015年8月5日 下午4:57:40
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/res")
public class ResRest {
	@Autowired
	private ResService resService;
	
	@Autowired
	private ResRepository resRepository;
	/**
	 * url:/ynlxcloud/res/
	 * 函数功能说明:添加资源
	 * @author 孙越 
	 * @date 2015年8月5日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param res
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/saveRes",method = RequestMethod.POST)
	public SingleJsonStructure save(@ModelAttribute Res res){
		res.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
		SingleJsonStructure json = new SingleJsonStructure();
		resRepository.saveAndFlush(res);
		json.setMsg(BaseRest.SAVE_SUCCESS);
		return json;
	}
	/**
	 * url:ynlxcloud/res/{resId}
	 * 函数功能说明:更新资源
	 * @author 孙越 
	 * @date 2015年8月5日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param cid
	 * @param @param Res
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{resId}",method = RequestMethod.POST)
	public SingleJsonStructure updateRes(@PathVariable Integer resId,@ModelAttribute Res res) {
		Res oldRes =resRepository.findOne(resId);
		System.out.println("简历时间"+oldRes.getCreateTime());
		res.setCreateTime(oldRes.getCreateTime());
		resRepository.saveAndFlush(res);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.UPDATE_SUCCESS);
		return json;
	}
	
	/**
	 * url:/ynlxcloud/res/{resIds}
	 * 函数功能说明:删除资源
	 * @author 孙越 
	 * @date 2015年8月5日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param resIds
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{resIds}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteRes(@PathVariable String resIds) {
		if(!resIds.isEmpty()){
			String [] arrCids = resIds.split(",");
			for(String cid : arrCids){
				resService.deleteRes(Integer.valueOf(cid));
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg(BaseRest.DELTE_SUCCESS);
		return json;
	}
	/**
	 * url:/ynlxcloud/res/
	 * 函数功能说明：查询所有的资源
	 * @author 孙越 
	 * @date 2015年8月5日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return    
	 * @return ListJsonStructure<Res>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ListJsonStructure<Res> findAll(){
		ListJsonStructure<Res> json = new ListJsonStructure<Res>();
		List<Res> list= resRepository.findAll();
		json.setRows(list);
		json.setMsg(BaseRest.FIND_SUCCESS);
		return json;
	}
	
	/**
	 *url:/ynlxcloud/res/toPage
	 * 函数功能说明:分页查询
	 * @author 孙越 
	 * @date 2015年8月5日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param size
	 * @param @return    
	 * @return ListJsonStructure<Res>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<Res> findAlltoPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size
			){
		Pageable pageable = new PageRequest(page-1, size); 
		Page<Res> pages = resRepository.findAll(pageable);
		ListJsonStructure<Res> json = new ListJsonStructure<Res>();
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}
	/**
	 * url:/ynlxcloud/searchByRtype/toPage
	 * 函数功能说明:按条件查询资源
	 * @author 孙越 
	 * @date 2015年8月11日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param resType
	 * @param @return    
	 * @return List<Res>   
	 * @throws
	 */
	@RequestMapping(value="/searchByRtype/toPage",method = RequestMethod.GET)
	public ListJsonStructure<Res> findByDId(
		@RequestParam("value")String value,
		@RequestParam("page")int page,
		@RequestParam("rows")int size
		) {
		Page<Res> list= null ;
		Pageable pageable = new PageRequest(page-1, size); 
		list =resRepository.findByResTypeLike(value+"%",pageable);
		ListJsonStructure<Res> json = new ListJsonStructure<Res>();
		if(list==null){
			json.setSuccess(false);
		}
		json.setRows(list.getContent());
		json.setTotal((int)list.getTotalElements());
		json.setMsg(BaseRest.FIND_SUCCESS);
		return json;
	}
	
	@RequestMapping(value="/checkRes/{res}",method = RequestMethod.GET)
	public boolean checkRes(@PathVariable("res")String res) {
		String [] arrRes = res.split(",");
		String resType=arrRes[0];
		int itemId=Integer.parseInt(arrRes[1]);
		List<Res> list= null ;
		list =resRepository.findOnlyOne(resType,itemId);
		if(list.size()>=1){
			return false;
		}else{
			return true;
		}

	}
}
