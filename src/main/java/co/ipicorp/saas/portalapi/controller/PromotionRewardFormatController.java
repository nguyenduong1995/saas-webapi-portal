/**
 * PromotionConditionFormatController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     ntduong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.io.Serializable;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.nrms.model.PromotionRewardFormat;
import co.ipicorp.saas.nrms.service.PromotionRewardFormatService;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.DtoFetchingUtils;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * PromotionConditionFormatController. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
@RestController
@SuppressWarnings("unchecked")
public class PromotionRewardFormatController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;
    
    @Autowired
    private PromotionRewardFormatService promotionRewardFormatService;

    @GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_REWARD_FORMAT_ACTION)
    @ResponseBody
    @NoRequiredAuth
    public ResponseEntity<?> listAll(HttpServletRequest request, HttpServletResponse response) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                return promotionRewardFormatService.getAllActivated();
            }

            @Override
            public String getAttributeName() {
                return "PromotionRewardFormats";
            }

            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                return DtoFetchingUtils.fetchPromotionRewardFormats((List<PromotionRewardFormat>) entities);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
}
