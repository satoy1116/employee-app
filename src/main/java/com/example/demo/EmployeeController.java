package com.example.demo;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmployeeController {

	private final EmployeeRepository employeeRepository;
	
	public EmployeeController(EmployeeRepository empolyeeRepository) {
		this.employeeRepository = empolyeeRepository;
	}
	
	@GetMapping("/employees")
	public List<Employee> findAll() {
		return employeeRepository.findAll();
	}
	
	@PostMapping("/employees")
	public String add(@RequestBody Employee emp) {
		
		int count = employeeRepository.insert(emp);
		
		return count + "件登録しました。";
	}
	
	@PutMapping("/employees/{empId}")
	public String update(
			@PathVariable int empId,
			@RequestBody Employee emp) {
		
		emp.setEmpId(empId);
		
		int count = employeeRepository.update(emp);
		
		return count + "件更新しました。";
	}
	
	@DeleteMapping("/employees/{empId}")
	public String delete(@PathVariable int empId) {
		
		int count = employeeRepository.delete(empId);
		
		return count + "件削除しました。";
	}
}
