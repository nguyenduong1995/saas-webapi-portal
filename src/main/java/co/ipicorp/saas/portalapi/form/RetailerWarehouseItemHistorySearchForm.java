/**
 * RetailerWarehouseItemHistorySearchForm.java
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
 * RetailerWarehouseItemHistorySearchForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class RetailerWarehouseItemHistorySearchForm implements Serializable {

	private static final long serialVersionUID = -5285132386139038887L;

	private Integer retailerId;

    private Integer segment;

    private Integer offset;
    
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
	
	public Integer getSegment() {
		return segment;
	}

	public void setSegment(Integer segment) {
		this.segment = segment;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
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
        return "WarehouseItemHistorySearchForm [retailerId=" + retailerId + ", segment=" + segment 
        		+ ", offset=" + offset + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
    }
	
}
