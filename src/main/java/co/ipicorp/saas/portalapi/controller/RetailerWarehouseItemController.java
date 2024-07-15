/**
 * RetailerWarehouseItemController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import co.ipicorp.saas.nrms.model.RetailerWarehouseItem;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseItemService;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.portalapi.form.RetailerWarehouseItemForm;
import co.ipicorp.saas.portalapi.util.ControllerAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * RetailerWarehouseItemController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
@SuppressWarnings("unchecked")
public class RetailerWarehouseItemController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private RetailerWarehouseItemService retailerWarehouseItemService;

    @Autowired
    private ProductResourceService productResourceService;
    
    @GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_WAREHOUSE_ITEM_BY_RETAILER_ACTION)
    @ResponseBody
    @NoRequiredAuth
    public ResponseEntity<?> listAllByRetailerId(HttpServletRequest request, HttpServletResponse response,
    		@RequestBody RetailerWarehouseItemForm form) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                return retailerWarehouseItemService.getAllByRetailerId(form.getRetailerId());
            }

            @Override
            public String getAttributeName() {
                return "retailerWarehouseItems";
            }

            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                DtoFetchingUtils.setProductResourceService(productResourceService);
                return DtoFetchingUtils.fetchRetailerWarehouseItems((List<RetailerWarehouseItem>) entities);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
//    @GetMapping(value = ControllerAction.APP_PORTAL_ORDER_SELLOUT_BY_CONSUMER_ACTION)
//    @ResponseBody
//    @NoRequiredAuth
//    public ResponseEntity<?> listAllByConsumerId(HttpServletRequest request, HttpServletResponse response,
//    		@RequestBody OrderSelloutForm form) {
//        AppControllerSupport support = new AppControllerListingSupport() {
//
//            @Override
//            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
//                    ErrorsKeyConverter errorsProcessor) {
//                return orderSelloutService.getAllByConsumerId(form.getConsumerId());
//            }
//
//            @Override
//            public String getAttributeName() {
//                return "orderSellouts";
//            }
//
//            @Override
//            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
//            	DtoFetchingUtils.setOrderSelloutItemService(orderSelloutItemService);
//                return DtoFetchingUtils.fetchOrderSellouts((List<OrderSellout>) entities);
//            }
//        };
//
//        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
//    }
//    @PostMapping(value = ControllerAction.APP_PORTAL_CONSUMER_ADDRESS_ACTION)
//	@Validation(schema = @AppJsonSchema("/schema/consumer_address_create.json"), validators = { ConsumerExistedValidator.class } )
//    @NoRequiredAuth
//	@Logged
//	public ResponseEntity<?> consumerAddressCreate(HttpServletRequest request, HttpServletResponse response,
//			@RequestBody ConsumerAddressForm form, BindingResult errors) {
//		AppControllerSupport support = new AppControllerCreationSupport() {
//			
//			@Override
//			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
//					ErrorsKeyConverter errorsProcessor) {
//				
//				OrderSelloutController.this.doCreateConsumerAddress(form, getRpcResponse(), (BindingResult) errors);
//                getRpcResponse().addAttribute("message", "successful");
//				
//			}
//		};
//			
//		return support.doSupport(request, null, errors, errorsProcessor);
//	}
//    
//    protected ConsumerAddress doCreateConsumerAddress(ConsumerAddressForm form, RpcResponse rpcResponse, BindingResult errors) {
//    	ConsumerAddress consumerAddress = new ConsumerAddress();
//    	consumerAddress.setConsumerId(form.getConsumerId());
//    	consumerAddress.setName(form.getName());
//    	consumerAddress.setAddressType(AddressType.fromValue(form.getAddressType()));
//    	consumerAddress.setMobile(form.getMobile());
//    	consumerAddress.setAddress(form.getAddress());
//    	consumerAddress.setCityId(form.getCityId());
//    	consumerAddress.setDistrictId(form.getDistrictId());
//    	consumerAddress.setWardId(form.getWardId());
//    	consumerAddress.setUseAsDefault(form.getUseAsDefault());
//    	consumerAddress.setStatus(Status.ACTIVE);
//		
//		this.consumerAddressService.create(consumerAddress);
//		return consumerAddress;
//	}

}
