package com.aceplus.rmsproject.rmsproject.utils;

import com.aceplus.rmsproject.rmsproject.object.ActivateKey;
import com.aceplus.rmsproject.rmsproject.object.Download_orderroom;
import com.aceplus.rmsproject.rmsproject.object.Download_ordertable;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseAddOn;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseBooking;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseCategory;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseConfig;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseContiment;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseDiscount;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseItem;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseKitchen;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseMember;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseOrderStatus;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseRoom;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseSetItem;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseSetMenu;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseTable;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseTableVersion;
import com.aceplus.rmsproject.rmsproject.object.JsonResponseSyncs;
import com.aceplus.rmsproject.rmsproject.object.JsonResponseforInvoice;
import com.aceplus.rmsproject.rmsproject.object.JsonResponseforInvoiceDetail;
import com.aceplus.rmsproject.rmsproject.object.Login;
import com.aceplus.rmsproject.rmsproject.object.LoginOrderIdRequest;
import com.aceplus.rmsproject.rmsproject.object.Success;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by kyawminlwin on 7/14/16.
 */
public interface RequestInterface {

    @FormUrlEncoded
    @POST("api/v1/category")
    Call<JSONResponseCategory> getJSON(@Field("site_activation_key") String activate_key);

    @GET("api/v1/order_status")
    Call<JSONResponseOrderStatus> getOrderStatus();


    @FormUrlEncoded
    @POST("api/v1/item")
    Call<JSONResponseItem> getItem(@Field("site_activation_key") String activate_key);


    @FormUrlEncoded
    @POST("api/v1/continent")
    Call<JSONResponseContiment> getContiment(@Field("site_activation_key") String activate_key);

    @FormUrlEncoded
    @POST("api/v1/room")
    Call<JSONResponseRoom> getRoom(@Field("site_activation_key") String activate_key);

    @GET("api/v1/kitchen")
    Call<JSONResponseKitchen> getKitchen();

    @FormUrlEncoded
    @POST("api/v1/booking")
    Call<JSONResponseBooking> getBooking(@Field("site_activation_key") String activate_key);

    @FormUrlEncoded
    @POST("api/v1/discount")
    Call<JSONResponseDiscount> getDiscount(@Field("site_activation_key") String activate_key);

    @GET("api/v1/member")
    Call<JSONResponseMember> getMember();

    @FormUrlEncoded
    @POST("api/v1/table")
    Call<JSONResponseTable> getTable(@Field("site_activation_key") String activate_key);

    @GET("api/v1/config")
    Call<JSONResponseConfig> getConfig();

    @FormUrlEncoded
    @POST("api/v1/set_menu")
    Call<JSONResponseSetMenu> getSet_Menu(@Field("site_activation_key") String activate_key);

    @FormUrlEncoded
    @POST("api/v1/set_item")
    Call<JSONResponseSetItem> getSet_Item(@Field("site_activation_key") String activate_key);

    @FormUrlEncoded
    @POST("api/v1/addon")
    Call<JSONResponseAddOn> getAddon(@Field("site_activation_key") String activate_key);

    @FormUrlEncoded
    @POST("api/v1/syncs_table")
    Call<JSONResponseTableVersion> getSyncsTable(@Field("site_activation_key") String activate_key);

    @FormUrlEncoded
    @POST("api/v1/download_voucher")
    Call<JsonResponseforInvoice> getforInvoice(@Field("site_activation_key") String activate_key);

    @FormUrlEncoded
    @POST("api/v1/download_voucher_detail")
    Call<JsonResponseforInvoiceDetail> getforInvoiceDetail(@Field("site_activation_key") String activate_key,
                                                           @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("api/v1/login")
    Call<Login> createTask(@Field("username") String username,
                           @Field("password") String password,
                           @Field("site_activation_key") String activate_key);

    @FormUrlEncoded
    @POST("api/v1/create_voucher")
    Call<Success> postOrderInvoice(@Field("orderID") String orderJson);

    @FormUrlEncoded
    @POST("api/v1/add_new_to_voucher")
    Call<Success> postOrderAddInvoice(@Field("orderID") String orderJson);

    @FormUrlEncoded
    @POST("api/v1/invoice/member")
    Call<Success> createMember(@Field("voucher_no") String order_id,
                               @Field("member_id") String member_id,
                               @Field("total_amount") double total_amount,
                               @Field("tax_amount") double tax_amount,
                               @Field("service_amount") double service_amount,
                               @Field("foc_amount") double foc_amount,
                               @Field("all_total_amount") double net_amount,
                               @Field("foc_description") String foc_description,
                               @Field("pay_amount") double pay_amount,
                               @Field("refund") double refund,
                               @Field("discount_amount") double discount_amount
    );


    @FormUrlEncoded
    @POST("api/v1/order_table")
    Call<Download_ordertable> getOrderTable(@Field("site_activation_key") String activate_key,
                                            @Field("table_id") String table_id);

    @FormUrlEncoded
    @POST("api/v1/order_room")
    Call<Download_orderroom> getOrderRoom(@Field("site_activation_key") String activate_key,
                                          @Field("room_id") String room_id);

    @FormUrlEncoded
    @POST("api/v1/download_order_table_with_order_id")
    Call<JsonForShowTableId> getforshowOrderTable(@Field("site_activation_key") String activate_key,
                                                  @Field("order_id") String orderID
    );

    @FormUrlEncoded
    @POST("api/v1/download_order_room_with_order_id")
    Call<JsonForShowRoomId> getforshowOrderRoom(@Field("site_activation_key") String activate_key,
                                                @Field("order_id") String orderID
    );

    @FormUrlEncoded
    @POST("api/v1/table_status")
    Call<Success> postTableStatus(@Field("table") String table);

    @FormUrlEncoded
    @POST("api/v1/room_status")
    Call<Success> postRoomStatus(@Field("room") String room);


    @FormUrlEncoded
    @POST("api/v1/table_transfer")
    Call<Success> postTableTransfer(@Field("order_id") String order_id,
                                    @Field("transfer_from_table_id") String transfer_from_table_id,
                                    @Field("transfer_to_table_id") String transfer_to_table_id);

    @FormUrlEncoded
    @POST("api/v1/room_transfer")
    Call<Success> postRoomTransfer(@Field("order_id") String order_id,
                                   @Field("transfer_from_room_id") String transfer_from_room_id,
                                   @Field("transfer_to_room_id") String transfer_to_room_id);

    @FormUrlEncoded
    @POST("api/v1/check_cancel_status")
    Call<Success> postCancel(@Field("cancel") String cancel);

    @FormUrlEncoded
    @POST("api/v1/take")
    Call<Success> postTake(@Field("take") String take);

    @FormUrlEncoded
    @POST("api/v1/post_kitchen_cancel")
    Call<Success> postKitchenCancel(@Field("kitchen_cancel") String take);

    @FormUrlEncoded
    @POST("api/v1/customer_cancel")
    Call<Success> postCustomerCancle(@Field("customer_cancel") String customer_cancel);

    @FormUrlEncoded
    @POST("api/v1/syncs")
    Call<JsonResponseSyncs> getUpdateData(@Field("category") int category,
                                          @Field("items") int items,
                                          @Field("add_on") int extra,
                                          @Field("members") int members,
                                          @Field("set_menu") int sub_menu,
                                          @Field("set_item") int sub_item,
                                          @Field("rooms") int rooms,
                                          @Field("tables") int tables,
                                          @Field("booking") int booking,
                                          @Field("config") int config,
                                          @Field("promotions") int promotions,
                                          @Field("promotion_items") int promotion_items,
                                          @Field("discount") int discount,
                                          @Field("site_activation_key") String activate_key);


    @FormUrlEncoded
    @POST("api/activate")
    Call<ActivateKey> activateKey(@Field("param_data") String id);

    @FormUrlEncoded
    @POST("api/v1/first_time_login")
    Call<LoginOrderIdRequest> LOGIN_ORDER_ID_REQUEST_CALL(@Field("tabletId") String username,
                                                          @Field("site_activation_key") String activate_key);
}
