package com.tracking.controllers.tld;

import com.tracking.lang.Language;
import com.tracking.models.Category;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import java.io.IOException;
import java.util.List;
import java.util.StringJoiner;

/**
 * Tag class, that responsible for building string of categories on Activities page
 */
public class CategoriesForActivitiesTag extends TagSupport {

    private List<Category> categoryList;
    private List<Integer> activityCategoriesList;
    private String lang;

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<Category> categoryList) {
        this.categoryList = categoryList;
    }

    public List<Integer> getActivityCategoriesList() {
        return activityCategoriesList;
    }

    public void setActivityCategoriesList(List<Integer> activityCategoriesList) {
        this.activityCategoriesList = activityCategoriesList;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    @Override
    public int doStartTag() throws JspException {
        try {
            StringJoiner categories = new StringJoiner(", ");
            if (activityCategoriesList == null || activityCategoriesList.isEmpty()) {
                for (Category category : categoryList) {
                    if (category.getId() == 0) {
                        if (lang.equals(Language.UA.getValue()))
                            categories.add(category.getNameUA());
                        else if (lang.equals(Language.EN.getValue()))
                            categories.add(category.getNameEN());
                        break;
                    }
                }
            } else {
                for (Category category : categoryList) {
                    if (activityCategoriesList.contains(category.getId())) {
                        if (lang.equals(Language.UA.getValue()))
                            categories.add(category.getNameUA());
                        else if (lang.equals(Language.EN.getValue()))
                            categories.add(category.getNameEN());
                    }
                }
            }
            pageContext.getOut().write(categories.toString());
        } catch (IOException e) {
            e.printStackTrace();
            throw new JspException(e);
        }
        return super.doStartTag();
    }
}

