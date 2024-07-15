package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.WarehouseImportTicketForm;
import co.ipicorp.saas.portalapi.form.WarehouseImportTicketItemForm;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class WarehouseImportTicketCreationValidator extends AbstractFormValidator {
    @Autowired
    private ProductVariationService productVariationService;
    
	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Override
    public boolean doValidate(Serializable form, Errors errors) {
        WarehouseImportTicketForm warehouseImportTicketForm = (WarehouseImportTicketForm) form;
        return this.validateByProductVariation(warehouseImportTicketForm, errors);
    }
    
    private boolean validateByProductVariation(WarehouseImportTicketForm form, Errors errors) {
        if (CollectionUtils.isNotEmpty(form.getWarehouseImportTicketItems())) {
            for (WarehouseImportTicketItemForm itemForm : form.getWarehouseImportTicketItems()) {
                Integer productVariationId = itemForm.getProductVariationId();
                ProductVariation productVariation = this.productVariationService.get(productVariationId);
                validateProductVariation(productVariation, itemForm, errors);
            }
        } 
        return !errors.hasErrors();
    }

    private void validateProductVariation(ProductVariation productVariation, WarehouseImportTicketItemForm itemForm, Errors errors) {
        if ( productVariation == null ) {
            errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST,
                    new Object[] { "Product Variation Id", itemForm.getProductVariationId() },
                    ErrorCode.APP_1401_FIELD_NOT_EXIST);
        } else {
            System.err.println(productVariation);
            System.err.println(itemForm);
            if (productVariation.getProductId() != itemForm.getProductId()) {
                errors.reject(ErrorCode.APP_1403_FIELD_NOT_BELONG_TO_OBJECT,
                        new Object[] {"Product Variation Id", productVariation.getId(), "Product Id", productVariation.getProductId() },
                        ErrorCode.APP_1403_FIELD_NOT_BELONG_TO_OBJECT);
            }
            
            if (!productVariation.getSku().equals(itemForm.getSku())) {
                errors.reject(ErrorCode.APP_1403_FIELD_NOT_BELONG_TO_OBJECT,
                        new Object[] {"Sku", productVariation.getSku(), "Product Variation Id", productVariation.getId() },
                        ErrorCode.APP_1403_FIELD_NOT_BELONG_TO_OBJECT);
            }
        }
        
    }
}
