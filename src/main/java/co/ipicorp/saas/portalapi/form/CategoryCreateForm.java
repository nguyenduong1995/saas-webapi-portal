/**
 * CategoryCreateForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

/**
 * CategoryCreateForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class CategoryCreateForm implements Serializable {

	private static final long serialVersionUID = 2180662002190954039L;
	
	private String name;
	
	private String description;
	
	private Integer parentId;
	
	/**
     * get value of <b>name</b>.
     * @return the name
     */
	public String getName() {
		return name;
	}

	/**
     * Set value to <b>name</b>.
     * @param name the name to set
     */
	public void setName(String name) {
		this.name = name;
	}

	/**
     * get value of <b>description</b>.
     * @return the description
     */
	public String getDescription() {
		return description;
	}

	/**
     * Set value to <b>description</b>.
     * @param description the description to set
     */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
     * get value of <b>parentId</b>.
     * @return the parentId
     */
    public Integer getParentId() {
		return parentId;
	}

    /**
     * Set value to <b>parentId</b>.
     * @param parentId the parentId to set
     */
	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	@Override
    public String toString() {
        return "CategoryCreateForm [name=" + name + "description=" + description + "parentId=" + parentId + "]";
    }
	
}
