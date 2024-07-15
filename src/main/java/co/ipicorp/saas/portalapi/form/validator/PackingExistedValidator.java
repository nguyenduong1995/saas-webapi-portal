package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.Packing;
import co.ipicorp.saas.nrms.service.PackingService;
import co.ipicorp.saas.nrms.web.form.ProductVariationForm;
import co.ipicorp.saas.nrms.web.util.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class PackingExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return form instanceof ProductVariationForm;
	}
	
	@Autowired
	private PackingService packingService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
	    ProductVariationForm productVariationForm = (ProductVariationForm) form;
		return this.validatePackingById(productVariationForm, errors);
	}
	
	private boolean validatePackingById(ProductVariationForm productVariationForm, Errors errors) {
		Integer packingId = productVariationForm.getPackingId();
		if ( packingId != null) {
			Packing packing = this.packingService.get(packingId);
			if ( packing == null ) {
				errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST,
						new Object[] { "Packing Id", packingId },
						ErrorCode.APP_1401_FIELD_NOT_EXIST);
			}
		}
		return !errors.hasErrors();
	}
}
