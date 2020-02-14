package com.numanstc.file.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.numanstc.file.model.UserFiles;

@Repository
public interface UserFileRepository extends CrudRepository<UserFiles, Long> {

	@Query("select f from UserFiles as f where f.user.id = ?1")
	List<UserFiles> findFilesByUserId(Long userId);

	@Modifying
	@Query("delete from UserFiles as f where f.user.id = ?1 and f.modifiedFileName in (?2) ")
	void deleteFilesByUserIdAndImageNames(Long id, List<String> removeImages);

	@Modifying
	@Query("delete from UserFiles as f where f.user.id = ?1 ")
	void deleteFilesByUserId(Long userId);

}
