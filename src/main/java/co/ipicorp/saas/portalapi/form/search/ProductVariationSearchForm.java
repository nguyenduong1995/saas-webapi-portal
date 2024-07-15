package co.ipicorp.saas.portalapi.form.search;

import java.io.Serializable;
import java.util.List;

public class ProductVariationSearchForm implements Serializable {

    private static final long serialVersionUID = 7982771981104194859L;
    
    private Integer segment = 0;
    
    private Integer offset = 10;
    
    private Integer categoryIdLv0;

    private Integer categoryIdLv1;

    private Integer categoryIdLv2;
    
    private Integer status;
    
    private Integer brandId;
    
    private String keyword;
    
    private List<Integer> productVariationIds;

    public Integer getSegment() {
        return segment;
    }

    public void setSegment(Integer segment) {
        this.segment = segment;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getCategoryIdLv0() {
        return categoryIdLv0;
    }

    public void setCategoryIdLv0(Integer categoryIdLv0) {
        this.categoryIdLv0 = categoryIdLv0;
    }

    public Integer getCategoryIdLv1() {
        return categoryIdLv1;
    }

    public void setCategoryIdLv1(Integer categoryIdLv1) {
        this.categoryIdLv1 = categoryIdLv1;
    }

    public Integer getCategoryIdLv2() {
        return categoryIdLv2;
    }

    public void setCategoryIdLv2(Integer categoryIdLv2) {
        this.categoryIdLv2 = categoryIdLv2;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getBrandId() {
        return brandId;
    }

    public void setBrandId(Integer brandId) {
        this.brandId = brandId;
    }

    public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public List<Integer> getProductVariationIds() {
		return productVariationIds;
	}

	public void setProductVariationIds(List<Integer> productVariationIds) {
		this.productVariationIds = productVariationIds;
	}

	@Override
	public String toString() {
		return "ProductVariationSearchForm [segment=" + segment + ", offset=" + offset + ", categoryIdLv0="
				+ categoryIdLv0 + ", categoryIdLv1=" + categoryIdLv1 + ", categoryIdLv2=" + categoryIdLv2 + ", status="
				+ status + ", brandId=" + brandId + ", keyword=" + keyword + ", productVariationIds="
				+ productVariationIds + "]";
	}
    
}
