package com.numanstc.file.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.numanstc.file.model.User;
import com.numanstc.file.model.UserFiles;
import com.numanstc.file.service.UserService;

@Controller
public class FileUploadController {

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String users(Model model) {

		List<User> users = userService.getAllUsers();

		model.addAttribute("users", users);
		model.addAttribute("user", new User());
		model.addAttribute("userFiles", new ArrayList<UserFiles>());
		model.addAttribute("isAdd", true);

		return "view/user";
	}

	@PostMapping("/save")
	public String save(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model) {

		User dbUser = userService.save(user);
		
		if (dbUser != null) {
			redirectAttributes.addFlashAttribute("successmessage", "User is saved saccesfully");
			return "redirect:/";
		}
		
		model.addAttribute("errormessage", "User is not save. Please try again");
		model.addAttribute("user", user);
		return "view/user";
	}
	
	@GetMapping("/edituser/{userId}")
	public String editUser(@PathVariable("userId") Long userId, Model model) {

		User user = userService.findById(userId);
		List<UserFiles> userFiles = userService.findFilesByUserId(userId);
		
		List<User> users = userService.getAllUsers();

		model.addAttribute("users", users);
		model.addAttribute("user", user);
		model.addAttribute("userfiles", userFiles);
		model.addAttribute("isAdd", false);

		return "view/user";
	}
	
	@PostMapping("/update")
	public String update(@ModelAttribute User user, RedirectAttributes redirectAttributes, Model model) {

		User dbUser = userService.update(user);
		
		if (dbUser != null) {
			redirectAttributes.addFlashAttribute("successmessage", "User is updated saccesfully");
			return "redirect:/";
		}
		
		model.addAttribute("errormessage", "User is not update. Please try again");
		model.addAttribute("user", user);
		return "view/user";
	}
	
	@GetMapping("/deleteuser/{userId}")
	public String deleteUser(@PathVariable("userId") Long userId, RedirectAttributes redirectAttributes) {
		
		userService.deleteFilesByUserId(userId);
		userService.deleteUserById(userId);
		
		redirectAttributes.addFlashAttribute("successmessage", "User is updated saccesfully");
		return "redirect:/";
	}
	
	@GetMapping("/viewuser/{userId}")
	public String viewUser(@PathVariable("userId") Long userId, Model model) {

		User user = userService.findById(userId);
		List<UserFiles> userFiles = userService.findFilesByUserId(userId);
		
		List<User> users = userService.getAllUsers();

		model.addAttribute("users", users);
		model.addAttribute("user", user);
		model.addAttribute("userfiles", userFiles);

		return "view/viewuser";
	}
	

	
}
