package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by kyawminlwin on 7/27/16.
 */
public class JSONSyncsResponse {
    private JSONResponseCategory category;
    private JSONResponseItem items;
    private JSONResponseAddOn addon;
    private JSONResponseMember member;
    private JSONResponseFavourites favourites;
    private JSONResponseSetMenu set_menu;
    private JSONResponseSetItem set_item;
    private JSONResponseRoom room;
    private JSONResponseTable table;
    private JSONResponseBooking booking;
    private JSONResponseConfig config;
    private JSONResponsePromotion promotion;
    private JSONResponseKitchen kitchen;
    private JSONResponsePromotionItem promotion_item;

    public JSONResponseCategory getCategory() {
        return category;
    }

    public JSONResponseItem getItems() {
        return items;
    }

    public JSONResponseAddOn getAddon() {
        return addon;
    }

    public JSONResponseMember getMember() {
        return member;
    }

    public JSONResponseFavourites getFavourites() {
        return favourites;
    }

    public JSONResponseSetMenu getSet_menu() {
        return set_menu;
    }

    public JSONResponseSetItem getSet_item() {
        return set_item;
    }

    public JSONResponseRoom getRoom() {
        return room;
    }

    public JSONResponseTable getTable() {
        return table;
    }

    public JSONResponseBooking getBooking() {
        return booking;
    }

    public JSONResponseConfig getConfig() {
        return config;
    }

    public JSONResponsePromotion getPromotion() {
        return promotion;
    }

    public JSONResponseKitchen getKitchen() {
        return kitchen;
    }

    public JSONResponsePromotionItem getPromotion_item() {
        return promotion_item;
    }
}
