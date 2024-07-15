package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.AccountType;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.RetailerCreationForm;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class RetailerInfoExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}

	@Autowired
	private AccountService accountService;

	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		RetailerCreationForm creationForm = (RetailerCreationForm) form;
		return this.validateInfo(creationForm, errors);
	}

	private boolean validateInfo(RetailerCreationForm creationForm, Errors errors) {
		String mobile = creationForm.getMobile();
		if (mobile != null && StringUtils.isNotEmpty(mobile)) {
			List<Account> accounts = this.accountService.getByLoginName(mobile, Arrays.asList(AccountType.RETAILER));
			Account account = null;
			if (CollectionUtils.isNotEmpty(accounts)) {
				account = accounts.get(0);
			}

			if (account != null) {
				errors.reject(ErrorCode.APP_1202_RETAILER_IS_EXISTED, new Object[] { "Số điện thoại", mobile },
						ErrorCode.APP_1202_RETAILER_IS_EXISTED);
			}
		}
		return !errors.hasErrors();
	}

}
