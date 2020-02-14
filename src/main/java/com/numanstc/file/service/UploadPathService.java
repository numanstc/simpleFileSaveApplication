package com.numanstc.file.service;

import java.io.File;

public interface UploadPathService {

	File getFilePath(String modifiedFileName, String path);

}
