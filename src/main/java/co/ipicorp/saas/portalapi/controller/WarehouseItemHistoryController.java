/**
 * WarehouseItemHistoryController.java
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

import co.ipicorp.saas.nrms.model.WarehouseItemHistory;
import co.ipicorp.saas.nrms.model.dto.WarehouseItemHistorySearchCondition;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.WarehouseExportTicketService;
import co.ipicorp.saas.nrms.service.WarehouseImportTicketService;
import co.ipicorp.saas.nrms.service.WarehouseItemHistoryService;
import co.ipicorp.saas.nrms.service.WarehouseService;
import co.ipicorp.saas.portalapi.dto.WarehouseExportItemHistoryDto;
import co.ipicorp.saas.portalapi.dto.WarehouseImportItemHistoryDto;
import co.ipicorp.saas.portalapi.form.WarehouseItemHistorySearchForm;
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
 * WarehouseItemHistoryController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
public class WarehouseItemHistoryController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private WarehouseItemHistoryService warehouseItemHistoryService;
    
    @Autowired
    private ProductVariationService productVariationService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private WarehouseImportTicketService importTicketService;
    
    @Autowired
    private WarehouseExportTicketService exportTicketService;
    
    @Autowired
    private WarehouseService warehouseService;

    @GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_ITEM_HISTORY_ACTION + "/import/search")
    @Validation(validators = { WarehouseItemHistorySearchValidator.class } )
    @NoRequiredAuth
    @Logged
    public ResponseEntity<?> searchWarehouseImport(HttpServletRequest request, HttpServletResponse response,
    		@GetBody WarehouseItemHistorySearchForm form) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                WarehouseItemHistorySearchCondition condition = new WarehouseItemHistorySearchCondition();
                String props = "segment,offset,warehouseId,fromDate,toDate,keyword";
                SystemUtils.getInstance().copyProperties(form, condition, props.split(","));
                condition.setLimitSearch(true);
                condition.setImport(true);
                if (form.getFromDate() != null && form.getToDate() != null) {
                	condition.setEnableCreatedDate(true);
                }
                
                long total = warehouseItemHistoryService.count(condition);
                getRpcResponse().addAttribute("count", total);
                List<WarehouseImportItemHistoryDto> dtos = new LinkedList<>();
                if (total > form.getSegment()) {
                	DtoFetchingUtils.setProductVariationService(productVariationService);
                	DtoFetchingUtils.setProductService(productService);
                	DtoFetchingUtils.setImportTicketService(importTicketService);
	                List<WarehouseItemHistory> warehouses = WarehouseItemHistoryController.this.warehouseItemHistoryService.search(condition);
	                dtos = DtoFetchingUtils.fetchWarehouseImportItemHistories(warehouses);
                }
                getRpcResponse().addAttribute("WarehouseItemHistory", dtos);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_ITEM_HISTORY_ACTION + "/export/search")
    @Validation(validators = { WarehouseItemHistorySearchValidator.class } )
    @NoRequiredAuth
    @Logged
    public ResponseEntity<?> searchWarehouseExport(HttpServletRequest request, HttpServletResponse response,
    		@GetBody WarehouseItemHistorySearchForm form) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                WarehouseItemHistorySearchCondition condition = new WarehouseItemHistorySearchCondition();
                String props = "segment,offset,warehouseId,fromDate,toDate,keyword";
                SystemUtils.getInstance().copyProperties(form, condition, props.split(","));
                condition.setLimitSearch(true);
                condition.setImport(false);
                if (form.getFromDate() != null && form.getToDate() != null) {
                	condition.setEnableCreatedDate(true);
                }
                
                long total = warehouseItemHistoryService.count(condition);
                getRpcResponse().addAttribute("count", total);
                List<WarehouseExportItemHistoryDto> dtos = new LinkedList<>();
                if (total > form.getSegment()) {
                	DtoFetchingUtils.setProductVariationService(productVariationService);
                	DtoFetchingUtils.setProductService(productService);
                	DtoFetchingUtils.setExportTicketService(exportTicketService);
                	DtoFetchingUtils.setWarehouseService(warehouseService);
	                List<WarehouseItemHistory> warehouses = WarehouseItemHistoryController.this.warehouseItemHistoryService.search(condition);
	                dtos = DtoFetchingUtils.fetchWarehouseExportItemHistories(warehouses);
                }
                getRpcResponse().addAttribute("WarehouseItemHistory", dtos);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    
}
