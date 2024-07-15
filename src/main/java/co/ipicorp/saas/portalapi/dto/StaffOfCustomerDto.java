package co.ipicorp.saas.portalapi.dto;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * UserDto. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
public class StaffOfCustomerDto extends StatusTimestampDto implements Serializable {

	private static final long serialVersionUID = 5263529404360475401L;

	@JsonProperty(value = "accountId")
    private Integer accountId;
	
	@JsonProperty(value = "customerId")
    private Integer customerId;
	
	@JsonProperty(value = "email")
	private String email;
	
	@JsonProperty(value = "loginName")
	private String loginName;
    
	@JsonProperty(value = "fullname")
    private String fullName;
	
	@JsonProperty(value = "address")
    private String address;
	
	@JsonProperty(value = "mobile")
    private String mobile;

	@JsonProperty(value = "tel")
    private String tel;
	
	@JsonProperty(value = "telExt")
    private String telExt;
	
	@JsonProperty(value = "avatar")
	private String avatar;
	
	/**
	 * @return the accountId
	 */
    public Integer getAccountId() {
		return accountId;
	}

    /**
	 * @param name the accountId to set
	 */
	public void setAccountId(Integer accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the fullName
	 */
	public String getFullName() {
		return fullName;
	}

	/**
	 * @param name the fullName to set
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return the address
	 */
	public String getAddress() {
		return address;
	}

	/**
	 * @param name the address to set
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return the mobile
	 */
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param name the mobile to set
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return the tel
	 */
	public String getTel() {
		return tel;
	}

	/**
	 * @param name the tel to set
	 */
	public void setTel(String tel) {
		this.tel = tel;
	}

	/**
	 * @return the telExt
	 */
	public String getTelExt() {
		return telExt;
	}

	/**
	 * @param telExt the telExt to set
	 */
	public void setTelExt(String telExt) {
		this.telExt = telExt;
	}	
	
	public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "StaffOfCustomerDto [accountId=" + accountId + ", customerId=" + customerId + ", email=" + email + ", loginName=" + loginName + ", fullName="
                + fullName + ", address=" + address + ", mobile=" + mobile + ", tel=" + tel + ", telExt=" + telExt + ", avatar=" + avatar + "]";
    }

}
