package com.ynswet.system.sc.service;

import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Pageable;

import com.ynswet.common.domain.ListJsonStructure;

/**
 * 
 * 类功能说明：员工管理
 * <p>Title: EmpService.java</p>
 * @author 李玉鹏
 * @date 2015年5月13日 上午11:29:16
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public interface EmpService {
	
	public void deleteEmp(Integer uid);
	public List<Map<String,Object>> searchEmpInOrgids(Integer[] orgids);
	public void saveOperate(String userName,String cell,String userType,String idType,String idNo,
			String email,String cell2,String marriage,String blood,String gender,String status,
			String education,String empCode,String empPassword,String postRank,String roleIds,Integer orgid,
			String note);
	public void updateOperate(Integer uid,String userName,String status,String roleCode,Integer orgid,String note,String empCode,String empPassword);
	public boolean delete(Integer[] uids);
	public List<Map<String,Object>> empDetil( Pageable pageable);
	public ListJsonStructure<Map<String,Object>> searchByValue(String value,Pageable pageable);
}
