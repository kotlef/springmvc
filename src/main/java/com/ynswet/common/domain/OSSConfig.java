package com.ynswet.common.domain;

/**
 * 阿里去OSS配置类
 * 类功能说明
 * <p>Title: OSSConfig.java</p>
 * @author 张明坤
 * @date 2015年11月2日 上午11:10:08
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public class OSSConfig {
	private String bucketName="swet";
	private String accessKeyId = "9yp7AZMExif6OG6V";
	private String accessKeySecret = "drDjiDYP7M1HN7m5Mv4oNxTHMGqj5O";
	private String endpoint="http://oss-cn-hangzhou.aliyuncs.com";
	public OSSConfig() {
	}
	
	public OSSConfig(String accessKeyId, String accessKeySecret) {
		super();
		this.accessKeyId = accessKeyId;
		this.accessKeySecret = accessKeySecret;
	}

	public String getBucketName() {
		return bucketName;
	}
	public void setBucketName(String bucketName) {
		this.bucketName = bucketName;
	}
	public String getAccessKeyId() {
		return accessKeyId;
	}
	public void setAccessKeyId(String accessKeyId) {
		this.accessKeyId = accessKeyId;
	}
	public String getAccessKeySecret() {
		return accessKeySecret;
	}
	public void setAccessKeySecret(String accessKeySecret) {
		this.accessKeySecret = accessKeySecret;
	}
	public String getEndpoint() {
		return endpoint;
	}
	public void setEndpoint(String endpoint) {
		this.endpoint = endpoint;
	}
	@Override
	public String toString() {
		return "OSSConfig [bucketName=" + bucketName + ", accessKeyId="
				+ accessKeyId + ", accessKeySecret=" + accessKeySecret
				+ ", endpoint=" + endpoint + "]";
	}
	
}
