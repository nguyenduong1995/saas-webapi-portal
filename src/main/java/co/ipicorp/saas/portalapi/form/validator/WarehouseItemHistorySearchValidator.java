package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.portalapi.form.RetailerWarehouseItemHistorySearchForm;
import co.ipicorp.saas.portalapi.form.WarehouseItemHistorySearchForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class WarehouseItemHistorySearchValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return form instanceof WarehouseItemHistorySearchForm || form instanceof RetailerWarehouseItemHistorySearchForm;
	}
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		WarehouseItemHistorySearchForm formSearch = new WarehouseItemHistorySearchForm();
		if (form instanceof RetailerWarehouseItemHistorySearchForm) {
			formSearch.setFromDate(((RetailerWarehouseItemHistorySearchForm) form).getFromDate());
			formSearch.setToDate(((RetailerWarehouseItemHistorySearchForm) form).getToDate());
		} else {
			formSearch = (WarehouseItemHistorySearchForm) form;
		}
		
		return this.checkDate(formSearch, errors);
	}
	
	private boolean checkDate(WarehouseItemHistorySearchForm formSearch, Errors errors) {
		
		if ( formSearch.getFromDate() != null && formSearch.getToDate() != null ) {
			if (formSearch.getFromDate().compareTo(formSearch.getToDate()) > 0) {
				errors.reject(ErrorCode.APP_1701_FROM_DATE_NO_REASONABLE,
						new Object[] { formSearch.getFromDate(), formSearch.getToDate() },
						ErrorCode.APP_1701_FROM_DATE_NO_REASONABLE);
				
				return !errors.hasErrors();
			}
			
			DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date fromDate = null;
			Date toDate = null;
			try {
				fromDate = simpleDateFormat.parse(formSearch.getFromDate().toString());
				toDate = simpleDateFormat.parse(formSearch.getToDate().toString());
				
				long amountDays = toDate.getTime() - fromDate.getTime();
				long limitDays = amountDays / (24*60*60*1000);
				
				if ( limitDays > 30 ) {
					errors.reject(ErrorCode.APP_1702_NUMBER_DAY_TOO_MANY,
							new Object[] { formSearch.getFromDate(), formSearch.getToDate(), limitDays },
							ErrorCode.APP_1702_NUMBER_DAY_TOO_MANY);
					
					return !errors.hasErrors();
				}
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		
		
		return !errors.hasErrors();
	}
}
