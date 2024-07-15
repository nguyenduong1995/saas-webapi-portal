/**
 * ForgotPasswordForm.java
 * @author     hieu.vo
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.ipicorp.saas.core.model.AccountType;

/**
 * ForgotPasswordForm.
 * 
 * @author hieumicro
 * @access public
 */
public class ForgotPasswordForm implements Serializable {
    public static Map<String, String> fieldMap = new LinkedHashMap<String, String>();

    static {
        fieldMap.put("email", "email");
        fieldMap.put("accountType", "accountType");
    }

    private static final long serialVersionUID = 675658644345061593L;

    @JsonProperty("email")
    private String email;
    
    @JsonProperty("account_type")
    private AccountType accountType = AccountType.CUSTOMER;

    /**
     * get value of <b>email</b>.
     * 
     * @return the email
     */
    public String getEmail() {
        return email;
    }

    /**
     * Set value to <b>email</b>.
     * 
     * @param email
     *            the email to set
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
	 * @return the accountType
	 */
	public AccountType getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType the accountType to set
	 */
	public void setAccountType(AccountType accountType) {
		this.accountType = accountType;
	}

	@Override
    public String toString() {
        return "ResetPasswordForm [email=" + email + "]";
    }

}
