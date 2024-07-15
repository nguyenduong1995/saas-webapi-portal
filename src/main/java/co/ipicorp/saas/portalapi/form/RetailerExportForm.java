package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import co.ipicorp.saas.nrms.web.dto.RetailerDto;

public class RetailerExportForm implements Serializable {
	
	private static final long serialVersionUID = 7621699704457274907L;
	
	@JsonProperty("retailers")
	List<RetailerDto> retailers = new ArrayList<RetailerDto>();

	public List<RetailerDto> getRetailers() {
		return retailers;
	}

	public void setRetailers(List<RetailerDto> retailers) {
		this.retailers = retailers;
	}

	@Override
	public String toString() {
		return "RetailerExportForm [retailers=" + retailers + "]";
	}
	
}
