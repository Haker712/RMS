package com.aceplus.rmsproject.rmsproject;

import android.app.Activity;
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
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aceplus.rmsproject.rmsproject.object.BRoom;
import com.aceplus.rmsproject.rmsproject.object.BTable;
import com.aceplus.rmsproject.rmsproject.object.Download_AddOn;
import com.aceplus.rmsproject.rmsproject.object.Download_Booking;
import com.aceplus.rmsproject.rmsproject.object.Download_Category;
import com.aceplus.rmsproject.rmsproject.object.Download_Config;
import com.aceplus.rmsproject.rmsproject.object.Download_Discount;
import com.aceplus.rmsproject.rmsproject.object.Download_Item;
import com.aceplus.rmsproject.rmsproject.object.Download_Member;
import com.aceplus.rmsproject.rmsproject.object.Download_Promotion;
import com.aceplus.rmsproject.rmsproject.object.Download_Promotion_Item;
import com.aceplus.rmsproject.rmsproject.object.Download_Room;
import com.aceplus.rmsproject.rmsproject.object.Download_SetItem;
import com.aceplus.rmsproject.rmsproject.object.Download_SetMenu;
import com.aceplus.rmsproject.rmsproject.object.Download_Table;
import com.aceplus.rmsproject.rmsproject.object.Download_TableVersion;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseDiscount;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseTableVersion;
import com.aceplus.rmsproject.rmsproject.object.JsonResponseSyncs;
import com.aceplus.rmsproject.rmsproject.object.Login;
import com.aceplus.rmsproject.rmsproject.utils.Database;
import com.aceplus.rmsproject.rmsproject.utils.RequestInterface;

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

public class MainActivity extends Activity {
    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText activateEdit;
    private Button loginBtn;
    private Button submmitBtn;
    private TextView nameTxt;
    private TextView ipchangeTxt;
    private RelativeLayout loginLayout;
    private RelativeLayout activateLayout;
    SQLiteDatabase database;
    private Retrofit retrofit;
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
    public static String URL = "http://192.168.11.57:8900";
    //public static String URL = "http://192.168.11.62:8019";
//    public static String URL = "http://192.168.195.1:8900";

    SharedPreferences sharedpreferences;
    public static final String LOGIN_PREFERENCES = "Login";
    public static final String WAITER_ID = "waiter_id";
    public static boolean userLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        database = new Database(this).getDataBase();
        sharedpreferences = getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        registerIds();
        if (getActivateKeyStatus() == true) {  // get ket from backend
            loginLayout.setVisibility(View.GONE);
            activateLayout.setVisibility(View.VISIBLE);
            nameTxt.setText("Activate Key");
            submmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    database.beginTransaction();
                    ContentValues cv = new ContentValues();
                    cv.put("status", "false");
                    cv.put("backend_activation_key", "roXfvF8FeyLY");
                    database.insert("activate_key", null, cv);
                    database.setTransactionSuccessful();
                    database.endTransaction();
                    Toast.makeText(MainActivity.this, "Successfully!", Toast.LENGTH_LONG).show();
                    activateLayout.setVisibility(View.GONE);
                    loginLayout.setVisibility(View.VISIBLE);
                    nameTxt.setText("User Login");
                    catchEvents();
                    Toast.makeText(MainActivity.this, "Successfully", Toast.LENGTH_LONG).show();
                }
            });
        } else
        {
            activateLayout.setVisibility(View.GONE);
            loginLayout.setVisibility(View.VISIBLE);
            nameTxt.setText("User Login");
            catchEvents();
        }
    }

    private void setIPAddress() {
        final AlertDialog builder = new AlertDialog.Builder(MainActivity.this, R.style.InvitationDialog)
                .setPositiveButton(R.string.invitation_ok, null)
                .setNegativeButton(R.string.invitation_cancel, null)
                .create();
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.set_ip_dialog, null);
        final EditText ipEdit = (EditText) view.findViewById(R.id.ip_edit);
        builder.setView(view);
        builder.setTitle(R.string.quantity_title);
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnAccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (ipEdit.getText().toString().isEmpty()) {
                            ipEdit.setError("IP Address is required.");
                            ipEdit.requestFocus();
                        } else {
                            String ip = ipEdit.getText().toString();
                            URL = ip;
                            Toast.makeText(MainActivity.this, "IP address" + ip, Toast.LENGTH_LONG).show();
                            builder.dismiss();
                        }
                    }
                });
                final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Quantity", "Invitation declined");
                        builder.dismiss();
                    }
                });
            }
        });
        builder.show();
    }

    private boolean getActivateKeyStatus() {
        database.beginTransaction();
        boolean status = false;
        Cursor cur = database.rawQuery("SELECT * FROM activate_key", null);
        while (cur.moveToNext()) {
            status = Boolean.parseBoolean(cur.getString(cur.getColumnIndex("status")));
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return status;
    }

    private void registerIds() {
        usernameEdit = (EditText) findViewById(R.id.user_name_edit);
        passwordEdit = (EditText) findViewById(R.id.password_edit);
        activateEdit = (EditText) findViewById(R.id.activate_edit);
        ipchangeTxt = (TextView) findViewById(R.id.user_login_txt);
        loginBtn = (Button) findViewById(R.id.login_btn);
        submmitBtn = (Button) findViewById(R.id.submmit_btn);
        loginLayout = (RelativeLayout) findViewById(R.id.relative_layout);
        activateLayout = (RelativeLayout) findViewById(R.id.activate_relative_layout);
        nameTxt = (TextView) findViewById(R.id.user_login_txt);
    }

    private void callDialog(String message) {
        progressDialog = new ProgressDialog(this, ProgressDialog.THEME_HOLO_LIGHT);
        progressDialog.setIndeterminate(false);
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void catchEvents() { // log in processes !
        ipchangeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setIPAddress();
            }
        });
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("X-Authorization", "25c512a9b6b76c778e321e35606016f10e95e74b").build();
                return chain.proceed(newRequest);
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        builder.readTimeout(180, TimeUnit.SECONDS);
        builder.connectTimeout(180, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (usernameEdit.getText().length() == 0) {
                    usernameEdit.setError("Username is required.");
                    usernameEdit.requestFocus();
                    return;
                } else if (passwordEdit.getText().length() == 0) {
                    passwordEdit.setError("Password is required.");
                    passwordEdit.requestFocus();
                    return;
                }
                Log.e("TureOrFalse", hasCategoryDataInDb() + "");
                if (hasCategoryDataInDb()) {
                    callDialog("User Login....");
                    RequestInterface request = retrofit.create(RequestInterface.class);
                    Call<Login> call = request.createTask("Phyoe Lay", "11111111", getActivateKeyFromDB());
                    //Call<Login> call = request.createTask(usernameEdit.getText().toString(), passwordEdit.getText().toString() , getActivateKeyFromDB());
                    call.enqueue(new Callback<Login>() {
                        @Override
                        public void onResponse(Call<Login> call, Response<Login> response) {
                            try {
                                progressDialog.dismiss();
                                Login jsonResponse = response.body();
                                String message = jsonResponse.getMessage();
                                if (message.equals("Success")) {
                                    userLogin = true;
                                    Log.d("Login", message);
                                    Log.d("Waiter_ID", jsonResponse.getWaiter_id());
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString(WAITER_ID, jsonResponse.getWaiter_id());
                                    editor.commit();
                                    loadSyncsTable(getVersionList());
                                } else if (message.equals("Fail")) {
                                    final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, R.style.InvitationDialog)
                                            .setPositiveButton(R.string.invitation_ok, null)
                                            .setNegativeButton(R.string.invitation_cancel, null)
                                            .create();
                                    builder.setTitle(R.string.alert);
                                    builder.setMessage("Username or password is incorrect!");
                                    builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                        @Override
                                        public void onShow(DialogInterface dialog) {
                                            final Button btnAccept = builder.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                                            btnAccept.setOnClickListener(new View.OnClickListener() {
                                                @Override
                                                public void onClick(View v) {
                                                    builder.dismiss();
                                                    passwordEdit.selectAll();
                                                }
                                            });
                                            final Button btnCancle = builder.getButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE);
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
                            } catch (Exception e) {
                                e.printStackTrace();
                                callUploadDialog("Login data is null.");
                            }
                        }
                        @Override
                        public void onFailure(Call<Login> call, Throwable t) {
                            progressDialog.dismiss();
                            callUploadDialog("Please login again!");
                        }
                    });
                } else {
                    loadTableVersion(0);
                }
            }
        });
    }

    private void callUploadDialog(String message) {
        try {
            final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(MainActivity.this, R.style.InvitationDialog)
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
                            passwordEdit.selectAll();
                        }
                    });
                }
            });
            builder.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void loadTableVersion(final int value) {  // tables are update by version loading table version processes here !!
        if (value == 0) {
            callDialog("Download table data....");
        }
        RequestInterface request = retrofit.create(RequestInterface.class);
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
                        database.insert("tableVersion", null, cv);
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();
                } catch (Exception e) {
                    e.printStackTrace();
                    callUploadDialog("Table data is null.");
                }
            }
            @Override
            public void onFailure(Call<JSONResponseTableVersion> call, Throwable t) {
                if (value == 0) {
                    progressDialog.dismiss();
                }
                callUploadDialog("Please downlaod again!");
            }
        });
    }

    private ArrayList<String> getVersionList() {
        database.beginTransaction();
        ArrayList<String> versionList = new ArrayList<>();
        Cursor cur = database.rawQuery("SELECT * FROM tableVersion", null);
        while (cur.moveToNext()) {
            versionList.add(cur.getString(cur.getColumnIndex("version")));
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return versionList;
    }

    private void loadSyncsTable(ArrayList<String> version) {  // syncs down the tables from back end !!
        callDialog("Update Data....");
        final RequestInterface request = retrofit.create(RequestInterface.class);
        String vCategory = null;
        String vItem = null;
        String vAddon = null;
        String vMember = null;
        String vSetMenu = null;
        String vSetItem = null;
        String vRoom = null;
        String vTable = null;
        String vBooking = null;
        String vConfig = null;
        String vPromotion = null;
        String vPromotionItem = null;
        String vDiscount = null;
        for (int i = 0; i < version.size(); i++) {
            vCategory = version.get(0);
            Log.d("vCategory", vCategory);

            vItem = version.get(1);
            Log.d("vItem", vItem);

            vAddon = version.get(2);
            Log.d("vAddon", vAddon);

            vMember = version.get(3);
            Log.d("vMember", vMember);

            vSetMenu = version.get(4);
            Log.d("vSetMenu", vSetMenu);

            vSetItem = version.get(5);
            Log.d("vSetItem", vSetItem);

            vRoom = version.get(6);
            Log.d("vRoom", vRoom);

            vTable = version.get(7);
            Log.d("vTable", vTable);

            vBooking = version.get(8);
            Log.d("vBooking", vBooking);

            vConfig = version.get(9);
            Log.d("vConfig", vConfig);

            vPromotion = version.get(10);
            Log.d("vPromotion", vPromotion);

            vPromotionItem = version.get(11);
            Log.d("vPromotionItem", vPromotionItem);

            vDiscount = version.get(12);
            Log.d("vDiscount", vDiscount);
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
                        if (jsonResponse.getCategory() != null) {
                            download_categoryArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getCategory()));
                            if (download_categoryArrayList.size() > 0) {
                                deleteTableVersion("category");
                                ContentValues setMenuCV = new ContentValues();
                                setMenuCV.put("id", "set_menu");
                                setMenuCV.put("name", "SetMenu");
                                setMenuCV.put("status", "1");
                                setMenuCV.put("parent_id", "0");
                                setMenuCV.put("kitchen_id", "0");
                                setMenuCV.put("image", imageEncoded);
                                database.insert("category", null, setMenuCV);
                            }
                            for (Download_Category download_category : download_categoryArrayList) {
                                ContentValues cv = new ContentValues();
                                cv.put("id", download_category.getId());
                                cv.put("name", download_category.getName());
                                cv.put("status", download_category.getStatus());
                                cv.put("parent_id", download_category.getParent_id());
                                cv.put("kitchen_id", download_category.getKitchen_id());
                                cv.put("image", download_category.getMobile_image());
                                database.insert("category", null, cv);
                            }
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
                            cv.put("image", download_item.getMobile_image());
                            cv.put("price", download_item.getPrice());
                            cv.put("status", download_item.getStatus());
                            cv.put("category_id", download_item.getCategory_id());
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
                            cv.put("image", download_addOn.getMobile_image());
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
                        download_setMenuArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getSet_menu()));
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
                        Log.d("Demo SetMenu", download_setMenuArrayList.size() + "");
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
        RequestInterface request = retrofit.create(RequestInterface.class);
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
                    startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                    finish();
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

    private boolean hasCategoryDataInDb() {
        return database.rawQuery("SELECT * FROM tableVersion", null).getCount() >= 1;
    }

}
