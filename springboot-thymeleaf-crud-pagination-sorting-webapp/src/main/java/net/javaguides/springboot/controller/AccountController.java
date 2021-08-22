package net.javaguides.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import net.javaguides.springboot.model.Account;
import net.javaguides.springboot.model.Employee;
import net.javaguides.springboot.service.AccountService;
import net.javaguides.springboot.service.EmployeeService;

@Controller
//@RequestMapping(value = "/admin/user")
@CrossOrigin(origins = "*")
public class AccountController {

	@Autowired
	private AccountService accountService;
	
	@GetMapping("/admin/user/list")
	public ResponseEntity<List<Account>> getAccountList() {
		System.out.println("---------------------");
		List<Account> result = accountService.getAllAccounts();
		final ObjectMapper mapper = new ObjectMapper();
        String json = "";
		try {
			json = mapper.writeValueAsString(result);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println("................json: "+ json);
		//return new ResponseEntity<List<Account>>(accountService.getAllAccounts(), HttpStatus.OK);
		ResponseEntity<List<Account>> res = new ResponseEntity<List<Account>>(result, HttpStatus.OK);
		return res;
	}
	
	// display list of employees
	@GetMapping("/user")
	public String viewHomePage(Model model) {
		//return findPaginated(1, "firstName", "asc", model);
		return "user/index";
	}
	
	@PostMapping("/admin/user/save")
	public ResponseEntity<Void> saveAccount(@RequestBody Account acc) {
		System.out.println("name: "+ acc.getUsername() + "; mail: "+ acc.getEmail() + "; pass=" + acc.getPassword());
		  accountService.saveAccount(acc);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
	/*
	@PostMapping("/admin/user/save")
	public String saveAccount(@ModelAttribute("account") Account acc) {
		// save employee to database
		accountService.saveAccount(acc);
		return "redirect:/admin/user";
	}
	*/
	@GetMapping("/admin/user/update/{id}")
	public String showFormForUpdate(@PathVariable ( value = "id") long id, Model model) {
		
		// get employee from the service
		Account acc = accountService.getAccountById(id);
		
		// set employee as a model attribute to pre-populate the form
		model.addAttribute("account", acc);
		return "/user/index";
	}
	
	@DeleteMapping("/admin/user/delete/{id}")
	public ResponseEntity<Void> deleteAccount(@PathVariable (value = "id") long id) {
		Account acc = accountService.getAccountById(id);
		// call delete employee method 
		if (acc != null) {
			this.accountService.deleteAccountById(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		} else {
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@GetMapping("/admin/user/page/{pageNo}")
	public String findPaginated(@PathVariable (value = "pageNo") int pageNo, 
			@RequestParam("sortField") String sortField,
			@RequestParam("sortDir") String sortDir,
			Model model) {
		int pageSize = 5;
		
		Page<Account> page = accountService.findPaginated(pageNo, pageSize, sortField, sortDir);
		List<Account> listAccounts = page.getContent();
		
		model.addAttribute("currentPage", pageNo);
		model.addAttribute("totalPages", page.getTotalPages());
		model.addAttribute("totalItems", page.getTotalElements());
		
		model.addAttribute("sortField", sortField);
		model.addAttribute("sortDir", sortDir);
		model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
		
		model.addAttribute("listAccounts", listAccounts);
		return "user/index";
	}
}
