package co.ipicorp.saas.portalapi.form.validator;

import co.ipicorp.saas.nrms.model.Warehouse;
import co.ipicorp.saas.nrms.service.WarehouseService;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.WarehouseImportTicketForm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.io.Serializable;

import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class WarehouseExistedValidator extends AbstractFormValidator {

	@Override
	public boolean support(Serializable form) {
		return true;
	}
	
	@Autowired
	private WarehouseService warehouseService;
	
	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		WarehouseImportTicketForm warehouseImportTicketForm = (WarehouseImportTicketForm) form;
		return this.validateById(warehouseImportTicketForm, errors);
	}
	
	private boolean validateById(WarehouseImportTicketForm warehouseImportTicketForm, Errors errors) {
		Integer warehouseId = warehouseImportTicketForm.getWarehouseId();
		if ( warehouseId != null ) {
			Warehouse warehouse = this.warehouseService.get(warehouseId);
			if ( warehouse == null ) {
				errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST,
						new Object[] { "Warehouse Id",  warehouseId },
						ErrorCode.APP_1401_FIELD_NOT_EXIST);
			}
		}
		return !errors.hasErrors();
	}
}
