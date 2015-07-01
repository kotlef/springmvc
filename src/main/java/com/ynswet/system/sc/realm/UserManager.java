package com.ynswet.system.sc.realm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.system.sc.domain.Homepage;
import com.ynswet.system.sc.domain.Menu;
import com.ynswet.system.sc.domain.Org;
import com.ynswet.system.sc.domain.Res;
import com.ynswet.system.sc.domain.Role;
import com.ynswet.system.sc.domain.User;
import com.ynswet.system.sc.domain.Userlogin;
import com.ynswet.system.sc.repository.HomepageRepository;
import com.ynswet.system.sc.repository.MenuRepository;
import com.ynswet.system.sc.repository.OrgRepository;
import com.ynswet.system.sc.repository.PermissionRepository;
import com.ynswet.system.sc.repository.PostRepository;
import com.ynswet.system.sc.repository.ResRepository;
import com.ynswet.system.sc.repository.RoleRepository;
import com.ynswet.system.sc.repository.UserRepository;
import com.ynswet.system.sc.repository.UserloginRepository;
import com.ynswet.system.sc.util.AuthorizationCacheHelper;
import com.ynswet.system.sc.util.PasswordHelper;
import com.ynswet.system.sc.util.SystemVariableUtils;

/**
 * 用戶管理
 *
 * @author elfmatian
 */
@Service
public class UserManager {

	// 用户数据访问
	@Autowired
	private UserloginRepository userloginRepository;

	@Autowired
	private OrgRepository orgRepository;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private ResRepository resRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private MenuRepository menuRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordHelper passwordHelper;

	@Autowired
	private AuthorizationCacheHelper cacheHelper;

	@Autowired
	private HomepageRepository homepageRepository;
	/**
	 * shiro授权缓存key
	 */
	private static final String ShiroAuthorizationCache = "shiroAuthorizationCache";

	/**
	 * shiro认证缓存key
	 */
	private static final String ShiroAuthenticationCache = "shiroAuthenticationCache";

	// ------------------------------用户管理-----------------------------------//

	/**
	 * 更新当前用户密码
	 *
	 * @param oldPassword
	 *            旧密码
	 * @param newPassword
	 *            新密码
	 * @return boolean
	 */
	@Transactional
	public boolean updateUserPassword(String oldPassword, String newPassword) {
		Userlogin userlogin = SystemVariableUtils.getCommonVariableModel()
				.getUserlogin();
		String pwd = passwordHelper.getPassword(userlogin, oldPassword);
		if (pwd.equals(userlogin.getPassword())) {
			List<Userlogin> userloginList = userloginRepository
					.findByUid(userlogin.getUid());
			for (Userlogin ul : userloginList) {
				ul.setPassword(newPassword);
				passwordHelper.encryptPassword(ul);
				userloginRepository.save(ul);
			}
			userloginRepository.flush();
			return true;
		}
		return false;
	}

	/**
	 *
	 * 函数功能说明
	 *
	 * @author 原勇
	 * @date 2015年5月10日 修改者名字 修改日期 修改内容
	 * @param @param loginString
	 * @param @return   
	 * @return Userlogin   
	 * @throws
	 */
	public Userlogin getUserlogin(String loginString) {
		return userloginRepository.findOne(loginString);
	}

	/**
	 *
	 * 函数功能说明
	 *
	 * @author 原勇
	 * @date 2015年5月10日 修改者名字 修改日期 修改内容
	 * @param @param entity   
	 * @return void   
	 * @throws
	 */
	public void insertUser(Userlogin entity) {
		if (!isUserLoginStringUnique(entity.getLoginString())) {
			throw new ServiceException("用户名已存在");
		}

		passwordHelper.encryptPassword(entity);
		userloginRepository.saveAndFlush(entity);
	}

	/**
	 *
	 * 函数功能说明
	 *
	 * @author 原勇
	 * @date 2015年5月10日 修改者名字 修改日期 修改内容
	 * @param @param entity   
	 * @return void   
	 * @throws
	 */
	@CacheEvict(value = ShiroAuthorizationCache, allEntries = true)
	public void updateUser(Userlogin entity) {
		userloginRepository.saveAndFlush(entity);
	}

	/**
	 *
	 * 函数功能说明
	 *
	 * @author 原勇
	 * @date 2015年5月10日 修改者名字 修改日期 修改内容
	 * @param @param loginString
	 * @param @return   
	 * @return boolean   
	 * @throws
	 */
	public boolean isUserLoginStringUnique(String loginString) {
		return userloginRepository.findOne(loginString) == null;
	}

	// ------------------------------资源管理-----------------------------------//

	/**
	 *
	 * 函数功能说明
	 *
	 * @author 原勇
	 * @date 2015年5月10日 修改者名字 修改日期 修改内容
	 * @param @param uuid
	 * @param @return   
	 * @return List<String>   
	 * @throws
	 */
	public List<Res> getRessByUserId(Integer uid) {
		Collection<Integer> orgIds = postRepository.findUserOrgIdsByUid(uid);

		return (null == orgIds || orgIds.size() == 0) ? null : resRepository
				.findByInOrgids(orgIds);
	}

	public List<Role> getRolesByUserId(Integer uid) {
		Collection<Integer> roleIds = postRepository.findUserRoleIdsByUid(uid);
		return (null == roleIds || roleIds.size() == 0) ? null : roleRepository
				.findByroleIdIn(roleIds);
	}

	public List<Org> getOrgsByUserId(Integer uid) {
		Collection<Integer> orgIds = postRepository.findUserOrgIdsByUid(uid);
		return (null == orgIds || orgIds.size() == 0) ? null : orgRepository
				.findByOrgidIn(orgIds);
	}

	public User getUserByUserId(Integer uid) {
		return userRepository.findOne(uid);
	}

	public List<Menu> getMenusByUserId(Integer uid) {

		Collection<Integer> roleIds = postRepository.findUserRoleIdsByUid(uid);

		return (null == roleIds || roleIds.size() == 0) ? null : menuRepository
				.findByUserRoleIds(roleIds);
	}

	public List<Homepage> getHomepageByUserId(Integer uid) {
//		List<Homepage> homepageList = new ArrayList<Homepage>();
//		List<Role> roleList = getRolesByUserId(uid);
//		for (Role role : roleList) {
//			Homepage hp = homepageRepository.getOne(role.getHomepageId());
//			if (null != hp)
//				homepageList.add(hp);
//		}
//		return homepageList;
		Collection<Integer> roleIds = postRepository.findUserRoleIdsByUid(uid);

		return (null == roleIds || roleIds.size() == 0) ? null : homepageRepository
				.findByUserRoleIds(roleIds);
	}



}
