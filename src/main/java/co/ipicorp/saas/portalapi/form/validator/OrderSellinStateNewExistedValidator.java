package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.OrderSellin;
import co.ipicorp.saas.nrms.model.OrderSellinStatus;
import co.ipicorp.saas.nrms.service.OrderSellinService;
import co.ipicorp.saas.portalapi.form.OrderSellinForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class OrderSellinStateNewExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
    
	@Autowired
    private OrderSellinService orderSellinService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		OrderSellinForm orderSellinForm = (OrderSellinForm) form;
		return this.validateById(orderSellinForm, errors);
	}
	
	private boolean validateById(OrderSellinForm orderSellinForm, Errors errors) {
		
		if ( orderSellinForm.getOrderSellinId() != null ) {
			OrderSellin orderSellin = this.orderSellinService.getActivated(orderSellinForm.getOrderSellinId());
	    	
	    	if ( !orderSellin.getOrderStatus().equals(OrderSellinStatus.NEW.toString())){
	    		errors.reject(ErrorCode.APP_1909_ORDER_SELLIN_STATE_NOT_EXACTLY,
						new Object[] { "Order Sellin Id", orderSellinForm.getOrderSellinId(), OrderSellinStatus.NEW.toString() },
						ErrorCode.APP_1909_ORDER_SELLIN_STATE_NOT_EXACTLY );
	    	}
    	}

		return !errors.hasErrors();
	}
}
