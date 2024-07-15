/**
 * WarehouseController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
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

import co.ipicorp.saas.core.model.City;
import co.ipicorp.saas.core.service.CityService;
import co.ipicorp.saas.core.service.LocationService;
import co.ipicorp.saas.nrms.model.Warehouse;
import co.ipicorp.saas.nrms.model.dto.WarehouseSearchCondition;
import co.ipicorp.saas.nrms.service.WarehouseService;
import co.ipicorp.saas.nrms.web.dto.WarehouseDto;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.portalapi.form.WarehouseCreationForm;
import co.ipicorp.saas.portalapi.form.WarehouseSearchForm;
import co.ipicorp.saas.portalapi.form.WarehouseUpdateForm;
import co.ipicorp.saas.portalapi.form.validator.WarehouseCodeExistedValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;

/**
 * WarehouseController. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
@RestController
public class WarehouseController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private WarehouseService warehouseService;

    @Autowired
    private CityService cityService;

    @Autowired
    private LocationService locationService;

    @GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_ACTION)
    @ResponseBody
    @RequiresAuthentication
    public ResponseEntity<?> listAll(HttpServletRequest request, HttpServletResponse response) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                return warehouseService.getAll();
            }

            @Override
            public String getAttributeName() {
                return "warehouses";
            }

            @SuppressWarnings("unchecked")
            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                return DtoFetchingUtils.fetchWarehouses((List<Warehouse>) entities);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_ACTION + "/search")
    @RequiresAuthentication
    @Logged
    public ResponseEntity<?> searchWarehouse(HttpServletRequest request, HttpServletResponse response, @GetBody WarehouseSearchForm form) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                WarehouseSearchCondition condition = new WarehouseSearchCondition();
                condition.setSegment(form.getSegment());
                condition.setOffset(form.getOffset());
                condition.setLimitSearch(true);
                
                if (form.getStatus() != null && form.getStatus() >= 0) {
                    condition.setStatus(Status.fromValue(form.getStatus()));
                }
                
                if (StringUtils.isNotBlank(form.getKeyword())) {
                	condition.setKeyword(form.getKeyword());
                }
                
                condition.setWarehouseTypeId(form.getWarehouseTypeId());
                long count = WarehouseController.this.warehouseService.count(condition);
                getRpcResponse().addAttribute("count", count);
                
                List<WarehouseDto> warehouseDtos = new LinkedList<WarehouseDto>();
                if (count > form.getSegment()) {
                	List<Warehouse> warehouses = WarehouseController.this.warehouseService.searchWarehouses(condition);
                    warehouseDtos = DtoFetchingUtils.fetchWarehouses(warehouses);
                }
                getRpcResponse().addAttribute("warehouses", warehouseDtos);

            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @GetMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_DETAIL_ACTION)
    @RequiresAuthentication
    public ResponseEntity<?> warehouseDetail(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer id) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
            	if (id == null) {
            		errors.reject("404", "Not found");
            		return;
            	}
            	Warehouse warehouse = WarehouseController.this.warehouseService.getActivated(id);
                WarehouseDto warehouseDto = new WarehouseDto();
                if (warehouse != null) {
                    warehouseDto = DtoFetchingUtils.fetchWarehouse(warehouse);
                    getRpcResponse().addAttribute("warehouse", warehouseDto);
                } else {
                    errors.reject("404", "Not found");
                }
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    @PostMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_ACTION)
    @ResponseBody
    @RequiresAuthentication
    @Validation(validators = WarehouseCodeExistedValidator.class)
    public ResponseEntity<?> createWarehouse(HttpServletRequest request, HttpServletResponse response, @RequestBody WarehouseCreationForm form) {
        AppControllerSupport support = new AppControllerCreationSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
            	WarehouseController.this.doCreateWarehouse(form, getRpcResponse(), (BindingResult) errors);
                getRpcResponse().addAttribute("message", "successful");
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    protected Warehouse doCreateWarehouse(WarehouseCreationForm form, RpcResponse rpcResponse, BindingResult errors) {
        Warehouse warehouse = new Warehouse();
        warehouse.setWarehouseTypeId(form.getWarehouseTypeId());
        warehouse.setName(form.getName());
        warehouse.setCode(form.getCode());
        warehouse.setCityId(form.getCityId());
        warehouse.setDistrictId(form.getDistrictId());
        warehouse.setWardId(form.getWardId());
        warehouse.setAddress(form.getAddress());
        City city = this.cityService.get(form.getCityId());
        warehouse.setRegionId(city.getRegionId());
        String fullAddress = locationService.generateFullAddress(form.getAddress(), form.getWardId(), form.getDistrictId(), form.getCityId());
        warehouse.setFullAddress(fullAddress);
        warehouse.setExtraData(new LinkedHashMap<>());
        warehouse.setStatus(Status.ACTIVE);

        warehouse = this.warehouseService.create(warehouse);
        return warehouse;
    }

    @PutMapping(value = ControllerAction.APP_PORTAL_WAREHOUSE_ACTION)
    @RequiresAuthentication
    public ResponseEntity<?> updateWarehouse(HttpServletRequest request, HttpServletResponse response, @RequestBody WarehouseUpdateForm form) {
        AppControllerSupport support = new AppControllerCreationSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                WarehouseController.this.doUpdateWarehouse(form, getRpcResponse(), (BindingResult) errors);
                getRpcResponse().addAttribute("message", "successful");

            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }

    protected Warehouse doUpdateWarehouse(WarehouseUpdateForm form, RpcResponse rpcResponse, BindingResult errors) {
        Warehouse warehouse = this.warehouseService.getActivated(form.getWarehouseId());
        warehouse.setWarehouseTypeId(form.getWarehouseTypeId());
        warehouse.setName(form.getName());
        warehouse.setCode(form.getCode());
        warehouse.setCityId(form.getCityId());
        warehouse.setDistrictId(form.getDistrictId());
        warehouse.setWardId(form.getWardId());
        warehouse.setAddress(form.getAddress());
        
        String fullAddress = locationService.generateFullAddress(form.getAddress(), form.getWardId(), form.getDistrictId(), form.getCityId());
        warehouse.setFullAddress(fullAddress);

        warehouse = this.warehouseService.updatePartial(warehouse);
        return warehouse;
    }
}
