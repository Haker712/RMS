package com.aceplus.rmsproject.rmsproject.object;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kyawminlwin on 7/14/16.
 */
public class Download_Config {
    String tax;
    String service;
    String booking_warning_time;
    String booking_waiting_time;
    String booking_service_time;
    @SerializedName("room_charge")
    @Expose
    int room_charge;
    String restaurant_name;
    String logo;
    String mobile_logo;
    String email;
    String website;
    String phone;
    String address;
    String message;
    String remark;
    String mobile_image;

    public String getTax() {
        return tax;
    }

    public String getService() {
        return service;
    }

    public String getBooking_warning_time() {
        return booking_warning_time;
    }

    public String getBooking_waiting_time() {
        return booking_waiting_time;
    }

    public String getBooking_service_time() {
        return booking_service_time;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public String getLogo() {
        return logo;
    }

    public String getMobile_logo() {
        return mobile_logo;
    }

    public String getEmail() {
        return email;
    }

    public String getWebsite() {
        return website;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddress() {
        return address;
    }

    public String getMessage() {
        return message;
    }

    public String getRemark() {
        return remark;
    }

    public String getMobile_image() {
        return mobile_image;
    }

    public int getRoom_charge() {
        return room_charge;
    }
}
