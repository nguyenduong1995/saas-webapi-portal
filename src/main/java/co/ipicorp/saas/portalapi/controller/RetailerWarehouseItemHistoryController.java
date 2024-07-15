/**
 * RetailerWarehouseItemHistoryController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.nrms.model.RetailerWarehouseItemHistory;
import co.ipicorp.saas.nrms.model.dto.RetailerWarehouseItemHistorySearchCondition;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseExportTicketService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseImportTicketService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseItemHistoryService;
import co.ipicorp.saas.portalapi.dto.RetailerWarehouseExportItemHistoryDto;
import co.ipicorp.saas.portalapi.dto.RetailerWarehouseImportItemHistoryDto;
import co.ipicorp.saas.portalapi.form.RetailerWarehouseItemHistorySearchForm;
import co.ipicorp.saas.portalapi.form.validator.WarehouseItemHistorySearchValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.DtoFetchingUtils;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * RetailerWarehouseItemHistoryController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
public class RetailerWarehouseItemHistoryController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private RetailerWarehouseItemHistoryService retailerWarehouseItemHistoryService;
    
    @Autowired
    private ProductVariationService productVariationService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private RetailerWarehouseImportTicketService rwhImportTicketService;
    
    @Autowired
    private RetailerWarehouseExportTicketService rwhExportTicketService;
    
    @Autowired
    private RetailerService retailerService;

    @GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_WAREHOUSE_ITEM_HISTORY_ACTION + "/import/search")
    @Validation(validators = { WarehouseItemHistorySearchValidator.class } )
    @NoRequiredAuth
    @Logged
    public ResponseEntity<?> searchWarehouseImport(HttpServletRequest request, HttpServletResponse response,
    		@GetBody RetailerWarehouseItemHistorySearchForm form) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                RetailerWarehouseItemHistorySearchCondition condition = new RetailerWarehouseItemHistorySearchCondition();
                String props = "segment,offset,retailerId,fromDate,toDate";
                SystemUtils.getInstance().copyProperties(form, condition, props.split(","));
                List<Integer> types = new LinkedList<>();
                types.add(1);
                condition.setChangeTypes(types);
                condition.setLimitSearch(true);
                if (form.getFromDate() != null && form.getToDate() != null) {
                	condition.setEnableDateRange(true);
                }
                
                long total = retailerWarehouseItemHistoryService.count(condition);
                getRpcResponse().addAttribute("count", total);
                List<RetailerWarehouseImportItemHistoryDto> dtos = new LinkedList<>();
                if (total > form.getSegment()) {
                	DtoFetchingUtils.setProductVariationService(productVariationService);
                	DtoFetchingUtils.setProductService(productService);
                	DtoFetchingUtils.setRwhImportTicketService(rwhImportTicketService);
	                List<RetailerWarehouseItemHistory> warehouses = RetailerWarehouseItemHistoryController.this.retailerWarehouseItemHistoryService.search(condition);
	                dtos = DtoFetchingUtils.fetchRetailerWarehouseImportItemHistories(warehouses);
                }
                getRpcResponse().addAttribute("RetailerWarehouseItemHistory", dtos);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_WAREHOUSE_ITEM_HISTORY_ACTION + "/export/search")
    @Validation(validators = { WarehouseItemHistorySearchValidator.class } )
    @NoRequiredAuth
    @Logged
    public ResponseEntity<?> searchWarehouseExport(HttpServletRequest request, HttpServletResponse response,
    		@GetBody RetailerWarehouseItemHistorySearchForm form) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
            	RetailerWarehouseItemHistorySearchCondition condition = new RetailerWarehouseItemHistorySearchCondition();
                String props = "segment,offset,retailerId,fromDate,toDate";
                SystemUtils.getInstance().copyProperties(form, condition, props.split(","));
                condition.setLimitSearch(true);
                List<Integer> types = new LinkedList<>();
                types.add(2);
                condition.setChangeTypes(types);
                if (form.getFromDate() != null && form.getToDate() != null) {
                	condition.setEnableDateRange(true);
                }
                
                long total = retailerWarehouseItemHistoryService.count(condition);
                getRpcResponse().addAttribute("count", total);
                List<RetailerWarehouseExportItemHistoryDto> dtos = new LinkedList<>();
                if (total > form.getSegment()) {
                	DtoFetchingUtils.setProductVariationService(productVariationService);
                	DtoFetchingUtils.setProductService(productService);
                	DtoFetchingUtils.setRwhExportTicketService(rwhExportTicketService);
                	DtoFetchingUtils.setRetailerService(retailerService);
	                List<RetailerWarehouseItemHistory> warehouses = RetailerWarehouseItemHistoryController.this.retailerWarehouseItemHistoryService.search(condition);
	                dtos = DtoFetchingUtils.fetchRetailerWarehouseExportItemHistories(warehouses);
                }
                getRpcResponse().addAttribute("RetailerWarehouseItemHistory", dtos);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    
}
