package co.ipicorp.saas.portalapi.dto;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

import grass.micro.apps.model.dto.StatusTimestampDto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * InvoiceDto. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
public class InvoiceDto extends StatusTimestampDto implements Serializable {

	private static final long serialVersionUID = -1197431275575478668L;

	@JsonProperty(value = "issue_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime issueDate;

	@JsonProperty(value = "paid_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime paidDate;
	
	@JsonProperty(value = "start_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime startDate;
	
	@JsonProperty(value = "end_date")
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime endDate;

	@JsonProperty(value = "vat")
	private Integer vat;
	
	@JsonProperty(value = "total")
	private Integer total;
	
	@JsonProperty(value = "bill_items")
	private LinkedHashMap<String, Object> billItems;
	
	@JsonProperty(value = "customer_id")
	private Integer customerId;
	
	@JsonProperty(value = "plan_id")
	private Integer planId;
	
	@JsonProperty(value = "customer_plan_id")
	private Integer customerPlanId;
	
	public LocalDateTime getIssueDate() {
		return issueDate;
	}

	public void setIssueDate(LocalDateTime issueDate) {
		this.issueDate = issueDate;
	}

	public LocalDateTime getPaidDate() {
		return paidDate;
	}

	public void setPaidDate(LocalDateTime paidDate) {
		this.paidDate = paidDate;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDateTime endDate) {
		this.endDate = endDate;
	}

	public Integer getVat() {
		return vat;
	}

	public void setVat(Integer vat) {
		this.vat = vat;
	}

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public LinkedHashMap<String, Object> getBillItems() {
		return billItems;
	}

	public void setBillItems(LinkedHashMap<String, Object> billItems) {
		this.billItems = billItems;
	}

	public Integer getCustomerId() {
		return customerId;
	}

	public void setCustomerId(Integer customerId) {
		this.customerId = customerId;
	}

	public Integer getPlanId() {
		return planId;
	}

	public void setPlanId(Integer planId) {
		this.planId = planId;
	}

	public Integer getCustomerPlanId() {
		return customerPlanId;
	}

	public void setCustomerPlanId(Integer customerPlanId) {
		this.customerPlanId = customerPlanId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "InvoiceDto [issueDate=" + issueDate + ", paidDate=" + paidDate + ", startDate=" + startDate + ", endDate=" + endDate 
				+ ", vat=" + vat + ", total=" + total + ", billItems=" + billItems + ", customerId=" + customerId 
				+ ", planId=" + planId + ", customerPlanId=" + customerPlanId + "]";
	}

}
