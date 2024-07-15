package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.WarehouseImportTicketSearchForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class WarehouseImportTicketSearchValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Override
    public boolean doValidate(Serializable form, Errors errors) {
        WarehouseImportTicketSearchForm formSearch = (WarehouseImportTicketSearchForm) form;
        return this.limitDate(formSearch, errors);
    }
    
    private boolean limitDate(WarehouseImportTicketSearchForm formSearch, Errors errors) {
        
        if ( formSearch.getFromDate() != null && formSearch.getToDate() != null ) {
            if (formSearch.getFromDate().compareTo(formSearch.getToDate()) > 0) {
                errors.reject(ErrorCode.APP_1701_FROM_DATE_NO_REASONABLE,
                        new Object[] { "" + formSearch.getFromDate() + ">" + formSearch.getToDate() },
                        ErrorCode.APP_1701_FROM_DATE_NO_REASONABLE);
            }
        }
        
        
        return !errors.hasErrors();
    }
	
}
