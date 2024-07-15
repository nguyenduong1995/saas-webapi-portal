package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.service.PromotionService;
import co.ipicorp.saas.portalapi.form.PromotionCreationForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class PromotionCodeDuplicationValidator  extends AbstractFormValidator {
    
    @Autowired
    private PromotionService promotionService;

    @Override
    public boolean support(Serializable form) {
        
        return form instanceof PromotionCreationForm;
    }
    
    @Override
    public boolean doValidate(Serializable sForm, Errors errors) {
        PromotionCreationForm form = (PromotionCreationForm) sForm;
        String promotionCode = form.getPromotionCode();
        if (StringUtils.isNotBlank(promotionCode)) {
            Promotion promotion = promotionService.getByPromotionCode(promotionCode);
            if (promotion != null) {
                errors.reject(ErrorCode.APP_2110_PROMOTION_CODE_DUPLICATED,
                        new Object[] { promotionCode },
                        ErrorCode.APP_2110_PROMOTION_CODE_DUPLICATED);
            }
        }
        return !errors.hasErrors(); 
    }


}
 