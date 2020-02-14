package com.numanstc.file.service;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.servlet.ServletContext;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.numanstc.file.model.User;
import com.numanstc.file.model.UserFiles;
import com.numanstc.file.repository.UserFileRepository;
import com.numanstc.file.repository.UserRepostiory;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	private static final String Long = null;

	@Autowired
	private UserRepostiory userRepostiory;

	@Autowired
	private UploadPathService uploadPathService;

	@Autowired
	private UserFileRepository userFileRepository;

	@Autowired
	private ServletContext context;

	@Override
	public List<User> getAllUsers() {

		return (List<User>) userRepostiory.findAll();
	}

	@Override
	public User save(User user) {

		user.setCreatedDate(new Date());
		User dbUser = userRepostiory.save(user);

		if (dbUser != null && user.getFiles() != null && user.getFiles().size() > 0) {
			for (MultipartFile file : dbUser.getFiles()) {
				String fileName = file.getOriginalFilename();
				String modifiedFileName = FilenameUtils.getBaseName(fileName) + "_" + System.currentTimeMillis() + "."
						+ FilenameUtils.getExtension(fileName);

				File storeFile = uploadPathService.getFilePath(modifiedFileName, "images");

				if (storeFile != null) {
					try {
						FileUtils.writeByteArrayToFile(storeFile, file.getBytes());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				UserFiles files = new UserFiles();
				files.setFilesExtension(FilenameUtils.getExtension(fileName));
				files.setFileName(fileName);
				files.setModifiedFileName(modifiedFileName);
				files.setUser(dbUser);
				userFileRepository.save(files);

			}
		}
		return dbUser;
	}

	@Override
	public User findById(Long userId) {

		Optional<User> user = userRepostiory.findById(userId);

		if (user.isPresent()) {
			return user.get();
		}
		return null;
	}

	@Override
	public List<UserFiles> findFilesByUserId(Long userId) {

		return userFileRepository.findFilesByUserId(userId);
	}

	@Override
	public User update(User user) {

		user.setUpdatedDate(new Date());
		User dbUser = userRepostiory.save(user);

//		image remove section
		if (user != null && user.getRemoveImages() != null && user.getRemoveImages().size() > 0) {
			userFileRepository.deleteFilesByUserIdAndImageNames(user.getId(), user.getRemoveImages());
			for (String file : user.getRemoveImages()) {
				File dbFile = new File(context.getRealPath("/images/" + File.separator + file));
				if (dbFile.exists()) {
					dbFile.delete();
				}
			}

		}

//		if images added so we adding new images
		if (dbUser != null && user.getFiles() != null && user.getFiles().size() > 0) {
			for (MultipartFile file : user.getFiles()) {
				String fileName = file.getOriginalFilename();
				if (fileName.equals(""))
					continue;
				String modifiedFileName = FilenameUtils.getBaseName(fileName) + "_" + System.currentTimeMillis() + "."
						+ FilenameUtils.getExtension(fileName);

				File storeFile = uploadPathService.getFilePath(modifiedFileName, "images");

				if (storeFile != null) {
					try {
						FileUtils.writeByteArrayToFile(storeFile, file.getBytes());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}

				UserFiles files = new UserFiles();
				files.setFilesExtension(FilenameUtils.getExtension(fileName));
				files.setFileName(fileName);
				files.setModifiedFileName(modifiedFileName);
				files.setUser(dbUser);
				userFileRepository.save(files);

			}
		}
		return dbUser;
	}

	@Override
	public void deleteFilesByUserId(Long userId) {

		List<UserFiles> userFiles = userFileRepository.findFilesByUserId(userId);

		if (userFiles != null && userFiles.size() > 0) {
			for (UserFiles dbfile : userFiles) {
				File dbStoreFile = new File(context.getRealPath("/images/" + File.separator + dbfile.getModifiedFileName()));
				if(dbStoreFile.exists()) {
					dbStoreFile.delete();
				}
			}
			
		userFileRepository.deleteFilesByUserId(userId);
		
		}
	}

	@Override
	public void deleteUserById(Long userId) {

		userRepostiory.deleteById(userId);
	}

}
