package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.OrderSellinItem;
import co.ipicorp.saas.nrms.model.WarehouseItem;
import co.ipicorp.saas.nrms.service.OrderSellinItemService;
import co.ipicorp.saas.nrms.service.WarehouseItemService;
import co.ipicorp.saas.portalapi.form.OrderSellinForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class WarehouseItemExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Autowired
    private OrderSellinItemService orderSellinItemService;
	
	@Autowired
	private WarehouseItemService warehouseItemService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		OrderSellinForm orderSellinForm = (OrderSellinForm) form;

		return this.validateById(orderSellinForm, errors);
	}
	
	private boolean validateById(OrderSellinForm orderSellinForm, Errors errors) {
		if ( orderSellinForm.getOrderSellinId() != null ) {
	    	List<OrderSellinItem> orderSellinItems =  this.orderSellinItemService.getAllBySellinId(orderSellinForm.getOrderSellinId());
	    	if (orderSellinItems == null || orderSellinItems.isEmpty()) {
	    		errors.reject(ErrorCode.APP_1802_ORDER_SELLIN_ITEM_NOT_EXIST,
						new Object[] { "Order Sellin Item", orderSellinForm.getOrderSellinId() },
						ErrorCode.APP_1802_ORDER_SELLIN_ITEM_NOT_EXIST );
				
				return errors.hasErrors();
	    	}
	    	
    		for (OrderSellinItem item : orderSellinItems) {
				WarehouseItem warehouseItem = this.warehouseItemService.getBySellinItemInfo(orderSellinForm.getWarehouseId(),
																		item.getProductId(), item.getProductVariationId());
						
				if ( warehouseItem == null ) {
					errors.reject(ErrorCode.APP_1803_WAREHOUSE_ITEM_NOT_EXIST,
							new Object[] { "Warehouse Item", orderSellinForm.getWarehouseId(), item.getProductId(), item.getProductVariationId()},
							ErrorCode.APP_1803_WAREHOUSE_ITEM_NOT_EXIST );
					
					return errors.hasErrors();
				}
				
				Integer amount = warehouseItem.getAmount() - item.getTotalAmount();
				if ( amount < 0) {
					errors.reject(ErrorCode.APP_1804_WAREHOUSE_ITEM_AMOUNT_NOT_ENOUGH,
							new Object[] { "Warehouse Item", warehouseItem.getId(), Math.abs(amount)},
								  ErrorCode.APP_1804_WAREHOUSE_ITEM_AMOUNT_NOT_ENOUGH);
					
					return errors.hasErrors();
				}
				
	    	}
		}
		
		return !errors.hasErrors();
	}
}
