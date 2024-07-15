/**
 * ChangePasswordForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

/**
 * ChangePasswordForm.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
public class ChangePasswordForm implements Serializable {

    private static final long serialVersionUID = -2873546639123463896L;

    private String currentPassword;

    private String newPassword;

    /**
     * get value of <b>currentPassword</b>.
     * @return the currentPassword
     */
    public String getCurrentPassword() {
        return currentPassword;
    }

    /**
     * Set value to <b>currentPassword</b>.
     * @param currentPassword the currentPassword to set
     */
    public void setCurrentPassword(String currentPassword) {
        this.currentPassword = currentPassword;
    }

    /**
     * get value of <b>newPassword</b>.
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Set value to <b>newPassword</b>.
     * @param newPassword the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ChangePasswordForm [currentPassword=" + currentPassword + ", newPassword=" + newPassword + "]";
    }
    
}
