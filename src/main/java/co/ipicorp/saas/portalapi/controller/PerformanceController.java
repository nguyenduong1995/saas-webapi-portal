/**
 * PerformanceController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import co.ipicorp.saas.nrms.model.OrderSellin;
import co.ipicorp.saas.nrms.model.OrderSellout;
import co.ipicorp.saas.nrms.model.dto.DateSearchCondition;
import co.ipicorp.saas.nrms.model.dto.OrderSellinSearchCondition;
import co.ipicorp.saas.nrms.service.OrderSellinService;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.portalapi.form.PerformanceSearchForm;
import co.ipicorp.saas.portalapi.form.validator.PerformanceSearchValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;

import com.sun.istack.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.GeneralController;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * PerformanceController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
@SuppressWarnings("unused")
public class PerformanceController extends GeneralController {
	
    private static final Logger logger = Logger.getLogger(PerformanceController.class);

	@Autowired
	private OrderSellinService orderSellinService;

	@Autowired
	private OrderSelloutService orderSelloutService;
	
	@Autowired
	private ErrorsKeyConverter errorsProcessor;
	
	@GetMapping(value = ControllerAction.APP_PORTAL_PERFORMANCE_ACTION)
	@NoRequiredAuth
	@Logged
	@Validation(validators = { PerformanceSearchValidator.class })
	public ResponseEntity<?> performance(HttpServletRequest request, HttpServletResponse response,
			@GetBody PerformanceSearchForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				OrderSellinSearchCondition condition = new OrderSellinSearchCondition();
				condition.setFromDate(form.getFromDate());
				condition.setToDate(form.getToDate());
				long totalDays = 1;
				DateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
				Date fromDate = null;
				Date toDate = null;
				if (form.getFromDate() != null && form.getToDate() != null) {
					condition.setEnableCreatedDate(true);
					
					try {
						fromDate = simpleDateFormat.parse(form.getFromDate().toString());
						toDate = simpleDateFormat.parse(form.getToDate().toString());
						
						long miliSeconds = toDate.getTime() - fromDate.getTime();
						totalDays = miliSeconds / (24*60*60*1000);
						
					} catch (ParseException e) {
						e.printStackTrace();
					}
				}
				
				List<OrderSellin> orderSellins = PerformanceController.this.orderSellinService.searchOrderSellin(condition, form.getRetailerId(), null);
				long orderSellinCount = orderSellins.stream().count();
				Double orderSellinTotalCost = orderSellins.stream().collect(Collectors.summingDouble(OrderSellin::getFinalCost));
				double countPerDay = (double)(orderSellinCount / totalDays);
				double orderSellinCountPerDay = Math.round(countPerDay * 10)/10;
				Double costPerDay = orderSellinTotalCost / totalDays;
				Double orderSellinCostPerDay = (double) (Math.round(costPerDay * 10)/10);
				getRpcResponse().addAttribute("orderSellinCount", orderSellinCount);
				getRpcResponse().addAttribute("orderSellinTotalCost", orderSellinTotalCost);
				getRpcResponse().addAttribute("orderSellinCountPerDay", orderSellinCountPerDay);
				getRpcResponse().addAttribute("orderSellinCostPerDay", orderSellinCostPerDay);
				
				List<OrderSellout> orderSellouts = PerformanceController.this.orderSelloutService.getAllByRetailerId(form.getRetailerId());
				long orderSelloutCount = 0;
				List<OrderSellout> listOrderSellouts = new LinkedList<OrderSellout>();
				if (orderSellouts != null && orderSellouts.size() > 0) {
					Stream<OrderSellout> streamSellout = orderSellouts.stream()
												 .filter(orderSellout -> orderSellout.getOrderDate().toLocalDate().compareTo(form.getFromDate()) >= 0 
												 && orderSellout.getOrderDate().toLocalDate().compareTo(form.getToDate()) <= 0);
					orderSelloutCount = streamSellout.count();
					listOrderSellouts = streamSellout.collect(Collectors.toList());
				}
				
				Double orderSelloutTotalCost = listOrderSellouts.stream().collect(Collectors.summingDouble(OrderSellout::getFinalCost));
				double countSoPerDay = (double)(orderSelloutCount / totalDays);
				double orderSelloutCountPerDay = Math.round(countSoPerDay * 10)/10;
				Double costSoPerDay = orderSelloutTotalCost / totalDays;
				Double orderSelloutCostPerDay = (double) (Math.round(costSoPerDay * 10)/10);
				getRpcResponse().addAttribute("orderSelloutCount", orderSelloutCount);
				getRpcResponse().addAttribute("orderSelloutTotalCost", orderSelloutTotalCost);
				getRpcResponse().addAttribute("orderSelloutCountPerDay", orderSelloutCountPerDay);
				getRpcResponse().addAttribute("orderSelloutCostPerDay", orderSelloutCostPerDay);
				
			}
		};
		
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);

	}
}
