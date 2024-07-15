/**
 * WarehouseItemHistorySearchForm.java
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
 * WarehouseItemHistorySearchForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class WarehouseItemHistorySearchForm extends LimittedForm {

	private static final long serialVersionUID = -1353803876017964272L;

	private Integer warehouseId;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fromDate;
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate toDate;

    private String keyword;
    
    public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return "WarehouseItemHistorySearchForm [warehouseId=" + warehouseId + ", fromDate=" + fromDate + ", toDate="
				+ toDate + ", keyword=" + keyword + "]";
	}
	
}
