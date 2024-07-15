/**
 * WarehouseImportTicketForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * WarehouseImportTicketForm. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
public class WarehouseImportTicketForm implements Serializable{

    private static final long serialVersionUID = 4710386923270830112L;
    
    @JsonProperty("warehouseId")
    private Integer warehouseId;
    
    @JsonProperty("importType")
    private Integer importType;
    
    @JsonProperty("description")
    private String description;
    
    @JsonProperty("extraData")
    private LinkedHashMap<String, Object> extraData = new LinkedHashMap<>();
    
    @JsonProperty("warehouseImportTicketItems")
    private List<WarehouseImportTicketItemForm> warehouseImportTicketItems = new ArrayList<WarehouseImportTicketItemForm>();
    

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
     * get value of <b>importType</b>.
     * @return the importType
     */
    public Integer getImportType() {
        return importType;
    }

    /**
     * Set value to <b>importType</b>.
     * @param importType the importType to set
     */
    public void setImportType(Integer importType) {
        this.importType = importType;
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
     * get value of <b>extraData</b>.
     * @return the extraData
     */
    public LinkedHashMap<String, Object> getExtraData() {
        return extraData;
    }

    /**
     * Set value to <b>extraData</b>.
     * @param extraData the extraData to set
     */
    public void setExtraData(LinkedHashMap<String, Object> extraData) {
        this.extraData = extraData;
    }
    
    /**
     * get value of <b>warehouseImportTicketItems</b>.
     * @return the warehouseImportTicketItems
     */
    public List<WarehouseImportTicketItemForm> getWarehouseImportTicketItems() {
        return warehouseImportTicketItems;
    }

    /**
     * Set value to <b>warehouseImportTicketItems</b>.
     * @param warehouseImportTicketItems the warehouseImportTicketItems to set
     */
    public void setWarehouseImportTicketItems(List<WarehouseImportTicketItemForm> warehouseImportTicketItems) {
        this.warehouseImportTicketItems = warehouseImportTicketItems;
    }
    
    @Override
    public String toString() {
        return "WarehouseImportTicketForm [warehouseId=" + warehouseId + ", importType=" + importType + ", description=" + description + ", extraData="
                + extraData + ", warehouseImportTicketItems=" + warehouseImportTicketItems + "]";
    }
    
    
}
