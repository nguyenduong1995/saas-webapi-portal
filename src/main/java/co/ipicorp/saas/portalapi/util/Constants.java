package co.ipicorp.saas.portalapi.util;

public class Constants {

    public static final String APP_DEFAULT_SITE_NAME_KEY = "app.name";
    // START - FORGOT PASSWORD
    public static final String APP_FORGOTPASSWORD_EXPIRATION_KEY = "forgotpassword.expiration";
    public static final String APP_FORGOT_PASSWORD_EMAIL_TITLE_KEY = "forgotpassword.email.title";
    public static final String APP_RESET_PASSWORD_EMAIL_TITLE_KEY = "resetpassword.email.title";
    public static final String APP_RESET_URL_KEY = "resetpassword.url";
    public static final String APP_FORGOT_PASSWORD_TEMPLATE_KEY = "resetpassword.url";
    public static final String APP_PORTAL_URL_KEY = "portal.url";
    
    public static final int    APP_DEFAULT_FORGOTPASSWORD_EXPIRATION = 24;
    public static final String APP_DEFAULT_FORGOT_PASSWORD_TEMPLATE = "/forgot_password.html";
    public static final String APP_DEFAULT_FORGOT_PASSWORD_EMAIL_TITLE = "Đặt lại mật khẩu cho tài khoản Megasop";
    public static final String APP_DEFAULT_RESET_PASSWORD_EMAIL_TITLE = "Tài khoản Megasop của bạn đã được thay đổi mật khẩu";
    // END - FORGOT PASSWORD
    
    public static final String APP_SETTING_MAIL_SUPPORT_KEY = "mail.support";
    public static final String APP_SETTING_REGISTRATION_SUPPORT = "registration.support";
    
    // SESSION INFO
    public static final String APP_SESSION_INFO_KEY = "saas-webapi-portal-session-info";
    
    // HAZELCASE
    public static final String APP_HAZELCAST_SESSION_POOL_KEY = "hazelcast.sessionpool.key";
    public static final String APP_HAZELCAST_SESSION_POOL = "saas-webapi-portal-session-pool";
}
