package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by PhoneLinAung on 9/11/2017.
 */

public class JSONResponseContiment {

    @SerializedName("continents")
    @Expose
    private List<Download_Contiment> contiments = null;

    public List<Download_Contiment> getContiments() {
        return contiments;
    }

    public void setContinents(List<Download_Contiment> contiments) {
        this.contiments = contiments;
    }

}
