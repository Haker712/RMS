package com.aceplus.rmsproject.rmsproject;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aceplus.rmsproject.rmsproject.object.ActivateKey;
import com.aceplus.rmsproject.rmsproject.object.BRoom;
import com.aceplus.rmsproject.rmsproject.object.BTable;
import com.aceplus.rmsproject.rmsproject.object.Download_AddOn;
import com.aceplus.rmsproject.rmsproject.object.Download_Booking;
import com.aceplus.rmsproject.rmsproject.object.Download_Category;
import com.aceplus.rmsproject.rmsproject.object.Download_Config;
import com.aceplus.rmsproject.rmsproject.object.Download_Discount;
import com.aceplus.rmsproject.rmsproject.object.Download_Item;
import com.aceplus.rmsproject.rmsproject.object.Download_Member;
import com.aceplus.rmsproject.rmsproject.object.Download_OrderStatus;
import com.aceplus.rmsproject.rmsproject.object.Download_Promotion;
import com.aceplus.rmsproject.rmsproject.object.Download_Promotion_Item;
import com.aceplus.rmsproject.rmsproject.object.Download_Room;
import com.aceplus.rmsproject.rmsproject.object.Download_SetItem;
import com.aceplus.rmsproject.rmsproject.object.Download_SetMenu;
import com.aceplus.rmsproject.rmsproject.object.Download_Table;
import com.aceplus.rmsproject.rmsproject.object.Download_TableVersion;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseDiscount;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseOrderStatus;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseTableVersion;
import com.aceplus.rmsproject.rmsproject.object.JsonResponseSyncs;
import com.aceplus.rmsproject.rmsproject.utils.Database;
import com.aceplus.rmsproject.rmsproject.utils.RequestInterface;
import com.aceplus.rmsproject.rmsproject.utils.RetrofitService;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HomePageActivity extends ActionBarActivity {
    private Button takeawayBtn;
    private Button tableBtn;
    private Button roomBtn;
    private Button invoiceBtn;
    private Button messageBtn;
    private Button syncBtn;
    private Retrofit retrofit;
    private ArrayList<Download_OrderStatus> download_orderStatusArrayList;
    SQLiteDatabase database;

    String waiterName;
    String waiterId;
    String waiterRole;


    ProgressDialog progressDialog;
    private ArrayList<Download_Category> download_categoryArrayList;
    private ArrayList<Download_Item> download_itemArrayList;
    private ArrayList<Download_AddOn> download_addOnArrayList;
    private ArrayList<Download_Member> download_memberArrayList;
    private ArrayList<Download_SetMenu> download_setMenuArrayList;
    private ArrayList<Download_SetItem> download_setItemArrayList;
    private ArrayList<Download_Room> download_roomArrayList;
    private ArrayList<Download_Table> download_tableArrayList;
    private ArrayList<Download_Booking> download_bookingArrayList;
    private ArrayList<Download_Config> download_configArrayList;
    private ArrayList<Download_Promotion> download_promotionArrayList;
    private ArrayList<Download_Promotion_Item> download_promotion_itemArrayList;
    private ArrayList<Download_Discount> download_discountArrayList;
    private ArrayList<Download_TableVersion> download_tableVersionArrayList;
    //    public static final String URL = "http://192.168.11.57:9090";
    //public static  String URL = "http://192.168.11.62:8800";
    //i/me/my/mine//
    //public static String URL = "http://192.168.11.57:8900";
    //public static String URL = "http://192.168.11.62:8019";
    //public static String URL = "http://192.168.195.1:8900";
    //public static String URL = "http://192.168.11.180:8080";
    //public static String URL = "http://192.168.11.57:8900";
    //public static String URL = "http://192.168.195.1:8900";
    public static String URL = "";
    //    public static String ACTIVATED_URL = "http://aceplusactivation.com";
    public static String IMG_URL_PREFIX = "http://192.168.11.201:8080/uploads/";
    //public static String URL = "http://192.168.7.176:8080";

    //  public static String URL = "http://192.168.137.1:8080";


    // public static String URL = "http://10.42.0.1:8080";
    // public static String URL = "http://192.168.7.175:8080";

    SharedPreferences sharedpreferences;
    public static final String LOGIN_PREFERENCES = "Login";
    public static final String WAITER_ID = "waiter_id";
    public static boolean userLogin = false;

    ActivateKey activateKey = new ActivateKey();

    String activationKey;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    String Waitername = "";
    String UserRole = "";

    public static String tablet_id;

    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        activity = this;


        waiterName = getIntent().getStringExtra("WaiterName");
        waiterId = getIntent().getStringExtra("WaiterId");
        waiterRole = getIntent().getStringExtra("UserRole");
        final Button homeMenuBtn = (Button) findViewById(R.id.home_menu_btn);
        homeMenuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(HomePageActivity.this, homeMenuBtn);
                //Inflating the Popup using xml file
                popup.getMenuInflater()
                        .inflate(R.menu.popup_menu, popup.getMenu());

                //registering popup with OnMenuItemClickListener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {

                        if (item.getTitle().equals("UserProfile")) {

                            final AlertDialog builder = new AlertDialog.Builder(HomePageActivity.this, R.style.InvitationDialog)
                                    .setTitle("User Info")
                                    .create();
                            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View view = layoutInflater.inflate(R.layout.userinfo_dialog, null);
                            TextView txtwaiterName, txtwaiterId, txtwaiterRole;

                            txtwaiterName = (TextView) view.findViewById(R.id.waiter_name);
                            txtwaiterId = (TextView) view.findViewById(R.id.waiter_id);
                            txtwaiterRole = (TextView) view.findViewById(R.id.waiter_role);

//                            txtwaiterName.setText(waiterName);
//                            txtwaiterId.setText(waiterId);
//                            txtwaiterRole.setText(waiterRole);

                            builder.setView(view);
                            builder.show();


                        } else {
                            onBackPressed();
                        }

                        return true;
                    }
                });

                popup.show(); //showing popup menu
            }
        });

        try {
            database = new Database(HomePageActivity.this).getDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("X-Authorization", getActivateKeyFromDB()).build();
                return chain.proceed(newRequest);
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        builder.readTimeout(120, TimeUnit.SECONDS);
        builder.connectTimeout(120, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(60000);
                        runOnUiThread(new Runnable() {
                            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                            @Override
                            public void run() {
                                loadOrderStatusJson();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };
        t.start();
        registerIDs();
        catchEvents();
    }

    private String getActivateKeyFromDB() { // for activation key
        database.beginTransaction();
        String backend_activate_key = null;
        Cursor cur = database.rawQuery("SELECT * FROM activate_key", null);
        while (cur.moveToNext()) {
            backend_activate_key = cur.getString(cur.getColumnIndex("backend_activation_key"));
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return backend_activate_key;
    }

    private void registerIDs() {
        takeawayBtn = (Button) findViewById(R.id.take_away_btn);
        tableBtn = (Button) findViewById(R.id.table_btn);
        roomBtn = (Button) findViewById(R.id.room_btn);
        invoiceBtn = (Button) findViewById(R.id.invoice_btn);
        messageBtn = (Button) findViewById(R.id.message_btn);
        syncBtn = (Button) findViewById(R.id.sync);

    }

    private void notificationView(int size) { // for notification in status bar
        Intent intent = new Intent(HomePageActivity.this, MessageActivity.class);
        PendingIntent pending = PendingIntent.getActivity(HomePageActivity.this, 0, intent, 0);
        Notification notifications = new Notification.Builder(HomePageActivity.this)
                .setContentTitle("Restaurant Management System")
                .setContentText("View all Message")
                .setSmallIcon(getNotificationIcon())
                .setContentIntent(pending)
                .setContentInfo(size + "").getNotification();
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        notifications.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(0, notifications);
    }

    private int getNotificationIcon() {
        boolean useWhiteIcon = (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP);
        return useWhiteIcon ? R.drawable.noti_icon : R.drawable.noti_icon;
    }

    private void loadOrderStatusJson() { //  downloading order status from back end for message activity !!
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseOrderStatus> call = request.getOrderStatus();

        call.enqueue(new Callback<JSONResponseOrderStatus>() {
            @Override
            public void onResponse(Call<JSONResponseOrderStatus> call, Response<JSONResponseOrderStatus> response) {
                try {
                    JSONResponseOrderStatus jsonResponse = response.body();
                    download_orderStatusArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getOrder_status()));
                    int size = download_orderStatusArrayList.size();
                    Log.e("KitchenMessage", size + "");
                    notificationView(size);
                } catch (Exception e) {
                    e.printStackTrace();
                    callUploadDialog("Kitchen message is null");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseOrderStatus> call, Throwable t) {
                callUploadDialog("Please upload again!");
                Log.d("ErrorCategory", t.getMessage() + "");
            }
        });
    }

    private void callUploadDialog(String message) {
        try {
            final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(HomePageActivity.this, R.style.InvitationDialog)
                    .setPositiveButton(R.string.invitation_ok, null)
                    .create();
            builder.setTitle(R.string.alert);
            builder.setMessage(message);
            builder.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface dialog) {
                    final Button btnAccept = builder.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                    btnAccept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            builder.dismiss();
                        }
                    });
                }
            });
            if (activity.isFinishing()) {
                return;
            } else {
                builder.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void catchEvents() {  // onclick are lead to related activity !!
        takeawayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CategoryActivity.TAKE_AWAY = "take";
                CategoryActivity.VOUNCHER_ID = null;
                CategoryActivity.TABLE_ID = null;
                CategoryActivity.ROOM_ID = null;
                CategoryActivity.groupTableArrayList = null;
                CategoryActivity.check_check = "null";
                CategoryActivity.ADD_INVOICE = null;
                startActivity(new Intent(HomePageActivity.this, CategoryActivity.class));
                finish();
            }
        });
        tableBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, TableActivity.class));
                finish();
            }
        });
        roomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, RoomActivity.class));
                finish();
            }
        });
        invoiceBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, InvoiceActivity.class));
                finish();
            }
        });
        messageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomePageActivity.this, MessageActivity.class));
                finish();
            }
        });
        syncBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loadSyncsTable(getVersionList());

            }
        });
    }

    @Override
    public void onBackPressed() {
        final AlertDialog builder = new AlertDialog.Builder(HomePageActivity.this, R.style.InvitationDialog)
                .setPositiveButton(R.string.invitation_ok, null)
                .setNegativeButton(R.string.invitation_cancel, null)
                .create();
        builder.setTitle(R.string.clear);
        builder.setMessage("Do you want to Exit?");
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                        startActivity(new Intent(HomePageActivity.this, MainActivity.class));
                        finish();
                    }
                });

                final Button btnCancle = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                btnCancle.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.dismiss();
                    }
                });
            }
        });
        builder.show();

    }


    private void loadSyncsTable(ArrayList<Integer> version) {  // syncs down the tables from back end !!
        callDialog("Update Data....");
        final RequestInterface request = RetrofitService.createRetrofitService(RequestInterface.class, HomePageActivity.this);
        int vCategory = 0;
        int vItem = 0;
        int vAddon = 0;
        int vMember = 0;
        int vSetMenu = 0;
        int vSetItem = 0;
        int vRoom = 0;
        int vTable = 0;
        int vBooking = 0;
        int vConfig = 0;
        int vPromotion = 0;
        int vPromotionItem = 0;
        int vDiscount = 0;
        for (int i = 0; i < version.size(); i++) {
            vCategory = version.get(0);
            Log.d("vCategory", vCategory + "");

            vItem = version.get(1);
            Log.d("vItem", vItem + "");

            vAddon = version.get(2);
            Log.d("vAddon", vAddon + "");

            vMember = version.get(3);
            Log.d("vMember", vMember + "");

            vSetMenu = version.get(4);
            Log.d("vSetMenu", vSetMenu + "");

            vSetItem = version.get(5);
            Log.d("vSetItem", vSetItem + "");

            vRoom = version.get(6);
            Log.d("vRoom", vRoom + "");

            vTable = version.get(7);
            Log.d("vTable", vTable + "");

            vBooking = version.get(8);
            Log.d("vBooking", vBooking + "");

            vConfig = version.get(9);
            Log.d("vConfig", vConfig + "");

            vPromotion = version.get(10);
            Log.d("vPromotion", vPromotion + "");

            vPromotionItem = version.get(11);
            Log.d("vPromotionItem", vPromotionItem + "");

            vDiscount = version.get(12);
            Log.d("vDiscount", vDiscount + "");
        }
        Call<JsonResponseSyncs> call = request.getUpdateData(vCategory, vItem, vAddon, vMember, vSetMenu, vSetItem, vRoom, vTable, vBooking, vConfig, vPromotion, vPromotionItem, vDiscount, getActivateKeyFromDB());
        call.enqueue(new Callback<JsonResponseSyncs>() {
            @Override
            public void onResponse(Call<JsonResponseSyncs> call, Response<JsonResponseSyncs> response) {
                Log.i("Response code>>>", response.code() + "");
                if (response.code() != 500) {
                    try {
                        JsonResponseSyncs jsonResponse = response.body();
                        progressDialog.dismiss();
                        Resources r = getResources();
                        Bitmap bm = BitmapFactory.decodeResource(r, R.drawable.img1);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                        byte[] b = baos.toByteArray();
                        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
                        database.beginTransaction();

                        download_setMenuArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getSet_menu()));
                        if (download_setMenuArrayList.size() > 0) {
                            deleteTableVersion("setMenu");
                        }
                        for (Download_SetMenu download_setMenu : download_setMenuArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("id", download_setMenu.getId());
                            cv.put("set_menu_name", download_setMenu.getSet_menus_name());
                            cv.put("set_menu_price", download_setMenu.getSet_menus_price());
                            cv.put("image", download_setMenu.getImage());
                            cv.put("status", download_setMenu.getStatus());
                            database.insert("setMenu", null, cv);
                        }
                        Log.d("Demo SetMenu", download_setMenuArrayList.size() + "");

                        if (jsonResponse.getCategory() != null) {
                            download_categoryArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getCategory()));
//                            deleteTableVersion("category");
//                            if (download_setMenuArrayList.size() > 0) {
//                                // deleteTableVersion("category");
//                                ContentValues setMenuCV = new ContentValues();
//                                setMenuCV.put("id", "set_menu");
//                                setMenuCV.put("name", "SetMenu");
//                                setMenuCV.put("status", "1");
//                                setMenuCV.put("parent_id", "0");
//                                setMenuCV.put("kitchen_id", "0");
//                                setMenuCV.put("image", "setmenu.jpg");
//                                database.insert("category", null, setMenuCV);
//                            }
                            if (download_categoryArrayList.size() > 0) {//kyi kyi ma kyi kyi delete ya ml//think again
                                deleteTableVersion("category");
                            }
                            for (Download_Category download_category : download_categoryArrayList) {
                                ContentValues cv = new ContentValues();
                                cv.put("id", download_category.getId());
                                cv.put("name", download_category.getName());
                                cv.put("status", download_category.getStatus());
                                cv.put("parent_id", download_category.getParent_id());
                                cv.put("kitchen_id", download_category.getKitchen_id());
                                cv.put("image", download_category.getImage());
                                database.insert("category", null, cv);
                            }
                        }

                        Cursor cursor = database.rawQuery("SELECT * FROM setMenu", null);


                        Cursor cursor1 = database.rawQuery("SELECT * FROM category where id='set_menu'", null);


                        if (cursor.getCount() > 0 && cursor1.getCount() != 1) {
                            ContentValues setMenuCV = new ContentValues();
                            setMenuCV.put("id", "set_menu");
                            setMenuCV.put("name", "SetMenu");
                            setMenuCV.put("status", "1");
                            setMenuCV.put("parent_id", "0");
                            setMenuCV.put("kitchen_id", "0");
                            setMenuCV.put("image", "setmenu.jpg");
                            database.insert("category", null, setMenuCV);
                        }

                        Log.d("Demo Category", download_categoryArrayList.size() + "");
                        download_itemArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getItems()));
                        if (download_itemArrayList.size() > 0) {
                            deleteTableVersion("item");
                        }
                        for (Download_Item download_item : download_itemArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("id", download_item.getId());
                            cv.put("name", download_item.getName());
                            cv.put("image", download_item.getImage());
                            cv.put("price", download_item.getPrice());
                            cv.put("status", download_item.getStatus());
                            cv.put("category_id", download_item.getCategory_id());
                            cv.put("contiment_id", download_item.getContiment_id());
                            cv.put("group_id", download_item.getGroup_id());
                            cv.put("isdefault", download_item.getIsdefault());
                            cv.put("has_contiment", download_item.getHas_contiment());

                            database.insert("item", null, cv);
                        }
                        Log.d("Demo Item", download_itemArrayList.size() + "");
                        download_addOnArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getAddon()));
                        if (download_addOnArrayList.size() > 0) {
                            deleteTableVersion("addOn");
                        }
                        for (Download_AddOn download_addOn : download_addOnArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("id", download_addOn.getId());
                            cv.put("food_name", download_addOn.getFood_name());
                            cv.put("category_id", download_addOn.getCategory_id());
                            cv.put("image", download_addOn.getImage());
                            cv.put("price", download_addOn.getPrice());
                            database.insert("addOn", null, cv);
                        }
                        Log.d("Demo Addon", download_addOnArrayList.size() + "");
                        download_memberArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getMember()));
                        if (download_memberArrayList.size() > 0) {
                            deleteTableVersion("member");
                        }
                        for (Download_Member download_member : download_memberArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("id", download_member.getId());
                            cv.put("member_card_no", download_member.getMember_card_no());
                            cv.put("discount", download_member.getDiscount_amount());
                            database.insert("member", null, cv);
                        }
                        Log.d("Demo Member", download_memberArrayList.size() + "");
                        /*download_setMenuArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getSet_menu()));
                        if (download_setMenuArrayList.size() > 0) {
                            deleteTableVersion("setMenu");
                        }
                        for (Download_SetMenu download_setMenu : download_setMenuArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("id", download_setMenu.getId());
                            cv.put("set_menu_name", download_setMenu.getSet_menus_name());
                            cv.put("set_menu_price", download_setMenu.getSet_menus_price());
                            cv.put("image", download_setMenu.getMobile_image());
                            cv.put("status", download_setMenu.getStatus());
                            database.insert("setMenu", null, cv);
                        }
                        Log.d("Demo SetMenu", download_setMenuArrayList.size() + "");*/
                        download_setItemArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getSet_item()));
                        if (download_setItemArrayList.size() > 0) {
                            deleteTableVersion("setItem");
                        }
                        for (Download_SetItem download_setItem : download_setItemArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("id", download_setItem.getId());
                            cv.put("set_menu_id", download_setItem.getSet_menu_id());
                            cv.put("item_id", download_setItem.getItem_id());
                            database.insert("setItem", null, cv);
                        }
                        Log.d("Demo SetItem", download_setItemArrayList.size() + "");
                        download_roomArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getRoom()));
                        if (download_roomArrayList.size() > 0) {
                            deleteTableVersion("room");
                        }
                        for (Download_Room download_room : download_roomArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("id", download_room.getId());
                            cv.put("room_name", download_room.getRoom_name());
                            cv.put("status", download_room.getStatus());
                            database.insert("room", null, cv);
                        }
                        Log.d("Demo Room", download_roomArrayList.size() + "");
                        download_tableArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getTable()));
                        if (download_tableArrayList.size() > 0) {
                            deleteTableVersion("tableList");
                        }
                        for (Download_Table download_table : download_tableArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("id", download_table.getId());
                            cv.put("table_no", download_table.getTable_no());
                            cv.put("status", download_table.getStatus());
                            database.insert("tableList", null, cv);
                        }
                        Log.d("Demo Table", download_tableArrayList.size() + "");
                        download_bookingArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getBooking()));
                        if (download_bookingArrayList.size() > 0) {
                            deleteTableVersion("booking");
                            deleteTableVersion("booking_table");
                            deleteTableVersion("booking_room");
                        }
                        for (Download_Booking download_booking : download_bookingArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("id", download_booking.getId());
                            cv.put("customer_name", download_booking.getCustomer_name());
                            cv.put("from_time", download_booking.getFrom_time());
                            Log.e("BTable", download_booking.getBooking_table() + "");
                            for (BTable bTable : download_booking.getBooking_table()) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("booking_id", bTable.getBooking_id());
                                contentValues.put("table_id", bTable.getTable_id());
                                database.insert("booking_table", null, contentValues);
                            }
                            for (BRoom bRoom : download_booking.getBooking_room()) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put("booking_id", bRoom.getBooking_id());
                                contentValues.put("room_id", bRoom.getRoom_id());
                                database.insert("booking_room", null, contentValues);
                            }
                            database.insert("booking", null, cv);
                        }
                        Log.d("Demo Booking", download_bookingArrayList.size() + "");

                        download_configArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getConfig()));
                        if (download_configArrayList.size() > 0) {
                            deleteTableVersion("config");
                        }
                        for (Download_Config download_config : download_configArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("tax", download_config.getTax());
                            cv.put("service", download_config.getService());
                            cv.put("booking_warning_time", download_config.getBooking_warning_time());
                            cv.put("booking_waiting_time", download_config.getBooking_waiting_time());
                            cv.put("booking_service_time", download_config.getBooking_service_time());
                            cv.put("restaurant_name", download_config.getRestaurant_name());
                            cv.put("logo", download_config.getLogo());
                            cv.put("mobile_logo", download_config.getMobile_image());
                            cv.put("email", download_config.getEmail());
                            cv.put("website", download_config.getWebsite());
                            cv.put("phone", download_config.getPhone());
                            cv.put("address", download_config.getAddress());
                            cv.put("message", download_config.getMessage());
                            cv.put("remark", download_config.getRemark());
                            cv.put("room_charge", download_config.getRoom_charge());

                            database.insert("config", null, cv);
                        }
                        Log.d("Demo Config", download_configArrayList.size() + "");
                        download_promotionArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getPromotion()));
                        if (download_promotionArrayList.size() > 0) {
                            deleteTableVersion("promotion");
                        }
                        for (Download_Promotion download_promotion : download_promotionArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("id", download_promotion.getId());
                            cv.put("promotion_type", download_promotion.getPromotion_type());
                            cv.put("from_date", download_promotion.getFrom_date());
                            cv.put("to_date", download_promotion.getTo_date());
                            cv.put("from_time", download_promotion.getFrom_time());
                            cv.put("to_time", download_promotion.getTo_time());
                            cv.put("sell_item_qty", download_promotion.getSell_item_qty());
                            cv.put("present_item", download_promotion.getPresent_item());
                            database.insert("promotion", null, cv);
                        }
                        Log.d("Demo Promotion", download_promotionArrayList.size() + "");
                        download_promotion_itemArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getPromotion_item()));
                        if (download_promotion_itemArrayList.size() > 0) {
                            deleteTableVersion("promotionItem");
                        }
                        for (Download_Promotion_Item download_promotion_item : download_promotion_itemArrayList) {
                            ContentValues cv = new ContentValues();
                            cv.put("promotion_id", download_promotion_item.getPromotion_id());
                            cv.put("item_id", download_promotion_item.getItem_id());
                            database.insert("promotionItem", null, cv);
                        }
                        Log.d("Demo PromotionItem", download_promotion_itemArrayList.size() + "");
                        database.setTransactionSuccessful();
                        database.endTransaction();
                        loadDiscountJson();
                    } catch (Exception e) {
                        e.printStackTrace();
                        callUploadDialog("Syncs data is null" + e.getMessage());
                    }
                } else {
                    callUploadDialog(response.message());
                }
            }

            @Override
            public void onFailure(Call<JsonResponseSyncs> call, Throwable t) {
                Log.d("Login", t.getMessage());
                progressDialog.dismiss();
                callUploadDialog("Please upload again!");
            }
        });
    }

    private void loadDiscountJson() {
        callDialog("Updating data....");
        RequestInterface request = RetrofitService.createRetrofitService(RequestInterface.class, HomePageActivity.this);
        Call<JSONResponseDiscount> call = request.getDiscount(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseDiscount>() {
            @Override
            public void onResponse(Call<JSONResponseDiscount> call, Response<JSONResponseDiscount> response) {
                try {
                    JSONResponseDiscount jsonResponse = response.body();
                    progressDialog.dismiss();
                    deleteTableVersion("discount");
                    download_discountArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getDiscount()));
                    database.beginTransaction();
                    for (Download_Discount download_discount : download_discountArrayList) {
                        ContentValues cv = new ContentValues();
                        cv.put("item_id", download_discount.getItem_id());
                        cv.put("amount", download_discount.getAmount());
                        cv.put("type", download_discount.getType());
                        database.insert("discount", null, cv);
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();
                    deleteTableVersion("tableVersion");
                    loadTableVersion(1);

                } catch (Exception e) {
                    e.printStackTrace();
                    progressDialog.dismiss();
                    callUploadDialog("Discount data is null.");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseDiscount> call, Throwable t) {
                progressDialog.dismiss();
                callUploadDialog("Please upload again!");
                Log.d("ErrorDiscount", t.getMessage());
            }
        });
    }

    private void deleteTableVersion(String tableName) {
        database.beginTransaction();
        database.execSQL("DELETE FROM " + tableName);
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void callDialog(String message) {
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private ArrayList<Integer> getVersionList() {
        database.beginTransaction();
        ArrayList<Integer> versionList = new ArrayList<>();
        Cursor cur = database.rawQuery("SELECT * FROM tableVersion", null);
        while (cur.moveToNext()) {
            versionList.add(cur.getInt(cur.getColumnIndex("version")));
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return versionList;
    }

    private void loadTableVersion(final int value) {  // tables are update by version loading table version processes here !!
        if (value == 0) {
            callDialog("Download table data....");
        }

        RequestInterface request = RetrofitService.createRetrofitService(RequestInterface.class, HomePageActivity.this);
        Call<JSONResponseTableVersion> call = request.getSyncsTable(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseTableVersion>() {
            @Override
            public void onResponse(Call<JSONResponseTableVersion> call, Response<JSONResponseTableVersion> response) {
                try {
                    if (value == 0) {
                        progressDialog.dismiss();
                    }
                    JSONResponseTableVersion jsonResponse = response.body();
                    download_tableVersionArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getSyncsTable()));
                    database.beginTransaction();

                    for (Download_TableVersion download_tableVersion : download_tableVersionArrayList) {
                        ContentValues cv = new ContentValues();
                        cv.put("id", download_tableVersion.getId());
                        cv.put("table_name", download_tableVersion.getTable_name());
                        if (value == 0) {
                            cv.put("version", "0");
                        } else {
                            cv.put("version", download_tableVersion.getVersion());
                        }
                        Log.i("b4 insert : ", download_tableVersionArrayList.size() + "");
                        database.insert("tableVersion", null, cv);
                        Log.i("After insert : ", download_tableVersionArrayList.size() + "");
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();
                } catch (Exception e) {
                    Log.i("EXCEPTION insert : ", download_tableVersionArrayList.size() + "");
                    e.printStackTrace();
                    if (response.message() != null && !response.message().equals("")) {
                        callUploadDialog(response.message());
                    } else {
                        callUploadDialog(getResources().getString(R.string.connection_failure));
                    }
                }
            }

            @Override
            public void onFailure(Call<JSONResponseTableVersion> call, Throwable t) {
                if (value == 0) {
                    progressDialog.dismiss();
                }
                callUploadDialog("Invalid Requested URL");
            }
        });
    }

}
