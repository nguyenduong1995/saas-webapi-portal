/**
 * ProductCategoryController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     thuy.nguyen
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import co.ipicorp.saas.nrms.model.Category;
import co.ipicorp.saas.nrms.model.ProductCategory;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.ProductVariationAttribute;
import co.ipicorp.saas.nrms.model.ProductVariationChangeHistory;
import co.ipicorp.saas.nrms.model.dto.ProductVariationSearchCondition;
import co.ipicorp.saas.nrms.service.CategoryService;
import co.ipicorp.saas.nrms.service.ProductCategoryService;
import co.ipicorp.saas.nrms.service.ProductResourceService;
import co.ipicorp.saas.nrms.service.ProductVariationAttributeService;
import co.ipicorp.saas.nrms.service.ProductVariationChangeHistoryService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.UnitService;
import co.ipicorp.saas.nrms.web.dto.ProductVariationDto;
import co.ipicorp.saas.nrms.web.form.ProductVariationAttributeForm;
import co.ipicorp.saas.nrms.web.form.ProductVariationForm;
import co.ipicorp.saas.nrms.web.util.DtoFetchingUtils;
import co.ipicorp.saas.portalapi.form.search.ProductVariationSearchForm;
import co.ipicorp.saas.portalapi.form.validator.AttributeExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.PackingExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.ProductExistedValidator;
import co.ipicorp.saas.portalapi.form.validator.ProductVariationInOrderValidator;
import co.ipicorp.saas.portalapi.form.validator.ProductVariationSearchValidator;
import co.ipicorp.saas.portalapi.form.validator.UnitExistedValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Logged;
import grass.micro.apps.annotation.NoRequiredAuth;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.model.util.EntityUpdateTracker;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.Api;

/**
 * ProductCategoryController. <<< Detail note.
 * 
 * @author thuy.nguyen
 * @access public
 */
@RestController
@Api(tags = "Product Variation APIs", description = "all APIs relate to create/update Product Variation")
public class ProductVariationController {

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private ProductVariationService productVariationService;

	@Autowired
	private ProductVariationChangeHistoryService pvhService;

	@Autowired
	private ProductVariationAttributeService productVariationAttributeService;

	@Autowired
	private ProductResourceService productResourceService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private CategoryService categoryService;
	
	@Autowired
	private UnitService unitService;

	@RequestMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_VARIATION_ACTION, method = RequestMethod.POST)
	@Validation(validators = { ProductExistedValidator.class, AttributeExistedValidator.class,
			PackingExistedValidator.class, UnitExistedValidator.class })
	@Logged
	public ResponseEntity<?> create(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ProductVariationForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				ProductVariation productVariation = ProductVariationController.this
						.processSaveOrUpdateProductVariation(form, true, (BindingResult) errors);
				getRpcResponse().addAttribute("message", "successful");
				getRpcResponse().addAttribute("productVariationId", productVariation.getId());

			}
		};

		return support.doSupport(request, response, errors, errorsProcessor);
	}

	@PutMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_VARIATION_ACTION)
	@Validation(validators = { PackingExistedValidator.class, ProductVariationInOrderValidator.class })
	@Logged
	public ResponseEntity<?> update(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ProductVariationForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				ProductVariation productVariation = ProductVariationController.this
						.processSaveOrUpdateProductVariation(form, false, (BindingResult) errors);
				getRpcResponse().addAttribute("message", "update successful");
				getRpcResponse().addAttribute("productVariationId", productVariation.getId());
			}
		};

		return support.doSupport(request, response, errors, errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_VARIATION_ACTION + "/search")
	@Validation(validators = { ProductVariationSearchValidator.class })
	@Logged
	@NoRequiredAuth
	public ResponseEntity<?> searchProductVariation(HttpServletRequest request, HttpServletResponse response,
			@GetBody ProductVariationSearchForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				ProductVariationSearchCondition condition = new ProductVariationSearchCondition();
				String props = "segment,offset,categoryIdLv0,categoryIdLv1,categoryIdLv2,status,brandId,keyword";
				SystemUtils.getInstance().copyProperties(form, condition, props.split(","));

				long total = productVariationService.count(condition);
				getRpcResponse().addAttribute("count", total);

				List<ProductVariationDto> dtos = new LinkedList<>();
				if (total > form.getSegment()) {
					List<ProductVariation> productVariations = ProductVariationController.this.productVariationService
							.search(condition);

					DtoFetchingUtils.setProductResourceService(productResourceService);
					dtos = DtoFetchingUtils.fetchProductVariations(productVariations);
					
					if ((form.getKeyword() != null && form.getKeyword().trim() != "") || dtos.size() == 0) {
						getRpcResponse().addAttribute("productVariationDtos", dtos);
						return;
					}
					for (ProductVariationDto dto : dtos) {
						ProductCategory mainCate = productCategoryService.getMainCateByProductId(dto.getProductId());
						if (mainCate != null) {
							List<Integer> categoryIds = Arrays.asList(mainCate.getCategoryIdLv0(),
									mainCate.getCategoryIdLv1(), mainCate.getCategoryIdLv2());
							List<Category> categories = categoryService.get(categoryIds);
							if (categories.size() != 0) {
								for (Category category : categories) {
									switch (category.getLevel()) {
									case 0:
										dto.setCategoryLv0(category.getName());
										break;
									case 1:
										dto.setCategoryLv1(category.getName());
										break;
									default:
										dto.setCategoryLv2(category.getName());
										break;
									}
								}
							}
						}
					}
				}

				getRpcResponse().addAttribute("productVariationDtos", dtos);
			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}
	
	@RequestMapping(value = ControllerAction.APP_PORTAL_CONSUMER_PRODUCT_VARIATION_ACTION + "/searchIds", method = RequestMethod.POST)
	@Logged
	@ResponseBody
	@NoRequiredAuth
	public ResponseEntity<?> searchProductVariationExceptIds(HttpServletRequest request, HttpServletResponse response,
			@RequestBody ProductVariationSearchForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerSupport() {
			
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				ProductVariationSearchCondition condition = new ProductVariationSearchCondition();
				String props = "segment,offset,status,keyword,productVariationIds";
				SystemUtils.getInstance().copyProperties(form, condition, props.split(","));

				long total = productVariationService.countExceptIds(condition);
				getRpcResponse().addAttribute("count", total);

				List<ProductVariationDto> dtos = new LinkedList<>();
				if (total > form.getSegment()) {
					List<ProductVariation> productVariations = ProductVariationController.this.productVariationService
							.searchExceptIds(condition);

					DtoFetchingUtils.setProductResourceService(productResourceService);
					DtoFetchingUtils.setUnitService(unitService);
					DtoFetchingUtils.setProductCategoryService(productCategoryService);
					dtos = DtoFetchingUtils.fetchProductVariations(productVariations);
				}

				getRpcResponse().addAttribute("productVariationDtos", dtos);
			}
		};
		return support.doSupport(request, response, errors, errorsProcessor);
	}

	/**
	 * @param form
	 * @param errors
	 * @return
	 */
	protected ProductVariation processSaveOrUpdateProductVariation(ProductVariationForm form, boolean isNew,
			BindingResult errors) {
		ProductVariation productVariation = null;

		ProductVariationChangeHistory history = null;
		if (!isNew) {
			productVariation = productVariationService.get(form.getProductVariationId());
			setModifierOnProductVariation(productVariation);
			history = createhistory(productVariation, form);
		}

		productVariation = mappingToProductVariationEntity(productVariation, form, isNew);

		try {
			productVariation = productVariationService.saveOrUpdate(productVariation);
			// Create Attributes
			if (!isNew) {
				this.pvhService.create(history);
			} else {
				createProductAttributes(productVariation, form.getAttributes());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return productVariation;
	}

	private ProductVariationChangeHistory createhistory(ProductVariation pv, ProductVariationForm form) {
		ProductVariationChangeHistory history = new ProductVariationChangeHistory();
		history.setProductId(pv.getProductId());
		history.setProductVariationId(pv.getId());

		history.setOldIncomePrice(pv.getIncomePrice());
		history.setOldName(pv.getName());
		history.setOldUnitId(pv.getUnitId());
		history.setOldPackingId(pv.getPackingId());
		history.setOldPrice(pv.getPrice());
		history.setOldPackingPrice(pv.getPackingPrice());
		history.setOldPackingExchangeRatio(pv.getPackingExchangeRatio());
		history.setOldVirtualPrice(pv.getVirtualPrice());

		history.setIncomePrice(form.getIncomePrice());
		history.setName(form.getName());
		history.setUnitId(form.getUnitId());
		history.setPackingId(form.getPackingId());
		history.setPrice(form.getPrice());
		history.setPackingPrice(form.getPackingPrice());
		history.setPackingExchangeRatio(form.getPackingExchangeRatio());
		history.setVirtualPrice(form.getVirtualPrice());

		return history;
	}

	/**
	 * @param productVariation
	 */
	private void setModifierOnProductVariation(ProductVariation productVariation) {
		if (productVariation != null && productVariation.getId() != null) {
			EntityUpdateTracker.getInstance().track(ProductVariation.class, productVariation.getId(),
					productVariation.getUpdateCount());
		}
	}

	/**
	 * create product Attributes belong to Product variation
	 * 
	 * @param productVariation
	 * @param attributes
	 */
	private void createProductAttributes(ProductVariation productVariation,
			List<ProductVariationAttributeForm> attributes) {
		for (ProductVariationAttributeForm attribute : attributes) {
			createProductVariationAttribute(productVariation, attribute);
		}
	}

	/**
	 * @param productVariation
	 * @param attribute
	 * @return
	 */
	private ProductVariationAttribute createProductVariationAttribute(ProductVariation productVariation,
			ProductVariationAttributeForm attributeForm) {
		ProductVariationAttribute productVariationAttribute = new ProductVariationAttribute();
		productVariationAttribute.setProductId(productVariation.getProductId());
		productVariationAttribute.setProductVariationId(productVariation.getId());
		productVariationAttribute.setAttributeId(attributeForm.getAttributeId());
		productVariationAttribute.setAttributeValue(attributeForm.getAttributeValue());
		productVariationAttribute.setAttributeTypeId(attributeForm.getAttributeTypeId());
		productVariationAttribute.setAttributeTypeName(attributeForm.getAttributeTypeValue());

		productVariationAttributeService.create(productVariationAttribute);
		return productVariationAttribute;
	}

	/**
	 * @param form
	 * @return
	 */
	private ProductVariation mappingToProductVariationEntity(ProductVariation productVariation,
			ProductVariationForm form, boolean isNew) {
		if (productVariation == null) {
			productVariation = new ProductVariation();
		}
		productVariation.setProductId(form.getProductId());
		productVariation.setName(StringUtils.isNotEmpty(form.getName()) ? form.getName() : productVariation.getName());
		productVariation.setDescription(StringUtils.isNotEmpty(form.getDescription()) ? form.getDescription()
				: productVariation.getDescription());
		productVariation.setStatus(
				form.getStatus() != null ? Status.fromValue(form.getStatus()) : productVariation.getStatus());

		if (isNew) {
			productVariation.setSku(StringUtils.isNotEmpty(form.getSku()) ? form.getSku() : productVariation.getSku());
		}

		productVariation.setPrice(form.getPrice() != null ? form.getPrice() : productVariation.getPrice());
		productVariation.setVirtualPrice(
				form.getVirtualPrice() != null ? form.getVirtualPrice() : productVariation.getVirtualPrice());
		productVariation.setIncomePrice(
				form.getIncomePrice() != null ? form.getIncomePrice() : productVariation.getIncomePrice());
		productVariation.setPackingPrice(
				form.getPackingPrice() != null ? form.getPackingPrice() : productVariation.getPackingPrice());
		productVariation.setUnitId(form.getUnitId() != null ? form.getUnitId() : productVariation.getUnitId());
		productVariation
				.setPackingId(form.getPackingId() != null ? form.getPackingId() : productVariation.getPackingId());
		productVariation.setMinOrder(form.getMinOrder() != null ? form.getMinOrder() : productVariation.getMinOrder());
		productVariation.setOrderInc(form.getOrderInc() != null ? form.getOrderInc() : productVariation.getOrderInc());
		productVariation.setPackingExchangeRatio(form.getPackingExchangeRatio() != null ? form.getPackingExchangeRatio()
				: productVariation.getPackingExchangeRatio());

		return productVariation;
	}

}
