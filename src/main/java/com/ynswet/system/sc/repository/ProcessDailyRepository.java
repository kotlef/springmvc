/**
 * @Title: ProcessDailyRepository.java
 * @Package com.ynswet.homecloud.common.repository
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月14日 下午3:28:16
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
package com.ynswet.system.sc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynswet.system.sc.domain.ProcessDaily;

/**
 * 类功能说明：日结进程表
 * <p>Title: ProcessDailyRepository.java</p>
 * @author 李玉鹏
 * @date 2015年5月14日 下午3:28:16
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface ProcessDailyRepository extends JpaRepository<ProcessDaily, Integer>{

}
