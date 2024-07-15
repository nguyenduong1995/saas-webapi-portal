/**
 * OrderSellinPromotionController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.SubjectType;
import co.ipicorp.saas.nrms.model.dto.PromotionReportDetailForRetailerDto;
import co.ipicorp.saas.nrms.model.dto.PromotionReportForRetailerDto;
import co.ipicorp.saas.nrms.model.dto.PromotionReportProductVariationDto;
import co.ipicorp.saas.nrms.model.dto.PromotionReportSearchCondition;
import co.ipicorp.saas.nrms.model.dto.PromotionReportSummaryDto;
import co.ipicorp.saas.nrms.service.OrderSellinPromotionLimitationDetailItemService;
import co.ipicorp.saas.nrms.service.OrderSellinPromotionService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionService;
import co.ipicorp.saas.nrms.service.PromotionConditionFormatService;
import co.ipicorp.saas.nrms.service.PromotionRewardFormatService;
import co.ipicorp.saas.nrms.service.PromotionService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.PromotionReportSearchForm;
import co.ipicorp.saas.portalapi.form.validator.PromotionExistedValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.ReportType;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * OrderSellinPromotionController. <<< Detail note.
 * 
 * @author thuy nguyen
 * @access public
 */
@RestController
@Api(tags = "Promotion Report Summary for retailer/end user APIs", description = "all APIs relate to export the Promotion Report Summary for retailer/end user")
public class PromotionReportController {

    private Logger logger = Logger.getLogger(PromotionReportController.class);

    private static String TEMP_DIRECTORY = System.getProperty("java.io.tmpdir");
    static {
        if (!TEMP_DIRECTORY.endsWith("/")) {
            TEMP_DIRECTORY += "/";
        }
    }

    private static final DateTimeFormatter DATETIME_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    
    private static final String PROMOTION_REPORT_SUCJECT_TYPE_RETAILER_TEMPLATE = "Template_Báo_cáo_CTKM_(Đại_lý).xlsx";
    private static final String PROMOTION_REPORT_SUCJECT_TYPE_CONSUMER_TEMPLATE = "Template_Báo_cáo_CTKM_(EU).xlsx";
    private static final String SELLOUT_PROMOTION_REPORT_FOR_RETAILER_TEMPLATE = "Template_BCCTDS_CTKM_(EU).xlsx";
    private static final String SELLIN_PROMOTION_REPORT_FOR_RETAILER_TEMPLATE = "Template_BCCTDS_CTKM_(Đại_lý).xlsx";
    private static final String TEMPLATE_DIRECTORY = "template/";
    private static final String PROMOTION_REPORT_FILE_NAME = "Báo cáo CTKM";
    private static final String PROMOTION_REPORT_FOR_RETAILER_FILE_NAME = "Báo cáo Chi Tiết Doanh Số";
    // Declare row and column index for sellin/out promotion report
    // Tổng doanh số đơn hàng hoàn thành (C8)
    private static final int TOTAL_FINAL_COST_ROW = 7;
    private static final int TOTAL_FINAL_COST_COLUMN = 2;
    
    // Tổng doanh số sản phẩm (C9)
    private static final int TOTAL_PRODUCT_ON_PROMOTION_COST_ROW = 8;
    private static final int TOTAL_PRODUCT_ON_PROMOTION_COST_COLUMN = 2;
    
    // Tổng chiết khấu (C10)
    private static final int TOTAL_DISCOUNT_ROW = 9;
    private static final int TOTAL_DISCOUNT_COLUMN = 2;
    
    // Tổng chiết khấu (C11)
    private static final int TOTAL_REWARD_AMOUNT_ROW = 10;
    private static final int TOTAL_REWARD_AMOUNT_COLUMN = 2;
    
    // Tổng chiết khấu (C12)
    private static final int SUMMARY_CIR_ROW = 11;
    private static final int SUMMARY_CIR_COLUMN = 2;
    
    private static final int PROMOTION_REPORT_DETAIL_START_ROW = 15;
    
    private static final int ORDER_NUMBER_COLUMN = 0;
    private static final int RETAILER_CODE_COLUMN = 1;
    private static final int RETAILER_NAME_COLUMN = 2;
    private static final int FINAL_COST_COLUMN = 3;
    private static final int PRODUCT_ON_PROMOTION_COST_COLUMN = 4;
    private static final int PRODUCT_ON_PROMOTION_AMOUNT_COLUMN = 5;
    private static final int DISCOUNT_COLUMN = 6;
    private static final int REWARD_AMOUNT_COLUMN = 7;

    // Declare row and column index for sellin/out promotion detail report
    private static final int PROMOTION_REPORT_DETAIL_FOR_RETAILER_START_ROW = 12;
    private static final int RETAILER_CODE_ON_PROMOTION_COLUMN = 1;
    private static final int RETAILER_NAME_ON_PROMOTION_COLUMN = 2;
    private static final int ORDER_CODE_COLUMN = 3;
    private static final int ORDER_PRODUCT_VARIATION_CODE_COLUMN = 4;
    private static final int ORDER_PRODUCT_VARIATION_NAME_COLUMN = 5;
    private static final int ORDER_PRODUCT_VARIATION_NUMBER_COLUMN = 6;
    private static final int ORDER_ITEM_COST_COLUMN = 7;
    private static final int FINAL_COST_ON_PROMOTION_COLUMN = 8;
    private static final int ORDER_FINAL_COST_COLUMN = 9;
    private static final int ORDER_ITEM_REDUCE_COST_COLUMN = 11;
    private static final int ORDER_REWARD_NUMBER_COLUMN = 12;
    private static final int ORDER_CUSTOMER_NAME_COLUMN = 3;
    private static final int ORDER_MOBILE_COLUMN = 4;
    private static final int MAXIMUM_RECORD = 1000;

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private OrderSellinPromotionService orderSellinPromotionService;

    @Autowired
    private OrderSelloutPromotionService orderSelloutPromotionService;

    @Autowired
    private OrderSellinPromotionLimitationDetailItemService orderSellinPromotionDetailService;

    @Autowired
    private PromotionService promotionService;

    @Autowired
    private RetailerService retailerService;

    @SuppressWarnings("unused")
    @Autowired
    private PromotionConditionFormatService promotionConditionFormatService;

    @SuppressWarnings("unused")
    @Autowired
    private PromotionRewardFormatService promotionRewardFormatService;

    @GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_REPORT_SUMMARY_ACTION)
    @NoRequiredAuth
    @Validation(validators = { PromotionExistedValidator.class })
    public ResponseEntity<?> getPromotionReportSummary(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer promotionId) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {

                Promotion promotion = promotionService.get(promotionId);
                PromotionReportSummaryDto osiSummary = orderSellinPromotionService.getOrderSellinPromotionSummaryByPromotionId(promotionId);
                getRpcResponse().addAttribute("retailer", osiSummary);

                if (SubjectType.CONSUMER.equals(promotion.getSubjectType())) {
                    PromotionReportSummaryDto osoSummary = orderSelloutPromotionService.getOrderSelloutPromotionSummaryByPromotionId(promotionId);
                    getRpcResponse().addAttribute("consumer", osoSummary);
                }
            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_RETAILER_SUMMARY_ACTION)
    @Validation(validators = { PromotionExistedValidator.class })
    @NoRequiredAuth
    public ResponseEntity<?> getRetailerSummaryOfPromotion(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer promotionId,
            @GetBody PromotionReportSearchForm form) {

        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                PromotionReportSearchCondition condition = new PromotionReportSearchCondition();
                condition.setLimitSearch(true);
                condition.setSegment(form.getSegment());
                condition.setOffset(form.getOffset());
                condition.setPromotionId(promotionId);
                condition.setKeyword(form.getKeySearch());

                if (ReportType.SELLIN.equals(form.getReportType())) {
                    long count = orderSellinPromotionService.count(condition);
                    getRpcResponse().addAttribute("count", count);

                    if (count > form.getSegment()) {
                        List<PromotionReportSummaryDto> summaries = orderSellinPromotionService.getRetailerSummaryForPromotion(condition);
                        getRpcResponse().addAttribute("retailers", summaries);
                    }
                } else {
                    long count = orderSelloutPromotionService.countSelloutOrderPromotionForRetailer(condition);
                    getRpcResponse().addAttribute("count", count);

                    if (count > form.getSegment()) {
                        List<PromotionReportSummaryDto> summaries = orderSelloutPromotionService.getRetailerSummaryForPromotion(condition);
                        getRpcResponse().addAttribute("retailers", summaries);
                    }
                }

            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_RETAILER_DETAIL_SUMMARY_ACTION)
    @Validation(validators = { PromotionExistedValidator.class })
    @NoRequiredAuth
    public ResponseEntity<?> getPromotionSummaryForRetailer(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("id") Integer promotionId, @PathVariable("retailerId") Integer retailerId) {

        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                Promotion promotion = promotionService.get(promotionId);
                PromotionReportSummaryDto osiSummary = orderSellinPromotionService.getOrderSellinPromotionSummaryByPromotionIdAndRetailerId(promotionId, retailerId);
                osiSummary.makeDefaultValue();
                getRpcResponse().addAttribute("retailer", osiSummary);

                if (SubjectType.CONSUMER.equals(promotion.getSubjectType())) {
                    PromotionReportSummaryDto osoSummary = orderSelloutPromotionService.getOrderSelloutPromotionSummaryByPromotionIdAndRetailerId(promotionId, retailerId);
                    osoSummary.makeDefaultValue();
                    getRpcResponse().addAttribute("consumer", osoSummary);
                }
            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_RETAILER_PRODUCT_VARIATION_ACTION)
    @Validation(validators = { PromotionExistedValidator.class })
    @NoRequiredAuth
    public ResponseEntity<?> searchProductVariationInPromotionOfRetailer(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("id") Integer promotionId, @PathVariable("retailerId") Integer retailerId, @GetBody PromotionReportSearchForm form) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                Retailer retailer = retailerService.get(retailerId);
                if (retailer == null) {
                    errors.reject(ErrorCode.APP_1201_RETAILER_NOT_EXIST, ErrorCode.APP_1201_RETAILER_NOT_EXIST);
                    return;
                }
                
                PromotionReportSearchCondition condition = new PromotionReportSearchCondition();
                condition.setLimitSearch(true);
                condition.setSegment(form.getSegment());
                condition.setOffset(form.getOffset());
                condition.setPromotionId(promotionId);
                condition.setRetailerIds(Arrays.asList(retailerId));
                condition.setKeyword(form.getKeySearch());

                if (ReportType.SELLIN.equals(form.getReportType())) {
                    long count = orderSellinPromotionService.countProductVariations(condition);
                    getRpcResponse().addAttribute("count", count);

                    if (count > form.getSegment()) {
                        List<PromotionReportProductVariationDto> productVariations = orderSellinPromotionService.searchProductVariations(condition);
                        getRpcResponse().addAttribute("productVariations", productVariations);
                    }
                } else {
                    long count = orderSelloutPromotionService.countProductVariations(condition);
                    getRpcResponse().addAttribute("count", count);

                    if (count > form.getSegment()) {
                        List<PromotionReportProductVariationDto> orders = orderSelloutPromotionService.searchProductVariations(condition);
                        getRpcResponse().addAttribute("productVariations", orders);
                    }
                }
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_RETAILER_ORDER_ACTION)
    @Validation(validators = { PromotionExistedValidator.class })
    @NoRequiredAuth
    public ResponseEntity<?> searchOrderSellinPromotionOfRetailer(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("id") Integer promotionId, @PathVariable("retailerId") Integer retailerId, @GetBody PromotionReportSearchForm form) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                Retailer retailer = retailerService.get(retailerId);
                if (retailer == null) {
                    errors.reject(ErrorCode.APP_1201_RETAILER_NOT_EXIST, ErrorCode.APP_1201_RETAILER_NOT_EXIST);
                    return;
                }

                PromotionReportSearchCondition condition = new PromotionReportSearchCondition();
                condition.setLimitSearch(true);
                condition.setSegment(form.getSegment());
                condition.setOffset(form.getOffset());
                condition.setPromotionId(promotionId);
                condition.setRetailerIds(Arrays.asList(retailerId));
                condition.setKeyword(form.getKeySearch());

                if (ReportType.SELLIN.equals(form.getReportType())) {
                    long count = orderSellinPromotionService.countOrders(condition);
                    getRpcResponse().addAttribute("count", count);

                    if (count > form.getSegment()) {
                        List<PromotionReportForRetailerDto> orders = orderSellinPromotionService.searchOrders(condition);
                        getRpcResponse().addAttribute("orders", orders);
                    }
                } else {
                    long count = orderSelloutPromotionService.countOrders(condition);
                    getRpcResponse().addAttribute("count", count);

                    if (count > form.getSegment()) {
                        List<PromotionReportForRetailerDto> orders = orderSelloutPromotionService.searchOrders(condition);
                        getRpcResponse().addAttribute("orders", orders);
                    }
                }
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @Logged
    @GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_REPORT_EXPORT_ACTION, 
                produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE })
    @Validation(validators = { PromotionExistedValidator.class })
    public ResponseEntity<?> downloadPromotionReportFile(@PathVariable("id") Integer promotionId) throws IOException {
        File file = null;
        FileInputStream inputStream = null;
        try {
            Promotion promotion = promotionService.get(promotionId);
            ResponseEntity<Object> responseEntity = null;
            
            if (promotion != null) {
                String reportFileName = buildPromotionReportFileName(promotion, false);
                HttpHeaders headers = setHttpHeaders(reportFileName);
                readAndWritePromotionReport(promotion, reportFileName);

                file = new File(TEMP_DIRECTORY + reportFileName);
                inputStream = new FileInputStream(file);
                byte[] body = IOUtils.toByteArray(inputStream);
                responseEntity = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(new ByteArrayResource(body));
            }
            return responseEntity;
        } catch (Exception ex) {
            logger.error(ex.getMessage(), ex);
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.error(ErrorCode.APP_1000_SYSTEM_ERROR, ex.getMessage());
            return new ResponseEntity<RpcResponse>(rpcResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (file != null && file.exists()) {
                FileUtils.forceDelete(file);
            }
        }
    }

    @GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_RETAILER_EXPORT_ACTION, produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    @Logged
    @NoRequiredAuth
    public ResponseEntity<?> downloadPromotionDetailReportForRetailer(@PathVariable("id") Integer promotionId, 
            @PathVariable("retailerId") Integer retailerId) throws IOException {

        File file = null;
        FileInputStream fis = null;
        ResponseEntity<Object> result = null;
        try {
            Promotion promotion = promotionService.get(promotionId);
            Retailer retailer = retailerService.get(retailerId);
            if (promotion != null && retailer != null) {

                String reportFileName = buildPromotionReportFileName(promotion, true);
                HttpHeaders headers = setHttpHeaders(reportFileName);

                readAndWritePromotionReportForRetailer(promotion, retailer, reportFileName);

                file = new File(TEMP_DIRECTORY + reportFileName);
                fis = new FileInputStream(file);
                byte[] body = IOUtils.toByteArray(fis);
                result = ResponseEntity.ok().headers(headers).contentLength(file.length()).contentType(MediaType.APPLICATION_OCTET_STREAM)
                        .body(new ByteArrayResource(body));
            }

            return result;
        } catch (Exception e) {
            RpcResponse rpcResponse = new RpcResponse();
            rpcResponse.error(ErrorCode.APP_1000_SYSTEM_ERROR, e.getMessage());
            return new ResponseEntity<RpcResponse>(rpcResponse, HttpStatus.INTERNAL_SERVER_ERROR);
        } finally {
            if (fis != null) {
                fis.close();
            }
            if (file != null && file.exists()) {
                FileUtils.forceDelete(file);
            }
        }
    }

    /**
     * @param promotion
     * @param reportFileName
     * @param reportType
     * @throws IOException
     */
    private void readAndWritePromotionReportForRetailer(Promotion promotion, Retailer retailer, String reportFileName) throws IOException {
        String templateFileName = SELLIN_PROMOTION_REPORT_FOR_RETAILER_TEMPLATE;
        if (SubjectType.CONSUMER.equals(promotion.getSubjectType())) {
            templateFileName = SELLOUT_PROMOTION_REPORT_FOR_RETAILER_TEMPLATE;
        }

        File templateFile = ResourceUtils.getFile("classpath:" + TEMPLATE_DIRECTORY + templateFileName);

        FileInputStream fis = new FileInputStream(templateFile);
        XSSFWorkbook workbook = new XSSFWorkbook(fis);
        XSSFSheet sellinSheet = workbook.getSheetAt(0);
        workbook.setMissingCellPolicy(Row.RETURN_BLANK_AS_NULL);

        writePromotionInformationIntoExcel(sellinSheet, promotion);
        writePromotionReportSummaryIntoExcel(sellinSheet, promotion, retailer, ReportType.SELLIN);
        writePromotionReportDetailBodyIntoExcel(sellinSheet, promotion, retailer, ReportType.SELLIN);

        if (!isPromotionReportForRetailer(promotion.getSubjectType())) {
            XSSFSheet selloutSheet = workbook.getSheetAt(1);
            writePromotionInformationIntoExcel(selloutSheet, promotion);
            writePromotionReportSummaryIntoExcel(selloutSheet, promotion, retailer, ReportType.SELLOUT);
            writePromotionReportDetailBodyIntoExcel(selloutSheet, promotion, retailer, ReportType.SELLOUT);
        }
        
        fis.close();

        File file = new File(TEMP_DIRECTORY + reportFileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        workbook.write(outputStream);
        outputStream.close();
    }

    /**
     * @param name
     * @param reportType
     * @return
     */
    private String buildPromotionReportFileName(Promotion promotion, boolean isReportDetail) {
        String reportTypeStr = SubjectType.RETAILER.equals(promotion.getSubjectType()) ? "ĐL" : "EU";
        String baseFileName = !isReportDetail ? PromotionReportController.PROMOTION_REPORT_FILE_NAME
                : PromotionReportController.PROMOTION_REPORT_FOR_RETAILER_FILE_NAME;
        return baseFileName + "_" + promotion.getPromotionCode() + "(" + reportTypeStr + ").xlsx";
    }

    /**
     * @param promotionId
     * @return
     * @throws IOException
     */
    private void readAndWritePromotionReport(Promotion promotion, String reportFileName) throws IOException {
        
        String templateFileName = PromotionReportController.PROMOTION_REPORT_SUCJECT_TYPE_CONSUMER_TEMPLATE;
        if (isPromotionReportForRetailer(promotion.getSubjectType())) {
            templateFileName = PromotionReportController.PROMOTION_REPORT_SUCJECT_TYPE_RETAILER_TEMPLATE;
        }
        
        String path = "classpath:" + PromotionReportController.TEMPLATE_DIRECTORY + templateFileName;
        
        File templateFile = ResourceUtils.getFile(path);
        FileInputStream fis = new FileInputStream(templateFile);
        XSSFWorkbook book = new XSSFWorkbook(fis);
        XSSFSheet sellinSheet = book.getSheetAt(0);
        book.setMissingCellPolicy(Row.RETURN_BLANK_AS_NULL);
        writePromotionInformationIntoExcel(sellinSheet, promotion);
        writePromotionReportSummaryIntoExcel(sellinSheet, promotion, ReportType.SELLIN);
        writePromotionReportDetailIntoExcel(sellinSheet, promotion, ReportType.SELLIN);

        if (!isPromotionReportForRetailer(promotion.getSubjectType())) {
            XSSFSheet selloutSheet = book.getSheetAt(1);
            writePromotionInformationIntoExcel(selloutSheet, promotion);
            writePromotionReportSummaryIntoExcel(selloutSheet, promotion, ReportType.SELLOUT);
            writePromotionReportDetailIntoExcel(selloutSheet, promotion, ReportType.SELLOUT);
        }
        
        fis.close();

        File file = new File(TEMP_DIRECTORY + reportFileName);
        FileOutputStream outputStream = new FileOutputStream(file);
        book.write(outputStream);
        outputStream.close();
    }

    /**
     * Write body of Generic report to excel sheet.
     * @param sheet
     * @param promotion
     * @param reportType
     */
    private void writePromotionReportDetailIntoExcel(XSSFSheet sheet, Promotion promotion, ReportType reportType) {
        Integer segment = 0;
        Integer offset = MAXIMUM_RECORD;
        boolean isStillHavingDatas = true;
        int startDetailRow = PROMOTION_REPORT_DETAIL_START_ROW;
        while (isStillHavingDatas) {
            List<PromotionReportSummaryDto> dtos = getPromotionReportDetails(segment, offset, promotion, reportType);
            if (CollectionUtils.isNotEmpty(dtos)) {
                for (int index = 0; index < dtos.size(); index++) {
                    writePromotionDetailRow(dtos.get(index), sheet, startDetailRow, index + 1);
                    startDetailRow++;
                }
                
                segment = offset;
                offset += MAXIMUM_RECORD;
                startDetailRow++;
            } else {
                isStillHavingDatas = false;
            }
        }
    }
    
    /**
     * Write body of Detail report to excel sheet.
     * @param sheet
     * @param promotion
     * @param retailer
     * @param reportType
     */
    private void writePromotionReportDetailBodyIntoExcel(XSSFSheet sheet, Promotion promotion, Retailer retailer, ReportType reportType) {
        Integer segment = 0;
        Integer offset = MAXIMUM_RECORD;
        boolean isStillHavingDatas = true;
        int startDetailRow = PROMOTION_REPORT_DETAIL_START_ROW;
        while (isStillHavingDatas) {
            List<PromotionReportProductVariationDto> dtos = getPromotionReportDetailBody(segment, offset, promotion, retailer, reportType);
            if (CollectionUtils.isNotEmpty(dtos)) {
                for (int index = 0; index < dtos.size(); index++) {
                    if (ReportType.SELLIN.equals(reportType)) {
                        writePromotionDetailSellinRow(dtos.get(index), sheet, startDetailRow, index + 1);
                    } else {
                        writePromotionDetailSelloutRow(dtos.get(index), sheet, startDetailRow, index + 1);
                    }
                    
                    startDetailRow++;
                }
                
                segment = offset;
                offset += MAXIMUM_RECORD;
                startDetailRow++;
            } else {
                isStillHavingDatas = false;
            }
        }

    }

    private List<PromotionReportProductVariationDto> getPromotionReportDetailBody(Integer segment, Integer offset, Promotion promotion, Retailer retailer, ReportType reportType) {
        PromotionReportSearchCondition condition = new PromotionReportSearchCondition();
        condition.setLimitSearch(true);
        condition.setPromotionId(promotion.getId());
        condition.setSegment(segment);
        condition.setOffset(offset);
        condition.setRetailerIds(Arrays.asList(retailer.getId()));
        List<PromotionReportProductVariationDto> dtos = null;
        if (ReportType.SELLIN.equals(reportType)) {
            dtos = orderSellinPromotionService.searchProductVariations(condition);
        } else {
            dtos = orderSelloutPromotionService.searchProductVariations(condition);
        }
        return dtos;
    }
    
    /**
     * @param segment
     * @param offset
     * @param promotion
     * @param reportType
     * @return
     */
    private List<PromotionReportSummaryDto> getPromotionReportDetails(Integer segment, Integer offset, Promotion promotion, ReportType reportType) {
        PromotionReportSearchCondition condition = new PromotionReportSearchCondition();
        condition.setLimitSearch(true);
        condition.setPromotionId(promotion.getId());
        condition.setSegment(segment);
        condition.setOffset(offset);
        List<PromotionReportSummaryDto> dtos = null;
        if (ReportType.SELLIN.equals(reportType)) {
            dtos = orderSellinPromotionService.getRetailerSummaryForPromotion(condition);
        } else {
            dtos = orderSelloutPromotionService.getRetailerSummaryForPromotion(condition);
        }
        return dtos;
    }

    private XSSFCellStyle createBorderStyle(XSSFSheet sheet) {
        XSSFCellStyle style = sheet.getWorkbook().createCellStyle();
        style.setBorderBottom(BorderStyle.THIN);
        style.setBorderTop(BorderStyle.THIN);
        style.setBorderLeft(BorderStyle.THIN);
        style.setBorderRight(BorderStyle.THIN);
        return style;
    }
    
    /**
     * @param dto
     * @param sheet
     */
    private void writePromotionDetailRow(PromotionReportSummaryDto dto, XSSFSheet sheet, int rowIndex, int orderNumber) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        DecimalFormat fommater = new DecimalFormat("#,###");
        
        XSSFCellStyle style = this.createBorderStyle(sheet);
        style.setAlignment(HorizontalAlignment.CENTER);
        
        Cell cell = createOrGetCell(row, ORDER_NUMBER_COLUMN);
        cell.setCellStyle(style);
        cell.setCellValue(orderNumber);

        cell = createOrGetCell(row, RETAILER_CODE_COLUMN);
        cell.setCellStyle(style);
        cell.setCellValue(dto.getRetailerCode());

        style = this.createBorderStyle(sheet);
        style.setAlignment(HorizontalAlignment.LEFT);
        
        cell = createOrGetCell(row, RETAILER_NAME_COLUMN);
        cell.setCellStyle(style);
        cell.setCellValue(dto.getRetailerName());

        style = this.createBorderStyle(sheet);
        style.setAlignment(HorizontalAlignment.RIGHT);
        
        cell = createOrGetCell(row, FINAL_COST_COLUMN);
        cell.setCellStyle(style);
        cell.setCellValue(fommater.format(dto.getOrderCost()) + "đ");

        cell = createOrGetCell(row, PRODUCT_ON_PROMOTION_COST_COLUMN);
        cell.setCellStyle(style);
        cell.setCellValue(fommater.format(dto.getProductOnPromotionCost()) + "đ");
        
        cell = createOrGetCell(row, PRODUCT_ON_PROMOTION_AMOUNT_COLUMN);
        cell.setCellStyle(style);
        cell.setCellValue(dto.getProductOnPromotionAmount());

        cell = createOrGetCell(row, DISCOUNT_COLUMN);
        cell.setCellStyle(style);
        cell.setCellValue(fommater.format(dto.getDiscount()) + "đ");

        cell = createOrGetCell(row, REWARD_AMOUNT_COLUMN);
        cell.setCellStyle(style);
        cell.setCellValue(dto.getRewardAmount());
    }
    
    /**
     * @param dto {@link PromotionReportProductVariationDto}
     * @param sheet
     * @param rowIndex
     * @param orderNumber
     */
    private void writePromotionDetailSellinRow(PromotionReportProductVariationDto dto, XSSFSheet sheet, int rowIndex, int orderNumber) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        DecimalFormat fommater = new DecimalFormat("#,###");
        
        XSSFCellStyle centerStyle = this.createBorderStyle(sheet);
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        XSSFCellStyle leftStyle = this.createBorderStyle(sheet);
        leftStyle.setAlignment(HorizontalAlignment.LEFT);
        
        XSSFCellStyle rightStyle = this.createBorderStyle(sheet);
        rightStyle.setAlignment(HorizontalAlignment.RIGHT);
        
        int colIndx = 0;
        Cell cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(orderNumber);

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(dto.getRetailerCode());

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(leftStyle);
        cell.setCellValue(dto.getRetailerName());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(dto.getOrderCode());

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(rightStyle);
        cell.setCellValue(fommater.format(dto.getOrderCost()) + "đ");

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(dto.getSku());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(leftStyle);
        cell.setCellValue(dto.getProductVariationName());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(rightStyle);
        cell.setCellValue(dto.getProductOnPromotionAmount());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(dto.getUnitName());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(rightStyle);
        cell.setCellValue(fommater.format(dto.getProductOnPromotionCost()) + "đ");

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(rightStyle);
        cell.setCellValue(fommater.format(dto.getDiscount()) + "đ");
    }
    
    /**
     * @param dto {@link PromotionReportProductVariationDto}
     * @param sheet
     * @param rowIndex
     * @param orderNumber
     */
    private void writePromotionDetailSelloutRow(PromotionReportProductVariationDto dto, XSSFSheet sheet, int rowIndex, int orderNumber) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }

        DecimalFormat fommater = new DecimalFormat("#,###");
        
        XSSFCellStyle centerStyle = this.createBorderStyle(sheet);
        centerStyle.setAlignment(HorizontalAlignment.CENTER);
        
        XSSFCellStyle leftStyle = this.createBorderStyle(sheet);
        leftStyle.setAlignment(HorizontalAlignment.LEFT);
        
        XSSFCellStyle rightStyle = this.createBorderStyle(sheet);
        rightStyle.setAlignment(HorizontalAlignment.RIGHT);
        
        int colIndx = 0;
        Cell cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(orderNumber);

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(dto.getRetailerCode());

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(leftStyle);
        cell.setCellValue(dto.getRetailerName());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(dto.getConsumerPhone());

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(leftStyle);
        cell.setCellValue(dto.getConsumerName());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(dto.getOrderCode());

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(rightStyle);
        cell.setCellValue(fommater.format(dto.getOrderCost()) + "đ");

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(centerStyle);
        cell.setCellType(Cell.CELL_TYPE_STRING);
        cell.setCellValue(dto.getSku());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(leftStyle);
        cell.setCellValue(dto.getProductVariationName());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(rightStyle);
        cell.setCellValue(dto.getProductOnPromotionAmount());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(centerStyle);
        cell.setCellValue(dto.getUnitName());
        
        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(rightStyle);
        cell.setCellValue(fommater.format(dto.getProductOnPromotionCost()) + "đ");

        cell = createOrGetCell(row, colIndx++);
        cell.setCellStyle(rightStyle);
        cell.setCellValue(fommater.format(dto.getDiscount()) + "đ");
    }

    /**
     * @param reportType
     * @return
     */
    private boolean isPromotionReportForRetailer(SubjectType subjectType) {
        return SubjectType.RETAILER.equals(subjectType);
    }

    /**
     * Retailer report summary.
     * @param sheet
     * @param promotion
     * @param retailer
     * @param reportType
     */
    private void writePromotionReportSummaryIntoExcel(XSSFSheet sheet, Promotion promotion, Retailer retailer, ReportType reportType) {
        PromotionReportSummaryDto summary = null;
        
        if (ReportType.SELLIN.equals(reportType)) {
            summary = orderSellinPromotionService.getOrderSellinPromotionSummaryByPromotionIdAndRetailerId(promotion.getId(), retailer.getId());
        } else {
            summary = orderSelloutPromotionService.getOrderSelloutPromotionSummaryByPromotionIdAndRetailerId(promotion.getId(), retailer.getId());
        }
        
        if (summary != null) {
            DecimalFormat fommater = new DecimalFormat("#,###");
            Cell cell = createOrGetCell(sheet, TOTAL_FINAL_COST_ROW, TOTAL_FINAL_COST_COLUMN);
            cell.setCellValue(fommater.format(summary.getOrderCost()) + "đ");

            cell = createOrGetCell(sheet, TOTAL_PRODUCT_ON_PROMOTION_COST_ROW, TOTAL_PRODUCT_ON_PROMOTION_COST_COLUMN);
            cell.setCellValue(fommater.format(summary.getProductOnPromotionCost()) + "đ");

            cell = createOrGetCell(sheet, TOTAL_DISCOUNT_ROW, TOTAL_DISCOUNT_COLUMN);
            cell.setCellValue(fommater.format(summary.getDiscount() + summary.getRewardValue()) + "đ");

            cell = createOrGetCell(sheet, TOTAL_REWARD_AMOUNT_ROW, TOTAL_REWARD_AMOUNT_COLUMN);
            cell.setCellValue(summary.getRewardAmount());

            cell = createOrGetCell(sheet, SUMMARY_CIR_ROW, SUMMARY_CIR_COLUMN);
            cell.setCellValue(summary.getCir() + "%");
        }
    }
    
    /**
     * Generic Report Summary.
     * @param sheet
     * @param promotion
     * @param reportType
     */
    private void writePromotionReportSummaryIntoExcel(XSSFSheet sheet, Promotion promotion, ReportType reportType) {
        PromotionReportSummaryDto summary = null;
        
        if (ReportType.SELLIN.equals(reportType)) {
            summary = orderSellinPromotionService.getOrderSellinPromotionSummaryByPromotionId(promotion.getId());
        } else {
            summary = orderSelloutPromotionService.getOrderSelloutPromotionSummaryByPromotionId(promotion.getId());
        }
        
        if (summary != null) {
            summary.makeDefaultValue(); 
            
            DecimalFormat fommater = new DecimalFormat("#,###");
            Cell cell = createOrGetCell(sheet, TOTAL_FINAL_COST_ROW, TOTAL_FINAL_COST_COLUMN);
            cell.setCellValue(fommater.format(summary.getOrderCost()) + "đ");

            cell = createOrGetCell(sheet, TOTAL_PRODUCT_ON_PROMOTION_COST_ROW, TOTAL_PRODUCT_ON_PROMOTION_COST_COLUMN);
            cell.setCellValue(fommater.format(summary.getProductOnPromotionCost()) + "đ");

            cell = createOrGetCell(sheet, TOTAL_DISCOUNT_ROW, TOTAL_DISCOUNT_COLUMN);
            cell.setCellValue(fommater.format(summary.getDiscount() + summary.getRewardValue()) + "đ");

            cell = createOrGetCell(sheet, TOTAL_REWARD_AMOUNT_ROW, TOTAL_REWARD_AMOUNT_COLUMN);
            cell.setCellValue(summary.getRewardAmount());

            cell = createOrGetCell(sheet, SUMMARY_CIR_ROW, SUMMARY_CIR_COLUMN);
            cell.setCellValue(summary.getCir() + "%");
        }
    }

    /**
     * @param sheet
     * @param promotion
     */
    private void writePromotionInformationIntoExcel(XSSFSheet sheet, Promotion promotion) {
        // Mã CTKM (C3)
        Cell cell = this.createOrGetCell(sheet, 2, 2);
        cell.setCellValue(promotion.getPromotionCode());
        
        // Tên CTKM (C4)
        cell = this.createOrGetCell(sheet, 3, 2);
        cell.setCellValue(promotion.getName());
        
        // Thời gian xuất dữ liệu (C5)
        cell = this.createOrGetCell(sheet, 4, 2);
        cell.setCellValue(DATETIME_FORMATTER.format(LocalDateTime.now()));
        
        // Điều kiện nhận thưởng (F3)
        cell = this.createOrGetCell(sheet, 2, 5);
        String conditonFormat = promotion.getConditionFormatId() == Promotion.CONDITION_FORMAT_PRODUCT_COUNT 
                ? "Số lượng sản phẩm"
                : (promotion.getConditionFormatId() == Promotion.CONDITION_FORMAT_PRODUCT_VALUE ? "Doanh số sản phẩm" : "Doanh số đơn hàng");
        cell.setCellValue(conditonFormat);
        
        // Loại xét thưởng (F4)
        cell = this.createOrGetCell(sheet, 3, 5);
        String comparisionType = promotion.getConditionComparitionType() == Promotion.COMPARE_TYPE_RANGE  ? "Khoảng xét thưởng" : "Mức xét thưởng";
        cell.setCellValue(comparisionType);
        
        // Loại thưởng (F5)
        cell = this.createOrGetCell(sheet, 4, 5);
        String rewardFormat = promotion.getRewardFormatId() == Promotion.REWARD_PRODUCT 
                ? "Tặng sản phẩm"
                : (promotion.getRewardFormatId() == Promotion.REWARD_VALUE ? "Giảm giá trị" : "Giảm phần trăm (%)");
        cell.setCellValue(rewardFormat);
        
        // Ngày bắt đầu (I4)
        cell = this.createOrGetCell(sheet, 3, 8);
        cell.setCellValue(DATE_FORMATTER.format(promotion.getStartDate()));
        
        // Loại thưởng (I5)
        cell = this.createOrGetCell(sheet, 4, 8);
        cell.setCellValue(DATE_FORMATTER.format(promotion.getEndDate()));
        
        if (!isPromotionReportForRetailer(promotion.getSubjectType())) {
            // Ngày áp dụng sớm (I3)
            cell = this.createOrGetCell(sheet, 2, 8);
            cell.setCellValue(DATE_FORMATTER.format(promotion.getPreparationDate()));
        }
    }

    /**
     * @param row
     * @param promotionCodeColumn
     * @return
     */
    private Cell createOrGetCell(Row row, int columnIndex) {
        Cell cell = row.getCell(columnIndex, Row.RETURN_NULL_AND_BLANK);
        return cell == null ? row.createCell(columnIndex) : cell;
    }
    
    private Cell createOrGetCell(XSSFSheet sheet, int rowIndex, int columnIndex) {
        Row row = sheet.getRow(rowIndex);
        if (row == null) {
            row = sheet.createRow(rowIndex);
        }
        return createOrGetCell(row, columnIndex);
    }
    

    /**
     * @param name
     * @return
     * @throws UnsupportedEncodingException
     */
    private HttpHeaders setHttpHeaders(String fileName) throws UnsupportedEncodingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION,
                String.format("attachment; filename=\"%s\"", URLEncoder.encode(fileName.replace(" ", "blank"), "UTF-8").replace("blank", " ")));
        headers.add(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, must-revalidate");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        return headers;
    }
}
