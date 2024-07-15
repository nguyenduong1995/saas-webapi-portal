/**
 * WarehouseDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.dto;

import co.ipicorp.saas.nrms.model.ImportType;
import co.ipicorp.saas.nrms.model.dto.WarehouseImportTicketItemDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;

import grass.micro.apps.model.dto.StatusTimestampDto;

/**
 * WarehouseDto. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
public class WarehouseImportTicketDto extends StatusTimestampDto implements Serializable {
    
    private static final long serialVersionUID = 301127278877008663L;

    @JsonProperty("warehouseId")
    private Integer warehouseId;

    @JsonProperty("importTicketCode")
    private String importTicketCode;

    @JsonProperty("importType")
    private ImportType importType;
    
    @JsonProperty("importPerson")
    private String importPerson;

    @JsonProperty("importDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy HH:mm:ss")
    private LocalDateTime importDate;

    @JsonProperty("approvedPerson")
    private String approvedPerson;

    @JsonProperty("description")
    private String description;

    @JsonProperty("total")
    private Double total;
    
    @JsonProperty("extraData")
    private LinkedHashMap<String, Object> extraData;
    
    @JsonProperty("warehouseImportTicketItems")
    private List<WarehouseImportTicketItemDto> items;

    /**
     * 
     */
    public WarehouseImportTicketDto() {
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
     * get value of <b>importType</b>.
     * @return the importType
     */
	public ImportType getImportType() {
		return importType;
	}

	/**
     * Set value to <b>importType</b>.
     * @param importType the importType to set
     */
	public void setImportType(ImportType importType) {
		this.importType = importType;
	}

	/**
     * get value of <b>importPerson</b>.
     * @return the importPerson
     */
	public String getImportPerson() {
		return importPerson;
	}

	/**
     * Set value to <b>importPerson</b>.
     * @param importPerson the importPerson to set
     */
	public void setImportPerson(String importPerson) {
		this.importPerson = importPerson;
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
     * get value of <b>approvedPerson</b>.
     * @return the approvedPerson
     */
	public String getApprovedPerson() {
		return approvedPerson;
	}

	/**
     * Set value to <b>approvedPerson</b>.
     * @param approvedPerson the approvedPerson to set
     */
	public void setApprovedPerson(String approvedPerson) {
		this.approvedPerson = approvedPerson;
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
     * get value of <b>total</b>.
     * @return the total
     */
	public Double getTotal() {
		return total;
	}

	/**
     * Set value to <b>total</b>.
     * @param total the total to set
     */
	public void setTotal(Double total) {
		this.total = total;
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
     * get value of <b>items</b>.
     * @return the items
     */
    public List<WarehouseImportTicketItemDto> getItems() {
		return items;
	}

    /**
     * Set value to <b>items</b>.
     * @param items the items to set
     */
	public void setItems(List<WarehouseImportTicketItemDto> items) {
		this.items = items;
	}

	@Override
    public String toString() {
        return "WarehouseImportTicketDto [warehouseId=" + warehouseId + ", importTicketCode=" + importTicketCode + ", importType=" + importType 
        		+ ", importPerson=" + importPerson + ", importDate=" + importDate + ", approvedPerson=" + approvedPerson + ", description=" 
        		+ description + ", total=" + total + ", extraData=" + extraData + ", items=" + items + "]";
    }
    
}
