package com.ynswet.system.sc.realm;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.shiro.authc.AccountException;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.config.ConfigurationException;
import org.apache.shiro.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.ynswet.system.sc.domain.Userlogin;
import com.ynswet.system.sc.model.CommonVariableModel;
import com.ynswet.system.sc.model.UserAuthorizationInfo;
import com.ynswet.system.sc.repository.PermissionRepository;
import com.ynswet.system.sc.repository.PostRepository;
import com.ynswet.system.sc.repository.RoleRepository;
import com.ynswet.system.sc.util.PasswordHelper;
import com.ynswet.system.sc.util.SimpleByteSource;

/**
 *
 * apache shiro 的身份验证类
 *
 * @author elfmatian
 *
 */
public class JdbcAuthenticationRealm extends AuthorizationRealm {

	@Autowired
	private UserManager userManager;

	@Autowired
	private PostRepository postRepository;

	@Autowired
	private PermissionRepository permissionRepository;

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private PasswordHelper passwordHelper;

	/**
	 * 用户登录的身份验证方法
	 *
	 */
	@Transactional(readOnly=true)
	protected AuthenticationInfo doGetAuthenticationInfo(
			AuthenticationToken token) throws AuthenticationException {

		UsernamePasswordToken loginStringPasswordToken = (UsernamePasswordToken) token;

		String loginString = loginStringPasswordToken.getUsername();

		if (loginString == null) {
			throw new AccountException("用户名不能为空!");
		}

		Userlogin userlogin = userManager.getUserlogin(loginString);

		if (userlogin == null) {
			throw new UnknownAccountException("用户名不存在!");
		}
		if(userlogin !=null && userlogin.getStatus().equals("S")){
			throw new AccountException("用户已失效!");
		}
		CommonVariableModel model = new CommonVariableModel(userlogin);
		
		Integer uid = userlogin.getUid();
		
		model.setAuthorizationInfo(getUserAuthorizationInfoByUserId(uid));

		return new SimpleAuthenticationInfo(model, userlogin.getPassword(),
				new SimpleByteSource(passwordHelper
						.getCredentialsSalt(userlogin)), getName());

	}


	/**
	 *
	 * 函数功能说明
	 *
	 * @author 原勇
	 * @date 2015年5月10日 修改者名字 修改日期 修改内容
	 * @param @param uid
	 * @param @return   
	 * @return UserAuthorizationInfo   
	 * @throws
	 */
	public UserAuthorizationInfo getUserAuthorizationInfoByUserId(Integer uid) {

		Collection<Integer> roleIds = postRepository.findUserRoleIdsByUid(uid);

		List<String> roles = roleRepository.findRoleCodeByRoleId(roleIds);

		List<String> permValue = permissionRepository
				.findPermissionValueByRoleIds(roleIds);

		UserAuthorizationInfo ua = new UserAuthorizationInfo();

		List<String> perms = new ArrayList<String>();
		for (String perm : permValue) {
			perms.add(getPermissions(perm));
		}
		ua.setPerms(perms);
		ua.setRoles(roles);
		return ua;
	}

	/**
	 * from DefaultFilterChainManager
	 */
	protected String[] splitChainDefinition(String chainDefinition) {

		return StringUtils.split(chainDefinition,
				StringUtils.DEFAULT_DELIMITER_CHAR, '[', ']', true, true);
	}

	/**
	 * from DefaultFilterChainManager
	 */
	protected String[] toNameConfigPair(String token)
			throws ConfigurationException {

		try {
			String[] pair = token.split("\\[", 2);
			String name = StringUtils.clean(pair[0]);

			if (name == null) {
				throw new IllegalArgumentException(
						"Filter name not found for filter chain definition token: "
								+ token);
			}
			String config = null;

			if (pair.length == 2) {
				config = StringUtils.clean(pair[1]);

				config = config.substring(0, config.length() - 1);
				config = StringUtils.clean(config);

				if (config != null && config.startsWith("\"")
						&& config.endsWith("\"")) {
					String stripped = config.substring(1, config.length() - 1);
					stripped = StringUtils.clean(stripped);

					if (stripped != null && stripped.indexOf('"') == -1) {
						config = stripped;
					}

				}
			}

			return new String[]{name, config};

		} catch (Exception e) {
			String msg = "Unable to parse filter chain definition token: "
					+ token;
			throw new ConfigurationException(msg, e);
		}
	}

	protected String getPermissions(String chainDefinition) {

		String[] filterTokens = splitChainDefinition(chainDefinition);

		for (String token : filterTokens) {
			String[] nameConfigPair = toNameConfigPair(token);
			if ("perms".equals(nameConfigPair[0]))
				return nameConfigPair[1];

		}
		return null;

	}

	public static void main(String[] args) {

		JdbcAuthenticationRealm ja = new JdbcAuthenticationRealm();
		String chainDefinition = "perms[user:save:*],roles[files,user_roles,urls:save,update,delete:*]";
		chainDefinition = "perms[user:save:4]";
		String[] filterTokens = ja.splitChainDefinition(chainDefinition);

		// each token is specific to each filter.
		// strip the name and extract any filter-specific config between
		// brackets [ ]
		for (String token : filterTokens) {
			if ("perms".equals(token)) {
				String[] nameConfigPair = ja.toNameConfigPair(token);
				// System.out.println(nameConfigPair[0] + "|" +
				// nameConfigPair[1]);
			}

		}
	}

}
