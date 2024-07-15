package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.AccountType;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.CustomerRegisterForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class CustomerRegisterExistedValidator extends AbstractFormValidator {

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
			Account account = this.accountService.getByEmail(customerRegisterForm.getEmail(), AccountType.CUSTOMER);
			if ( account != null ) {
				errors.reject(ErrorCode.APP_2091_EMAIL_HAS_BEEN_EXISTED,
						new Object[] { "" + customerRegisterForm.getEmail() },
						ErrorCode.APP_2091_EMAIL_HAS_BEEN_EXISTED);
			}
		}
		return !errors.hasErrors();
	}
}
