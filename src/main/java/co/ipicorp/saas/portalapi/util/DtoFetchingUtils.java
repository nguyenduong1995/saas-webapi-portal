/**
 * DtoFetchingUtils.java
 * @copyright  Copyright © 2020 Micro App
 * @author     ntduong
 * @package    co.ipicorp.saas.ms.identity.util
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.util;

import co.ipicorp.saas.core.model.Account;
import co.ipicorp.saas.core.model.City;
import co.ipicorp.saas.core.model.Company;
import co.ipicorp.saas.core.model.Customer;
import co.ipicorp.saas.core.model.District;
import co.ipicorp.saas.core.model.Industry;
import co.ipicorp.saas.core.model.Invoice;
import co.ipicorp.saas.core.model.Module;
import co.ipicorp.saas.core.model.Plan;
import co.ipicorp.saas.core.model.PriceUnit;
import co.ipicorp.saas.core.model.StaffOfCustomer;
import co.ipicorp.saas.core.model.User;
import co.ipicorp.saas.core.web.dto.AccountDto;
import co.ipicorp.saas.core.web.dto.CityDto;
import co.ipicorp.saas.core.web.dto.CompanyDto;
import co.ipicorp.saas.core.web.dto.CustomerDto;
import co.ipicorp.saas.core.web.dto.DistrictDto;
import co.ipicorp.saas.nrms.model.Product;
import co.ipicorp.saas.nrms.model.ProductVariation;
import co.ipicorp.saas.nrms.model.Promotion;
import co.ipicorp.saas.nrms.model.PromotionConditionFormat;
import co.ipicorp.saas.nrms.model.PromotionLimitation;
import co.ipicorp.saas.nrms.model.PromotionLimitationItem;
import co.ipicorp.saas.nrms.model.PromotionLimitationItemRewardProduct;
import co.ipicorp.saas.nrms.model.PromotionLocation;
import co.ipicorp.saas.nrms.model.PromotionParticipantRetailer;
import co.ipicorp.saas.nrms.model.PromotionProductGroup;
import co.ipicorp.saas.nrms.model.PromotionProductGroupDetail;
import co.ipicorp.saas.nrms.model.PromotionRewardFormat;
import co.ipicorp.saas.nrms.model.Retailer;
import co.ipicorp.saas.nrms.model.RetailerWarehouseExportTicket;
import co.ipicorp.saas.nrms.model.RetailerWarehouseImportTicket;
import co.ipicorp.saas.nrms.model.RetailerWarehouseItemHistory;
import co.ipicorp.saas.nrms.model.Unit;
import co.ipicorp.saas.nrms.model.Warehouse;
import co.ipicorp.saas.nrms.model.WarehouseDailyRemain;
import co.ipicorp.saas.nrms.model.WarehouseExportTicket;
import co.ipicorp.saas.nrms.model.WarehouseImportTicket;
import co.ipicorp.saas.nrms.model.WarehouseImportTicketItem;
import co.ipicorp.saas.nrms.model.WarehouseItemHistory;
import co.ipicorp.saas.nrms.model.WarehouseTotalItem;
import co.ipicorp.saas.nrms.model.dto.WarehouseImportTicketItemDto;
import co.ipicorp.saas.nrms.service.OrderSellinItemService;
import co.ipicorp.saas.nrms.service.OrderSellinService;
import co.ipicorp.saas.nrms.service.OrderSelloutItemService;
import co.ipicorp.saas.nrms.service.ProductCategoryService;
import co.ipicorp.saas.nrms.service.ProductService;
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
import co.ipicorp.saas.nrms.service.RetailerService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseExportTicketService;
import co.ipicorp.saas.nrms.service.RetailerWarehouseImportTicketService;
import co.ipicorp.saas.nrms.service.UnitService;
import co.ipicorp.saas.nrms.service.WarehouseExportTicketService;
import co.ipicorp.saas.nrms.service.WarehouseImportTicketItemService;
import co.ipicorp.saas.nrms.service.WarehouseImportTicketService;
import co.ipicorp.saas.nrms.service.WarehouseService;
import co.ipicorp.saas.nrms.web.config.persistence.CustomerContext;
import co.ipicorp.saas.nrms.web.dto.ProductDto;
import co.ipicorp.saas.nrms.web.util.ResourceUrlResolver;
import co.ipicorp.saas.portalapi.dto.IndustryDto;
import co.ipicorp.saas.portalapi.dto.InvoiceDto;
import co.ipicorp.saas.portalapi.dto.ModuleDto;
import co.ipicorp.saas.portalapi.dto.PlanDto;
import co.ipicorp.saas.portalapi.dto.PriceUnitDto;
import co.ipicorp.saas.portalapi.dto.PromotionConditionFormatDto;
import co.ipicorp.saas.portalapi.dto.PromotionDto;
import co.ipicorp.saas.portalapi.dto.PromotionLimitationDto;
import co.ipicorp.saas.portalapi.dto.PromotionLimitationItemDto;
import co.ipicorp.saas.portalapi.dto.PromotionLimitationItemRewardProductDto;
import co.ipicorp.saas.portalapi.dto.PromotionLocationDetailDto;
import co.ipicorp.saas.portalapi.dto.PromotionLocationDto;
import co.ipicorp.saas.portalapi.dto.PromotionParticipantRetailerDto;
import co.ipicorp.saas.portalapi.dto.PromotionProductGroupDetailDto;
import co.ipicorp.saas.portalapi.dto.PromotionProductGroupDto;
import co.ipicorp.saas.portalapi.dto.PromotionRewardFormatDto;
import co.ipicorp.saas.portalapi.dto.RetailerWarehouseExportItemHistoryDto;
import co.ipicorp.saas.portalapi.dto.RetailerWarehouseImportItemHistoryDto;
import co.ipicorp.saas.portalapi.dto.StaffOfCustomerDto;
import co.ipicorp.saas.portalapi.dto.UserDto;
import co.ipicorp.saas.portalapi.dto.WarehouseDailyRemainDto;
import co.ipicorp.saas.portalapi.dto.WarehouseExportItemHistoryDto;
import co.ipicorp.saas.portalapi.dto.WarehouseImportItemHistoryDto;
import co.ipicorp.saas.portalapi.dto.WarehouseImportTicketDto;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import grass.micro.apps.util.SystemUtils;
import grass.micro.apps.web.util.CommonFetchingUtils;

/**
 * DtoFetchingUtils. <<< Detail note.
 * 
 * @author ntduong
 * @access public
 */
public class DtoFetchingUtils {

	private static OrderSellinService orderSellinService;

	private static OrderSellinItemService orderSellinItemService;

	private static OrderSelloutItemService orderSelloutItemService;

	private static WarehouseImportTicketItemService ticketItemService;

	private static WarehouseImportTicketService importTicketService;

	private static WarehouseExportTicketService exportTicketService;

	private static ProductCategoryService productCategoryService;

	private static ProductService productService;

	private static ProductVariationService productVariationService;

	private static UnitService unitService;

	private static WarehouseService warehouseService;

	private static RetailerWarehouseImportTicketService rwhImportTicketService;

	private static RetailerWarehouseExportTicketService rwhExportTicketService;

	private static RetailerService retailerService;

	private static PromotionProductGroupDetailService promotionProductGroupDetailService;

	private static PromotionParticipantRetailerService promotionParticipantRetailerService;

	private static PromotionLocationService promotionLocationService;

	private static PromotionRewardFormatService promotionRewardFormatService;

	private static PromotionConditionFormatService promotionConditionFormatService;

	private static PromotionLimitationService promotionLimitationService;

	private static PromotionLimitationItemService promotionLimitationItemService;

	private static PromotionProductGroupService promotionProductGroupService;

	private static PromotionLimitationItemRewardProductService promotionLimitationItemRewardProductService;

	private DtoFetchingUtils() {
	}

	/**
	 * get value of <b>orderSellinService</b>.
	 * 
	 * @return the orderSellinService
	 */
	public static OrderSellinService getOrderSellinService() {
		return orderSellinService;
	}

	/**
	 * Set value to <b>orderSellinService</b>.
	 * 
	 * @param orderSellinService the orderSellinService to set
	 */
	public static void setOrderSellinService(OrderSellinService orderSellinService) {
		DtoFetchingUtils.orderSellinService = orderSellinService;
	}

	/**
	 * get value of <b>orderSellinItemService</b>.
	 * 
	 * @return the orderSellinItemService
	 */
	public static OrderSellinItemService getOrderSellinItemService() {
		return orderSellinItemService;
	}

	/**
	 * Set value to <b>orderSellinItemService</b>.
	 * 
	 * @param orderSellinItemService the orderSellinItemService to set
	 */
	public static void setOrderSellinItemService(OrderSellinItemService orderSellinItemService) {
		DtoFetchingUtils.orderSellinItemService = orderSellinItemService;
	}

	/**
	 * get value of <b>orderSelloutItemService</b>.
	 * 
	 * @return the orderSelloutItemService
	 */
	public static OrderSelloutItemService getOrderSelloutItemService() {
		return orderSelloutItemService;
	}

	/**
	 * Set value to <b>orderSelloutItemService</b>.
	 * 
	 * @param orderSelloutItemService the orderSelloutItemService to set
	 */
	public static void setOrderSelloutItemService(OrderSelloutItemService orderSelloutItemService) {
		DtoFetchingUtils.orderSelloutItemService = orderSelloutItemService;
	}

	/**
	 * get value of <b>ticketItemService</b>.
	 * 
	 * @return the ticketItemService
	 */
	public static WarehouseImportTicketItemService getTicketItemService() {
		return ticketItemService;
	}

	/**
	 * Set value to <b>ticketItemService</b>.
	 * 
	 * @param ticketItemService the ticketItemService to set
	 */
	public static void setTicketItemService(WarehouseImportTicketItemService ticketItemService) {
		DtoFetchingUtils.ticketItemService = ticketItemService;
	}

	/**
	 * get value of <b>productService</b>.
	 * 
	 * @return the productService
	 */
	public static ProductService getProductService() {
		return productService;
	}

	/**
	 * Set value to <b>productService</b>.
	 * 
	 * @param productService the productService to set
	 */
	public static void setProductService(ProductService productService) {
		DtoFetchingUtils.productService = productService;
	}

	/**
	 * get value of <b>productVariationService</b>.
	 * 
	 * @return the productVariationService
	 */
	public static ProductVariationService getProductVariationService() {
		return productVariationService;
	}

	/**
	 * Set value to <b>productVariationService</b>.
	 * 
	 * @param productVariationService the productVariationService to set
	 */
	public static void setProductVariationService(ProductVariationService productVariationService) {
		DtoFetchingUtils.productVariationService = productVariationService;
	}

	/**
	 * get value of <b>unitService</b>.
	 * 
	 * @return the unitService
	 */
	public static UnitService getUnitService() {
		return unitService;
	}

	/**
	 * Set value to <b>unitService</b>.
	 * 
	 * @param unitService the unitService to set
	 */
	public static void setUnitService(UnitService unitService) {
		DtoFetchingUtils.unitService = unitService;
	}

	/**
	 * get value of <b>importTicketService</b>.
	 * 
	 * @return the importTicketService
	 */
	public static WarehouseImportTicketService getImportTicketService() {
		return importTicketService;
	}

	/**
	 * Set value to <b>importTicketService</b>.
	 * 
	 * @param importTicketService the importTicketService to set
	 */
	public static void setImportTicketService(WarehouseImportTicketService importTicketService) {
		DtoFetchingUtils.importTicketService = importTicketService;
	}

	/**
	 * get value of <b>exporTicketService</b>.
	 * 
	 * @return the exporTicketService
	 */
	public static WarehouseExportTicketService getExportTicketService() {
		return exportTicketService;
	}

	/**
	 * Set value to <b>exporTicketService</b>.
	 * 
	 * @param exporTicketService the exporTicketService to set
	 */
	public static void setExportTicketService(WarehouseExportTicketService exportTicketService) {
		DtoFetchingUtils.exportTicketService = exportTicketService;
	}

	/**
	 * get value of <b>warehouseService</b>.
	 * 
	 * @return the warehouseService
	 */
	public static WarehouseService getWarehouseService() {
		return warehouseService;
	}

	/**
	 * Set value to <b>warehouseService</b>.
	 * 
	 * @param warehouseService the warehouseService to set
	 */
	public static void setWarehouseService(WarehouseService warehouseService) {
		DtoFetchingUtils.warehouseService = warehouseService;
	}

	public static RetailerWarehouseImportTicketService getRwhImportTicketService() {
		return rwhImportTicketService;
	}

	public static void setRwhImportTicketService(RetailerWarehouseImportTicketService rwhImportTicketService) {
		DtoFetchingUtils.rwhImportTicketService = rwhImportTicketService;
	}

	public static RetailerWarehouseExportTicketService getRwhExportTicketService() {
		return rwhExportTicketService;
	}

	public static void setRwhExportTicketService(RetailerWarehouseExportTicketService rwhExportTicketService) {
		DtoFetchingUtils.rwhExportTicketService = rwhExportTicketService;
	}

	public static RetailerService getRetailerService() {
		return retailerService;
	}

	public static void setRetailerService(RetailerService retailerService) {
		DtoFetchingUtils.retailerService = retailerService;
	}

	/**
	 * get value of <b>productCategoryService</b>.
	 * 
	 * @return the productCategoryService
	 */
	public static ProductCategoryService getProductCategoryService() {
		return productCategoryService;
	}

	/**
	 * Set value to <b>productCategoryService</b>.
	 * 
	 * @param productCategoryService the productCategoryService to set
	 */
	public static void setProductCategoryService(ProductCategoryService productCategoryService) {
		DtoFetchingUtils.productCategoryService = productCategoryService;
	}

	/**
	 * get value of <b>promotionProductGroupDetailService</b>.
	 * 
	 * @return the promotionProductGroupDetailService
	 */
	public static PromotionProductGroupDetailService getPromotionProductGroupDetailService() {
		return promotionProductGroupDetailService;
	}

	/**
	 * Set value to <b>promotionProductGroupDetailService</b>.
	 * 
	 * @param promotionProductGroupDetailService the
	 *                                           promotionProductGroupDetailService
	 *                                           to set
	 */
	public static void setPromotionProductGroupDetailService(
			PromotionProductGroupDetailService promotionProductGroupDetailService) {
		DtoFetchingUtils.promotionProductGroupDetailService = promotionProductGroupDetailService;
	}

	/**
	 * get value of <b>promotionLocationService</b>.
	 * 
	 * @return the promotionLocationService
	 */
	public static PromotionLocationService getPromotionLocationService() {
		return promotionLocationService;
	}

	/**
	 * Set value to <b>promotionLocationService</b>.
	 * 
	 * @param promotionLocationService the promotionLocationService to set
	 */
	public static void setPromotionLocationService(PromotionLocationService promotionLocationService) {
		DtoFetchingUtils.promotionLocationService = promotionLocationService;
	}

	/**
	 * get value of <b>promotionRewardFormatService</b>.
	 * 
	 * @return the promotionRewardFormatService
	 */
	public static PromotionRewardFormatService getPromotionRewardFormatService() {
		return promotionRewardFormatService;
	}

	/**
	 * Set value to <b>promotionRewardFormatService</b>.
	 * 
	 * @param promotionRewardFormatService the promotionRewardFormatService to set
	 */
	public static void setPromotionRewardFormatService(PromotionRewardFormatService promotionRewardFormatService) {
		DtoFetchingUtils.promotionRewardFormatService = promotionRewardFormatService;
	}

	/**
	 * get value of <b>promotionConditionFormatService</b>.
	 * 
	 * @return the promotionConditionFormatService
	 */
	public static PromotionConditionFormatService getPromotionConditionFormatService() {
		return promotionConditionFormatService;
	}

	/**
	 * Set value to <b>promotionConditionFormatService</b>.
	 * 
	 * @param promotionConditionFormatService the promotionConditionFormatService to
	 *                                        set
	 */
	public static void setPromotionConditionFormatService(
			PromotionConditionFormatService promotionConditionFormatService) {
		DtoFetchingUtils.promotionConditionFormatService = promotionConditionFormatService;
	}

	/**
	 * get value of <b>promotionLimitationService</b>.
	 * 
	 * @return the promotionLimitationService
	 */
	public static PromotionLimitationService getPromotionLimitationService() {
		return promotionLimitationService;
	}

	/**
	 * Set value to <b>promotionLimitationService</b>.
	 * 
	 * @param promotionLimitationService the promotionLimitationService to set
	 */
	public static void setPromotionLimitationService(PromotionLimitationService promotionLimitationService) {
		DtoFetchingUtils.promotionLimitationService = promotionLimitationService;
	}

	/**
	 * get value of <b>promotionLimitationItemService</b>.
	 * 
	 * @return the promotionLimitationItemService
	 */
	public static PromotionLimitationItemService getPromotionLimitationItemService() {
		return promotionLimitationItemService;
	}

	/**
	 * Set value to <b>promotionLimitationItemService</b>.
	 * 
	 * @param promotionLimitationItemService the promotionLimitationItemService to
	 *                                       set
	 */
	public static void setPromotionLimitationItemService(
			PromotionLimitationItemService promotionLimitationItemService) {
		DtoFetchingUtils.promotionLimitationItemService = promotionLimitationItemService;
	}

	/**
	 * get value of <b>promotionProductGroupService</b>.
	 * 
	 * @return the promotionProductGroupService
	 */
	public static PromotionProductGroupService getPromotionProductGroupService() {
		return promotionProductGroupService;
	}

	/**
	 * Set value to <b>promotionProductGroupService</b>.
	 * 
	 * @param promotionProductGroupService the promotionProductGroupService to set
	 */
	public static void setPromotionProductGroupService(PromotionProductGroupService promotionProductGroupService) {
		DtoFetchingUtils.promotionProductGroupService = promotionProductGroupService;
	}

	/**
	 * get value of <b>promotionLimitationItemRewardProductService</b>.
	 * 
	 * @return the promotionLimitationItemRewardProductService
	 */
	public static PromotionLimitationItemRewardProductService getPromotionLimitationItemRewardProductService() {
		return promotionLimitationItemRewardProductService;
	}

	/**
	 * Set value to <b>promotionLimitationItemRewardProductService</b>.
	 * 
	 * @param promotionLimitationItemRewardProductService the
	 *                                                    promotionLimitationItemRewardProductService
	 *                                                    to set
	 */
	public static void setPromotionLimitationItemRewardProductService(
			PromotionLimitationItemRewardProductService promotionLimitationItemRewardProductService) {
		DtoFetchingUtils.promotionLimitationItemRewardProductService = promotionLimitationItemRewardProductService;
	}

	/**
	 * get value of <b>promotionParticipantRetailerService</b>.
	 * 
	 * @return the promotionParticipantRetailerService
	 */
	public static PromotionParticipantRetailerService getPromotionParticipantRetailerService() {
		return promotionParticipantRetailerService;
	}

	/**
	 * Set value to <b>promotionParticipantRetailerService</b>.
	 * 
	 * @param promotionParticipantRetailerService the
	 *                                            promotionParticipantRetailerService
	 *                                            to set
	 */
	public static void setPromotionParticipantRetailerService(
			PromotionParticipantRetailerService promotionParticipantRetailerService) {
		DtoFetchingUtils.promotionParticipantRetailerService = promotionParticipantRetailerService;
	}

	/**
	 * Fetch one Account.
	 * 
	 * @param account
	 * @return
	 */
	public static AccountDto fetchAccount(Account account) {
		AccountDto result = new AccountDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, account);
		result.setId(account.getId());
		result.setEmail(account.getEmail());
		result.setLoginName(account.getLoginName());
		result.setAccountType(account.getAccountType());
		return result;
	}

	/**
	 * Fetch List of Company to List of CompanyDto.
	 * 
	 * @param roles list of {@link Company}
	 * @return list of {@link CompanyDto}
	 */
	public static List<CompanyDto> fetchCompanies(List<Company> companies) {
		if (companies == null) {
			return new ArrayList<>();
		}

		List<CompanyDto> result = new LinkedList<>();
		for (Company company : companies) {
			result.add(fetchCompany(company));
		}

		return result;
	}

	/**
	 * Fetch Company to CompanyDto.
	 * 
	 * @param role {@link Company}
	 * @return {@link CompanyDto}
	 */
	private static CompanyDto fetchCompany(Company company) {
		CompanyDto result = new CompanyDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, company);
		result.setShortName(company.getShortName());
		result.setFullname(company.getFullname());
		result.setAddress(company.getAddress());
		result.setFax(company.getFax());
		result.setTax(company.getTax());
		result.setTelephone(company.getTelephone());
		result.setWebsite(company.getWebsite());
		DistrictDto district = fetchDistrict(company.getDistrict());
		result.setDistrict(district);
		result.setCity(district.getCity());
		district.setCity(null);
		return result;
	}

	/**
	 * Fetch List of District to List of DistrictDto.
	 * 
	 * @param roles list of {@link District}
	 * @return list of {@link DistrictDto}
	 */
	public static List<DistrictDto> fetchDistricts(List<District> districts) {
		if (districts == null) {
			return new ArrayList<>();
		}

		List<DistrictDto> result = new LinkedList<>();
		for (District district : districts) {
			result.add(fetchDistrict(district));
		}

		return result;
	}

	/**
	 * Fetch District to DistrictDto.
	 * 
	 * @param role {@link District}
	 * @return {@link DistrictDto}
	 */
	public static DistrictDto fetchDistrict(District district) {
		DistrictDto result = new DistrictDto();
		result.setCode(district.getCode());
		result.setName(district.getName());
		result.setType(district.getType());
		result.setCity(fetchCity(district.getCity()));
		return result;
	}

	/**
	 * Fetch List of City to List of CityDto.
	 * 
	 * @param roles list of {@link City}
	 * @return list of {@link CityDto}
	 */
	public static List<CityDto> fetchCities(List<City> cities) {
		if (cities == null) {
			return new ArrayList<>();
		}

		List<CityDto> result = new LinkedList<>();
		for (City city : cities) {
			result.add(fetchCity(city));
		}

		return result;
	}

	/**
	 * Fetch City to CityDto.
	 * 
	 * @param role {@link City}
	 * @return {@link CityDto}
	 */
	public static CityDto fetchCity(City city) {
		CityDto result = new CityDto();
		result.setCode(city.getCode());
		result.setName(city.getName());
		result.setType(city.getType());
		return result;
	}

	public static List<CustomerDto> fetchCustomers(List<Customer> customers) {
		if (customers == null) {
			return new ArrayList<>();
		}

		List<CustomerDto> result = new LinkedList<>();
		for (Customer customer : customers) {
			result.add(fetchCustomer(customer, false));
		}

		return result;
	}

	public static CustomerDto fetchCustomer(Customer customer, boolean fetchDetail) {
		CustomerDto result = new CustomerDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, customer);
		String props = "email,fullname,shortname,telephone,industryId,representative,representativeEmail,address,city,districtId,wardId,telephone,fax,website,picture,customerPlanId";
		result.setRepresentativeTel(customer.getRepresentativeMobile());
		SystemUtils.getInstance().copyProperties(customer, result, props.split(","));
		if (fetchDetail) {
			if (customer.getAccount() != null) {
				result.setAccount(fetchAccount(customer.getAccount()));
			}
		}
		
		if (StringUtils.isNotBlank(customer.getPicture())) {
            String picture = customer.getPicture();
            picture = ResourceUrlResolver.getInstance().resolveUrl(customer.getId(), picture);
            result.setPicture(picture);
        }

		return result;
	}

	public static List<IndustryDto> fetchIndustries(List<Industry> industries) {
		if (industries == null) {
			return new ArrayList<>();
		}

		List<IndustryDto> result = new LinkedList<>();
		for (Industry industry : industries) {
			result.add(fetchIndustry(industry, false));
		}

		return result;
	}

	public static IndustryDto fetchIndustry(Industry industry, boolean fetchDetail) {
		IndustryDto result = new IndustryDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, industry);
		String props = "name,industryCode,level,description,level1Code,level2Code,level3Code,level4Code,level5Code";
		SystemUtils.getInstance().copyProperties(industry, result, props.split(","));

		return result;
	}

	public static List<PriceUnitDto> fetchPriceUnits(List<PriceUnit> priceUnits) {
		if (priceUnits == null) {
			return new ArrayList<>();
		}

		List<PriceUnitDto> result = new LinkedList<>();
		for (PriceUnit priceUnit : priceUnits) {
			result.add(fetchPriceUnit(priceUnit, false));
		}

		return result;
	}

	public static PriceUnitDto fetchPriceUnit(PriceUnit priceUnit, boolean fetchDetail) {
		PriceUnitDto result = new PriceUnitDto();
		ModuleDto module = fetchModule(priceUnit.getModule(), false);
		CommonFetchingUtils.fetchStatusTimestamp(result, priceUnit);
		String props = "type,yearlyPrice,monthlyPrice,quotaKey,quotaUnitCount";
		SystemUtils.getInstance().copyProperties(priceUnit, result, props.split(","));
		result.setModule(module);

		return result;
	}

	public static ModuleDto fetchModule(Module module, boolean fetchDetail) {
		ModuleDto result = new ModuleDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, module);
		String props = "moduleName,description,moduleGroup,moduleGroupName,requiredModules";
		SystemUtils.getInstance().copyProperties(module, result, props.split(","));

		return result;
	}

	public static List<InvoiceDto> fetchInvoices(List<Invoice> invoices) {
		if (invoices == null) {
			return new ArrayList<>();
		}

		List<InvoiceDto> result = new LinkedList<>();
		for (Invoice invoice : invoices) {
			result.add(fetchInvoice(invoice, false));
		}

		return result;
	}

	public static InvoiceDto fetchInvoice(Invoice invoice, boolean fetchDetail) {
		InvoiceDto result = new InvoiceDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, invoice);
		String props = "issueDate,paidDate,startDate,endDate,vat,total,billItems,customerId,planId,customerPlanId";
		SystemUtils.getInstance().copyProperties(invoice, result, props.split(","));

		return result;
	}

	public static List<PlanDto> fetchPlans(List<Plan> plans) {
		if (plans == null) {
			return new ArrayList<>();
		}

		List<PlanDto> result = new LinkedList<>();
		for (Plan plan : plans) {
			result.add(fetchPlan(plan, false));
		}

		return result;
	}

	public static PlanDto fetchPlan(Plan plan, boolean fetchDetail) {
		PlanDto result = new PlanDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, plan);
		String props = "name,description,isCustomize,isPublic";
		SystemUtils.getInstance().copyProperties(plan, result, props.split(","));

		return result;
	}

	public static List<UserDto> fetchUsers(List<User> users) {
		if (users == null) {
			return new ArrayList<>();
		}

		List<UserDto> result = new LinkedList<>();
		for (User user : users) {
			result.add(fetchUser(user, false));
		}

		return result;
	}

	public static UserDto fetchUser(User user, boolean fetchDetail) {
		UserDto result = new UserDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, user);
		String props = "accountId,fullName,address,mobile,tel,telExt";
		SystemUtils.getInstance().copyProperties(user, result, props.split(","));

		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<?> fetchWarehouseImportTickets(List<WarehouseImportTicket> warehouseImportTickets) {
		if (warehouseImportTickets == null) {
			return new ArrayList<>();
		}

		List<WarehouseImportTicketDto> result = new LinkedList<>();
		for (WarehouseImportTicket warehouseImportTicket : warehouseImportTickets) {
			result.add(fetchWarehouseImportTicket(warehouseImportTicket, false));
		}

		return result;
	}

	/**
	 * @param warehouseImportTicket
	 * @return
	 */
	public static WarehouseImportTicketDto fetchWarehouseImportTicket(WarehouseImportTicket warehouseImportTicket,
			boolean isDetail) {
		WarehouseImportTicketDto result = new WarehouseImportTicketDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, warehouseImportTicket);
		String props = "warehouseId,importTicketCode,importType,importPerson,importDate,approvedPerson,description,total,extraData";
		SystemUtils.getInstance().copyProperties(warehouseImportTicket, result, props.split(","));
		List<WarehouseImportTicketItemDto> dtos = new ArrayList<WarehouseImportTicketItemDto>();
		if (isDetail) {
			List<WarehouseImportTicketItem> items = getTicketItemService()
					.getAllByTicketId(warehouseImportTicket.getId());
			dtos = fetchWarehouseImportTicketItems(items);
		}
		result.setItems(dtos);
		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<WarehouseImportTicketItemDto> fetchWarehouseImportTicketItems(
			List<WarehouseImportTicketItem> warehouseImportTicketItems) {
		if (warehouseImportTicketItems == null) {
			return new ArrayList<>();
		}

		List<WarehouseImportTicketItemDto> result = new LinkedList<>();
		for (WarehouseImportTicketItem warehouseImportTicketItem : warehouseImportTicketItems) {
			result.add(fetchWarehouseImportTicketItem(warehouseImportTicketItem));
		}

		return result;
	}

	/**
	 * @param warehouseImportTicket
	 * @return
	 */
	public static WarehouseImportTicketItemDto fetchWarehouseImportTicketItem(
			WarehouseImportTicketItem warehouseImportTicketItem) {
		WarehouseImportTicketItemDto result = new WarehouseImportTicketItemDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, warehouseImportTicketItem);
		String props = "warehouseId,importTicketId,importTicketCode,productId,productVariationId,sku,amount,incomePrice,total";
		SystemUtils.getInstance().copyProperties(warehouseImportTicketItem, result, props.split(","));

		Product product = getProductService().getActivated(warehouseImportTicketItem.getProductId());
		if (product != null) {
			result.setProductCode(product.getProductCode());
			result.setProductName(product.getName());
		}

		ProductVariation productVari = getProductVariationService()
				.getActivated(warehouseImportTicketItem.getProductVariationId());
		if (productVari != null) {
			result.setUnitId(productVari.getUnitId());
			Unit unit = getUnitService().getActivated(productVari.getUnitId());
			if (unit != null) {
				result.setUnitName(unit.getName());
			}
		}

		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<?> fetchProducts(List<Product> products) {
		if (products == null) {
			return new ArrayList<>();
		}

		List<ProductDto> result = new LinkedList<>();
		for (Product product : products) {
			result.add(fetchProduct(product));
		}

		return result;
	}

	/**
	 * @param product
	 * @return
	 */
	public static ProductDto fetchProduct(Product product) {
		ProductDto result = new ProductDto();
		String props = "id,productCode,brandId,name,minOrder,status,mainCateId";
		SystemUtils.getInstance().copyProperties(product, result, props.split(","));
		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<?> fetchPromotionConditionFormats(List<PromotionConditionFormat> promotions) {
		if (promotions == null) {
			return new ArrayList<>();
		}

		List<PromotionConditionFormatDto> result = new LinkedList<>();
		for (PromotionConditionFormat promotion : promotions) {
			result.add(fetchPromotionConditionFormat(promotion));
		}

		return result;
	}

	/**
	 * @param PromotionConditionFormatDto
	 * @return
	 */
	public static PromotionConditionFormatDto fetchPromotionConditionFormat(PromotionConditionFormat promotion) {
		PromotionConditionFormatDto result = new PromotionConditionFormatDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, promotion);
		result.setName(promotion.getName());

		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<?> fetchPromotionRewardFormats(List<PromotionRewardFormat> promotions) {
		if (promotions == null) {
			return new ArrayList<>();
		}

		List<PromotionRewardFormatDto> result = new LinkedList<>();
		for (PromotionRewardFormat promotion : promotions) {
			result.add(fetchPromotionRewardFormat(promotion));
		}

		return result;
	}

	/**
	 * @param PromotionRewardFormatDto
	 * @return
	 */
	public static PromotionRewardFormatDto fetchPromotionRewardFormat(PromotionRewardFormat promotion) {
		PromotionRewardFormatDto result = new PromotionRewardFormatDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, promotion);
		result.setName(promotion.getName());

		return result;
	}

	/**
	 * @param warehouseDailyRemains
	 * @return
	 */
	public static List<WarehouseDailyRemainDto> fetchWarehouseDailyRemains(
			List<WarehouseDailyRemain> warehouseDailyRemains) {
		if (warehouseDailyRemains == null) {
			return new ArrayList<>();
		}

		List<WarehouseDailyRemainDto> result = new LinkedList<>();
		for (WarehouseDailyRemain warehouseDailyRemain : warehouseDailyRemains) {
			result.add(fetchWarehouseDailyRemain(warehouseDailyRemain));
		}

		return result;
	}

	/**
	 * @param warehouseDailyRemain
	 * @return
	 */
	private static WarehouseDailyRemainDto fetchWarehouseDailyRemain(WarehouseDailyRemain warehouseDailyRemain) {
		WarehouseDailyRemainDto result = new WarehouseDailyRemainDto();
		String props = "logDate,warehouseId,productId,productVariationId,sku,amountApprovedOrder,amountAvailable,amountToday,amountImport,amountExport,amount";
		SystemUtils.getInstance().copyProperties(warehouseDailyRemain, result, props.split(","));
		Product product = getProductService().getActivated(warehouseDailyRemain.getProductId());
		if (product != null) {
			result.setProductCode(product.getProductCode());
		}

		ProductVariation productVari = getProductVariationService()
				.getActivated(warehouseDailyRemain.getProductVariationId());
		if (productVari != null) {
			result.setUnitId(productVari.getUnitId());
			result.setProductName(productVari.getName());
			Unit unit = getUnitService().getActivated(productVari.getUnitId());
			if (unit != null) {
				result.setUnitName(unit.getName());
			}
		}
		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<WarehouseImportItemHistoryDto> fetchWarehouseImportItemHistories(
			List<WarehouseItemHistory> warehouseItemHistories) {
		if (warehouseItemHistories == null) {
			return new ArrayList<>();
		}

		List<WarehouseImportItemHistoryDto> result = new LinkedList<>();
		for (WarehouseItemHistory warehouseItemHistory : warehouseItemHistories) {
			result.add(fetchWarehouseImportItemHistory(warehouseItemHistory));
		}

		return result;
	}

	/**
	 * @param warehouse
	 * @return
	 */
	public static WarehouseImportItemHistoryDto fetchWarehouseImportItemHistory(
			WarehouseItemHistory warehouseItemHistory) {
		WarehouseImportItemHistoryDto result = new WarehouseImportItemHistoryDto();
		String props = "id,warehouseId,productVariationId,importTicketCode,sku";
		SystemUtils.getInstance().copyProperties(warehouseItemHistory, result, props.split(","));
		ProductVariation productVari = getProductVariationService()
				.getActivated(warehouseItemHistory.getProductVariationId());
		Product product = getProductService().getActivated(warehouseItemHistory.getProductId());
		result.setAmount(warehouseItemHistory.getChangeAmount());
		result.setImportDate(warehouseItemHistory.getChangeDate());
		if (productVari != null) {
			result.setProductVariationName(productVari.getName());
			result.setUnitId(productVari.getUnitId());
		}

		if (product != null) {
			result.setProductVariationCode(product.getProductCode());
		}

		if (warehouseItemHistory.getImportTicketId() != null) {
			WarehouseImportTicket warehouseImportTicket = getImportTicketService()
					.getActivated(warehouseItemHistory.getImportTicketId());
			if (warehouseImportTicket != null) {
				result.setPersonInCharge(warehouseImportTicket.getApprovedPerson());
				result.setDescription(warehouseImportTicket.getDescription());
			}
		}

		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<WarehouseExportItemHistoryDto> fetchWarehouseExportItemHistories(
			List<WarehouseItemHistory> warehouseItemHistories) {
		if (warehouseItemHistories == null) {
			return new ArrayList<>();
		}

		List<WarehouseExportItemHistoryDto> result = new LinkedList<>();
		for (WarehouseItemHistory warehouseItemHistory : warehouseItemHistories) {
			result.add(fetchWarehouseExportItemHistory(warehouseItemHistory));
		}

		return result;
	}

	/**
	 * @param warehouse
	 * @return
	 */
	public static WarehouseExportItemHistoryDto fetchWarehouseExportItemHistory(
			WarehouseItemHistory warehouseItemHistory) {
		WarehouseExportItemHistoryDto result = new WarehouseExportItemHistoryDto();
		String props = "id,warehouseId,productVariationId,sku";
		SystemUtils.getInstance().copyProperties(warehouseItemHistory, result, props.split(","));
		ProductVariation productVari = getProductVariationService()
				.getActivated(warehouseItemHistory.getProductVariationId());
		Product product = getProductService().getActivated(warehouseItemHistory.getProductId());
		Warehouse warehouse = getWarehouseService().getActivated(warehouseItemHistory.getWarehouseId());
		result.setAmount(warehouseItemHistory.getChangeAmount());
		result.setExportDate(warehouseItemHistory.getChangeDate());
		result.setOrderCode(StringUtils.isNotBlank(warehouseItemHistory.getOrderSellinCode())
				? warehouseItemHistory.getOrderSellinCode()
				: warehouseItemHistory.getOrderSelloutCode());
		if (productVari != null) {
			result.setProductVariationName(productVari.getName());
			result.setUnitId(productVari.getUnitId());
		}

		if (product != null) {
			result.setProductVariationCode(product.getProductCode());
		}

		if (warehouseItemHistory.getExportTicketId() != null) {
			WarehouseExportTicket warehouseExportTicket = getExportTicketService()
					.getActivated(warehouseItemHistory.getExportTicketId());
			if (warehouseExportTicket != null) {
				result.setPersonInCharge(warehouseExportTicket.getExportPerson());
				result.setDescription(warehouseExportTicket.getDescription());
			}
		}

		if (warehouse != null) {
			result.setDeliveryAddress(warehouse.getFullAddress());
		}

		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<RetailerWarehouseImportItemHistoryDto> fetchRetailerWarehouseImportItemHistories(
			List<RetailerWarehouseItemHistory> retailerWarehouseItemHistories) {
		if (retailerWarehouseItemHistories == null) {
			return new ArrayList<>();
		}

		List<RetailerWarehouseImportItemHistoryDto> result = new LinkedList<>();
		for (RetailerWarehouseItemHistory retailerWarehouseItemHistory : retailerWarehouseItemHistories) {
			result.add(fetchRetailerWarehouseImportItemHistory(retailerWarehouseItemHistory));
		}

		return result;
	}

	/**
	 * @param warehouse
	 * @return
	 */
	public static RetailerWarehouseImportItemHistoryDto fetchRetailerWarehouseImportItemHistory(
			RetailerWarehouseItemHistory retailerWarehouseItemHistory) {
		RetailerWarehouseImportItemHistoryDto result = new RetailerWarehouseImportItemHistoryDto();
		String props = "id,retailerId,productVariationId,importTicketCode";
		SystemUtils.getInstance().copyProperties(retailerWarehouseItemHistory, result, props.split(","));
		ProductVariation productVari = getProductVariationService()
				.getActivated(retailerWarehouseItemHistory.getProductVariationId());
		Product product = getProductService().getActivated(retailerWarehouseItemHistory.getProductId());
		result.setAmount(retailerWarehouseItemHistory.getChangeAmount());
		result.setImportDate(retailerWarehouseItemHistory.getChangeDate());
		if (productVari != null) {
			result.setProductVariationName(productVari.getName());
			result.setUnitId(productVari.getUnitId());
		}

		if (product != null) {
			result.setProductVariationCode(product.getProductCode());
		}

		if (retailerWarehouseItemHistory.getImportTicketId() != null) {
			RetailerWarehouseImportTicket warehouseImportTicket = getRwhImportTicketService()
					.getActivated(retailerWarehouseItemHistory.getImportTicketId());
			if (warehouseImportTicket != null) {
				result.setPersonInCharge(warehouseImportTicket.getImportPerson());
				result.setDescription(warehouseImportTicket.getDescription());
			}
		}

		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<RetailerWarehouseExportItemHistoryDto> fetchRetailerWarehouseExportItemHistories(
			List<RetailerWarehouseItemHistory> retailerWarehouseItemHistories) {
		if (retailerWarehouseItemHistories == null) {
			return new ArrayList<>();
		}

		List<RetailerWarehouseExportItemHistoryDto> result = new LinkedList<>();
		for (RetailerWarehouseItemHistory retailerWarehouseItemHistory : retailerWarehouseItemHistories) {
			result.add(fetchRetailerWarehouseExportItemHistory(retailerWarehouseItemHistory));
		}

		return result;
	}

	/**
	 * @param warehouse
	 * @return
	 */
	public static RetailerWarehouseExportItemHistoryDto fetchRetailerWarehouseExportItemHistory(
			RetailerWarehouseItemHistory warehouseItemHistory) {
		RetailerWarehouseExportItemHistoryDto result = new RetailerWarehouseExportItemHistoryDto();
		String props = "id,retailerId,productVariationId";
		SystemUtils.getInstance().copyProperties(warehouseItemHistory, result, props.split(","));
		ProductVariation productVari = getProductVariationService()
				.getActivated(warehouseItemHistory.getProductVariationId());
		Product product = getProductService().getActivated(warehouseItemHistory.getProductId());
		Retailer retailer = getRetailerService().getActivated(warehouseItemHistory.getRetailerId());
		result.setAmount(warehouseItemHistory.getChangeAmount());
		result.setExportDate(warehouseItemHistory.getChangeDate());
		result.setOrderCode(warehouseItemHistory.getOrderSellinCode());
		if (productVari != null) {
			result.setProductVariationName(productVari.getName());
			result.setUnitId(productVari.getUnitId());
		}

		if (product != null) {
			result.setProductVariationCode(product.getProductCode());
		}

		if (retailer != null) {
			result.setDeliveryAddress(retailer.getFullAddress());
		}

		if (warehouseItemHistory.getExportTicketId() != null) {
			RetailerWarehouseExportTicket warehouseExportTicket = getRwhExportTicketService()
					.getActivated(warehouseItemHistory.getExportTicketId());
			if (warehouseExportTicket != null) {
				result.setPersonInCharge(warehouseExportTicket.getExportPerson());
				result.setDescription(warehouseExportTicket.getDescription());
			}
		}

		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<StaffOfCustomerDto> fetchStaffOfCustomers(List<StaffOfCustomer> staffOfCustomers) {
		if (staffOfCustomers == null) {
			return new ArrayList<>();
		}
    
		List<StaffOfCustomerDto> result = new LinkedList<>();
		for (StaffOfCustomer staffOfCustomer : staffOfCustomers) {
			result.add(fetchStaffOfCustomerInfo(staffOfCustomer));
		}

		return result;
	}

	/**
     * @param staffOfCustomer
     * @return
     */
    public static StaffOfCustomerDto fetchStaffOfCustomerInfo(StaffOfCustomer staffOfCustomer) {
        StaffOfCustomerDto result = new StaffOfCustomerDto();
        CommonFetchingUtils.fetchStatusTimestamp(result, staffOfCustomer);
        result.setFullName(staffOfCustomer.getFullName());
        result.setAddress(staffOfCustomer.getAddress());
        result.setMobile(staffOfCustomer.getMobile());
        result.setTel(staffOfCustomer.getTelephone());
        result.setTelExt(staffOfCustomer.getTelephoneExt());
        result.setAccountId(staffOfCustomer.getAccount().getId());
        result.setCustomerId(staffOfCustomer.getCustomer().getId());
        result.setEmail(staffOfCustomer.getEmail());
        result.setLoginName(staffOfCustomer.getAccount().getLoginName());
        
        String avatar = staffOfCustomer.getAvatar();
        if (StringUtils.isNotBlank(avatar)) {
            avatar = ResourceUrlResolver.getInstance().resolveStaffUrl(CustomerContext.getCustomerId(), avatar);
        } else {
            avatar = ResourceUrlResolver.getInstance().resolveStaffUrl(CustomerContext.getCustomerId(), "default.png");
        }
        
        result.setAvatar(avatar);
        return result;
    }

    
	/**
	 * @param promotionProductGroupDetails
	 * @return
	 */
	public static List<PromotionProductGroupDetailDto> fetchPromotionGroupDetails(
			List<PromotionProductGroupDetail> promotionProductGroupDetails) {
		if (promotionProductGroupDetails == null) {
			return new ArrayList<>();
		}
		List<PromotionProductGroupDetailDto> result = new LinkedList<>();
		for (PromotionProductGroupDetail promotionProductGroupDetail : promotionProductGroupDetails) {
			result.add(fetchPromotionProductGroupDetail(promotionProductGroupDetail));
		}
		return result;
	}

	/**
	 * @param promotionProductGroupDetail
	 * @return
	 */
	private static PromotionProductGroupDetailDto fetchPromotionProductGroupDetail(
			PromotionProductGroupDetail promotionProductGroupDetail) {
		PromotionProductGroupDetailDto result = new PromotionProductGroupDetailDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, promotionProductGroupDetail);
		String props = "productVariationId,productVariationName,categoryId,categoryLevel,categoryIdLv0,categoryIdLv1,categoryIdLv2,categoryIdLv3,categoryIdLv4,categoryIdLv5";
		SystemUtils.getInstance().copyProperties(promotionProductGroupDetail, result, props.split(","));
		return result;
	}

	/**
	 * @param entities
	 * @return
	 */
	public static List<?> fetchPromotions(List<Promotion> promotions) {
		if (promotions == null) {
			return new ArrayList<>();
		}

		List<PromotionDto> result = new LinkedList<>();
		for (Promotion promotion : promotions) {
			result.add(fetchPromotion(promotion));
		}

		return result;
	}

	/**
	 * @param PromotionDto
	 * @return
	 */
	public static PromotionDto fetchPromotion(Promotion promotion) {
		PromotionDto result = new PromotionDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, promotion);
		String props = "id,promotionCode,promotionType,name,content,startDate,endDate,preparationDate,manualEndDate,promotionState,"
				+ "conditionFormatId,rewardFormatId,subjectType,conditionComparitionType,limitationClaimType,display";
		SystemUtils.getInstance().copyProperties(promotion, result, props.split(","));

		String banner = promotion.getBanner();
		banner = ResourceUrlResolver.getInstance().resolvePromotionUrl(1, banner);
		result.setBanner(banner);
		return result;
	}

	/**
	 * @param PromotionDto
	 * @return
	 */
	public static PromotionDto fetchPromotionWithFullInformation(Promotion promotion) {
		PromotionDto result = fetchPromotion(promotion);
		// fetch location
		List<PromotionLocation> promotionLocations = getPromotionLocationService().getByPromotionId(promotion.getId());
		result.setPromotionLocationDto(fetchPromotionLocations(promotionLocations));

		// fetch retailer
		List<PromotionParticipantRetailer> promotionParticpipants = getPromotionParticipantRetailerService()
				.getByPromotionId(promotion.getId());
		result.setPromotionParticipants(fetchPromotionParticipants(promotionParticpipants));

		// fetch promotion group
		List<PromotionProductGroupDto> promotionProductGroupDtos = fetchPromotionGroups(
				getPromotionProductGroupService().getByPromotionId(promotion.getId()));
		result.setPromotionGroups(promotionProductGroupDtos);
		// fetch promotion reward format
		PromotionRewardFormat promotionRewardFormat = getPromotionRewardFormatService()
				.getActivated(promotion.getRewardFormatId());
		result.setPromotionRewardFormat(DtoFetchingUtils.fetchPromotionRewardFormat(promotionRewardFormat));
		// fetch promotion condition format
		PromotionConditionFormat promotionConditionFormat = getPromotionConditionFormatService()
				.getActivated(promotion.getConditionFormatId());
		result.setPromotionConditionFormat(DtoFetchingUtils.fetchPromotionConditionFormat(promotionConditionFormat));
		// fetch Promotion Limitation
		List<PromotionLimitation> promotionLimitations = getPromotionLimitationService()
				.getByPromotionId(promotion.getId());
		result.setPromotionLimitations(fetchPromotionLimitations(promotionLimitations));
		return result;
	}

	private static PromotionParticipantRetailerDto fetchPromotionParticipants(
			List<PromotionParticipantRetailer> promotionParticpipants) {
		PromotionParticipantRetailerDto result = new PromotionParticipantRetailerDto();

		if (CollectionUtils.isNotEmpty(promotionParticpipants)) {
			List<Integer> retailerIds = promotionParticpipants.stream().map(p -> p.getRetailerId())
					.collect(Collectors.toList());

			List<Retailer> retailers = getRetailerService().get(retailerIds);

			StringBuilder displayNames = new StringBuilder();
			int count = 1;
			for (Retailer retailer : retailers) {
				displayNames.append(retailer.getName());

				if (count++ < retailers.size()) {
					displayNames.append(", ");
				}
			}

			result.setRetailerIds(retailerIds);
			result.setRetailerNames(displayNames.toString());
		}
		return result;
	}
    
	/**
	 * @param promotionLimitations
	 * @return
	 */
	private static List<PromotionLimitationDto> fetchPromotionLimitations(
			List<PromotionLimitation> promotionLimitations) {
		if (promotionLimitations == null) {
			return new ArrayList<>();
		}

		List<PromotionLimitationDto> result = new LinkedList<>();
		for (PromotionLimitation promotionLimitation : promotionLimitations) {
			result.add(fetchPromotionLimitation(promotionLimitation));
		}

		return result;
	}

	/**
	 * @param promotionLimitation
	 * @return
	 */
	private static PromotionLimitationDto fetchPromotionLimitation(PromotionLimitation promotionLimitation) {
		PromotionLimitationDto result = new PromotionLimitationDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, promotionLimitation);
		String props = "promotionId,rewardFormatId,orderNumber";
		SystemUtils.getInstance().copyProperties(promotionLimitation, result, props.split(","));

		List<PromotionLimitationItem> promotionLimitationItems = getPromotionLimitationItemService()
				.getByPromotionIdAndPromotionLimitationId(promotionLimitation.getPromotionId(),
						promotionLimitation.getId());

		result.setPromotionLimitationItems(fetchPromotionLimitationItems(promotionLimitationItems));
		return result;
	}

	/**
	 * @param promotionLimitationItems
	 * @return
	 */
	private static List<PromotionLimitationItemDto> fetchPromotionLimitationItems(
			List<PromotionLimitationItem> promotionLimitationItems) {
		if (promotionLimitationItems == null) {
			return new ArrayList<>();
		}

		List<PromotionLimitationItemDto> result = new LinkedList<>();
		for (PromotionLimitationItem promotionLimitationItem : promotionLimitationItems) {
			result.add(fetchPromotionLimitationItem(promotionLimitationItem));
		}

		return result;
	}

	private static PromotionLimitationItemDto fetchPromotionLimitationItem(
			PromotionLimitationItem promotionLimitationItem) {
		PromotionLimitationItemDto result = new PromotionLimitationItemDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, promotionLimitationItem);
		String props = "promotionId,limitationId,limitationOrderNumber,conditionFixValue,conditionRangeFrom,conditionRangeTo,rewardFormatId,rewardPercent,rewardValue,productGroupId";
		SystemUtils.getInstance().copyProperties(promotionLimitationItem, result, props.split(","));
		if (result.getProductGroupId() != null) {
			PromotionProductGroup promotionProductGroup = getPromotionProductGroupService()
					.get(result.getProductGroupId());
			result.setPromotionProductGroupName(promotionProductGroup.getName());
		}

		// fetch promotion limitation item rewards
		List<PromotionLimitationItemRewardProduct> promotionLimitationItemRewardProducts = getPromotionLimitationItemRewardProductService()
				.getByLimitationIdAndPromotionLimitationItemId(promotionLimitationItem.getLimitationId(),
						promotionLimitationItem.getId());
		result.setPromotionLimitationItemRewardProducts(
				fetchPromotionLimitationItemRewardProducts(promotionLimitationItemRewardProducts));
		return result;
	}

	/**
	 * @param promotionLimitationItemRewardProducts
	 * @return
	 */
	private static List<PromotionLimitationItemRewardProductDto> fetchPromotionLimitationItemRewardProducts(
			List<PromotionLimitationItemRewardProduct> promotionLimitationItemRewardProducts) {
		if (promotionLimitationItemRewardProducts == null) {
			return new ArrayList<>();
		}

		List<PromotionLimitationItemRewardProductDto> result = new LinkedList<>();
		for (PromotionLimitationItemRewardProduct promotionLimitationItemRewardProduct : promotionLimitationItemRewardProducts) {
			result.add(fetchPromotionLimitationItemRewardProduct(promotionLimitationItemRewardProduct));
		}

		return result;
	}

	/**
	 * @param promotionLimitationItemRewardProduct
	 * @return
	 */
	public static PromotionLimitationItemRewardProductDto fetchPromotionLimitationItemRewardProduct(
			PromotionLimitationItemRewardProduct promotionLimitationItemRewardProduct) {
		PromotionLimitationItemRewardProductDto result = new PromotionLimitationItemRewardProductDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, promotionLimitationItemRewardProduct);
		String props = "promotionId,limitationId,limitationItemId,type,productId,productVariationId,productVariationName,sku,amount,unitAmount,packingAmount,unitId,packingId,packingExchangeRatio";
		SystemUtils.getInstance().copyProperties(promotionLimitationItemRewardProduct, result, props.split(","));
		return result;
	}

	/**
	 * @param promotionLocations
	 * @return
	 */
	private static PromotionLocationDto fetchPromotionLocations(List<PromotionLocation> promotionLocations) {
		PromotionLocationDto result = new PromotionLocationDto();
		List<PromotionLocationDetailDto> PromotionLocationDetailDtos = fetchPromotionLocationDetails(
				promotionLocations);
		if (CollectionUtils.isNotEmpty(PromotionLocationDetailDtos)) {
			result.setPromotionLocationNamesForDisplay(
					PromotionLocationDetailDtos.stream().map(pl -> pl.getName()).collect(Collectors.joining(", ")));
			result.setPromotionLocationIds(
					PromotionLocationDetailDtos.stream().map(pl -> pl.getId()).collect(Collectors.toList()));
			result.setPromotionLocationDetail(PromotionLocationDetailDtos);
		}
		return result;
	}

	/**
	 * @param promotionLocations
	 * @return
	 */
	private static List<PromotionLocationDetailDto> fetchPromotionLocationDetails(
			List<PromotionLocation> promotionLocations) {
		if (promotionLocations == null) {
			return new ArrayList<>();
		}

		List<PromotionLocationDetailDto> result = new LinkedList<>();
		for (PromotionLocation promotionLocation : promotionLocations) {
			result.add(fetchPromotionLocationDetail(promotionLocation));
		}

		return result;
	}

	/**
	 * @param promotionLocation
	 * @return
	 */
	private static PromotionLocationDetailDto fetchPromotionLocationDetail(PromotionLocation promotionLocation) {
		PromotionLocationDetailDto result = new PromotionLocationDetailDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, promotionLocation);
		String props = "promotionId,name,locationType,cityId,regionId,wardId,districtId";
		SystemUtils.getInstance().copyProperties(promotionLocation, result, props.split(","));

		return result;
	}

	private static List<PromotionProductGroupDto> fetchPromotionGroups(
			List<PromotionProductGroup> promotionProductGroups) {
		if (promotionProductGroups == null) {
			return new ArrayList<>();
		}

		List<PromotionProductGroupDto> result = new LinkedList<>();
		for (PromotionProductGroup promotionProductGroup : promotionProductGroups) {
			result.add(fetchPromotionGroup(promotionProductGroup));
		}

		return result;
	}

	/**
	 * @param promotionProductGroup
	 * @return
	 */
	private static PromotionProductGroupDto fetchPromotionGroup(PromotionProductGroup promotionProductGroup) {
		PromotionProductGroupDto result = new PromotionProductGroupDto();
		CommonFetchingUtils.fetchStatusTimestamp(result, promotionProductGroup);
		String props = "promotionId,groupId,groupName";
		SystemUtils.getInstance().copyProperties(promotionProductGroup, result, props.split(","));
		List<PromotionProductGroupDetail> promotionGroupDetails = getPromotionProductGroupDetailService()
				.getByPromotionIdAndGroupId(promotionProductGroup.getPromotionId(), promotionProductGroup.getId());
		result.setPromotionProductGroupDetails(fetchPromotionGroupDetails(promotionGroupDetails));
		return result;
	}

	public static List<WarehouseDailyRemainDto> fetchWarehouseTotalItems(List<WarehouseTotalItem> warehouseTotalItems) {
		if (warehouseTotalItems == null) {
			return new LinkedList<WarehouseDailyRemainDto>();
		}

		List<WarehouseDailyRemainDto> result = new LinkedList<>();
		for (WarehouseTotalItem warehouseTotalItem : warehouseTotalItems) {
			result.add(fetchWarehouseTotalItem(warehouseTotalItem));
		}

		return result;
	}

	private static WarehouseDailyRemainDto fetchWarehouseTotalItem(WarehouseTotalItem warehouseTotalItem) {
		WarehouseDailyRemainDto result = new WarehouseDailyRemainDto();
		String props = "productId,productVariationId,sku,amountAvailable,amountToday,amountImport,amountExport,amount";
		SystemUtils.getInstance().copyProperties(warehouseTotalItem, result, props.split(","));
		result.setAmountApprovedOrder(warehouseTotalItem.getAmountInOrders());
		Product product = getProductService().getActivated(warehouseTotalItem.getProductId());
		if (product != null) {
			result.setProductCode(product.getProductCode());
		}

		ProductVariation productVari = getProductVariationService()
				.getActivated(warehouseTotalItem.getProductVariationId());
		if (productVari != null) {
			result.setUnitId(productVari.getUnitId());
			result.setProductName(productVari.getName());
			Unit unit = getUnitService().getActivated(productVari.getUnitId());
			if (unit != null) {
				result.setUnitName(unit.getName());
			}
		}
		return result;
	}
}
