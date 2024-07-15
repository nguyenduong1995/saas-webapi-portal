package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.ProductResource;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.web.form.ProductResourceDeleteForm;
import co.ipicorp.saas.nrms.web.util.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class ProductResourceExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return form instanceof ProductResourceDeleteForm;
	}
	
	@Autowired
	private ProductResourceService productResourceService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
	    ProductResourceDeleteForm productResourceDeleteForm = (ProductResourceDeleteForm) form;
		return this.validateByIds(productResourceDeleteForm, errors);
	}
	
    private boolean validateByIds(ProductResourceDeleteForm productResourceDeleteForm, Errors errors) {
        for (Integer productResourceId : productResourceDeleteForm.getProductResourceIds()) {
            ProductResource productResource = this.productResourceService.get(productResourceId);
            if (productResource == null) {
                errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST, 
                        new Object[] { "Product Resource Id", productResourceId }, 
                        ErrorCode.APP_1401_FIELD_NOT_EXIST);
            } else if (productResource.getProductVariationId() != productResourceDeleteForm.getProductVariationId()){
                errors.reject(ErrorCode.APP_1403_FIELD_NOT_BELONG_TO_OBJECT,
                        new Object[] {"Product Resource Id", productResourceId, "Product Variation Id", productResourceDeleteForm.getProductVariationId() },
                        ErrorCode.APP_1403_FIELD_NOT_BELONG_TO_OBJECT);
            }
        }

        return !errors.hasErrors();
    }
}
