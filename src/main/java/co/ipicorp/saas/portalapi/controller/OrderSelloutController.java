package co.ipicorp.saas.portalapi.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.model.OrderSelloutStatus;
import co.ipicorp.saas.nrms.model.dto.DateSearchCondition;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.web.dto.OrderSelloutDto;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.portalapi.form.OrderSelloutForm;
import co.ipicorp.saas.portalapi.form.validator.OrderSelloutSearchValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

@RestController
public class OrderSelloutController {

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private OrderSelloutService orderSelloutService;

	@GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_ORDER_SELLOUT_SEARCH_ACTION)
	@Validation(validators = { OrderSelloutSearchValidator.class })
	@NoRequiredAuth
	public ResponseEntity<?> searchOrderSellout(HttpServletRequest request, HttpServletResponse response,
			@GetBody OrderSelloutForm form) {
		AppControllerSupport support = new AppControllerSupport() {
			
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				try {
					DateSearchCondition condition = new DateSearchCondition();
					condition.setLimitSearch(true);
					condition.setSegment(form.getSegment());
					condition.setOffset(form.getOffset());
					condition.setFromDate(form.getFromDate());
					condition.setToDate(form.getToDate());
					if (form.getFromDate() != null && form.getToDate() != null) {
						condition.setEnableCreatedDate(true);
					} else {
						condition.setEnableCreatedDate(false);
					}
					
					String status = StringUtils.isNotBlank(form.getStatus()) ? form.getStatus() : OrderSelloutStatus.FINISH.toString(); 
					long count = orderSelloutService.count(condition, form.getRetailerId(), status);
					
					getRpcResponse().addAttribute("count", count);

					if (count > form.getSegment()) {
						List<OrderSellout> orderSellouts = orderSelloutService.searchOrderSellout(condition,
								form.getRetailerId(), status);
						initFetchService();
						List<OrderSelloutDto> dtos = DtoFetchingUtils.fetchRetailerOrderSellouts(orderSellouts);
						getRpcResponse().addAttribute("orderSellouts", dtos);
					}
				} catch (Exception e) {
					e.printStackTrace();
					errors.reject(ErrorCode.APP_1000_SYSTEM_ERROR);
				}
			}
		};
		
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	protected void initFetchService() {
		DtoFetchingUtils.setOrderSelloutService(orderSelloutService);
	}
}
