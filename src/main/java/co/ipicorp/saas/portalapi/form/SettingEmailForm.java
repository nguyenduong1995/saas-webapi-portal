/**
 * SettingEmailForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

/**
 * SettingEmailForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class SettingEmailForm implements Serializable{

    private static final long serialVersionUID = 5257657554880674521L;
    
    private String emailStaff;
    
    private String emailRetailer;
    
    public SettingEmailForm() {
    }

    /**
     * get value of <b>emailStaff</b>.
     * @return the emailStaff
     */
    public String getEmailStaff() {
        return emailStaff;
    }

    /**
     * Set value to <b>emailStaff</b>.
     * @param emailStaff the emailStaff to set
     */
    public void setEmailStaff(String emailStaff) {
        this.emailStaff = emailStaff;
    }

    /**
     * get value of <b>emailRetailer</b>.
     * @return the emailRetailer
     */
    public String getEmailRetailer() {
        return emailRetailer;
    }

    /**
     * Set value to <b>emailRetailer</b>.
     * @param emailRetailer the emailRetailer to set
     */
    public void setEmailRetailer(String emailRetailer) {
        this.emailRetailer = emailRetailer;
    }

    @Override
    public String toString() {
        return "SettingEmailForm [emailStaff=" + emailStaff + ", emailRetailer=" + emailRetailer + "]";
    }

}
