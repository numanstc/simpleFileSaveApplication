package com.numanstc.file.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.ServletContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.numanstc.file.model.UserFiles;
import com.numanstc.file.service.UserService;

@Controller
public class SpringFileDownloadController {

	@Autowired
	private ServletContext context;

	@Autowired
	private UserService userService;

	@GetMapping("/downloadfile/{fileName}/{modifiedFileName}")
	public void downloadFile(@PathVariable String fileName, @PathVariable String modifiedFileName,
			HttpServletResponse response) {

		String fullPath = context.getRealPath("/images/" + File.separator + modifiedFileName);
		File file = new File(fullPath);
		final int BUFFER_SIZE = 4096;

		if (file.exists()) {
			try {
				FileInputStream inputStream = new FileInputStream(file);
				String mimeType = context.getMimeType(fullPath);
				response.setContentType(mimeType);
				response.setHeader("Content-disposition", "attachment; filename=" + fileName);
				OutputStream outputStream = response.getOutputStream();
				byte[] buffer = new byte[BUFFER_SIZE];
				int bytesRead = -1;
				while ((bytesRead = inputStream.read(buffer)) != -1) {
					outputStream.write(buffer, 0, bytesRead);
				}
				inputStream.close();
				outputStream.close();

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

//		/downloadfilesaszip/

	@GetMapping("/downloadfilesaszip/{userId}")
	public void downloadFileAsZip(@PathVariable("userId") Long userId, HttpServletResponse response) {

		List<UserFiles> userFiles = userService.findFilesByUserId(userId);
		if (userFiles != null && userFiles.size() > 0) {
			downloadZipFiles(userFiles, "files.zip", response);
		}

	}

	private void downloadZipFiles(List<UserFiles> userFiles, String zipName, HttpServletResponse response) {

		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ZipOutputStream zos = new ZipOutputStream(baos);

			byte bytes[] = new byte[122880];
			for (UserFiles file : userFiles) {
				if(file != null && StringUtils.hasText(file.getModifiedFileName())) {
					FileInputStream fis = new FileInputStream(context.getRealPath("/images/"+file.getModifiedFileName()));
					BufferedInputStream bis = new BufferedInputStream(fis);
					zos.putNextEntry(new ZipEntry(file.getFileName()));
					int bytesRead;
					while ((bytesRead = bis.read(bytes)) != -1) {
						zos.write(bytes, 0, bytesRead);
					}
					zos.closeEntry();
					bis.close();
					fis.close();
				}
			}
			zos.flush();
			baos.flush();
			zos.close();
			baos.close();
			
			byte[] zip = baos.toByteArray();
			ServletOutputStream sos = response.getOutputStream();
			response.setContentType("application/zip");
			response.setHeader("Content-disposition", "attachment; filename=" + zipName);
			sos.write(zip);
			sos.flush();
			sos.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

//	
}
