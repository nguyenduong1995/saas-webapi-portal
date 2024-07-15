package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.Attribute;
import co.ipicorp.saas.nrms.service.AttributeService;
import co.ipicorp.saas.nrms.web.form.ProductVariationAttributeForm;
import co.ipicorp.saas.nrms.web.form.ProductVariationForm;
import co.ipicorp.saas.nrms.web.util.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class AttributeExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return form instanceof ProductVariationForm;
	}
	
	@Autowired
	private AttributeService attributeService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
	    ProductVariationForm productVariationForm = (ProductVariationForm) form;
		return this.validateByAttributeIds(productVariationForm, errors);
	}
	
	private boolean validateByAttributeIds(ProductVariationForm productVariationForm, Errors errors) {
		for (ProductVariationAttributeForm attributeForm : productVariationForm.getAttributes()) {
		    Integer attributeId = attributeForm.getAttributeId();
		    if ( attributeId != null) {
	            Attribute attribute = this.attributeService.get(attributeId);
	            if ( attribute == null ) {
	                errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST,
	                        new Object[] {"Attribute Id", attributeId },
	                        ErrorCode.APP_1401_FIELD_NOT_EXIST);
	            } else if (attribute.getAttributeTypeId() != attributeForm.getAttributeTypeId()) {
	                errors.reject(ErrorCode.APP_1403_FIELD_NOT_BELONG_TO_OBJECT,
                            new Object[] {"Attribute Id", attributeId, "Attribute Type", attributeForm.getAttributeTypeId() },
                            ErrorCode.APP_1403_FIELD_NOT_BELONG_TO_OBJECT);
	            }
	        }
		}
		
		return !errors.hasErrors();
	}
}
