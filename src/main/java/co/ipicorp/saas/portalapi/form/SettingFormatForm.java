package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

public class SettingFormatForm implements Serializable {
	
	private static final long serialVersionUID = 5527836523372184037L;

	private Integer idSetting;
	
	private String keySetting;
	
	private String valueSetting;
	
	private String stringHtml;

	/**
     * get value of <b>keySetting</b>.
     * @return the keySetting
     */
	public String getKeySetting() {
		return keySetting;
	}

	/**
     * Set value to <b>keySetting</b>.
     * @param keySetting the keySetting to set
     */
	public void setKeySetting(String keySetting) {
		this.keySetting = keySetting;
	}

	/**
     * get value of <b>valueSetting</b>.
     * @return the valueSetting
     */
	public String getValueSetting() {
		return valueSetting;
	}

	/**
     * Set value to <b>valueSetting</b>.
     * @param valueSetting the valueSetting to set
     */
	public void setValueSetting(String valueSetting) {
		this.valueSetting = valueSetting;
	}
	
	/**
     * get value of <b>idSetting</b>.
     * @return the idSetting
     */
	public Integer getIdSetting() {
		return idSetting;
	}

	/**
     * Set value to <b>idSetting</b>.
     * @param idSetting the idSetting to set
     */
	public void setIdSetting(Integer idSetting) {
		this.idSetting = idSetting;
	}

	/**
     * get value of <b>stringHtml</b>.
     * @return the stringHtml
     */
	public String getStringHtml() {
		return stringHtml;
	}

	/**
     * Set value to <b>stringHtml</b>.
     * @param stringHtml the stringHtml to set
     */
	public void setStringHtml(String stringHtml) {
		this.stringHtml = stringHtml;
	}

	@Override
    public String toString() {
        return "SettingFormatForm [idSetting=" + idSetting + ", keySetting=" + keySetting + ", valueSetting=" + valueSetting + ", stringHtml=" + stringHtml + "]";
    }
}
