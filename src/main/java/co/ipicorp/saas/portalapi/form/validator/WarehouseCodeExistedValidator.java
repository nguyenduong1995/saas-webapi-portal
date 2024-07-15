package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.Warehouse;
import co.ipicorp.saas.nrms.service.WarehouseService;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.WarehouseCreationForm;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class WarehouseCodeExistedValidator extends AbstractFormValidator {
	
	@Autowired
	private WarehouseService warehouseService;

	@Override
	public boolean support(Serializable form) {
		return true;
	}

	@Override
	public boolean doValidate(Serializable form, Errors errors) {
		WarehouseCreationForm warehouseCreationForm = (WarehouseCreationForm) form;
		return this.validateByCode(warehouseCreationForm, errors);
	}

	private boolean validateByCode(WarehouseCreationForm warehouseCreationForm, Errors errors) {
		String warehouseCode = warehouseCreationForm.getCode();
		if (!warehouseCode.trim().isEmpty()) {
			Warehouse warehouse = this.warehouseService.getByCode(warehouseCode.trim());
			if (warehouse != null) {
				errors.reject(ErrorCode.APP_1402_FILED_IS_EXISTED,
						new Object[] { "Warehouse Code",  warehouseCode },
						ErrorCode.APP_1402_FILED_IS_EXISTED);
			}
		} else {
			errors.reject(ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL,
					new Object[] { "Warehouse Code",  warehouseCode },
					ErrorCode.APP_1404_FIELD_CAN_NOT_BE_NULL);
		}
		return !errors.hasErrors();
	}

}
