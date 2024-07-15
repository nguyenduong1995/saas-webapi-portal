/**
 * SettingCreationForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import java.io.Serializable;

/**
 * SettingCreationForm. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
public class SettingCreationForm implements Serializable {

    private static final long serialVersionUID = 5257657554880674521L;

    private String appName;

    private String logo;

    private String icon;

    private String avatarLandingPage;

    private boolean enableLogo = false;

    private boolean enableIcon = false;

    private boolean enableAvatarLandingPage = false;

    public SettingCreationForm() {
    }

    /**
     * get value of <b>appName</b>.
     * 
     * @return the appName
     */
    public String getAppName() {
        return appName;
    }

    /**
     * Set value to <b>appName</b>.
     * 
     * @param appName
     *            the appName to set
     */
    public void setAppName(String appName) {
        this.appName = appName;
    }

    /**
     * get value of <b>logo</b>.
     * 
     * @return the logo
     */
    public String getLogo() {
        return logo;
    }

    /**
     * Set value to <b>logo</b>.
     * 
     * @param logo
     *            the logo to set
     */
    public void setLogo(String logo) {
        this.logo = logo;
    }

    /**
     * get value of <b>icon</b>.
     * 
     * @return the icon
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Set value to <b>icon</b>.
     * 
     * @param icon
     *            the icon to set
     */
    public void setIcon(String icon) {
        this.icon = icon;
    }

    /**
     * get value of <b>avatarLandingPage</b>.
     * 
     * @return the avatarLandingPage
     */
    public String getAvatarLandingPage() {
        return avatarLandingPage;
    }

    /**
     * Set value to <b>avatarLandingPage</b>.
     * 
     * @param avatarLandingPage
     *            the avatarLandingPage to set
     */
    public void setAvatarLandingPage(String avatarLandingPage) {
        this.avatarLandingPage = avatarLandingPage;
    }

    /**
     * get value of <b>enableLogo</b>.
     * 
     * @return the enableLogo
     */
    public boolean isEnableLogo() {
        return enableLogo;
    }

    /**
     * Set value to <b>enableLogo</b>.
     * 
     * @param enableLogo
     *            the enableLogo to set
     */
    public void setEnableLogo(boolean enableLogo) {
        this.enableLogo = enableLogo;
    }

    /**
     * get value of <b>enableIcon</b>.
     * 
     * @return the enableIcon
     */
    public boolean isEnableIcon() {
        return enableIcon;
    }

    /**
     * Set value to <b>enableIcon</b>.
     * 
     * @param enableIcon
     *            the enableIcon to set
     */
    public void setEnableIcon(boolean enableIcon) {
        this.enableIcon = enableIcon;
    }

    /**
     * get value of <b>enableAvatarLandingPage</b>.
     * 
     * @return the enableAvatarLandingPage
     */
    public boolean isEnableAvatarLandingPage() {
        return enableAvatarLandingPage;
    }

    /**
     * Set value to <b>enableAvatarLandingPage</b>.
     * 
     * @param enableAvatarLandingPage
     *            the enableAvatarLandingPage to set
     */
    public void setEnableAvatarLandingPage(boolean enableAvatarLandingPage) {
        this.enableAvatarLandingPage = enableAvatarLandingPage;
    }

    @Override
    public String toString() {
        return "SettingCreationForm [appName=" + appName + ", logo=" + logo + ", icon=" + icon + ", avatarLandingPage=" + avatarLandingPage + ", enableLogo="
                + enableLogo + ", enableIcon=" + enableIcon + ", enableAvatarLandingPage=" + enableAvatarLandingPage + "]";
    }
}
