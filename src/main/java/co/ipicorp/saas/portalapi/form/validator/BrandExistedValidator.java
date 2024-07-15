package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.Brand;
import co.ipicorp.saas.nrms.service.BrandService;
import co.ipicorp.saas.nrms.web.form.ProductForm;
import co.ipicorp.saas.nrms.web.util.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class BrandExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return form instanceof ProductForm;
	}
	
	@Autowired
	private BrandService brandService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		ProductForm productForm = (ProductForm) form;
		return this.validateBrandById(productForm, errors);
	}
	
	private boolean validateBrandById(ProductForm productForm, Errors errors) {
	    if (productForm.getBrandId() != null) {
    		Integer brandId = productForm.getBrandId();
    		if ( brandId != null) {
    			Brand brand = this.brandService.get(brandId);
    			if ( brand == null ) {
    				errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST,
    						new Object[] {"Brand Id", brandId },
    						ErrorCode.APP_1401_FIELD_NOT_EXIST);
    			}
    		}
	    }
		return !errors.hasErrors();
	}
}
