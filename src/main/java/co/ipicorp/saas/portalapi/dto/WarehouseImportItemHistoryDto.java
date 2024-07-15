/**
 * WarehouseImportItemHistoryDto.java
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
 * WarehouseImportItemHistoryDto. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class WarehouseImportItemHistoryDto implements Serializable {

	private static final long serialVersionUID = 1795944489263613210L;
	
	@JsonProperty("id")
    private Integer id;
	
	@JsonProperty("warehouseId")
    private Integer warehouseId;
	
	@JsonProperty("sku")
    private String sku;
	
    @JsonProperty("importTicketCode")
    private String importTicketCode;
	
    @JsonProperty("importDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime importDate;

    @JsonProperty("amount")
    private Integer amount;

    @JsonProperty("productVariationCode")
    private String productVariationCode;
    
    @JsonProperty("productVariationName")
    private String productVariationName;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("unitId")
    private Integer unitId;

    @JsonProperty("personInCharge")
    private String personInCharge;
    
    /**
     * 
     */
    public WarehouseImportItemHistoryDto() {
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
     * get value of <b>importTicketCode</b>.
     * @return the importTicketCode
     */
	public String getImportTicketCode() {
		return importTicketCode;
	}

	/**
     * Set value to <b>importTicketCode</b>.
     * @param importTicketCode the importTicketCode to set
     */
	public void setImportTicketCode(String importTicketCode) {
		this.importTicketCode = importTicketCode;
	}

	/**
     * get value of <b>importDate</b>.
     * @return the importDate
     */
	public LocalDateTime getImportDate() {
		return importDate;
	}

	/**
     * Set value to <b>importDate</b>.
     * @param importDate the importDate to set
     */
	public void setImportDate(LocalDateTime importDate) {
		this.importDate = importDate;
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
        return "WarehouseImportItemHistoryDto [id=" + id + ", warehouseId=" + warehouseId + ", sku=" + sku + ", importTicketCode=" + importTicketCode
                + ", importDate=" + importDate + ", amount=" + amount + ", productVariationCode=" + productVariationCode + ", productVariationName="
                + productVariationName + ", description=" + description + ", unitId=" + unitId + ", personInCharge=" + personInCharge + "]";
    }
    
}
