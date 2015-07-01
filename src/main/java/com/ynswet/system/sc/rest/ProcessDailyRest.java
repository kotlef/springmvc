/**
 * @Title: ProcessDailyRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月14日 下午3:39:04
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
import com.ynswet.system.sc.domain.ProcessDaily;
import com.ynswet.system.sc.repository.ProcessDailyRepository;
import com.ynswet.system.sc.service.ProcessDailyService;

/**
 * 类功能说明：日结进程管理
 * <p>Title: ProcessDailyRest.java</p>
 * @author 李玉鹏
 * @date 2015年5月14日 下午3:39:04
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/processDaily")
public class ProcessDailyRest extends BaseRest{

	@Autowired
	private ProcessDailyService processDailyService;

	@Autowired
	private ProcessDailyRepository processDailyRepository;

	/**
	 * url:/ynlxmainarchive/processDaily
	 * 函数功能说明:保存日结进程记录
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param processDaily
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure saveProcessDaily(@ModelAttribute ProcessDaily processDaily) {
		processDaily.setCreateTime(DateTimeUtils.getSystemCurrentTimeMillis());
		processDailyRepository.saveAndFlush(processDaily);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("保存成功！");
		return json;

	}
	/**
	 * url:/ynlxmainarchive/processDaily/{executeId}
	 * 函数功能说明:修改日结进程记录
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param executeId
	 * @param @param processDaily
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{executeId}",method = RequestMethod.POST)
	public SingleJsonStructure updateProcessDaily(@PathVariable Integer executeId,@ModelAttribute ProcessDaily processDaily) {
		ProcessDaily oldProcessDaily = processDailyRepository.findOne(executeId);
		processDaily.setCreateTime(oldProcessDaily.getCreateTime());
		processDailyRepository.saveAndFlush(processDaily);
		SingleJsonStructure json = new SingleJsonStructure();
		json.setMsg("修改成功！");
		return json;
	}

	/**
	 * url:/ynlxmainarchive/processDaily/{executeIds}
	 * 函数功能说明:删除日结进程记录
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param executeIds
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{executeIds}",method = RequestMethod.DELETE)
	public SingleJsonStructure deleteProcessDaily(@PathVariable String executeIds) {
		if(!executeIds.isEmpty()){
			String [] arrExecuteIds = executeIds.split(",");
			for(String executeId : arrExecuteIds){
				processDailyService.deleteProcessDaily(Integer.valueOf(executeId));
			}
		}
		SingleJsonStructure json = new SingleJsonStructure();
		return json;
	}

	/**
	 * url:/ynlxmainarchive/processDaily
	 * 函数功能说明:查询所欲的日结进程记录
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return   
	 * @return ListJsonStructure<ProcessDaily>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ListJsonStructure<ProcessDaily> findAll() {
		List<ProcessDaily> list = processDailyRepository.findAll();
		ListJsonStructure<ProcessDaily> json = new ListJsonStructure<ProcessDaily>();
		json.setRows(list);
		return json;
	}
	/**
	 * url:/ynlxmainarchive/processDaily/toPage
	 * 函数功能说明:分页查询所有的日结进程记录
	 * @author 李玉鹏
	 * @date 2015年5月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param size
	 * @param @return   
	 * @return ListJsonStructure<ProcessDaily>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<ProcessDaily> findAlltoPages(
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		Pageable pageable = new PageRequest(page-1, size);
		Page<ProcessDaily> pages = processDailyRepository.findAll(pageable);
		ListJsonStructure<ProcessDaily> json = new ListJsonStructure<ProcessDaily>();
		json.setRows(pages.getContent());
		json.setTotal((int) pages.getTotalElements());
		return json;
	}

}
