package dto;

public class UserDTO {
	
	private String deptment;
	private String name;
	private double gpa;
	private int interview_score;
	private int test_score;
	private String dpa_yn;
	private String pass_yn;
	private String fail_reason;

	public String getDeptment() {
		return deptment;
	}

	public void setDeptment(String deptment) {
		this.deptment = deptment;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getGpa() {
		return gpa;
	}

	public void setGpa(double gpa) {
		this.gpa = gpa;
	}

	public int getInterview_score() {
		return interview_score;
	}

	public void setInterview_score(int interview_score) {
		this.interview_score = interview_score;
	}

	public int getTest_score() {
		return test_score;
	}

	public void setTest_score(int test_score) {
		this.test_score = test_score;
	}

	public String getDpa_yn() {
		return dpa_yn;
	}

	public void setDpa_yn(String dpa_yn) {
		this.dpa_yn = dpa_yn;
	}

	public String getPass_yn() {
		return pass_yn;
	}

	public void setPass_yn(String pass_yn) {
		this.pass_yn = pass_yn;
	}

	public String getFail_reason() {
		return fail_reason;
	}

	public void setFail_reason(String fail_reason) {
		this.fail_reason = fail_reason;
	}
	
	
}
