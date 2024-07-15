/**
 * OrderSellinController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.nrms.model.OrderSellin;
import co.ipicorp.saas.nrms.model.OrderSellinStatus;
import co.ipicorp.saas.nrms.model.dto.OrderSellinCancelType;
import co.ipicorp.saas.nrms.model.dto.OrderSellinSearchCondition;
import co.ipicorp.saas.nrms.service.OrderSellinItemService;
import co.ipicorp.saas.nrms.service.OrderSellinPromotionLimitationDetailRewardService;
import co.ipicorp.saas.nrms.service.OrderSellinPromotionService;
import co.ipicorp.saas.nrms.service.OrderSellinService;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.PromotionService;
import co.ipicorp.saas.nrms.service.UnitService;
import co.ipicorp.saas.nrms.web.dto.OrderSellinDto;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.OrderSellinForm;
import co.ipicorp.saas.portalapi.form.validator.OrderSellinSearchValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * OrderSellinController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
public class OrderSellinController {

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private OrderSellinService orderSellinService;

	@Autowired
	private OrderSellinItemService orderSellinItemService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductResourceService productResourceService;

	@Autowired
	private ProductVariationService productVariationService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private OrderSellinPromotionService orderSellinPromotionService;

	@Autowired
	private OrderSellinPromotionLimitationDetailRewardService orderSellinPromotionRewardService;

	private void initFetchService() {
		DtoFetchingUtils.setOrderSellinItemService(orderSellinItemService);
		DtoFetchingUtils.setProductService(productService);
		DtoFetchingUtils.setProductResourceService(productResourceService);
		DtoFetchingUtils.setProductVariationService(productVariationService);
		DtoFetchingUtils.setUnitService(unitService);

		DtoFetchingUtils.setPromotionService(promotionService);
		DtoFetchingUtils.setOrderSellinPromotionService(orderSellinPromotionService);
		DtoFetchingUtils.setOrderSellinPromotionRewardService(orderSellinPromotionRewardService);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLIN_ACTION)
	public ResponseEntity<?> listAllByRetailerId(HttpServletRequest request, HttpServletResponse response,
			@GetBody OrderSellinForm form) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				List<Map<String, Object>> orderSellins = null;
				long count = orderSellinService.countByRetailerId(form.getRetailerId());
				getRpcResponse().addAttribute("count", count);

				if (count > form.getSegment()) {
					orderSellins = orderSellinService.getAllByRetailerId(form.getRetailerId(), form.getSegment(),
							form.getOffset());
					getRpcResponse().addAttribute("OrderSellins", orderSellins);
				}
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLIN_SEARCH_ACTION)
	@Validation(validators = { OrderSellinSearchValidator.class })
	public ResponseEntity<?> searchOrderSellin(HttpServletRequest request, HttpServletResponse response,
			@GetBody OrderSellinForm form) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				OrderSellinSearchCondition condition = new OrderSellinSearchCondition();
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
				
				if (StringUtils.isNotBlank(form.getKeyWord())) {
					condition.setKeyword(form.getKeyWord());
				}

				long count = orderSellinService.count(condition, form.getRetailerId(), form.getStatus());
				getRpcResponse().addAttribute("count", count);

				if (count > form.getSegment()) {
					List<OrderSellin> orderSellins = orderSellinService.searchOrderSellin(condition,
							form.getRetailerId(), form.getStatus());
					initFetchService();
					List<OrderSellinDto> orderSellinDtos = DtoFetchingUtils.fetchOrderSellins(orderSellins);
					getRpcResponse().addAttribute("OrderSellins", orderSellinDtos);
				}
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}
	
	@GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_ORDER_SELLIN_SEARCH_ACTION)
	@Validation(validators = { OrderSellinSearchValidator.class })
	public ResponseEntity<?> searchRetailerOrderSellin(HttpServletRequest request, HttpServletResponse response,
			@GetBody OrderSellinForm form) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				OrderSellinSearchCondition condition = new OrderSellinSearchCondition();
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

				String status = StringUtils.isNotBlank(form.getStatus()) ? form.getStatus() : OrderSellinStatus.FINISH.toString();
				long count = orderSellinService.count(condition, form.getRetailerId(), status);
				getRpcResponse().addAttribute("count", count);

				if (count > form.getSegment()) {
					List<OrderSellin> orderSellins = orderSellinService.searchOrderSellin(condition,
							form.getRetailerId(), status);
					initFetchService();
					List<OrderSellinDto> orderSellinDtos = DtoFetchingUtils.fetchRetailerOrderSellins(orderSellins);
					getRpcResponse().addAttribute("orderSellins", orderSellinDtos);
				}
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLIN_COUNT_ACTION)
	@NoRequiredAuth
	public ResponseEntity<?> countOrderSellin(HttpServletRequest request, HttpServletResponse response,
			@GetBody OrderSellinForm form) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {

				try {
					long totalOrderSellin = orderSellinService.getTotalOrder(form.getFromDate());
					Map<String, Integer> orderCount = orderSellinService.countByOrderStatus(form.getFromDate());
					Integer newOrderCount = orderCount.get(OrderSellinStatus.NEW.toString()) == null ? 0
							: orderCount.get(OrderSellinStatus.NEW.toString());
					Integer approveOrderCount = orderCount.get(OrderSellinStatus.APPROVED.toString()) == null ? 0
							: orderCount.get(OrderSellinStatus.APPROVED.toString());
					Integer deliveredOrderCount = orderCount.get(OrderSellinStatus.DELIVERED.toString()) == null ? 0
							: orderCount.get(OrderSellinStatus.DELIVERED.toString());
					Integer finishOrderCount = orderCount.get(OrderSellinStatus.FINISH.toString()) == null ? 0
							: orderCount.get(OrderSellinStatus.FINISH.toString());
					Integer returnOrderCount = orderCount.get(OrderSellinStatus.RETURN.toString()) == null ? 0
							: orderCount.get(OrderSellinStatus.RETURN.toString());
					
					Map<OrderSellinCancelType, Long> canceledMap = orderSellinService.countCanceledOrder(form.getFromDate());
					
					long canceledByOutStock = canceledMap.getOrDefault(OrderSellinCancelType.STOCK_EMPTY, 0L);
					long canceledByCustomer = canceledMap.getOrDefault(OrderSellinCancelType.CUSTOMER_REQUEST, 0L);
					long canceledApprovedOrderCount = canceledMap.getOrDefault(OrderSellinCancelType.CANCEL_APPROVED_ORDER, 0L);
					long canceledByOtherCount = canceledMap.getOrDefault(OrderSellinCancelType.OTHER, 0L);

					// Tổng số đơn
					getRpcResponse().addAttribute("totalOrderCount", totalOrderSellin);
					// Số đơn chưa xác nhận
					getRpcResponse().addAttribute("newOrderCount", newOrderCount);
					// Số đơn đã xác nhận
					getRpcResponse().addAttribute("approveOrderCount", approveOrderCount);
					// Số đơn đã giao
					getRpcResponse().addAttribute("deliveredOrderCount", deliveredOrderCount);
					// Số đơn hoàn thành
					getRpcResponse().addAttribute("finishOrderCount", finishOrderCount);
					// Số đơn bị trả
					getRpcResponse().addAttribute("returnOrderCount", returnOrderCount);
					// Số đơn chưa xác nhận bị hủy do KH
					getRpcResponse().addAttribute("canceledByCustomerCount", canceledByCustomer);
					// Số đơn chưa xác nhận bị hủy do hết hàng
					getRpcResponse().addAttribute("canceledByOutStockCount", canceledByOutStock);
					// Số đơn chưa xác nhận bị hủy lý do khác
					getRpcResponse().addAttribute("canceledByOtherCount", canceledByOtherCount);
					// Số đơn đã xác nhận bị hủy
					getRpcResponse().addAttribute("approveToCancelCount", canceledApprovedOrderCount);
				} catch (Exception e) {
					e.printStackTrace();
					errors.reject(ErrorCode.APP_1000_SYSTEM_ERROR);
				}
			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLIN_DETAIL_ACTION)
	@Logged
	public ResponseEntity<?> getOrderSellin(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer id) {
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				initFetchService();

				OrderSellin orderSellin = OrderSellinController.this.orderSellinService.getActivated(id);
				OrderSellinDto orderSellinDto = DtoFetchingUtils.fetchOrderSellin(orderSellin);
				getRpcResponse().addAttribute("order", orderSellinDto);
			}

		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLIN_DETAIL_ACTION + "/compareToWarehouse")
	@NoRequiredAuth
	public ResponseEntity<?> getOrderSellinItemsWithWarehouse(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer id, @RequestParam("warehouseId") Integer warehouseId) {
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				List<Map<String, Object>> items = orderSellinItemService.getOrderSellinItemsWithWarehouse(id.intValue(),
						warehouseId.intValue());

				processPromotions(items, id, warehouseId);

				getRpcResponse().addAttribute("items", items);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	protected void processPromotions(List<Map<String, Object>> items, int orderId, int warehouseId) {
		List<Integer> pvIds = items.stream().map(item -> (Integer) item.get("productVariationId"))
				.collect(Collectors.toList());

		List<Map<String, Object>> rewards = orderSellinItemService.getPromotionRewardItemsWithWarehouse(orderId,
				warehouseId);
		if (CollectionUtils.isNotEmpty(rewards)) {
			for (Map<String, Object> reward : rewards) {
				int idx = pvIds.indexOf((Integer) reward.get("productVariationId"));
				if (idx >= 0) {
					Map<String, Object> item = items.get(idx);

					int countPromotionRewardItemInOrder = (Integer) reward.get("countInOrder");
					int countInOrder = countPromotionRewardItemInOrder + (Integer) item.get("countInOrder");
					item.put("countInOrder", countInOrder);
				} else {
					items.add(reward);
				}
			}
		}
	}

}
