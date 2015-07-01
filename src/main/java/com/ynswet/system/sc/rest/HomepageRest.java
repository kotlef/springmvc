/**
 * @Title: MenuRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:41:29
 * @version V1.0  
 * <p>Copy
 * <p>Company:广州合光信息科技有限公司</p>
 */
package com.ynswet.system.sc.rest;

import java.sql.Timestamp;
import java.util.List;

import javax.management.MXBean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ynswet.common.domain.ListJsonStructure;
import com.ynswet.common.domain.SingleJsonStructure;
import com.ynswet.system.sc.domain.Homepage;
import com.ynswet.system.sc.domain.Menu;
import com.ynswet.system.sc.repository.HomepageRepository;
import com.ynswet.system.sc.repository.MenuRepository;
import com.ynswet.system.sc.service.MenuService;

/**
 *
 * 菜单Restful
 * <p>
 * Title: MenuRest.java
 * </p>
 *
 * @author 张明坤
 * @date 2015年5月22日 下午10:40:51 类修改者 修改日期 修改说明
 * @version V1.0
 *          <p>
 *          Description:立翔云
 *          </p>
 *          <p>
 *          Copyright: Copyright (c) 2015
 *          </p>
 *          <p>
 *          Company:广州合光信息科技有限公司
 *          </p>
 */
@RestController
@RequestMapping("/homepage")
public class HomepageRest {

	@Autowired
	private HomepageRepository homepageRepository;

	/**
	 *
	 * 函数功能说明
	 *
	 * @author 原勇
	 * @date 2015年6月18日 修改者名字 修改日期 修改内容
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public List<Homepage> findAll() {

		List<Homepage> homepageList = homepageRepository.findAll();

		return homepageList;
	}

	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public Homepage findById(@PathVariable Integer id) {

		return homepageRepository.getOne(id);
	}

}
