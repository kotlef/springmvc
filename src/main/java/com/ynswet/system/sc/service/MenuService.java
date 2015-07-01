package com.ynswet.system.sc.service;


/**
 * 
 * 菜单管理
 * <p>Title: MenuService.java</p>
 * @author 张明坤
 * @date 2015年5月23日 上午9:52:25
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface MenuService {
	
	/**
	 * 
	 * 批量删除菜单
	 * @author 张明坤 
	 * @date 2015年5月23日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param ids    
	 * @return void   
	 * @throws
	 */
	public void deleteMenus(String ids);
	
}
