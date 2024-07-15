package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.search.ProductVariationSearchForm;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class ProductVariationSearchValidator extends AbstractFormValidator {

    @Override
    public boolean support(Serializable form) {
        return true;
    }

    @Override
    public boolean doValidate(Serializable form, Errors errors) {
        ProductVariationSearchForm productVariationSearchForm = (ProductVariationSearchForm) form;
        return this.validateSearchForm(productVariationSearchForm, errors);
    }

    private boolean validateSearchForm(ProductVariationSearchForm productVariationSearchForm, Errors errors) {
        
        checkNumberOfIdValueValid("Category Id Level 0", productVariationSearchForm.getCategoryIdLv0(), errors);
        checkNumberOfIdValueValid("Category Id Level 1", productVariationSearchForm.getCategoryIdLv1(), errors);
        checkNumberOfIdValueValid("Category Id Level 2", productVariationSearchForm.getCategoryIdLv2(), errors);
        
        Integer statusId = productVariationSearchForm.getStatus();
        if ( statusId!= null && statusId < 0) {
            errors.reject(ErrorCode.APP_1406_FIELD_IS_NOT_VALID,
                    new Object[] { "Status", statusId },
                    ErrorCode.APP_1406_FIELD_IS_NOT_VALID);
        }
        return !errors.hasErrors();
    }
    
    private void checkNumberOfIdValueValid(String fileName, Integer id, Errors errors) {
        if ( id!= null && id <= 0) {
            errors.reject(ErrorCode.APP_1405_FIELD_MUST_BE_GREATER_THAN_ZERO,
                    new Object[] { "fileName", id },
                    ErrorCode.APP_1405_FIELD_MUST_BE_GREATER_THAN_ZERO);
        }
    }

}
