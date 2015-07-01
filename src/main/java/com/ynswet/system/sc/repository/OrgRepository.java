/**       
 * @Title: OrgRepository.java
 * @Package com.ynswet.homecloud.common.repository
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:15:16
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.repository;

import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynswet.system.sc.domain.Org;

/**
 * 类功能说明：组织管理
 * <p>Title: OrgRepository.java</p>
 * @author 李玉鹏
 * @date 2015年5月14日 下午4:15:16
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface OrgRepository extends JpaRepository<Org, Integer>{

	public List<Org> findByOrgidIn(Collection<Integer> orgIds);
}
