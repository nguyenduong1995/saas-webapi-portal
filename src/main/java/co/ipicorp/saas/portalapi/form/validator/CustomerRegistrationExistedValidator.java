package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.AccountType;
import co.ipicorp.saas.core.model.CustomerRegistration;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.core.service.CustomerRegistrationService;
import co.ipicorp.saas.portalapi.form.CustomerRegisterForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class CustomerRegistrationExistedValidator extends AbstractFormValidator {
	
	@Autowired
	private CustomerRegistrationService customerRegistrationService;
	
	private static List<CustomerRegistration> customerRegistrations = null;

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Autowired
	private AccountService accountService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		CustomerRegisterForm customerRegisterForm = (CustomerRegisterForm) form;
		return this.validateByEmail(customerRegisterForm, errors);
	}
	
	private boolean validateByEmail(CustomerRegisterForm customerRegisterForm, Errors errors) {
		
		if ( customerRegisterForm.getEmail() != null ) {
			Account account = this.accountService.getByEmail(customerRegisterForm.getRepresentEmail(), AccountType.CUSTOMER);
			int countRegisterAccept = 0;
			int countRegisterWaiting = 0;
			customerRegistrations = this.customerRegistrationService.getByEmail(customerRegisterForm.getEmail());
			if ( customerRegistrations.size() > 0 ) {
				countRegisterAccept = (int) customerRegistrations.stream().filter( customerRegistration -> customerRegistration.getStatus() == Status.ACTIVE ).count();
				countRegisterWaiting = (int) customerRegistrations.stream().filter( customerRegistration -> customerRegistration.getStatus() == Status.STATUS_TWO ).count();
			}
			if ( account != null || countRegisterWaiting >= 1 || countRegisterAccept >= 1 ) {
				errors.reject(ErrorCode.APP_2091_EMAIL_HAS_BEEN_EXISTED,
						new Object[] { "" + customerRegisterForm.getEmail() },
						ErrorCode.APP_2091_EMAIL_HAS_BEEN_EXISTED);
			}
		}
		return !errors.hasErrors();
	}
}
