package com.aceplus.rmsproject.rmsproject.object;

/**
 * Created by kyawminlwin on 8/1/16.
 */
public class BookingTable {
    String tableID;
    String tableService="0";
    String table_no;
    boolean table_check = false;
    String BookingID;
    String table_id;
    String room_id;
    String booking_time = "00:00:00";
    String booking_warning;
    String booking_waiting;
    String booking_service;
    String tableStatus = "0" ;//0 = availabe, 1 = service , 2 = warning ,  3 = waiting
    String backgroundColor = "#8dc63f";

    public String getTableService() {
        return tableService;
    }

    public void setTableService(String tableService) {
        this.tableService = tableService;
    }

    public String getBackgroundColor() {
        return backgroundColor;
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public String getTableStatus() {
        return tableStatus;
    }

    public void setTableStatus(String tableStatus) {
        this.tableStatus = tableStatus;
    }

    public String getTableID() {
        return tableID;
    }

    public void setTableID(String tableID) {
        this.tableID = tableID;
    }

    public String getTable_no() {
        return table_no;
    }

    public void setTable_no(String table_no) {
        this.table_no = table_no;
    }

    public boolean isTable_check() {
        return table_check;
    }

    public void setTable_check(boolean table_check) {
        this.table_check = table_check;
    }

    public String getBookingID() {
        return BookingID;
    }

    public void setBookingID(String bookingID) {
        BookingID = bookingID;
    }

    public String getTable_id() {
        return table_id;
    }

    public void setTable_id(String table_id) {
        this.table_id = table_id;
    }

    public String getRoom_id() {
        return room_id;
    }

    public void setRoom_id(String room_id) {
        this.room_id = room_id;
    }

    public String getBooking_time() {
        return booking_time;
    }

    public void setBooking_time(String booking_time) {
        this.booking_time = booking_time;
    }

    public String getBooking_warning() {
        return booking_warning;
    }

    public void setBooking_warning(String booking_warning) {
        this.booking_warning = booking_warning;
    }

    public String getBooking_waiting() {
        return booking_waiting;
    }

    public void setBooking_waiting(String booking_waiting) {
        this.booking_waiting = booking_waiting;
    }

    public String getBooking_service() {
        return booking_service;
    }

    public void setBooking_service(String booking_service) {
        this.booking_service = booking_service;
    }
}
