package co.ipicorp.saas.portalapi.form.search;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import grass.micro.apps.web.form.validator.LimittedForm;

	/**
	 * 
	 * @author hienlt1
	 *
	 */
public class CustomerSearchForm extends LimittedForm implements Serializable{

	private static final long serialVersionUID = 4582440145914777731L;

	private String name;
	private Integer customerType;
	private String technicalEmail;
	
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate fromDate;

	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate toDate;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

//	/**
//	 * @return the status
//	 */
//	public Integer getStatus() {
//		return status;
//	}
//
//	/**
//	 * @param status the status to set
//	 */
//	public void setStatus(Integer status) {
//		this.status = status;
//	}

	/**
	 * @return the customerType
	 */
	public Integer getCustomerType() {
		return customerType;
	}

	/**
	 * @param customerType the customerType to set
	 */
	public void setCustomerType(Integer customerType) {
		this.customerType = customerType;
	}

	/**
	 * @return the technicalEmail
	 */
	public String getTechnicalEmail() {
		return technicalEmail;
	}

	/**
	 * @param technicalEmail the technicalEmail to set
	 */
	public void setTechnicalEmail(String technicalEmail) {
		this.technicalEmail = technicalEmail;
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
		return "CustomerSearchForm [name=" + name + ", customerType=" + customerType
				+ ", technicalEmail=" + technicalEmail + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
	}
	
}
