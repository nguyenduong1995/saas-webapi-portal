/**
 * RetailerWarehouseItemDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.dto;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

import co.ipicorp.saas.nrms.web.dto.ProductVariationDto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RetailerWarehouseItemDto. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class RetailerWarehouseItemDto extends StatusTimestampDto implements Serializable {

	private static final long serialVersionUID = -5273404741110113013L;

	@JsonProperty("retailer_id")
	private Integer retailerId;
	
	@JsonProperty("retailer_warehouse_id")
	private Integer retailerWarehouseId;
	
	@JsonProperty("product_id")
	private Integer productId;

	@JsonInclude(value = Include.NON_NULL)
	private ProductVariationDto productVariation;
	
	@JsonProperty("sku")
	private String sku;
	
	@JsonProperty("amount")
	private Integer amount;
	
    public RetailerWarehouseItemDto() {
        super();
    }

    public Integer getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Integer retailerId) {
		this.retailerId = retailerId;
	}

	public Integer getRetailerWarehouseId() {
		return retailerWarehouseId;
	}

	public void setRetailerWarehouseId(Integer retailerWarehouseId) {
		this.retailerWarehouseId = retailerWarehouseId;
	}

	public Integer getProductId() {
		return productId;
	}

	public void setProductId(Integer productId) {
		this.productId = productId;
	}

	public ProductVariationDto getProductVariation() {
		return productVariation;
	}

	public void setProductVariation(ProductVariationDto productVariation) {
		this.productVariation = productVariation;
	}

	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	@Override
    public String toString() {
        return "RetailerWarehouseItemDto [retailerId=" + retailerId + ", retailerWarehouseId=" + retailerWarehouseId 
        		+ ", productId=" + productId + ", productVariation=" + productVariation + ", sku=" + sku + ", amount=" + amount + "]";
    }

}
