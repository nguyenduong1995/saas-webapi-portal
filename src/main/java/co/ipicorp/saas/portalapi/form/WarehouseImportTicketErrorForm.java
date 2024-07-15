package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.ipicorp.saas.portalapi.dto.ErrorImportingDto;

public class WarehouseImportTicketErrorForm implements Serializable {

	private static final long serialVersionUID = 4818965175019274691L;

	@JsonProperty("errorItems")
    private List<ErrorImportingDto> errorItems = new ArrayList<ErrorImportingDto>();

	public List<ErrorImportingDto> getErrorItems() {
		return errorItems;
	}

	public void setErrorItems(List<ErrorImportingDto> errorItems) {
		this.errorItems = errorItems;
	}

	@Override
	public String toString() {
		return "WarehouseImportTicketErrorForm [errorItems=" + errorItems + "]";
	}
	
}
