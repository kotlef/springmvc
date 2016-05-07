/**       
 * @Title: StationRepository.java
 * @Package com.ynswet.homecloud.common.repository
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月15日 上午9:53:39
 * @version V1.0  
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ynswet.system.sc.domain.Station;


/**
 * 类功能说明:服务中心管理
 * <p>Title: StationRepository.java</p>
 * @author 李玉鹏
 * @date 2015年5月15日 上午9:53:39
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@Repository
public interface StationRepository extends JpaRepository<Station, Integer>{

	@Query(value="SELECT s FROM Station s WHERE concat(s.stationNo,s.stationName) like concat('%',?1,'%')")
	public Page<Station> findByStationInfoLike(String stationNo,Pageable pageable);

	@Query(value = "select res,s from Station s,Res res where res.resId in ?1 and s.orgid = res.itemId")
	List<Object[]> findByStationIdAndResId(Integer[] resIds);
	
	public List<Station> findByStatus(String status);
	
	public Station findByOrgid(Integer orgid);
}
