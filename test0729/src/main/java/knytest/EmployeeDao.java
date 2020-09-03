package knytest;

import org.springframework.stereotype.Repository;

public class EmployeeDao {

    public int insert(final Employee emp) {
    	System.out.println(emp.getName() + "직원이 등록 됩니다.");
		return 1;
	}
}
