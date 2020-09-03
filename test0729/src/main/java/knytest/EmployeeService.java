package knytest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


public class EmployeeService {
	@Autowired
	private EmployeeDao empDao;

	public void register(Employee newEmployee) {
		empDao.insert(newEmployee);		
	}
}
