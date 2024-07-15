package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.portalapi.form.search.PromotionSearchForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class PromotionSearchValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		PromotionSearchForm formSearch = (PromotionSearchForm) form;
		return this.checkDate(formSearch, errors);
	}
	
	private boolean checkDate(PromotionSearchForm formSearch, Errors errors) {
		
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
