/**
 * PromotionCreationForm.java
 * @copyright  Copyright © 2020 Hieu Micro
 * @author     hieumicro
 * @version    1.0.0
 */
package co.ipicorp.saas.portalapi.form;

import co.ipicorp.saas.nrms.model.PromotionLimitationItemRewardProduct;
import co.ipicorp.saas.nrms.model.SubjectType;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * PromotionCreationForm. <<< Detail note.
 * 
 * @author hieumicro
 * @access public
 */
public class PromotionCreationForm implements Serializable {

    private static final long serialVersionUID = 1005215006837358364L;

    /*
     * Generic Information
     */
    private Integer promotionType;
    
    private String promotionCode;

    private Integer priority = 1;

    private Integer subjectType = SubjectType.CONSUMER.getValue();

    private String name;

    private String content;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate startDate;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate endDate;

    @DateTimeFormat(iso = ISO.DATE)
    private LocalDate preparationDate;

    private Integer conditionFormatId;

    private Integer rewardFormatId;

    private Integer conditionComparitionType;

    private Integer limitationClaimType;
    
    private Boolean display = Boolean.TRUE;

    private LinkedHashMap<String, Object> extraData = new LinkedHashMap<>();

    /*
     * Participant location & retailers
     */
    private List<Integer> cities = new ArrayList<>();
    private List<Integer> regions = new ArrayList<>();
    private List<Integer> retailers = new ArrayList<>();

    /*
     * Product Group information
     */
    private List<ProductGroup> groups = new ArrayList<>();

    /*
     * 
     */
    private List<Limitation> limitations = new ArrayList<>();

    /**
     * get value of <b>promotionCode</b>.
     * @return the promotionCode
     */
    public String getPromotionCode() {
        return promotionCode;
    }

    /**
     * Set value to <b>promotionCode</b>.
     * @param promotionCode the promotionCode to set
     */
    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    /**
     * get value of <b>promotionType</b>.
     * @return the promotionType
     */
    public Integer getPromotionType() {
        return promotionType;
    }

    /**
     * Set value to <b>promotionType</b>.
     * @param promotionType the promotionType to set
     */
    public void setPromotionType(Integer promotionType) {
        this.promotionType = promotionType;
    }

    /**
     * get value of <b>priority</b>.
     * @return the priority
     */
    public Integer getPriority() {
        return priority;
    }

    /**
     * Set value to <b>priority</b>.
     * @param priority the priority to set
     */
    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    /**
     * get value of <b>subjectType</b>.
     * @return the subjectType
     */
    public Integer getSubjectType() {
        return subjectType;
    }

    /**
     * Set value to <b>subjectType</b>.
     * @param subjectType the subjectType to set
     */
    public void setSubjectType(Integer subjectType) {
        this.subjectType = subjectType;
    }

    /**
     * get value of <b>name</b>.
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Set value to <b>name</b>.
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * get value of <b>content</b>.
     * @return the content
     */
    public String getContent() {
        return content;
    }

    /**
     * Set value to <b>content</b>.
     * @param content the content to set
     */
    public void setContent(String content) {
        this.content = content;
    }

    /**
     * get value of <b>startDate</b>.
     * @return the startDate
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Set value to <b>startDate</b>.
     * @param startDate the startDate to set
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    /**
     * get value of <b>endDate</b>.
     * @return the endDate
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Set value to <b>endDate</b>.
     * @param endDate the endDate to set
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    /**
     * get value of <b>preparationDate</b>.
     * @return the preparationDate
     */
    public LocalDate getPreparationDate() {
        return preparationDate;
    }

    /**
     * Set value to <b>preparationDate</b>.
     * @param preparationDate the preparationDate to set
     */
    public void setPreparationDate(LocalDate preparationDate) {
        this.preparationDate = preparationDate;
    }

    /**
     * get value of <b>conditionFormatId</b>.
     * @return the conditionFormatId
     */
    public Integer getConditionFormatId() {
        return conditionFormatId;
    }

    /**
     * Set value to <b>conditionFormatId</b>.
     * @param conditionFormatId the conditionFormatId to set
     */
    public void setConditionFormatId(Integer conditionFormatId) {
        this.conditionFormatId = conditionFormatId;
    }

    /**
     * get value of <b>rewardFormatId</b>.
     * @return the rewardFormatId
     */
    public Integer getRewardFormatId() {
        return rewardFormatId;
    }

    /**
     * Set value to <b>rewardFormatId</b>.
     * @param rewardFormatId the rewardFormatId to set
     */
    public void setRewardFormatId(Integer rewardFormatId) {
        this.rewardFormatId = rewardFormatId;
    }

    /**
     * get value of <b>conditionComparitionType</b>.
     * @return the conditionComparitionType
     */
    public Integer getConditionComparitionType() {
        return conditionComparitionType;
    }

    /**
     * Set value to <b>conditionComparitionType</b>.
     * @param conditionComparitionType the conditionComparitionType to set
     */
    public void setConditionComparitionType(Integer conditionComparitionType) {
        this.conditionComparitionType = conditionComparitionType;
    }

    /**
     * get value of <b>limitationClaimType</b>.
     * @return the limitationClaimType
     */
    public Integer getLimitationClaimType() {
        return limitationClaimType;
    }

    /**
     * Set value to <b>limitationClaimType</b>.
     * @param limitationClaimType the limitationClaimType to set
     */
    public void setLimitationClaimType(Integer limitationClaimType) {
        this.limitationClaimType = limitationClaimType;
    }

    /**
     * get value of <b>display</b>.
     * @return the display
     */
    public Boolean getDisplay() {
        return display;
    }

    /**
     * Set value to <b>display</b>.
     * @param display the display to set
     */
    public void setDisplay(Boolean display) {
        this.display = display;
    }

    /**
     * get value of <b>extraData</b>.
     * @return the extraData
     */
    public LinkedHashMap<String, Object> getExtraData() {
        return extraData;
    }

    /**
     * Set value to <b>extraData</b>.
     * @param extraData the extraData to set
     */
    public void setExtraData(LinkedHashMap<String, Object> extraData) {
        this.extraData = extraData;
    }

    /**
     * get value of <b>cities</b>.
     * @return the cities
     */
    public List<Integer> getCities() {
        return cities;
    }

    /**
     * Set value to <b>cities</b>.
     * @param cities the cities to set
     */
    public void setCities(List<Integer> cities) {
        this.cities = cities;
    }

    /**
     * get value of <b>regions</b>.
     * @return the regions
     */
    public List<Integer> getRegions() {
        return regions;
    }

    /**
     * Set value to <b>regions</b>.
     * @param regions the regions to set
     */
    public void setRegions(List<Integer> regions) {
        this.regions = regions;
    }

    /**
     * get value of <b>retailers</b>.
     * @return the retailers
     */
    public List<Integer> getRetailers() {
        return retailers;
    }

    /**
     * Set value to <b>retailers</b>.
     * @param retailers the retailers to set
     */
    public void setRetailers(List<Integer> retailers) {
        this.retailers = retailers;
    }

    /**
     * get value of <b>groups</b>.
     * @return the groups
     */
    public List<ProductGroup> getGroups() {
        return groups;
    }

    /**
     * Set value to <b>groups</b>.
     * @param groups the groups to set
     */
    public void setGroups(List<ProductGroup> groups) {
        this.groups = groups;
    }

    /**
     * get value of <b>limitations</b>.
     * @return the limitations
     */
    public List<Limitation> getLimitations() {
        return limitations;
    }

    /**
     * Set value to <b>limitations</b>.
     * @param limitations the limitations to set
     */
    public void setLimitations(List<Limitation> limitations) {
        this.limitations = limitations;
    }

    public static class ProductGroup implements Serializable {

        private static final long serialVersionUID = -4762680146642571868L;

        List<ProductGroupDetail> products = new ArrayList<>();

        /**
         * get value of <b>products</b>.
         * 
         * @return the products
         */
        public List<ProductGroupDetail> getProducts() {
            return products;
        }

        /**
         * Set value to <b>products</b>.
         * 
         * @param products
         *            the products to set
         */
        public void setProducts(List<ProductGroupDetail> products) {
            this.products = products;
        }

        @Override
        public String toString() {
            return "ProductGroup [products=" + products + "]";
        }

    }

    public static class ProductGroupDetail implements Serializable {
        private static final long serialVersionUID = 4292226121568760172L;

        // 5 RAND chars + productId + 5 RAND chars 
        private String id;
        private Integer productVariationId;
        private String name;
        private String description;
        private String sku;
        
        private Integer nodeLv0Id; // Category ID level 0 
        private Integer nodeLv1Id; // Category ID level 1
        private Integer nodeLv2Id; // Category ID level 2
        
        /**
         * get value of <b>id</b>.
         * @return the id
         */
        public String getId() {
            return id;
        }

        /**
         * Set value to <b>id</b>.
         * @param id the id to set
         */
        public void setId(String id) {
            this.id = id;
        }

        public Integer getProductId() {
            int result = 0;
            if (id != null && id.length() > 10) {
                int length = id.length();
                String productId = id.substring(5, 5 + (length - 10));
                try {
                    result = Integer.parseInt(productId);
                } catch (Exception ex) {
                }
            }
            return result;
        }
        
        /**
         * get value of <b>productVariationId</b>.
         * @return the productVariationId
         */
        public Integer getProductVariationId() {
            return productVariationId;
        }
        
        /**
         * Set value to <b>productVariationId</b>.
         * @param productVariationId the productVariationId to set
         */
        public void setProductVariationId(Integer productVariationId) {
            this.productVariationId = productVariationId;
        }
        /**
         * get value of <b>name</b>.
         * @return the name
         */
        public String getName() {
            return name;
        }
        /**
         * Set value to <b>name</b>.
         * @param name the name to set
         */
        public void setName(String name) {
            this.name = name;
        }
        /**
         * get value of <b>description</b>.
         * @return the description
         */
        public String getDescription() {
            return description;
        }
        /**
         * Set value to <b>description</b>.
         * @param description the description to set
         */
        public void setDescription(String description) {
            this.description = description;
        }
        /**
         * get value of <b>sku</b>.
         * @return the sku
         */
        public String getSku() {
            return sku;
        }
        /**
         * Set value to <b>sku</b>.
         * @param sku the sku to set
         */
        public void setSku(String sku) {
            this.sku = sku;
        }
        /**
         * get value of <b>nodeLv0Id</b>.
         * @return the nodeLv0Id
         */
        public Integer getNodeLv0Id() {
            return nodeLv0Id;
        }
        /**
         * Set value to <b>nodeLv0Id</b>.
         * @param nodeLv0Id the nodeLv0Id to set
         */
        public void setNodeLv0Id(Integer nodeLv0Id) {
            this.nodeLv0Id = nodeLv0Id;
        }
        /**
         * get value of <b>nodeLv1Id</b>.
         * @return the nodeLv1Id
         */
        public Integer getNodeLv1Id() {
            return nodeLv1Id;
        }
        /**
         * Set value to <b>nodeLv1Id</b>.
         * @param nodeLv1Id the nodeLv1Id to set
         */
        public void setNodeLv1Id(Integer nodeLv1Id) {
            this.nodeLv1Id = nodeLv1Id;
        }
        /**
         * get value of <b>nodeLv2Id</b>.
         * @return the nodeLv2Id
         */
        public Integer getNodeLv2Id() {
            return nodeLv2Id;
        }
        /**
         * Set value to <b>nodeLv2Id</b>.
         * @param nodeLv2Id the nodeLv2Id to set
         */
        public void setNodeLv2Id(Integer nodeLv2Id) {
            this.nodeLv2Id = nodeLv2Id;
        }
    }

    public static class Limitation implements Serializable {
        private static final long serialVersionUID = 6283874019216796746L;

        private List<LimitationItem> items = new ArrayList<>();

        /**
         * get value of <b>items</b>.
         * 
         * @return the items
         */
        public List<LimitationItem> getItems() {
            return items;
        }

        /**
         * Set value to <b>items</b>.
         * 
         * @param items
         *            the items to set
         */
        public void setItems(List<LimitationItem> items) {
            this.items = items;
        }

    }

    public static class LimitationItem implements Serializable {

        private static final long serialVersionUID = 4632491338767130797L;

        private Double conditionFixValue = 0d;

        private Double conditionRangeFrom = 0d;

        private Double conditionRangeTo = 0d;

        private Integer rewardFormatId = 0;

        private Double rewardPercent = 0d;

        private Double rewardValue = 0d;

        private Integer productGroupIndex; // Index from 1
        
        // TODO: Tương lại nếu muốn setup cho từng sản phẩm trong nhóm
        private Integer productVariationId;
        
        private List<RewardProduct> rewards = new ArrayList<>();

        /**
         * get value of <b>rewards</b>.
         * @return the rewards
         */
        public List<RewardProduct> getRewards() {
            return rewards;
        }

        /**
         * Set value to <b>rewards</b>.
         * @param rewards the rewards to set
         */
        public void setRewards(List<RewardProduct> rewards) {
            this.rewards = rewards;
        }

        /**
         * get value of <b>conditionFixValue</b>.
         * 
         * @return the conditionFixValue
         */
        public Double getConditionFixValue() {
            return conditionFixValue;
        }

        /**
         * Set value to <b>conditionFixValue</b>.
         * 
         * @param conditionFixValue
         *            the conditionFixValue to set
         */
        public void setConditionFixValue(Double conditionFixValue) {
            this.conditionFixValue = conditionFixValue;
        }

        /**
         * get value of <b>conditionRangeFrom</b>.
         * 
         * @return the conditionRangeFrom
         */
        public Double getConditionRangeFrom() {
            return conditionRangeFrom;
        }

        /**
         * Set value to <b>conditionRangeFrom</b>.
         * 
         * @param conditionRangeFrom
         *            the conditionRangeFrom to set
         */
        public void setConditionRangeFrom(Double conditionRangeFrom) {
            this.conditionRangeFrom = conditionRangeFrom;
        }

        /**
         * get value of <b>conditionRangeTo</b>.
         * 
         * @return the conditionRangeTo
         */
        public Double getConditionRangeTo() {
            return conditionRangeTo;
        }

        /**
         * Set value to <b>conditionRangeTo</b>.
         * 
         * @param conditionRangeTo
         *            the conditionRangeTo to set
         */
        public void setConditionRangeTo(Double conditionRangeTo) {
            this.conditionRangeTo = conditionRangeTo;
        }

        /**
         * get value of <b>rewardFormatId</b>.
         * 
         * @return the rewardFormatId
         */
        public Integer getRewardFormatId() {
            return rewardFormatId;
        }

        /**
         * Set value to <b>rewardFormatId</b>.
         * 
         * @param rewardFormatId
         *            the rewardFormatId to set
         */
        public void setRewardFormatId(Integer rewardFormatId) {
            this.rewardFormatId = rewardFormatId;
        }

        /**
         * get value of <b>rewardPercent</b>.
         * 
         * @return the rewardPercent
         */
        public Double getRewardPercent() {
            return rewardPercent;
        }

        /**
         * Set value to <b>rewardPercent</b>.
         * 
         * @param rewardPercent
         *            the rewardPercent to set
         */
        public void setRewardPercent(Double rewardPercent) {
            this.rewardPercent = rewardPercent;
        }

        /**
         * get value of <b>rewardValue</b>.
         * 
         * @return the rewardValue
         */
        public Double getRewardValue() {
            return rewardValue;
        }

        /**
         * Set value to <b>rewardValue</b>.
         * 
         * @param rewardValue
         *            the rewardValue to set
         */
        public void setRewardValue(Double rewardValue) {
            this.rewardValue = rewardValue;
        }

        /**
         * get value of <b>productGroupIndex</b>.
         * 
         * @return the productGroupIndex
         */
        public Integer getProductGroupIndex() {
            return productGroupIndex;
        }

        /**
         * Set value to <b>productGroupIndex</b>.
         * 
         * @param productGroupIndex
         *            the productGroupIndex to set
         */
        public void setProductGroupIndex(Integer productGroupIndex) {
            this.productGroupIndex = productGroupIndex;
        }
        
        /**
         * get value of <b>productVariationId</b>.
         * @return the productVariationId
         */
        public Integer getProductVariationId() {
            return productVariationId;
        }

        /**
         * Set value to <b>productVariationId</b>.
         * @param productVariationId the productVariationId to set
         */
        public void setProductVariationId(Integer productVariationId) {
            this.productVariationId = productVariationId;
        }

        @Override
        public String toString() {
            return "LimitationItem [conditionFixValue=" + conditionFixValue + ", conditionRangeFrom=" + conditionRangeFrom + ", conditionRangeTo="
                    + conditionRangeTo + ", rewardFormatId=" + rewardFormatId + ", rewardPercent=" + rewardPercent + ", rewardValue=" + rewardValue
                    + ", productGroupIndex=" + productGroupIndex + "]";
        }

    }
    
    public static class RewardProduct implements Serializable {
        
        private static final long serialVersionUID = -310542755632317821L;

        private Integer type = PromotionLimitationItemRewardProduct.TYPE_AND;
        
        private Integer productVariationId;
        
        private Integer unitAmount;
        
        private Integer packingAmount = 0;

        /**
         * get value of <b>type</b>.
         * @return the type
         */
        public Integer getType() {
            return type;
        }

        /**
         * Set value to <b>type</b>.
         * @param type the type to set
         */
        public void setType(Integer type) {
            this.type = type;
        }

        /**
         * get value of <b>productVariationId</b>.
         * @return the productVariationId
         */
        public Integer getProductVariationId() {
            return productVariationId;
        }

        /**
         * Set value to <b>productVariationId</b>.
         * @param productVariationId the productVariationId to set
         */
        public void setProductVariationId(Integer productVariationId) {
            this.productVariationId = productVariationId;
        }

        /**
         * get value of <b>unitAmount</b>.
         * @return the unitAmount
         */
        public Integer getUnitAmount() {
            return unitAmount;
        }

        /**
         * Set value to <b>unitAmount</b>.
         * @param unitAmount the unitAmount to set
         */
        public void setUnitAmount(Integer unitAmount) {
            this.unitAmount = unitAmount;
        }

        /**
         * get value of <b>packingAmount</b>.
         * @return the packingAmount
         */
        public Integer getPackingAmount() {
            return packingAmount;
        }

        /**
         * Set value to <b>packingAmount</b>.
         * @param packingAmount the packingAmount to set
         */
        public void setPackingAmount(Integer packingAmount) {
            this.packingAmount = packingAmount;
        }

        @Override
        public String toString() {
            return "RewardProduct [type=" + type + ", productVariationId=" + productVariationId + ", unitAmount=" + unitAmount + ", packingAmount="
                    + packingAmount + "]";
        }

    }
}
