/**
 * ErrorImportingDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * ErrorImportingDto. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
public class ErrorImportingDto implements Serializable {

    private static final long serialVersionUID = 6925231269502401153L;

    @JsonProperty("orderNumber")
    private String orderNumber;
    
    @JsonProperty("sku")
    private String sku;
    
    @JsonProperty("amount")
    private String amount;
    
    @JsonProperty("error")
    private String error;

    /**
     * get value of <b>orderNumber</b>.
     * @return the orderNumber
     */
    public String getOrderNumber() {
        return orderNumber;
    }

    /**
     * Set value to <b>orderNumber</b>.
     * @param orderNumber the orderNumber to set
     */
    public void setOrderNumber(String orderNumber) {
        this.orderNumber = orderNumber;
    }

    /**
     * get value of <b>sku</b>.
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * Set value to <b>sku</b>.
     * @param sku the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * get value of <b>amount</b>.
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * Set value to <b>amount</b>.
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }

    /**
     * get value of <b>error</b>.
     * @return the error
     */
    public String getError() {
        return error;
    }

    /**
     * Set value to <b>error</b>.
     * @param error the error to set
     */
    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return "ErrorImportingDto [orderNumber=" + orderNumber + ", sku=" + sku + ", amount=" + amount + ", error=" + error + "]";
    }
    
}
