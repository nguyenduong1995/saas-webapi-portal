/**
 * CategoryImageForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

/**
 * CategoryImageForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class CategoryImageForm implements Serializable {

	private static final long serialVersionUID = -2539114380506482693L;
	
	private Integer id;
	
	private String image;

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

	/**
     * get value of <b>image</b>.
     * @return the image
     */
	public String getImage() {
		return image;
	}

	/**
     * Set value to <b>image</b>.
     * @param image the image to set
     */
	public void setImage(String image) {
		this.image = image;
	}

	@Override
    public String toString() {
        return "CategoryImageForm [image=" + image + "id=" + id + "]";
    }
	
}
