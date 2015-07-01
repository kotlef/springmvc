package com.ynswet.common.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ynswet.common.mapper.JacksonMapper;
import com.fasterxml.jackson.core.JsonParser.Feature;


/**
 * 对象、json、xml转化类
 * <p>Title: JacksonUtil.java</p>
 * @author root
 * @date 2014-4-1 下午3:58:36
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2006</p>
 * <p>Company:云南立翔科技有限公司</p>
 */
public class JacksonUtil {

	/**
	 * json字符串转对象
	 * @author root
	 * @date 2014-4-1
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param content
	 * @param @param valueType
	 * @param @return   
	 * @return T   
	 * @throws
	 */
	public static <T> T json2Object(String content, Class<T> valueType) {
		/*JacksonMapper jm=new JacksonMapper();
		T t=jm.fromJson(content, valueType);
		return t;*/
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.ALLOW_UNQUOTED_FIELD_NAMES, true);
		try {
			return (T) objectMapper.readValue(content, valueType);
		} catch (JsonParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonMappingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 *
	 * 对象转json字符串
	 * @author root
	 * @date 2014-4-1
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param obj
	 * @param @return   
	 * @return String   
	 * @throws
	 */
	public static String obj2Json(Object obj) {
		JacksonMapper jsm = JacksonMapper.nonNullMapper();
		return jsm.toJson(obj);
	}

	/**
	 *
	 * xml字符串转对象
	 * @author root
	 * @date 2014-4-1
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param content
	 * @param @param valueType
	 * @param @return   
	 * @return T   
	 * @throws
	 */
	@SuppressWarnings("unchecked")
	public static <T> T xml2Object(String content, Class<T> valueType) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(valueType);
			Unmarshaller um = jaxbContext.createUnmarshaller();
			T t = (T) um.unmarshal(new ByteArrayInputStream(content.getBytes()));
			return t;
		} catch (JAXBException e) {
			//System.out.println("JAXB castor failed to convert the metadata to module instance.");
		}
		return null;
	}

	/**
	 *
	 * 对象转xml
	 * @author root
	 * @date 2014-4-1
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param obj
	 * @param @return   
	 * @return String   
	 * @throws
	 */
	@SuppressWarnings("null")
	public static  String obj2Xml(Object obj) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller=jaxbContext.createMarshaller();
			//marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        marshaller.marshal(obj, outputStream);
			return outputStream.toString();
		} catch (JAXBException e) {
			//System.out.println("JAXB castor failed to convert the module instance  to metadata.");
		}
		return null;
	}

	/**
	 *
	 * 对象转格式化xml
	 * @author root
	 * @date 2014-4-1
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param obj
	 * @param @return   
	 * @return String   
	 * @throws
	 */
	@SuppressWarnings("null")
	public static  String obj2FormcatXml(Object obj) {
		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(obj.getClass());
			Marshaller marshaller=jaxbContext.createMarshaller();
			marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	        marshaller.marshal(obj, outputStream);
			return outputStream.toString();
		} catch (JAXBException e) {
			//System.out.println("JAXB castor failed to convert the module instance  to metadata.");
		}
		return null;
	}


	/**
	 * 将Object转成JSON输出
	 * @param writer
	 * @param obj
	 */
	public static void writerJson(Writer writer, Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
			mapper.writeValue(writer, ObjectUtil.parseNull(obj));
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将Object转成JSON输出
	 * @param out
	 * @param obj
	 */
	public static void writerJson(OutputStream out, Object obj) {
        ObjectMapper mapper = new ObjectMapper();
        try {
			mapper.writeValue(out, ObjectUtil.parseNull(obj));
		} catch (JsonGenerationException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将JSON格式的字符串转成对象
	 * @param content
	 * @param ref
	 * @return
	 */
	public static <T> T readValue(String content, TypeReference<T> ref) {
		if(StringUtils.isTrimEmpty(content)) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(StringUtils.parseNull(content), ref);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将JSON格式的字符串转成对象
	 * @param content
	 * @param ref
	 * @return
	 */
	public static <T> T readValue(String content) {
		return JacksonUtil.readValue(content, new TypeReference<T>() {});
	}

	/**
	 * 将JSON格式的字符串转成对象
	 * @param content
	 * @param class1
	 * @return
	 */
	public static <T>  T  readArrayValue(String content, Class<T> class1) {
		if(StringUtils.isTrimEmpty(content)) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		try {
			return mapper.readValue(StringUtils.parseNull(content), class1);
		} catch (JsonParseException e) {
			throw new RuntimeException(e);
		} catch (JsonMappingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 将JSON格式的字符串转成对象
	 * @param content
	 * @param class1
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static <T> List  readListValue(String content, Class<T> class1) {
		Object[] arr=(Object[]) JacksonUtil.readArrayValue(content, class1);
	   return  Arrays.asList(arr);
	}

}
