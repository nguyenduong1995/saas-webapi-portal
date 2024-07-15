package co.ipicorp.saas.portalapi.form.search;

import java.io.Serializable;

/**
 * 
 * WarehouseDailyRemainSearchForm. <<< Detail note.
 * 
 * @author thuy nguyen
 * @access public
 */
public class WarehouseDailyRemainSearchForm implements Serializable {

    private static final long serialVersionUID = -5379337736902826380L;

    private Integer warehouseId;

    private Integer segment = 0;

    private Integer offset = 10;

    private String keyWord;

    public Integer getWarehouseId() {
        return warehouseId;
    }

    public void setWarehouseId(Integer warehouseId) {
        this.warehouseId = warehouseId;
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

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    @Override
    public String toString() {
        return "WarehouseDailyRemainSearchForm [warehouseId=" + warehouseId + ", segment=" + segment + ", offset=" + offset + ", keyWord=" + keyWord + "]";
    }

}
