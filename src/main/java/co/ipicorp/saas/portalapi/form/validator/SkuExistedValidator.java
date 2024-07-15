package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.web.form.ProductVariationForm;
import co.ipicorp.saas.nrms.web.util.ErrorCode;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;
import java.util.List;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class SkuExistedValidator extends AbstractFormValidator {

    @Override
    public boolean support(Serializable form) {
        return form instanceof ProductVariationForm;
    }
    
    @Autowired
    private ProductVariationService productVariationService;
    
    @Override
    public boolean doValidate(Serializable form, Errors errors) {
        ProductVariationForm productVariationForm = (ProductVariationForm) form;
        return this.validateBySku(productVariationForm, errors);
    }
    
    private boolean validateBySku(ProductVariationForm productVariationForm, Errors errors) {
        String sku = productVariationForm.getSku();
        if (StringUtils.isNotEmpty(sku)) {
        	List<ProductVariation> productVariations = this.productVariationService.getBySku(sku);
			if (CollectionUtils.isNotEmpty(productVariations)) {
                errors.reject(ErrorCode.APP_1402_FILED_IS_EXISTED,
                        new Object[] { "Sku" , sku },
                        ErrorCode.APP_1402_FILED_IS_EXISTED);
            }
        }
        return !errors.hasErrors();
    }
}
