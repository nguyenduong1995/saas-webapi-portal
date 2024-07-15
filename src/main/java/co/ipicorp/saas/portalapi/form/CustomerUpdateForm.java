/**
 * CustomerUpdateForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CustomerUpdateForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class CustomerUpdateForm implements Serializable {

	private static final long serialVersionUID = 3482780235620017075L;
	
	private String email;
	
	private String fullname;
    
    private Integer industry;
    
    private String representative;

    private String position;
    
    private String address;
    
    private Integer cityId;
    
    private Integer districtId;
    
    private Integer wardId;
    
    private String tel;
    
    private String website;
    
    private String picture;
    
    @JsonProperty("organization")
    private LinkedHashMap<String, Object> organization;
    
    @JsonProperty("subscription")
    private LinkedHashMap<String, Object> subscription;
    
    @JsonProperty("payment_address")
    private LinkedHashMap<String, Object> paymentAddress;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getFullname() {
		return fullname;
	}

	public void setFullname(String fullname) {
		this.fullname = fullname;
	}

	public Integer getIndustry() {
		return industry;
	}

	public void setIndustry(Integer industry) {
		this.industry = industry;
	}

	public String getRepresentative() {
		return representative;
	}

	public void setRepresentative(String representative) {
		this.representative = representative;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
     * get value of <b>website</b>.
     * @return the website
     */
    public String getWebsite() {
        return website;
    }

    /**
     * Set value to <b>website</b>.
     * @param website the website to set
     */
    public void setWebsite(String website) {
        this.website = website;
    }

    public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	/**
     * get value of <b>organization</b>.
     * @return the organization
     */
	public LinkedHashMap<String, Object> getOrganization() {
		return organization;
	}

	/**
     * Set value to <b>organization</b>.
     * @param organization the organization to set
     */
	public void setOrganization(LinkedHashMap<String, Object> organization) {
		this.organization = organization;
	}
	
	/**
     * get value of <b>subscription</b>.
     * @return the subscription
     */
	public LinkedHashMap<String, Object> getSubscription() {
		return subscription;
	}

	/**
     * Set value to <b>subscription</b>.
     * @param subscription the subscription to set
     */
	public void setSubscription(LinkedHashMap<String, Object> subscription) {
		this.subscription = subscription;
	}

	/**
     * get value of <b>paymentAddress</b>.
     * @return the paymentAddress
     */
	public LinkedHashMap<String, Object> getPaymentAddress() {
		return paymentAddress;
	}

	/**
     * Set value to <b>paymentAddress</b>.
     * @param paymentAddress the paymentAddress to set
     */
	public void setPaymentAddress(LinkedHashMap<String, Object> paymentAddress) {
		this.paymentAddress = paymentAddress;
	}

	@Override
    public String toString() {
        return "CustomerUpdateForm [email=" + email + ", fullname=" + fullname + ", industry=" + industry + ", representative=" + representative + ", position="
                + position + ", address=" + address + ", cityId=" + cityId + ", districtId=" + districtId + ", wardId=" + wardId + ", tel=" + tel + ", website="
                + website + ", picture=" + picture + ", organization=" + organization + ", subscription=" + subscription + ", paymentAddress=" + paymentAddress
                + "]";
    }
	
}
