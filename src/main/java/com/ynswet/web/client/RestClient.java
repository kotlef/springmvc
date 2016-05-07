package com.ynswet.web.client;

import java.util.Map;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynswet.common.util.JacksonUtil;
import com.ynswet.common.util.MapUtils;

public class RestClient {

	/**
	 * 
	 * Form Param发送
	 * @author 张明坤 
	 * @date 2015年12月29日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param restTemplate
	 * @param @param msg
	 * @param @param url
	 * @param @param method    
	 * @return JsonNode   
	 * @throws
	 */
	public static JsonNode send2Form(WebRestTemplate restTemplate,Map<String,Object> msg,String url,HttpMethod method){
		ObjectMapper mapper= new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		HttpEntity<String> entity = new HttpEntity<String>(MapUtils.getUrlParamsByMap(msg),headers);
		try{
		    ResponseEntity<String> resp = restTemplate.exchange(url, method, entity,String.class);
		    if(null==resp)
		    	return null;
		    JsonNode node= mapper.readTree(resp.getBody());
		    System.out.println(node.toString());
		    return node;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
	}
	/**
	 * 
	 * POST Json发送
	 * @author 张明坤 
	 * @date 2015年12月29日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param restTemplate
	 * @param @param msg
	 * @param @param url    
	 * @return JsonNode   
	 * @throws
	 */
	public static JsonNode send2Json(WebRestTemplate restTemplate,Object msg,String url){
		ObjectMapper mapper= new ObjectMapper();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<String>(JacksonUtil.obj2Json(msg),headers);
		try{
		    ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.POST, entity,String.class);
		    if(null==resp)
		    	return null;
		    JsonNode node= mapper.readTree(resp.getBody());
		    System.out.println(node.toString());
		    return node;
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
		
	}
}
