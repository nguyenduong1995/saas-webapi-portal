package co.ipicorp.saas.portalapi.form.validator;

import java.io.Serializable;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import co.ipicorp.saas.nrms.model.Category;
import co.ipicorp.saas.nrms.service.CategoryService;
import co.ipicorp.saas.nrms.web.form.ProductForm;
import co.ipicorp.saas.nrms.web.util.ErrorCode;
import co.ipicorp.saas.portalapi.form.CategoryUpdateForm;
import grass.micro.apps.web.form.validator.AbstractFormValidator;

@Component
public class CategoryExistedValidator extends AbstractFormValidator {

    @Override
    public boolean support(Serializable form) {
        return form instanceof ProductForm || form instanceof CategoryUpdateForm;
    }

    @Autowired
    private CategoryService categoryService;

    @Override
    public boolean doValidate(Serializable form, Errors errors) {
    	if (form instanceof ProductForm) {
    		ProductForm productForm = (ProductForm) form;
            return this.validateCategoriesByIds(productForm, errors);
    	} else {
    		CategoryUpdateForm categoryForm = (CategoryUpdateForm) form;
            return this.validateCategoriesById(categoryForm, errors);
    	}
        
    }

    private boolean validateCategoriesByIds(ProductForm productForm, Errors errors) {
        if (CollectionUtils.isNotEmpty(productForm.getCategroyIds())) {
            for (Integer categoryId : productForm.getCategroyIds()) {
                Category category = this.categoryService.get(categoryId);
                if (category == null) {
                    errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST, 
                            new Object[] { "Category Id", categoryId }, 
                            ErrorCode.APP_1401_FIELD_NOT_EXIST);
                }
            }
        }
        return !errors.hasErrors();
    }
    
    private boolean validateCategoriesById(CategoryUpdateForm categoryForm, Errors errors) {
        if (categoryForm.getId() != null) {
            Category category = this.categoryService.get(categoryForm.getId());
            if (category == null) {
                errors.reject(ErrorCode.APP_1401_FIELD_NOT_EXIST, 
                        new Object[] { "Category Id", categoryForm.getId() }, 
                        ErrorCode.APP_1401_FIELD_NOT_EXIST);
            }
        }
        
        return !errors.hasErrors();
    }
}
