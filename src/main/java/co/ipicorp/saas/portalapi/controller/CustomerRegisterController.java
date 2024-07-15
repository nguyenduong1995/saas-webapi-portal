/**
 * CustomerRegisterController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     ntduong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import co.ipicorp.saas.core.model.CustomerRegistration;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.core.service.CustomerRegistrationService;
//import co.ipicorp.saas.core.service.CustomerMetadataService;
import co.ipicorp.saas.core.service.CustomerService;
import co.ipicorp.saas.portalapi.form.CustomerRegisterForm;
import co.ipicorp.saas.portalapi.form.validator.CustomerRegistrationExistedValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.FetchInstanceUtil;

import com.sun.istack.logging.Logger;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.AppJsonSchema;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.service.EmailService;
import grass.micro.apps.service.exception.ServiceException;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.GeneralController;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;

/**
 * CustomerRegisterController. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
@RestController
@SuppressWarnings("unused")
public class CustomerRegisterController extends GeneralController {

	private static final Logger logger = Logger.getLogger(CustomerRegisterController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private FetchInstanceUtil fetchInstanceUtil;

	@Autowired
	private CustomerRegistrationService customerRegistrationService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@PostMapping(value = ControllerAction.APP_CUSTOMER_REGISTER_ACTION)
	@ResponseBody
	@NoRequiredAuth
	@Logged
	@Validation(validators = { CustomerRegistrationExistedValidator.class })
	public ResponseEntity<?> createNewRegister(HttpServletRequest request, HttpServletResponse response,
			@RequestBody CustomerRegisterForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				CustomerRegistration registration = doCreateCustomerRegistration(form, getRpcResponse(), (BindingResult) errors);
				CustomerRegisterController.this.sendMail(registration);
				getRpcResponse().addAttribute("message", "successful");
			}
		};

		return support.doSupport(request, null, errors, errorsProcessor);
	}

	private void sendMail(CustomerRegistration registration) {
		String content = generateEmailContent();
		if (StringUtils.isNotBlank(content)) {
			content = content.replace("[[FULLNAME]]", StringEscapeUtils.escapeHtml4(registration.getRepresentative()));
			logger.info("Send registered email to " + registration.getEmail());
			String title = "Chúc mừng bạn đã đăng ký tài khoản Megasop thành công";
			emailService.sendMail(registration.getEmail(), title, content);
		} else {
			logger.info("Get Email content failure, cannot send email to: " + registration.getEmail());
		}
	}

	protected CustomerRegistration doCreateCustomerRegistration(CustomerRegisterForm form, RpcResponse rpcResponse,
			BindingResult errors) {
		
		CustomerRegistration registration = new CustomerRegistration();
		registration.setRepresentative(form.getRepresentative());
        registration.setRepresentativeMobile(form.getRepresentativeMobile());
        registration.setRepresentativeEmail(form.getRepresentEmail());
        
		registration.setFullname(form.getFullname());
		registration.setWebsite(form.getWebsite());
		registration.setIndustryId(form.getIndustry());
		registration.setEmail(form.getEmail());
		
		registration.setExtra(form.getOrganization());
		registration.setStatus(Status.STATUS_TWO);
		registration = this.customerRegistrationService.create(registration);
		return registration;
	}

	private String generateEmailContent() {
		String content = null;

		BufferedReader br = null;
		try {
			InputStream stream = this.getClass().getResourceAsStream("/new_registration.html");
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
