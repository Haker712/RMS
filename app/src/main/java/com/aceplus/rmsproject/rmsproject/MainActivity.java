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
import android.os.Environment;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aceplus.rmsproject.rmsproject.object.ActivateKey;
import com.aceplus.rmsproject.rmsproject.object.ActivationRequestData;
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
import com.aceplus.rmsproject.rmsproject.object.LoginOrderIdRequest;
import com.aceplus.rmsproject.rmsproject.utils.ActivationRequestInterface;
import com.aceplus.rmsproject.rmsproject.utils.Database;
import com.aceplus.rmsproject.rmsproject.utils.RequestInterface;
import com.aceplus.rmsproject.rmsproject.utils.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends Activity {
    private EditText usernameEdit;
    private EditText passwordEdit;
    private EditText activateEdit;
    private Button loginBtn;
    private Button submmitBtn;
    private TextView nameTxt;
    private TextView ipchangeTxt;
    private ImageView logoImage;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        database = new Database(this).getDataBase();
        sharedpreferences = getSharedPreferences(LOGIN_PREFERENCES, Context.MODE_PRIVATE);
        registerIds();
        activateEdit.setText("880661d88126fffe7360cb8b1e494db0");

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        tablet_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        prefs = getSharedPreferences("MYPRE", MODE_PRIVATE);
        editor = prefs.edit();

        if (getActivateKeyStatus() == true) {  // get ket from backend
            loginLayout.setVisibility(View.GONE);
            activateLayout.setVisibility(View.VISIBLE);
            nameTxt.setText("Activate Key");
            submmitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final ProgressDialog mProgressDialog = new ProgressDialog(MainActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
                    mProgressDialog.setIndeterminate(false);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                    mProgressDialog.setMessage(getResources().getString(R.string.download_progress));
                    mProgressDialog.show();

                    ActivationRequestData activationRequestData = new ActivationRequestData();
                    activationRequestData.setTablet_activation_key(activateEdit.getText().toString());
                    activationRequestData.setTablet_id(tablet_id);

                    String param_Data = getJsonFromObject(activationRequestData);
                    Log.i("Activation_PD", param_Data);

                    ActivationRequestInterface activationRequestInterface = RetrofitService.createService(ActivationRequestInterface.class);
                    Call<ActivateKey> call = activationRequestInterface.getActivation(param_Data);

                    call.enqueue(new Callback<ActivateKey>() {
                        @Override
                        public void onResponse(Call<ActivateKey> call, Response<ActivateKey> response) {
                            if (response.code() == 200) {
                                activateKey = response.body();
                                if (activateKey.getStatusCode().equals("200")) {
                                    mProgressDialog.cancel();
                                    activationKey = activateKey.getBackend_activation_key();

                                    editor.putString("BACKEND_URL", activateKey.getBackend_url());
                                    editor.commit();

                                    database.beginTransaction();
                                    ContentValues cv = new ContentValues();
                                    cv.put("status", "false");
                                    cv.put("backend_activation_key", activationKey);
                                    database.insert("activate_key", null, cv);
                                    database.setTransactionSuccessful();
                                    database.endTransaction();
                                    Toast.makeText(MainActivity.this, "Successfully!", Toast.LENGTH_LONG).show();
                                    activateLayout.setVisibility(View.GONE);
                                    loginLayout.setVisibility(View.VISIBLE);
                                    nameTxt.setText("User Login");
                                    catchEvents();
                                } else {
                                    mProgressDialog.dismiss();
                                    if (response.body().getMessage() != null && !response.body().getMessage().equals("")) {
                                        callUploadDialog(response.body().getMessage());
                                    } else {
                                        callUploadDialog(getResources().getString(R.string.server_error));
                                    }
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ActivateKey> call, Throwable t) {
                            mProgressDialog.cancel();
                            callUploadDialog(getResources().getString(R.string.connection_failure));
                        }
                    });
                   /* try {
                        activateKey = call.execute().body();
                        if (activateKey != null) {
                            if (activateKey.getStatusCode().equals("200")) {
                                progressDialog.cancel();
                                activationKey = activateKey.getBackend_activation_key();

                                editor.putString("BACKEND_URL", activateKey.getBackend_url());
                                editor.commit();

                                database.beginTransaction();
                                ContentValues cv = new ContentValues();
                                cv.put("status", "false");
                                cv.put("backend_activation_key", activationKey);
                                database.insert("activate_key", null, cv);
                                database.setTransactionSuccessful();
                                database.endTransaction();
                                Toast.makeText(MainActivity.this, "Successfully!", Toast.LENGTH_LONG).show();
                                activateLayout.setVisibility(View.GONE);
                                loginLayout.setVisibility(View.VISIBLE);
                                nameTxt.setText("User Login");
                                catchEvents();
                            }
                        }
                    } catch (IOException e) {
                        callUploadDialog(getResources().getString(R.string.connection_failure));
                        e.printStackTrace();
                    }*/

                }
            });
        } else {
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
        ipEdit.setText("http://192.168.1.1:8888");
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
                            editor.putString("BACKEND_URL", URL);
                            editor.commit();
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
        logoImage = (ImageView) findViewById(R.id.logo_img);
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

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (prefs.getString("BACKEND_URL", "") != null) {
                    String BACKEND_URL = prefs.getString("BACKEND_URL", "");
                    URL = BACKEND_URL;
                    IMG_URL_PREFIX = BACKEND_URL + "/uploads/";
                }


                /*Interceptor interceptor = new Interceptor() {
                    @Override
                    public okhttp3.Response intercept(Chain chain) throws IOException {
                        Request newRequest = chain.request().newBuilder().addHeader("X-Authorization", getActivateKeyFromDB()).build();
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
                        .build();*/

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
                    userLogIn();
                } else {
                    loadTableVersion(0);
                    userLogIn();
                    requestLastOrderId();

                }
            }
        });

        logoImage.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                backupDatabase(MainActivity.this);
                return false;
            }
        });
    }

    private void userLogIn() {
        callDialog("User Login....");
        RequestInterface request = RetrofitService.createRetrofitService(RequestInterface.class, MainActivity.this);
        Call<Login> call = request.createTask(usernameEdit.getText().toString(), String.valueOf(passwordEdit.getText()), getActivateKeyFromDB());
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
                        Waitername = jsonResponse.getWaiter_name();
                        UserRole = jsonResponse.getRole();
                        Log.i("Role", UserRole);
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
                    progressDialog.dismiss();
                    if (response.message() != null && !response.message().equals("")) {
                        callUploadDialog(response.message());
                    } else {
                        callUploadDialog(getResources().getString(R.string.server_error));
                    }
                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                progressDialog.dismiss();
                callUploadDialog("Please login again!");
            }
        });

    }

    private void requestLastOrderId() {


        callDialog("Request LastOrderId...");
        RequestInterface request = RetrofitService.createRetrofitService(RequestInterface.class, MainActivity.this);
        final Call<LoginOrderIdRequest> call1 = request.LOGIN_ORDER_ID_REQUEST_CALL(tablet_id, getActivateKeyFromDB());
        call1.enqueue(new Callback<LoginOrderIdRequest>() {
            @Override
            public void onResponse(Call<LoginOrderIdRequest> call, Response<LoginOrderIdRequest> response) {

                try {
                    LoginOrderIdRequest loginOrderIdRequest = response.body();

                    database.beginTransaction();

                    database.execSQL("DELETE FROM voucher");

                    ContentValues cv = new ContentValues();
                    cv.put("id", loginOrderIdRequest.getTablet_generated_id());
                    cv.put("voucher_count", loginOrderIdRequest.getOrder_id());
                    database.insert("voucher", null, cv);


                    database.setTransactionSuccessful();
                    database.endTransaction();

                } catch (Exception e) {

                    e.printStackTrace();
                    progressDialog.dismiss();
//                   if (response.message() != null && !response.message().equals("")) {
//                       callUploadDialog(response.message());
//                   } else {
//                       callUploadDialog(getResources().getString(R.string.server_error));
//                   }
                }

            }

            @Override
            public void onFailure(Call<LoginOrderIdRequest> call, Throwable t) {

            }
        });

    }

    /**
     * Back-up current database
     *
     * @param context current activity
     */
    public static void backupDatabase(Context context) {
        String today = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                Toast.makeText(context, "Backup database is starting...",
                        Toast.LENGTH_SHORT).show();
                String currentDBPath = "/data/com.aceplus.rmsproject.rmsproject/databases/restaurant.sqlite";

                String backupDBPath = "ROS_DB_Backup_" + today + ".db";
                File currentDB = new File(data, currentDBPath);

                String folderPath = "mnt/sdcard/ROS_DB_Backup";
                File f = new File(folderPath);
                f.mkdir();
                File backupDB = new File(f, backupDBPath);
                FileChannel source = new FileInputStream(currentDB).getChannel();
                FileChannel destination = new FileOutputStream(backupDB).getChannel();
                destination.transferFrom(source, 0, source.size());
                source.close();
                destination.close();
                Toast.makeText(context, "Backup database Successful!",
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(context, "Please set Permission for Storage in Setting!", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Toast.makeText(context, "Cannot Backup!", Toast.LENGTH_SHORT).show();
        }
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

        RequestInterface request = RetrofitService.createRetrofitService(RequestInterface.class, MainActivity.this);
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
                    } catch(Exception e){
                       // Log.i("EXCEPTION insert : ", download_tableVersionArrayList.size() + "");
                        e.printStackTrace();
                        if (response.message() != null && !response.message().equals("")) {
                            callUploadDialog(response.message());
                        } else {
                            callUploadDialog(getResources().getString(R.string.connection_failure));
                        }
                    }
                }

                @Override
                public void onFailure (Call < JSONResponseTableVersion > call, Throwable t){
                    if (value == 0) {
                        progressDialog.dismiss();
                    }
                    callUploadDialog("Invalid Requested URL");
                }
            });
        }

    private ArrayList<Integer> getVersionList () {
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

    private void loadSyncsTable(ArrayList<Integer> version) {  // syncs down the tables from back end !!
        callDialog("Update Data....");
        final RequestInterface request = RetrofitService.createRetrofitService(RequestInterface.class, MainActivity.this);
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
           // Log.d("vCategory", vCategory);

            vItem = version.get(1);
           // Log.d("vItem", vItem);

            vAddon = version.get(2);
           // Log.d("vAddon", vAddon);

            vMember = version.get(3);
           // Log.d("vMember", vMember);

            vSetMenu = version.get(4);
           // Log.d("vSetMenu", vSetMenu);

            vSetItem = version.get(5);
           // Log.d("vSetItem", vSetItem);

            vRoom = version.get(6);
           // Log.d("vRoom", vRoom);

            vTable = version.get(7);
           // Log.d("vTable", vTable);

            vBooking = version.get(8);
           // Log.d("vBooking", vBooking);

            vConfig = version.get(9);
          //  Log.d("vConfig", vConfig);

            vPromotion = version.get(10);
           // Log.d("vPromotion", vPromotion);

            vPromotionItem = version.get(11);
           // Log.d("vPromotionItem", vPromotionItem);

            vDiscount = version.get(12);
          //  Log.d("vDiscount", vDiscount);
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
                            if (download_setMenuArrayList.size() > 0) {
                                deleteTableVersion("category");
                                ContentValues setMenuCV = new ContentValues();
                                setMenuCV.put("id", "set_menu");
                                setMenuCV.put("name", "SetMenu");
                                setMenuCV.put("status", "1");
                                setMenuCV.put("parent_id", "0");
                                setMenuCV.put("kitchen_id", "0");
                                setMenuCV.put("image", "setmenu.jpg");
                                database.insert("category", null, setMenuCV);
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
                            Log.i("RoomCharge",download_config.getRoom_charge()+"");

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
        RequestInterface request = RetrofitService.createRetrofitService(RequestInterface.class, MainActivity.this);
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
//                    startActivity(new Intent(MainActivity.this, HomePageActivity.class));
                    Intent intent = new Intent(MainActivity.this, HomePageActivity.class);
                    intent.putExtra("WaiterName", Waitername);
                    intent.putExtra("UserRole", UserRole);
                    startActivity(intent);
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

    private String getJsonFromObject(Object object) {
        Gson gson = new GsonBuilder().serializeNulls().create();
        String jsonString = gson.toJson(object);
        return jsonString;
    }

}
