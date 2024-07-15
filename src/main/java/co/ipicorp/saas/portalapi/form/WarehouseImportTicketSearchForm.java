/**
 * WarehouseImportTicketSearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.time.LocalDate;

import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * WarehouseImportTicketSearchForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class WarehouseImportTicketSearchForm extends LimittedForm {

	private static final long serialVersionUID = 7490441246911957214L;

	private Integer warehouseImportTicketId;
	   
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fromDate = LocalDate.now().minusDays(7);
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate toDate = LocalDate.now();
    
    private Integer warehouseId;

    private Integer status;
    
    private String keyword;
	
    /**
     * get value of <b>warehouseImportTicketId</b>.
     * @return the warehouseImportTicketId
     */
	public Integer getWarehouseImportTicketId() {
		return warehouseImportTicketId;
	}

	/**
     * Set value to <b>warehouseImportTicketId</b>.
     * @param warehouseImportTicketId the warehouseImportTicketId to set
     */
	public void setWarehouseImportTicketId(Integer warehouseImportTicketId) {
		this.warehouseImportTicketId = warehouseImportTicketId;
	}
	
    /**
     * get value of <b>fromDate</b>.
     * @return the fromDate
     */
    public LocalDate getFromDate() {
		return fromDate;
	}

    /**
     * Set value to <b>fromDate</b>.
     * @param fromDate the fromDate to set
     */
	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	/**
     * get value of <b>toDate</b>.
     * @return the toDate
     */
	public LocalDate getToDate() {
		return toDate;
	}

	/**
     * Set value to <b>toDate</b>.
     * @param toDate the toDate to set
     */
	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	/**
     * get value of <b>warehouseId</b>.
     * @return the warehouseId
     */
	public Integer getWarehouseId() {
		return warehouseId;
	}

	/**
     * Set value to <b>warehouseId</b>.
     * @param warehouseId the warehouseId to set
     */
	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	/**
     * get value of <b>status</b>.
     * @return the status
     */
	public Integer getStatus() {
		return status;
	}

	/**
     * Set value to <b>status</b>.
     * @param status the status to set
     */
	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return "WarehouseImportTicketSearchForm [warehouseImportTicketId=" + warehouseImportTicketId + ", fromDate="
				+ fromDate + ", toDate=" + toDate + ", warehouseId=" + warehouseId + ", status=" + status + ", keyword="
				+ keyword + "]";
	}
	
}
