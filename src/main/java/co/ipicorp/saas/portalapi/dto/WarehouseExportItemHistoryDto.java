/**
 * WarehouseExportItemHistoryDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.dto;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * WarehouseExportItemHistoryDto. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class WarehouseExportItemHistoryDto implements Serializable {

	private static final long serialVersionUID = 1795944489263613210L;

	@JsonProperty("id")
    private Integer id;
	
	@JsonProperty("warehouseId")
    private Integer warehouseId;
	
    @JsonProperty("exportDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime exportDate;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("sku")
    private String sku;
    
    @JsonProperty("productVariationCode")
    private String productVariationCode;
    
    @JsonProperty("productVariationName")
    private String productVariationName;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("unitId")
    private Integer unitId;

    @JsonProperty("deliveryAddress")
    private String deliveryAddress;
    
    @JsonProperty("orderCode")
    private String orderCode;
    
    @JsonProperty("personInCharge")
    private String personInCharge;
    
    /**
     * 
     */
    public WarehouseExportItemHistoryDto() {
        super();
        // TODO Auto-generated constructor stub
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
     * get value of <b>exportDate</b>.
     * @return the exportDate
     */
	public LocalDateTime getExportDate() {
		return exportDate;
	}

	/**
     * Set value to <b>exportDate</b>.
     * @param exportDate the exportDate to set
     */
	public void setExportDate(LocalDateTime exportDate) {
		this.exportDate = exportDate;
	}

	/**
     * get value of <b>amount</b>.
     * @return the amount
     */
	public Integer getAmount() {
		return amount;
	}

	/**
     * Set value to <b>amount</b>.
     * @param amount the amount to set
     */
	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	/**
     * get value of <b>productVariationCode</b>.
     * @return the productVariationCode
     */
	public String getProductVariationCode() {
		return productVariationCode;
	}

	/**
     * Set value to <b>productVariationCode</b>.
     * @param productVariationCode the productVariationCode to set
     */
	public void setProductVariationCode(String productVariationCode) {
		this.productVariationCode = productVariationCode;
	}

	/**
     * get value of <b>productVariationName</b>.
     * @return the productVariationName
     */
	public String getProductVariationName() {
		return productVariationName;
	}

	/**
     * Set value to <b>productVariationName</b>.
     * @param productVariationName the productVariationName to set
     */
	public void setProductVariationName(String productVariationName) {
		this.productVariationName = productVariationName;
	}

	/**
     * get value of <b>description</b>.
     * @return the description
     */
	public String getDescription() {
		return description;
	}

	/**
     * Set value to <b>description</b>.
     * @param description the description to set
     */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
     * get value of <b>unitId</b>.
     * @return the unitId
     */
	public Integer getUnitId() {
		return unitId;
	}

	/**
     * Set value to <b>unitId</b>.
     * @param unitId the unitId to set
     */
	public void setUnitId(Integer unitId) {
		this.unitId = unitId;
	}

	/**
     * get value of <b>personInCharge</b>.
     * @return the personInCharge
     */
	public String getPersonInCharge() {
		return personInCharge;
	}

	/**
     * Set value to <b>personInCharge</b>.
     * @param personInCharge the personInCharge to set
     */
	public void setPersonInCharge(String personInCharge) {
		this.personInCharge = personInCharge;
	}

	/**
     * get value of <b>deliveryAddress</b>.
     * @return the deliveryAddress
     */
	public String getDeliveryAddress() {
		return deliveryAddress;
	}

	/**
     * Set value to <b>deliveryAddress</b>.
     * @param deliveryAddress the deliveryAddress to set
     */
	public void setDeliveryAddress(String deliveryAddress) {
		this.deliveryAddress = deliveryAddress;
	}

	/**
     * get value of <b>orderCode</b>.
     * @return the orderCode
     */
	public String getOrderCode() {
		return orderCode;
	}

	/**
     * Set value to <b>orderCode</b>.
     * @param orderCode the orderCode to set
     */
	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	/**
     * get value of <b>id</b>.
     * @return the id
     */
	public Integer getId() {
		return id;
	}

	/**
     * Set value to <b>id</b>.
     * @param id the id to set
     */
	public void setId(Integer id) {
		this.id = id;
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

    @Override
    public String toString() {
        return "WarehouseExportItemHistoryDto [id=" + id + ", warehouseId=" + warehouseId + ", exportDate=" + exportDate + ", amount=" + amount + ", sku=" + sku
                + ", productVariationCode=" + productVariationCode + ", productVariationName=" + productVariationName + ", description=" + description
                + ", unitId=" + unitId + ", deliveryAddress=" + deliveryAddress + ", orderCode=" + orderCode + ", personInCharge=" + personInCharge + "]";
    }
    
}