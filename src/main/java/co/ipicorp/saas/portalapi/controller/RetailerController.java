/**
 * RetailerController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     ntduong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.AccountType;
import co.ipicorp.saas.core.model.City;
import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.model.District;
import co.ipicorp.saas.core.model.Ward;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.core.service.CityService;
import co.ipicorp.saas.core.service.DistrictService;
import co.ipicorp.saas.core.service.WardService;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.model.OrderSellinStatus;
import co.ipicorp.saas.nrms.model.OrderSelloutStatus;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.RetailerInvoiceInfo;
import co.ipicorp.saas.nrms.model.dto.DateSearchCondition;
import co.ipicorp.saas.nrms.model.dto.RetailerSearchCondition;
import co.ipicorp.saas.nrms.service.OrderSellinService;
import co.ipicorp.saas.nrms.service.OrderSelloutService;
import co.ipicorp.saas.nrms.service.RetailerInvoiceInfoService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseService;
import co.ipicorp.saas.nrms.web.dto.RetailerDto;
import co.ipicorp.saas.nrms.web.dto.RetailerOrderDto;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.portalapi.form.AvatarUploadForm;
import co.ipicorp.saas.portalapi.form.RetailerCreationForm;
import co.ipicorp.saas.portalapi.form.RetailerExportForm;
import co.ipicorp.saas.portalapi.form.RetailerInvoiceInfoCreationForm;
import co.ipicorp.saas.portalapi.form.RetailerInvoiceInfoUpdateForm;
import co.ipicorp.saas.portalapi.form.RetailerSearchForm;
import co.ipicorp.saas.portalapi.form.RetailerUpdateForm;
import co.ipicorp.saas.portalapi.form.validator.RetailerEmailExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.RetailerInfoExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.RetailerSearchValidator;
import co.ipicorp.saas.portalapi.security.CustomerSessionInfo;
import co.ipicorp.saas.portalapi.util.Constants;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.annotation.AppJsonSchema;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.service.EmailService;
import grass.micro.apps.service.exception.ServiceException;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.exception.HttpNotFoundException;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * RetailerController. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
@RestController
@Api(tags = "Retailer APIs", description = "all APIs relate to create/search/detail retailer")
public class RetailerController {

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private RetailerService retailerService;

	@Autowired
	private RetailerInvoiceInfoService retailerInvoiceInfoService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private CityService cityService;

	@Autowired
	private DistrictService districtService;

	@Autowired
	private WardService wardService;

	@Autowired
	private OrderSellinService orderSellinService;

	@Autowired
	private RetailerWarehouseService rwhService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private OrderSelloutService orderSelloutService;

	private static String[] HEADER_STRING = { "ID", "Tên đại lý", "Mã đại lý", "Ngày tạo", "Điện thoại", "Thành phồ",
			"Số đơn hàng sellin", "Giá trị đơn sellin", "Số đơn hàng sellout", "Giá trị đơn sellout", "Trạng thái" };

	@GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> listAll(HttpServletRequest request, HttpServletResponse response) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				List<Retailer> retailers = retailerService.getAllActivated();
				int count = retailers.size();
				List<RetailerDto> dto = DtoFetchingUtils.fetchRetailers(retailers);
				getRpcResponse().addAttribute("retailers", dto);
				getRpcResponse().addAttribute("count", count);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_DETAIL_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> getRetailerDetail(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer retailerId) {
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance()
						.getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
				Customer customer = sessionInfo.getCustomer();
				if (customer == null || customer.isDeleted()) {
					throw new HttpNotFoundException();
				}
				Retailer retailer = retailerService.get(retailerId);
				if (retailer == null || retailer.isDeleted()) {
					throw new HttpNotFoundException();
				}
				DtoFetchingUtils.setOrderSellinService(orderSellinService);
				getRpcResponse().addAttribute("retailer", DtoFetchingUtils.fetchRetailer(retailer, customer.getId()));
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_SUMMARY_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> summary(HttpServletRequest request, HttpServletResponse response) {
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {

				Map<Status, Integer> counter = retailerService
						.countByStatus(Arrays.asList(Status.INACTIVE, Status.ACTIVE, Status.STATUS_TWO));

				long inactiveUsers = counter.get(Status.INACTIVE);
				long activeUsers = counter.get(Status.ACTIVE);
				long pendingUsers = counter.get(Status.STATUS_TWO);

				getRpcResponse().addAttribute("InactiveUsers", inactiveUsers);
				getRpcResponse().addAttribute("ActiveUsers", activeUsers);
				getRpcResponse().addAttribute("PendingUsers", pendingUsers);

				getRpcResponse().addAttribute("TotalUsers", inactiveUsers + activeUsers + pendingUsers);

				long haveOrders = orderSellinService.countRetailerHaveOrdersInDate(LocalDate.now());
				long haveFirstOrders = orderSellinService.countRetailerHaveFirstOrderInDate(LocalDate.now());

				getRpcResponse().addAttribute("HaveOrders", haveOrders);
				getRpcResponse().addAttribute("HaveFirstOrders", haveFirstOrders);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_SEARCH_ACTION)
	@Validation(validators = RetailerSearchValidator.class)
	@RequiresAuthentication
	public ResponseEntity<?> search(HttpServletRequest request, HttpServletResponse response,
			@GetBody RetailerSearchForm form) {
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				RetailerSearchCondition condition = this.createSearchCondition(form);

				long count = retailerService.count(condition);
				getRpcResponse().addAttribute("count", count);

				List<Retailer> retailers = new LinkedList<>();
				if (count > form.getSegment()) {
					retailers = retailerService.search(condition);
					DtoFetchingUtils.setOrderSellinService(orderSellinService);
				}

				List<RetailerDto> result = new LinkedList<>();
				if (CollectionUtils.isNotEmpty(retailers)) {
					result = this.getRetailerStatistics(retailers);
				}

				getRpcResponse().addAttribute("retailers", result);
			}

			/**
			 * @param retailers
			 * @return
			 */
			private List<RetailerDto> getRetailerStatistics(List<Retailer> retailers) {
				List<RetailerDto> result;
				result = DtoFetchingUtils.fetchRetailers(retailers);
				List<Integer> ids = retailers.stream().map(r -> r.getId()).collect(Collectors.toList());
				Map<Integer, Map<String, Object>> map = retailerService.getRetailerStatistic(ids,
						Arrays.asList(OrderSellinStatus.FINISH.toString()),
						Arrays.asList(OrderSelloutStatus.FINISH.toString()));
				for (RetailerDto dto : result) {
					Map<String, Object> stats = map.get(dto.getId());
					dto.setNumberOfOrderSellin((Long) stats.getOrDefault("numberOfOrderSellin", 0L));
					dto.setTotalCostOrderSellin((Double) stats.getOrDefault("totalCostOrderSellin", 0d));
					dto.setNumberOfOrderSellout((Long) stats.getOrDefault("numberOfOrderSellout", 0L));
					dto.setTotalCostOrderSellout((Double) stats.getOrDefault("totalCostOrderSellout", 0d));
				}
				return result;
			}

			/**
			 * @param form
			 * @return
			 */
			private RetailerSearchCondition createSearchCondition(RetailerSearchForm form) {
				RetailerSearchCondition condition = new RetailerSearchCondition();
				condition.setSegment(form.getSegment());
				condition.setOffset(form.getOffset());
				condition.setLimitSearch(true);
				condition.setFromDate(form.getFromDate());
				condition.setToDate(form.getToDate());
				condition.setStatus(form.getStatus());
				condition.setCityId(form.getCityId());
				condition.setKeyword(form.getKeyWord());
				if (form.getFromDate() != null && form.getToDate() != null) {
					condition.setEnableCreatedDate(true);
				} else {
					condition.setEnableCreatedDate(false);
				}
				return condition;
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_DETAIL_SEARCH_ACTION)
	@RequiresAuthentication
	public ResponseEntity<?> searchRetailerDetail(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer retailerId, @GetBody RetailerSearchForm form) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				try {
					Retailer retailer = retailerService.get(retailerId);
					if (retailer == null || retailer.isDeleted()) {
						throw new HttpNotFoundException();
					}
					if (form.getFromDate() == null || form.getToDate() == null) {
						throw new HttpNotFoundException();
					}
					DateSearchCondition condition = new DateSearchCondition();
					condition.setFromDate(form.getFromDate());
					condition.setToDate(form.getToDate());
					condition.setEnableCreatedDate(true);
					
					long totalDate = ChronoUnit.DAYS.between(form.getFromDate(), form.getToDate());
					List<String> statuses = Arrays.asList(OrderSellinStatus.FINISH.toString());
					List<Map<String, Object>> orderSellins = orderSellinService.searchOrderByRetailerId(condition,
							retailerId, statuses);
					RetailerOrderDto retailerSellin = this.fetchRetailerOrder(orderSellins, totalDate, true);
					List<Map<String, Object>> orderSellouts = orderSelloutService.searchOrderByRetailerId(condition,
							retailerId, statuses);
					RetailerOrderDto retailerSellout = this.fetchRetailerOrder(orderSellouts, totalDate, false);

					getRpcResponse().addAttribute("retailerSellin", retailerSellin);
					getRpcResponse().addAttribute("retailerSellout", retailerSellout);

				} catch (Exception e) {
					e.printStackTrace();
					errors.reject(ErrorCode.APP_1000_SYSTEM_ERROR);
				}
			}

			private RetailerOrderDto fetchRetailerOrder(List<Map<String, Object>> orders, long totalDate, boolean isSellin) {
				RetailerOrderDto dto = new RetailerOrderDto();
				Long totalOrder = 0L;
				Double finalCost = 0D;
				Double distribution = 0D;
				Double meanCost = 0D;
				for (Map<String, Object> order : orders) {
					totalOrder += Long.parseLong(order.get("noOrders").toString());
					finalCost += Double.parseDouble(order.get("finalCost").toString());
				}
				dto.setNumberOfOrder(totalOrder);
				dto.setTotalCostOrder(finalCost);
				if (totalDate != 0 && totalOrder != 0) {
					distribution = isSellin ? Math.round((totalDate * 1.0 / totalOrder) * 100.0) / 100.0
							: Math.round((totalOrder * 1.0 / totalDate) * 100.0) / 100.0;
					meanCost = Math.round((finalCost * 1.0 / totalOrder) * 100.0) / 100.0;
				}
				
				dto.setMeanCostOrder(meanCost);
				dto.setDistribution(distribution);
				return dto;
			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@PostMapping(value = ControllerAction.APP_PORTAL_RETAILER_ACTION)
	@Validation(schema = @AppJsonSchema("/schema/retailer_create.json"), validators = {
			RetailerInfoExistedValidator.class, RetailerEmailExistedValidator.class })
	@RequiresAuthentication
	@Logged
	public ResponseEntity<?> createRetailer(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RetailerCreationForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {

				CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance()
						.getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
				Customer customer = sessionInfo.getCustomer();
				if (customer != null) {
					String rawPassword = SystemUtils.getInstance().randomPassword();
					RetailerController.this.doCreateRetailer(form, customer, rawPassword, getRpcResponse(),
							(BindingResult) errors);
					getRpcResponse().addAttribute("message", "successful");
				} else {
					getRpcResponse().error(ErrorCode.APP_1000_SYSTEM_ERROR, "Session timeout");
				}
			}
		};

		return support.doSupport(request, response, errors, errorsProcessor);
	}

	@PostMapping(value = ControllerAction.APP_PORTAL_RETAILER_CHANGE_AVATAR_ACTION)
	@RequiresAuthentication
	@Logged
	public ResponseEntity<?> changeAvatar(HttpServletRequest request, HttpServletResponse response,
			@RequestBody AvatarUploadForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance()
						.getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
				Customer customer = sessionInfo.getCustomer();
				if (customer != null) {
					Retailer retailer = retailerService.get(form.getSubjectId());
					if (retailer == null || retailer.isDeleted()) {
						throw new HttpNotFoundException();
					}
					retailer = processUploadAndUpdateAvatar(form.getAvatar(), customer.getId(), retailer);
					getRpcResponse().addAttribute("message", "successful");
				} else {
					getRpcResponse().error(ErrorCode.APP_1000_SYSTEM_ERROR, "Session timeout");
				}
			}
		};
		return support.doSupport(request, response, errors, errorsProcessor);
	}

	@PutMapping(value = ControllerAction.APP_PORTAL_RETAILER_ACTION)
	@Logged
	@RequiresAuthentication
	public ResponseEntity<?> updateRetailer(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RetailerUpdateForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				try {
					CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance()
							.getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
					Customer customer = sessionInfo.getCustomer();
					if (customer == null || customer.isDeleted()) {
						throw new HttpNotFoundException();
					}
					Retailer retailer = RetailerController.this.doUpdateRetailer(customer.getId(), form,
							(BindingResult) errors);
					DtoFetchingUtils.setOrderSellinService(orderSellinService);
					getRpcResponse().addAttribute("retailer",
							co.ipicorp.saas.nrms.web.util.DtoFetchingUtils.fetchRetailer(retailer));
				} catch (Exception ex) {
					ex.printStackTrace();
					errors.reject(ErrorCode.APP_1000_SYSTEM_ERROR, "System error");
				}
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@PutMapping(value = ControllerAction.APP_PORTAL_RETAILER_CHANGE_STATUS_ACTION)
	@Logged
	@NoRequiredAuth
	public ResponseEntity<?> changeStatusRetailer(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer retailerId) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				try {
					Retailer retailer = retailerService.get(retailerId);
					if (retailer == null) {
						errors.reject(ErrorCode.APP_1201_RETAILER_NOT_EXIST, "Đại lý không tồn tại");
						return;
					}
					Status status = retailer.getStatus();
					switch (status) {
					case STATUS_TWO:
						retailer = acceptRetailer(retailer);
						break;
					case ACTIVE:
						retailer = pauseRetailer(retailer);
						break;
					default:
						retailer = activeRetailer(retailer);
						break;
					}
					retailerService.updatePartial(retailer);
				} catch (Exception e) {
					errors.reject(ErrorCode.APP_1000_SYSTEM_ERROR, "System error");
				}
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	protected Retailer activeRetailer(Retailer retailer) {
		retailer.setStatus(Status.ACTIVE);
		return retailer;
	}

	protected Retailer pauseRetailer(Retailer retailer) {
//		DateSearchCondition condition = new DateSearchCondition();
//		condition.setEnableCreatedDate(false);
//
//		List<String> statuses = Arrays.asList(OrderSellinStatus.FINISH.toString(),
//				OrderSellinStatus.CANCELED.toString(), OrderSellinStatus.RETURN.toString());
//		List<Map<String, Object>> orderSellins = orderSellinService.searchOrderByRetailerId(condition, retailer.getId(),
//				statuses);
//		List<Map<String, Object>> orderSellouts = orderSelloutService.searchOrderByRetailerId(condition, retailer.getId(),
//				statuses);
		retailer.setStatus(Status.INACTIVE);
		return retailer;
	}

	protected Retailer acceptRetailer(Retailer retailer) {
		String rawPassword = SystemUtils.getInstance().randomPassword();
		try {
			List<Account> accounts = accountService.getByLoginName(retailer.getMobile(),
					Arrays.asList(AccountType.RETAILER));
			if (CollectionUtils.isNotEmpty(accounts)) {
				Account account = accounts.get(0);
				String content = generateNewAccountEmailContent(retailer, rawPassword);
				RetailerController.this.emailService.sendMail(retailer.getEmail(), "Welcome to Megasop", content);
				String encodedPassword = SystemUtils.getInstance().md5(rawPassword + account.getSalt());
				account.setPassword(encodedPassword);
				accountService.updatePartial(account);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		retailer.setStatus(Status.ACTIVE);
		return retailer;
	}

	@PostMapping(value = ControllerAction.APP_PORTAL_RETAILER_EXPORT_ACTION, produces = {
			MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@Logged
	@RequiresAuthentication
	public ResponseEntity<?> export(HttpServletRequest request, HttpServletResponse response,
			@RequestBody RetailerExportForm form) throws IOException {

		if (form != null && form.getRetailers().size() > 0) {
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION,
					String.format("attachment; filename=Error_Items_List_" + currentDateTime + ".xlsx"));
			headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
			headers.add(HttpHeaders.PRAGMA, "no-cache");
			headers.add(HttpHeaders.EXPIRES, "0");
			ByteArrayInputStream in = exportData(form.getRetailers());

			byte[] content = IOUtils.toByteArray(in);
			ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(content);
			return responseEntity;
		}

		RpcResponse rpcResponse = new RpcResponse();
		rpcResponse.error(ErrorCode.APP_1000_SYSTEM_ERROR, "Error");
		return new ResponseEntity<RpcResponse>(rpcResponse, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	private ByteArrayInputStream exportData(List<RetailerDto> retailers) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			XSSFSheet sheet = workbook.createSheet("ds_dai_ly");

			// Header
			Row headerRow = sheet.createRow(0);

			CellStyle style = workbook.createCellStyle();
			XSSFFont font = workbook.createFont();
			font.setBold(true);
			font.setFontHeight(16);
			style.setFont(font);

			for (int col = 0; col < HEADER_STRING.length; col++) {
				Cell cell = headerRow.createCell(col);
				cell.setCellValue(HEADER_STRING[col]);
				cell.setCellStyle(style);
			}
			int rowIdx = 1;
			for (RetailerDto retailer : retailers) {
				Row row = sheet.createRow(rowIdx++);
				City city = cityService.get(retailer.getCityId());
				row.createCell(0).setCellValue(retailer.getId());
				row.createCell(1).setCellValue(retailer.getName());
				row.createCell(2).setCellValue(retailer.getRetailerCode());
				row.createCell(3).setCellValue(retailer.getCreatedDate().toString());
				row.createCell(4).setCellValue(retailer.getMobile());
				row.createCell(5).setCellValue(city.getName());
				row.createCell(6).setCellValue(retailer.getNumberOfOrderSellin());
				row.createCell(7).setCellValue(retailer.getTotalCostOrderSellin());
				row.createCell(8).setCellValue(retailer.getNumberOfOrderSellout());
				row.createCell(9).setCellValue(retailer.getTotalCostOrderSellout());
				row.createCell(10).setCellValue(retailer.getStatus().toString());
			}
			workbook.write(outStream);
			return new ByteArrayInputStream(outStream.toByteArray());

		} catch (Exception e) {
			throw new RuntimeException("Fail to download file: " + e.getMessage());
		}
	}

	protected Retailer doUpdateRetailer(Integer customerId, RetailerUpdateForm form, BindingResult errors)
			throws Exception {
		Integer retailerId = form.getId();
		Retailer retailer = this.retailerService.get(retailerId);
		if (retailer == null || retailer.isDeleted()) {
			throw new HttpNotFoundException("Can not find the retailer with id " + retailerId);
		}

		updateAccountInformation(customerId, retailer.getAccountId(), form);
		retailer.setName(StringUtils.isNotEmpty(form.getFullName()) ? form.getFullName() : retailer.getName());
		retailer.setEmail(form.getEmail());
		retailer.setRetailerSign(
				StringUtils.isNotEmpty(form.getRetailerSign()) ? form.getRetailerSign() : retailer.getRetailerSign());
		retailer.setRetailerIdCard(form.getRetailerIdCard());
		retailer.setBirthday(form.getBirthday() != null ? form.getBirthday() : retailer.getBirthday());
		retailer.setAddress(StringUtils.isNotEmpty(form.getAddress()) ? form.getAddress() : retailer.getAddress());
		retailer.setCityId(form.getCityId() != null ? form.getCityId() : retailer.getCityId());
		retailer.setDistrictId(form.getDistrictId() != null ? form.getDistrictId() : retailer.getDistrictId());
		retailer.setWardId(form.getWardId() != null ? form.getWardId() : retailer.getWardId());
		retailer.setFullAddress(buildFullAddress(form.getAddress(), retailer.getCityId(), retailer.getDistrictId(),
				retailer.getWardId()));
		retailer.setStatus(form.getStatus() != null ? Status.fromValue(form.getStatus()) : retailer.getStatus());

		retailer = this.retailerService.updatePartial(retailer);
		if (form.getRetailerInvoice() != null) {
			updateRetailerInvoice(retailerId, form.getRetailerInvoice());
		}
		return retailer;
	}

	private RetailerInvoiceInfo updateRetailerInvoice(Integer retailerId, RetailerInvoiceInfoUpdateForm form)
			throws Exception {
		RetailerInvoiceInfo retailerInvoice = this.retailerInvoiceInfoService.get(form.getId());
		if (retailerInvoice == null || retailerInvoice.isDeleted()) {
			throw new HttpNotFoundException(
					"Can not find the retailerInvoice with id " + form.getId() + " belongs to retailer " + retailerId);
		}

		retailerInvoice.setRetailerInvoiceName(
				StringUtils.isNotEmpty(form.getRetailerInvoiceName()) ? form.getRetailerInvoiceName()
						: retailerInvoice.getRetailerInvoiceName());
		retailerInvoice.setAddressText(StringUtils.isNotEmpty(form.getAddressText()) ? form.getAddressText()
				: retailerInvoice.getAddressText());
		retailerInvoice
				.setTaxNo(StringUtils.isNotEmpty(form.getTaxNo()) ? form.getTaxNo() : retailerInvoice.getTaxNo());
		retailerInvoice.setTel(StringUtils.isNotEmpty(form.getTel()) ? form.getTel() : retailerInvoice.getTel());
		retailerInvoice
				.setTelExt(StringUtils.isNotEmpty(form.getTelExt()) ? form.getTelExt() : retailerInvoice.getTelExt());
		retailerInvoice.setBankName(
				StringUtils.isNotEmpty(form.getBankName()) ? form.getBankName() : retailerInvoice.getBankName());
		retailerInvoice.setBankBranch(
				StringUtils.isNotEmpty(form.getBankBranch()) ? form.getBankBranch() : retailerInvoice.getBankBranch());
		retailerInvoice.setBankAccountNo(StringUtils.isNotEmpty(form.getBankAccountNo()) ? form.getBankAccountNo()
				: retailerInvoice.getBankAccountNo());
		retailerInvoice.setBankAccountName(StringUtils.isNotEmpty(form.getBankAccountName()) ? form.getBankAccountName()
				: retailerInvoice.getBankAccountName());
		retailerInvoice
				.setStatus(form.getStatus() != null ? Status.fromValue(form.getStatus()) : retailerInvoice.getStatus());
		retailerInvoice = this.retailerInvoiceInfoService.updatePartial(retailerInvoice);

		return retailerInvoice;
	}

	private Account updateAccountInformation(Integer customerId, Integer accountId, RetailerUpdateForm form)
			throws Exception {
		Account account = this.accountService.get(accountId);
		if (account != null && customerId.equals(account.getCustomerId())) {
			if (StringUtils.isNotEmpty(form.getEmail())) {
				account.setEmail(form.getEmail());
				account = this.accountService.updatePartial(account);
			}
		} else {
			throw new HttpNotFoundException(
					"Can not find the Account with id " + accountId + " belongs to customer " + customerId);
		}
		return account;
	}

	protected Retailer doCreateRetailer(RetailerCreationForm form, Customer customer, String rawPassword,
			RpcResponse rpcResponse, BindingResult errors) {
		Account account = this.doCreateAccount(form, rawPassword);
		Retailer retailer = null;
		if (account != null) {
			retailer = new Retailer();
			retailer.setName(form.getFullName());
			retailer.setEmail(form.getEmail());
			String retailerCode = UUID.randomUUID().toString();
			retailer.setRetailerCode(retailerCode);
			retailer.setRetailerSign(form.getName());
			retailer.setBirthday(form.getBirthday());
			retailer.setRetailerIdCard(form.getRetailerIdCard());
			retailer.setMobile(form.getMobile());
			retailer.setAddress(form.getAddress());
			retailer.setCityId(form.getCityId());
			retailer.setWardId(form.getWardId());
			retailer.setDistrictId(form.getDistrictId());
			retailer.setLevel(1);
			retailer.setRetailerLv1(form.getRetailerLv1());
			retailer.setRetailerLv2(form.getRetailerLv2());
			retailer.setRetailerLv3(form.getRetailerLv3());
			retailer.setStatus(Status.STATUS_TWO);
			retailer.setAccountId(account.getId());
			retailer.setFullAddress(
					buildFullAddress(form.getAddress(), form.getCityId(), form.getDistrictId(), form.getWardId()));
			retailer.setImage("");
			try {
				Point point = new GeometryFactory().createPoint(new Coordinate(10, 5));
				retailer.setGeoPosition(point);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			retailer = this.retailerService.create(retailer);
			this.processUploadAndUpdateAvatar(form.getAvatar(), customer.getId(), retailer);
			this.autoGenerationRetailerCode(retailer, customer.getId());
			this.doCreateRetailerInvoice(createRetailerInvoiceInfoForm(retailer), retailer.getId(),
					Status.ACTIVE.getValue());
			this.rwhService.createDefaultForRetailer(retailer.getId());
		}

		return retailer;
	}

	private Retailer processUploadAndUpdateAvatar(String avatar, Integer customerId, Retailer retailer) {
		if (StringUtils.isNotBlank(retailer.getImage()) && avatar.contains(retailer.getImage())) {
			return retailer;
		}

		if (StringUtils.isNotEmpty(avatar)) {
			// delete old avatar
			String location = ResourceUrlResolver.getInstance().resolveFtpRetailerPath(customerId, "");
			if (StringUtils.isNotEmpty(retailer.getImage())) {
				this.fileStorageService.deleteFile(location + retailer.getImage());
			}

			String[] data = avatar.split(",");
			String avatarPath = uploadAvatarToFTP(location, avatar, customerId, retailer.getId());
			String extension = "png";

			try {
				extension = data[0].split(";")[0].split("/")[1];
			} catch (Exception e) {
				// do nothing
			}

			String path = avatarPath + "." + extension;
			retailer.setImage(path);
			retailerService.updatePartial(retailer);
		}

		return retailer;
	}

	private String uploadAvatarToFTP(String location, String avatar, Integer customerId, Integer retailerId) {
		String fileName = SystemUtils.getInstance().generateCode("C", customerId, "R", retailerId);
		this.fileStorageService.storFile(avatar, location, fileName);
		return fileName;
	}

	private RetailerInvoiceInfoCreationForm createRetailerInvoiceInfoForm(Retailer retailer) {
		RetailerInvoiceInfoCreationForm form = new RetailerInvoiceInfoCreationForm();
		form.setAddressText(retailer.getFullAddress());
		form.setBankAccountNo("");
		form.setBankAccountName("");
		form.setBankBranch("");
		form.setBankName("");
		form.setRetailerInvoiceName(retailer.getName());
		form.setTaxNo("");
		form.setTel("");
		form.setTelExt("");
		return form;
	}

	private String buildFullAddress(String address, Integer cityId, Integer districtId, Integer wardId) {
		String result = "";
		try {
			City city = this.cityService.get(cityId);
			District district = this.districtService.get(districtId);
			Ward ward = this.wardService.get(wardId);
			result = address + "," + ward.getName() + "," + district.getName() + "," + city.getName();
		} catch (Exception e) {
			// TODO: handle exception
		}

		return result;
	}

	protected Account doCreateAccount(RetailerCreationForm form, String rawPassword) {
		Account account = new Account();
		account.setCustomerId(1); // TODO: get from session.
		account.setLoginName(form.getUsername());
		account.setAccountType(AccountType.RETAILER);
		account.setEmail(form.getEmail());
		String salt = SystemUtils.getInstance().randomPassword() + SystemUtils.getInstance().randomPassword();
		account.setPassword(SystemUtils.getInstance().generatePassword(rawPassword, salt));
		account.setSalt(salt);
		account.setStatus(Status.ACTIVE);
		account = this.accountService.create(account);

		return account;
	}

	protected RetailerInvoiceInfo doCreateRetailerInvoice(RetailerInvoiceInfoCreationForm form, Integer id,
			Integer status) {
		RetailerInvoiceInfo retailerInvoice = new RetailerInvoiceInfo();
		retailerInvoice.setRetailerId(id);
		retailerInvoice.setRetailerInvoiceName(form.getRetailerInvoiceName());
		retailerInvoice.setAddressText(form.getAddressText());
		retailerInvoice.setTaxNo(form.getTaxNo());
		retailerInvoice.setTel(form.getTel());
		retailerInvoice.setTelExt(form.getTelExt());
		retailerInvoice.setBankName(form.getBankName());
		retailerInvoice.setBankBranch(form.getBankBranch());
		retailerInvoice.setBankAccountNo(form.getBankAccountNo());
		retailerInvoice.setBankAccountName(form.getBankAccountName());
		retailerInvoice.setStatus(Status.fromValue(status));

		this.retailerInvoiceInfoService.create(retailerInvoice);
		return retailerInvoice;
	}

	protected void autoGenerationRetailerCode(Retailer retailer, Integer customerId) {
		String retailerCode = "C" + customerId + "R" + retailer.getId();
		retailer.setRetailerCode(retailerCode);
		retailerService.updatePartial(retailer);
	}

	private String generateNewAccountEmailContent(Retailer retailer, String rawPassword) throws ServiceException {

		String content = this.getNewRetailerEmailContent();
		content = content.replace("[[FULLNAME]]", StringEscapeUtils.escapeHtml4(retailer.getName()));
		content = content.replace("[[USERNAME]]", StringEscapeUtils.escapeHtml4(retailer.getMobile()));
		content = content.replace("[[PASSWORD]]", StringEscapeUtils.escapeHtml4(rawPassword));
		content = content.replace("[[SUPPORT_EMAIL]]", StringEscapeUtils.escapeHtml4("support@ipisaas.com"));
		return content;
	}

	private String getNewRetailerEmailContent() {
		String content = null;

		BufferedReader br = null;
		try {
			InputStream stream = this.getClass().getResourceAsStream("/create_retailer.html");
			br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));

			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = br.readLine()) != null) {
				sb.append(line).append(System.getProperty("line.separator"));
			}
			content = sb.toString();
		} catch (Exception ex) {
			throw new ServiceException("GET EMAIL CONTENT ERROR: " + ex.getMessage(), ex);
		} finally {
			try {
				br.close();
			} catch (Exception e2) {
				// do nothing
			}
		}

		return content;
	}

}
