package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.Product;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.web.form.ProductForm;
import co.ipicorp.saas.nrms.web.util.ErrorCode;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class ProductCodeExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return form instanceof ProductForm;
	}
	
	@Autowired
	private ProductService productService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		ProductForm productForm = (ProductForm) form;
		return this.validateByProductCode(productForm, errors);
	}
	
	private boolean validateByProductCode(ProductForm productForm, Errors errors) {
		String productCode = productForm.getProductCode();
		if ( StringUtils.isNotEmpty(productCode)) {
			Product product = this.productService.getByProductCode(productCode);
			if ( product != null ) {
				errors.reject(ErrorCode.APP_1402_FILED_IS_EXISTED,
						new Object[] { "Product Code", productCode },
						ErrorCode.APP_1402_FILED_IS_EXISTED);
			}
		}
		return !errors.hasErrors();
	}
}
