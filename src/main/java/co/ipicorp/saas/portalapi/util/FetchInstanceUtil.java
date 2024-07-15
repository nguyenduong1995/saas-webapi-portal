/**
 * 
 */
package co.ipicorp.saas.portalapi.util;

import co.ipicorp.saas.core.model.CustomerMetadata;
import co.ipicorp.saas.core.web.dto.SettingDto;
import co.ipicorp.saas.nrms.model.Setting;
import co.ipicorp.saas.nrms.web.config.persistence.CustomerContext;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.portalapi.form.CustomerRegisterForm;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * FetchInstanceUtil
 * 
 * @author nt.duong
 *
 */
@Component
public class FetchInstanceUtil {

    public static FetchInstanceUtil instance;

    public static FetchInstanceUtil getInstance() {
        if (instance == null) {
            instance = new FetchInstanceUtil();
        }
        return instance;
    }

    /**
     * 
     * @param entities
     *            list entity {@link Setting}
     * @return List SettingDto
     */
    public static List<SettingDto> fetchSettings(List<Setting> settings) {
        List<SettingDto> settingDtos = new ArrayList<SettingDto>();
        if (settings != null && !settings.isEmpty()) {
            for (Setting setting : settings) {
                SettingDto settingDto = fetchSettingDto(setting, false);
                settingDtos.add(settingDto);
            }
        }

        return settingDtos;
    }

    /**
     * 
     * @param setting
     *            {@link Setting}
     * @return settingDto {@link SettingDto}
     */
    public static SettingDto fetchSettingDto(Setting setting, boolean isEmail) {
        SettingDto dto = new SettingDto();
        dto.setId(setting.getId());
        dto.setKey(setting.getKey());
        if (setting.getValue() != null && !setting.getValue().isEmpty()) {
        	LinkedHashMap<String, String> map = setting.getValue();
        	if (isEmail) {
        		dto.setEmailStaff(map.get("emailStaff"));
        		dto.setEmailRetailer(map.get("emailRetailer"));
        	} else {
        		dto.setAppName(map.get("appName"));
            	dto.setLogo(getSettingImageFromFtp(map.get("logo")));
            	dto.setIcon(getSettingImageFromFtp(map.get("icon")));
            	dto.setAvatarLandingPage(getSettingImageFromFtp(map.get("avatar")));
        	}
        }

        return dto;
    }
    
    private static String getSettingImageFromFtp(String image) {
        if (StringUtils.isBlank(image)) {
            return "";
        }
        
        return ResourceUrlResolver.getInstance().resolveSettingUrl(CustomerContext.getCustomerId(), image);
    }

    /**
     * 
     * @param setting
     *            {@link Setting}
     * @return settingDto {@link SettingDto}
     */
    public List<CustomerMetadata> fetchCustomerMetadatas(CustomerRegisterForm form, int customerId) {
    	List<CustomerMetadata> customerMetadatas = new ArrayList<CustomerMetadata>();

    	CustomerMetadata customerMetaOrg = new CustomerMetadata();
    	customerMetaOrg.setCustomerId(customerId);
    	customerMetaOrg.setMetaKey("organization_scale");
    	customerMetaOrg.setMetaContent(form.getOrganization());
    	
    	customerMetadatas.add(customerMetaOrg);
    	
        return customerMetadatas;
    }
}
