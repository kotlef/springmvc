
package com.ynswet.system.sc.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.system.sc.repository.MenuRepository;

/**
 * 
 * 类功能说明
 * <p>Title: MenuServiceImpl.java</p>
 * @author 张明坤
 * @date 2015年5月23日 上午9:56:05
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Service
public class MenuServiceImpl implements MenuService{

	@Autowired
	private MenuRepository menuRepository;


	/* 
	 * (non-Javadoc)    
	 * @see com.ynswet.system.sc.service.MenuService#deleteMenus(java.lang.String)
	 */
	@Transactional
	public void deleteMenus(String ids) {
		// TODO Auto-generated method stub
		String[] idsArr=ids.split(",");
		for(int i=0;i<idsArr.length;i++){
			menuRepository.delete(Integer.parseInt(idsArr[i]));
		}
	}
	
	

}
