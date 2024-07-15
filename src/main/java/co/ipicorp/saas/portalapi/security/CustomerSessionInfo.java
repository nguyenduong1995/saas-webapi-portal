/**
 * CustomerSessionInfo.java
 * @copyright  Copyright © 2020 Duy Vo
 * @author     duyvk
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.security;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.model.StaffOfCustomer;

import grass.micro.apps.web.security.SessionInfo;

/**
 * CustomerSessionInfo. <<< Detail note.
 * 
 * @author duyvk
 * @access public
 */
public class CustomerSessionInfo extends SessionInfo {
    private static final long serialVersionUID = -5357680670533819229L;
	public static final String APP_CUSTOMER_SESSION_ID_KEY = "__a_c_s_id";
    
	private Customer customer;
	
	private Account account;
	
	private StaffOfCustomer staffOfCustomer;

    /**
     * get value of <b>customer</b>.
     * 
     * @return the customer
     */
    public Customer getCustomer() {
        return customer;
    }

    /**
     * Set value to <b>customer</b>.
     * 
     * @param customer
     *            the customer to set
     */
    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    /**
     * get value of <b>account</b>.
     * @return the account
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Set value to <b>account</b>.
     * @param account the account to set
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    @Override
    public String getUsername() {
        return account.getLoginName();
    }

    /**
     * get value of <b>staffOfCustomer</b>.
     * @return the staffOfCustomer
     */
    public StaffOfCustomer getStaffOfCustomer() {
        return staffOfCustomer;
    }

    /**
     * Set value to <b>staffOfCustomer</b>.
     * @param staffOfCustomer the staffOfCustomer to set
     */
    public void setStaffOfCustomer(StaffOfCustomer staffOfCustomer) {
        this.staffOfCustomer = staffOfCustomer;
    }

    @Override
    public String toString() {
        return "CustomerSessionInfo [customer=" + customer + ", account=" + account + ", staffOfCustomer=" + staffOfCustomer + "]";
    }

}
