/**
 * TicketController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.ipicorp.saas.nrms.model.dto.WarehouseImportTicketItemDto;
import co.ipicorp.saas.nrms.service.WarehouseImportTicketItemService;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.dto.ErrorImportingDto;
import co.ipicorp.saas.portalapi.form.WarehouseImportTicketErrorForm;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.GeneralController;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * TicketController. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
@RestController
@Api(tags = "Warehouse Import Ticket Item APIs", description = "all APIs relate to upload/download warehouse import ticket item")
public class WarehouseImportTicketItemController extends GeneralController {

	private static final int ROW_START = 4;
	private static final int NUMBER_OF_ROW = 1005;
	private static final String FIELD_CAN_NOT_BE_NULL = "không được để trống";
	private static final String FILED_IS_DUPLICATED_AT_LINE = "nhập trùng tại dòng";
	private static final String FIELD_IS_NOT_EXIST = "không tồn tại";
	private static final String FIELD_HAS_TO_GREATER_THAN_ZERO = "phải lớn hơn 0";
	private static final String FIELD_IS_NOT_NUMBER = "phải là số";
	private static String TEMPLATE_DIRECTORY = "template/";
	private static String PRODUCT_LIST_IMPPORT_TEMPLATE_FILE_NAME = "Template nhập kho.xlsx";
	private static String SUCCESS_KEY = "successList";
	private static String FAILURE_KEY = "failureList";
	private static String SKU_TITTLE = "Mã sản phẩm";
	private static String ORDER_NUMBER_TITTLE = "STT";
	private static String AMOUNT_TITTLE = "Số lượng nhập";
	private static String[] HEADER_STRING = { "ID", "Mã sản phẩm", "Số lượng nhập", "Lỗi" };

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private WarehouseImportTicketItemService warehouseImportTicketItemService;

	@GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_IMPORT_TICKET_ITEM_DOWNLOAD_ACTION, produces = {
			MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@Logged
	public ResponseEntity<?> downloadTemplateFile() throws IOException {
		try {
			File templateFile = ResourceUtils
					.getFile("classpath:" + WarehouseImportTicketItemController.TEMPLATE_DIRECTORY
							+ WarehouseImportTicketItemController.PRODUCT_LIST_IMPPORT_TEMPLATE_FILE_NAME);
			FileInputStream fi = new FileInputStream(templateFile);
//            InputStreamResource resource = new InputStreamResource(new FileInputStream(templateFile));
			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION, String.format("attachment; filename=\"%s\"",
					URLEncoder.encode(templateFile.getName().replace(" ", "blank"), "UTF-8").replace("blank", " ")));
			headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
			headers.add(HttpHeaders.PRAGMA, "no-cache");
			headers.add(HttpHeaders.EXPIRES, "0");

			byte[] body = IOUtils.toByteArray(fi);
			ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
					.contentLength(templateFile.length()).contentType(MediaType.APPLICATION_OCTET_STREAM)
					.body(new ByteArrayResource(body));
			return responseEntity;
		} catch (Exception e) {
			System.err.println(e.getMessage());
			RpcResponse rpcResponse = new RpcResponse();
			rpcResponse.error(ErrorCode.APP_1000_SYSTEM_ERROR, e.getMessage());
			return new ResponseEntity<RpcResponse>(rpcResponse, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PostMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_IMPORT_TICKET_ERROR_DOWNLOAD_ACTION, produces = {
			MediaType.APPLICATION_OCTET_STREAM_VALUE })
	@ResponseBody
	@Logged
	public ResponseEntity<?> downloadErrorImportFile(HttpServletRequest request, HttpServletResponse response,
			@RequestBody WarehouseImportTicketErrorForm form) throws IOException {
		if (form != null && form.getErrorItems().size() > 0) {
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());

			HttpHeaders headers = new HttpHeaders();
			headers.add(HttpHeaders.CONTENT_DISPOSITION,
					String.format("attachment; filename=Error_Items_List_" + currentDateTime + ".xlsx"));
			headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
			headers.add(HttpHeaders.PRAGMA, "no-cache");
			headers.add(HttpHeaders.EXPIRES, "0");
			ByteArrayInputStream in = exportErrorData(form.getErrorItems());

			byte[] content = IOUtils.toByteArray(in);
			ResponseEntity<Object> responseEntity = ResponseEntity.ok().headers(headers)
					.contentType(MediaType.APPLICATION_OCTET_STREAM).body(content);
			return responseEntity;
		}
		RpcResponse rpcResponse = new RpcResponse();
		rpcResponse.error(ErrorCode.APP_1000_SYSTEM_ERROR, "Error");
		return new ResponseEntity<RpcResponse>(rpcResponse, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	private ByteArrayInputStream exportErrorData(List<ErrorImportingDto> dtos) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			ByteArrayOutputStream outStream = new ByteArrayOutputStream();
			XSSFSheet sheet = workbook.createSheet("nhap_kho_loi");

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
			for (ErrorImportingDto dto : dtos) {
				Row row = sheet.createRow(rowIdx++);
				row.createCell(0).setCellValue(dto.getOrderNumber());
				row.createCell(1).setCellValue(dto.getSku());
				row.createCell(2).setCellValue(dto.getAmount());
				row.createCell(3).setCellValue(dto.getError());
			}
			workbook.write(outStream);
			return new ByteArrayInputStream(outStream.toByteArray());

		} catch (Exception e) {
			throw new RuntimeException("Fail to download file: " + e.getMessage());
		}
	}

	@PostMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_IMPORT_TICKET_ITEM_UPLOAD_ACTION, consumes = "multipart/form-data", produces = {
			"application/json" })
	@ResponseBody
	@Logged
	public ResponseEntity<?> uploadItems(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile file) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				if (!FilenameUtils.getExtension(file.getOriginalFilename()).contains("xls;xlsx")) {
					try {
						Map<String, List<? extends Serializable>> data = WarehouseImportTicketItemController.this
								.readAndValidateData(file);
						getRpcResponse().addAttribute(SUCCESS_KEY, data.get(SUCCESS_KEY));
						getRpcResponse().addAttribute(FAILURE_KEY, data.get(FAILURE_KEY));
					} catch (Exception e) {
						System.err.println(e.getMessage());
						getRpcResponse().error(ErrorCode.APP_1000_SYSTEM_ERROR, e.getMessage());
					}
				} else {
					getRpcResponse().error(ErrorCode.APP_1000_SYSTEM_ERROR, "File format invalid");
				}
			}

		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	/**
	 * @param file
	 * @return
	 * @throws Exception
	 */
	protected Map<String, List<? extends Serializable>> readAndValidateData(MultipartFile file) throws Exception {
		Map<String, List<? extends Serializable>> result = new HashMap<String, List<? extends Serializable>>();
		Map<Integer, ErrorImportingDto> originalDataMap = new HashMap<Integer, ErrorImportingDto>();
		Map<String, String> producCodesMap = new HashMap<String, String>();// will store product code and line contains
																			// in excel file
		List<WarehouseImportTicketItemDto> successList = new ArrayList<>();
		List<ErrorImportingDto> failureList = new ArrayList<>();
		List<String> skuList = new ArrayList<String>();

		readImportingFile(file.getInputStream(), originalDataMap, skuList);

		Map<String, WarehouseImportTicketItemDto> warehouseImportTicketItemMap = getAllUnitInformation(skuList);
		validateExcelData(warehouseImportTicketItemMap, originalDataMap, successList, failureList, producCodesMap);
		result.put(SUCCESS_KEY, successList);
		result.put(FAILURE_KEY, failureList);
		return result;
	}

	/**
	 * @param inputStream
	 * @param originalDataMap
	 * @param skuList
	 * @throws Exception
	 */
	private void readImportingFile(InputStream inputStream, Map<Integer, ErrorImportingDto> originalDataMap,
			List<String> skuList) throws Exception {
		// Create Workbook instance holding reference to .xlsx file
		XSSFWorkbook workbook = new XSSFWorkbook(inputStream);
		try {
			// Get first/desired sheet from the workbook
			XSSFSheet sheet = workbook.getSheetAt(0);
			if (sheet.getLastRowNum() > NUMBER_OF_ROW) {
				throw new Exception("The number of data in file cannot exceed 1000 lines");
			}
			// Iterate through each rows one by one
			Iterator<Row> rowIterator = sheet.iterator();
			Row previousRow = null;
			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				// start to read data from row 5
				if (row.getRowNum() < ROW_START) {
					continue;
				}

				if (isRowEmpty(row) && isRowEmpty(previousRow)) {
					// throw new Exception("File không thể chứa 2 dòng rỗng liền nhau!");
					System.err.println("File cannot contain two consecutive empty lines !");
					break;
				}
				if (!isRowEmpty(row)) {
					// For each row, iterate through all the columns
					String orderNumber = row.getCell(0).getCellType() == Cell.CELL_TYPE_NUMERIC
							? String.valueOf((int) row.getCell(0).getNumericCellValue())
							: row.getCell(0).getStringCellValue();
					String sku = row.getCell(1).getCellType() == Cell.CELL_TYPE_NUMERIC
							? String.valueOf((int) row.getCell(1).getNumericCellValue())
							: row.getCell(1).getStringCellValue();
					String amount = String.valueOf((int) row.getCell(2).getNumericCellValue());

					// Add all to failure map with key is row number
					ErrorImportingDto errorDto = new ErrorImportingDto();
					errorDto.setOrderNumber(orderNumber);
					errorDto.setSku(sku);
					errorDto.setAmount(amount);
					originalDataMap.put(row.getRowNum(), errorDto);
					if (StringUtils.isNotEmpty(sku)) {
						skuList.add(sku);
					}
				}
				previousRow = row;
			}
		} catch (Exception ex) {
			System.err.println(ex.getMessage());
			throw new Exception("Import File Error!");
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException ex) {
					System.err.println(ex.getMessage());
					throw new Exception("Can not close file!");
				}
			}
		}

	}

	/**
	 * @param skuList
	 * @return
	 */
	private Map<String, WarehouseImportTicketItemDto> getAllUnitInformation(List<String> skuList) {
		Map<String, WarehouseImportTicketItemDto> warehouseImportTicketItemMap = new HashMap<String, WarehouseImportTicketItemDto>();
		Collection<List<String>> skuForQueryList = splitArrayList(skuList, 100);
		for (List<String> subList : skuForQueryList) {
			System.err.println("ABS: " + subList);
			warehouseImportTicketItemMap
					.putAll(this.warehouseImportTicketItemService.getAllDefaultValuesOfImportTicketBySku(subList));
		}
		return warehouseImportTicketItemMap;
	}

	/**
	 * @param skuList
	 * @param querySize
	 * @return
	 */
	private Collection<List<String>> splitArrayList(List<String> skuList, int chunkSize) {
		AtomicInteger counter = new AtomicInteger();
		return skuList.stream().collect(Collectors.groupingBy(i -> counter.getAndIncrement() / chunkSize)).values();

	}

	/**
	 * @param originalDataMap
	 * @param successList
	 * @param failureList
	 * @param producCodesMap
	 */
	private void validateExcelData(Map<String, WarehouseImportTicketItemDto> warehouseImportTicketItemMap,
			Map<Integer, ErrorImportingDto> originalDataMap, List<WarehouseImportTicketItemDto> successList,
			List<ErrorImportingDto> failureList, Map<String, String> producCodesMap) {
		for (Map.Entry<Integer, ErrorImportingDto> entry : originalDataMap.entrySet()) {
			int rowNum = entry.getKey();
			ErrorImportingDto errorDto = entry.getValue();
			String sku = errorDto.getSku();
			String orderNumber = errorDto.getOrderNumber();
			String amount = errorDto.getAmount();
			String messageForOrderNumberChecking = validateNumber(ORDER_NUMBER_TITTLE, orderNumber);
			String messageForSkuChecking = validateSku(SKU_TITTLE, sku, warehouseImportTicketItemMap, producCodesMap,
					rowNum);
			String messageForAmountChecking = validateNumber(AMOUNT_TITTLE, amount);
			if (StringUtils.isEmpty(messageForOrderNumberChecking) && StringUtils.isEmpty(messageForSkuChecking)
					&& StringUtils.isEmpty(messageForAmountChecking)) {
				WarehouseImportTicketItemDto dto = warehouseImportTicketItemMap.get(sku);
				dto.setAmount(Integer.valueOf(amount));
				successList.add(dto);
			} else {
				String errorMessageJoined = Stream
						.of(messageForOrderNumberChecking, messageForSkuChecking, messageForAmountChecking)
						.filter(s -> StringUtils.isNotEmpty(s)).collect(Collectors.joining(", "));

				errorDto.setError(errorMessageJoined);
				failureList.add(errorDto);
			}
		}
	}

	/**
	 * @param sku
	 * @param producCodesMap
	 * @param rowNum
	 * @return
	 */
	private String validateSku(String prefixMessage, String sku,
			Map<String, WarehouseImportTicketItemDto> warehouseImportTicketItemMap, Map<String, String> producCodesMap,
			int rowNum) {
		String message = null;
		String displayRowNum = String.valueOf(rowNum + 1);
		if (StringUtils.isEmpty(sku)) {
			message = prefixMessage + ": " + sku + " " + FIELD_CAN_NOT_BE_NULL;
		} else if (producCodesMap.containsKey(sku)) {
			message = prefixMessage + ": " + sku + " " + FILED_IS_DUPLICATED_AT_LINE + " : " + producCodesMap.get(sku);
			String lineExisting = String.join(", ", producCodesMap.get(sku), displayRowNum);
			producCodesMap.put(sku, lineExisting);
		} else {
			// check from db
			if (MapUtils.isEmpty(warehouseImportTicketItemMap) || !warehouseImportTicketItemMap.containsKey(sku)) {
				message = prefixMessage + ": " + sku + " " + FIELD_IS_NOT_EXIST;
			}
			producCodesMap.put(sku, displayRowNum);
		}
		return message;
	}

	private String validateNumber(String prefixMessage, String strNum) {
		String message = null;
		if (StringUtils.isNotEmpty(strNum)) {
			try {
				message = Double.parseDouble(strNum) <= 0 ? prefixMessage + " " + FIELD_HAS_TO_GREATER_THAN_ZERO
						: message;
			} catch (NumberFormatException nfe) {
				message = prefixMessage + " " + FIELD_IS_NOT_NUMBER;
			}
		} else {
			message = prefixMessage + " " + FIELD_CAN_NOT_BE_NULL;
		}
		return message;
	}

	private static boolean isRowEmpty(Row row) {
		if (row != null && row.getLastCellNum() > 0) {
			for (int idx = row.getFirstCellNum(); idx < row.getLastCellNum(); idx++) {
				Cell cell = row.getCell(idx);
				if (cell != null && cell.getCellType() != Cell.CELL_TYPE_BLANK) {
					return false;
				}
			}
		}

		return true;
	}
}
