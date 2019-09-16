package com.dandi.api;

import org.springframework.stereotype.Repository;
import org.springframework.data.repository.CrudRepository;

@Repository
public interface UserRepository extends CrudRepository<User, Long>{

	User findByUserId(Long userId);
	User findByUserNameAndPassword(String userName, String password);
	User findByUserName(String userName);
}
