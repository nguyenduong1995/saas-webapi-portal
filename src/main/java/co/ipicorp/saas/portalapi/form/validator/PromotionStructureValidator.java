package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.model.SubjectType;
import co.ipicorp.saas.nrms.service.PromotionService;
import co.ipicorp.saas.portalapi.form.PromotionCreationForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class PromotionStructureValidator extends AbstractFormValidator {
    @Autowired
    private PromotionService promotionService;
    
	@Override
	public boolean support(Serializable form) {
		return form instanceof PromotionCreationForm;
	}
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
	    PromotionCreationForm promotionForm = (PromotionCreationForm) form;
	    this.validatePromotionCode(promotionForm, errors);
		this.validatePromotionType(promotionForm, errors);
		this.validateSubjectType(promotionForm, errors);
		
		
		if (!errors.hasErrors()) {
		    this.validateWithSubjectType(promotionForm, errors);
		    this.validateDate(promotionForm, errors);
		}
		
		if (!errors.hasErrors()) {
            this.validateCondition(promotionForm, errors);
        }
	
		return !errors.hasErrors();	
	}
	
    private void validatePromotionType(PromotionCreationForm form, Errors errors) {
		Integer promotionType = form.getPromotionType();
		
		if ( promotionType != null) {
			List<Integer> list = Arrays.asList(new Integer[] { Promotion.PROMOTION_TYPE_CTKM, Promotion.PROMOTION_TYPE_CSBH });
			if (!list.contains(promotionType)) {
				errors.reject(ErrorCode.APP_2102_PROMOTION_TYPE_INVALID, new Object[] { promotionType }, "Promotion Type is invalid.");
			}
		} else {
		    errors.reject(ErrorCode.APP_2101_PROMOTION_TYPE_IS_REQUIRED, new Object[] { }, "Promotion Type is required.");
		}
		
	}
	
	private void validateSubjectType(PromotionCreationForm form, Errors errors) {
        Integer subjectType = form.getSubjectType();
        
        if (subjectType != null) {
            List<Integer> list = Arrays.asList(new Integer[] { SubjectType.RETAILER.getValue(), SubjectType.CONSUMER.getValue() });
            if (!list.contains(subjectType)) {
                errors.reject(ErrorCode.APP_2103_SUBJECT_TYPE_INVALID, new Object[] { subjectType }, "Subject Type is invalid.");
            }
        } else {
            errors.reject(ErrorCode.APP_2104_SUBJECT_TYPE_IS_REQUIRED, new Object[] { }, "Subject Type is required.");
        }
    }
	
	private void validateWithSubjectType(PromotionCreationForm form, Errors errors) {
	    Integer promotionType = form.getPromotionType();
	    Integer subjectType = form.getSubjectType();
	    
	    if (Promotion.PROMOTION_TYPE_CSBH == promotionType && SubjectType.RETAILER.getValue() != subjectType) {
	        errors.reject(ErrorCode.APP_2105_SUBJECT_TYPE_INVALID, new Object[] { subjectType }, "CSBH must have Subject Type is RETAILER.");
	    }
    }

	private void validateDate(PromotionCreationForm form, Errors errors) {
	    Integer promotionType = form.getPromotionType();
        Integer subjectType = form.getSubjectType();
        
        if (form.getStartDate() == null || form.getEndDate() == null) {
            errors.reject(ErrorCode.APP_2106_DATE_IS_REQUIRED, new Object[] { subjectType }, "Both startDate and endDate are required.");    
        } else if (form.getStartDate().isBefore(LocalDate.now())) {
            errors.reject(ErrorCode.APP_2203_STARTDATE_MUSTBE_AFTER_CURRENT_DATE, new Object[] { subjectType }, "startDate must be after current Date.");
        } else if (form.getStartDate().isAfter(form.getEndDate())) {
            errors.reject(ErrorCode.APP_2107_STARTDATE_MUSTBE_BEFORE_ENDDATE, new Object[] { subjectType }, "startDate must be before or equal endDate.");
        } else {
            if (Promotion.PROMOTION_TYPE_CTKM == promotionType && SubjectType.CONSUMER.getValue() == subjectType) {
                if (form.getPreparationDate() == null) {
                    errors.reject(ErrorCode.APP_2108_PREPARATION_DATE_IS_REQUIRED, new Object[] { subjectType }, "CTKM for End-User must have Preparation Date.");
                } else if (form.getPreparationDate().isBefore(LocalDate.now())) {
                    errors.reject(ErrorCode.APP_2204_PREPARATION_DATE_MUSTBE_AFTER_CURRENT_DATE, new Object[] { }, "Preparation must be after current Date.");
                } else if (form.getPreparationDate().isAfter(form.getStartDate())) {
                    errors.reject(ErrorCode.APP_2205_PREPARATION_DATE_MUSTBE_AFTER_START_DATE, new Object[] { subjectType }, "preparationDate must be before or equal startDate.");
                }
            }
        }
    }
	
	private void validatePromotionCode(PromotionCreationForm form, Errors errors) {
        String promotionCode = form.getPromotionCode();
        
        if (StringUtils.isBlank(promotionCode)) {
            errors.reject(ErrorCode.APP_2109_PROMOTION_CODE_IS_REQUIRED, new Object[] {}, "PromotionCode is required.");    
        } else {
            Promotion promotion = promotionService.getByPromotionCode(promotionCode);
            if (promotion != null) {
                errors.reject(ErrorCode.APP_2111_PROMOTION_CODE_WAS_USED, new Object[] { promotionCode }, "PromotionCode {1} was used.");
            }
        }
    }
	
	private void validateCondition(PromotionCreationForm form, Errors errors) {
        int conditionFormatId = form.getConditionFormatId();
        int rewardFormatId = form.getRewardFormatId();
        int conditionCompareType = form.getConditionComparitionType();
        // TODO:  Promotion
        
    }
}
