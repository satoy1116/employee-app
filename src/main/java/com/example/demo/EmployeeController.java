package com.example.demo;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
public class EmployeeController {

	private final EmployeeRepository employeeRepository;
	
	public EmployeeController(EmployeeRepository empolyeeRepository) {
		this.employeeRepository = empolyeeRepository;
	}
	
	@GetMapping("/employees")
	public String list(
	        String empName,
	        String department,
	        Integer page,
	        String sort,
	        Model model) {
	  
	  int currentPage = 1;
	  int pageSize = 5;
	  
	  if (sort == null || sort.isBlank()) {
		  sort = "empId";
	  }
	  
	  if (page != null && page > 0) {
		  currentPage = page;
	  }
	  
	  int totalCount = employeeRepository.count(empName, department);
	  int totalPages = (int) Math.ceil((double) totalCount / pageSize);
	        
	  List<Employee> employees = employeeRepository.searchWithPaging(
			  empName,
			  department,
			  currentPage,
			  pageSize,
			  sort);
	  
	  model.addAttribute("employees", employees);
	  model.addAttribute("empName", empName);
	  model.addAttribute("department", department);
	  model.addAttribute("currentPage", currentPage);
	  model.addAttribute("totalCount", totalCount);
	  model.addAttribute("totalPages", totalPages);
	  model.addAttribute("sort", sort);
	  
	  return "employees";
	}
	
	@GetMapping("/employees/new")
	public String newEmployee(Model model) {
		model.addAttribute("employee", new Employee());
		return "employee-form";
	}
	
	@GetMapping("/employees/edit/{empId}")
	public String edit(
			@PathVariable int empId,
			String empName,
			String department,
			Integer page,
			String sort,
			Model model) {
		
		Employee employee = employeeRepository.findById(empId);
		
		model.addAttribute("employee", employee);
		
		model.addAttribute("empName", empName);
		model.addAttribute("department", department);
		model.addAttribute("page", page);
		model.addAttribute("sort", sort);
		
		return "employee-edit";
	}
	
	@PostMapping("/employees")
	public String add(
			@Valid Employee emp,
			BindingResult bindingResult,
			Model model,
			RedirectAttributes redirectAttributes) {
	  
	  if (bindingResult.hasErrors()) {
		  model.addAttribute("employee", emp);
		  return "employee-form";
	  }
	  
	  if (employeeRepository.existsById(emp.getEmpId())) {
		  bindingResult.rejectValue("empId", "duplicate", "この社員IDは登録されています。");
		  
		  return "employee-form";
	  }
	  
	  employeeRepository.insert(emp);
	  
	  redirectAttributes.addFlashAttribute("message", "社員を登録しました。");
	  
	  return "redirect:/employees";
	}
	
	@PostMapping("/employees/update")
	public String updateFromForm(
	        @Valid Employee emp,
	        BindingResult bindingResult,
	        String searchEmpName,
	        String searchDepartment,
	        Integer page,
	        String sort,
	        Model model,
	        RedirectAttributes redirectAttributes) {
	  
	  if (bindingResult.hasErrors()) {
	    model.addAttribute("employee", emp);
	    model.addAttribute("empName", searchEmpName);
	    model.addAttribute("department", searchDepartment);
	    model.addAttribute("page", page);
	    model.addAttribute("sort", sort);
	    return "employee-edit";
	  }
	  
	  employeeRepository.update(emp);
	  
	  redirectAttributes.addFlashAttribute("message", "社員情報を更新しました。");
	  redirectAttributes.addAttribute("empName", searchEmpName);
	  redirectAttributes.addAttribute("department", searchDepartment);
	  redirectAttributes.addAttribute("page", page);
	  redirectAttributes.addAttribute("sort", sort);
	  
	  return "redirect:/employees";
	}
	
	@PostMapping("/employees/delete/{empId}")
	public String deleteFromForm(
			@PathVariable int empId,
			String searchEmpName,
			String searchDepartment,
			Integer currentPage,
			String sort,
			RedirectAttributes redirectAttributes) {
		
		employeeRepository.delete(empId);
		
		redirectAttributes.addFlashAttribute("message", "社員を削除しました。");
		redirectAttributes.addAttribute("empName", searchEmpName);
		redirectAttributes.addAttribute("department", searchDepartment);
		redirectAttributes.addAttribute("page", currentPage);
		redirectAttributes.addAttribute("sort", sort);
		
		return "redirect:/employees";
	}
	
	@ResponseBody
	@PutMapping("/employees/{empId}")
	public String update(
			@PathVariable int empId,
			@RequestBody Employee emp) {
		
		emp.setEmpId(empId);
		
		int count = employeeRepository.update(emp);
		
		return count + "件更新しました。";
	}
	
	@ResponseBody
	@DeleteMapping("/employees/{empId}")
	public String delete(@PathVariable int empId) {
		
		int count = employeeRepository.delete(empId);
		
		return count + "件削除しました。";
	}
}
