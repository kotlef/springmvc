package com.ynswet.system.sc.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.ynswet.system.sc.domain.Station;
import com.ynswet.system.sc.domain.StationR;


/**
 * 
 * 类功能说明
 * <p>Title: StationRRepository.java</p>
 * @author 张明坤
 * @date 2015年6月17日 上午11:49:09
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface StationRRepository extends JpaRepository <StationR, Integer> {

	@Query(value="Select myStation from Station myStation,StationR myStationR where myStation.orgid=myStationR.id.ROrgid and myStationR.id.orgid=?1")
	public List<Station> findStationByOrgid(Integer orgid);
	
	@Query(value="select sr from StationR sr where sr.id.orgid = ?1")
	public List<StationR> findByOrgId(Integer orgid);
}

