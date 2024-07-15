/**
 * WarehouseDailyRemainDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDate;

import grass.micro.apps.model.dto.TimestampDto;

/**
 * WarehouseDailyRemainDto.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
public class WarehouseDailyRemainDto extends TimestampDto implements Serializable {
    
    private static final long serialVersionUID = -6911384410579717770L;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("id")
    private Integer id;
    
    @JsonProperty("logDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate logDate;
    
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @JsonProperty("warehouseId")
    private Integer warehouseId;
    
    @JsonProperty("productId")
    private Integer productId;
    
    @JsonProperty("productVariationId")
    private Integer productVariationId;
    
    @JsonProperty("sku")
    private String sku;
    
    @JsonProperty("amountApprovedOrder")
    private Integer amountApprovedOrder;
    
    @JsonProperty("amountAvailable")
    private Integer amountAvailable;
    
    @JsonProperty("amountToday")
    private Integer amountToday;
    
    @JsonProperty("amountImport")
    private Integer amountImport;
    
    @JsonProperty("amountExport")
    private Integer amountExport;
    
    @JsonProperty("amount")
    private Integer amount;
    
    @JsonProperty("productCode")
    private String productCode;
    
    @JsonProperty("productName")
    private String productName;
    
    @JsonProperty("unitId")
    private Integer unitId;
    
    @JsonProperty("unitName")
    private String unitName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public LocalDate getLogDate() {
        return logDate;
    }

    public void setLogDate(LocalDate logDate) {
        this.logDate = logDate;
    }

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getProductVariationId() {
        return productVariationId;
    }

    public void setProductVariationId(Integer productVariationId) {
        this.productVariationId = productVariationId;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    /**
     * get value of <b>amountApprovedOrder</b>.
     * @return the amountApprovedOrder
     */
    public Integer getAmountApprovedOrder() {
        return amountApprovedOrder;
    }

    /**
     * Set value to <b>amountApprovedOrder</b>.
     * @param amountApprovedOrder the amountApprovedOrder to set
     */
    public void setAmountApprovedOrder(Integer amountApprovedOrder) {
        this.amountApprovedOrder = amountApprovedOrder;
    }

    /**
     * get value of <b>amountAvailable</b>.
     * @return the amountAvailable
     */
    public Integer getAmountAvailable() {
        return amountAvailable;
    }

    /**
     * Set value to <b>amountAvailable</b>.
     * @param amountAvailable the amountAvailable to set
     */
    public void setAmountAvailable(Integer amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public Integer getAmountToday() {
        return amountToday;
    }

    public void setAmountToday(Integer amountToday) {
        this.amountToday = amountToday;
    }

    public Integer getAmountImport() {
        return amountImport;
    }

    public void setAmountImport(Integer amountImport) {
        this.amountImport = amountImport;
    }

    public Integer getAmountExport() {
        return amountExport;
    }

    public void setAmountExport(Integer amountExport) {
        this.amountExport = amountExport;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public Integer getUnitId() {
        return unitId;
    }

    public void setUnitId(Integer unitId) {
        this.unitId = unitId;
    }

    public String getUnitName() {
        return unitName;
    }

    public void setUnitName(String unitName) {
        this.unitName = unitName;
    }

    @Override
    public String toString() {
        return "WarehouseDailyRemainDto [id=" + id + ", logDate=" + logDate + ", warehouseId=" + warehouseId + ", productId=" + productId
                + ", productVariationId=" + productVariationId + ", sku=" + sku + ", amountApprovedOrder=" + amountApprovedOrder + ", amountAvailable="
                + amountAvailable + ", amountToday=" + amountToday + ", amountImport=" + amountImport + ", amountExport=" + amountExport + ", amount=" + amount
                + ", productCode=" + productCode + ", productName=" + productName + ", unitId=" + unitId + ", unitName=" + unitName + "]";
    }

}
