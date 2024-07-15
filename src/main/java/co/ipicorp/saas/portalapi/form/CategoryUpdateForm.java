/**
 * CategoryUpdateForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

/**
 * CategoryUpdateForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class CategoryUpdateForm implements Serializable {

	private static final long serialVersionUID = 8899108544970949501L;

	private String name;
	
	private String description;
	
	private Integer id;
	
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
     * get value of <b>id</b>.
     * @return the id
     */
    public Integer getId() {
		return id;
	}

    /**
     * Set value to <b>id</b>.
     * @param id the id to set
     */
	public void setId(Integer id) {
		this.id = id;
	}

	@Override
    public String toString() {
        return "CategoryUpdateForm [name=" + name + "description=" + description + "id=" + id + "]";
    }
	
}
