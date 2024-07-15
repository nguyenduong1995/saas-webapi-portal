/**
 * LoginController.java
 * @copyright  Copyright © 2021 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.AccountType;
import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.model.StaffOfCustomer;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.core.service.AccountTwoFactorService;
import co.ipicorp.saas.core.service.CustomerService;
import co.ipicorp.saas.core.service.StaffOfCustomerService;
import co.ipicorp.saas.nrms.web.config.persistence.CustomerContext;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.LoginForm;
import co.ipicorp.saas.portalapi.security.CustomerSessionInfo;
import co.ipicorp.saas.portalapi.util.Constants;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.DtoFetchingUtils;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.component.SystemConfiguration;
import grass.micro.apps.util.PropertiesConstants;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.component.SessionPool;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.dto.RpcResponse;
import grass.micro.apps.web.util.RequestUtils;

/**
 * LoginController.
 * @author hieumicro
 *
 */
@Controller
public class LoginController {

    @Autowired
    private SessionPool<CustomerSessionInfo> pool;

    @Autowired
    private StaffOfCustomerService staffOfCustomerService;
    
    @Autowired
    private CustomerService customerService;

    @Autowired
    private AccountService accountService;

    @SuppressWarnings("unused")
    @Autowired
    private AccountTwoFactorService account2faService;

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    /**
     * Submit Login action processing.
     * 
     * @param request
     *            {@link HttpServletRequest}
     * @param loginForm
     *            {@link LoginForm}
     * @param errors
     *            {@link Errors}
     * @return APP_LOGIN_VIEW if failure, redirect to TopController.APP_TOP_ACTION if success.
     */
    @RequestMapping(value = ControllerAction.APP_LOGIN_ACTION, method = RequestMethod.POST)
    @ResponseBody
    @NoRequiredAuth
    public ResponseEntity<?> login(HttpServletRequest request, HttpServletResponse response,
            @RequestBody LoginForm loginForm, BindingResult errors) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            protected Map<String, String> getFieldMap(HttpServletRequest request, HttpServletResponse response) {
                return LoginForm.fieldMap;
            }

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                processLogin(loginForm, request, response, errors, getRpcResponse());
            }
        };

        return support.doSupport(request, response, errors, errorsProcessor);
    }

    /*
     * @param loginForm
     * 
     * @param request
     * 
     * @param response
     * 
     * @param errors
     */
    private void processLogin(LoginForm loginForm, HttpServletRequest request, HttpServletResponse response,
            Errors errors, RpcResponse rpcResponse) {
        
        if (!errors.hasErrors()) {
            Account account = LoginController.this.accountService.login(loginForm.getLoginName(),
                    loginForm.getPassword(), Arrays.asList(AccountType.CUSTOMER, AccountType.STAFF_OF_CUSTOMER));
            
            // validate user
            if (account != null && (account.getAccountType() == AccountType.CUSTOMER
                    || account.getAccountType() == AccountType.STAFF_OF_CUSTOMER)) {

                if (!errors.hasErrors()) {
                    Customer customer = LoginController.this.customerService.get(account.getCustomerId());
                    if (customer != null) {
                        StaffOfCustomer staff = null;
                        if (AccountType.STAFF_OF_CUSTOMER.equals(account.getAccountType())) {
                            staff = staffOfCustomerService.getByAccountId(account.getId());
                        }
                        
                        LoginController.this.saveSession(request, response, customer, account, staff, loginForm.getPassword(), rpcResponse);
                    } else {
                        errors.reject(ErrorCode.APP_1101_ACCOUNT_NOT_EXIST);
                    }
                }
            } else {
                errors.reject(ErrorCode.APP_1101_ACCOUNT_NOT_EXIST);
            }
        }
    }

    /*
     * Build SessionInfo object from Logged-in User and it into Session.
     * 
     * @param request {@link HttpServletRequest}
     * 
     * @param loginUser {@link User}
     */
    private void saveSession(HttpServletRequest request, HttpServletResponse response, Customer customer, Account account,
            StaffOfCustomer staff, String password, RpcResponse result) {
        CustomerContext.set("" + customer.getId());// Put customer id to context for multi-tenant
        Map<String, Object> map = new LinkedHashMap<>();
        map.put("loginName", account.getLoginName());
        map.put("email", account.getEmail());
        map.put("sessionId", request.getSession(true).getId());
        map.put("accountId", account.getId());
        map.put("accountType", account.getAccountType().toString());
        int timeout = SystemConfiguration.getInstance().getInt(PropertiesConstants.APP_SESSION_TIMEOUT_KEY,
                PropertiesConstants.APP_DEFAULT_SESSION_TIMEOUT);
        String branchName = SystemConfiguration.getInstance().getProperty(Constants.APP_DEFAULT_SITE_NAME_KEY);
        String token = SystemUtils.getInstance().createJWT("A_C_" + customer.getId(), branchName,
                "C" + customer.getAccount().getId(), map, timeout * 60 * 1000);
        result.addAttribute("token", token);
        CustomerSessionInfo sessionInfo = new CustomerSessionInfo();
        sessionInfo.setUsername(account.getLoginName());
        sessionInfo.setAccount(account);
        sessionInfo.setStaffOfCustomer(staff);
        sessionInfo.setToken(token);
        sessionInfo.setCustomer(customer);

        this.pool.putSession(account.getLoginName(), sessionInfo);
        RequestUtils.getInstance().setSessionInfo(request, sessionInfo, Constants.APP_SESSION_INFO_KEY);
        result.addAttribute("accountType", account.getAccountType());
        result.addAttribute("customer", DtoFetchingUtils.fetchCustomer(customer, false));
        if (AccountType.STAFF_OF_CUSTOMER.equals(account.getAccountType())) {
            result.addAttribute("staff", DtoFetchingUtils.fetchStaffOfCustomerInfo(staff));
        } else {
            result.addAttribute("staff", null);
        }
    }
}
