package net.javaguides.springboot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import net.javaguides.springboot.model.Account;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long>{
	//@Query("Select u from User u WHERE u.username=:username")
    //Account findByUsername(@Param("username") String username);
	Account findByUsername(String username);
	
	@Transactional
	@Modifying
    @Query(value = "DELETE FROM role_user WHERE USER_ID = :user_id", nativeQuery = true)       // it will delete all the record with specific name
    int deleteByUser(@Param("user_id") Long user_id);
}
