/**
 * ProfileForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

/**
 * ProfileForm. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class ProfileForm implements Serializable {

    private static final long serialVersionUID = 6633235251392135483L;

    private String fullname;
    
    private String phoneNumber;

    private String avatar;
    
    /**
     * get value of <b>fullname</b>.
     * 
     * @return the fullname
     */
    public String getFullname() {
        return fullname;
    }

    /**
     * Set value to <b>fullname</b>.
     * 
     * @param fullname
     *            the fullname to set
     */
    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    /**
     * get value of <b>phoneNumber</b>.
     * 
     * @return the phoneNumber
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * Set value to <b>phoneNumber</b>.
     * 
     * @param phoneNumber
     *            the phoneNumber to set
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
    
    /**
     * get value of <b>avatar</b>.
     * @return the avatar
     */
    public String getAvatar() {
        return avatar;
    }

    /**
     * Set value to <b>avatar</b>.
     * @param avatar the avatar to set
     */
    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "ProfileForm [fullname=" + fullname + ", phoneNumber=" + phoneNumber + "]";
    }

}
