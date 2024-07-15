package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.web.form.ProductResourceDeleteForm;
import co.ipicorp.saas.nrms.web.form.ProductVariationForm;
import co.ipicorp.saas.nrms.web.util.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class ProductVariationExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Autowired
	private ProductVariationService productVariationService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
	    Integer productVariationId = null;
	    if (form instanceof ProductResourceDeleteForm) {
	        productVariationId = ((ProductResourceDeleteForm) form).getProductVariationId();
	    } else if (form instanceof ProductVariationForm) {
	        productVariationId = ((ProductVariationForm) form).getProductVariationId();
	    } 
		return this.validateByProductVariationId(productVariationId, errors);
	}
	
    private boolean validateByProductVariationId(Integer productVariationId, Errors errors) {
		if ( productVariationId != null) {
			ProductVariation productVariation = this.productVariationService.get(productVariationId);
			if ( productVariation == null ) {
				errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST,
						new Object[] { "Product Variation Id", productVariationId },
						ErrorCode.APP_1401_FIELD_NOT_EXIST);
			} 
		} else {
		    errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL,
                    new Object[] { "Product Variation Id"},
                    ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
		}
		return !errors.hasErrors();
	}
}
