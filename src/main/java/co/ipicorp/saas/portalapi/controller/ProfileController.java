/**
 * ProfileController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.AccountType;
import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.model.StaffOfCustomer;
import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.core.service.CustomerService;
import co.ipicorp.saas.core.service.StaffOfCustomerService;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.portalapi.form.ChangePasswordForm;
import co.ipicorp.saas.portalapi.form.ProfileForm;
import co.ipicorp.saas.portalapi.security.CustomerSessionInfo;
import co.ipicorp.saas.portalapi.util.Constants;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.Logged;
import grass.micro.apps.model.util.EntityUpdateTracker;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * All API related to Profile information of Logged-in User.
 * <<< Detail note.
 * @author hieumicro
 * @access public
 */
@RestController
@Api(tags = "Profile APIs", description = " All API related to Profile information of Logged-in User")
public class ProfileController {
    
    @Autowired
    private ErrorsKeyConverter errorsProcessor;
    
    @Autowired
    private CustomerService customerService;
    
    @Autowired
    private AccountService accountService;
    
    @Autowired
    private StaffOfCustomerService staffService;
    
    @Autowired
    private FileStorageService fileStorageService;
    
    @PostMapping(value = "/profile")
    @Logged
    public ResponseEntity<?> updateProfile(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ProfileForm form, BindingResult errors) {
        
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                ProfileController.this.updateProfile(request, form, info);
            }
        };

        return support.doSupport(request, response, errors, errorsProcessor);
    }
    
    protected void updateProfile(HttpServletRequest request, ProfileForm form, CustomerSessionInfo info) {
        Account account = info.getAccount();
        if (AccountType.STAFF_OF_CUSTOMER.equals(account.getAccountType())) {
            StaffOfCustomer staff = staffService.get(info.getStaffOfCustomer().getId());
            staff.setFullName(form.getFullname());
            staff.setMobile(form.getPhoneNumber());
            this.staffService.updatePartial(staff);
            
            info.setStaffOfCustomer(staff);
            RequestUtils.getInstance().setSessionInfo(request, info, Constants.APP_SESSION_INFO_KEY);
        } else {
            Customer customer = this.customerService.get(info.getCustomer().getId());
            customer.setFullname(form.getFullname());
            customer.setTelephone(form.getPhoneNumber());
            this.customerService.updatePartial(customer);
            
            info.setCustomer(customer);
            RequestUtils.getInstance().setSessionInfo(request, info, Constants.APP_SESSION_INFO_KEY);
        }
    }

    @PostMapping(value = "/profile/avatar")
    @Logged
    public ResponseEntity<?> updateAvatar(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ProfileForm form, BindingResult errors) {
        
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                ProfileController.this.changeAvatar(request, info, form, errors);
                
                String url = "";
                if (AccountType.CUSTOMER.equals(info.getAccount().getAccountType())) {
                    url = ResourceUrlResolver.getInstance().resolveUrl(info.getCustomer().getId(), info.getCustomer().getPicture());
                } else {
                    url = ResourceUrlResolver.getInstance().resolveStaffUrl(info.getCustomer().getId(), info.getStaffOfCustomer().getAvatar());
                }
                
                getRpcResponse().addAttribute("avatar",  url);

            }
        };

        return support.doSupport(request, response, errors, errorsProcessor);
    }
    
    protected void changeAvatar(HttpServletRequest request, CustomerSessionInfo info, ProfileForm form, Errors errors) {
        this.processUploadAndUpdateImage(info, form.getAvatar());
        RequestUtils.getInstance().setSessionInfo(request, info, Constants.APP_SESSION_INFO_KEY);
    }
    
    protected String processUploadAndUpdateImage(CustomerSessionInfo info, String base64Image) {
        Integer customerId = info.getAccount().getCustomerId();
        AccountType type = info.getAccount().getAccountType();
        
        String path = "";
        if (StringUtils.isNotEmpty(base64Image)) {
            String location = "";
            if (AccountType.CUSTOMER.equals(type) && StringUtils.isNotBlank(info.getCustomer().getPicture())) {
                location = ResourceUrlResolver.getInstance().resolveFtpPath(customerId, "");
                this.fileStorageService.deleteFile(location + info.getCustomer().getPicture());
            } else if (AccountType.STAFF_OF_CUSTOMER.equals(type) && StringUtils.isNotBlank(info.getStaffOfCustomer().getAvatar())) {
                location = ResourceUrlResolver.getInstance().resolveFtpStaffPath(customerId, "");
                this.fileStorageService.deleteFile(location + info.getStaffOfCustomer().getAvatar());
            }
            
            String imagePath = uploadImageToFTP(base64Image, customerId, type);
            String extension = "png";
            try {
                extension = base64Image.split(";")[0].split("/")[1];
            } catch (Exception ex) {
                // do nothing
            }
            
            path = imagePath + "." + extension;
            
            if (AccountType.CUSTOMER.equals(type)) {
                Customer customer = this.customerService.get(customerId);
                customer.setPicture(path);
                customer = this.customerService.updatePartial(customer);
                info.setCustomer(customer);
            } else {
                StaffOfCustomer staffOfCustomer = this.staffService.get(info.getStaffOfCustomer().getId());
                staffOfCustomer.setAvatar(path);
                staffOfCustomer = this.staffService.updatePartial(staffOfCustomer);
                info.setStaffOfCustomer(staffOfCustomer);
            }
        }
        
        return path;
    }
    
    private String uploadImageToFTP(String image, Integer customerId, AccountType type) {
        String location = "";
        if (AccountType.CUSTOMER.equals(type)) {
            location = ResourceUrlResolver.getInstance().resolveFtpPath(customerId, "");
        } else {
            location = ResourceUrlResolver.getInstance().resolveFtpStaffPath(customerId, "");
        }
        
        String fileName = SystemUtils.getInstance().generateCode("C", customerId, "M", 9);

        this.fileStorageService.storFile(image, location, fileName);
        return fileName;
    }
    @PostMapping(value = "/profile/password")
    @Logged
    public ResponseEntity<?> changePassword(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ChangePasswordForm form, BindingResult errors) {
        
        AppControllerSupport support = new AppControllerSupport() {
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                CustomerSessionInfo info = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                ProfileController.this.changePassword(request, info, form, errors);
            }
        };

        return support.doSupport(request, response, errors, errorsProcessor);
    }

    protected void changePassword(HttpServletRequest request, CustomerSessionInfo info, ChangePasswordForm form, Errors errors) {
        Account account = this.accountService.get(info.getAccount().getId());
        String enscriptedPasswd = SystemUtils.getInstance().generatePassword(form.getCurrentPassword(), account.getSalt());
        if (!enscriptedPasswd.equals(account.getPassword())) {
            errors.reject(co.ipicorp.saas.portalapi.util.ErrorCode.APP_2056_OLD_PASSWORD_NOT_MATCH,
                    co.ipicorp.saas.portalapi.util.ErrorCode.APP_2056_OLD_PASSWORD_NOT_MATCH);
            return;
        }

        account.setPassword(SystemUtils.getInstance().generatePassword(form.getNewPassword(), account.getSalt()));
        EntityUpdateTracker.getInstance().track(Account.class, account.getId(), account.getUpdateCount());
        this.accountService.update(account);
    }
}
