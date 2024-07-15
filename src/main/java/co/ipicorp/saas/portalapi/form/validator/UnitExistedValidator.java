package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.Unit;
import co.ipicorp.saas.nrms.service.UnitService;
import co.ipicorp.saas.nrms.web.form.ProductVariationForm;
import co.ipicorp.saas.nrms.web.util.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class UnitExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return form instanceof ProductVariationForm;
	}
	
	@Autowired
	private UnitService unitService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
	    ProductVariationForm productVariationForm = (ProductVariationForm) form;
		return this.validateUnitById(productVariationForm, errors);
	}
	
	private boolean validateUnitById(ProductVariationForm productVariationForm, Errors errors) {
		Integer unitId = productVariationForm.getUnitId();
		if ( unitId != null) {
			Unit unit = this.unitService.get(unitId);
			if ( unit == null ) {
				errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST,
						new Object[] { "Unit", unitId },
						ErrorCode.APP_1401_FIELD_NOT_EXIST);
			}
		}
		return !errors.hasErrors();
	}
}
