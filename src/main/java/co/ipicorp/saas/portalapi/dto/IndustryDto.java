package co.ipicorp.saas.portalapi.dto;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * IndustryDto. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
public class IndustryDto extends StatusTimestampDto implements Serializable {
	
	private static final long serialVersionUID = 417120084465935733L;
	
	@JsonProperty(value = "name")
    private String name;

	@JsonProperty(value = "industry_code")
    private String industryCode;
    
	@JsonProperty(value = "level")
    private Integer level;
    
	@JsonProperty(value = "description")
    private String description;
    
	@JsonProperty(value = "level1_code")
    private String level1Code;
    
	@JsonProperty(value = "level2_code")
    private String level2Code;
    
	@JsonProperty(value = "level3_code")
    private String level3Code;
    
	@JsonProperty(value = "level4_code")
    private String level4Code;
    
	@JsonProperty(value = "level5_code")
    private String level5Code;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the industryCode
	 */
	public String getIndustryCode() {
		return industryCode;
	}

	/**
	 * @param name the industryCode to set
	 */
	public void setIndustryCode(String industryCode) {
		this.industryCode = industryCode;
	}
	
	/**
	 * @return the level
	 */
	public Integer getLevel() {
		return level;
	}

	/**
	 * @param name the level to set
	 */
	public void setLevel(Integer level) {
		this.level = level;
	}
	
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the level1Code
	 */
	public String getLevel1Code() {
		return level1Code;
	}

	/**
	 * @param name the level1Code to set
	 */
	public void setLevel1Code(String level1Code) {
		this.level1Code = level1Code;
	}
	
	/**
	 * @return the level2Code
	 */
	public String getLevel2Code() {
		return level1Code;
	}

	/**
	 * @param name the level1Code to set
	 */
	public void setLevel2Code(String level2Code) {
		this.level2Code = level2Code;
	}
	
	/**
	 * @return the level3Code
	 */
	public String getLevel3Code() {
		return level3Code;
	}

	/**
	 * @param name the level3Code to set
	 */
	public void setLevel3Code(String level3Code) {
		this.level3Code = level3Code;
	}
	
	/**
	 * @return the level4Code
	 */
	public String getLevel4Code() {
		return level4Code;
	}

	/**
	 * @param name the level4Code to set
	 */
	public void setLevel4Code(String level4Code) {
		this.level4Code = level4Code;
	}
	
	/**
	 * @return the level5Code
	 */
	public String getLevel5Code() {
		return level5Code;
	}

	/**
	 * @param name the level5Code to set
	 */
	public void setLevel5Code(String level5Code) {
		this.level5Code = level5Code;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "IndustryDto [name=" + name + ", industryCode=" + industryCode + ", level=" + level + ", description=" + description 
				+ ", level1Code=" + level1Code + ", level2Code=" + level2Code + ", level3Code=" + level3Code 
				+ ", level4Code=" + level4Code + ", level5Code=" + level5Code + "]";
	}

}
