package org.cateproject.repository.jpa.auth;
import org.cateproject.domain.auth.UserAccount;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long>,  JpaSpecificationExecutor<UserAccount>{
	UserAccount findByUsername(String username);
	
	Page<UserAccount> findByUsernameStartingWith(String username, Pageable pageable);
}
