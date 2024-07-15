/**
 * PortalSettingController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.util.LinkedHashMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.sun.istack.logging.Logger;

import co.ipicorp.saas.core.service.AccountService;
import co.ipicorp.saas.core.service.CustomerService;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.core.web.dto.SettingDto;
import co.ipicorp.saas.nrms.model.Setting;
import co.ipicorp.saas.nrms.service.NrmsSettingService;
import co.ipicorp.saas.nrms.web.config.persistence.CustomerContext;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.portalapi.form.SettingCreationForm;
import co.ipicorp.saas.portalapi.form.SettingEmailForm;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.FetchInstanceUtil;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.service.EmailService;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.GeneralController;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * PortalSettingController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
@SuppressWarnings("unused")
public class PortalSettingController extends GeneralController {
	
    private static final Logger logger = Logger.getLogger(PortalSettingController.class);

	@Autowired
	private CustomerService customerService;

	@Autowired
	private AccountService accountService;

	@Autowired
	private EmailService emailService;

	@Autowired
	private NrmsSettingService settingService;

	@Autowired
    private FileStorageService fileStorageService;
	
	@Autowired
	private ErrorsKeyConverter errorsProcessor;
	
	@PostMapping(value = ControllerAction.APP_PORTAL_SETTING_ACTION + "/consumer")
    @Logged
    public ResponseEntity<?> settingConsumer(HttpServletRequest request, HttpServletResponse response,
    		@RequestBody SettingCreationForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerSupport() {
			
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Setting setting = settingService.findByKey(Setting.SETTING_CONSUMER_KEY);
				boolean isNew = false;
                if (setting == null) {
                    setting = new Setting();
                    isNew = true;
                }
                
                LinkedHashMap<String, String> map = setting.getValue();
                if (map == null) {
                    map = new LinkedHashMap<>();
                }
                
				convertFieldToMap(map, setting, form);
				
				setting.setKey(Setting.SETTING_CONSUMER_KEY);
				setting.setValue(map);
				setting.setStatus(Status.ACTIVE);
				
				if (isNew) {
				    setting = settingService.create(setting);
				} else {
				    setting = settingService.updatePartial(setting);
				}
				
				getRpcResponse().addAttribute("setting", setting);
			}
		}; 

		return support.doSupport(request, response, errors, errorsProcessor);
    }
	
	@PostMapping(value = ControllerAction.APP_PORTAL_SETTING_ACTION + "/retailer")
    @Logged
    public ResponseEntity<?> settingRetailer(HttpServletRequest request, HttpServletResponse response,
    		@RequestBody SettingCreationForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerSupport() {
			
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Setting setting = settingService.findByKey(Setting.SETTING_RETAILER_KEY);
				boolean isNew = false;
                if (setting == null) {
                    setting = new Setting();
                    isNew = true;
                }
                
				LinkedHashMap<String, String> map = setting.getValue();
		        if (map == null) {
		            map = new LinkedHashMap<>();
		        }
		        
		        convertFieldToMap(map, setting, form);
				
				setting.setKey(Setting.SETTING_RETAILER_KEY);
				setting.setValue(map);
				setting.setStatus(Status.ACTIVE);
				
				if (isNew) {
				    setting = settingService.create(setting);
				} else {
				    setting = settingService.updatePartial(setting);
				}
				
				getRpcResponse().addAttribute("setting", setting);
			}
		}; 

		return support.doSupport(request, response, errors, errorsProcessor);
    }
	
	private LinkedHashMap<String, String> convertFieldToMap(LinkedHashMap<String, String> map, Setting setting, SettingCreationForm form) {
		map.put("appName", form.getAppName());
		String oldLogo = "";
		String oldIcon = "";
		String oldAvatar = "";
		
		if (setting != null) {
			oldLogo = setting.getValue().get("logo");
			oldIcon = setting.getValue().get("icon");
			oldAvatar = setting.getValue().get("avatar");
		}
		
		if (form.isEnableIcon()) {
		    map.put("icon", processUploadAndUpdateImage(CustomerContext.getCustomerId(), oldIcon, form.getIcon()));
		}
		
		if (form.isEnableLogo()) {
		    map.put("logo", processUploadAndUpdateImage(CustomerContext.getCustomerId(), oldLogo, form.getLogo()));
		}
		
		if (form.isEnableAvatarLandingPage()) {
		    map.put("avatar", processUploadAndUpdateImage(CustomerContext.getCustomerId(), oldAvatar, form.getAvatarLandingPage()));
		}
		
		return map;
	}
	
	@PostMapping(value = ControllerAction.APP_PORTAL_SETTING_ACTION + "/email")
    @Logged
    public ResponseEntity<?> settingEmail(HttpServletRequest request, HttpServletResponse response,
    		@RequestBody SettingEmailForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerSupport() {
			
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Setting setting = settingService.findByKey(Setting.SETTING_EMAIL_KEY);
				boolean isNew = false;
                if (setting == null) {
                    setting = new Setting();
                    isNew = true;
                }
                
				LinkedHashMap<String, String> map = setting.getValue();
		        if (map == null) {
		            map = new LinkedHashMap<>();
		        }
		        
				map.put("emailStaff", form.getEmailStaff());
				map.put("emailRetailer", form.getEmailRetailer());
				
				setting.setKey(Setting.SETTING_EMAIL_KEY);
				setting.setValue(map);
				setting.setStatus(Status.ACTIVE);
				
				if (isNew) {
				    setting = settingService.create(setting);
				} else {
				    setting = settingService.save(setting);
				}
				
				getRpcResponse().addAttribute("setting", setting);
			}
		}; 

		return support.doSupport(request, response, errors, errorsProcessor);
    }
	
	@GetMapping(value = ControllerAction.APP_PORTAL_SETTING_ACTION + "/consumer")
    @Logged
    public ResponseEntity<?> getConsumer(HttpServletRequest request, HttpServletResponse response) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Setting setting = settingService.findByKey(Setting.SETTING_CONSUMER_KEY);
				SettingDto dto = FetchInstanceUtil.fetchSettingDto(setting, false);
				getRpcResponse().addAttribute("setting", dto);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	@GetMapping(value = ControllerAction.APP_PORTAL_SETTING_ACTION + "/retailer")
    @Logged
    public ResponseEntity<?> getRetailer(HttpServletRequest request, HttpServletResponse response) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Setting setting = settingService.findByKey(Setting.SETTING_RETAILER_KEY);
				SettingDto dto = FetchInstanceUtil.fetchSettingDto(setting, false);
				getRpcResponse().addAttribute("setting", dto);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	@GetMapping(value = ControllerAction.APP_PORTAL_SETTING_ACTION + "/email")
    @Logged
    public ResponseEntity<?> getEmail(HttpServletRequest request, HttpServletResponse response) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Setting setting = settingService.findByKey(Setting.SETTING_EMAIL_KEY);
				SettingDto dto = FetchInstanceUtil.fetchSettingDto(setting, true);
				getRpcResponse().addAttribute("setting", dto);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	protected String processUploadAndUpdateImage(Integer customerId, String oldImage, String newImage) {

		String path = "";
        if (StringUtils.isNotEmpty(newImage)) {
            if (StringUtils.isNotEmpty(oldImage)) {
                String ftpPath = ResourceUrlResolver.getInstance().resolveFtpSettingPath(customerId, oldImage);
                this.fileStorageService.deleteFile(ftpPath);
            }
            String imagePath = uploadImageToFTP(newImage, customerId);
            String extension = "png";
            try {
                extension = newImage.split(";")[0].split("/")[1];
            } catch (Exception ex) {
                // do nothing
            }
            
            path = imagePath + "." + extension;
        }
        
        return path;
    }

    /**
     * @param image
     * @return
     */
    private String uploadImageToFTP(String image, Integer customerId) {
    	String location = ResourceUrlResolver.getInstance().resolveFtpSettingPath(customerId, "");
        String fileName = SystemUtils.getInstance().generateCode("C", customerId, "P", 9);

        this.fileStorageService.storFile(image, location, fileName);
        return fileName;
    }
    
}
