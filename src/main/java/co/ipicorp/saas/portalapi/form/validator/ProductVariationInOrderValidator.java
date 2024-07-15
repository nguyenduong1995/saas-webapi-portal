package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.OrderSellinStatus;
import co.ipicorp.saas.nrms.model.OrderSelloutStatus;
import co.ipicorp.saas.nrms.service.OrderSellinService;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.web.form.ProductVariationForm;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class ProductVariationInOrderValidator extends AbstractFormValidator {

	@Autowired
	private OrderSellinService orderSellinService;
	
	@Autowired
	private OrderSelloutService orderSelloutService;
	
	@Override
	public boolean support(Serializable form) {
		return form instanceof ProductVariationForm;
	}

	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		ProductVariationForm productVariationForm = (ProductVariationForm) form;
		return this.validateOrdersByProductSku(productVariationForm, errors);
	}

	private boolean validateOrdersByProductSku(ProductVariationForm productVariationForm, Errors errors) {
		String sku = productVariationForm.getSku();
		long orderSellinCount = orderSellinService.countBySkuAndOrderStatus(sku, Arrays.asList(OrderSellinStatus.NEW.toString(), OrderSellinStatus.APPROVED.toString()));
		long orderSelloutCount = orderSelloutService.countBySkuAndOrderStatus(sku, Arrays.asList(OrderSelloutStatus.NEW.toString(), OrderSelloutStatus.APPROVED.toString()));
		
		if (productVariationForm.getStatus() == 0 && (orderSellinCount > 0 || orderSelloutCount > 0)) {
			errors.reject(ErrorCode.APP_2400_PRODUCT_VARIATION_IS_EXISTED_IN_ORDER);
		}
		return !errors.hasErrors();
	}

}
