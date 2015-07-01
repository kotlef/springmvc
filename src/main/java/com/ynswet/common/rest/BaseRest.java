package com.ynswet.common.rest;

import java.sql.Time;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;

@Controller
public class BaseRest {

	/**
	 * Register custom, context-specific property editors
	 *
	 */

	public final static String DEFAULT_PASSWORD="888888";

	public final static String ALREADY_EXISTS="账号已经存在，请重新填写";

	public final static String SAVE_SUCCESS="添加成功";

	public final static String UPDATE_SUCCESS="更新成功";

	public final static String DELTE_SUCCESS="删除成功";

	public final static String FAILURE="操作失败";


	@InitBinder
	public void initBinder(WebDataBinder binder) {

		binder.registerCustomEditor(java.util.Calendar.class,
				new com.ynswet.common.util.databinding.CustomCalendarEditor());
		binder.registerCustomEditor(
				byte[].class,
				new org.springframework.web.multipart.support.ByteArrayMultipartFileEditor());
		binder.registerCustomEditor(boolean.class,
				new com.ynswet.common.util.databinding.EnhancedBooleanEditor(
						false));
		binder.registerCustomEditor(Boolean.class,
				new com.ynswet.common.util.databinding.EnhancedBooleanEditor(
						true));

		binder.registerCustomEditor(java.math.BigDecimal.class,
				new com.ynswet.common.util.databinding.NaNHandlingNumberEditor(
						java.math.BigDecimal.class, true));
		binder.registerCustomEditor(Integer.class,
				new com.ynswet.common.util.databinding.NaNHandlingNumberEditor(
						Integer.class, true));
		binder.registerCustomEditor(java.util.Date.class,
				new com.ynswet.common.util.databinding.CustomDateEditor());
		binder.registerCustomEditor(String.class,
				new com.ynswet.common.util.databinding.StringEditor());
		binder.registerCustomEditor(Long.class,
				new com.ynswet.common.util.databinding.NaNHandlingNumberEditor(
						Long.class, true));
		binder.registerCustomEditor(Double.class,
				new com.ynswet.common.util.databinding.NaNHandlingNumberEditor(
						Double.class, true));

//		binder.registerCustomEditor(Date.class,
//				new com.ynswet.common.util.databinding.CustomDateEditor(
//						"yyyy-MM-dd HH:mm:ss"));

		binder.registerCustomEditor(Date.class,
				new com.ynswet.common.util.databinding.CustomDateEditor(
						"yyyy-MM-dd"));

	}





}
