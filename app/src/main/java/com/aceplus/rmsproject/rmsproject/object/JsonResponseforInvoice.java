package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by PhyoKyawSwar on 12/23/16.
 */
public class JsonResponseforInvoice {
    @SerializedName("Data")
    @Expose
    private ArrayList<Download_forInvoice> forInvoices;

    public ArrayList<Download_forInvoice> getForInvoices() {
        return forInvoices;
    }

    public void setForInvoices(ArrayList<Download_forInvoice> forInvoices) {
        this.forInvoices = forInvoices;
    }
}
