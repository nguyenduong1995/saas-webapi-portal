package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.RetailerCreationForm;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class RetailerEmailExistedValidator extends AbstractFormValidator {

	@Autowired
	private RetailerService retailerService;
	
	@Override
	public boolean support(Serializable form) {
		return true;
	}

	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		RetailerCreationForm creationForm = (RetailerCreationForm) form;
		return this.validateByEmail(creationForm, errors);
	}

	private boolean validateByEmail(RetailerCreationForm creationForm, Errors errors) {
		String email = creationForm.getEmail();
		if (email != null && StringUtils.isNotEmpty(email)) {
			List<Retailer> retailers = retailerService.getByEmail(email);
			if (CollectionUtils.isNotEmpty(retailers)) {
				errors.reject(ErrorCode.APP_1402_FILED_IS_EXISTED, new Object[] { "Email đăng ký", email },
						ErrorCode.APP_1402_FILED_IS_EXISTED);
			}
		}
		return !errors.hasErrors();
	}

}
