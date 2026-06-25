package com.example.demo;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmployeeRepository {
	
	private final JdbcTemplate jdbcTemplate;
	
	public EmployeeRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	public List<Employee> findAll() {
		
		String sql = "SELECT emp_id, emp_name, department FROM employee ORDER BY emp_id";
		
		return jdbcTemplate.query(
		    sql,
	        (rs, rowNum) -> {
	        	Employee emp = new Employee();
	        	
	        	emp.setEmpId(rs.getInt("emp_id"));
	        	emp.setEmpName(rs.getString("emp_name"));
	        	emp.setDepartment(rs.getString("department"));
	        	
	        	return emp;
	        }
		);
	}
	
	public int insert(Employee emp) {
		
		String sql ="INSERT INTO employee (emp_id, emp_name, department) VALUES (?, ?, ?)";
		
		return jdbcTemplate.update(
				sql,
				emp.getEmpId(),
				emp.getEmpName(),
				emp.getDepartment()
			);
	}
	
	public int update(Employee emp) {
		
		String sql = "UPDATE employee SET emp_name = ?, department = ? WHERE emp_id = ?";
		
		return jdbcTemplate.update(
				sql,
				emp.getEmpName(),
				emp.getDepartment(),
				emp.getEmpId()
			);
	}
	
	public int delete(int empId) {
		
		String sql = "DELETE FROM employee WHERE emp_id = ?";
		
		return jdbcTemplate.update(sql, empId);
	}
	
	public Employee findById (int empId) {
		
		String sql = "SELECT emp_id, emp_name, department FROM employee WHERE emp_id = ?";
		
		return jdbcTemplate.queryForObject(
				sql,
				(rs, rowNum) -> {
					Employee emp = new Employee();
					
					emp.setEmpId(rs.getInt("emp_id"));
					emp.setEmpName(rs.getString("emp_name"));
					emp.setDepartment(rs.getString("department"));
					
					return emp;
				},
				empId
			);
	}
	
	public List<Employee> search(String empName, String department) {
		
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT emp_id, emp_name, department ");
		sql.append("FROM employee ");
		sql.append("WHERE 1 = 1 ");
		
		List<Object> params = new java.util.ArrayList<>();
		
		if (empName != null && !empName.isBlank()) {
			sql.append("AND emp_name LIKE ? ");
			params.add("%" + empName + "%");
		}
		
		if (department != null && !department.isBlank()) {
			sql.append("AND department LIKE ? ");
			params.add("%" + department + "%");
		}
		
		sql.append("ORDER BY emp_id");
		
		return jdbcTemplate.query(
			sql.toString(),
			(rs, rowNum) -> {
				Employee emp = new Employee();
				emp.setEmpId(rs.getInt("emp_id"));
				emp.setEmpName(rs.getString("emp_name"));
				emp.setDepartment(rs.getString("department"));
				return emp;
			},
			params.toArray()
		);
	}
}
