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

import com.ynswet.common.rest.BaseRest;
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
public class MenuRest extends BaseRest {
	
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
		//获取并设置创建时间
		Timestamp timestamp = new Timestamp(System.currentTimeMillis());
		SingleJsonStructure json = new SingleJsonStructure();
		menu.setCreateTime(timestamp);
		//改变新父菜单状态为closed
		Integer parentId=menu.getParentId();
		Menu pmenu=menuRepository.findByMenuId(parentId);
		if(pmenu!=null){
			pmenu.setState("closed");
			pmenu.setMenuUrl(null);
			menuRepository.saveAndFlush(pmenu);
		}
		//保存menu
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
		//改变新父菜单状态为closed
		Integer parentId=menu.getParentId();
		Menu pmenu=menuRepository.findByMenuId(parentId);
		if(pmenu!=null){
			pmenu.setState("closed");
			pmenu.setMenuUrl(null);
			menuRepository.saveAndFlush(pmenu);
		}
		//查看老菜单及老父菜单
		Menu omenu=menuRepository.findByMenuId(id);
		Menu opmenu=menuRepository.findByMenuId(omenu.getParentId());
		//查看新菜单有无子菜单，有就保持状态为closed
		List<Menu> ocmenu=menuRepository.findByParentId(id);
		if(!ocmenu.isEmpty()){
			menu.setState("closed");
		}
		//更新menu
		menu.setCreateTime(omenu.getCreateTime());
		menuRepository.saveAndFlush(menu);
		json.setMsg("更新成功!");
		//修改之后老父菜单是否为空，为空设置state为open
		if(opmenu!=null){
			List<Menu> opmenuList=menuRepository.findByParentId(opmenu.getMenuId());
			if(opmenuList.isEmpty()){
				opmenu.setState("open");
				menuRepository.saveAndFlush(opmenu);
			}	
		}
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
	@RequestMapping(value="/{menuIds}",method = RequestMethod.DELETE)
	private SingleJsonStructure deleteMenu(@PathVariable("menuIds") Integer id) {
		// TODO Auto-generated method stub
		SingleJsonStructure json = new SingleJsonStructure();
		if(id!=null){
				//查看菜单
				Menu omenu=menuRepository.findByMenuId(id);
				//查看老父菜单
				Menu opmenu=menuRepository.findByMenuId(omenu.getParentId());
				//查看菜单子菜单，级联删除子菜单，判断有无三级菜单
				List<Menu> cmenu=menuRepository.findByParentId(id);	
					if(cmenu.isEmpty()){//没有子菜单，直接删除
						String seid=id.toString();
						menuService.deleteMenus(seid);
					}else{//有子菜单，循环删除子菜单
						for(int n = 0;n<cmenu.size();n++){
							Integer cid=cmenu.get(n).getMenuId();
							List<Menu> ccmenu=menuRepository.findByParentId(cid);
							if(ccmenu.isEmpty()){//没有第三级菜单，删除
								String scid=cid.toString();
								menuService.deleteMenus(scid);
							}else{//如果有第三层，不允许删除
								json.setMsg("请先删除底部数据");
								return json;
							}
						}
						String seid=id.toString();
						menuService.deleteMenus(seid);
					}	
				//查看老父菜单是否有子孙菜单，没有，设置状态为open
				if(opmenu!=null){
					List<Menu> list=menuRepository.findByParentId(opmenu.getMenuId());
					if(list.isEmpty()){
						opmenu.setState("open");
						menuRepository.saveAndFlush(opmenu);
					}
				}
			}	
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
	@RequestMapping(value="/serchByParentId",method = RequestMethod.GET)
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
	
	/**
	 *
	 * 搜索菜单功能
	 * @author 孟话然
	 * @date 2015年7月16日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param key value
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(value="/searchBymenuName/params",method = RequestMethod.GET)
	public List<Menu> findMenuBymenuNameLike(
			@RequestParam("value")String value
			) {
		List<Menu> menus = null ;
		menus = menuRepository.findByMenuNameLike(value);
		return menus;
	}
	
	/**
	 * url:/ynlxhealth/menu/searchByRoleIds
	 * 根据角色内码查询当前用户的菜单
	 * @author 李玉鹏 
	 * @date 2015年8月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param roleIds[]
	 * @param @return    
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(value="/searchByRoleIds",method = RequestMethod.GET)
	public ListJsonStructure<Menu> findMenuByRoleIds(@RequestParam(value = "roleIds[]") Integer[] roleIds) {
		List<Menu> menus = menuRepository.findMenuByRoleIds(roleIds,"0");
		ListJsonStructure<Menu> json = new ListJsonStructure<Menu>();
		json.setMsg(BaseRest.FIND_SUCCESS);
		json.setRows(menus);
		return json;
	}

	/**
	 * url:/ynlxhealth/menu/searchByRoleIds
	 * 根据角色内码查询当前用户的菜单
	 * @author 李玉鹏
	 * @date 2015年8月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param roleIds[]
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(value="/searchByRoleIds/toTree",method = RequestMethod.GET)
	public List<Menu> findMenuTreeByRoleIds(
			@RequestParam(value = "roleIds[]") Integer[] roleIds,
			@ModelAttribute("id") String id) {
		int parentId=0;
		if(null!=id&&!id.isEmpty()){
			parentId=Integer.parseInt(id);
			List<Menu> menuList=menuRepository.findByParentIdAndRoleIds(parentId,roleIds);
			return menuList;
		}else{
			List<Menu> menus = menuRepository.findMenuByRoleIds(roleIds,"0");
			return menus;
		}
	}

	/**
	 * url:/ynlxhealth/menu/serchByMenuId
	 * 根据菜单id查询parentid返回给跟多菜单模块
	 * @author 李玉鹏
	 * @date 2015年8月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param roleIds[]
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(value="/serchByMenuId/{menuId}",method = RequestMethod.GET)
	public Integer serchByMenuId(@PathVariable Integer menuId) {
		Menu menu = menuRepository.findByMenuId(menuId);
		return menu.getParentId();
	}

	/**
	 * url:/ynlxhealth/menu/serchByMenuId
	 * 根据菜单id查询parentid返回给跟多菜单模块
	 * @author 李玉鹏
	 * @date 2015年8月14日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param roleIds[]
	 * @param @return   
	 * @return ListJsonStructure<Menu>   
	 * @throws
	 */
	@RequestMapping(value="/serchByParentIdAndRoles",method = RequestMethod.GET)
	public List<Menu> serchByMenuId(
			@ModelAttribute("id") String id,
			@RequestParam(value = "roleIds[]") Integer[] roleIds) {
		int parentId=0;
		if(null!=id&&!id.isEmpty()){
			parentId=Integer.parseInt(id);
		}
		List<Menu> menu = menuRepository.findByParentIdAndRoleIds(parentId,roleIds);
		return menu;
	}
}
