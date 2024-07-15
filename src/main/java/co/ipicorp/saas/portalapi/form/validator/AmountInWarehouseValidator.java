package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.OrderSellinItem;
import co.ipicorp.saas.nrms.service.OrderSellinItemService;
import co.ipicorp.saas.portalapi.form.OrderSellinForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class AmountInWarehouseValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
//	@Autowired
//	private WarehouseTotalItemService warehouseTotalItemService;
//	
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
			
			List<Map<String, Object>> list = this.orderSellinItemService.getOrderSellinItemsWithWarehouse(orderSellinForm.getOrderSellinId(), orderSellinForm.getWarehouseId());
			for (Map<String, Object> map : list) {
			    int countInOrder = map.get("countInOrder") != null ? (Integer) map.get("countInOrder") : 0;
			    int countInWarehouse = map.get("countInOrder") != null ? (Integer) map.get("countInWarehouse") : 0;
			    if (countInOrder > countInWarehouse) {
			        errors.reject(ErrorCode.APP_1901_WAREHOUSE_TOTAL_ITEM_NOT_EXIST,
                            new Object[] { "Warehouse Total Item", map.get("productId"), map.get("productVariationId") },
                            ErrorCode.APP_1901_WAREHOUSE_TOTAL_ITEM_NOT_EXIST );
			    }
			}		
		}
		
		return !errors.hasErrors();
	}
}
