/**
 * CustomerController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.logging.Logger;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.model.CustomerMetadata;
import co.ipicorp.saas.core.model.CustomerMetakeyType;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.core.service.CustomerMetadataService;
import co.ipicorp.saas.core.service.CustomerService;
import co.ipicorp.saas.core.service.SettingService;
import co.ipicorp.saas.core.web.dto.CustomerDto;
import co.ipicorp.saas.core.web.util.DtoFetchingUtils;
import co.ipicorp.saas.portalapi.form.CustomerUpdateForm;
import co.ipicorp.saas.portalapi.security.CustomerSessionInfo;
import co.ipicorp.saas.portalapi.util.Constants;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.service.EmailService;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.GeneralController;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.form.validator.LimittedForm;
import grass.micro.apps.web.util.RequestUtils;

/**
 * CustomerController. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
@RestController
@SuppressWarnings("unused")
public class CustomerController extends GeneralController {
	
    private static final Logger logger = Logger.getLogger(CustomerController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private SettingService settingService;
	
	@Autowired
	private CustomerMetadataService custMetadataService;

	@Autowired
	private ErrorsKeyConverter errorsProcessor;
	
	@GetMapping(value = "/me")
    @Logged
    public ResponseEntity<?> me(HttpServletRequest request, HttpServletResponse response) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                Integer customerId = info.getCustomer().getId();
                Customer customer = customerService.get(customerId);
                info.setCustomer(customer);
                
                CustomerDto dto = DtoFetchingUtils.fetchCustomer(info.getCustomer(), false);
                CustomerMetadata metaData = custMetadataService.getByCustomerIdAndKey(info.getCustomer().getId(), "organization_scale");
                dto.setOrganization(metaData.getMetaContent());

                getRpcResponse().addAttribute("customer", dto);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	@PutMapping(value = "/customer/representative")
    @Logged
    public ResponseEntity<?> updateRepresentative(HttpServletRequest request, HttpServletResponse response, @RequestBody CustomerUpdateForm form ) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
            	CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
            	CustomerController.this.doUpdateRepresentative(info.getCustomer().getId(), form, getRpcResponse(), (BindingResult) errors);
                getRpcResponse().addAttribute("message", "successfull");
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	protected Customer doUpdateRepresentative(Integer id, CustomerUpdateForm form, RpcResponse rpcResponse, BindingResult errors) {
		Customer customer = customerService.getActivated(id);
		customer.setRepresentativeEmail(form.getEmail());
		customer.setRepresentative(form.getRepresentative());
		customer.setRepresentativeMobile(form.getTel());

		customer = this.customerService.updatePartial(customer);
		rpcResponse.addAttribute("customer", customer);
		
		return customer;
	}
	
	@PutMapping(value = "/customer/companyInfo")
    @Logged
    public ResponseEntity<?> updateCompanyInfo(HttpServletRequest request, HttpServletResponse response, @RequestBody CustomerUpdateForm form ) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
            	CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
            	CustomerController.this.doUpdateCompanyInfo(info, form, getRpcResponse(), (BindingResult) errors);
                getRpcResponse().addAttribute("message", "successfull");
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	protected Customer doUpdateCompanyInfo(CustomerSessionInfo info, CustomerUpdateForm form, RpcResponse rpcResponse, BindingResult errors) {
	    Customer customer = customerService.getActivated(info.getCustomer().getId());
		customer.setFullname(form.getFullname());
		customer.setWebsite(form.getWebsite());
		customer.setIndustryId(form.getIndustry());
//		customer.setEmail(form.getEmail());
		
//		Account account = info.getAccount();
//		account.setEmail(form.getEmail());
//			
//		accountService.updatePartial(account);
		customer = this.customerService.updatePartial(customer);
		rpcResponse.addAttribute("customer", customer);
		
		return customer;
	}
	
	@PutMapping(value = "/customer/organization")
    @Logged
    public ResponseEntity<?> updateOrganization(HttpServletRequest request, HttpServletResponse response, @RequestBody CustomerUpdateForm form ) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
            	CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
            	CustomerController.this.doUpdateOrganization(info.getCustomer().getId(), form, getRpcResponse(), (BindingResult) errors);
                getRpcResponse().addAttribute("message", "successfull");
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	protected void doUpdateOrganization(Integer id, CustomerUpdateForm form, RpcResponse rpcResponse,
			BindingResult errors) {
		CustomerMetadata customerMetadata = custMetadataService.getByCustomerIdAndKey(id, "organization_scale");
		boolean isNew = false;
		if (customerMetadata == null) {
		    customerMetadata = new CustomerMetadata();
		    customerMetadata.setMetaKeyType(CustomerMetakeyType.JSON);
		    customerMetadata.setCustomerId(id);
		    isNew = true;
		}
		
		System.err.println("Organization: " + form.getOrganization());
		customerMetadata.setMetaContent(form.getOrganization());
		if (isNew) {
		    this.custMetadataService.create(customerMetadata);
		} else {
		    this.custMetadataService.updatePartial(customerMetadata);
		}
	}
	
	@PutMapping(value = "/customer/subscription")
    @Logged
    public ResponseEntity<?> updateSubscription(HttpServletRequest request, HttpServletResponse response, @RequestBody CustomerUpdateForm form ) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
            	CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
            	System.err.println(info.getCustomer());
            	CustomerController.this.doUpdateSubscription(info.getCustomer().getId(), form, getRpcResponse(), (BindingResult) errors);
                getRpcResponse().addAttribute("message", "successfull");
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	protected void doUpdateSubscription(Integer id, CustomerUpdateForm form, RpcResponse rpcResponse,
			BindingResult errors) {
		List<CustomerMetadata> custMetadatas = custMetadataService.getByCustomerId(id);
		CustomerMetadata customerMetadata = new CustomerMetadata();
		if (custMetadatas != null && custMetadatas.size() > 0 ) {
			Optional<CustomerMetadata> metadata = custMetadatas.stream().filter(custMetadata -> custMetadata.getMetaKey().equals("subscription")).findFirst();
			if (metadata != null && metadata.isPresent()) {
				customerMetadata = metadata.get();
				customerMetadata.setMetaContent(form.getSubscription());
				custMetadataService.update(customerMetadata);
				return;
			}
		}
		customerMetadata.setCustomerId(id);
		customerMetadata.setMetaKey("subscription");
		customerMetadata.setMetaContent(form.getSubscription());
		this.custMetadataService.create(customerMetadata);
	}
	
	@PutMapping(value = "/customer/paymentAddress")
    @Logged
    public ResponseEntity<?> updatePaymentAddress(HttpServletRequest request, HttpServletResponse response, @RequestBody CustomerUpdateForm form ) {
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
            	CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
            	CustomerController.this.doUpdatePaymentAddress(info.getCustomer().getId(), form, getRpcResponse(), (BindingResult) errors);
                getRpcResponse().addAttribute("message", "successfull");
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	protected void doUpdatePaymentAddress(Integer id, CustomerUpdateForm form, RpcResponse rpcResponse,
			BindingResult errors) {
		List<CustomerMetadata> custMetadatas = custMetadataService.getByCustomerId(id);
		CustomerMetadata customerMetadata = new CustomerMetadata();
		if (custMetadatas != null && custMetadatas.size() > 0 ) {
			Optional<CustomerMetadata> metadata = custMetadatas.stream().filter(custMetadata -> custMetadata.getMetaKey().equals("payment_address")).findFirst();
			if (metadata != null && metadata.isPresent()) {
				customerMetadata = metadata.get();
				customerMetadata.setMetaContent(form.getPaymentAddress());
				custMetadataService.update(customerMetadata);
				return;
			}
		}
		customerMetadata.setCustomerId(id);
		customerMetadata.setMetaKey("payment_address");
		customerMetadata.setMetaContent(form.getPaymentAddress());
		this.custMetadataService.create(customerMetadata);
	}
}
