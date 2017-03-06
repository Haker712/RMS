package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by PhyoKyawSwar on 12/26/16.
 */
public class JsonResponseforInvoiceDetail {

    //private Download_ForInvoiceDetail[] forInvoiceDetails;
    @SerializedName("Data")
    @Expose

    private ArrayList<Download_ForInvoiceDetail> download_forInvoiceDetailArrayList;

    public ArrayList<Download_ForInvoiceDetail> getDownload_forInvoiceDetailArrayList() {
        return download_forInvoiceDetailArrayList;
    }

    public void setDownload_forInvoiceDetailArrayList(ArrayList<Download_ForInvoiceDetail> download_forInvoiceDetailArrayList) {
        this.download_forInvoiceDetailArrayList = download_forInvoiceDetailArrayList;
    }
}
