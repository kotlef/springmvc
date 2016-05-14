package com.ynswet.system.sc.mgt;

import org.apache.shiro.mgt.DefaultSessionStorageEvaluator;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.WebUtils;

public class MixSessionStorageEvaluator extends DefaultSessionStorageEvaluator {

	@Override
	public boolean isSessionStorageEnabled(Subject subject) {
		// TODO Auto-generated method stub

		if (WebUtils.isWeb(subject)) {

			return (subject != null && subject.getSession(false) != null)
					|| isSessionStorageEnabled();

		} else {

			// not a web request - maybe a RMI or daemon invocation?

			// set 'enabled' another way …
			return false;

		}

	}

}
