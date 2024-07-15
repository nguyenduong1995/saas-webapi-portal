/**
 * ErrorCode.java
 * @copyright  Copyright © 2020 Micro App
 * @author     hieumicro
 * @package    co.ipicorp.saas.ms.identity.util
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.util;

/**
 * ErrorCode.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
public class ErrorCode {
    public static final String APP_1000_SYSTEM_ERROR = "1000";
    public static final String APP_1001_REQUIRED = "1001";
    
    public static final String APP_1101_ACCOUNT_NOT_EXIST = "1101";
    public static final String APP_1102_ACCOUNT_REQUIRED_GOOGLE_CODE = "1102";
    public static final String APP_1103_GOOGLE_CODE_INVALID = "1103";
    public static final String APP_1104_LOGIN_ERROR = "1104";
    
    public static final String APP_1201_RETAILER_NOT_EXIST = "1201";
    
    //Product Validation Errors
    public static final String APP_1401_FIELD_NOT_EXIST = "1401";
    public static final String APP_1402_FILED_IS_EXISTED = "1402";
    public static final String APP_1403_FIELD_NOT_BELONG_TO_OBJECT = "1403";
    public static final String APP_1404_FIELD_CAN_NOT_BE_NULL = "1404";
    
    public static final String APP_1701_FROM_DATE_NO_REASONABLE = "1701";
    public static final String APP_1702_NUMBER_DAY_TOO_MANY = "1702";
    public static final String APP_1801_ORDER_SELLIN_NOT_EXIST = "1801";
    public static final String APP_1802_ORDER_SELLIN_ITEM_NOT_EXIST = "1802";
    public static final String APP_1803_WAREHOUSE_ITEM_NOT_EXIST = "1803";
    public static final String APP_1804_WAREHOUSE_ITEM_AMOUNT_NOT_ENOUGH = "1804";
    
    public static final String APP_1901_WAREHOUSE_TOTAL_ITEM_NOT_EXIST = "1901";
    
    public static final String APP_1902_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH= "1902";
    
    public static final String APP_1903_RETAILER_WAREHOUSE_TOTAL_ITEM_NOT_EXIST = "1903";
    
    public static final String APP_1904_RETAILER_WAREHOUSE_AMOUNT_AVAILABLE_NOT_ENOUGH= "1904";
    
    public static final String APP_1905_PRODUCT_VARIATION_NOT_EXIST = "1905";
    
    public static final String APP_1906_WAREHOUSE_AMOUNT_NOT_ENOUGH= "1906";
    
    public static final String APP_1909_ORDER_SELLIN_STATE_NOT_EXACTLY = "1909";
    
    public static final String APP_2011_LOGIN_FAIL = "2011";
    
    public static final String APP_2021_EMAIL_INVALID = "2021";
    public static final String APP_2022_ACCOUNT_NOT_EXIST = "2022";
    public static final String APP_2023_ACCOUNT_IS_BLOCKED = "2023";
    
    public static final String APP_2031_KEY_REQUIRED = "2031";
    public static final String APP_2032_KEY_INVALID = "2032";
    
    public static final String APP_2041_ACCOUNT_EXISTED = "2041";
    public static final String APP_2042_KEY_INVALID = "2042";

    public static final String APP_2051_USER_WITH_EMAIL_EXISTED = "2051";
    public static final String APP_2052_USER_WITH_LOGIN_NAME_EXISTED = "2052";
    public static final String APP_2053_USER_IS_NOT_EXISTED = "2053";
    public static final String APP_2054_USER_IS_NOT_INIT_2FA = "2054";
    public static final String APP_2055_WRONG_VERIFICATION_CODE = "2055";
    public static final String APP_2056_OLD_PASSWORD_NOT_MATCH = "2056";

    public static final String APP_2091_EMAIL_HAS_BEEN_EXISTED = "2091";
    public static final String APP_2092_EMAIL_HAS_PASSED_LIMIT = "2092";
    
    public static final String APP_2081_CUSTOMER_IS_NOT_EXISTED = "2081";
    
    // PROMOTION CREATION ERRORS
    public static final String APP_2101_PROMOTION_TYPE_IS_REQUIRED = "2101";
    public static final String APP_2102_PROMOTION_TYPE_INVALID = "2102";
    public static final String APP_2103_SUBJECT_TYPE_INVALID = "2103";
    public static final String APP_2104_SUBJECT_TYPE_IS_REQUIRED = "2104";
    public static final String APP_2105_SUBJECT_TYPE_INVALID = "2105";
    public static final String APP_2106_DATE_IS_REQUIRED = "2106";
    public static final String APP_2107_STARTDATE_MUSTBE_BEFORE_ENDDATE = "2107";
    public static final String APP_2108_PREPARATION_DATE_IS_REQUIRED = "2108";
    public static final String APP_2109_PROMOTION_CODE_IS_REQUIRED = "2109";
    public static final String APP_2110_PROMOTION_CODE_DUPLICATED = "2110";
    public static final String APP_2111_PROMOTION_CODE_WAS_USED = "2111";
    
    // PROMOTION CREATION ERRORS
    public static final String APP_2201_PROMOTION_STATE_INVALID = "2201";
    public static final String APP_2202_PROMOTION_NEW_ENDDATE_INVALID = "2202";
    public static final String APP_2203_STARTDATE_MUSTBE_AFTER_CURRENT_DATE = "2203";
    public static final String APP_2204_PREPARATION_DATE_MUSTBE_AFTER_CURRENT_DATE = "2204";
    public static final String APP_2205_PREPARATION_DATE_MUSTBE_AFTER_START_DATE = "2205";

    public static final String APP_2301_PROMOTION_NOT_FOUND = "2301";
    public static final String APP_2302_PROMOTION_STATE_IS_NOT_VALID_TO_CHANGE = "2302";
    // PROMOTION REPORT ERRORS
    public static final String APP_2303_PROMOTION_REPORT_ORDER_TYPE_INVALID = "2303";
    
    public static final String APP_2400_PRODUCT_VARIATION_IS_EXISTED_IN_ORDER = "2400";
    
    private ErrorCode() {}
}
