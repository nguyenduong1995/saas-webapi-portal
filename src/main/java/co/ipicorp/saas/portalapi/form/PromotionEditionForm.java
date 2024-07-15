/**
 * PromotionEditionForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * PromotionEditionForm.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
public class PromotionEditionForm implements Serializable {
    
    private static final long serialVersionUID = 4024724301934098804L;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate endDate;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate preparationDate;
    
    /*
     * Participant location & retailers
     */
    private List<Integer> cities = new ArrayList<>();
    private List<Integer> regions = new ArrayList<>();
    private List<Integer> retailers = new ArrayList<>();
    
    private String content;
    private String note;

    /**
     * get value of <b>startDate</b>.
     * @return the startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Set value to <b>startDate</b>.
     * @param startDate the startDate to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * get value of <b>endDate</b>.
     * @return the endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Set value to <b>endDate</b>.
     * @param endDate the endDate to set
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * get value of <b>preparationDate</b>.
     * @return the preparationDate
     */
    public LocalDate getPreparationDate() {
        return preparationDate;
    }

    /**
     * Set value to <b>preparationDate</b>.
     * @param preparationDate the preparationDate to set
     */
    public void setPreparationDate(LocalDate preparationDate) {
        this.preparationDate = preparationDate;
    }
    
    /**
     * get value of <b>cities</b>.
     * @return the cities
     */
    public List<Integer> getCities() {
        return cities;
    }

    /**
     * Set value to <b>cities</b>.
     * @param cities the cities to set
     */
    public void setCities(List<Integer> cities) {
        this.cities = cities;
    }

    /**
     * get value of <b>regions</b>.
     * @return the regions
     */
    public List<Integer> getRegions() {
        return regions;
    }

    /**
     * Set value to <b>regions</b>.
     * @param regions the regions to set
     */
    public void setRegions(List<Integer> regions) {
        this.regions = regions;
    }

    /**
     * get value of <b>retailers</b>.
     * @return the retailers
     */
    public List<Integer> getRetailers() {
        return retailers;
    }

    /**
     * Set value to <b>retailers</b>.
     * @param retailers the retailers to set
     */
    public void setRetailers(List<Integer> retailers) {
        this.retailers = retailers;
    }

    /**
     * get value of <b>content</b>.
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set value to <b>content</b>.
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * get value of <b>note</b>.
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * Set value to <b>note</b>.
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "PromotionEditionForm [startDate=" + startDate + ", endDate=" + endDate + ", preparationDate=" + preparationDate + ", cities=" + cities
                + ", regions=" + regions + ", retailers=" + retailers + ", note=" + note + "]";
    }
    
}
