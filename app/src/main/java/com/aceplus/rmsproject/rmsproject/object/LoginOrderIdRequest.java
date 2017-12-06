package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by phonelin on 12/4/17.
 */

public class LoginOrderIdRequest {

    int tablet_generated_id;
    int order_id;

    public int getTablet_generated_id() {
        return tablet_generated_id;
    }

    public void setTablet_generated_id(int tablet_generated_id) {
        this.tablet_generated_id = tablet_generated_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }
}
