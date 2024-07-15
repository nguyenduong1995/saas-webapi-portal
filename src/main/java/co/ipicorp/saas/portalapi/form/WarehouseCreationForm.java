/**
 * WarehouseCreationForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

/**
 * WarehouseCreationForm.
 * <<< Detail note.
 * @author nt.duong
 * @access public
 */
public class WarehouseCreationForm implements Serializable {

	private static final long serialVersionUID = -5673812357042908103L;

	private String name;
    
    private String code;

    private Integer warehouseTypeId;

    private Integer cityId = 0;
    
    private Integer districtId = 0;
    
    private Integer wardId = 0;
    
    private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Integer getWarehouseTypeId() {
		return warehouseTypeId;
	}

	public void setWarehouseTypeId(Integer warehouseTypeId) {
		this.warehouseTypeId = warehouseTypeId;
	}

	public Integer getCityId() {
		return cityId;
	}

	public void setCityId(Integer cityId) {
		this.cityId = cityId;
	}

	public Integer getDistrictId() {
		return districtId;
	}

	public void setDistrictId(Integer districtId) {
		this.districtId = districtId;
	}

	public Integer getWardId() {
		return wardId;
	}

	public void setWardId(Integer wardId) {
		this.wardId = wardId;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	@Override
    public String toString() {
        return "WarehouseCreationForm [name=" + name + "code=" + code + "warehouseTypeId=" + warehouseTypeId + "cityId=" + cityId 
        		+ "districtId=" + districtId + "wardId=" + wardId + "address=" + address + "]";
    }
	
}
