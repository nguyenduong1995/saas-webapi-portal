/**
 * RetailerCreationForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     ntduong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * RetailerCreationForm.
 * <<< Detail note.
 * @author ntduong
 * @access public
 */
public class RetailerCreationForm implements Serializable {

	private static final long serialVersionUID = -2109935856313136434L;

	@JsonProperty("name")
    private String name;

    @JsonProperty("fullname")
    private String fullName;
    
    @JsonProperty("email")
    private String email;

    @DateTimeFormat(iso = ISO.DATE)
    @JsonProperty("birthday")
    private LocalDate birthday;
    
    @JsonProperty("retailerIdCard")
    private String retailerIdCard;
    
    @JsonProperty("mobile")
    private String mobile;
    
    @JsonProperty("address")
    private String address;
    
    @JsonProperty("wardId")
    private Integer wardId;
    
    @JsonProperty("cityId")
    private Integer cityId;
    
    @JsonProperty("districtId")
    private Integer districtId;
    
    @JsonProperty("level")
    private Integer level;
    
    @JsonProperty("retailerIdLv1")
    private Integer retailerLv1;
    
    @JsonProperty("retailerIdLv2")
    private Integer retailerLv2;
    
    @JsonProperty("retailerIdLv3")
    private Integer retailerLv3;
    
    @JsonProperty("username")
    private String username;
    
    @JsonProperty("avatar")
    private String avatar;
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getFullName() {
		return fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public LocalDate getBirthday() {
		return birthday;
	}

	public void setBirthday(LocalDate birthday) {
		this.birthday = birthday;
	}

	public String getRetailerIdCard() {
		return retailerIdCard;
	}

	public void setRetailerIdCard(String retailerIdCard) {
		this.retailerIdCard = retailerIdCard;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
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

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public Integer getRetailerLv1() {
		return retailerLv1;
	}

	public void setRetailerLv1(Integer retailerLv1) {
		this.retailerLv1 = retailerLv1;
	}

	public Integer getRetailerLv2() {
		return retailerLv2;
	}

	public void setRetailerLv2(Integer retailerLv2) {
		this.retailerLv2 = retailerLv2;
	}

	public Integer getRetailerLv3() {
		return retailerLv3;
	}

	public void setRetailerLv3(Integer retailerLv3) {
		this.retailerLv3 = retailerLv3;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

    public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	@Override
	public String toString() {
		return "RetailerCreationForm [name=" + name + ", fullName=" + fullName + ", email=" + email + ", birthday="
				+ birthday + ", retailerIdCard=" + retailerIdCard + ", mobile=" + mobile + ", address=" + address
				+ ", wardId=" + wardId + ", cityId=" + cityId + ", districtId=" + districtId + ", level=" + level
				+ ", retailerLv1=" + retailerLv1 + ", retailerLv2=" + retailerLv2 + ", retailerLv3=" + retailerLv3
				+ ", username=" + username + ", avatar=" + avatar + "]";
	}
	
}
