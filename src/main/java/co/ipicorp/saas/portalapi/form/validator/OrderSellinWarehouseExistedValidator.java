package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.OrderSellin;
import co.ipicorp.saas.nrms.model.Warehouse;
import co.ipicorp.saas.nrms.service.OrderSellinService;
import co.ipicorp.saas.nrms.service.WarehouseService;
import co.ipicorp.saas.portalapi.form.OrderSellinForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class OrderSellinWarehouseExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Autowired
	private OrderSellinService orderSellinService;
	
	@Autowired
    private WarehouseService warehouseService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		OrderSellinForm orderSellinForm = (OrderSellinForm) form;
		return this.validateById(orderSellinForm, errors);
	}
	
	private boolean validateById(OrderSellinForm form, Errors errors) {
		
	    OrderSellin orderSellin = null;
	    
		if (form.getOrderSellinId() != null ) {
			orderSellin = this.orderSellinService.getActivated(form.getOrderSellinId());
		}
		
		if (orderSellin == null ) {
            errors.reject(ErrorCode.APP_1801_ORDER_SELLIN_NOT_EXIST,
                    new Object[] { "OrderSellin", form.getOrderSellinId() },
                    ErrorCode.APP_1801_ORDER_SELLIN_NOT_EXIST);
        }
		
		Warehouse wh = null;
		if (form.getWarehouseId() != null ) {
		    wh = this.warehouseService.getActivated(form.getWarehouseId());
		}
		
		if (wh == null ) {
		    errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST,
                    new Object[] { "Warehouse Id",  form.getWarehouseId() },
                    ErrorCode.APP_1401_FIELD_NOT_EXIST);
        }
		
		return !errors.hasErrors();
	}
}
