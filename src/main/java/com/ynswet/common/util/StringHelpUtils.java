package com.ynswet.common.util;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringHelpUtils extends org.springframework.util.StringUtils {

	public static String replaceAll(String regex, String str,
			Map<String, String> map) {

		String replacement = null;

		Matcher m = Pattern.compile(regex).matcher(str);

		if (m.find()) {

			replacement = map.get(m.group(1));

		}
		return m.replaceAll(replacement);
	}

}
