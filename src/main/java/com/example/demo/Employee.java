package com.example.demo;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public class Employee {
	
	@NotNull(message = "社員IDを入力してください。")
	@Min(value = 1, message = "社員IDは1以上で入力してください。")
	private Integer empId;
	
	@NotBlank(message = "社員名を入力してください。")
	private String empName;
	
	@NotBlank(message = "部署を入力してください。")
	private String department;
	
	public Integer getEmpId() {
		return empId;
	}
	public void setEmpId(Integer empId) {
		this.empId = empId;
	}
	
	public String getEmpName() {
		return empName;
	}
	public void setEmpName(String empName) {
		this.empName = empName;
	}
	
	public String getDepartment() {
		return department;
	}
	public void setDepartment(String department) {
		this.department = department;
	}
}
