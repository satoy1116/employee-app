package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;


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
	        Model model) {
	        
	  List<Employee> employees = employeeRepository.search(empName, department);
	  
	  model.addAttribute("employees", employees);
	  model.addAttribute("empName", empName);
	  model.addAttribute("department", department);
	  
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
			Model model	) {
		
		Employee employee = employeeRepository.findById(empId);
		
		model.addAttribute("employee", employee);
		
		return "employee-edit";
	}
	
	@PostMapping("/employees")
	public String add(Employee emp) {
		
		employeeRepository.insert(emp);
		
		return "redirect:/employees";
	}
	
	@PostMapping("/employees/update")
	public String updateFromForm(Employee emp) {
		
		employeeRepository.update(emp);
		
		return "redirect:/employees";
	}
	
	@PostMapping("/employees/delete/{empId}")
	public String deleteFromForm(@PathVariable int empId) {
		
		employeeRepository.delete(empId);
		
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
