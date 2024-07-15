/**
 * WarehouseImportTicketItemForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * WarehouseImportTicketItemForm. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
public class WarehouseImportTicketItemForm implements Serializable {

    private static final long serialVersionUID = 2642817299525401356L;

    @JsonProperty("id")
    private Integer id;

    @JsonProperty("productId")
    private Integer productId;

    @JsonProperty("productVariationId")
    private Integer productVariationId;

    @JsonProperty("unitId")
    private Integer unitId;

    @JsonProperty("sku")
    private String sku;

    @JsonProperty("amount")
    private Integer amount = 0;

    @JsonProperty("incomePrice")
    private Double incomePrice = Double.valueOf(0);

    @JsonProperty("total")
    private Double total = Double.valueOf(0);

    /**
     * get value of <b>id</b>.
     * 
     * @return the id
     */
    public Integer getId() {
        return id;
    }

    /**
     * Set value to <b>id</b>.
     * 
     * @param id
     *            the id to set
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * get value of <b>productId</b>.
     * 
     * @return the productId
     */
    public Integer getProductId() {
        return productId;
    }

    /**
     * Set value to <b>productId</b>.
     * 
     * @param productId
     *            the productId to set
     */
    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    /**
     * get value of <b>productVariationId</b>.
     * 
     * @return the productVariationId
     */
    public Integer getProductVariationId() {
        return productVariationId;
    }

    /**
     * Set value to <b>productVariationId</b>.
     * 
     * @param productVariationId
     *            the productVariationId to set
     */
    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
    }

    /**
     * get value of <b>unitId</b>.
     * 
     * @return the unitId
     */
    public Integer getUnitId() {
        return unitId;
    }

    /**
     * Set value to <b>unitId</b>.
     * 
     * @param unitId
     *            the unitId to set
     */
    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    /**
     * get value of <b>sku</b>.
     * 
     * @return the sku
     */
    public String getSku() {
        return sku;
    }

    /**
     * Set value to <b>sku</b>.
     * 
     * @param sku
     *            the sku to set
     */
    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * get value of <b>amount</b>.
     * 
     * @return the amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * Set value to <b>amount</b>.
     * 
     * @param amount
     *            the amount to set
     */
    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    /**
     * get value of <b>incomePrice</b>.
     * 
     * @return the incomePrice
     */
    public Double getIncomePrice() {
        return incomePrice;
    }

    /**
     * Set value to <b>incomePrice</b>.
     * 
     * @param incomePrice
     *            the incomePrice to set
     */
    public void setIncomePrice(Double incomePrice) {
        this.incomePrice = incomePrice;
    }

    /**
     * get value of <b>total</b>.
     * 
     * @return the total
     */
    public Double getTotal() {
        return total;
    }

    /**
     * Set value to <b>total</b>.
     * 
     * @param total
     *            the total to set
     */
    public void setTotal(Double total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "WarehouseImportTicketItemForm [id=" + id + ", productId=" + productId + ", productVariationId="
                + productVariationId + ", unitId=" + unitId + ", sku=" + sku + ", amount=" + amount + ", incomePrice=" + incomePrice + ", total=" + total + "]";
    }

}
