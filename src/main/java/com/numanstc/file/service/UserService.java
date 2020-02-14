package com.numanstc.file.service;

import java.util.List;

import com.numanstc.file.model.User;
import com.numanstc.file.model.UserFiles;

public interface UserService {

	List<User> getAllUsers();

	User save(User user);

	User findById(Long userId);

	List<UserFiles> findFilesByUserId(Long userId);

	User update(User user);

	void deleteFilesByUserId(Long userId);

	void deleteUserById(Long userId);

}
