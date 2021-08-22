package net.javaguides.springboot.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import net.javaguides.springboot.model.Account;
import net.javaguides.springboot.model.AdminLoginDto;
import net.javaguides.springboot.service.AccountService;

@Controller
public class AdminLoginController {
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private HttpSession session;
	@GetMapping("login")
	public String login(ModelMap model) {
		model.addAttribute("account", new AdminLoginDto());
		return "login";
	}
	
	@PostMapping("login")
	public ModelAndView login(ModelMap model, 
			@Valid @ModelAttribute("account") AdminLoginDto dto,
			BindingResult result
			) {
		if (result.hasErrors()) {
			//List<ObjectError> errors = result.getAllErrors();
			List<FieldError> errors = result.getFieldErrors();
	        List<String> message = new ArrayList<>();
	        
			return new ModelAndView("login",model);
		}
		Account account = accountService.login(dto.getUsername(), dto.getPassword());
		if (account == null) {
			model.addAttribute("message", "Invalid username or password");
			return new ModelAndView("login", model);
		}
		Object ruri = session.getAttribute("redirect-url");
		session.setAttribute("username", account.getUsername());
		session.setAttribute("user", account);
		/*
		if (ruri != null) {
			session.removeAttribute("redirect-url");
			return new ModelAndView("redirect:/" + ruri);
		} else {
			return new ModelAndView("redirect:/");
		}
		*/
		return new ModelAndView("redirect:/user");
	}
	@PostMapping("logout")
	public ModelAndView logout(ModelMap model
			) {
		session.removeAttribute("username");
		session.removeAttribute("user");
		return new ModelAndView("redirect:/login");
	}
}
