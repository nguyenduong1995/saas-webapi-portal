/**
 * RetailerSearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * RetailerSearchForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class RetailerSearchForm implements Serializable {

	private static final long serialVersionUID = 4921855762703493812L;
	
    private Integer cityId;

    private Integer status;

    private Integer segment;

    private Integer offset;
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fromDate;
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate toDate;
    
    private String keyWord;

    public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

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

	public LocalDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(LocalDate fromDate) {
		this.fromDate = fromDate;
	}

	public LocalDate getToDate() {
		return toDate;
	}

	public void setToDate(LocalDate toDate) {
		this.toDate = toDate;
	}

	public String getKeyWord() {
		return keyWord;
	}

	public void setKeyWord(String keyWord) {
		this.keyWord = keyWord;
	}

	@Override
	public String toString() {
		return "RetailerSearchForm [cityId=" + cityId + ", status=" + status + ", segment=" + segment + ", offset="
				+ offset + ", fromDate=" + fromDate + ", toDate=" + toDate + ", keyWord=" + keyWord + "]";
	}
	
}
