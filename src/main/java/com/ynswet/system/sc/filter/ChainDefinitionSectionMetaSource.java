package com.ynswet.system.sc.filter;

import java.util.List;

import org.apache.shiro.config.Ini;
import org.apache.shiro.config.Ini.Section;
import org.apache.shiro.util.CollectionUtils;
import org.apache.shiro.util.StringUtils;
import org.apache.shiro.web.config.IniFilterChainResolverFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.annotation.Autowired;

import com.ynswet.system.sc.domain.Permission;

import com.ynswet.system.sc.repository.PermissionRepository;

/**
 * 借助spring {@link FactoryBean} 对apache shiro的premission进行动态创建
 * 
 * @author elfmatian
 * 
 */
public class ChainDefinitionSectionMetaSource implements
		FactoryBean<Ini.Section> {

	
	@Autowired
	private PermissionRepository permissionRepository;

	// shiro默认的链接定义
	private String filterChainDefinitions;

	/**
	 * 通过filterChainDefinitions对默认的链接过滤定义
	 * 
	 * @param filterChainDefinitions
	 *            默认的接过滤定义
	 */
	public void setFilterChainDefinitions(String filterChainDefinitions) {
		this.filterChainDefinitions = filterChainDefinitions;
	}

	public Section getObject() throws BeansException {
		Ini ini = new Ini();
		// 加载默认的url
		ini.load(filterChainDefinitions);

		Ini.Section section = ini
				.getSection(IniFilterChainResolverFactory.URLS);
		if (CollectionUtils.isEmpty(section)) {
			section = ini.getSection(Ini.DEFAULT_SECTION_NAME);
		}

		// 查出shiro必须授权验证安全数据资源
		List<Permission> permissions = permissionRepository.findAll();
		

		// 循环数据库资源的url
		if (null != permissions && permissions.size() > 0) {
			for (Permission permission : permissions) {
				if (StringUtils.hasText(permission.getPermissionUrl())
						&& StringUtils.hasText(permission.getPermissionValue())) {
					section.put(permission.getPermissionUrl(), permission.getPermissionValue());
				}
			}
		}

		return section;
	}

	public Class<?> getObjectType() {
		return Section.class;
	}

	public boolean isSingleton() {
		return true;
	}

}