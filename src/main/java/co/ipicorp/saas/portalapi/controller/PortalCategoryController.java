/**
 * CategoryController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.model.Category;
import co.ipicorp.saas.nrms.service.CategoryService;
import co.ipicorp.saas.nrms.service.ProductCategoryService;
import co.ipicorp.saas.nrms.web.dto.CategoryDto;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.portalapi.form.CategoryCreateForm;
import co.ipicorp.saas.portalapi.form.CategoryImageForm;
import co.ipicorp.saas.portalapi.form.CategoryUpdateForm;
import co.ipicorp.saas.portalapi.form.validator.CategoryExistedValidator;
import co.ipicorp.saas.portalapi.security.CustomerSessionInfo;
import co.ipicorp.saas.portalapi.util.Constants;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;

/**
 * CategoryController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
public class PortalCategoryController {

	@Autowired
    private ErrorsKeyConverter errorsProcessor;
	
	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private ProductCategoryService productCategoryService;
	
	@Autowired
    private FileStorageService fileStorageService;
	
	@GetMapping(value = ControllerAction.APP_PORTAL_CATEGORY_ACTION + "/listAll")
    public ResponseEntity<?> getAllByparentId(HttpServletRequest request, HttpServletResponse response, @RequestParam Integer parentId) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                
                List<Category> categories = null;
                if (parentId > 0) {
                    categories = categoryService.getActivateByParentId(parentId);
                } else {
                    categories = categoryService.getActivateByLevel(0);
                }
                
                List<CategoryDto> dtos = new ArrayList<CategoryDto>();
                if ( categories != null && categories.size() > 0 ) {
                	for ( Category category : categories ) {
                		CategoryDto dto = DtoFetchingUtils.fetchCategory(category);
                		long totalProduct = productCategoryService.count(category.getId());
                		dto.setTotalProduct(totalProduct);
                		dtos.add(dto);
                	}
                }
                
                getRpcResponse().addAttribute("categories", dtos);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	@PostMapping(value = ControllerAction.APP_PORTAL_CATEGORY_ACTION)
	@Logged
    public ResponseEntity<?> createCategory(HttpServletRequest request, HttpServletResponse response, 
    			@RequestBody CategoryCreateForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {

            	Category category = PortalCategoryController.this.doCreateCategory(form);
            	if (category != null) {
            		getRpcResponse().addAttribute("message", "successful");
                    getRpcResponse().addAttribute("categoryId", category.getId());
            	} else {
            		getRpcResponse().addAttribute("message", "Parent id level is incompatible");
            	}
                
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	private Category doCreateCategory(CategoryCreateForm form) {
		
		Category category = new Category();
        category.setName(form.getName());
        category.setDescription(form.getDescription());
        if (form.getParentId() <= 0) {
        	category.setLevel(0);
        } else {
        	Integer level = 0;
        	Category cateByParentId = this.categoryService.getActivated(form.getParentId());
        	if (cateByParentId == null) {
        		return null;
        	}
        	
        	level = cateByParentId.getLevel();
        	if (level == 0) {
        		category.setLevel(1);
        		category.setParentIdLv0(form.getParentId());
        	} else if (level == 1) {
        		category.setLevel(2);
        		category.setParentIdLv0(cateByParentId.getParentIdLv0());
        		category.setParentIdLv1(form.getParentId());
        	} else {
        		return null;
        	}
        }
        
        category.setStatus(Status.ACTIVE);
        this.categoryService.create(category);
        
        return category;
	}

	@PutMapping(value = ControllerAction.APP_PORTAL_CATEGORY_ACTION)
	@Validation(validators = { CategoryExistedValidator.class } )
	@Logged
    public ResponseEntity<?> updateCategory(HttpServletRequest request, HttpServletResponse response, 
    	@RequestBody CategoryUpdateForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {

            	Category category = PortalCategoryController.this.doUpdateCategory(form);
        		getRpcResponse().addAttribute("message", "successful");
                getRpcResponse().addAttribute("categoryId", category.getId());
                
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	private Category doUpdateCategory(CategoryUpdateForm form) {
		Category category = this.categoryService.getActivated(form.getId());
        category.setName(form.getName());
        category.setDescription(form.getDescription());
        this.categoryService.updatePartial(category);
        
        return category;
	}

	@DeleteMapping(value = ControllerAction.APP_PORTAL_CATEGORY_ACTION)
    public ResponseEntity<?> deleteCategory(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") Integer categoryId) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {

            	String message = PortalCategoryController.this.removeCategory(categoryId);
                getRpcResponse().addAttribute("message", message);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	private String removeCategory(Integer categoryId) {
		String message = "";
		long totalProduct = 0;
		Category category = categoryService.getActivated(categoryId);
        if ( category != null ) {
        	totalProduct = productCategoryService.count(category.getId());
        }
        
        if (totalProduct == 0) {
        	productCategoryService.delete(categoryId);
        	message = "successful";
        } else {
        	message = "Product is existed";
        }
        
        return message;
	}
	
	@PutMapping(value = ControllerAction.APP_PORTAL_CATEGORY_IMAGE_ACTION)
	@Logged
    public ResponseEntity<?> updateImageCategory(HttpServletRequest request, HttpServletResponse response, 
    	@RequestBody CategoryImageForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerSupport() {

            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {

            	CustomerSessionInfo sessionInfo = (CustomerSessionInfo) RequestUtils.getInstance().getSessionInfo(request, Constants.APP_SESSION_INFO_KEY);
                Customer customer = sessionInfo.getCustomer();
                
            	Category category = PortalCategoryController.this.processUploadAndUpdateImage(form, customer);
        		getRpcResponse().addAttribute("message", "successful");
                getRpcResponse().addAttribute("categoryId", category.getId());
                
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
	
	/**
     * @param form
     * @param customer
     * @return
     */
    protected Category processUploadAndUpdateImage(CategoryImageForm form, Customer customer) {
    	Category category = this.categoryService.getActivated(form.getId());
        if (StringUtils.isNotEmpty(form.getImage())) {
            //delete old image
            if (StringUtils.isNotEmpty(form.getImage())) {
                this.fileStorageService.deleteFile(category.getImage());
            }
            String imagePath = uploadImageToFTP(form.getImage(), customer.getId(), category.getId());
            category.setImage(imagePath);
        }
        category = this.categoryService.updatePartial(category);
        return category;
    }

    /**
     * @param image
     * @return
     */
    private String uploadImageToFTP(String image, Integer customerId, Integer categoryId) {
        String location = ResourceUrlResolver.getInstance().resolveFtpCategoryPath(customerId, "");
        String fileName = SystemUtils.getInstance().generateCode("C", customerId, "C", categoryId);
        return this.fileStorageService.storFile(image, location, fileName);
    }
    
}
