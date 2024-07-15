package co.ipicorp.saas.portalapi.form.search;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import grass.micro.apps.web.form.validator.LimittedForm;

public class CustomerRegistrationSearchForm extends LimittedForm implements Serializable{
	
	private static final long serialVersionUID = 4582440002314777731L;

	private String email;
	private Integer status;
	private Integer companyId;
	private String telephone;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate fromDate;

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate toDate;

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @param email the email to set
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return the status
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * @return the company
	 */
	public Integer getCompanyId() {
		return companyId;
	}

	/**
	 * @param company the company to set
	 */
	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}

	/**
	 * @return the telephone
	 */
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone the telephone to set
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return the fromDate
	 */
	public LocalDate getFromDate() {
		return fromDate;
	}

	/**
	 * @param fromDate the fromDate to set
	 */
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	/**
	 * @return the toDate
	 */
	public LocalDate getToDate() {
		return toDate;
	}

	/**
	 * @param toDate the toDate to set
	 */
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CustomerRegistrationSearchForm [email=" + email + ", status=" + status + ", companyId=" + companyId
				+ ", telephone=" + telephone + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
	}

}
