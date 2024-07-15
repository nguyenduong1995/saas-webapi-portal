/**
 * PerformanceSearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * PerformanceSearchForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class PerformanceSearchForm implements Serializable {

	private static final long serialVersionUID = -8745556561999492579L;

	private Integer retailerId;
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fromDate;
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate toDate;

	public Integer getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Integer retailerId) {
		this.retailerId = retailerId;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	@Override
    public String toString() {
        return "PerformanceSearchForm [retailerId=" + retailerId + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
    }
	
}
