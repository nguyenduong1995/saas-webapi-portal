package co.ipicorp.saas.portalapi.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.nrms.model.WarehouseDailyRemain;
import co.ipicorp.saas.nrms.model.WarehouseTotalItem;
import co.ipicorp.saas.nrms.model.dto.WarehouseDailyRemainPeriodDto;
import co.ipicorp.saas.nrms.model.dto.WarehouseDailyRemainSearchCondition;
import co.ipicorp.saas.nrms.model.dto.WarehouseDailyRemainWithDateSearchCondition;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.UnitService;
import co.ipicorp.saas.nrms.service.WarehouseDailyRemainService;
import co.ipicorp.saas.nrms.service.WarehouseTotalItemService;
import co.ipicorp.saas.portalapi.dto.WarehouseDailyRemainDto;
import co.ipicorp.saas.portalapi.form.search.WarehouseDailyRemainDateSearchForm;
import co.ipicorp.saas.portalapi.form.search.WarehouseDailyRemainSearchForm;
import co.ipicorp.saas.portalapi.form.validator.WarehouseDailyRemainSearchValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.DtoFetchingUtils;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * 
 * WarehouseDailyRemainController. <<< Detail note.
 * 
 * @author thuy nguyen
 * @access public
 */
@RestController
@Api(tags = "Warehouse Daily Remain APIs", description = "all APIs relate to search warehouse daily remain ticket")
public class WarehouseDailyRemainController {

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private WarehouseDailyRemainService warehouseDailyRemainService;

	@Autowired
	private ProductVariationService productVariationService;

	@Autowired
	private ProductService productService;

	@Autowired
	private UnitService unitService;

	@Autowired
	private WarehouseTotalItemService warehouseTotalItemService;

	@GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_DAILY_REMAIN_ACTION + "/search")
	@RequiresAuthentication
	@Logged
	public ResponseEntity<?> searchWarehouseDailyRemain(HttpServletRequest request, HttpServletResponse response,
			@GetBody WarehouseDailyRemainSearchForm searchForm) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {

				WarehouseDailyRemainSearchCondition condition = createSearchForm(searchForm);
				long count = 0;
				DtoFetchingUtils.setProductVariationService(productVariationService);
				DtoFetchingUtils.setProductService(productService);
				DtoFetchingUtils.setUnitService(unitService);
				
				List<WarehouseDailyRemainDto> warehouseDailyRemainDtos = new LinkedList<WarehouseDailyRemainDto>();
				
				if (searchForm.getWarehouseId() == null) {
					count = warehouseTotalItemService.count(condition);
					List<WarehouseTotalItem> warehouseTotalItems = warehouseTotalItemService.search(condition);
					warehouseDailyRemainDtos = DtoFetchingUtils.fetchWarehouseTotalItems(warehouseTotalItems);
				} else {
					count = warehouseDailyRemainService.count(condition);

					List<WarehouseDailyRemain> warehouseDailyRemains = WarehouseDailyRemainController.this.warehouseDailyRemainService
							.search(condition);

					warehouseDailyRemainDtos = DtoFetchingUtils.fetchWarehouseDailyRemains(warehouseDailyRemains);
				}
				
				getRpcResponse().addAttribute("warehouseDailyRemains", warehouseDailyRemainDtos);
				getRpcResponse().addAttribute("count", count);

			}

			private WarehouseDailyRemainSearchCondition createSearchForm(WarehouseDailyRemainSearchForm searchForm) {
				WarehouseDailyRemainSearchCondition condition = new WarehouseDailyRemainSearchCondition();
				condition.setOffset(searchForm.getOffset());
				condition.setSegment(searchForm.getSegment());
				condition.setWarehouseId(searchForm.getWarehouseId());
				condition.setKeyWord(searchForm.getKeyWord());
				condition.setLimitSearch(true);
				return condition;
			}

		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_DAILY_REMAIN_ACTION + "/search/advance")
	@Validation(validators = { WarehouseDailyRemainSearchValidator.class })
	@RequiresAuthentication
	@Logged
	public ResponseEntity<?> searchWarehouseDailyRemainWithPeriod(HttpServletRequest request,
			HttpServletResponse response, @GetBody WarehouseDailyRemainDateSearchForm searchForm) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				WarehouseDailyRemainWithDateSearchCondition condition = new WarehouseDailyRemainWithDateSearchCondition();
				condition.setOffset(searchForm.getOffset());
				condition.setSegment(searchForm.getSegment());
				condition.setWarehouseId(searchForm.getWarehouseId());
				condition.setKeyWord(searchForm.getKeyWord());
				condition.setFromDate(searchForm.getFromDate());
				condition.setToDate(searchForm.getToDate());
				condition.setLimitSearch(true);

				long count = warehouseDailyRemainService.countDailyRemainPeriod(condition);
				getRpcResponse().addAttribute("count", count);

				List<WarehouseDailyRemainPeriodDto> warehouseDailyRemainPeriodDtos = WarehouseDailyRemainController.this.warehouseDailyRemainService
						.searchAdvance(condition);
				getRpcResponse().addAttribute("warehouseDailyRemains", warehouseDailyRemainPeriodDtos);
			}

		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}
}
