package com.ynswet.common.util;

import static com.aliyun.oss.internal.OSSConstants.DEFAULT_BUFFER_SIZE;
import static com.aliyun.oss.internal.OSSConstants.DEFAULT_CHARSET_NAME;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.common.utils.HttpUtil;
import com.aliyun.oss.model.OSSObject;
import com.aliyun.oss.model.ObjectMetadata;
import com.ynswet.common.domain.OSSConfig;
import com.ynswet.common.domain.SingleJsonStructure;

/***
 * 
 * 阿里去上传类
 * <p>Title: AlyOSS.java</p>
 * @author 张明坤
 * @date 2015年11月2日 上午11:09:42
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
public class OSSUtils {
    private OSSConfig conf;
    private OSSClient client;
    private int size_m=1000000;
    private int fileSize=20;
	public OSSUtils() {
		// TODO Auto-generated constructor stub
		conf=new OSSConfig();
		// 初始化OSSClient
		client = new OSSClient(conf.getEndpoint(), conf.getAccessKeyId(), conf.getAccessKeySecret());
	}
	
	public OSSUtils(OSSConfig conf) {
		// TODO Auto-generated constructor stub
		this.conf=conf;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	/**
	 * 上传文件至阿里云,返回云地址
	 * 函数功能说明
	 * @author 张明坤 
	 * @date 2015年11月2日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param fileData
	 * @param @param relpath
	 * @param @return    
	 * @return String   
	 * @throws
	 */
	public SingleJsonStructure uploadAly(MultipartFile fileData,String relpath){
		String host=conf.getEndpoint().replace("http://", "");
		SingleJsonStructure json=postObjectAly(fileData, relpath);
		json.setRows("http://"+conf.getBucketName()+"."+host+"/"+json.getRows().toString());
		return json;
	}
	
	/**
	 * 
	 * 上传文件至阿里云
	 * @author 张明坤 
	 * @date 2015年11月9日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param fileData
	 * @param @param repath
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	private SingleJsonStructure postObjectAly(MultipartFile fileData, String relpath) {
		SingleJsonStructure json=new SingleJsonStructure();
		if(fileData.getSize()>(size_m*fileSize)){ 
			json.setSuccess(false);
			json.setMsg("上传失败：文件大小不能超过"+fileSize+"M!");
			return json;
		}
		String originName = getOriginName(fileData);
		if(originName.isEmpty()){
			json.setSuccess(false);
			json.setMsg("获取文件名失败!");
			return json;
		}
		String repath=relpath + originName;

		// 获取指定文件的输入流
		InputStream content = null;
		try {
			content = fileData.getInputStream();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg(e.getMessage());
			return json;
		}
		// 创建上传Object的Metadata
		ObjectMetadata meta = new ObjectMetadata();
		// 必须设置ContentLength
		meta.setContentLength(fileData.getSize());
		try {
			// 上传Object.
			client.putObject(conf.getBucketName(), repath, content, meta);
		} catch (ClientException e1) {
			e1.printStackTrace();
			json.setSuccess(false);
			json.setMsg(e1.getMessage());
			return json;
		}
		json.setMsg("文件上传成功！");
		json.setRows(repath);
		return json;
	}
	
	/**
	 * 
	 * 上传文件至阿里云,返回本地地址
	 * @author 张明坤 
	 * @date 2015年11月6日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param fileData
	 * @param @param relpath
	 * @param @param request
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	public SingleJsonStructure uploadAly(MultipartFile fileData,String relpath,HttpServletRequest request){
		SingleJsonStructure json=postObjectAly(fileData, relpath);
		String ipport="http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath()+"/";
		json.setMsg("文件上传成功！");
		json.setRows(ipport+"oss/"+json.getRows().toString());
		return json;
	}
	/**
	 * 
	 * 上传文件至本地
	 * @author 张明坤 
	 * @date 2015年11月2日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param fileData
	 * @param @param relpath
	 * @param @param request
	 * @param @return    
	 * @return SingleJsonStructure   
	 * @throws
	 */
	public SingleJsonStructure uploadLocal(MultipartFile fileData,String relpath,HttpServletRequest request){
		SingleJsonStructure json=new SingleJsonStructure();
		if(fileData.getSize()>(size_m*fileSize)){ 
			json.setSuccess(false);
			json.setMsg("上传失败：文件大小不能超过"+fileSize+"M!");
			return json;
		}
		String realpath = request.getSession().getServletContext().getRealPath("/"); 
		String ipport="http://"+request.getLocalAddr()+":"+request.getLocalPort()+request.getContextPath()+"/";
		String originName = getOriginName(fileData);
		if(originName.isEmpty()){
			json.setSuccess(false);
			json.setMsg("获取文件名失败!");
			return json;
		}
		String repath=relpath + originName;
		String savePath =realpath+repath;
		
		try {
			FileUtils.copyInputStreamToFile(fileData.getInputStream(), new File(savePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			json.setSuccess(false);
			json.setMsg(e.getMessage());
			return json;
		}
		json.setRows(ipport+repath);
		json.setMsg("文件上传成功！");
		return json;
	}
	
	/**
	 * 
	 * 下载阿里云文件
	 * @author 张明坤 
	 * @date 2015年11月6日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param relpath
	 * @param @param response
	 * @param @throws IOException    
	 * @return void   
	 * @throws
	 */
	public void downloadAly(String relpath,HttpServletResponse response) throws IOException {
	    // 获取Object，返回结果为OSSObject对象
	    OSSObject object = client.getObject(conf.getBucketName(), relpath);
	    
	    // 获取Object的输入流
	    InputStream objectContent = object.getObjectContent();
	    
	    // 处理Object
	    ObjectMetadata omd=object.getObjectMetadata();
	    response.reset();
	    response.setHeader("Connection", "keep-alive");
	    response.setHeader("Pragma", "No-cache");
	    response.setDateHeader("Expires", 0);
	    response.setHeader("Cache-Control", omd.getCacheControl()); 
	    response.setHeader("Content-Disposition",omd.getContentDisposition());
	    response.setContentType(object.getObjectMetadata().getContentType());
	    OutputStream fos=response.getOutputStream();
		BufferedOutputStream bos=new BufferedOutputStream(fos);
		 byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
         int bytesRead;
         while ((bytesRead = objectContent.read(buffer)) != -1) {
        	 bos.write(buffer, 0, bytesRead);
         }
        // 关闭流
 	    objectContent.close();
		bos.close();
		fos.close(); 
	}
	
	/**
	 * 
	 * 重定向至云地址
	 * @author 张明坤 
	 * @date 2016年1月12日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param relpath
	 * @param @param request
	 * @param @param response
	 * @param @throws IOException    
	 * @return void   
	 * @throws
	 */
	public void redirectAly(String relpath,HttpServletRequest request,HttpServletResponse response) throws IOException{
		// 设置URL过期时间为1小时
		Date expiration = new Date(new Date().getTime() + 120 * 1000);
		String host=conf.getEndpoint().replace("http://", "");
		// 生成URL
		URL url = client.generatePresignedUrl(conf.getBucketName(), relpath, expiration);
		String redirect="http://"+conf.getBucketName()+"."+host+"/"+urlEncodeKey(relpath)+"?"+url.getQuery();
		response.sendRedirect(redirect); //重定向
	}
	
	 /**
     * Encode object URI.
     */
    private String urlEncodeKey(String key) {
        StringBuffer resultUri = new StringBuffer();
        String[] keys = key.split("/");
        resultUri.append(HttpUtil.urlEncode(keys[0], DEFAULT_CHARSET_NAME));
        for (int i = 1; i < keys.length; i++) {
            resultUri.append("/").append(HttpUtil.urlEncode(keys[i], DEFAULT_CHARSET_NAME));
        }
        if (key.endsWith("/")) {
            // String#split ignores trailing empty strings,
            // e.g., "a/b/" will be split as a 2-entries array,
            // so we have to append all the trailing slash to the uri.
            for (int i = key.length() - 1; i >= 0; i--) {
                if (key.charAt(i) == '/') {
                    resultUri.append("/");
                } else {
                    break;
                }
            }
        }
        return resultUri.toString();
    }
    
	/**
	 * 
	 * 获取原始文件名
	 * @author 张明坤 
	 * @date 2015年11月2日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param fileData
	 * @param @return    
	 * @return String   
	 * @throws
	 */
	private  String getOriginName(MultipartFile fileData){
		String fileName="";
		try {
			fileName = java.net.URLDecoder.decode(fileData.getOriginalFilename(),"UTF-8");
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			return "";
		}
		String[] fileNameArray = fileName.split("\\\\");
	    String originName = fileNameArray[fileNameArray.length - 1];
	    return originName;
	}
}
