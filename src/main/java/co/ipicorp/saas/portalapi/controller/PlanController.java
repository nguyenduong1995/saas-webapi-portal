/**
 * PlanController.java
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.logging.Logger;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.AccountType;
import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.model.CustomerMetadata;
import co.ipicorp.saas.core.model.Plan;
import co.ipicorp.saas.core.service.PlanService;
import co.ipicorp.saas.portalapi.form.CustomerRegisterForm;
import co.ipicorp.saas.portalapi.form.PlanCreationForm;
import co.ipicorp.saas.portalapi.form.validator.CustomerRegisterExistedValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.DtoFetchingUtils;
import grass.micro.apps.annotation.AppJsonSchema;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.GeneralController;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * PlanController. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
@RestController
@SuppressWarnings("unused")
public class PlanController extends GeneralController {
	
    private static final Logger logger = Logger.getLogger(PlanController.class);

	@Autowired
	private PlanService planService;

	@Autowired
	private ErrorsKeyConverter errorsProcessor;
	
	@GetMapping(value = ControllerAction.APP_PORTAL_PLAN_ACTION)
	@Logged
	public ResponseEntity<?> listPlans(HttpServletRequest request, HttpServletResponse response,
			@GetBody LimittedForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerListingSupport() {
			@Override
			public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response,
					Errors errors, ErrorsKeyConverter errorsProcessor) {
				return PlanController.this.planService.getAll();
			}

			@Override
			public String getAttributeName() {
				return "plans";
			}

			@SuppressWarnings("unchecked")
			@Override
			public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
				return DtoFetchingUtils.fetchPlans((List<Plan>) entities);
			}
		};

		return support.doSupport(request, response, null, errorsProcessor);
	}
	
	@PostMapping(value = ControllerAction.APP_PORTAL_PLAN_ACTION)
	@Validation(schema = @AppJsonSchema("/schema/plan_create.json"))
	@NoRequiredAuth
	@Logged
	public ResponseEntity<?> planCreate(HttpServletRequest request, HttpServletResponse response,
			@RequestBody PlanCreationForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {
			
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				
				PlanController.this.doCreatePlan(form, getRpcResponse(), (BindingResult) errors);
                getRpcResponse().addAttribute("message", "successful");
				
			}
		};
			
		return support.doSupport(request, null, errors, errorsProcessor);
	}
	
	protected Plan doCreatePlan(PlanCreationForm form, RpcResponse rpcResponse, BindingResult errors) {
		Plan plan = new Plan();
		plan.setName(form.getName());
		plan.setDescription(form.getDescription());
		plan.setIsCustomize(form.getIsCustomize());
		plan.setIsPublic(form.getIsPublic());
		plan.setStatus(Status.ACTIVE);
		
		this.planService.create(plan);
		return plan;
	}
}
