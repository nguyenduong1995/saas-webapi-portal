package co.ipicorp.saas.portalapi.dto;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PriceUnitDto. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
public class PriceUnitDto extends StatusTimestampDto implements Serializable {
	
	private static final long serialVersionUID = 417120084465935733L;
	
	@JsonProperty(value = "type")
    private String type;
	
	@JsonProperty(value = "yearly_price_per_unit")
    private Integer yearlyPrice;

	@JsonProperty(value = "monthly_price_per_unit")
    private Integer monthlyPrice;
    
	@JsonProperty(value = "quota_key")
    private String quotaKey;
    
	@JsonProperty(value = "quota_unit_count")
    private Integer quotaUnitCount;
    
    @JsonProperty(value = "module")
    private ModuleDto module;
	
    /**
	 * @return the type
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param name the type to set
	 */
	public void setType(String type) {
		this.type = type;
	}
    
	/**
	 * @return the yearlyPrice
	 */
	public Integer getYearlyPrice() {
		return yearlyPrice;
	}

	/**
	 * @param name the yearlyPrice to set
	 */
	public void setYearlyPrice(Integer yearlyPrice) {
		this.yearlyPrice = yearlyPrice;
	}
	
	/**
	 * @return the monthlyPrice
	 */
	public Integer getMonthlyPrice() {
		return monthlyPrice;
	}

	/**
	 * @param name the monthlyPrice to set
	 */
	public void setMonthlyPrice(Integer monthlyPrice) {
		this.monthlyPrice = monthlyPrice;
	}
	
	/**
	 * @return the quotaKey
	 */
	public String getQuotaKey() {
		return quotaKey;
	}

	/**
	 * @param name the quotaKey to set
	 */
	public void setQuotaKey(String quotaKey) {
		this.quotaKey = quotaKey;
	}

	/**
	 * @return the quotaUnitCount
	 */
	public Integer getQuotaUnitCount() {
		return quotaUnitCount;
	}

	/**
	 * @param name the quotaUnitCount to set
	 */
	public void setQuotaUnitCount(Integer quotaUnitCount) {
		this.quotaUnitCount = quotaUnitCount;
	}

	/**
	 * @return the module
	 */
	public ModuleDto getModule() {
		return module;
	}

	/**
	 * @param name the module to set
	 */
	public void setModule(ModuleDto module) {
		this.module = module;
	}
	
	@Override
    public String toString() {
        return "PriceUnitDto [type=" + type + ", yearlyPrice=" + yearlyPrice + ", monthlyPrice=" + monthlyPrice 
        		+ ", quotaKey=" + quotaKey + ", quotaUnitCount=" + quotaUnitCount + ", module=" + module + "]";
    }

}
