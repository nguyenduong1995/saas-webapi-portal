/**
 * CustomerRegisterForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;
import java.util.LinkedHashMap;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * CustomerRegisterForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class CustomerRegisterForm implements Serializable {
	
	private static final long serialVersionUID = -2154511090237455389L;

	
	@JsonProperty("fullname")
    private String fullname;

	@JsonProperty("email")
    private String email;
	
    @JsonProperty("representative")
    private String representative;
    
    @JsonProperty("representative_mobile")
    private String representativeMobile;
    
    @JsonProperty("represent_email")
    private String representEmail;
    
    @JsonProperty("city_id")
    private Integer cityId;
    
    @JsonProperty("website")
    private String website;
    
    @JsonProperty("mobile")
    private String mobile;
    
    @JsonProperty("industry")
    private Integer industry;
    
    @JsonProperty("organizational_scale")
    private LinkedHashMap<String, Object> organization;

    /**
     * get value of <b>fullname</b>.
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Set value to <b>fullname</b>.
     * @param fullname the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * get value of <b>email</b>.
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set value to <b>email</b>.
     * @param email the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * get value of <b>representative</b>.
     * @return the representative
     */
    public String getRepresentative() {
        return representative;
    }

    /**
     * Set value to <b>representative</b>.
     * @param representative the representative to set
     */
    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    /**
     * get value of <b>representativeMobile</b>.
     * @return the representativeMobile
     */
    public String getRepresentativeMobile() {
        return representativeMobile;
    }

    /**
     * Set value to <b>representativeMobile</b>.
     * @param representativeMobile the representativeMobile to set
     */
    public void setRepresentativeMobile(String representativeMobile) {
        this.representativeMobile = representativeMobile;
    }

    /**
     * get value of <b>representEmail</b>.
     * @return the representEmail
     */
    public String getRepresentEmail() {
        return representEmail;
    }

    /**
     * Set value to <b>representEmail</b>.
     * @param representEmail the representEmail to set
     */
    public void setRepresentEmail(String representEmail) {
        this.representEmail = representEmail;
    }

    /**
     * get value of <b>cityId</b>.
     * @return the cityId
     */
    public Integer getCityId() {
        return cityId;
    }

    /**
     * Set value to <b>cityId</b>.
     * @param cityId the cityId to set
     */
    public void setCityId(Integer cityId) {
        this.cityId = cityId;
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

    /**
     * get value of <b>mobile</b>.
     * @return the mobile
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * Set value to <b>mobile</b>.
     * @param mobile the mobile to set
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    /**
     * get value of <b>industry</b>.
     * @return the industry
     */
    public Integer getIndustry() {
        return industry;
    }

    /**
     * Set value to <b>industry</b>.
     * @param industry the industry to set
     */
    public void setIndustry(Integer industry) {
        this.industry = industry;
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

    @Override
    public String toString() {
        return "CustomerRegisterForm [fullname=" + fullname + ", email=" + email + ", representative=" + representative + ", representativeMobile="
                + representativeMobile + ", representEmail=" + representEmail + ", cityId=" + cityId + ", website=" + website + ", mobile=" + mobile
                + ", industry=" + industry + ", organization=" + organization + "]";
    }
    
}
