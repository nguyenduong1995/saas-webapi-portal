package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.Product;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.web.form.ProductForm;
import co.ipicorp.saas.nrms.web.form.ProductVariationForm;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class ProductExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return form instanceof ProductForm || form instanceof ProductVariationForm;
	}
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private ProductVariationService productVariationService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
	    Integer productId = null;
	    if (form instanceof ProductForm) {
	        productId = ((ProductForm) form).getProductId();
	    } else if (form instanceof ProductVariationForm) {
	        productId = ((ProductVariationForm) form).getProductId();
	    }
	    
	    String sku = "";
	    if (form instanceof ProductVariationForm) {
	        sku = ((ProductVariationForm) form).getSku();
	    }
		return this.validateByProductId(productId, errors) && this.validateBySku(sku, errors);
	}
	
	private boolean validateBySku(String sku, Errors errors) {
		if (StringUtils.isNotEmpty(sku)) {
			List<ProductVariation> productVariations = this.productVariationService.getBySku(sku);
			if (CollectionUtils.isNotEmpty(productVariations)) {
				errors.reject(ErrorCode.APP_1402_FILED_IS_EXISTED,
						new Object[] { "Sku", sku },
						ErrorCode.APP_1402_FILED_IS_EXISTED);
			}
		}
		return !errors.hasErrors();
	}

	private boolean validateByProductId(Integer productId, Errors errors) {
		if ( productId != null) {
			Product product = this.productService.get(productId);
			if ( product == null ) {
				errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST,
						new Object[] { "Product Id", productId },
						ErrorCode.APP_1401_FIELD_NOT_EXIST);
			}
		} else {
		    errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL, 
		                new Object[] { "Product Id"}, 
		                ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
		}
		return !errors.hasErrors();
	}
}
