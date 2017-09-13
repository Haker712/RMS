package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kyawminlwin on 7/14/16.
 */
public class Download_Item {
    String id;
    String name;
    String image;
    String price;
    String status;
    String category_id;
    String mobile_image;

    @SerializedName("continent_id")
    @Expose
    int contiment_id;
    String group_id;
    int isdefault;
    @SerializedName("has_continent")
    @Expose
    int has_contiment;


    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getImage() {
        return image;
    }

    public String getPrice() {
        return price;
    }

    public String getStatus() {
        return status;
    }

    public String getCategory_id() {
        return category_id;
    }

    public String getMobile_image() {
        return mobile_image;
    }

    public int getContiment_id() {
        return contiment_id;
    }

    public void setContiment_id(int contiment_id) {
        this.contiment_id = contiment_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public int getIsdefault() {
        return isdefault;
    }

    public void setIsdefault(int isdefault) {
        this.isdefault = isdefault;
    }

    public int getHas_contiment() {
        return has_contiment;
    }

    public void setHas_contiment(int has_contiment) {
        this.has_contiment = has_contiment;
    }
}
