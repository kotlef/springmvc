/**       
 * @Title: UserloginRepository.java
 * @Package com.ynswet.system.sc.repository
 * @Description: TODO
 * @author 原勇
 * @date 2015年5月9日 下午3:29:23
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ynswet.system.sc.domain.Userlogin;

/**
 * 类功能说明
 * <p>Title: UserloginRepository.java</p>
 * @author 原勇
 * @date 2015年5月9日 下午3:29:23
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */

public interface UserloginRepository extends JpaRepository<Userlogin, String> {

	public List<Userlogin> findByUid(Integer uid);
	
}
