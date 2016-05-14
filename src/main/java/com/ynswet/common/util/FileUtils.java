package com.ynswet.common.util;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.multipart.MultipartFile;

/**
 * File操作帮助类基于commons.io.FileUtils
 *
 * @author elfmatian
 *
 */
public class FileUtils extends org.apache.commons.io.FileUtils {

	// FEFF because this is the Unicode char represented by the UTF-8 byte order
	// mark (EF BB BF).
	public static final String UTF8_BOM = "\uFEFF";

	/**
	 * 功能描述：拷贝一个目录或者文件到指定路径下，即把源文件拷贝到目标文件路径下
	 *
	 * @param source
	 *            源文件
	 * @param target
	 *            目标文件路径
	 * @return void
	 */
	public static void copy(File source, File tarpath) {

		if (source.isDirectory()) {
			tarpath.mkdir();
			File[] dir = source.listFiles();
			for (int i = 0; i < dir.length; i++) {
				copy(dir[i], tarpath);
			}
		} else {
			try {
				InputStream is = new FileInputStream(source); // 用于读取文件的原始字节流
				OutputStream os = new FileOutputStream(tarpath); // 用于写入文件的原始字节的流
				byte[] buf = new byte[1024];// 存储读取数据的缓冲区大小
				int len = 0;
				while ((len = is.read(buf)) != -1) {
					os.write(buf, 0, len);
				}
				is.close();
				os.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	public static String removeUTF8BOM(String s) {
		if (s.startsWith(UTF8_BOM)) {
			s = s.substring(1);
		}
		return s;
	}

	public static void saveFile(File f, String data, boolean append)
			throws IOException {
		BufferedWriter bw = null;
		OutputStreamWriter osw = null;

		FileOutputStream fos = new FileOutputStream(f, append);
		try {
			// write UTF8 BOM mark if file is empty
			if (f.length() < 1) {
				final byte[] bom = new byte[] { (byte) 0xEF, (byte) 0xBB,
						(byte) 0xBF };
				fos.write(bom);
			}

			osw = new OutputStreamWriter(fos, "UTF-8");
			bw = new BufferedWriter(osw);
			if (data != null)
				bw.write(data);
		} catch (IOException ex) {
			throw ex;
		} finally {
			try {
				bw.close();
				fos.close();
			} catch (Exception ex) {
			}
		}
	}

	/**
	 * 保存上传的文件到指定目录
	 * @param savePath 要保存到的文件夹的路径
	 * @param fileData 要保存的数据
	 * @return true表示保存成功，false保存失败
	 */
	public static boolean saveUploadFile(String savePath,MultipartFile fileData){
		return saveUploadFile(savePath,null,fileData);
	}

	/**
	 * 保存上传的文件到指定目录
	 * @param savePath 要保存到的文件夹的路径
	 * @param saveName 指定保存的文件名
	 * @param fileData 要保存的数据
	 * @return true表示保存成功，false保存失败
	 */
	public static boolean saveUploadFile(String savePath,String saveName,MultipartFile fileData){
		if (fileData.isEmpty()) {
			return false;
		}

		String parentPath=new File(FileUtils.class.getResource("/").getPath()).getParent();
		savePath=savePath.indexOf("/")==0?savePath:"/"+savePath;
		String path = parentPath+ savePath;
		if(saveName==null){
			saveName = fileData.getOriginalFilename();
		}
		try {
			File file=new File(path,saveName);
			FileUtils.copyInputStreamToFile(fileData.getInputStream(),file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 下载文件
	 * @author root
	 * @date 2014年6月12日
	 * 修改者名字 修改日期
	 * 修改内容
	 * @param path
	 * @param response   
	 * void   
	 */
	public static void downloadFile(String path,HttpServletResponse response){
		try{
			File file=new File(path);
			String fileName=file.getName();
			response.reset();
			response.setContentType("application/x-msdownload");
			response.setHeader("Content-Disposition", "attachment;success=true;filename="+new String(fileName.getBytes("gbk"),"iso-8859-1"));
			BufferedInputStream bis=null;
			BufferedOutputStream bos=null;
			OutputStream fos=null;
			InputStream fis=null;
			fis=new FileInputStream(file);
			bis=new BufferedInputStream(fis);
			fos=response.getOutputStream();
			bos=new BufferedOutputStream(fos);
			int bytesRead=0;
			byte[] buffer=new byte[8192];
			while((bytesRead=bis.read(buffer,0,8192))!=-1){
				bos.write(buffer,0,bytesRead);
			}
			bos.flush();
			fis.close();
			bis.close();
			fos.close();
			bos.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}