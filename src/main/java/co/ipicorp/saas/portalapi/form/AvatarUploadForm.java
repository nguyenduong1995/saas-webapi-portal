/**
 * AvatarUploadForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.Serializable;

/**
 * AvatarUploadForm.
 * <<< Detail note.
 * @author thuy nguyen
 * @access public
 */
public class AvatarUploadForm implements Serializable{

    private static final long serialVersionUID = -4547438800693319884L;
    
    @JsonProperty("subjectId")
    private Integer subjectId;
    
    @JsonProperty("avatar")
    private String avatar;

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSubjectId() {
		return subjectId;
	}

	public void setSubjectId(Integer subjectId) {
		this.subjectId = subjectId;
	}

	@Override
	public String toString() {
		return "AvatarUploadForm [subjectId=" + subjectId + ", avatar=" + avatar + "]";
	}
    
}
