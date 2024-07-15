/**
 * StaffOfCustomerForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * StaffOfCustomerForm.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
public class StaffOfCustomerForm implements Serializable {

    private static final long serialVersionUID = -2517412111999219603L;
    
    @JsonProperty("id")
    private Integer id = null;
    
    @JsonProperty("email")
    private String email;
    
    @JsonProperty("fullName")
    private String fullName;
    
    @JsonProperty("mobile")
    private String mobile;
    
    @JsonProperty("address")
    private String address;
 
    @JsonProperty("tel")
    private String tel;
    
    @JsonProperty("telExt")
    private String telExt;
    
    @JsonProperty("avatar")
    private String avatar;
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getTelExt() {
        return telExt;
    }

    public void setTelExt(String telExt) {
        this.telExt = telExt;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }
    
    @Override
    public String toString() {
        return "StaffOfCustomerForm [id=" + id + ", email=" + email + ", fullname=" + fullName + ", mobile=" + mobile + ", address=" + address + ", tel=" + tel
                + ", telExt=" + telExt + ", avatar=" + avatar + "]";
    }
    
}
