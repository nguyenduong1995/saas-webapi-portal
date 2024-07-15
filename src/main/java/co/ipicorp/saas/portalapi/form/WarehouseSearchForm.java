/**
 * WarehouseSearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * WarehouseSearchForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class WarehouseSearchForm extends LimittedForm implements Serializable {

	private static final long serialVersionUID = 594315854091247368L;
	
	private Integer warehouseId;
	
    private Integer warehouseTypeId;

    private Integer status;
    
    private String keyword;
	
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
     * get value of <b>warehouseTypeId</b>.
     * @return the warehouseTypeId
     */
	public Integer getWarehouseTypeId() {
		return warehouseTypeId;
	}

	/**
     * Set value to <b>warehouseTypeId</b>.
     * @param warehouseTypeId the warehouseTypeId to set
     */
	public void setWarehouseTypeId(Integer warehouseTypeId) {
		this.warehouseTypeId = warehouseTypeId;
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
		return "WarehouseSearchForm [warehouseId=" + warehouseId + ", warehouseTypeId=" + warehouseTypeId + ", status="
				+ status + ", keyword=" + keyword + "]";
	}
	
}
