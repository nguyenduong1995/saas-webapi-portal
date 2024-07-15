/**
 * OrderSellinForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;
import java.time.LocalDate;

import co.ipicorp.saas.nrms.model.dto.OrderSellinCancelType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * OrderSellinForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class OrderSellinForm implements Serializable {

	private static final long serialVersionUID = -1241124773964142290L;

	private Integer orderSellinId;
	
    private Integer retailerId;
    
    private Integer warehouseId;
    
	private Integer segment = 0;
    
    private Integer offset = 10;

    private String status;
    
    private OrderSellinCancelType cancelType;
    
    private String reason = "";
    
    private String keyWord = "";
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fromDate;
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate toDate;
	
    public Integer getOrderSellinId() {
		return orderSellinId;
	}

	public void setOrderSellinId(Integer orderSellinId) {
		this.orderSellinId = orderSellinId;
	}

	public Integer getRetailerId() {
		return retailerId;
	}

	public void setRetailerId(Integer retailerId) {
		this.retailerId = retailerId;
	}

	public Integer getWarehouseId() {
		return warehouseId;
	}

	public void setWarehouseId(Integer warehouseId) {
		this.warehouseId = warehouseId;
	}

	public Integer getSegment() {
		return segment;
	}

	public void setSegment(Integer segment) {
		this.segment = segment;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}
	
	/**
     * get value of <b>reason</b>.
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * Set value to <b>reason</b>.
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	
	/**
     * get value of <b>cancelType</b>.
     * @return the cancelType
     */
    public OrderSellinCancelType getCancelType() {
        return cancelType;
    }

    /**
     * Set value to <b>cancelType</b>.
     * @param cancelType the cancelType to set
     */
    public void setCancelType(OrderSellinCancelType cancelType) {
        this.cancelType = cancelType;
    }

    @Override
    public String toString() {
        return "OrderSellinForm [orderSellinId=" + orderSellinId + ", retailerId=" + retailerId + ", warehouseId=" + warehouseId + ", segment=" + segment
                + ", offset=" + offset + ", status=" + status + ", cancelType=" + cancelType + ", reason=" + reason + ", keyWord=" + keyWord + ", fromDate="
                + fromDate + ", toDate=" + toDate + "]";
    }
	
}
