package com.ynswet.common.domain;

import java.util.List;

/**
 * 发送消息类
 * <p>Title: SendMessage.java</p>
 * @author zmk
 * @date 2014年8月21日 上午11:51:43
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:云南立翔科技有限公司</p>
 */
public class ThirdMessage {

	private String vtype;
	//短信
	private List<String> numbers;
	private String templateId;
	private String templateParam;
	
	//推送
	private String id;
	private String type;
	private String content;
	private int num = 1;
	private String[] pushCode;
	
	//打印
	private String msgNo;
	private String title;
	private boolean  isPrintCode=true;
	
	
	public void setSMSMsg(List<String> numbers, String templateId, String templateParam) {
		this.vtype = "sms";
		this.numbers = numbers;
		this.templateId = templateId;
		this.templateParam = templateParam;
	}
	
	public void setSMSMsgTask(List<String> numbers, String templateId, String templateParam) {
		this.vtype = "smsTask";
		this.numbers = numbers;
		this.templateId = templateId;
		this.templateParam = templateParam;
	}

	public void setPushMsg(String id, String type, String content, String[] pushCode) {
		this.vtype = "push";
		this.id = id;
		this.type = type;
		this.content = content;
		this.pushCode = pushCode;
	}

	public void setPrintMsg(String msgNo, String title,String content) {
		this.vtype = "print";
		this.content = content;
		this.msgNo = msgNo;
		this.title = title;
	}

	public String getVtype() {
		return vtype;
	}

	public String getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public int getNum() {
		return num;
	}

	public void setNum(int num) {
		this.num = num;
	}

	public String getType() {
		return type;
	}
	
	public List<String> getNumbers() {
		return numbers;
	}

	public String getTemplateId() {
		return templateId;
	}

	public String getTemplateParam() {
		return templateParam;
	}

	public String[] getPushCode() {
		return pushCode;
	}
	
	public String getMsgNo() {
		return msgNo;
	}

	public String getTitle() {
		return title;
	}

	public boolean isPrintCode() {
		return isPrintCode;
	}

	public void setPrintCode(boolean isPrintCode) {
		this.isPrintCode = isPrintCode;
	}
	
	
}
