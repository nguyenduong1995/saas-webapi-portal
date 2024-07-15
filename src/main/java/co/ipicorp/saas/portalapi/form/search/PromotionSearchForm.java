/**
 * PromotionSearchForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form.search;

import java.io.Serializable;
import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

/**
 * PromotionSearchForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class PromotionSearchForm implements Serializable {

	private static final long serialVersionUID = -3704400987583530407L;

	private String state;

    private Integer segment;

    private Integer offset;
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fromDate;
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate toDate;
    
    private String keyword;

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
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

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	@Override
	public String toString() {
		return "PromotionSearchForm [state=" + state + ", segment=" + segment + ", offset=" + offset + ", fromDate="
				+ fromDate + ", toDate=" + toDate + ", keyword=" + keyword + "]";
	}
	
}
