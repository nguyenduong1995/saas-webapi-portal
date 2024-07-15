package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.OrderSellinItem;
import co.ipicorp.saas.nrms.model.WarehouseTotalItem;
import co.ipicorp.saas.nrms.service.OrderSellinItemService;
import co.ipicorp.saas.nrms.service.WarehouseTotalItemService;
import co.ipicorp.saas.portalapi.form.OrderSellinForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class AmountAvailableInWarehouseValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Autowired
	private WarehouseTotalItemService warehouseTotalItemService;
	
	@Autowired
	private OrderSellinItemService orderSellinItemService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		OrderSellinForm orderSellinForm = (OrderSellinForm) form;
		return this.validate(orderSellinForm, errors);
	}
	
	private boolean validate(OrderSellinForm orderSellinForm, Errors errors) {
		
		if ( orderSellinForm.getOrderSellinId() != null ) {
			
			List<OrderSellinItem> orderSellinItems = this.orderSellinItemService.getAllBySellinId(orderSellinForm.getOrderSellinId());
			if (orderSellinItems == null) {
				errors.reject(ErrorCode.APP_1802_ORDER_SELLIN_ITEM_NOT_EXIST,
						new Object[] { "Order Sellin Item", orderSellinForm.getOrderSellinId() },
						ErrorCode.APP_1802_ORDER_SELLIN_ITEM_NOT_EXIST );
				
				return !errors.hasErrors();
			}
			
			for (OrderSellinItem item : orderSellinItems) {
				
				WarehouseTotalItem warehouseTotalItem = this.warehouseTotalItemService.getBySellinItemInfo(item.getProductId(), item.getProductVariationId());
				if ( warehouseTotalItem == null ) {
					errors.reject(ErrorCode.APP_1901_WAREHOUSE_TOTAL_ITEM_NOT_EXIST,
							new Object[] { "Warehouse Total Item", item.getProductId(), item.getProductVariationId() },
							ErrorCode.APP_1901_WAREHOUSE_TOTAL_ITEM_NOT_EXIST );
					
					return !errors.hasErrors();
				}
				
				Integer amount = warehouseTotalItem.getAmountAvailable() - item.getTotalAmount();
				if ( amount < 0) {
					errors.reject(ErrorCode.APP_1902_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH,
							new Object[] { "Warehouse Total Item", warehouseTotalItem.getId(), Math.abs(amount) },
								  ErrorCode.APP_1902_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH);
					
					return !errors.hasErrors();
				}
				
	    	}
		}
		
		return !errors.hasErrors();
	}
}
