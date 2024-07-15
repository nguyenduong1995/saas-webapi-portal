/**
 * PromotionController.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     nt.duong
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.controller;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import co.ipicorp.saas.core.model.City;
import co.ipicorp.saas.core.model.Region;
import co.ipicorp.saas.core.service.CityService;
import co.ipicorp.saas.core.service.RegionService;
import co.ipicorp.saas.core.web.components.FileStorageService;
import co.ipicorp.saas.nrms.model.OrderSellinPromotion;
import co.ipicorp.saas.nrms.model.OrderSellinStatus;
import co.ipicorp.saas.nrms.model.OrderSelloutPromotion;
import co.ipicorp.saas.nrms.model.OrderSelloutStatus;
import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.model.PromotionLimitation;
import co.ipicorp.saas.nrms.model.PromotionLimitationItem;
import co.ipicorp.saas.nrms.model.PromotionLimitationItemRewardProduct;
import co.ipicorp.saas.nrms.model.PromotionLocation;
import co.ipicorp.saas.nrms.model.PromotionParticipantRetailer;
import co.ipicorp.saas.nrms.model.PromotionProductGroup;
import co.ipicorp.saas.nrms.model.PromotionProductGroupDetail;
import co.ipicorp.saas.nrms.model.PromotionState;
import co.ipicorp.saas.nrms.model.PromotionStateChangeHistory;
import co.ipicorp.saas.nrms.model.SubjectType;
import co.ipicorp.saas.nrms.model.dto.PromotionSearchCondition;
import co.ipicorp.saas.nrms.service.OrderSellinPromotionService;
import co.ipicorp.saas.nrms.service.OrderSelloutPromotionService;
import co.ipicorp.saas.nrms.service.ProductCategoryService;
import co.ipicorp.saas.nrms.service.ProductVariationService;
import co.ipicorp.saas.nrms.service.PromotionConditionFormatService;
import co.ipicorp.saas.nrms.service.PromotionLimitationItemRewardProductService;
import co.ipicorp.saas.nrms.service.PromotionLimitationItemService;
import co.ipicorp.saas.nrms.service.PromotionLimitationService;
import co.ipicorp.saas.nrms.service.PromotionLocationService;
import co.ipicorp.saas.nrms.service.PromotionParticipantRetailerService;
import co.ipicorp.saas.nrms.service.PromotionProductGroupDetailService;
import co.ipicorp.saas.nrms.service.PromotionProductGroupService;
import co.ipicorp.saas.nrms.service.PromotionRewardFormatService;
import co.ipicorp.saas.nrms.service.PromotionService;
import co.ipicorp.saas.nrms.service.PromotionStateChangeHistoryService;
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.web.config.persistence.CustomerContext;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.portalapi.dto.PromotionDto;
import co.ipicorp.saas.portalapi.form.PromotionCreationForm;
import co.ipicorp.saas.portalapi.form.PromotionCreationForm.Limitation;
import co.ipicorp.saas.portalapi.form.PromotionCreationForm.LimitationItem;
import co.ipicorp.saas.portalapi.form.PromotionCreationForm.ProductGroup;
import co.ipicorp.saas.portalapi.form.PromotionCreationForm.ProductGroupDetail;
import co.ipicorp.saas.portalapi.form.PromotionCreationForm.RewardProduct;
import co.ipicorp.saas.portalapi.form.PromotionEditionForm;
import co.ipicorp.saas.portalapi.form.search.PromotionSearchForm;
import co.ipicorp.saas.portalapi.form.validator.PromotionCodeDuplicationValidator;
import co.ipicorp.saas.portalapi.form.validator.PromotionSearchValidator;
import co.ipicorp.saas.portalapi.form.validator.PromotionStructureValidator;
import co.ipicorp.saas.portalapi.util.ControllerAction;
import co.ipicorp.saas.portalapi.util.DtoFetchingUtils;
import co.ipicorp.saas.portalapi.util.ErrorCode;
import grass.micro.apps.annotation.GetBody;
import grass.micro.apps.annotation.Validation;
import grass.micro.apps.model.base.Status;
import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.component.ErrorsKeyConverter;
import grass.micro.apps.web.controller.support.AppControllerCreationSupport;
import grass.micro.apps.web.controller.support.AppControllerListingSupport;
import grass.micro.apps.web.controller.support.AppControllerSupport;
import grass.micro.apps.web.util.RequestUtils;
import io.swagger.annotations.ApiOperation;

/**
 * PromotionController. <<< Detail note.
 * 
 * @author nt.duong
 * @access public
 */
@RestController
@SuppressWarnings("unchecked")
public class PromotionController {

	private static final String PROMOTION_PRODUCT_GROUP_NAME_PREFIX = "Nhóm ";
	private static final String PROMOTION_DEFAULT_BANNER = "promotion_default.png";

	private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	private static final DateTimeFormatter formatterFull = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

	private Logger logger = Logger.getLogger(PromotionController.class);

	@Autowired
	private ErrorsKeyConverter errorsProcessor;

	@Autowired
	private PromotionService promotionService;

	@Autowired
	private RegionService regionService;

	@Autowired
	private CityService cityService;

	@Autowired
	private PromotionLocationService promotionLocationService;

	@Autowired
	private PromotionParticipantRetailerService pprService;

	@Autowired
	private PromotionLimitationService promotionLimitationService;

	@Autowired
	private PromotionLimitationItemService pliService;

	@Autowired
	private ProductCategoryService productCategoryService;

	@Autowired
	private PromotionRewardFormatService promotionRewardFormatService;

	@Autowired
	private PromotionConditionFormatService promotionConditionFormatService;

	@Autowired
	private PromotionProductGroupDetailService promotionProductGroupDetailService;

	@Autowired
	private PromotionProductGroupService promotionProductGroupService;

	@SuppressWarnings("unused")
	@Autowired
	private ProductVariationService productVariationService;

	@Autowired
	private PromotionLimitationItemRewardProductService promotionLimitationItemRewardProductService;

	@Autowired
	private PromotionStateChangeHistoryService promotionStateChangeHistoryService;

	@Autowired
	private FileStorageService fileStorageService;

	@Autowired
	private RetailerService retailerService;

	@Autowired
	private OrderSellinPromotionService osipService;

	@Autowired
	private OrderSelloutPromotionService osopService;

	@GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> listAll(HttpServletRequest request, HttpServletResponse response) {
		AppControllerSupport support = new AppControllerListingSupport() {

			@Override
			public List<? extends Serializable> getEntityList(HttpServletRequest request, HttpServletResponse response,
					Errors errors, ErrorsKeyConverter errorsProcessor) {
				return promotionService.getAllActivated();
			}

			@Override
			public String getAttributeName() {
				return "promotions";
			}

			@Override
			public List<?> fetchEntitiesToDtos(List<? extends Serializable> entities) {
				return DtoFetchingUtils.fetchPromotions((List<Promotion>) entities);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_SEARCH_ACTION)
	@Validation(validators = PromotionSearchValidator.class)
	@RequiresAuthentication
	public ResponseEntity<?> search(HttpServletRequest request, HttpServletResponse response,
			@GetBody PromotionSearchForm form) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				PromotionSearchCondition condition = new PromotionSearchCondition();
				String props = "segment,offset,fromDate,toDate,state,keyword";
				SystemUtils.getInstance().copyProperties(form, condition, props.split(","));
				condition.setLimitSearch(true);
				if (form.getFromDate() != null && form.getToDate() != null) {
					condition.setEnableCreatedDate(true);
				} else {
					condition.setEnableCreatedDate(false);
				}

				long total = promotionService.count(condition);
				getRpcResponse().addAttribute("count", total);
				List<PromotionDto> dtos = new LinkedList<>();
				if (total > form.getSegment()) {
					List<Promotion> promotions = promotionService.search(condition);
					dtos = (List<PromotionDto>) DtoFetchingUtils.fetchPromotions(promotions);
				}

				getRpcResponse().addAttribute("promotions", dtos);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_CHECK_DUPLICATE_CODE_ACTION)
	@RequiresAuthentication
	public ResponseEntity<?> checkDuplicatePromotionCode(HttpServletRequest request, HttpServletResponse response,
			@RequestParam("code") String promotionCode) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Promotion promotion = promotionService.getByPromotionCode(promotionCode);
				if (promotion != null) {
					errors.reject(ErrorCode.APP_2111_PROMOTION_CODE_WAS_USED, new Object[] { promotionCode },
							"Promotion Code [{1}] was used.");
				} else {
					getRpcResponse().addAttribute("message", "Code is available");
				}
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_DETAIL_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> getPromotionDetail(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer id) {
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Promotion promotion = PromotionController.this.promotionService.getActivated(id);
				if (promotion != null) {
					initRelatedServices();
					PromotionDto dto = DtoFetchingUtils.fetchPromotionWithFullInformation(promotion);
					getRpcResponse().addAttribute("promotion", dto);
				} else {
					errors.reject("404", "Not found");
				}
			}
		};
		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	/**
	 * Step 1: Create promotion object from generic information. Step 2: Create
	 * promotion location list Step 3: Create This operation need transaction. Any
	 * step failure will be rollback all operation above step. Promotion in step 1
	 * need to deleted.
	 * 
	 * @param request
	 * @param response
	 * @param form
	 * @param errors
	 * @return
	 */
	@PostMapping(value = ControllerAction.APP_PORTAL_PROMOTION_ACTION)
	@ResponseBody
	@RequiresAuthentication
	@ApiOperation(value = "Create promotion", notes = "This method creates a new promotion")
	@Validation(validators = { PromotionStructureValidator.class, PromotionCodeDuplicationValidator.class })
	public ResponseEntity<?> createPromotion(HttpServletRequest request, HttpServletResponse response,
			@RequestBody PromotionCreationForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerCreationSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Promotion promotion = createPromotion(form);

				try {
					promotion = promotionService.create(promotion);
					createPromotionLocation(promotion, form);
					createPromotionParticipant(promotion, form);
					List<PromotionProductGroup> groups = new ArrayList<>();
					if (promotion.getConditionFormatId() != Promotion.CONDITION_FORMAT_ORDER_VALUE) {
						groups = createPromotionGroup(promotion, form);
					}

					createPromotionLimitation(promotion, form, groups);

					getRpcResponse().addAttribute("promotionId", promotion.getId());
				} catch (Exception ex) {
					errors.reject(ErrorCode.APP_1000_SYSTEM_ERROR);
				}
			}
		};

		return support.doSupport(request, response, errors, errorsProcessor);
	}

	protected void createPromotionLimitation(Promotion promotion, PromotionCreationForm form,
			List<PromotionProductGroup> groups) {
		List<Limitation> limitations = form.getLimitations();
		if (CollectionUtils.isNotEmpty(limitations)) {
			List<PromotionLimitation> pLimitations = new LinkedList<>();

			int idx = 0;
			for (Limitation limitation : limitations) {
				idx++;
				PromotionLimitation pLimitation = new PromotionLimitation();
				pLimitation.setPromotionId(promotion.getId());
				pLimitation.setOrderNumber(idx);
				pLimitation.setRewardFormatId(form.getRewardFormatId());
				pLimitation.setStatus(Status.ACTIVE);
				pLimitation = this.promotionLimitationService.create(pLimitation);
				pLimitations.add(pLimitation);

				this.createPromotionLimitationDetail(promotion, pLimitation, limitation.getItems(), groups);
			}
		}
	}

	private void createPromotionLimitationDetail(Promotion promotion, PromotionLimitation limitation,
			List<LimitationItem> items, List<PromotionProductGroup> groups) {
		if (CollectionUtils.isEmpty(items)) {
			return;
		}

		int index = 0;
		System.err.println("GROUP SIZE: " + groups.size());
		for (LimitationItem item : items) {
			System.err.println("INDEX: " + index);
			Integer groupId = groups.size() - 1 >= index ? groups.get(index).getId() : null;
			;
			PromotionLimitationItem pItem = new PromotionLimitationItem();
			pItem.setPromotionId(limitation.getPromotionId());
			pItem.setLimitationId(limitation.getId());
			pItem.setProductGroupId(groupId);
			pItem.setLimitationOrderNumber(++index);

			pItem.setConditionFixValue(item.getConditionFixValue());
			pItem.setConditionRangeFrom(item.getConditionRangeFrom());
			pItem.setConditionRangeTo(item.getConditionRangeTo());

			pItem.setRewardFormatId(item.getRewardFormatId());
			pItem.setRewardPercent(item.getRewardPercent());
			pItem.setRewardValue(item.getRewardValue());

			pItem.setStatus(Status.ACTIVE);
			pItem = pliService.create(pItem);

			if (promotion.getRewardFormatId() == Promotion.REWARD_PRODUCT) {
				this.createPromotionLimitationDetailReward(promotion, limitation, pItem, item.getRewards());
			}
		}
	}

	private void createPromotionLimitationDetailReward(Promotion promotion, PromotionLimitation pLimitation,
			PromotionLimitationItem pItem, List<RewardProduct> rewards) {
		if (CollectionUtils.isEmpty(rewards)) {
			return;
		}

		for (RewardProduct reward : rewards) {
			PromotionLimitationItemRewardProduct pReward = new PromotionLimitationItemRewardProduct();
			pReward.setPromotionId(promotion.getId());
			pReward.setLimitationId(pItem.getLimitationId());
			pReward.setLimitationItemId(pItem.getId());
			pReward.setType(reward.getType() == null && reward.getType() >= 0 ? reward.getType()
					: PromotionLimitationItemRewardProduct.TYPE_AND);
			pReward.setUnitAmount(reward.getUnitAmount());
			pReward.setPackingAmount(
					reward.getPackingAmount() != null && reward.getPackingAmount() > 0 ? reward.getPackingAmount() : 0);
			pReward.setAmount(0);
			pReward.setUnitId(0);
			pReward.setPackingId(0);
			pReward.setPackingExchangeRatio(0);
			pReward.setProductId(-1);
			pReward.setProductVariationId(reward.getProductVariationId());
			pReward.setStatus(Status.ACTIVE);
			pReward.setProductVariationName("Fulfill later");
			pReward.setSku("Fulfill later");
			pReward = this.promotionLimitationItemRewardProductService.create(pReward);
		}

		try {
			this.promotionLimitationItemRewardProductService.updateProductVariationInfo(promotion.getId());
		} catch (Exception ex) {
			ex.printStackTrace(System.err);
		}

	}

	protected List<PromotionProductGroup> createPromotionGroup(Promotion promotion, PromotionCreationForm form) {
		List<ProductGroup> groups = form.getGroups();
		if (CollectionUtils.isNotEmpty(groups)) {
			List<PromotionProductGroup> ppGroups = new LinkedList<>();
			int idx = 1;
			for (ProductGroup group : groups) {
				PromotionProductGroup ppGroup = new PromotionProductGroup();
				ppGroup.setPromotionId(promotion.getId());
				ppGroup.setName(PROMOTION_PRODUCT_GROUP_NAME_PREFIX + idx++);
				ppGroup.setStatus(Status.ACTIVE);
				ppGroup = this.promotionProductGroupService.create(ppGroup);
				ppGroups.add(ppGroup);

				this.createPromotionGroupDetail(promotion, ppGroup, group.getProducts());
			}

			return ppGroups;
		}

		return new LinkedList<>();
	}

	private List<PromotionProductGroupDetail> createPromotionGroupDetail(Promotion promotion,
			PromotionProductGroup ppGroup, List<ProductGroupDetail> products) {
		if (CollectionUtils.isNotEmpty(products)) {
			List<PromotionProductGroupDetail> details = new LinkedList<>();

			for (ProductGroupDetail product : products) {
				PromotionProductGroupDetail detail = new PromotionProductGroupDetail();
				detail.setPromotionId(promotion.getId());
				detail.setGroupId(ppGroup.getId());
				detail.setGroupName(ppGroup.getName());
				detail.setCategoryIdLv0(product.getNodeLv0Id());
				detail.setCategoryIdLv1(product.getNodeLv1Id());
				detail.setCategoryIdLv2(product.getNodeLv2Id());
				
				int level = product.getNodeLv2Id() > 0 ? 2 : (product.getNodeLv1Id() > 0 ? 1 : 0);
				int cateogryId = product.getNodeLv2Id() > 0 ? product.getNodeLv2Id()
                        : (product.getNodeLv1Id() > 0 ? product.getNodeLv1Id() : product.getNodeLv0Id());
                
				detail.setCategoryLevel(level);
				detail.setCategoryId(cateogryId);
				detail.setProductId(product.getProductId());
				detail.setProductVariationId(product.getProductVariationId());
				detail.setProductVariationName(product.getName());
				detail.setSku(product.getSku());
				detail.setDescription(product.getDescription());
				detail.setStatus(Status.ACTIVE);
				detail = this.promotionProductGroupDetailService.create(detail);
				details.add(detail);
			}

			return details;
		}

		return null;
	}

	protected void createPromotionParticipant(Promotion promotion, PromotionCreationForm form) {
		List<Integer> retailerIds = form.getRetailers();
		if (CollectionUtils.isNotEmpty(retailerIds)) {
			List<PromotionParticipantRetailer> participants = new LinkedList<>();
			for (Integer retailerId : retailerIds) {
				PromotionParticipantRetailer participant = new PromotionParticipantRetailer();
				participant.setPromotionId(promotion.getId());
				participant.setRetailerId(retailerId);
				participant.setStatus(Status.ACTIVE);
				participant.setParticipantType(PromotionParticipantRetailer.INCLUDE);
				participant = this.pprService.create(participant);
				participants.add(participant);
			}
		}
	}

	protected void createPromotionLocation(Promotion promotion, PromotionCreationForm form) {
		List<Integer> regions = form.getRegions();
		if (CollectionUtils.isNotEmpty(regions)) {
			List<PromotionLocation> locations = new LinkedList<>();
			for (Integer regionId : regions) {
				PromotionLocation location = new PromotionLocation();
				Region region = this.regionService.get(regionId);
				location.setPromotionId(promotion.getId());
				location.setName(region.getName());
				location.setRegionId(regionId);
				location.setLocationType(PromotionLocation.LOCATION_TYPE_REGION);
				location.setCityId(null);
				location.setDistrictId(null);
				location.setWardId(null);
				location.setStatus(Status.ACTIVE);
				logger.info("PROMOTION LOCATION: " + location);
				location = this.promotionLocationService.create(location);
				locations.add(location);
				logger.info("ADD LOCATION SUCCESS");
			}
		}

		List<Integer> cities = form.getCities();
		if (CollectionUtils.isNotEmpty(cities)) {
			List<PromotionLocation> locations = new LinkedList<>();
			for (Integer cityId : cities) {
				City city = this.cityService.get(cityId);
				PromotionLocation location = new PromotionLocation();
				location.setPromotionId(promotion.getId());
				location.setName(city.getName());
				location.setLocationType(PromotionLocation.LOCATION_TYPE_CITY);
				location.setCityId(cityId);
				location.setRegionId(null);
				location.setDistrictId(null);
				location.setWardId(null);
				location.setStatus(Status.ACTIVE);
				logger.info("PROMOTION LOCATION: " + location);
				location = this.promotionLocationService.create(location);
				locations.add(location);
				logger.info("ADD LOCATION SUCCESS");
			}
		}
	}

	protected Promotion createPromotion(PromotionCreationForm form) {
		Promotion promotion = new Promotion();
		promotion.setPromotionCode(form.getPromotionCode());
		promotion.setPromotionType(form.getPromotionType());
		promotion.setPriority(form.getPriority());
		promotion.setName(form.getName());
		promotion.setContent(form.getContent());
		promotion.setSubjectType(SubjectType.fromValue(form.getSubjectType()));
		promotion.setPreparationDate(
				form.getPreparationDate() != null ? form.getPreparationDate().atStartOfDay() : null);
		promotion.setStartDate(form.getStartDate());
		promotion.setEndDate(form.getEndDate());

		promotion.setConditionFormatId(form.getConditionFormatId());
		promotion.setRewardFormatId(form.getRewardFormatId());
		promotion.setConditionComparitionType(form.getConditionComparitionType());
		promotion.setLimitationClaimType(
				form.getRewardFormatId() == Promotion.REWARD_PRODUCT ? Promotion.LIMITATION_CLAIM_TYPE_ALL
						: Promotion.LIMITATION_CLAIM_TYPE_FIRST);

		promotion.setPromotionState(PromotionState.NEW.toString());
		promotion.setDisplay(form.getDisplay());
		promotion.setBanner(PROMOTION_DEFAULT_BANNER);
		promotion.setExtraData(form.getExtraData());
		promotion.setStatus(Status.INACTIVE);

		return promotion;
	}

	protected void initRelatedServices() {
		DtoFetchingUtils.setRetailerService(retailerService);
		DtoFetchingUtils.setPromotionProductGroupDetailService(promotionProductGroupDetailService);
		DtoFetchingUtils.setProductCategoryService(productCategoryService);
		DtoFetchingUtils.setPromotionLocationService(promotionLocationService);
		DtoFetchingUtils.setPromotionRewardFormatService(promotionRewardFormatService);
		DtoFetchingUtils.setPromotionConditionFormatService(promotionConditionFormatService);
		DtoFetchingUtils.setPromotionLimitationService(promotionLimitationService);
		DtoFetchingUtils.setPromotionLimitationItemService(pliService);
		DtoFetchingUtils.setPromotionProductGroupService(promotionProductGroupService);
		DtoFetchingUtils.setPromotionLimitationItemRewardProductService(promotionLimitationItemRewardProductService);
		DtoFetchingUtils.setPromotionParticipantRetailerService(pprService);
	}

	/**
	 * @param promotionId
	 * @param promotion
	 * @return
	 */
	private PromotionStateChangeHistory createHistory(PromotionEditionForm form, Promotion promotion) {
		PromotionStateChangeHistory history = new PromotionStateChangeHistory();
		history.setPromotionId(promotion.getId());
		history.setChangeDate(LocalDateTime.now());
		history.setFromState(promotion.getPromotionState());
		history.setToState(promotion.getPromotionState());
		LinkedHashMap<String, Object> extraData = new LinkedHashMap<String, Object>();
		if (Promotion.PROMOTION_TYPE_CTKM == promotion.getPromotionType()
				&& SubjectType.CONSUMER.getValue() == promotion.getSubjectType().getValue()) {
			extraData.put(PromotionStateChangeHistory.EXTRA_KEY_OLD_PREPARATION_DATE,
					formatterFull.format(promotion.getPreparationDate()));
			extraData.put(PromotionStateChangeHistory.EXTRA_KEY_NEW_PREPARATION_DATE,
					formatterFull.format(promotion.getPreparationDate()));
		}

		extraData.put(PromotionStateChangeHistory.EXTRA_KEY_OLD_START_DATE, formatter.format(promotion.getStartDate()));
		extraData.put(PromotionStateChangeHistory.EXTRA_KEY_NEW_START_DATE, formatter.format(promotion.getStartDate()));

		extraData.put(PromotionStateChangeHistory.EXTRA_KEY_OLD_END_DATE, formatter.format(promotion.getEndDate()));
		extraData.put(PromotionStateChangeHistory.EXTRA_KEY_NEW_END_DATE, formatter.format(promotion.getEndDate()));

		extraData.put(PromotionStateChangeHistory.EXTRA_KEY_NOTE, form.getNote());

		history.setExtraData(extraData);
		return history;
	}

	@PostMapping(value = ControllerAction.APP_PORTAL_PROMOTION_BANNER_ACTION, consumes = "multipart/form-data", produces = {
			"application/json" })
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> updateBanner(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer promotionId, @RequestParam("files[]") MultipartFile[] files) {
		logger.info("HAS FILE UPLOADS: " + (files != null && files.length > 0));
		AppControllerSupport support = new AppControllerSupport() {

			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Map<String, MultipartFile> fileNameMap = new LinkedHashMap<String, MultipartFile>();
				String fileName = files[0].getOriginalFilename();
				String extension = FilenameUtils.getExtension(fileName);
				fileName = SystemUtils.getInstance().generateCode("promo", promotionId, 8) + "." + extension;
				fileNameMap.put(fileName, files[0]);

				// TODO: replace 1
				String location = ResourceUrlResolver.getInstance().resolveFtpPromotionPath(CustomerContext.getCustomerId(), "");
				fileStorageService.storeMultipleFiles(fileNameMap, location);

				Promotion promotion = promotionService.get(promotionId);
				promotion.setBanner(fileName);
				promotionService.updatePartial(promotion);

				List<String> filePaths = new ArrayList<>();
				filePaths.addAll(fileNameMap.keySet());
				getRpcResponse().addAttribute("files", filePaths);
			}

		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@PostMapping(value = ControllerAction.APP_PORTAL_PROMOTION_CHANGE_INFO_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> updatePromotionInfo(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer promotionId, @RequestBody PromotionEditionForm form, BindingResult errors) {
		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Promotion promotion = promotionService.get(promotionId);
				if (promotion == null) {
					errors.reject(ErrorCode.APP_2301_PROMOTION_NOT_FOUND, new Object[] { promotionId },
							"Promotion [{0}] was not found");
					return;
				}

				List<PromotionState> allowedRunningState = Arrays.asList(PromotionState.RUNNING, PromotionState.PAUSE);
				List<PromotionState> allowedPreparationState = Arrays.asList(PromotionState.APPROVED,
						PromotionState.NEW, PromotionState.PENDING);

				if (!allowedRunningState.contains(PromotionState.valueOf(promotion.getPromotionState()))
						&& !allowedPreparationState.contains(PromotionState.valueOf(promotion.getPromotionState()))) {
					errors.reject(ErrorCode.APP_2201_PROMOTION_STATE_INVALID, "promotion invalid state to edit info");
					return;
				}

				PromotionStateChangeHistory history = createHistory(form, promotion);

				if (allowedRunningState.contains(PromotionState.valueOf(promotion.getPromotionState()))) {
					processRunning(promotion, history, form, errors);
				}

				if (allowedPreparationState.contains(PromotionState.valueOf(promotion.getPromotionState()))) {
					processPreparation(promotion, history, form, errors);
				}

				if (!errors.hasErrors()) {
					promotionStateChangeHistoryService.create(history);
				}

				getRpcResponse().addAttribute("message", "SUCCESS");
			}

			private void processCities(List<PromotionLocation> locations, PromotionStateChangeHistory history) {
				List<Integer> cities = form.getCities();

				List<Integer> currentCities = locations.stream()
						.filter(pl -> pl.getLocationType() == PromotionLocation.LOCATION_TYPE_CITY)
						.map(pl -> pl.getCityId()).collect(Collectors.toList());

				List<Integer> expansion = new ArrayList<Integer>();
				expansion.addAll(cities);
				expansion.removeAll(currentCities);

				List<Integer> reduction = new ArrayList<Integer>();
				reduction.addAll(currentCities);
				reduction.removeAll(cities);

				if (!reduction.isEmpty()) {
					history.getExtraData().put(PromotionStateChangeHistory.EXTRA_KEY_REDUCTION_CITIES, reduction);
					List<PromotionLocation> deleteLocations = locations.stream()
							.filter(location -> reduction.contains(location.getCityId())).collect(Collectors.toList());
					promotionLocationService.deleteAll(deleteLocations);
				}

				if (!expansion.isEmpty()) {
					history.getExtraData().put(PromotionStateChangeHistory.EXTRA_KEY_EXPANSION_CITIES, expansion);
					for (Integer cityId : expansion) {
						City city = cityService.get(cityId);
						PromotionLocation location = new PromotionLocation();
						location.setPromotionId(history.getPromotionId());
						location.setName(city.getName());
						location.setLocationType(PromotionLocation.LOCATION_TYPE_CITY);
						location.setCityId(cityId);
						location.setRegionId(null);
						location.setDistrictId(null);
						location.setWardId(null);
						location.setStatus(Status.ACTIVE);
						logger.info("PROMOTION LOCATION: " + location);
						location = promotionLocationService.create(location);
						locations.add(location);
						logger.info("ADD LOCATION (CITY) SUCCESS");
					}
				}
			}

			private void processRegions(List<PromotionLocation> locations, PromotionStateChangeHistory history) {
				List<Integer> regions = new ArrayList<Integer>();
				List<Integer> currentRegions = locations.stream()
						.filter(pl -> pl.getLocationType() == PromotionLocation.LOCATION_TYPE_REGION)
						.map(pl -> pl.getRegionId()).collect(Collectors.toList());

				List<Integer> expansion = new ArrayList<Integer>();
				expansion.addAll(regions);
				expansion.removeAll(currentRegions);

				List<Integer> reduction = new ArrayList<Integer>();
				reduction.addAll(currentRegions);
				reduction.removeAll(regions);

				if (!reduction.isEmpty()) {
					history.getExtraData().put(PromotionStateChangeHistory.EXTRA_KEY_EXPANSION_REGIONS, reduction);
					List<PromotionLocation> deleteLocations = locations.stream()
							.filter(location -> reduction.contains(location.getRegionId()))
							.collect(Collectors.toList());
					promotionLocationService.deleteAll(deleteLocations);
				}

				if (!expansion.isEmpty()) {
					history.getExtraData().put(PromotionStateChangeHistory.EXTRA_KEY_EXPANSION_REGIONS, expansion);
					for (Integer regionId : expansion) {
						PromotionLocation location = new PromotionLocation();
						Region region = regionService.get(regionId);
						location.setPromotionId(history.getPromotionId());
						location.setName(region.getName());
						location.setRegionId(regionId);
						location.setLocationType(PromotionLocation.LOCATION_TYPE_REGION);
						location.setCityId(null);
						location.setDistrictId(null);
						location.setWardId(null);
						location.setStatus(Status.ACTIVE);
						logger.info("PROMOTION LOCATION: " + location);
						location = promotionLocationService.create(location);
						locations.add(location);
						logger.info("ADD LOCATION (REGION) SUCCESS");
					}
				}
			}

			private void updateLocation(Promotion promotion, PromotionStateChangeHistory history,
					PromotionEditionForm form, Errors errors) {
				List<PromotionLocation> locations = promotionLocationService.getByPromotionId(promotionId);
				this.processCities(locations, history);
				this.processRegions(locations, history);
			}

			private void updateParticipant(Promotion promotion, PromotionStateChangeHistory history,
					PromotionEditionForm form, Errors errors) {
				List<PromotionParticipantRetailer> pRetailers = pprService.getByPromotionId(promotion.getId());
				List<Integer> retailers = form.getRetailers();

				List<Integer> currentRetailers = pRetailers.stream()
						.filter(pp -> pp.getParticipantType() == PromotionParticipantRetailer.INCLUDE)
						.map(pl -> pl.getRetailerId()).collect(Collectors.toList());

				List<Integer> expansion = new ArrayList<Integer>();
				expansion.addAll(retailers);
				expansion.removeAll(currentRetailers);

				List<Integer> reduction = new ArrayList<Integer>();
				reduction.addAll(currentRetailers);
				reduction.removeAll(retailers);

				if (!reduction.isEmpty()) {
					history.getExtraData().put(PromotionStateChangeHistory.EXTRA_KEY_REDUCTION_RETAILER, reduction);
					List<PromotionParticipantRetailer> deleteParticipants = pRetailers.stream()
							.filter(location -> reduction.contains(location.getRetailerId()))
							.collect(Collectors.toList());
					pprService.deleteAll(deleteParticipants);
				}

				if (!expansion.isEmpty()) {
					history.getExtraData().put(PromotionStateChangeHistory.EXTRA_KEY_EXPANSION_CITIES, expansion);
					for (Integer retailerId : expansion) {
						PromotionParticipantRetailer participant = new PromotionParticipantRetailer();
						participant.setPromotionId(promotion.getId());
						participant.setRetailerId(retailerId);
						participant.setStatus(Status.ACTIVE);
						participant.setParticipantType(PromotionParticipantRetailer.INCLUDE);
						participant = pprService.create(participant);
					}
				}
			}

			private void processRunning(Promotion promotion, PromotionStateChangeHistory history,
					PromotionEditionForm form, Errors errors) {
				if (LocalDate.now().isAfter(form.getEndDate())) {
					errors.reject(ErrorCode.APP_2202_PROMOTION_NEW_ENDDATE_INVALID,
							"New End Date must be greater than current date");
				}

				if (!errors.hasErrors()) {
					promotion.setContent(form.getContent());
					promotion.setEndDate(form.getEndDate());
					promotionService.updatePartial(promotion);
					history.setChangeDate(LocalDateTime.now());
					history.getExtraData().put(PromotionStateChangeHistory.EXTRA_KEY_NEW_END_DATE,
							formatter.format(form.getEndDate()));
				}
			}

			private void processPreparation(Promotion promotion, PromotionStateChangeHistory history,
					PromotionEditionForm form, Errors errors) {
				Integer promotionType = promotion.getPromotionType();
				Integer subjectType = promotion.getSubjectType().getValue();

				if (form.getStartDate() == null || form.getEndDate() == null) {
					errors.reject(ErrorCode.APP_2106_DATE_IS_REQUIRED, new Object[] { subjectType },
							"Both startDate and endDate are required.");
				} else if (form.getStartDate().isBefore(LocalDate.now())) {
					errors.reject(ErrorCode.APP_2203_STARTDATE_MUSTBE_AFTER_CURRENT_DATE, new Object[] { subjectType },
							"startDate must be after current Date.");
				} else if (form.getStartDate().isAfter(form.getEndDate())) {
					errors.reject(ErrorCode.APP_2107_STARTDATE_MUSTBE_BEFORE_ENDDATE, new Object[] { subjectType },
							"startDate must be before or equal endDate.");
				} else {
					if (Promotion.PROMOTION_TYPE_CTKM == promotionType
							&& SubjectType.CONSUMER.getValue() == subjectType) {
						if (form.getPreparationDate() == null) {
							errors.reject(ErrorCode.APP_2108_PREPARATION_DATE_IS_REQUIRED, new Object[] { subjectType },
									"CTKM for End-User must have Preparation Date.");
						} else if (promotion.getPreparationDate().isAfter(LocalDateTime.now())
								&& form.getPreparationDate().isBefore(LocalDate.now())) {
							errors.reject(ErrorCode.APP_2204_PREPARATION_DATE_MUSTBE_AFTER_CURRENT_DATE,
									new Object[] {}, "Preparation must be after current Date.");
						} else if (form.getPreparationDate().isAfter(form.getStartDate())) {
							errors.reject(ErrorCode.APP_2205_PREPARATION_DATE_MUSTBE_AFTER_START_DATE,
									new Object[] { subjectType }, "preparationDate must be before or equal startDate.");
						}
					}
				}

				if (!errors.hasErrors()) {
					if (Promotion.PROMOTION_TYPE_CTKM == promotionType && SubjectType.CONSUMER.getValue() == subjectType) {
						if (promotion.getPromotionState().equals(PromotionState.NEW.toString())
								|| promotion.getPreparationDate().isAfter(LocalDateTime.now())) {
							promotion.setPreparationDate(form.getPreparationDate().atStartOfDay());
							history.getExtraData().put(PromotionStateChangeHistory.EXTRA_KEY_NEW_PREPARATION_DATE, formatterFull.format(promotion.getPreparationDate()));
						}
					}
					promotion.setStartDate(form.getStartDate());
					promotion.setEndDate(form.getEndDate());
					promotion.setContent(form.getContent());
					promotionService.updatePartial(promotion);

					history.getExtraData().put(PromotionStateChangeHistory.EXTRA_KEY_NEW_START_DATE,
							formatter.format(form.getStartDate()));
					history.getExtraData().put(PromotionStateChangeHistory.EXTRA_KEY_NEW_END_DATE,
							formatter.format(form.getEndDate()));
				}

				if (!errors.hasErrors()) {
					updateLocation(promotion, history, form, errors);
				}

				if (!errors.hasErrors()) {
					updateParticipant(promotion, history, form, errors);
				}
			}
		};

		return support.doSupport(request, response, errors, errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_SWITCH_STATUS_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> switchPromotionStatus(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer promotionId) {

		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Promotion promotion = promotionService.get(promotionId);
				if (promotion == null) {
					errors.reject(ErrorCode.APP_2301_PROMOTION_NOT_FOUND, new Object[] { promotionId },
							"Promotion [{0}] was not found");
					return;
				}

				List<PromotionState> runningStates = Arrays.asList(PromotionState.RUNNING, PromotionState.PAUSE);
				List<PromotionState> preparationStates = Arrays.asList(PromotionState.APPROVED, PromotionState.NEW,
						PromotionState.PENDING);

				if (!runningStates.contains(PromotionState.valueOf(promotion.getPromotionState()))
						&& !preparationStates.contains(PromotionState.valueOf(promotion.getPromotionState()))) {
					errors.reject(ErrorCode.APP_2302_PROMOTION_STATE_IS_NOT_VALID_TO_CHANGE,
							new Object[] { promotionId, promotion.getPromotionState() },
							"Promotion [{0}] state is {1}, it's not valid to change");
					return;
				}

				PromotionStateChangeHistory history = new PromotionStateChangeHistory();
				history.setPromotionId(promotionId);
				history.setChangeDate(LocalDateTime.now());
				history.setFromState(promotion.getPromotionState());
				LinkedHashMap<String, Object> extraData = new LinkedHashMap<String, Object>();
				history.setExtraData(extraData);

				PromotionState nextState = PromotionState.FINISH;
				if (runningStates.contains(PromotionState.valueOf(promotion.getPromotionState()))) {
					if (promotion.isActivated()) {
						nextState = PromotionState.PAUSE;
					} else {
						nextState = PromotionState.RUNNING;
					}
				} else {
					if (promotion.isActivated()) {
						nextState = PromotionState.PENDING;
					} else {
						nextState = PromotionState.APPROVED;
					}
				}

				history.setToState(nextState.toString());

				Status currentStatus = promotion.getStatus();
				Status nextStatus = Status.ACTIVE.equals(currentStatus) ? Status.INACTIVE : Status.ACTIVE;
				promotion.setStatus(nextStatus);
				promotion.setPromotionState(nextState.toString());

				promotionService.updatePartial(promotion);
				promotionStateChangeHistoryService.create(history);

				getRpcResponse().addAttribute("message", "SUCCESS");
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_FORCE_FINISH_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> forceEnd(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer promotionId) {

		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Promotion promotion = promotionService.get(promotionId);
				if (promotion == null) {
					errors.reject(ErrorCode.APP_2301_PROMOTION_NOT_FOUND, new Object[] { promotionId },
							"Promotion [{0}] was not found");
					return;
				}

				List<PromotionState> runningStates = Arrays.asList(PromotionState.RUNNING, PromotionState.PAUSE);
				List<PromotionState> preparationStates = Arrays.asList(PromotionState.APPROVED, PromotionState.NEW,
						PromotionState.PENDING);

				if (!runningStates.contains(PromotionState.valueOf(promotion.getPromotionState()))
						&& !preparationStates.contains(PromotionState.valueOf(promotion.getPromotionState()))) {
					errors.reject(ErrorCode.APP_2302_PROMOTION_STATE_IS_NOT_VALID_TO_CHANGE,
							new Object[] { promotionId, promotion.getPromotionState() },
							"Promotion [{0}] state is {1}, it's not valid to change");
					return;
				}

				PromotionStateChangeHistory history = new PromotionStateChangeHistory();
				history.setPromotionId(promotionId);
				history.setChangeDate(LocalDateTime.now());
				history.setFromState(promotion.getPromotionState());
				LinkedHashMap<String, Object> extraData = new LinkedHashMap<String, Object>();
				history.setExtraData(extraData);

				PromotionState nextState = PromotionState.FINISH;
				if (runningStates.contains(PromotionState.valueOf(promotion.getPromotionState()))) {
					nextState = PromotionState.FINISH;
				} else {
					nextState = PromotionState.CANCELED;
				}

				history.setToState(nextState.toString());
				promotion.setPromotionState(nextState.toString());
				promotion.setManualEndDate(LocalDateTime.now());
				promotionService.updatePartial(promotion);
				promotionStateChangeHistoryService.create(history);

				getRpcResponse().addAttribute("message", "SUCCESS");
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_HISTORY_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> getHistories(HttpServletRequest request, HttpServletResponse response,
			@PathVariable("id") Integer promotionId) {

		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				Promotion promotion = promotionService.get(promotionId);
				if (promotion == null) {
					errors.reject(ErrorCode.APP_2301_PROMOTION_NOT_FOUND, new Object[] { promotionId },
							"Promotion [{0}] was not found");
					return;
				}

				List<Map<String, Object>> histories = new LinkedList<Map<String, Object>>();
				List<PromotionStateChangeHistory> pHistories = promotionStateChangeHistoryService
						.getByPromotionId(promotionId);
				DateTimeFormatter df = DateTimeFormatter.ofPattern("dd/MM/yyyy - HH:mm");

				for (PromotionStateChangeHistory pHistory : pHistories) {
					Map<String, Object> history = new LinkedHashMap<String, Object>();
					history.put("date", df.format(pHistory.getChangeDate()));
					history.put("actor", pHistory.getCreatedUser());

					processDate(pHistory, history, PromotionStateChangeHistory.EXTRA_KEY_OLD_START_DATE);
					processDate(pHistory, history, PromotionStateChangeHistory.EXTRA_KEY_NEW_START_DATE);
					processDate(pHistory, history, PromotionStateChangeHistory.EXTRA_KEY_OLD_END_DATE);
					processDate(pHistory, history, PromotionStateChangeHistory.EXTRA_KEY_NEW_END_DATE);

					processDateTime(pHistory, history, PromotionStateChangeHistory.EXTRA_KEY_OLD_PREPARATION_DATE);
					processDateTime(pHistory, history, PromotionStateChangeHistory.EXTRA_KEY_NEW_PREPARATION_DATE);

					history.put("oldStatus", pHistory.getFromState());
					history.put("newStatus", pHistory.getToState());

					history.put("description",
							pHistory.getExtraData().getOrDefault(PromotionStateChangeHistory.EXTRA_KEY_NOTE, ""));

					LinkedHashMap<String, Object> extraData = pHistory.getExtraData();
					processOtherHistories(extraData, history);

					histories.add(history);
				}

				getRpcResponse().addAttribute("histories", histories);
			}

			private void processOtherHistories(LinkedHashMap<String, Object> extraData, Map<String, Object> history) {
				extraData.containsKey(PromotionStateChangeHistory.EXTRA_KEY_EXPANSION_CITIES);
			}

			/*
			 * @param pHistory
			 * 
			 * @param history
			 * 
			 * @param key
			 */

			private void processDate(PromotionStateChangeHistory pHistory, Map<String, Object> history, String key) {
				if (pHistory.getExtraData() == null) {
					return;
				}

				String oldStartDate = (String) pHistory.getExtraData().getOrDefault(key, "");
				if (StringUtils.isNotBlank(oldStartDate)) {
					oldStartDate = SystemUtils.getInstance().convertLocalDateFormat(oldStartDate, "yyyy-MM-dd",
							"dd/MM/yyyy");
				}

				history.put(key, oldStartDate);
			}

			private void processDateTime(PromotionStateChangeHistory pHistory, Map<String, Object> history,
					String key) {
				String oldStartDate = (String) pHistory.getExtraData().getOrDefault(key, "");
				if (StringUtils.isNotBlank(oldStartDate)) {
					oldStartDate = SystemUtils.getInstance().convertLocalDateTimeFormat(oldStartDate,
							"yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy");
				}

				history.put(key, oldStartDate);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}

	@GetMapping(value = ControllerAction.APP_PORTAL_PROMOTION_RUNNING_STATS_ACTION)
	@ResponseBody
	@RequiresAuthentication
	public ResponseEntity<?> getRunningStatistic(HttpServletRequest request, HttpServletResponse response) {

		AppControllerSupport support = new AppControllerSupport() {
			@Override
			public void process(HttpServletRequest request, HttpServletResponse response, Errors errors,
					ErrorsKeyConverter errorsProcessor) {
				initStats();
				List<Promotion> promotions = promotionService.getAllRunningPromotion();
				if (CollectionUtils.isNotEmpty(promotions)) {
					List<Promotion> retailerPromotions = promotions.stream()
							.filter(p -> SubjectType.RETAILER.equals(p.getSubjectType())).collect(Collectors.toList());
					List<Integer> retailerPromotionIds = retailerPromotions.stream().map(p -> p.getId())
							.collect(Collectors.toList());

					List<Promotion> consumerPromotions = promotions.stream()
							.filter(p -> SubjectType.CONSUMER.equals(p.getSubjectType())).collect(Collectors.toList());
					List<Integer> consumerPromotionIds = consumerPromotions.stream().map(p -> p.getId())
							.collect(Collectors.toList());

					List<OrderSellinPromotion> orderSellinPromotions = osipService
							.getByPromotionIdsAndOrderState(retailerPromotionIds, OrderSellinStatus.FINISH);
					List<OrderSelloutPromotion> orderSelloutPromotions = osopService
							.getByPromotionIdsAndOrderState(consumerPromotionIds, OrderSelloutStatus.FINISH);

					Double promotionRetailerTotalCost = orderSellinPromotions.stream().map(p -> p.getOrderCost())
							.reduce(0.0, (a, b) -> a + b);
					Double promotionRetailerDiscount = orderSellinPromotions.stream().map(p -> p.getDiscount())
							.reduce(0.0, (a, b) -> a + b);

					Double promotionConsumerTotalCost = orderSelloutPromotions.stream().map(p -> p.getOrderCost())
							.reduce(0.0, (a, b) -> a + b);
					Double promotionConsumerDiscount = orderSelloutPromotions.stream().map(p -> p.getDiscount())
							.reduce(0.0, (a, b) -> a + b);

					getRpcResponse().addAttribute("PROMOTION_CONSUMER_COUNT", consumerPromotions.size());
					getRpcResponse().addAttribute("PROMOTION_RETAILER_COUNT", retailerPromotions.size());
					getRpcResponse().addAttribute("PROMOTION_CONSUMER_DISCOUNT", promotionConsumerDiscount);
					getRpcResponse().addAttribute("PROMOTION_RETAILER_DISCOUNT", promotionRetailerDiscount);
					getRpcResponse().addAttribute("PROMOTION_CONSUMER_TOTAL_COST", promotionConsumerTotalCost);
					getRpcResponse().addAttribute("PROMOTION_RETAILER_TOTAL_COST", promotionRetailerTotalCost);
				}
			}

			private void initStats() {
				getRpcResponse().addAttribute("PROMOTION_CONSUMER_COUNT", 0);
				getRpcResponse().addAttribute("PROMOTION_RETAILER_COUNT", 0);
				getRpcResponse().addAttribute("PROMOTION_CONSUMER_DISCOUNT", 0);
				getRpcResponse().addAttribute("PROMOTION_RETAILER_DISCOUNT", 0);
				getRpcResponse().addAttribute("PROMOTION_CONSUMER_TOTAL_COST", 0);
				getRpcResponse().addAttribute("PROMOTION_RETAILER_TOTAL_COST", 0);
			}
		};

		return support.doSupport(request, response, RequestUtils.getInstance().getBindingResult(), errorsProcessor);
	}
}
