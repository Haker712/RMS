package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by kyawminlwin on 7/14/16.
 */
public class Download_Booking {
    String id;
    String customer_name;
    String from_time;
    String capacity;
    String to_time;
    BTable booking_table [];
    BRoom booking_room [];

    public String getId() {
        return id;
    }

    public String getCustomer_name() {
        return customer_name;
    }
    public String getFrom_time() {
        return from_time;
    }

    public String getCapacity(){

        return capacity;

    }

    public String getTo_time() {
        return to_time;
    }

    public BTable[] getBooking_table() {
        return booking_table;
    }

    public BRoom[] getBooking_room() {
        return booking_room;
    }
}
