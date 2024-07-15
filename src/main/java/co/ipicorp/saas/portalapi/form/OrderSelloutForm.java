package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public class OrderSelloutForm implements Serializable {

	private static final long serialVersionUID = 2172764090491446643L;
	
	private Integer orderSelloutId;
	
    private Integer retailerId;
    
    private Integer warehouseId;
    
	private Integer segment = 0;
    
    private Integer offset = 10;

    private String status;
    
    private String reason = "";
    
    private String keyWord = "";
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fromDate;
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate toDate;

	public Integer getOrderSelloutId() {
		return orderSelloutId;
	}

	public void setOrderSelloutId(Integer orderSelloutId) {
		this.orderSelloutId = orderSelloutId;
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

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
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

	@Override
	public String toString() {
		return "OrderSelloutForm [orderSelloutId=" + orderSelloutId + ", retailerId=" + retailerId + ", warehouseId="
				+ warehouseId + ", segment=" + segment + ", offset=" + offset + ", status=" + status + ", reason="
				+ reason + ", keyWord=" + keyWord + ", fromDate=" + fromDate + ", toDate=" + toDate + "]";
	}

}
