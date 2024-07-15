/**
 * ResetPasswordForm.java
 * @author     hieu.vo
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ResetPasswordForm.
 * 
 * @author hieumicro
 * @access public
 */
public class ResetPasswordForm implements Serializable {
    private static final long serialVersionUID = 675658644345061593L;

    @JsonProperty("reset_password_key")
    private String resetPasswordKey;

    @JsonProperty("new_password")
    private String newPassword;

    /**
     * get value of <b>resetPasswordKey</b>.
     * 
     * @return the resetPasswordKey
     */
    public String getResetPasswordKey() {
        return resetPasswordKey;
    }

    /**
     * Set value to <b>resetPasswordKey</b>.
     * 
     * @param resetPasswordKey
     *            the resetPasswordKey to set
     */
    public void setResetPasswordKey(String resetPasswordKey) {
        this.resetPasswordKey = resetPasswordKey;
    }

    /**
     * get value of <b>newPassword</b>.
     * 
     * @return the newPassword
     */
    public String getNewPassword() {
        return newPassword;
    }

    /**
     * Set value to <b>newPassword</b>.
     * 
     * @param newPassword
     *            the newPassword to set
     */
    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    @Override
    public String toString() {
        return "ResetPasswordForm [resetPasswordKey=" + resetPasswordKey + ", newPassword=" + newPassword + "]";
    }

}
