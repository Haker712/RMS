package com.aceplus.rmsproject.rmsproject.object;


/**
 * Created by kyawminlwin on 7/14/16.
 */
public class JSONResponseCategory {
    private Download_Category[] category;

    public Download_Category[] getCategory() {
        return category;
    }

    private Download_SetMenu[] set_menu;

    private Download_SetItem[] set_item;

    public void setCategory(Download_Category[] category) {
        this.category = category;
    }

    public Download_SetMenu[] getSet_menu() {
        return set_menu;
    }

    public void setSet_menu(Download_SetMenu[] set_menu) {
        this.set_menu = set_menu;
    }

    public Download_SetItem[] getSet_item() {
        return set_item;
    }

    public void setSet_item(Download_SetItem[] set_item) {
        this.set_item = set_item;
    }
}
