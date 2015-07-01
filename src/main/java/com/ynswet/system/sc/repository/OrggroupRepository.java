package com.ynswet.system.sc.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ynswet.system.sc.domain.Orggroup;

/**
 * 
 * 类功能说明：机构管理
 * <p>Title: OrggroupRepository.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:55:34
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface OrggroupRepository extends JpaRepository <Orggroup, Integer> {

	
}

