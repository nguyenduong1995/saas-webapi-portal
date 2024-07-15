package co.ipicorp.saas.portalapi.dto;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * ModuleDto. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
public class ModuleDto extends StatusTimestampDto implements Serializable {
	
	private static final long serialVersionUID = -6922756142853671498L;

	@JsonProperty(value = "module_name")
    private String moduleName;

	@JsonProperty(value = "description")
    private String description;
	
	@JsonProperty(value = "module_group")
    private Integer moduleGroup;
    
	@JsonProperty(value = "module_group_name")
    private String moduleGroupName;
    
	@JsonProperty(value = "required_modules")
    private String requiredModules;

	/**
	 * @return the moduleName
	 */
	public String getModuleName() {
		return moduleName;
	}

	/**
	 * @param name the moduleName to set
	 */
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
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
	 * @return the moduleGroup
	 */
	public Integer getModuleGroup() {
		return moduleGroup;
	}

	/**
	 * @param name the moduleGroup to set
	 */
	public void setModuleGroup(Integer moduleGroup) {
		this.moduleGroup = moduleGroup;
	}
	
	/**
	 * @return the moduleGroupName
	 */
	public String getModuleGroupName() {
		return moduleGroupName;
	}

	/**
	 * @param name the moduleGroupName to set
	 */
	public void setModuleGroupName(String moduleGroupName) {
		this.moduleGroupName = moduleGroupName;
	}
	
	/**
	 * @return the requiredModule
	 */
	public String getRequiredModules() {
		return requiredModules;
	}

	/**
	 * @param name the requiredModule to set
	 */
	public void setRequiredModules(String requiredModules) {
		this.requiredModules = requiredModules;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Module [moduleName=" + moduleName + ", description=" + description + ", moduleGroup=" + moduleGroup
				 + ", moduleGroupName=" + moduleGroupName + ", requiredModules=" + requiredModules + "]";
	}

}
