package com.ynswet.system.sc.service;

import java.util.Collection;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.ynswet.common.domain.ListJsonStructure;


/**
 * 
 * 类功能说明：用户管理
 * <p>Title: UserService.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:32:25
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface UserService {
	
	public void deleteUser(Integer uid);
	/**
	 * 
	 * 函数功能说明:查询组织下的员工
	 * @author 张毕思 
	 * @date 2015年10月26日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param orgids
	 * @param @return    
	 * @return List<User>   
	 * @throws
	 */
	public ListJsonStructure<Map<String,Object>> findInOrgids(Collection<Integer> orgids,Pageable pageable);
	
	public ListJsonStructure<Map<String,Object>> searchByValue(Collection<Integer> orgids,String value,Pageable pageable);
}
