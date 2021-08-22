package net.javaguides.springboot.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import net.javaguides.springboot.model.Account;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.repository.AccountRepository;
import net.javaguides.springboot.repository.EmployeeRepository;

@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private AccountRepository accountRepository;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

	public Account login(String username, String password) {
		Account account = accountRepository.findByUsername(username);
		System.out.println("account: "+ account);
		System.out.println("new pass: "+ bCryptPasswordEncoder.encode(password));
		if (account != null && bCryptPasswordEncoder.matches(password, account.getPassword())) {
			account.setPassword("");
			return account;
		}
		return null;
	}
	@Override
	public List<Account> getAllAccounts() {
		return accountRepository.findAll();
	}

	@Override
	public void saveAccount(Account acc) {
		Account account = null;
		if (acc.getId() == null) {
			acc.setPassword(bCryptPasswordEncoder.encode(acc.getPassword()));
			account = acc;
		} else {
			account = getAccountById(acc.getId());
			account.setUsername(acc.getUsername());
			account.setEmail(acc.getEmail());
		}
		this.accountRepository.save(account);
	}
	
	@Override
	public Account getAccountById(long id) {
		Optional<Account> optional = accountRepository.findById(id);
		Account acc = null;
		if (optional.isPresent()) {
			acc = optional.get();
		} else {
			throw new RuntimeException(" Acc not found for id :: " + id);
		}
		return acc;
	}

	@Override
	public void deleteAccountById(long id) {
		this.accountRepository.deleteById(id);
	}

	@Override
	public Page<Account> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
		Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
			Sort.by(sortField).descending();
		
		Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
		return this.accountRepository.findAll(pageable);
	}
	@Override
	public void deleteByUserId(Long userId) {
		int res = accountRepository.deleteByUser(userId);
	}
}
