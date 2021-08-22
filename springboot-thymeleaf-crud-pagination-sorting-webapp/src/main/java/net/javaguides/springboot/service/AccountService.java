package net.javaguides.springboot.service;

import java.util.List;

import org.springframework.data.domain.Page;

import net.javaguides.springboot.model.Account;
import net.javaguides.springboot.model.Employee;

public interface AccountService {
	List<Account> getAllAccounts();
	void saveAccount(Account acc);
	Account getAccountById(long id);
	void deleteAccountById(long id);
	Page<Account> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
	public Account login(String username, String password);
	public void deleteByUserId(Long userId);
}
