package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.RetailerInvoiceInfoCreationForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class RetailerExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Autowired
	private RetailerService retailerService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		RetailerInvoiceInfoCreationForm retailerInvoiceform = (RetailerInvoiceInfoCreationForm) form;
		return this.validateById(retailerInvoiceform, errors);
	}
	
	private boolean validateById(RetailerInvoiceInfoCreationForm retailerInvoiceform, Errors errors) {
		
		if ( retailerInvoiceform.getRetailerId() != null ) {
			Retailer retailer = this.retailerService.get(retailerInvoiceform.getRetailerId());
			if ( retailer == null ) {
				errors.reject(ErrorCode.APP_1201_RETAILER_NOT_EXIST,
						new Object[] { "" + retailerInvoiceform.getRetailerId() },
						ErrorCode.APP_1201_RETAILER_NOT_EXIST);
			}
		}
		return !errors.hasErrors();
	}
}
