/**
 * ProductController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.nrms.model.AttributeType;
import co.ipicorp.saas.nrms.model.Category;
import co.ipicorp.saas.nrms.model.Product;
import co.ipicorp.saas.nrms.model.ProductCategory;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.ProductVariationAttribute;
import co.ipicorp.saas.nrms.service.AttributeTypeService;
import co.ipicorp.saas.nrms.service.CategoryService;
import co.ipicorp.saas.nrms.service.ProductCategoryService;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.ProductService;
import co.ipicorp.saas.nrms.service.ProductVariationAttributeService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.web.dto.CategoryDto;
import co.ipicorp.saas.nrms.web.dto.ProductVariationDto;
import co.ipicorp.saas.nrms.web.form.ProductForm;
import co.ipicorp.saas.portalapi.form.validator.BrandExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.CategoryExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.ProductExistedValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.DtoFetchingUtils;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.model.util.EntityUpdateTracker;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * ProductController. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
@RestController
@SuppressWarnings("unchecked")
@Api(tags = "Product APIs", description = "all APIs relate to search/create... Product")
public class ProductController {

    @Autowired
    private ErrorsKeyConverter errorsProcessor;

    @Autowired
    private ProductService productService;
    
    @Autowired
    private ProductVariationService pvService;
    
    @Autowired
    private ProductResourceService prService;
    
    @Autowired
    private CategoryService categoryService;
    
    @Autowired
    private ProductCategoryService productCategoryService;
    
    @Autowired
    private ProductVariationAttributeService pVariationAttributeService;
    
    @Autowired
    private AttributeTypeService attributeTypeService;

    @RequestMapping(value = ControllerAction.APP_PORTAL_CONSUMER_LIST_ALL_PRODUCT_ACTION, method = RequestMethod.GET)
    @ResponseBody
    @Logged
    public ResponseEntity<?> listAll(HttpServletRequest request, HttpServletResponse response) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                return productService.getAll();
            }

            @Override
            public String getAttributeName() {
                return "products";
            }

            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                return DtoFetchingUtils.fetchProducts((List<Product>) entities);
            }
        };

        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @RequestMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_ACTION, method = RequestMethod.GET)
    @ResponseBody
    @Logged
    public ResponseEntity<?> listProductsByKeyword(HttpServletRequest request, HttpServletResponse response, @RequestParam("keyword") String keyword) {
        AppControllerSupport support = new AppControllerListingSupport() {

            @Override
            public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                return productService.getProductsByKeyword(keyword);
            }

            @Override
            public String getAttributeName() {
                return "products";
            }

            @Override
            public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
                return entities;
            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
//    @RequestMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_DETAIL_ACTION, method = RequestMethod.GET)
//    @ResponseBody
//    @Logged
//    public ResponseEntity<?> getProductDetail(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") String productId) {
//        AppControllerSupport support = new AppControllerSupport() {
//            
//            @Override
//            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
//                ProductDetailDto dto = productService.getProductDetail(Integer.valueOf(productId));
//                getRpcResponse().addAttribute("product", dto);
//            }
//        };
//        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
//    }
    
    @RequestMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_DETAIL_ACTION, method = RequestMethod.GET)
    @ResponseBody
    @RequiresAuthentication
    @Logged
    public ResponseEntity<?> getProductDetail(HttpServletRequest request, HttpServletResponse response, @RequestParam("id") Integer productId) {
        AppControllerSupport support = new AppControllerSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                Product product = productService.get(productId);
                getRpcResponse().addAttribute("product", DtoFetchingUtils.fetchProduct(product));
                
                List<ProductVariation> pvs = pvService.getByProductId(productId);
                co.ipicorp.saas.nrms.web.util.DtoFetchingUtils.setProductResourceService(prService);
                List<ProductVariationDto> dtos = co.ipicorp.saas.nrms.web.util.DtoFetchingUtils.fetchProductVariations(pvs);
                getRpcResponse().addAttribute("productVariations", dtos);
                
                List<ProductCategory> pcs = productCategoryService.getByProductId(productId);
                List<Integer> categoryLvIds = pcs.stream().map(proCate -> proCate.getCategoryId()).collect(Collectors.toList());
                List<Integer> categoryLv0Ids = pcs.stream().map(proCate -> proCate.getCategoryIdLv0()).collect(Collectors.toList());
				List<Integer> categoryLv1Ids = pcs.stream().map(proCate -> proCate.getCategoryIdLv1()).collect(Collectors.toList());
				List<Integer> categoryLv2Ids = pcs.stream().map(proCate -> proCate.getCategoryIdLv2()).collect(Collectors.toList());
				List<Integer> categoryIds = Stream.of(categoryLvIds, categoryLv0Ids, categoryLv1Ids, categoryLv2Ids).flatMap(Collection::stream).collect(Collectors.toList());
				List<Category> categories = categoryService.get(categoryIds);
				List<CategoryDto> categoryDtos = (List<CategoryDto>) co.ipicorp.saas.nrms.web.util.DtoFetchingUtils.fetchCategories(categories);
                getRpcResponse().addAttribute("categories", categoryDtos);
                
                List<ProductVariationAttribute> pvas = pVariationAttributeService.getByProductId(productId);
                List<Integer> ids = pvas.stream().map(pva -> pva.getAttributeTypeId()).collect(Collectors.toList());
                List<AttributeType> attributeTypes = attributeTypeService.get(ids);
                getRpcResponse().addAttribute("attributeValues", pvas);
                getRpcResponse().addAttribute("attributeTypes", attributeTypes);
            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @RequestMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_SEARCH_DETAIL_ACTION, method = RequestMethod.GET)
    @ResponseBody
    @RequiresAuthentication
    @Logged
    public ResponseEntity<?> searchProductDetail(HttpServletRequest request, HttpServletResponse response, @PathVariable("id") Integer productId) {
        AppControllerSupport support = new AppControllerSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors, ErrorsKeyConverter errorsProcessor) {
                Product product = productService.get(productId);
                getRpcResponse().addAttribute("product", DtoFetchingUtils.fetchProduct(product));
                
                List<ProductVariation> pvs = pvService.getByProductId(productId);
                co.ipicorp.saas.nrms.web.util.DtoFetchingUtils.setProductResourceService(prService);
                List<ProductVariationDto> dtos = co.ipicorp.saas.nrms.web.util.DtoFetchingUtils.fetchProductVariations(pvs);
                getRpcResponse().addAttribute("productVariations", dtos);
                
                List<ProductCategory> pcs = productCategoryService.getByProductId(productId);
                getRpcResponse().addAttribute("categories", co.ipicorp.saas.nrms.web.util.DtoFetchingUtils.fetchProductCategories(pcs));
                
                List<ProductVariationAttribute> pvas = pVariationAttributeService.getByProductId(productId);
                List<Integer> ids = pvas.stream().map(pva -> pva.getAttributeTypeId()).collect(Collectors.toList());
                List<AttributeType> attributeTypes = attributeTypeService.get(ids);
                getRpcResponse().addAttribute("attributeValues", pvas);
                getRpcResponse().addAttribute("attributeTypes", attributeTypes);
            }
        };
        return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
    }
    
    @PostMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_ACTION)
    @Validation(validators = { BrandExistedValidator.class, CategoryExistedValidator.class } )
    @Logged
    public ResponseEntity<?> createProduct(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ProductForm form, BindingResult errors) {
        System.err.println(form);
        AppControllerSupport support = new AppControllerCreationSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                
                Product product = ProductController.this.processSaveAndUpdateProduct(form, true);
                getRpcResponse().addAttribute("message", "successful");
                getRpcResponse().addAttribute("productId", product.getId());
                
            }
        };
            
        return support.doSupport(request, null, errors, errorsProcessor);
    }
    @PutMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_ACTION)
    @Validation(validators = {ProductExistedValidator.class, BrandExistedValidator.class, CategoryExistedValidator.class } )
    @Logged
    public ResponseEntity<?> upateProduct(HttpServletRequest request, HttpServletResponse response,
            @RequestBody ProductForm form, BindingResult errors) {
        AppControllerSupport support = new AppControllerCreationSupport() {
            
            @Override
            public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
                    ErrorsKeyConverter errorsProcessor) {
                Product product = ProductController.this.processSaveAndUpdateProduct(form, false);
                getRpcResponse().addAttribute("message", "successful");
                getRpcResponse().addAttribute("productId", product.getId());
                
            }
        };
            
        return support.doSupport(request, null, errors, errorsProcessor);
    }

    /**
     * @param form
     * @param error
     */
    protected Product processSaveAndUpdateProduct(ProductForm form, boolean isNew) {
        Product product = null; 
        if (!isNew)  {
            product = productService.get(form.getProductId());
            //delete old categories
            setModifierOnProduct(product);
            productCategoryService.deleteProductCategoriesByProductId(product.getId());
        } 
        product = mappingToProductEntity(product, form);
        product = productService.saveOrUpdate(product);
        // create data for product category
        saveOrUpdateProductCategories(product, form.getCategroyIds(), form.getMainCateId());
        return product;
    }

    /**
     * Insert product categories belong product and category
     * @param product 
     * @param categroyIds
     * @param mainCateId 
     */
    private void saveOrUpdateProductCategories(Product product, List<Integer> categroyIds, Integer mainCateId) {
        for (Integer categoryId : categroyIds) {
            ProductCategory productCategory = new ProductCategory();
            Category category = categoryService.get(categoryId);
            productCategory.setCategoryId(categoryId);
            productCategory.setCategoryLevel(category.getLevel());
            productCategory.setProductId(product.getId());
            mappingCategoryLevel(category, productCategory);
            productCategory.setBrandId(product.getBrandId());
            productCategory.setIsMainCategory(mainCateId == categoryId ? 1 : 0);
            productCategoryService.saveOrUpdate(productCategory);
        }
    }

    private void mappingCategoryLevel(Category category, ProductCategory productCategory) {
        Integer level = category.getLevel();
        switch (level) {
            case 0:
                productCategory.setCategoryIdLv0(category.getId());
                break;
            case 1:
                productCategory.setCategoryIdLv0(category.getParentIdLv0());
                productCategory.setCategoryIdLv1(category.getId());
                break;
            case 2:
                productCategory.setCategoryIdLv0(category.getParentIdLv0());
                productCategory.setCategoryIdLv1(category.getParentIdLv1());
                productCategory.setCategoryIdLv2(category.getId());
                break;
            default:
                break;
        }
        
    }

    /**
     * @param form
     * @return
     */
	private Product mappingToProductEntity(Product product, ProductForm form) {
        if (product == null) {
            product = new Product();
        }
        product.setId(form.getProductId());
        product.setName(StringUtils.isNotEmpty(form.getProductName()) ? form.getProductName() : product.getName());
        product.setProductCode(StringUtils.isNotEmpty(form.getProductCode()) ? form.getProductCode() : product.getProductCode());
        product.setBrandId(form.getBrandId() != null ? form.getBrandId() : product.getBrandId());
        product.setStatus(form.getStatus() != null ? Status.fromValue(form.getStatus().intValue()) : Status.ACTIVE);
        product.setMainCateId(form.getMainCateId() != null && form.getMainCateId() != 0 ? form.getMainCateId(): 0);
        return product;
    }

    private void setModifierOnProduct(Product product) {
        if (product != null && product.getId() != null) {
            EntityUpdateTracker.getInstance().track(Product.class, product.getId(), product.getUpdateCount());
        }
    }
    
}
