package co.ipicorp.saas.portalapi.dto;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PlanDto. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
public class PlanDto extends StatusTimestampDto implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -1980748952267895858L;

	@JsonProperty(value = "name")
    private String name;
    
	@JsonProperty(value = "description")
    private String description;
	
	@JsonProperty(value = "is_customize")
    private Integer isCustomize;
	
	@JsonProperty(value = "is_public")
    private Integer isPublic;

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
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param name the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return the isCustomize
	 */
	public Integer getIsCustomize() {
		return isCustomize;
	}

	/**
	 * @param name the isCustomize to set
	 */
	public void setIsCustomize(Integer isCustomize) {
		this.isCustomize = isCustomize;
	}

	/**
	 * @return the isPublic
	 */
	public Integer getIsPublic() {
		return isPublic;
	}

	/**
	 * @param name the isPublic to set
	 */
	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PlanDto [name=" + name + ", description=" + description + ", isCustomize=" + isCustomize + ", isPublic=" + isPublic + "]";
	}

}
