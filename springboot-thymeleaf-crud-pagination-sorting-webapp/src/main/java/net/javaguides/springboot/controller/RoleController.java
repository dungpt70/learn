package net.javaguides.springboot.controller;

import java.util.ArrayList;
import java.util.HashMap;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import net.javaguides.springboot.model.Account;
import net.javaguides.springboot.model.Role;
import net.javaguides.springboot.service.AccountService;
import net.javaguides.springboot.service.RoleService;

@Controller
@RequestMapping(value = "/admin/role")
@CrossOrigin(origins = "*")
public class RoleController {

	@Autowired
	private RoleService roleService;
	
	@Autowired
	private AccountService accountService;
	
	@GetMapping("/list")
	public ResponseEntity<List<Role>> getRoleList() {
		System.out.println("---------------------");
		List<Role> result = roleService.getAllRoles();
		System.out.println("-----------role----------result = " + result);
		System.out.println("-----------role----------result size= " + (result != null?result.size():result));
		//return new ResponseEntity<List<Account>>(accountService.getAllAccounts(), HttpStatus.OK);
		ResponseEntity<List<Role>> res = new ResponseEntity<List<Role>>(result, HttpStatus.OK);
		System.out.println("---------------------res = " + res);
		return res;
	}
	/*
	 * ,
			@RequestParam(value = "id") Long id,
			@RequestParam(value = "roleids") Long[] roleids
	 * */
	@RequestMapping(path = "/config", method = RequestMethod.POST)
	public ResponseEntity<Void> configRoles(
			@RequestBody HashMap<String, Object> map) {
		String suserid = map.get("id").toString();
		Long userid = 0L;
		try {
			userid = Long.parseLong(suserid);
		}catch(Exception e) {
			
		}
		Account acc = accountService.getAccountById(userid);
		String roleids = (String)map.get("roleids");
		roleids = (roleids==null)?"":roleids;
		String[] aRoleIds = roleids.split(",");
		accountService.deleteByUserId(userid);
		for(String roleid : aRoleIds) {
			Long lroleid = 0L;
			try {
				lroleid = Long.parseLong(roleid);
			}catch(Exception e) {
				
			}
			Role ru = roleService.findById(lroleid);
			acc.getLikedRoles().add(ru);
		}
		//roleService.saveMulti(list);
		accountService.saveAccount(acc);
		return new ResponseEntity<Void>(HttpStatus.OK);
	}
}
