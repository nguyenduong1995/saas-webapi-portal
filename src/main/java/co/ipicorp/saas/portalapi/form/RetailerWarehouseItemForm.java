/**
 * RetailerWarehouseItemForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RetailerWarehouseItemForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class RetailerWarehouseItemForm implements Serializable {

	private static final long serialVersionUID = 594315854091247368L;
	
	@JsonProperty("retailer_id")
    private Integer retailerId;
    
    public Integer getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Integer retailerId) {
		this.retailerId = retailerId;
	}

	@Override
    public String toString() {
        return "RetailerWarehouseItemForm [retailerId=" + retailerId + "]";
    }
	
}
