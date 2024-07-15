/**
 * InvoiceController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     ntduong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.logging.Logger;

import co.ipicorp.saas.core.model.Invoice;
import co.ipicorp.saas.core.service.InvoiceService;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.DtoFetchingUtils;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.GeneralController;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * InvoiceController. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
@RestController
@SuppressWarnings("unused")
public class InvoiceController extends GeneralController {
	
    private static final Logger logger = Logger.getLogger(InvoiceController.class);

	@Autowired
	private InvoiceService invoiceService;

	@Autowired
	private ErrorsKeyConverter errorsProcessor;
	
	@GetMapping(value = ControllerAction.APP_PORTAL_INVOCE_ACTION)
	@NoRequiredAuth
	@Logged
	public ResponseEntity<?> listInvoices(HttpServletRequest request, HttpServletResponse response,
			@GetBody LimittedForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerListingSupport() {
			@Override
			public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response,
					Errors errors, ErrorsKeyConverter errorsProcessor) {
				return InvoiceController.this.invoiceService.getAll();
			}

			@Override
			public String getAttributeName() {
				return "invoices";
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
				return DtoFetchingUtils.fetchInvoices((List<Invoice>) entities);
			}
		};

		return support.doSupport(request, response, null, errorsProcessor);
	}
}
