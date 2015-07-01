/**       
 * @Title: MenuRest.java
 * @Package com.ynswet.homecloud.common.rest
 * @Description: TODO
 * @author 李玉鹏
 * @date 2015年5月13日 上午10:41:29
 * @version V1.0  
 * <p>Copy 
 * <p>Company:广州合光信息科技有限公司</p>      
 */
package com.ynswet.system.sc.rest;

import java.io.File;
import java.io.IOException;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.ynswet.common.util.FileUtils;

/**
 * 
 * 类功能说明
 * <p>Title: FilesRest.java</p>
 * @author 张明坤
 * @date 2015年5月29日 下午4:45:18
 * 类修改者	修改日期
 * 修改说明
 * @version V1.0
 * <p>Description:立翔云</p>
 * <p>Copyright: Copyright (c) 2015</p>
 * <p>Company:广州合光信息科技有限公司</p>
 */
@RestController
@RequestMapping("/files")
public class FilesRest {
	

	/**
	 * 上传文件
	 * 函数功能说明
	 * @author 张明坤 
	 * @date 2015年5月29日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param @param fileData
	 * @param @return    
	 * @return String   
	 * @throws
	 */
	@RequestMapping(value="/upload",method = RequestMethod.POST)
	public String uploadFile(@RequestParam("Filedata") MultipartFile fileData){
		String savePath="E:\\"+fileData.getOriginalFilename();
		try {
			FileUtils.copyInputStreamToFile(fileData.getInputStream(), new File(savePath));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "true";
	}
	
}
