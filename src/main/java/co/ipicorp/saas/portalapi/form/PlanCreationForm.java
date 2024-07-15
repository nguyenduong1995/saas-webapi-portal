/**
 * PlanCreationForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     ntduong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * PlanCreationForm.
 * <<< Detail note.
 * @author ntduong
 * @access public
 */
public class PlanCreationForm implements Serializable {

	private static final long serialVersionUID = 813335563889465250L;

	@JsonProperty("name")
    private String name;

    @JsonProperty("description")
    private String description;
    
    @JsonProperty("is_customize")
    private Integer isCustomize;
    
    @JsonProperty("is_public")
    private Integer isPublic;
    
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getIsCustomize() {
		return isCustomize;
	}

	public void setIsCustomize(Integer isCustomize) {
		this.isCustomize = isCustomize;
	}

	public Integer getIsPublic() {
		return isPublic;
	}

	public void setIsPublic(Integer isPublic) {
		this.isPublic = isPublic;
	}

	@Override
    public String toString() {
        return "PlanCreationForm [name=" + name + ", description=" + description + ", isCustomize="
                + isCustomize + ", isPublic=" + isPublic + "]";
    }
	
}
