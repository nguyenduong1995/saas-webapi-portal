package co.ipicorp.saas.portalapi.form.search;

import java.io.Serializable;

/**
 * StaffOfCustomerSearchForm.
 * <<< Detail note.
 * @author Duy K. Vo
 * @access public
 */
public class StaffOfCustomerSearchForm implements Serializable {

	private static final long serialVersionUID = -3962012119477670370L;
	
	Integer status;
	
	private Integer segment;

    private Integer offset;

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSegment() {
		return segment;
	}

	public void setSegment(Integer segment) {
		this.segment = segment;
	}

	public Integer getOffset() {
		return offset;
	}

	public void setOffset(Integer offset) {
		this.offset = offset;
	}

	@Override
	public String toString() {
		return "StaffOfCustomerSearchForm [status=" + status + ", segment=" + segment + ", offset=" + offset + "]";
	}
    
    

}
