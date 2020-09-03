package knytest;

import java.util.Date;

import javax.validation.constraints.Past;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

public class Employee {
	private String name;
	@Size(min=4, max=4, message="사원번호는 4자리로 입력하세요.")
	private String number;
	private String addr1;
	private String addr2;
	@Size(min=5, max=7, message="우편번호는 5자리 이상 7자리 이하만 가능합니다.")
	private String zipCode;
	@Size(max=1999, message="태어난 연도는 2000년 이전만 가능합니다.")
	private int birtyYear;
	private String teamId;
	@Past(message="입사일은 과거 날짜만 가능합니다.")
	@DateTimeFormat(pattern="yyyy-MM-dd")
	private Date joinedDate;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	public String getAddr1() {
		return addr1;
	}
	public void setAddr1(String addr1) {
		this.addr1 = addr1;
	}
	public String getAddr2() {
		return addr2;
	}
	public void setAddr2(String addr2) {
		this.addr2 = addr2;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public int getBirtyYear() {
		return birtyYear;
	}
	public void setBirtyYear(int birtyYear) {
		this.birtyYear = birtyYear;
	}
	public String getTeamId() {
		return teamId;
	}
	public void setTeamId(String teamId) {
		this.teamId = teamId;
	}
	public Date getJoinedDate() {
		return joinedDate;
	}
	public void setJoinedDate(Date joinedDate) {
		this.joinedDate = joinedDate;
	}
	@Override
	public String toString() {
		return "Employee [name=" + name + ", number=" + number + ", addr1=" + addr1 + ", addr2=" + addr2 + ", zipCode="
				+ zipCode + ", birtyYear=" + birtyYear + ", teamId=" + teamId + ", joinedDate=" + joinedDate + "]";
	}
}
