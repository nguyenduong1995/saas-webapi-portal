/**
 * WarehouseImportTicketController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.web.util.CommonUtils;
import co.ipicorp.saas.nrms.model.ImportType;
import co.ipicorp.saas.nrms.model.WarehouseDailyRemain;
import co.ipicorp.saas.nrms.model.WarehouseImportTicket;
import co.ipicorp.saas.nrms.model.WarehouseImportTicketItem;
import co.ipicorp.saas.nrms.model.WarehouseItem;
import co.ipicorp.saas.nrms.model.WarehouseItemHistory;
import co.ipicorp.saas.nrms.model.WarehouseTotalItem;
import co.ipicorp.saas.nrms.model.dto.DateSearchCondition;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.UnitService;
import co.ipicorp.saas.nrms.service.WarehouseDailyRemainService;
import co.ipicorp.saas.nrms.service.WarehouseImportTicketItemService;
import co.ipicorp.saas.nrms.service.WarehouseImportTicketService;
import co.ipicorp.saas.nrms.service.WarehouseItemHistoryService;
import co.ipicorp.saas.nrms.service.WarehouseItemService;
import co.ipicorp.saas.nrms.service.WarehouseTotalItemService;
import co.ipicorp.saas.portalapi.dto.WarehouseImportTicketDto;
import co.ipicorp.saas.portalapi.form.WarehouseImportTicketForm;
import co.ipicorp.saas.portalapi.form.WarehouseImportTicketItemForm;
import co.ipicorp.saas.portalapi.form.WarehouseImportTicketSearchForm;
import co.ipicorp.saas.portalapi.form.validator.WarehouseExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.WarehouseImportTicketCreationValidator;
import co.ipicorp.saas.portalapi.form.validator.WarehouseImportTicketSearchValidator;
import co.ipicorp.saas.portalapi.security.CustomerSessionInfo;
import co.ipicorp.saas.portalapi.util.Constants;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.DtoFetchingUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * WarehouseImportTicketController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
@SuppressWarnings("unchecked")
@Api(tags = "Warehouse Import Ticket APIs", description = "all APIs relate to create/search/detail warehouse import ticket")
public class WarehouseImportTicketController {

	public static final String IMPORT_TICKET_CODE_PREFIX = "WH";
	public static final String IMPORT_TICKET_CODE_MIDDLE = "IMP";
	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private WarehouseItemService warehouseItemService;

	@Autowired
	private WarehouseItemHistoryService warehouseItemHistoryService;

	@Autowired
	private WarehouseTotalItemService warehouseTotalItemService;

	@Autowired
	private WarehouseDailyRemainService whDailyRemainService;

	@Autowired
	private WarehouseImportTicketService warehouseImportTicketService;

	@Autowired
	private WarehouseImportTicketItemService warehouseImportTicketItemService;

	@Autowired
	private ProductService productService;

	@Autowired
	private ProductVariationService productVariationService;

	@Autowired
	private UnitService unitService;

	@GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_IMPORT_TICKET_ACTION)
	@ResponseBody
	@Logged
	public ResponseEntity<?> listAll(HttpServletRequest request, HttpServletResponse response) {
		AppControllerSupport support = new AppControllerListingSupport() {

			@Override
			public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response,
					Errors errors, ErrorsKeyConverter errorsProcessor) {
				return warehouseImportTicketService.getAll();
			}

			@Override
			public String getAttributeName() {
				return "WarehouseImportTickets";
			}

			@Override
			public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
				return DtoFetchingUtils.fetchWarehouseImportTickets((List<WarehouseImportTicket>) entities);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_IMPORT_TICKET_SEARCH_ACTION)
	@ResponseBody
	@Validation(validators = { WarehouseImportTicketSearchValidator.class })
	@Logged
	public ResponseEntity<?> searchWarehouseImportTicket(HttpServletRequest request, HttpServletResponse response,
			@GetBody WarehouseImportTicketSearchForm form) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				DateSearchCondition condition = new DateSearchCondition();
				condition.setSegment(form.getSegment());
				condition.setOffset(form.getOffset());
				condition.setFromDate(form.getFromDate());
				condition.setToDate(form.getToDate());
				condition.setStatus(form.getStatus());
				if (form.getFromDate() != null && form.getToDate() != null) {
					condition.setEnableCreatedDate(true);
				} else {
					condition.setEnableCreatedDate(false);
				}

				long count = warehouseImportTicketService.count(condition, form.getWarehouseId(), form.getKeyword());
				getRpcResponse().addAttribute("count", count);

				List<WarehouseImportTicketDto> warehouseImpTicketDtos = new LinkedList<WarehouseImportTicketDto>();

				if (count > form.getSegment()) {
					List<WarehouseImportTicket> warehouseImpTickets = warehouseImportTicketService
							.searchWarehouseImportTicket(condition, form.getWarehouseId(), form.getKeyword());

					warehouseImpTicketDtos = (List<WarehouseImportTicketDto>) DtoFetchingUtils
							.fetchWarehouseImportTickets(warehouseImpTickets);
				}

				getRpcResponse().addAttribute("WarehouseImportTicketDtos", warehouseImpTicketDtos);

			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_IMPORT_TICKET_DETAIL_ACTION)
	@ResponseBody
	@Logged
	public ResponseEntity<?> warehouseImpTicketDetail(HttpServletRequest request, HttpServletResponse response,
			@GetBody WarehouseImportTicketSearchForm form) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				WarehouseImportTicket warehouseImpTicket = WarehouseImportTicketController.this.warehouseImportTicketService
						.getActivated(form.getWarehouseImportTicketId());
				WarehouseImportTicketDto warehouseImpTicketDto = fetchWarehouseImportTicketWithRelationInfo(
						warehouseImpTicket);
				getRpcResponse().addAttribute("warehouseImpTicketDto", warehouseImpTicketDto);

			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@PostMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_IMPORT_TICKET_ACTION)
	@Validation(validators = { WarehouseExistedValidator.class, WarehouseImportTicketCreationValidator.class })
	@ResponseBody
	@Logged
	public ResponseEntity<?> createWarehouseImportTicket(HttpServletRequest request, HttpServletResponse response,
			@RequestBody WarehouseImportTicketForm form) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance()
						.getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
				Customer customer = sessionInfo.getCustomer();
				if (customer != null) {
					WarehouseImportTicket warehouseImportTicket = WarehouseImportTicketController.this
							.processSaveOrUpdateWarehouseImportTicket(form, customer);
					getRpcResponse().addAttribute("warehouseImpTicket",
							fetchWarehouseImportTicketWithRelationInfo(warehouseImportTicket));
				}

			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	/**
	 * @param warehouseImpTicket
	 */
	protected WarehouseImportTicketDto fetchWarehouseImportTicketWithRelationInfo(
			WarehouseImportTicket warehouseImpTicket) {
		WarehouseImportTicketDto warehouseImpTicketDto = new WarehouseImportTicketDto();
		if (warehouseImpTicket != null) {
			DtoFetchingUtils.setTicketItemService(warehouseImportTicketItemService);
			DtoFetchingUtils.setProductService(productService);
			DtoFetchingUtils.setProductVariationService(productVariationService);
			DtoFetchingUtils.setUnitService(unitService);
			warehouseImpTicketDto = DtoFetchingUtils.fetchWarehouseImportTicket(warehouseImpTicket, true);
		}
		return warehouseImpTicketDto;
	}

	/**
	 * @param warehouseImportTicketForm
	 * @param customer
	 * @param isNew
	 * @return
	 */
	protected WarehouseImportTicket processSaveOrUpdateWarehouseImportTicket(
			WarehouseImportTicketForm warehouseImportTicketForm, Customer customer) {
		WarehouseImportTicket warehouseImportTicket = this.warehouseImportTicketService
				.saveOrUpdate(mappingWarehouseImportTicketFromForm(warehouseImportTicketForm, customer));
		;
		warehouseImportTicket.setImportTicketCode(CommonUtils.generateCode(IMPORT_TICKET_CODE_PREFIX,
				warehouseImportTicket.getWarehouseId(), IMPORT_TICKET_CODE_MIDDLE, warehouseImportTicket.getId()));
		List<WarehouseImportTicketItem> warehouseImportTicketItems = createOrUpdateWarehouseImportTicketItems(
				warehouseImportTicket, warehouseImportTicketForm.getWarehouseImportTicketItems());
		// create/update warehouse items
		createOrUpdateWarehouseItems(warehouseImportTicketItems);
		Double total = warehouseImportTicketItems.stream()
				.collect(Collectors.summingDouble(WarehouseImportTicketItem::getTotal));
		warehouseImportTicket.setTotal(total);
		warehouseImportTicket = this.warehouseImportTicketService.updatePartial(warehouseImportTicket);
		return warehouseImportTicket;
	}

	/**
	 * @param warehouseImportItems
	 * @return
	 */
	private List<WarehouseItem> createOrUpdateWarehouseItems(
			List<WarehouseImportTicketItem> warehouseImportTicketItems) {
		List<WarehouseItem> warehouseItems = new ArrayList<WarehouseItem>();
		for (WarehouseImportTicketItem warehouseImportTicketItem : warehouseImportTicketItems) {
			Integer warehouseId = warehouseImportTicketItem.getWarehouseId();
			Integer productId = warehouseImportTicketItem.getProductId();
			Integer productVariationId = warehouseImportTicketItem.getProductVariationId();
			String sku = warehouseImportTicketItem.getSku();
			Integer amount = warehouseImportTicketItem.getAmount();
			Integer oldAmount = 0;
			WarehouseItem item = this.warehouseItemService.getByWarehouseImportTicketItemInfo(warehouseId, productId,
					productVariationId, sku);

			if (item != null) {
				// update total amount
				oldAmount = item.getAmount();
				item.setAmount(oldAmount + amount);
				item = this.warehouseItemService.updatePartial(item);
			} else {
				// create new warehouse record
				item = this.warehouseItemService
						.create(new WarehouseItem(warehouseId, productId, productVariationId, sku, amount));
			}
			warehouseItems.add(item);
			// create or update history
			createOrUpdateWarehouseItemHistory(item.getId(), warehouseImportTicketItem, oldAmount);
			createOrUpdateWarehouseTotalItem(productId, productVariationId, sku, amount, item.getAmount());
			createOrUpdateWarehouseDailyRemain(item.getWarehouseId(), productId, productVariationId, sku, amount,
					item.getAmount());
		}
		return warehouseItems;
	}

	/*
	 * @param warehouseId
	 * 
	 * @param productId
	 * 
	 * @param productVariationId
	 * 
	 * @param amount
	 */
	private void createOrUpdateWarehouseDailyRemain(Integer warehouseId, Integer productId, Integer productVariationId,
			String sku, Integer amount, Integer totalAmount) {
		WarehouseDailyRemain daily = this.whDailyRemainService.getInWarehouseByProductInfo(warehouseId, productId,
				productVariationId);

		if (daily == null) {
			daily = new WarehouseDailyRemain();
			daily.setProductId(productId);
			daily.setProductVariationId(productVariationId);
			daily.setWarehouseId(warehouseId);
			daily.setSku(sku);
			daily.setAmountImport(totalAmount);
			daily.setAmount(totalAmount);
		} else {
			daily.setAmount(daily.getAmount() + totalAmount);
		}

		this.whDailyRemainService.saveOrUpdate(daily);
	}

	/**
	 * @param warehouseId
	 * @param productId
	 * @param productVariationId
	 * @param amount
	 */
	private void createOrUpdateWarehouseTotalItem(Integer productId, Integer productVariationId, String sku,
			Integer amount, Integer totalAmount) {
		WarehouseTotalItem warehouseTotalItem = this.warehouseTotalItemService
				.getByWarehouseImportTicketItemInfo(productId, productVariationId, sku);
		if (warehouseTotalItem == null) {
			warehouseTotalItem = new WarehouseTotalItem();
			warehouseTotalItem.setSku(sku);
			warehouseTotalItem.setProductId(productId);
			warehouseTotalItem.setProductVariationId(productVariationId);
		}
		warehouseTotalItem.setAmount(totalAmount);
		warehouseTotalItem.setAmountAvailable(totalAmount - warehouseTotalItem.getAmountInOrders());
		warehouseTotalItem.setAmountImport(warehouseTotalItem.getAmountImport() + amount);
		this.warehouseTotalItemService.saveOrUpdate(warehouseTotalItem);
	}

	/**
	 * @param warehouseImportTicketItem
	 */
	private void createOrUpdateWarehouseItemHistory(Integer warehouseItemId,
			WarehouseImportTicketItem warehouseImportTicketItem, Integer oldAmount) {
		Integer amount = warehouseImportTicketItem.getAmount();

		WarehouseItemHistory warehouseItemHistory = new WarehouseItemHistory();
		String props = "warehouseId,productId,productVariationId,sku,amount,importTicketCode,importTicketId";
		SystemUtils.getInstance().copyProperties(warehouseImportTicketItem, warehouseItemHistory, props.split(","));
		warehouseItemHistory.setWarehouseItemId(warehouseItemId);
		warehouseItemHistory.setChangeDate(LocalDateTime.now());
		warehouseItemHistory.setAmount(amount + oldAmount);
		warehouseItemHistory.setChangeType(WarehouseItemHistory.CHANGE_TYPE_ADDITINAL);
		warehouseItemHistory.setChangeAmount(amount);
		warehouseItemHistory.setOldAmount(oldAmount);
		warehouseItemHistory.setExtraData(new LinkedHashMap<>());
		System.err.println("warehouseItemHistory " + warehouseItemHistory);
		this.warehouseItemHistoryService.create(warehouseItemHistory);
	}

	/**
	 * @param warehouseImportTicket
	 * @param warehouseImportTicketItems
	 * @param errors
	 */
	private List<WarehouseImportTicketItem> createOrUpdateWarehouseImportTicketItems(
			WarehouseImportTicket warehouseImportTicket,
			List<WarehouseImportTicketItemForm> warehouseImportTicketItemsForm) {
		List<WarehouseImportTicketItem> warehouseImportItems = new ArrayList<WarehouseImportTicketItem>();
		for (WarehouseImportTicketItemForm form : warehouseImportTicketItemsForm) {
			WarehouseImportTicketItem item = this.warehouseImportTicketItemService
					.create(mappingWarehouseImportTicketItemFromForm(warehouseImportTicket, form));
			warehouseImportItems.add(item);
		}
		return warehouseImportItems;
	}

	/**
	 * @param warehouseImportTicket
	 * @param form
	 * @return
	 */
	private WarehouseImportTicketItem mappingWarehouseImportTicketItemFromForm(
			WarehouseImportTicket warehouseImportTicket, WarehouseImportTicketItemForm warehouseImportTicketItemForm) {
		WarehouseImportTicketItem item = new WarehouseImportTicketItem();
		item.setId(warehouseImportTicketItemForm.getId());
		item.setWarehouseId(warehouseImportTicket.getWarehouseId());
		item.setImportTicketId(warehouseImportTicket.getId());
		item.setImportTicketCode(warehouseImportTicket.getImportTicketCode());
		item.setProductId(warehouseImportTicketItemForm.getProductId());
		item.setProductVariationId(warehouseImportTicketItemForm.getProductVariationId());
		item.setSku(warehouseImportTicketItemForm.getSku());
		item.setAmount(warehouseImportTicketItemForm.getAmount());
		item.setIncomePrice(warehouseImportTicketItemForm.getIncomePrice());
		item.setTotal(warehouseImportTicketItemForm.getAmount() * warehouseImportTicketItemForm.getIncomePrice());// FIXME
																													// :
																													// will
																													// change
																													// formular
		item.setStatus(Status.ACTIVE);
		return item;
	}

	/**
	 * @param warehouseImportTicket
	 * @param form
	 * @param customer
	 * @return
	 */
	private WarehouseImportTicket mappingWarehouseImportTicketFromForm(WarehouseImportTicketForm form,
			Customer customer) {
		WarehouseImportTicket warehouseImportTicket = new WarehouseImportTicket();
		warehouseImportTicket.setWarehouseId(form.getWarehouseId());
		warehouseImportTicket.setImportTicketCode(IMPORT_TICKET_CODE_PREFIX + System.currentTimeMillis());
		warehouseImportTicket.setImportType(ImportType.COMPANY);
		warehouseImportTicket.setImportPerson(customer.getFullname());
		warehouseImportTicket.setImportDate(LocalDateTime.now());
		warehouseImportTicket.setApprovedPerson(customer.getFullname());
		warehouseImportTicket.setDescription(form.getDescription());
		warehouseImportTicket.setTotal(0d); // TODO: will change in the future
		warehouseImportTicket.setExtraData(new LinkedHashMap<>());
		warehouseImportTicket.setStatus(Status.ACTIVE);
		return warehouseImportTicket;
	}
}
