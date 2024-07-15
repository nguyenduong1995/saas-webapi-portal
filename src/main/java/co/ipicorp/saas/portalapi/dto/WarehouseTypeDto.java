/**
 * WarehouseTypeDto.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.dto;

import java.io.Serializable;

import grass.micro.apps.model.dto.StatusTimestampDto;

/**
 * WarehouseTypeDto. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class WarehouseTypeDto extends StatusTimestampDto implements Serializable {

	private static final long serialVersionUID = -7044178578078615844L;

    private String name;

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

    @Override
    public String toString() {
        return "WarehouseTypeDto [name=" + name + "]";
    }
    
}
