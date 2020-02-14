package com.numanstc.file.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.numanstc.file.model.User;

@Repository
public interface UserRepostiory extends CrudRepository<User, Long> {

}
