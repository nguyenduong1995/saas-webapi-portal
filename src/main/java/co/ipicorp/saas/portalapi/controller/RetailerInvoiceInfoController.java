/**
 * RetailerInvoiceInfoController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     ntduong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import co.ipicorp.saas.nrms.model.RetailerInvoiceInfo;
import co.ipicorp.saas.nrms.service.RetailerInvoiceInfoService;
import co.ipicorp.saas.portalapi.form.RetailerInvoiceInfoCreationForm;
import co.ipicorp.saas.portalapi.form.validator.RetailerExistedValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.websocket.server.PathParam;

import grass.micro.apps.annotation.AppJsonSchema;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * RetailerInvoiceInfoController. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
@RestController
@Api(tags = "Retailer Invoice APIs", description = "all APIs relate to create/detail retailer invoice")
public class RetailerInvoiceInfoController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private RetailerInvoiceInfoService retailerInvoiceInfoService;

    @PostMapping(value = ControllerAction.APP_PORTAL_RETAILER_INVOICE_ACTION)
	@Validation(schema = @AppJsonSchema("/schema/retailer_invoice_info_create.json"), validators = { RetailerExistedValidator.class } )
    @NoRequiredAuth
	@Logged
	public ResponseEntity<?> creatRetailerInvoice(HttpServletRequest request, HttpServletResponse response,
	        @PathParam("id") Integer retailerId, @RequestBody RetailerInvoiceInfoCreationForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {
			
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
			    form.setRetailerId(retailerId);
				RetailerInvoiceInfoController.this.doCreateRetailerInvoice(form, getRpcResponse(), (BindingResult) errors);
                getRpcResponse().addAttribute("message", "successful");
			}
		};
			
		return support.doSupport(request, null, errors, errorsProcessor);
	}
    
    @GetMapping(value = ControllerAction.APP_PORTAL_RETAILER_INVOICE_ACTION)
    @NoRequiredAuth
    @Logged
    public ResponseEntity<?> getRetailerInvoice(HttpServletRequest request, HttpServletResponse response,
            @PathVariable("id") Integer retailerId) {
        
        AppControllerSupport support = new AppControllerCreationSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                
                RetailerInvoiceInfo info = RetailerInvoiceInfoController.this.retailerInvoiceInfoService.getByRetailerId(retailerId);
                if (info == null) {
                    errors.reject("Không tìm thấy info");
                } else {
                    getRpcResponse().addAttribute("info", info);
                }
            }
        };
            
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    protected RetailerInvoiceInfo doCreateRetailerInvoice(RetailerInvoiceInfoCreationForm form, RpcResponse rpcResponse, BindingResult errors) {
    	RetailerInvoiceInfo retailerInvoice = new RetailerInvoiceInfo();
    	retailerInvoice.setRetailerId(form.getRetailerId());
    	retailerInvoice.setRetailerInvoiceName(form.getRetailerInvoiceName());
    	retailerInvoice.setAddressText(form.getAddressText());
    	retailerInvoice.setTaxNo(form.getTaxNo());
    	retailerInvoice.setTel(form.getTel());
    	retailerInvoice.setTelExt(form.getTelExt());
    	retailerInvoice.setBankName(form.getBankName());
    	retailerInvoice.setBankBranch(form.getBankBranch());
    	retailerInvoice.setBankAccountNo(form.getBankAccountNo());
    	retailerInvoice.setBankAccountName(form.getBankAccountName());
    	retailerInvoice.setStatus(Status.ACTIVE);
		
		this.retailerInvoiceInfoService.create(retailerInvoice);
		return retailerInvoice;
	}
    
}
