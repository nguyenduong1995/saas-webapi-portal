/**
 * OrderSellinPromotionForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import co.ipicorp.saas.portalapi.util.ReportType;

import grass.micro.apps.web.form.validator.LimittedForm;

/**
 * OrderSellinPromotionForm.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
public class PromotionReportSearchForm extends LimittedForm {

    private static final long serialVersionUID = -4752793327855709949L;
    
    private ReportType reportType = ReportType.SELLIN;
    private String keySearch;
    
    /**
     * get value of <b>reportType</b>.
     * @return the reportType
     */
    public ReportType getReportType() {
        return reportType;
    }

    /**
     * Set value to <b>reportType</b>.
     * @param reportType the reportType to set
     */
    public void setReportType(ReportType reportType) {
        this.reportType = reportType;
    }

    /**
     * get value of <b>keySearch</b>.
     * @return the keySearch
     */
    public String getKeySearch() {
        return keySearch;
    }

    /**
     * Set value to <b>keySearch</b>.
     * @param keySearch the keySearch to set
     */
    public void setKeySearch(String keySearch) {
        this.keySearch = keySearch;
    }

    @Override
    public String toString() {
        return "PromotionReportSearchForm [reportType=" + reportType + ", keySearch=" + keySearch + "]";
    }
    
}
