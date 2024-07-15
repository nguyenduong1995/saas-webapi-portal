package co.ipicorp.saas.portalapi.form.search;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * 
 * WarehouseDailyRemainSearchForm. <<< Detail note.
 * 
 * @author thuy nguyen
 * @access public
 */
public class WarehouseDailyRemainDateSearchForm extends WarehouseDailyRemainSearchForm implements Serializable {

    private static final long serialVersionUID = -24644492899522312L;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate fromDate = LocalDate.now().minusDays(7);
    
    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate toDate = LocalDate.now();

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

    @Override
    public String toString() {
        return "WarehouseDailyRemainDateSearchForm [fromDate=" + fromDate + ", toDate=" + toDate + "]";
    }

}
