package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.portalapi.form.OrderSellinForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class OrderSellinSearchValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		OrderSellinForm formSearch = (OrderSellinForm) form;
		return this.limitDate(formSearch, errors);
	}
	
	private boolean limitDate(OrderSellinForm formSearch, Errors errors) {
		
		if ( formSearch.getFromDate() != null && formSearch.getToDate() != null ) {
			if (formSearch.getFromDate().compareTo(formSearch.getToDate()) > 0) {
				errors.reject(ErrorCode.APP_1701_FROM_DATE_NO_REASONABLE,
						new Object[] { formSearch.getFromDate(), formSearch.getToDate() },
						ErrorCode.APP_1701_FROM_DATE_NO_REASONABLE);
			}
		}
		
		
		return !errors.hasErrors();
	}
}
