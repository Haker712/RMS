package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by kyawminlwin on 7/28/16.
 */
public class JsonResponseSyncs {
    private Download_Category[] category;
    private Download_Item[] items;
    private Download_AddOn[] addon;
    private Download_Member[] member;
    private Download_SetMenu[] set_menu;
    private Download_SetItem[] set_item;
    private Download_Room[] room;
    private Download_Table[] table;
    private Download_Booking[] booking;
    private Download_Promotion[] promotion;
    private Download_Promotion_Item[]  promotion_item;
    private Download_Discount[] discount;
    private Download_Config[] config;
    private Download_Contiment[] continent;
    private Download_ShiftCategory[] shift_category;
    private Download_ShiftSetmenu[] shift_setmenu;

    public Download_Category[] getCategory() {
        return category;
    }

    public Download_Item[] getItems() {
        return items;
    }

    public Download_AddOn[] getAddon() {
        return addon;
    }

    public Download_Member[] getMember() {
        return member;
    }

    public Download_SetMenu[] getSet_menu() {
        return set_menu;
    }

    public Download_SetItem[] getSet_item() {
        return set_item;
    }

    public Download_Room[] getRoom() {
        return room;
    }

    public Download_Table[] getTable() {
        return table;
    }

    public Download_Booking[] getBooking() {
        return booking;
    }

    public Download_Config[] getConfig() {
        return config;
    }

    public Download_Promotion[] getPromotion() {
        return promotion;
    }

    public Download_Promotion_Item[] getPromotion_item() {
        return promotion_item;
    }

    public Download_Discount[] getDiscount() {
        return discount;
    }

    public Download_Contiment[] getContiments() {
        return continent;
    }

    public Download_ShiftCategory[] getShift_category() {
        return shift_category;
    }

    public Download_ShiftSetmenu[] getShift_setmenu() {
        return shift_setmenu;
    }
}
