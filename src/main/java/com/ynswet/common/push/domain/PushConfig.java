package com.ynswet.common.push.domain;


public class PushConfig {

	private final static  String  ALARM_PUSH_API="/ynlxmsgpush/msg/alarm";

	private final static  String  EQPT_PUSH_API="/ynlxmsgpush/msg/eqpt";

	private final static  String  CARE_PUSH_API="/ynlxmsgpush/msg/care";

	private final static  String  SAFE_PUSH_API="/ynlxmsgpush/msg/safe";

	private final static  String  BILL_PUSH_API="/ynlxmsgpush/msg/bill";
	
	private final static  String  SMS_PUSH_API="/ynlxmsgpush/msg/sms";
	
	private final static  String  JPUSH_PUSH_API="/ynlxmsgpush/msg/jpush";
	
	private final static  String  PUSH_HOST="http://localhost:8080";
	
	private String pushHost=PUSH_HOST;

	private String alarmPushApi=ALARM_PUSH_API;

	private String eqptPushApi=EQPT_PUSH_API;

	private String acarePushApi=CARE_PUSH_API;

	private String safePushApi=SAFE_PUSH_API;

	private String billPushApi=BILL_PUSH_API;
	
	private String smsPushApi=SMS_PUSH_API;
	
	private String jpushPushApi=JPUSH_PUSH_API;

	
	public String getPushHost() {
		return pushHost;
	}

	public void setPushHost(String pushHost) {
		this.pushHost = pushHost;
	}

	public String getAlarmPushApi() {
		return pushHost+alarmPushApi;
	}

	public void setAlarmPushApi(String alarmPushApi) {
		this.alarmPushApi = alarmPushApi;
	}

	public String getEqptPushApi() {
		return pushHost+eqptPushApi;
	}

	public void setEqptPushApi(String eqptPushApi) {
		this.eqptPushApi = eqptPushApi;
	}

	public String getAcarePushApi() {
		return pushHost+acarePushApi;
	}

	public void setAcarePushApi(String acarePushApi) {
		this.acarePushApi = acarePushApi;
	}

	public String getSafePushApi() {
		return pushHost+safePushApi;
	}

	public void setSafePushApi(String safePushApi) {
		this.safePushApi = safePushApi;
	}

	public String getBillPushApi() {
		return pushHost+billPushApi;
	}

	public void setBillPushApi(String billPushApi) {
		this.billPushApi = billPushApi;
	}

	public String getSmsPushApi() {
		return pushHost+smsPushApi;
	}

	public void setSmsPushApi(String smsPushApi) {
		this.smsPushApi = smsPushApi;
	}

	public String getJpushPushApi() {
		return pushHost+jpushPushApi;
	}

	public void setJpushPushApi(String jpushPushApi) {
		this.jpushPushApi = jpushPushApi;
	}


}
