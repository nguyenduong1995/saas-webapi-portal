package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.service.PromotionService;
import co.ipicorp.saas.portalapi.util.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;
import grass.micro.apps.web.util.WebConstants;

@Component
public class PromotionExistedValidator extends AbstractFormValidator {

    @Autowired
    private PromotionService promotionService;

    @Override
    public boolean support(Serializable form) {
        return true;
    }

    @Override
    public boolean doValidate(Serializable form, Errors errors) {
        Integer id = (Integer) RequestContextHolder.getRequestAttributes()
                .getAttribute(WebConstants.APPS_API_RAW_PATH_VARIABLE_KEY, RequestAttributes.SCOPE_REQUEST);
        return this.validateForm(id, errors);
    }

    private boolean validateForm(Integer promotionId, Errors errors) {
        if (promotionId != null) {
            Promotion promotion = this.promotionService.getActivated(promotionId);
            if (promotion == null) {
                errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST, new Object[] { "promotion Id", promotionId }, ErrorCode.APP_1401_FIELD_NOT_EXIST);
            }
        } else {
            errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL, new Object[] { "promotion Id" }, ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
        }
        
        return !errors.hasErrors();
    }
}
