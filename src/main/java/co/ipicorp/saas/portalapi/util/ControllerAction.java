/**
 * ControllerAction.java
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.util;

/**
 * ControllerAction. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class ControllerAction {

    public static final String APP_ERROR_ACTION = "/error";
    public static final String APP_COMMON_ACTION = "/common";
    public static final String APP_LOGIN_ACTION = "/login";
    public static final String APP_LOGOUT_ACTION = "/logout";

    /*
     * CUSTOMER ANON ACTION
     */
    public static final String APP_AUTH_FORGOT_PASSWORD_ACTION = "/forgot-password";
    public static final String APP_AUTH_RESET_PASSWORD_ACTION = "/reset-password";
    public static final String APP_AUTH_RESET_PASSWORD_CHECK_KEY_ACTION = "/reset-password/check";
    public static final String APP_CUSTOMER_RESET_PASSWORD_ACTION = "/reset-password";
    public static final String APP_CUSTOMER_REGISTER_ACTION = "/customers/register";
    public static final String APP_CUSTOMER_ACTION = "/customers";
    
    /*
     * Setting action
     */
    public static final String APP_PORTAL_SETTING_ACTION = "/settings";
    public static final String APP_PORTAL_SETTING_DETAIL_ACTION = "/settings/{id}";
    
    /*
     * PriceUint action
     */
    public static final String APP_PORTAL_PRICE_UNIT_ACTION = "/price-units";
    public static final String APP_PORTAL_PRICE_UNIT_DETAIL_ACTION = "/price-units/{id}";
    
    /*
     * Invoice action
     */
    public static final String APP_PORTAL_INVOCE_ACTION = "/invoices";
    public static final String APP_PORTAL_INVOICE_DETAIL_ACTION = "/invoices/{id}";
    
    /*
     * Plan action
     */
    public static final String APP_PORTAL_PLAN_ACTION = "/plans";
    public static final String APP_PORTAL_PLAN_DETAIL_ACTION = "/plans/{id}";
    
    /*
     * User action
     */
    public static final String APP_PORTAL_USER_ACTION = "/users";
    public static final String APP_PORTAL_USER_DETAIL_ACTION = "/users/{id}";
    
    /*
     * Retailer Warehouse Item action
     */
    public static final String APP_PORTAL_RETAILER_WAREHOUSE_ITEM_BY_RETAILER_ACTION = "/retailer/warehouse/items";
    public static final String APP_PORTAL_RETAILER_WAREHOUSE_ITEM_DETAIL_ACTION = "/retailer/warehouse/items";
    

    /*
     * Warehouse
     */
    public static final String APP_PORTAL_WAREHOUSE_ACTION = "/warehouses";
    public static final String APP_PORTAL_WAREHOUSE_DETAIL_ACTION = "/warehouse/{id}";
    
    /*
     * Warehouse Import Ticket
     */
    public static final String APP_PORTAL_WAREHOUSE_IMPORT_TICKET_ACTION = "/warehouse/import/tickets";
    public static final String APP_PORTAL_WAREHOUSE_IMPORT_TICKET_SEARCH_ACTION = "/warehouse/import/tickets/search";
    public static final String APP_PORTAL_WAREHOUSE_IMPORT_TICKET_DETAIL_ACTION = "/warehouse/import/ticket/detail";
    
    /*
     * Warehouse Import Ticket Item
     */
    public static final String APP_PORTAL_WAREHOUSE_IMPORT_TICKET_ITEM_ACTION = "/warehouse/import/ticket/items/";
    public static final String APP_PORTAL_WAREHOUSE_IMPORT_TICKET_ITEM_UPLOAD_ACTION = "/warehouse/import/ticket/items/upload";
    public static final String APP_PORTAL_WAREHOUSE_IMPORT_TICKET_ITEM_DOWNLOAD_ACTION = "/warehouse/import/ticket/items/template/download";
    public static final String APP_PORTAL_WAREHOUSE_IMPORT_TICKET_ERROR_DOWNLOAD_ACTION = "/warehouse/import/ticket/items/error/download";
    
    /*
     * Warehouse Daily Remain
     */
    public static final String APP_PORTAL_WAREHOUSE_DAILY_REMAIN_ACTION = "/warehouse/daily/remain/";
    
    /*
     * Retailer action
     */
    public static final String APP_PORTAL_RETAILER_ACTION = "/retailers";
    public static final String APP_PORTAL_RETAILER_SUMMARY_ACTION = "/retailers/summary";
    public static final String APP_PORTAL_RETAILER_DETAIL_ACTION = "/retailers/{id}";
    public static final String APP_PORTAL_RETAILER_SEARCH_ACTION = "/retailers/search";
    public static final String APP_PORTAL_RETAILER_DETAIL_SEARCH_ACTION = "/retailers/{id}/search";
    public static final String APP_PORTAL_RETAILER_CHANGE_AVATAR_ACTION = "/retailers/changeAvatar";
    public static final String APP_PORTAL_RETAILER_CHANGE_STATUS_ACTION = "/retailers/{id}/changeStatus";
    public static final String APP_PORTAL_RETAILER_EXPORT_ACTION = "/retailers/export";
    
    /*
     * Retailer Invoice Info action
     */
    public static final String APP_PORTAL_RETAILER_INVOICE_ACTION = "/retailers/{id}/invoiceInfo";
    public static final String APP_PORTAL_RETAILER_INVOICE_DETAIL_ACTION = "/retailers/{id}/invoiceInfo";
    
    /*
     * Product Action
     */
    public static final String APP_PORTAL_CONSUMER_PRODUCT_ACTION = "/products";
    public static final String APP_PORTAL_CONSUMER_LIST_ALL_PRODUCT_ACTION = "/products/all";
    public static final String APP_PORTAL_CONSUMER_PRODUCT_DETAIL_ACTION = "/products/detail";
    public static final String APP_PORTAL_CONSUMER_PRODUCT_SEARCH_DETAIL_ACTION = "/products/{id}/detail";
    
    public static final String APP_PORTAL_CONSUMER_PRODUCT_VARIATION_ACTION = "/productVariations";
    public static final String APP_PORTAL_CONSUMER_PRODUCT_RESOURCE_ACTION = "/productResources";
    public static final String APP_PORTAL_CONSUMER_PRODUCT_RESOURCE_DELETE_ACTION = "/productResources/delete";

    /*
     * Order Sellin action
     */
    public static final String APP_PORTAL_ORDER_SELLIN_ACTION = "/orders/sellin";
    public static final String APP_PORTAL_ORDER_SELLIN_STATE_ACTION = "/orders/sellin/state";
    public static final String APP_PORTAL_ORDER_SELLIN_SEARCH_ACTION = "/orders/sellin/search";
    public static final String APP_PORTAL_ORDER_SELLIN_DETAIL_ACTION = "/orders/sellin/{id}";
    public static final String APP_PORTAL_ORDER_SELLIN_COUNT_ACTION = "/orders/sellin/count";
    public static final String APP_PORTAL_RETAILER_ORDER_SELLIN_SEARCH_ACTION = "/orders/sellin/retailer/search";
    
    /*
     * Order Sellout action
     */
    public static final String APP_PORTAL_RETAILER_ORDER_SELLOUT_SEARCH_ACTION = "/orders/sellout/retailer/search";
    
    /*
     * Promotion Condition Format
     */
    public static final String APP_PORTAL_PROMOTION_CONDITION_FORMAT_ACTION = "/promotions/condition/format";
    
    /*
     * Promotion Reward Format
     */
    public static final String APP_PORTAL_PROMOTION_REWARD_FORMAT_ACTION = "/promotions/reward/format";
    
    /*
     * Promotion
     */
    public static final String APP_PORTAL_PROMOTION_CHECK_DUPLICATE_CODE_ACTION = "/promotions/checkCode";
    public static final String APP_PORTAL_PROMOTION_ACTION = "/promotions";
    public static final String APP_PORTAL_PROMOTION_RUNNING_STATS_ACTION = "/promotions/stats";
    public static final String APP_PORTAL_PROMOTION_SEARCH_ACTION = "/promotions/search";
    public static final String APP_PORTAL_PROMOTION_DETAIL_ACTION = "/promotions/{id}";
    public static final String APP_PORTAL_PROMOTION_SUGGEST_ACTION = "/promotion/suggest";
    public static final String APP_PORTAL_PROMOTION_BANNER_ACTION = "/promotions/{id}/banner";
    public static final String APP_PORTAL_PROMOTION_SWITCH_STATUS_ACTION = "/promotions/{id}/switchStatus";
    public static final String APP_PORTAL_PROMOTION_FORCE_FINISH_ACTION = "/promotions/{id}/forceFinish";
    public static final String APP_PORTAL_PROMOTION_CHANGE_INFO_ACTION = "/promotions/{id}/changeInfo";
    public static final String APP_PORTAL_PROMOTION_HISTORY_ACTION = "/promotions/{id}/histories";
    
    public static final String APP_PORTAL_PROMOTION_REPORT_SUMMARY_ACTION = "/promotions/{id}/summary";
    public static final String APP_PORTAL_PROMOTION_RETAILER_SUMMARY_ACTION = "/promotions/{id}/retailers/summary";
    public static final String APP_PORTAL_PROMOTION_RETAILER_DETAIL_SUMMARY_ACTION = "/promotions/{id}/retailers/{retailerId}/summary";
    public static final String APP_PORTAL_PROMOTION_RETAILER_ORDER_ACTION = "/promotions/{id}/retailers/{retailerId}/orders";
    public static final String APP_PORTAL_PROMOTION_RETAILER_PRODUCT_VARIATION_ACTION = "/promotions/{id}/retailers/{retailerId}/productVariations";
    
    /* Promotion -- Export Excel action */
    public static final String APP_PORTAL_PROMOTION_REPORT_EXPORT_ACTION = "/promotions/{id}/export";
    public static final String APP_PORTAL_PROMOTION_RETAILER_EXPORT_ACTION = "/promotions/{id}/retailers/{retailerId}/export";
    
    /*
     * Retailer Warehouse Item History
     */
    public static final String APP_PORTAL_WAREHOUSE_ITEM_HISTORY_ACTION = "/warehouse/item/history";
    public static final String APP_PORTAL_RETAILER_WAREHOUSE_ITEM_HISTORY_ACTION = "/retailer/warehouse/item/history";
    
    /*
     * Performance action
     */
    public static final String APP_PORTAL_PERFORMANCE_ACTION = "/performance";
    
    /*
     * Staff of customer action
     */
    public static final String APP_PORTAL_STAFF_OF_CUSTOMER_ACTION = "/customer/staff";
    public static final String APP_PORTAL_STAFF_OF_CUSTOMER_DETAIL_ACTION = "/customer/staff/detail";
    public static final String APP_PORTAL_STAFF_OF_CUSTOMER_UPLOAD_AVATAR_ACTION = "/customer/staff/avatar";
    public static final String APP_CHANGE_AVATAR_ACTION = "/changeAvatar";
    public static final String APP_PORTAL_STAFF_OF_CUSTOMER_RESET_PASSWORD_ACTION = "/customer/staff/{id}/resetPassword";
    
    /*
     * Category action
     */
    public static final String APP_PORTAL_CATEGORY_ACTION = "/categories";
    public static final String APP_PORTAL_CATEGORY_IMAGE_ACTION = "/categories/image";
}

