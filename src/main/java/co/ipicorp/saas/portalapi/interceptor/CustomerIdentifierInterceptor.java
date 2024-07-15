/**
 * CustomerIdentifierInterceptor.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.interceptor;

import co.ipicorp.saas.nrms.web.config.persistence.CustomerContext;
import co.ipicorp.saas.portalapi.security.CustomerSessionInfo;
import co.ipicorp.saas.portalapi.util.Constants;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.web.util.RequestUtils;

/**
 * CustomerIdentifierInterceptor.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
public class CustomerIdentifierInterceptor extends HandlerInterceptorAdapter {
    public static final String CUSTOMER_RETAILER_ORIGIN_KEY = "ORIGIN_RETAILER"; 
    
    private Logger logger = Logger.getLogger(CustomerIdentifierInterceptor.class);
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
        if (info != null && info.getCustomer() != null) {
            logger.info("HAVE SESSION CUSTOMER");
            CustomerContext.set("" + info.getCustomer().getId());
        }
        
        logger.info("NO SESSION CUSTOMER");
        return true;
    }

}
