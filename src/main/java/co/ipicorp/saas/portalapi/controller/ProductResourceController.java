/**
 * ProductResourceController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.model.ProductResource;
import co.ipicorp.saas.nrms.model.ProductResourceType;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.web.config.persistence.CustomerContext;
import co.ipicorp.saas.nrms.web.form.ProductResourceDeleteForm;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.portalapi.util.ControllerAction;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.model.util.EntityUpdateTracker;
import grass.micro.apps.service.exception.ServiceException;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * ProductResourceController. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
@RestController
@Api(tags = "Product Resource APIs", description = "all APIs relate to create/delete Product Resource")
public class ProductResourceController {
    
    private static Logger logger = Logger.getLogger(ProductResourceController.class);
    
    private static String PRODUCT_RESOUCE_PREFIX = "product_";

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private ProductResourceService productResourceService;
    
    @Autowired
    private FileStorageService fileStorageService;

    @PostMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_RESOURCE_ACTION, 
                consumes = "multipart/form-data" ,
                produces = { "application/json"})
    @ResponseBody
    @NoRequiredAuth
    public ResponseEntity<?> create(HttpServletRequest request, HttpServletResponse response, @RequestParam("productId") Integer productId,
            @RequestParam("productVariationId") Integer productVariationId,  @RequestParam("files[]") MultipartFile[] files) {
        logger.info("HAS FILE UPLOADS: " + (files != null && files.length > 0));
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                Map<String, MultipartFile> fileNameCombineByIdsMap = createProductResourcesWithoutUpload(productId, productVariationId, files);
                String location = ResourceUrlResolver.getInstance().resolveFtpProductPath(CustomerContext.getCustomerId(), "");
                fileStorageService.storeMultipleFiles(fileNameCombineByIdsMap, location);
                List<String> filePaths = new ArrayList<>();
                filePaths.addAll(fileNameCombineByIdsMap.keySet());
                updateProductResourceUris(productId, productVariationId, filePaths);
                getRpcResponse().addAttribute("productVariationId", productVariationId);
                getRpcResponse().addAttribute("files", filePaths);
            }

        };

        return support.doSupport(request, response,  RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @DeleteMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_RESOURCE_DELETE_ACTION)
//    @Validation(schema = @AppJsonSchema("/schema/product_resource_delete.json"), 
//    validators = { ProductVariationExistedValidator.class, ProductResourceExistedValidator.class } )
    @NoRequiredAuth
    @Logged
    public ResponseEntity<?> deleteProductResources(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ProductResourceDeleteForm form, BindingResult errors) {
        
        AppControllerSupport support = new AppControllerSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                List<String> resultMessages = removeProductResources(form);
                getRpcResponse().addAttribute("result", resultMessages);
                
            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
        
    }
    
    /**
     * @param ProductResourceDeleteForm productResourceDeleteForm
     */
    protected List<String> removeProductResources(ProductResourceDeleteForm productResourceDeleteForm) {
        List<String> resultMessages = new ArrayList<String>();
        List<ProductResource> productResources = productResourceService.getProductResoucesByProductVariationIdWithResouceIds(productResourceDeleteForm.getProductVariationId(),productResourceDeleteForm.getProductResourceIds());
        
        for (ProductResource productResouce : productResources) {
            EntityUpdateTracker.getInstance().track(ProductResource.class, productResouce.getId(), productResouce.getUpdateCount());
            String uri = ResourceUrlResolver.getInstance().resolveFtpProductPath(CustomerContext.getCustomerId(), productResouce.getUri());
            logger.info("HERE productResouce.getUpdateCount(): " + productResouce.getUpdateCount());
            productResourceService.delete(productResouce.getId());
            //delete from FTP
            resultMessages.add("Product Resource Id: " + productResouce.getId() + ", " + fileStorageService.deleteFile(uri));
        }
        return resultMessages;
    }

    /**
     * @param productId
     * @param productVariationId
     * @param filePaths
     */
    protected void updateProductResourceUris(Integer productId, Integer productVariationId, List<String> filePaths) {
        for (String filePath : filePaths) {
            Integer productResourceId = Integer.valueOf(buildAndExtractFileName(productId, productVariationId, null, filePath, true));
            ProductResource productResource = this.productResourceService.get(productResourceId);
            if (productResource == null) {
                throw new ServiceException("Can not find the product resource with id: " + productResourceId);
            }
            
            productResource.setUri(filePath);
            this.productResourceService.saveOrUpdate(productResource);
        }
    }

    /**
     * @param productId
     * @param productVariationId
     * @param size
     * @return
     */
    protected Map<String, MultipartFile> createProductResourcesWithoutUpload(Integer productId, Integer productVariationId, MultipartFile[] files) {
        logger.info("FILES : " + (files != null ? files.length : null));
        Map<String, MultipartFile> fileNameCombineByIds = new HashMap<>();
        String fileName = null;
        for (int i = 0; i < files.length; i ++) {
            MultipartFile file = files[i];
            ProductResource productResource = new ProductResource();
            productResource.setProductId(productId);
            productResource.setProductVariationId(productVariationId);
            productResource.setType(getProductTypeByFileExtension(file));
            productResource.setOrderNumber(i + 1);
            productResource.setStatus(Status.ACTIVE);
            logger.info("file " + file.getOriginalFilename());
            fileName = file.getOriginalFilename();
            productResource.setUri(fileName);
            productResource = this.productResourceService.create(productResource);
            fileName = buildAndExtractFileName(productId, productVariationId, productResource.getId(), fileName, false);
            fileNameCombineByIds.put(fileName, file);
        }
        return fileNameCombineByIds;
    }

    /**
     * @param productId
     * @param productVariationId
     * @param id
     * @param name
     * @param b
     * @return
     */
    private String buildAndExtractFileName(Integer productId, Integer productVariationId, Integer productResourceId, String fileName, boolean isExtracted) {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMM");
        String result = formatter.format(new Date()) + "/";
        String prefixFileName = PRODUCT_RESOUCE_PREFIX + productId + productVariationId;
        if (isExtracted) {
            fileName = Paths.get(fileName).getFileName().toString();
            result = StringUtils.removeEnd(fileName, FilenameUtils.getExtension(fileName)).replace(prefixFileName, "").replace(".", "");
        } else {
            result +=  prefixFileName + productResourceId + "." + FilenameUtils.getExtension(fileName);
        }
        return result;
    }

    /**
     * @param multipartFile
     * @return
     */
    private ProductResourceType getProductTypeByFileExtension(MultipartFile multipartFile) {
        return ProductResourceType.IMAGE;
    }

}
