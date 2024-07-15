/**
 * RetailerinvoiceInfoCreationForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     ntduong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RetailerinvoiceInfoCreationForm.
 * <<< Detail note.
 * @author ntduong
 * @access public
 */
public class RetailerInvoiceInfoUpdateForm implements Serializable {

	private static final long serialVersionUID = 495853811025620835L;
	
	@JsonProperty("id")
    private Integer id;
	
	@JsonProperty("retailerId")
    private Integer retailerId;

	@JsonProperty("retailerInvoiceName")
    private String retailerInvoiceName;

	@JsonProperty("addressText")
    private String addressText;
	
	@JsonProperty("taxNo")
    private String taxNo;

	@JsonProperty("tel")
    private String tel;

	@JsonProperty("telExt")
    private String telExt;

	@JsonProperty("bankName")
    private String bankName;
    
	@JsonProperty("bankBranch")
    private String bankBranch;
    
	@JsonProperty("bankAccountNo")
    private String bankAccountNo;
    
	@JsonProperty("bankAccountName")
    private String bankAccountName;
	
	@JsonProperty("status")
    private Integer status;
	
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * get value of <b>retailerId</b>.
     * 
     * @return the retailerId
     */
    public Integer getRetailerId() {
		return retailerId;
	}

    /**
     * Set value to <b>retailerId</b>.
     * 
     * @param retailerId
     *            the retailerId to set
     */
	public void setRetailerId(Integer retailerId) {
		this.retailerId = retailerId;
	}

	/**
     * get value of <b>retailerInvoiceName</b>.
     * 
     * @return the retailerInvoiceName
     */
	public String getRetailerInvoiceName() {
		return retailerInvoiceName;
	}

	/**
     * Set value to <b>retailerInvoiceName</b>.
     * 
     * @param retailerInvoiceName
     *            the retailerInvoiceName to set
     */
	public void setRetailerInvoiceName(String retailerInvoiceName) {
		this.retailerInvoiceName = retailerInvoiceName;
	}

	/**
     * get value of <b>addressText</b>.
     * 
     * @return the addressText
     */
	public String getAddressText() {
		return addressText;
	}

	/**
     * Set value to <b>addressText</b>.
     * 
     * @param addressText
     *            the addressText to set
     */
	public void setAddressText(String addressText) {
		this.addressText = addressText;
	}

	/**
     * get value of <b>taxNo</b>.
     * 
     * @return the taxNo
     */
	public String getTaxNo() {
		return taxNo;
	}

	/**
     * Set value to <b>taxNo</b>.
     * 
     * @param taxNo
     *            the taxNo to set
     */
	public void setTaxNo(String taxNo) {
		this.taxNo = taxNo;
	}

	/**
     * get value of <b>tel</b>.
     * 
     * @return the tel
     */
	public String getTel() {
		return tel;
	}

	/**
     * Set value to <b>tel</b>.
     * 
     * @param tel
     *            the tel to set
     */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
     * get value of <b>telExt</b>.
     * 
     * @return the telExt
     */
	public String getTelExt() {
		return telExt;
	}

	/**
     * Set value to <b>telExt</b>.
     * 
     * @param telExt
     *            the telExt to set
     */
	public void setTelExt(String telExt) {
		this.telExt = telExt;
	}

	/**
     * get value of <b>bankName</b>.
     * 
     * @return the bankName
     */
	public String getBankName() {
		return bankName;
	}

	/**
     * Set value to <b>bankName</b>.
     * 
     * @param bankName
     *            the bankName to set
     */
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	/**
     * get value of <b>bankBranch</b>.
     * 
     * @return the bankBranch
     */
	public String getBankBranch() {
		return bankBranch;
	}

	/**
     * Set value to <b>bankBranch</b>.
     * 
     * @param bankBranch
     *            the bankBranch to set
     */
	public void setBankBranch(String bankBranch) {
		this.bankBranch = bankBranch;
	}

	/**
     * get value of <b>bankAccountNo</b>.
     * 
     * @return the bankAccountNo
     */
	public String getBankAccountNo() {
		return bankAccountNo;
	}

	/**
     * Set value to <b>bankAccountNo</b>.
     * 
     * @param bankAccountNo
     *            the bankAccountNo to set
     */
	public void setBankAccountNo(String bankAccountNo) {
		this.bankAccountNo = bankAccountNo;
	}

	/**
     * get value of <b>bankAccountName</b>.
     * 
     * @return the bankAccountName
     */
	public String getBankAccountName() {
		return bankAccountName;
	}

	/**
     * Set value to <b>bankAccountName</b>.
     * 
     * @param bankAccountName
     *            the bankAccountName to set
     */
	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

	public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "RetailerInvoiceInfoUpdateForm [id=" + id + ", retailerId=" + retailerId + ", retailerInvoiceName=" + retailerInvoiceName + ", addressText="
                + addressText + ", taxNo=" + taxNo + ", tel=" + tel + ", telExt=" + telExt + ", bankName=" + bankName + ", bankBranch=" + bankBranch
                + ", bankAccountNo=" + bankAccountNo + ", bankAccountName=" + bankAccountName + ", status=" + status + "]";
    }

	
}
