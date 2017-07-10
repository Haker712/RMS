package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by PhyoKyawSwar on 7/6/17.
 */

public class InvoiceDetailProductSetItem /*extends ArrayList<InvoiceDetailProductSetItem>*/ {

    String ItemId;
    String StatusId;
    String SetMenuId;

    public String getItemId() {
        return ItemId;
    }

    public void setItemId(String itemId) {
        ItemId = itemId;
    }

    public String getStatusId() {
        return StatusId;
    }

    public void setStatusId(String statusId) {
        StatusId = statusId;
    }

    public String getSetMenuId() {
        return SetMenuId;
    }

    public void setSetMenuId(String setMenuId) {
        SetMenuId = setMenuId;
    }
}
