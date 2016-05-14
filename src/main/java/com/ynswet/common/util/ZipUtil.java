package com.ynswet.common.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.Zip;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

public class ZipUtil {

	/**
	 * Java中使用Ant的API Zip压缩文件夹，使用简单，高效率，高灵活性
	 * 
	 * @param dest
	 * @param src
	 */
	public static void zip(String dest, String src) {
		Zip zip = new Zip();
		File file = new File(src);
		if (file.exists()) {
			if (file.isDirectory()) {
				zip.setBasedir(file);
				zip.setDestFile(new File(dest));
				Project p = new Project();
				p.setBaseDir(new File(src));
				zip.setProject(p);
				zip.execute();
			}
		}
	}

	/**
	 * 把files集合内的所有文件压缩成包test.zip
	 * 
	 * @param files
	 * @throws IOException
	 */
	public static void zipFiles(List<File> files, String filePath)
			throws IOException {
		File tmp_File = new File(filePath);
		if (!tmp_File.exists())
			tmp_File.createNewFile();
		FileInputStream fileStream = null;// 输入文件的流
		ByteArrayOutputStream out = null;// 把输入流转换成输出流

		FileOutputStream f_OutputStream = new FileOutputStream(tmp_File);
		// 构造zip输出流,用来生成zip文件
		ZipOutputStream zipOutputStream = new ZipOutputStream(f_OutputStream);

		try {

			// 循环即将被压缩的文件
			for (File file : files) {
				// 转换输入流转换成输出流
				out = new ByteArrayOutputStream();
				fileStream = new FileInputStream(file);
				byte[] b = new byte[1024];
				while (fileStream.read(b) != -1) {
					out.write(b); // 输入流转换到输出流中了
				}

				// 构造zip中某个文件
				zipOutputStream.putNextEntry(new ZipEntry(file.getName()));
				// 把输出流写到zip中,生成实际文件
				zipOutputStream.write(out.toByteArray());
				// 清空缓冲
				zipOutputStream.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fileStream != null)
				fileStream.close();
			if (out != null)
				out.close();
			if (zipOutputStream != null)
				zipOutputStream.close();
		}

	}

	/**
	 * unzip
	 * 
	 * @param inFile
	 * @param dest
	 * @throws IOException
	 */
	public static void unzip(String inFile, File dest) throws IOException {
		if (!dest.exists()) {
			dest.mkdirs();
		}
		if (!dest.isDirectory()) {
			throw new IOException("Destination must be a directory.");
		}

		InputStream inputStream;
		byte[] buffer = new byte[1024];

		try {
			ZipFile zipFile = new ZipFile(inFile, "UTF-8");
			Enumeration entries = zipFile.getEntries();
			while (entries.hasMoreElements()) {
				ZipEntry entry = (ZipEntry) entries.nextElement();
				String fileName = entry.getName();
				if (fileName.charAt(fileName.length() - 1) == '/') {
					fileName = fileName.substring(0, fileName.length() - 1);
				}
				if (fileName.charAt(0) == '/') {
					fileName = fileName.substring(1);
				}
				if (File.separatorChar != '/') {
					fileName = fileName.replace('/', File.separatorChar);
				}

				File file = new File(dest, fileName);
				if (entry.isDirectory()) {
					// make sure the directory exists
					file.mkdirs();
				} else {
					// make sure the directory exists
					File parent = file.getParentFile();
					if (parent != null && !parent.exists()) {
						parent.mkdirs();
					}
					// dump the file
					OutputStream out = new FileOutputStream(file);
					inputStream = zipFile.getInputStream(entry);
					int len = 0;
					while ((len = inputStream.read(buffer, 0, buffer.length)) != -1) {
						out.write(buffer, 0, len);
					}
					out.flush();
					out.close();
					inputStream.close();
					file.setLastModified(entry.getTime());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		//System.out.println("begin");
		zip("/home/elfmatian/test.zip", "/home/elfmatian/Workspaces");
		//System.out.println("end");
	}

}
