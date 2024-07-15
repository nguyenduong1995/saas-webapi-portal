package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.AccountType;
import co.ipicorp.saas.core.model.StaffOfCustomer;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.core.service.StaffOfCustomerService;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.StaffOfCustomerForm;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class EmailExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Autowired
	private AccountService accountService;
	
	@Autowired
	private StaffOfCustomerService staffOfCustomerService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
	    StaffOfCustomerForm staffOfCustomerForm = (StaffOfCustomerForm) form;
		return this.validateByEmail(staffOfCustomerForm, errors);
	}
	
	private boolean validateByEmail(StaffOfCustomerForm staffOfCustomerForm, Errors errors) {
		String email = staffOfCustomerForm.getEmail();
		if ( StringUtils.isNotEmpty(email)) {
			Account account = this.accountService.getByEmail(email, AccountType.STAFF_OF_CUSTOMER);
			if ( account != null && !isOwnerAccount(staffOfCustomerForm.getId(), account.getId())) {
				errors.reject(ErrorCode.APP_1402_FILED_IS_EXISTED,
						new Object[] { "Email", email },
						ErrorCode.APP_1402_FILED_IS_EXISTED);
			}
		}
		return !errors.hasErrors();
	}

    /**
     * @param staffOfCustomerId
     * @param accountId
     * @return
     */
    private boolean isOwnerAccount(Integer staffOfCustomerId, Integer accountId) {
        boolean isOwnerEmail = false;
        if (staffOfCustomerId != null) {
            StaffOfCustomer staff = this.staffOfCustomerService.getActivated(staffOfCustomerId);
            if (staff != null && staff.getAccount().getId() == accountId) {
                isOwnerEmail = true;
            }
        }
        return isOwnerEmail;
    }
}
