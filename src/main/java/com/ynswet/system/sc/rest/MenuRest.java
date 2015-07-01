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
import com.ynswet.system.sc.domain.Menu;
import com.ynswet.system.sc.repository.MenuRepository;
import com.ynswet.system.sc.service.MenuService;

/**
 *
 * 菜单Restful
 * <p>Title: MenuRest.java</p>
 * @author 张明坤
 * @date 2015年5月22日 下午10:40:51
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/menu")
public class MenuRest {



	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private MenuService menuService;

	/**
	 *
	 * 保存菜单
	 * @author 张明坤
	 * @date 2015年5月22日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param menu
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.POST)
	public SingleJsonStructure save(@ModelAttribute Menu menu){
		System.out.println(menu.toString());
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SingleJsonStructure json = new SingleJsonStructure();
		menu.setCreateTime(timestamp);
		menuRepository.saveAndFlush(menu);
		json.setMsg("添加成功！");
		return json;
	}
	/**
	 *
	 * 修改菜单
	 * @author 张明坤
	 * @date 2015年5月22日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param menu
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(value="/{id}",method = RequestMethod.POST)
	public SingleJsonStructure updateMenu(@ModelAttribute Menu menu,@PathVariable Integer id) {
		// TODO Auto-generated method stub
		SingleJsonStructure json = new SingleJsonStructure();
		Menu oldMenu=menuRepository.findOne(id);
		menu.setCreateTime(oldMenu.getCreateTime());
		menuRepository.saveAndFlush(menu);
		json.setMsg("更新成功!");
		return json;
	}
	/**
	 *
	 * 删除菜单
	 * @author 张明坤
	 * @date 2015年5月22日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param uid
	 * @param @return   
	 * @return SingleJsonStructure   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.DELETE)
	private SingleJsonStructure deleteMenu(@RequestParam String ids) {
		// TODO Auto-generated method stub
		SingleJsonStructure json = new SingleJsonStructure();
		menuService.deleteMenus(ids);
		json.setMsg("删除成功！");
		return json;
	}

	/**
	 *
	 * 获取菜单列表
	 * @author 张明坤
	 * @date 2015年5月22日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ListJsonStructure<Menu> findAll(){
		ListJsonStructure<Menu> json = new ListJsonStructure<Menu>();
		List<Menu> menuList= menuRepository.findAll();
		json.setRows(menuList);
		json.setTotal(menuList.size());
		return json;
	}

	/**
	 *
	 * 函数功能说明
	 * @author 张明坤
	 * @date 2015年5月27日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param page
	 * @param @param size
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(value="/toPage",method = RequestMethod.GET)
	public ListJsonStructure<Menu> findAllToPage(
			@RequestParam("page")int page,
			@RequestParam("rows")int size) {
		ListJsonStructure<Menu> json = new ListJsonStructure<Menu>();
		page--;
		Pageable pageable = new PageRequest(page, size);
		Page<Menu> menuList=menuRepository.findAll(pageable);
		json.setRows(menuList.getContent());
		json.setTotal((int)menuList.getTotalElements());
		return json;
	}

	/**
	 *
	 * 根据父节点ID查询子节点
	 * @author 张明坤
	 * @date 2015年5月26日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param parentId
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(value="/serchByParentId",method = RequestMethod.POST)
	public List<Menu> findMenusByParentId(@ModelAttribute("id") String id) {
		int parentId=0;
		if(null!=id&&!id.isEmpty()){
			parentId=Integer.parseInt(id);
		}
		ListJsonStructure<Menu> json = new ListJsonStructure<Menu>();
		List<Menu> menuList=menuRepository.findByParentId(parentId);
		json.setRows(menuList);
		json.setTotal(menuList.size());
		return menuList;
	}

}
