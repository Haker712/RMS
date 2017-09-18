package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Dell on 9/15/2017.
 */

public class ActivationRequestData {

    @SerializedName("tablet_id")
    @Expose
    String tablet_id;
    @SerializedName("tablet_activation_key")
    @Expose
    String tablet_activation_key;

    public String getTablet_id() {
        return tablet_id;
    }

    public void setTablet_id(String tablet_id) {
        this.tablet_id = tablet_id;
    }

    public String getTablet_activation_key() {
        return tablet_activation_key;
    }

    public void setTablet_activation_key(String tablet_activation_key) {
        this.tablet_activation_key = tablet_activation_key;
    }
}
