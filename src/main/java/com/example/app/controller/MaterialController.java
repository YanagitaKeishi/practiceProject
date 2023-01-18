package com.example.app.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.app.domain.Material;
import com.example.app.service.MaterialService;

@Controller
@RequestMapping("/admin/material")
public class MaterialController {
	
	@Autowired
	MaterialService service;
	
	@GetMapping("/list")
	public String list(@RequestParam(name="status",required=false)String status, Model model) throws Exception {
		model.addAttribute("materialList", service.getMaterialList());
		model.addAttribute("statusMessage", getStatusMessage(status));
		return "admin/list-material";
	}
	
	@GetMapping("/show/{id}")
	public String show(
			@PathVariable Integer id, Model model) throws Exception {
		model.addAttribute("material", service.getMaterialById(id));
		return "admin/show-material";
	}
	
	@GetMapping("/add")
	public String addGet(Model model) throws Exception {
		model.addAttribute("material", new Material());
		model.addAttribute("typeList", service.getMaterialTypeList());
		return "admin/add-material";
	}
	
	@PostMapping("/add")
	public String addPost(@Valid Material material, Errors errors,
								Model model) throws Exception{
		if(errors.hasErrors()) {
			model.addAttribute("typeList", service.getMaterialTypeList());
			return "admin/add-material";
		}
		
		service.addMaterial(material);
		return "redirect:/admin/material/list?status=add";
	}
	
	@GetMapping("/delete/{id}")
	public String delete(@PathVariable Integer id) throws Exception{
		service.deleteMaterial(id);
		return  "redirect:/admin/material/list?status=delete";
	}
	
	@GetMapping("/edit/{id}")
	public String getEdit(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("material",service.getMaterialById(id));
		model.addAttribute("typeList", service.getMaterialTypeList());
		return "admin/edit-material";
	}
	
	@PostMapping("/edit/{id}")
	public String postEdit(
			@PathVariable Integer id,
			@Valid Material material,
			Errors errors,
			Model model) throws Exception{
		if(errors.hasErrors()) {
			model.addAttribute("typeList", service.getMaterialTypeList());
			return "admin/edit-material";
		}
		
		service.editMaterial(material);
		return "redirect:/admin/material/list?status=edit";
	}
	
	private String getStatusMessage(String status) {
		String message = null;
		if(status == null) {
			return message;
		}
		switch(status) {
		case "add":
			message = "教材を追加しました";
			break;
		case "edit":
			message = "教材情報を更新しました";
			break;
		case "delete":
			message = "教材情報を削除しました";
			break;
		}
		return message;
	}
}
