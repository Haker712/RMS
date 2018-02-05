package com.aceplus.rmsproject.rmsproject;

import android.annotation.SuppressLint;
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
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aceplus.rmsproject.rmsproject.object.AddOn;
import com.aceplus.rmsproject.rmsproject.object.Category;
import com.aceplus.rmsproject.rmsproject.object.Category_Item;
import com.aceplus.rmsproject.rmsproject.object.ContimentforItemSelect;
import com.aceplus.rmsproject.rmsproject.object.Download_AddOn;
import com.aceplus.rmsproject.rmsproject.object.Download_Category;
import com.aceplus.rmsproject.rmsproject.object.Download_Contiment;
import com.aceplus.rmsproject.rmsproject.object.Download_Discount;
import com.aceplus.rmsproject.rmsproject.object.Download_ForInvoiceDetail;
import com.aceplus.rmsproject.rmsproject.object.Download_ForInvoiceExtraDetail;
import com.aceplus.rmsproject.rmsproject.object.Download_ForInvoiveItemDetail;
import com.aceplus.rmsproject.rmsproject.object.Download_Item;
import com.aceplus.rmsproject.rmsproject.object.Download_SetItem;
import com.aceplus.rmsproject.rmsproject.object.Download_SetMenu;
import com.aceplus.rmsproject.rmsproject.object.Download_forInvoice;
import com.aceplus.rmsproject.rmsproject.object.Invoice;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseAddOn;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseCategory;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseContiment;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseDiscount;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseItem;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseSetItem;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseSetMenu;
import com.aceplus.rmsproject.rmsproject.object.JsonResponseforInvoice;
import com.aceplus.rmsproject.rmsproject.object.JsonResponseforInvoiceDetail;
import com.aceplus.rmsproject.rmsproject.object.JsonTest;
import com.aceplus.rmsproject.rmsproject.object.SetItem;
import com.aceplus.rmsproject.rmsproject.object.Success;
import com.aceplus.rmsproject.rmsproject.utils.Database;
import com.aceplus.rmsproject.rmsproject.utils.Download_forShow_roomID;
import com.aceplus.rmsproject.rmsproject.utils.Download_forShow_tableID;
import com.aceplus.rmsproject.rmsproject.utils.JsonForShowRoomId;
import com.aceplus.rmsproject.rmsproject.utils.JsonForShowTableId;
import com.aceplus.rmsproject.rmsproject.utils.RequestInterface;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;

public class CategoryActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private RecyclerView recycleritemView;
    private RecyclerView listView;
    private Button previousBtn;
    private Button nextBtn;
    private TextView categoryTxt;
    private Button updateBtn;
    private Button saveBtn;
    private TextView tPriceTxt;
    private TextView tnetPriceTxt;
    private TextView serviceAmtTxt;
    private TextView servicePercentTxt;
    private TextView taxAmtTxt;
    private TextView taxPercentTxt;
    private TextView roomchargesTxt;
    private TextView roomchargesAmtTxt;
    private AutoCompleteTextView searchItemAuto;
    SimpleDateFormat date_format = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat time_format = new SimpleDateFormat("HH:mm:SS");
    private ArrayList<Category_Item> categoryItemList = new ArrayList<>();
    private ArrayList<Category_Item> tempcategoryItemList1 = new ArrayList<>();
    private ArrayList<Category_Item> TotalitemArraylist = new ArrayList<>();
    private ArrayList<String> searchItemList = new ArrayList<>();
    public static ArrayList<String> groupTableArrayList = null;
    private ArrayList<Category> searchItemSetMenuList = new ArrayList<>();
    private ArrayList<Category> searchTotallist = new ArrayList<>();
    SQLiteDatabase database;
    CategoryItemAdapter categoryItemAdapter;
    //AddOnAdapter addOnAdapter;
    DecimalFormat orderFormat = new DecimalFormat("00000");
    SimpleDateFormat orderDate = new SimpleDateFormat("yyMMdd");
    SimpleDateFormat orderTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    DecimalFormat commaSepFormat = new DecimalFormat("###,##0");
    private String parent_ID = null;
    private String ID = null;
    public String next_ID = null;
    public String previous_ID = null;
    public static String TAKE_AWAY = null;
    public static String TABLE_ID = null;
    public static String ROOM_ID = null;
    public static String VOUNCHER_ID = null;
    public static String ADD_INVOICE = null;
    public String ROOM_CHARGE = null;
    public String WAITER_ID = null;
    public Date from_time = null;
    public Date to_time = null;
    public Date from_date = null;
    public Date to_date = null;
    public int sell_quantity = 0;
    public String promotion_id = null;
    private int invoicecount;
    private int backend_tablet_id;
    private double taxAmt = 0.0;
    private double serviceAmt = 0.0;
    private double roomchargeAmt = 0.0;
    private int count = 0;
    private int count2 = 0;
    double taxValue = 0.0;
    double serviceValue = 0.0;
    private double totalAmt = 0.0;
    private double totalExtraAmt = 0.0;
    private double totalDisAmt = 0.0;
    private double totalTaxAmt = 0.0;
    private double totalServiceAmt = 0.0;
    public boolean recyclerState = false;
    private ProgressDialog mProgressDialog;
    private Retrofit retrofit;
    private ArrayList<Download_Category> download_categoryArrayList;
    private ArrayList<Download_Item> download_itemArrayList;
    private ArrayList<Download_Contiment> download_contimentArrayList;
    private ArrayList<Download_AddOn> download_addOnArrayList;
    private ArrayList<Download_SetMenu> download_setMenuArrayList;
    private ArrayList<Download_SetItem> download_setItemArrayList;
    private ArrayList<Download_Discount> download_discountArrayList;
    private ArrayList<Download_forShow_tableID> download_forShow_tableIDs;
    private ArrayList<Download_forShow_roomID> download_forShow_roomIDs;
    String SetMenuName = null;
    String ItemName = null;

    String Itemidfromdetail = null;

    public static String check_check = "null";
    Activity activity;
    /***
     * PhoneLinAung 11.9.17
     */

    ContimentforItemSelect contimet = new ContimentforItemSelect();
    ArrayList<ContimentforItemSelect> contimentforItemSelectList = new ArrayList<>();
    List<String> contimentnameList = new ArrayList<>();

    String contiment_name;
    int contiment_id;
    int selected_Contiment_id;
    String group_id;

    String itemname;
    String con_name;

    Invoice invoice;
    Socket socket;

    Typeface font;

    String con_disamount = "";
    String con_distype = "";
    String selected_ContimentName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);

        activity = this;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        database = new Database(this).getDataBase();
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        registerIDs();
        catchEvents();

        String mainurl = MainActivity.URL;
        String supmainturl = "";

        if (mainurl != null && mainurl.length() > 0) {
            supmainturl = mainurl.substring(0, mainurl.length() - 4);
        }
        try {
            String socketurl = supmainturl + JsonTest.SOCKET_PORT;
            Log.i("SocketUrl", socketurl);
            socket = IO.socket(socketurl);
        } catch (URISyntaxException e) {
            Log.e("URL ERR :", e.getMessage());

        }

        socket.on("order_remove", onNewMessage);
        // socket.on("invoice_payment", invoicePaidSocket);
        socket.connect();

        font = Typeface.createFromAsset(
                activity.getAssets(),
                "fonts/zawgyi.ttf");

        CustomExceptionHandler.newInstance().traceUnchagedException(this);

    }

    /**
     * If server is paid current invoice, it will be interrupt and close.
     */
//    private Emitter.Listener invoicePaidSocket = new Emitter.Listener() {
//        @Override
//        public void call(final Object... args) {
//            activity.runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//
//                    JSONObject data = (JSONObject) args[0];
//                    String InvoiceId = "";
//                    try {
//                        InvoiceId = data.getString("invoice_id");
//                        Toast.makeText(activity, InvoiceId + "", Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                    if (InvoiceId.equalsIgnoreCase(VOUNCHER_ID)) {
//                        saveBtn.setEnabled(false);
//                        saveBtn.setClickable(false);
//                        callInfoDialog("This invoice is already paid", activity);
//                    }
//                }
//            });
//        }
//    };
    public void callInfoDialog(String message, final Context context) {
        final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(context, R.style.InvitationDialog)
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
                        Intent toInvoiceActivity = new Intent(context, InvoiceActivity.class);
                        activity.startActivity(toInvoiceActivity);
                        finish();
                    }
                });
            }
        });

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) {
                    return;
                } else {
                    builder.show();
                }
            }
        });
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    JSONObject data = (JSONObject) args[0];
                    String InvoiceId = "";
                    try {
                        InvoiceId = data.getString("order_remove_id");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (InvoiceId == VOUNCHER_ID) {
                        getVouncherDetailData();
                        TotalitemArraylist.addAll(tempcategoryItemList1);
                    }

                }
            });
        }
    };

    private void callDialog(String messageTxt) {
        mProgressDialog = new ProgressDialog(CategoryActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(messageTxt);
        mProgressDialog.show();
    }

    private void registerIDs() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recycleritemView = (RecyclerView) findViewById(R.id.recycler_item_view);
        listView = (RecyclerView) findViewById(R.id.list_view);
        previousBtn = (Button) findViewById(R.id.previous_btn);
        nextBtn = (Button) findViewById(R.id.next_btn);
        categoryTxt = (TextView) findViewById(R.id.category_txt);
        updateBtn = (Button) findViewById(R.id.update_btn);
        saveBtn = (Button) findViewById(R.id.save_btn);
        tPriceTxt = (TextView) findViewById(R.id.tprice);
        tnetPriceTxt = (TextView) findViewById(R.id.namount);
        taxAmtTxt = (TextView) findViewById(R.id.tax_amt_txt);
        taxPercentTxt = (TextView) findViewById(R.id.tax_percent_txt);
        serviceAmtTxt = (TextView) findViewById(R.id.scharges);
        servicePercentTxt = (TextView) findViewById(R.id.service_percent_txt);
        roomchargesTxt = (TextView) findViewById(R.id.roomservice_charges);
        roomchargesAmtTxt = (TextView) findViewById(R.id.roomservice_chargesAmt);
        searchItemAuto = (AutoCompleteTextView) findViewById(R.id.auto_search_item);
    }

    private ArrayList<Category> categoryDataFromDB(String parent_id) {  // gettting category item in category activity
        ArrayList<Category> categoryList = new ArrayList<>();
        try {
            database.beginTransaction();
            Cursor cur = database.rawQuery("SELECT * FROM category WHERE parent_id = \"" + parent_id + "\"", null);
            while (cur.moveToNext()) {

                Category category = new Category();
                category.setId(cur.getString(cur.getColumnIndex("id")));
                category.setName(cur.getString(cur.getColumnIndex("name")));
                try {
                    category.setImage(cur.getString(cur.getColumnIndex("image")));
                } catch (OutOfMemoryError outOfMemoryError) {
                    Runtime.getRuntime().gc();
                    category.setImage(cur.getString(cur.getColumnIndex("image")));
                }
                category.setStatus(cur.getString(cur.getColumnIndex("status")));
                category.setParent_id(cur.getString(cur.getColumnIndex("parent_id")));
                category.setKitchen_id(cur.getString(cur.getColumnIndex("kitchen_id")));
                categoryList.add(category);
            }
            cur.close();
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("CategoryListSize", categoryList.size() + "");
        return categoryList;

    }

    private ArrayList<Category> cateDataFromDB(String id) {
        database.beginTransaction();
        ArrayList<Category> categoryList = new ArrayList<>();
        Cursor cur = database.rawQuery("SELECT * FROM category WHERE id = \"" + id + "\"", null);
        while (cur.moveToNext()) {
            Category category = new Category();
            category.setId(cur.getString(cur.getColumnIndex("id")));
            category.setName(cur.getString(cur.getColumnIndex("name")));
            category.setImage(cur.getString(cur.getColumnIndex("image")));
            category.setStatus(cur.getString(cur.getColumnIndex("status")));
            category.setParent_id(cur.getString(cur.getColumnIndex("parent_id")));
            category.setKitchen_id(cur.getString(cur.getColumnIndex("kitchen_id")));
            categoryList.add(category);
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return categoryList;
    }

//    private ArrayList<Category> itemDataFromDB(String category_id) { // getting item from database
//        database.beginTransaction();
//        ArrayList<Category> itemArrayList = new ArrayList<>();
//        Cursor cur = database.rawQuery("SELECT * FROM item WHERE category_id = \"" + category_id + "\" && has_contiment = '" + 0 + "' ", null);
//        while (cur.moveToNext()) {
//            Category item = new Category();
//            String itemID = cur.getString(cur.getColumnIndex("id"));
//            item.setId(itemID);
//            item.setName(cur.getString(cur.getColumnIndex("name")));
//            item.setImage(cur.getString(cur.getColumnIndex("image")));
//            item.setStatus(cur.getString(cur.getColumnIndex("status")));
//            item.setPrice(cur.getDouble(cur.getColumnIndex("price")));
//            item.setCategory_id(cur.getString(cur.getColumnIndex("category_id")));
//            Cursor curDiscount = database.rawQuery("SELECT * FROM discount WHERE item_id = \"" + itemID + "\"", null);
//            while (curDiscount.moveToNext()) {
//                item.setDiscount(curDiscount.getDouble(curDiscount.getColumnIndex("amount")));
//                item.setDiscount_type(curDiscount.getString(curDiscount.getColumnIndex("type")));
//            }
//            itemArrayList.add(item);
//        }
//        cur.close();
//        database.setTransactionSuccessful();
//        database.endTransaction();
//        return itemArrayList;
//    }

    private ArrayList<Category> itemDataFromDB(String category_id) { // getting item from database
        database.beginTransaction();
        ArrayList<Category> itemArrayList = new ArrayList<>();
        Cursor cur1 = database.rawQuery("SELECT * FROM item WHERE category_id = \"" + category_id + "\" and has_contiment = " + 1 + " and isdefault=" + 1, null);
        try {
            while (cur1.moveToNext()) {
                Category item = new Category();
                String itemID = cur1.getString(cur1.getColumnIndex("id"));
                item.setId(itemID);
                item.setName(cur1.getString(cur1.getColumnIndex("name")));
                item.setImage(cur1.getString(cur1.getColumnIndex("image")));
                item.setStatus(cur1.getString(cur1.getColumnIndex("status")));
                item.setPrice(cur1.getDouble(cur1.getColumnIndex("price")));
                item.setCategory_id(cur1.getString(cur1.getColumnIndex("category_id")));
                Cursor curDiscount = database.rawQuery("SELECT * FROM discount WHERE item_id = \"" + itemID + "\"", null);
                while (curDiscount.moveToNext()) {
                    item.setDiscount(curDiscount.getDouble(curDiscount.getColumnIndex("amount")));
                    item.setDiscount_type(curDiscount.getString(curDiscount.getColumnIndex("type")));
                }


                itemArrayList.add(item);
                Log.i("ItemArraySize", itemArrayList.size() + "");
            }
        } catch (Exception e) {
            Log.e("SQL ERROR -> ", e.getMessage());
        }
        cur1.close();

        Cursor cur2 = database.rawQuery("SELECT * FROM item WHERE category_id = \"" + category_id + "\" and has_contiment = " + 0, null);
        while (cur2.moveToNext()) {
            Category item = new Category();
            String itemID = cur2.getString(cur2.getColumnIndex("id"));
            item.setId(itemID);
            item.setName(cur2.getString(cur2.getColumnIndex("name")));
            item.setImage(cur2.getString(cur2.getColumnIndex("image")));
            item.setStatus(cur2.getString(cur2.getColumnIndex("status")));
            item.setPrice(cur2.getDouble(cur2.getColumnIndex("price")));
            item.setCategory_id(cur2.getString(cur2.getColumnIndex("category_id")));
            Cursor curDiscount = database.rawQuery("SELECT * FROM discount WHERE item_id = \"" + itemID + "\"", null);
            while (curDiscount.moveToNext()) {
                item.setDiscount(curDiscount.getDouble(curDiscount.getColumnIndex("amount")));
                item.setDiscount_type(curDiscount.getString(curDiscount.getColumnIndex("type")));
            }


            itemArrayList.add(item);
            Log.i("ItemArraySize1", itemArrayList.size() + "");
        }

        cur2.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return itemArrayList;
    }

    private ArrayList<Category> setMenuDataFromDB() {
        ArrayList<Category> itemArrayList = new ArrayList<>();
        try {
            database.beginTransaction();
            Cursor cur = database.rawQuery("SELECT * FROM setMenu", null);
            while (cur.moveToNext()) {
                Category item = new Category();
                String itemID = cur.getString(cur.getColumnIndex("id"));
                item.setId(itemID);
                item.setName(cur.getString(cur.getColumnIndex("set_menu_name")));
                try {
                    item.setImage(cur.getString(cur.getColumnIndex("image")));
                } catch (OutOfMemoryError outOfMemoryError) {
                    Runtime.getRuntime().gc();
                    item.setImage(cur.getString(cur.getColumnIndex("image")));
                }

                item.setStatus(cur.getString(cur.getColumnIndex("status")));
                item.setPrice(cur.getDouble(cur.getColumnIndex("set_menu_price")));
                item.setCategory_id("set_menu");
                itemArrayList.add(item);
                searchTotallist.add(item);
            }
            cur.close();
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return itemArrayList;
    }

    private void getPromotionDataInDB(String item_id) {
        database.beginTransaction();
        String promotionID = null;
        Cursor cur = database.rawQuery("SELECT * FROM promotionItem WHERE item_id = \"" + item_id + "\"", null);
        while (cur.moveToNext()) {
            promotionID = cur.getString(cur.getColumnIndex("promotion_id"));
            Log.e("PromotionID", promotionID);
            Cursor curPro = database.rawQuery("SELECT * FROM promotion WHERE id = \"" + promotionID + "\"", null);
            while (curPro.moveToNext()) {
                promotion_id = promotionID;
                try {
                    from_date = date_format.parse(curPro.getString(curPro.getColumnIndex("from_date")));
                    to_date = date_format.parse(curPro.getString(curPro.getColumnIndex("to_date")));
                    from_time = time_format.parse(curPro.getString(curPro.getColumnIndex("from_time")));
                    to_time = time_format.parse(curPro.getString(curPro.getColumnIndex("to_time")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sell_quantity = curPro.getInt(curPro.getColumnIndex("sell_item_qty"));
            }
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void getPromotionDataInDBforsetmenu(String setmenu_id) {
        database.beginTransaction();
        String promotionID = null;
        Cursor cur = database.rawQuery("SELECT * FROM promotionItem WHERE setmenu_id = \"" + setmenu_id + "\"", null);
        while (cur.moveToNext()) {
            promotionID = cur.getString(cur.getColumnIndex("promotion_id"));
            Log.e("PromotionID", promotionID);
            Cursor curPro = database.rawQuery("SELECT * FROM promotion WHERE id = \"" + promotionID + "\"", null);
            while (curPro.moveToNext()) {
                promotion_id = promotionID;
                try {
                    from_date = date_format.parse(curPro.getString(curPro.getColumnIndex("from_date")));
                    to_date = date_format.parse(curPro.getString(curPro.getColumnIndex("to_date")));
                    from_time = time_format.parse(curPro.getString(curPro.getColumnIndex("from_time")));
                    to_time = time_format.parse(curPro.getString(curPro.getColumnIndex("to_time")));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                sell_quantity = curPro.getInt(curPro.getColumnIndex("sell_item_qty"));
            }
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private ArrayList<Category> getItemForAuotSearch() {
        searchTotallist.clear();
        database.beginTransaction();
        ArrayList<Category> itemArrayList = new ArrayList<>();
        Cursor cur = database.rawQuery("SELECT * FROM item", null);
        while (cur.moveToNext()) {
            Category item = new Category();
            String itemID = cur.getString(cur.getColumnIndex("id"));
            item.setId(itemID);
            item.setName(cur.getString(cur.getColumnIndex("name")));
            try {
                item.setImage(cur.getString(cur.getColumnIndex("image")));
            } catch (OutOfMemoryError outOfMemoryError) {
                Runtime.getRuntime().gc();
                item.setImage(cur.getString(cur.getColumnIndex("image")));
            }
            item.setStatus(cur.getString(cur.getColumnIndex("status")));
            item.setPrice(cur.getDouble(cur.getColumnIndex("price")));
            item.setCategory_id(cur.getString(cur.getColumnIndex("category_id")));
            Cursor curDiscount = database.rawQuery("SELECT * FROM discount WHERE item_id = \"" + itemID + "\"", null);
            while (curDiscount.moveToNext()) {
                item.setDiscount(curDiscount.getDouble(curDiscount.getColumnIndex("amount")));
                item.setDiscount_type(curDiscount.getString(curDiscount.getColumnIndex("type")));
            }
            curDiscount.close();
            itemArrayList.add(item);
            searchTotallist.add(item);
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return itemArrayList;
    }

    private String getAddOnID(String parent_id) {
        String addOnID = parent_id;
        try {
            database.beginTransaction();
            int addCount = 1;
            Cursor cur;
            do {
                cur = database.rawQuery("SELECT * FROM category WHERE id = \"" + addOnID + "\"", null);
                while (cur.moveToNext()) {
                    addOnID = cur.getString(cur.getColumnIndex("parent_id"));
                    if (addOnID.equals("0")) {
                        addOnID = cur.getString(cur.getColumnIndex("id"));
                        cur.close();
                        database.setTransactionSuccessful();
                        database.endTransaction();
                        return addOnID;
                    }
                }
                cur.close();
                addCount++;
            } while (addCount < addCount + 1);
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return addOnID;
    }

    private ArrayList<AddOn> getAddonData(String category_id) {
        database.beginTransaction();
        Cursor cur = database.rawQuery("SELECT * FROM addOn WHERE category_id = \"" + category_id + "\"", null);
        ArrayList<AddOn> addOnArrayList = new ArrayList<>();
        while (cur.moveToNext()) {
            AddOn addOn = new AddOn();
            addOn.setId(cur.getString(cur.getColumnIndex("id")));
            addOn.setFood_name(cur.getString(cur.getColumnIndex("food_name")));
            addOn.setStatus(cur.getString(cur.getColumnIndex("status")));
            addOn.setPrice(cur.getDouble(cur.getColumnIndex("price")));
            addOn.setCategory_id(cur.getString(cur.getColumnIndex("category_id")));
            addOnArrayList.add(addOn);
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return addOnArrayList;
    }

    private ArrayList<SetItem> getseitemdata(String category_id) {
        database.beginTransaction();
        Cursor cursor = database.rawQuery("SELECT * FROM setItem WHERE set_menu_id = \"" + category_id + "\"", null);
        ArrayList<SetItem> setItemArrayList = new ArrayList<>();
        while (cursor.moveToNext()) {
            SetItem seitem = new SetItem();
            seitem.setId(cursor.getString(cursor.getColumnIndex("id")));
            seitem.setSet_menu_id(cursor.getString(cursor.getColumnIndex("set_menu_id")));
            seitem.setItem_id(cursor.getString(cursor.getColumnIndex("item_id")));
            setItemArrayList.add(seitem);
        }
        cursor.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return setItemArrayList;
    }

    @SuppressLint("LongLogTag")
    private void getVouncherDetailData() {   // getting voucher invoice data from back end if it's has !!

        categoryItemList.clear();
        TotalitemArraylist.clear();

        RequestInterface requestInterfacefortable = retrofit.create(RequestInterface.class);
        Call<JsonForShowTableId> callfortable = requestInterfacefortable.getforshowOrderTable(getActivateKeyFromDB(), VOUNCHER_ID);
        callfortable.enqueue(new Callback<JsonForShowTableId>() {
            @Override
            public void onResponse(Call<JsonForShowTableId> call, Response<JsonForShowTableId> response) {
                JsonForShowTableId jsonForShowTableId = response.body();
                download_forShow_tableIDs = jsonForShowTableId.getForShow_tableID();
                for (Download_forShow_tableID download_forShow_tableID : download_forShow_tableIDs) {
                    TABLE_ID = download_forShow_tableID.getTable_id();
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonForShowTableId> call, Throwable t) {
                Log.i("Error At Inserting TableID", "?!?!?!?!?!?!?");
            }
        });
        RequestInterface requestInterfaceforroom = retrofit.create(RequestInterface.class);
        Call<JsonForShowRoomId> callforroom = requestInterfaceforroom.getforshowOrderRoom(getActivateKeyFromDB(), VOUNCHER_ID);
        callforroom.enqueue(new Callback<JsonForShowRoomId>() {
            @Override
            public void onResponse(Call<JsonForShowRoomId> call, Response<JsonForShowRoomId> response) {
                JsonForShowRoomId jsonForShowRoomId = response.body();
                download_forShow_roomIDs = jsonForShowRoomId.getForShow_roomID();
                for (Download_forShow_roomID download_forShow_roomID : download_forShow_roomIDs) {
                    ROOM_ID = download_forShow_roomID.getRoom_id();
                }
            }

            @Override
            public void onFailure(Call<JsonForShowRoomId> call, Throwable t) {
                Log.i("Error At Inserting RoomID", "?!?!?!?!?!?!?");
            }
        });
        RequestInterface requestInterfacefororder = retrofit.create(RequestInterface.class);
        Call<JsonResponseforInvoiceDetail> callfororder = requestInterfacefororder.getforInvoiceDetail(getActivateKeyFromDB(), VOUNCHER_ID);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        JsonResponseforInvoiceDetail jsonResponseforInvoiceDetail = null;
        try {
            jsonResponseforInvoiceDetail = callfororder.execute().body();
            ArrayList<Download_ForInvoiceDetail> Download_ForInvoiceDetail = jsonResponseforInvoiceDetail.getDownload_forInvoiceDetailArrayList();
            for (Download_ForInvoiceDetail download_forInvoiceDetail : Download_ForInvoiceDetail) {
                String takeiddd = download_forInvoiceDetail.getTakeId();
                ArrayList<Download_ForInvoiveItemDetail> Download_ForInvoiceItemDetailArrayList = download_forInvoiceDetail.getForInvoiveItemDetail();
                categoryItemList.clear();
                for (Download_ForInvoiveItemDetail download_forInvoiveItemDetail : Download_ForInvoiceItemDetailArrayList) {
                    Log.i("itemdetailArrayListsiezzze", Download_ForInvoiceItemDetailArrayList.size() + "");
                    Category_Item category_item = new Category_Item();
                    if (Integer.parseInt(download_forInvoiveItemDetail.getItemId()) == 0) {
                        category_item.setSetid(download_forInvoiveItemDetail.getSetmenuId());
                        SetMenuName = getSetMenuName(download_forInvoiveItemDetail.getSetmenuId());
                        category_item.setItemName(SetMenuName);
                        category_item.setId(null);
                    } else {
                        Itemidfromdetail = download_forInvoiveItemDetail.getItemId();
                        category_item.setId(Itemidfromdetail);
                        ItemName = getItemName(Itemidfromdetail);
                        category_item.setItemName(ItemName);
                        category_item.setSetid(null);
                    }
                    category_item.setTakeid(takeiddd);
                    if (takeiddd.equals("1")) {
                        TAKE_AWAY = "take";
                    }
                    category_item.setTakeAway(settakeitemID(download_forInvoiveItemDetail.getTake_item()));
                    category_item.setOrderIDD(download_forInvoiveItemDetail.getOrderId());
                    category_item.setOrderDetailIDD(download_forInvoiveItemDetail.getOrderDetailId());
                    category_item.setStatusid(download_forInvoiveItemDetail.getStatusId());
                    category_item.setQuantity(download_forInvoiveItemDetail.getQuantity());
                    category_item.setPrice(download_forInvoiveItemDetail.getAmount());
                    category_item.setDiscount(download_forInvoiveItemDetail.getDiscountAmount());
                    category_item.setAmount(download_forInvoiveItemDetail.getAmountWithDiscount());
                    category_item.setUserRemark(download_forInvoiveItemDetail.getException());

                    category_item.setState(download_forInvoiveItemDetail.getState());

                    String orderType = (download_forInvoiveItemDetail.getOrderTypeId());
                    if (orderType.equals("2")) {
                        category_item.setOrder_type_id("2");
                        category_item.setTakeAway(true);
                    } else if (orderType.equals("1")) {
                        category_item.setOrder_type_id("1");
                        category_item.setTakeAway(false);
                    }
                    String category_id = null;
                    ArrayList<Download_ForInvoiceExtraDetail> download_forInvoiceExtraDetails = download_forInvoiveItemDetail.getOrderExtras();
                    ArrayList<AddOn> addOnArrayList = new ArrayList<>();
                    for (Download_ForInvoiceExtraDetail download_forInvoiceExtraDetail : download_forInvoiceExtraDetails) {
                        AddOn newAddOn = new AddOn();
                        if (download_forInvoiceExtraDetails.size() == 0) {
                            newAddOn.setId(null);
                            newAddOn.setFood_name(null);
                            newAddOn.setPrice(0);
                            newAddOn.setCategory_id(null);
                            newAddOn.setSelected(false);
                        } else {
                            Cursor cursor = database.rawQuery("SELECT * FROM addOn WHERE id  ='" + download_forInvoiceExtraDetail.getExtra_id() + "' ", null);
                            Log.i("cursorcursorcursorcursor>>>>>", cursor.getCount() + "");
                            while (cursor.moveToNext()) {
                                newAddOn.setFood_name(cursor.getString(cursor.getColumnIndex("food_name")));
                                newAddOn.setPrice(Double.parseDouble(cursor.getString(cursor.getColumnIndex("price"))));
                                newAddOn.setCategory_id(cursor.getString(cursor.getColumnIndex("category_id")));
                                category_id = cursor.getString(cursor.getColumnIndex("category_id"));
                                newAddOn.setSelected(true);
                                newAddOn.setId(download_forInvoiceExtraDetail.getExtra_id());
                            }
                            addOnArrayList.add(newAddOn);
                        }
                    }
                    category_item.setCategoryId(category_id);
                    category_item.setAddOnArrayList(addOnArrayList);
                    categoryItemList.add(category_item);
                    Log.i("TotalitemArraylist", category_item.getState());

                }

                TotalitemArraylist = new ArrayList<>();
                for (Category_Item category_item : categoryItemList) {
                    Category_Item item = new Category_Item();
                    item.setId(category_item.getId());
                    item.setItemName(category_item.getItemName());
                    item.setPrice(category_item.getPrice());
                    item.setQuantity(category_item.getQuantity());
                    item.setDiscount_id(category_item.getDiscount_id());
                    item.setPromotion_id(category_item.getPromotion_id());
                    item.setDiscount(category_item.getDiscount());
                    item.setTotalDiscount(category_item.getTotalDiscount());
                    item.setDiscount_type(category_item.getDiscount_type());
                    item.setExtra(category_item.getExtra());
                    item.setExtraPrice(category_item.getExtraPrice());
                    item.setTotalExtraPrice(category_item.getTotalExtraPrice());
                    item.setAmount(category_item.getAmount());
                    item.setTotalAmount(category_item.getTotalAmount());
                    item.setTakeAway(category_item.getTakeAway());
                    item.setCategoryId(category_item.getCategoryId());
                    item.setUserRemark(category_item.getUserRemark());
                    item.setItem_check(category_item.getItem_check());
                    item.setOrder_type_id(category_item.getOrder_type_id());
                    item.setSetid(category_item.getSetid());
                    item.setSet_menu_name(category_item.getSet_menu_name());
                    item.setSet_item_id(category_item.getSet_item_id());
                    item.setTakeid(category_item.getTakeid());
                    item.setStatusid(category_item.getStatusid());
                    item.setOrderIDD(category_item.getOrderIDD());
                    item.setOrderDetailIDD(category_item.getOrderDetailIDD());

                    ArrayList<AddOn> tempAddonList = new ArrayList<>();

                    for (AddOn addon : category_item.getAddOnArrayList()) {

                        AddOn addondata = new AddOn();
                        addondata.setId(addon.getId());
                        addondata.setCategory_id(addon.getCategory_id());
                        addondata.setFood_name(addon.getFood_name());
                        addondata.setImage(addon.getImage());
                        addondata.setPrice(addon.getPrice());
                        addondata.setSelected(addon.isSelected());
                        addondata.setStatus(addon.getStatus());

                        tempAddonList.add(addondata);
                    }

                    item.setAddOnArrayList(tempAddonList);

                    item.setSetItemArrayList(category_item.getSetItemArrayList());
                    item.setState(category_item.getState());
                    TotalitemArraylist.add(item);
                }

                //TotalitemArraylist.addAll(categoryItemList);

            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("Error At getvoucherdetail", "!?!?!?!?!?!");
        }
    }

    private String getSetMenuName(String set_id_forName) {
        String NameStr = "";
        Cursor cursor = database.rawQuery("SELECT * FROM setMenu WHERE id = '" + set_id_forName + "' ", null);
        while (cursor.moveToNext()) {
            NameStr = cursor.getString(cursor.getColumnIndex("set_menu_name"));
        }
        return NameStr;
    }

    private String getItemName(String item_id_forName) {
        String NameStr = "";
//        Cursor cursor = database.rawQuery("SELECT * FROM item WHERE id = '" + item_id_forName + "' ", null);
//        while (cursor.moveToNext()) {
//            NameStr = cursor.getString(cursor.getColumnIndex("name"));
//        }

        Cursor cursor = database.rawQuery("SELECT * FROM item where id='" + item_id_forName + "' and has_contiment =" + 1, null);

        if (cursor.getCount() > 0) {

            while (cursor.moveToNext()) {

                int con_id = cursor.getInt(cursor.getColumnIndex("contiment_id"));
                itemname = cursor.getString(cursor.getColumnIndex("name"));
                Cursor cursor1 = database.rawQuery("SELECT * FROM contiment where id=" + con_id, null);

                while (cursor1.moveToNext()) {

                    con_name = cursor1.getString(cursor1.getColumnIndex("name"));

                }

            }

            NameStr = con_name + " " + itemname;

        } else {

            Cursor cursor_noContiment = database.rawQuery("SELECT * FROM item where id='" + item_id_forName + "' and has_contiment =" + 0, null);

            while (cursor_noContiment.moveToNext()) {

                itemname = cursor_noContiment.getString(cursor_noContiment.getColumnIndex("name"));
                NameStr = itemname;
            }


        }

        return NameStr;
    }

    private String makeOrderID() {
        try {
            database.beginTransaction();
            Cursor cur = database.rawQuery("SELECT * FROM voucher", null);
            while (cur.moveToNext()) {
                invoicecount = cur.getInt(cur.getColumnIndex("voucher_count"));
                backend_tablet_id = cur.getInt(cur.getColumnIndex("id"));
            }
            cur.close();
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Calendar todayCal = Calendar.getInstance();
        //String todayDate = orderDate.format(todayCal.getTime());
//        String orderID = tablet_id+ "-" + orderFormat.format(invoicecount + 1);
//        int totalWord = 11;
        int a = invoicecount + 1;
        String pre_orderID = ("0000000000" + a).substring(String.valueOf(a).length());
        // String pre_orderID = String.format("%0" + (totalWord - String.valueOf(invoicecount).length()) + "d", a);

        try {
            database.beginTransaction();

            database.execSQL("UPDATE voucher set voucher_count = " + a + "");

            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (Exception e) {

            Log.i("Excep", e.getMessage());

        }

        String orderID = backend_tablet_id + "-" + pre_orderID;
        Log.i("GenerateOrderId", orderID);


        return orderID;
    }

    private void callUploadDialog(String Message) {
        final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                .setPositiveButton(R.string.invitation_ok, null)
                .create();
        builder.setTitle(R.string.alert);
        builder.setMessage(Message);
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
        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) {
                    return;
                } else {
                    builder.show();
                }
            }
        });
    }

    public void getConfigData() {
        database.beginTransaction();
        Cursor cur = database.rawQuery("SELECT * FROM config", null);
        while (cur.moveToNext()) {
            taxAmt = cur.getDouble(cur.getColumnIndex("tax"));
            serviceAmt = cur.getDouble(cur.getColumnIndex("service"));
            roomchargeAmt = cur.getDouble(cur.getColumnIndex("room_charge"));
            // Log.i("roomchargeAmtroomchargeAmt",roomchargeAmt+"");
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void uploadUpdateOrderData() {   //  upload data which has the invoice data already has !!
        double tvalue = Double.parseDouble(tPriceTxt.getText().toString().trim().replaceAll(",", ""));
        double taxamount = tvalue * taxAmt / 100;
        Log.e("TaxValue", taxamount + "," + taxAmt);
        double serviceamont = tvalue * serviceAmt / 100;
        Log.e("ServiceValue", serviceamont + "," + serviceAmt);
        JSONObject orderjsonObject = new JSONObject();
        JSONArray orderDetailJsonArray = new JSONArray();
        String orderType = null;
        for (int i = 0; i < TotalitemArraylist.size(); i++) {
            Category_Item category_item = TotalitemArraylist.get(i);
            if (category_item.isTakeAway() == true) {
                orderType = "2";
            } else {
                orderType = "1";
            }
            JSONObject detail_object = new JSONObject();
            if (category_item.getItem_check().equals("1")) {
                String order_detail_id = String.valueOf(i + 1);
                JSONArray orderExtraJsonArray = new JSONArray();

                if (category_item.getAddOnArrayList() != null) {

                    for (AddOn addOn : category_item.getAddOnArrayList()) {

                        if (addOn.isSelected()) {
                            addOn.setStatus("1");
                        } else {
                            addOn.setStatus("0");
                        }
                        // if (addOn.isSelected() == true) {
                        JSONObject extra_object = new JSONObject();
                        try {
                            extra_object.put("extra_id", addOn.getId());
                            extra_object.put("quantity", category_item.getQuantity());//set the same Quantity of OrderItem
                            extra_object.put("amount", addOn.getPrice());
//                                if (!addOn.getStatus().equals("0")){
//                                    extra_object.put("status","1");
//                                }else {
//                                    extra_object.put("status",addOn.getStatus());
//                                }
                            extra_object.put("status", addOn.getStatus());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        orderExtraJsonArray.put(extra_object);
                        // }
                    }
                }
                try {
                    if (category_item.getSetid() == "null" || category_item.getSetid() == null) {
                        detail_object.put("item_id", category_item.getId());
                        detail_object.put("set_id", "null");
                        JSONArray setItemArray = new JSONArray();
                        detail_object.put("set_item", setItemArray);
                        Log.e("ITEM_ID >>>>", category_item.getId() + "");
                    } else {
                        detail_object.put("set_id", category_item.getSetid());
                        detail_object.put("item_id", "null");
                        JSONArray setItemJsonArray = new JSONArray();
                        ArrayList<SetItem> setItemArrayList = new ArrayList<>();
                        setItemArrayList = getseitemdata(category_item.getSetid());
                        Log.i("testttttttttt>>>>>", setItemArrayList.size() + "");
                        for (SetItem setItem : setItemArrayList) {
                            if (setItemArrayList != null) {
                                JSONObject setitemJsonObject = new JSONObject();
                                try {
                                    setitemJsonObject.put("id", setItem.getId());
                                    setitemJsonObject.put("item_id", setItem.getItem_id());
                                    setitemJsonObject.put("set_menu_id", setItem.getSet_menu_id());
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                setItemJsonArray.put(setitemJsonObject);
                            }
                        }
                        detail_object.put("set_item", setItemJsonArray);
                        Log.e("SetID", category_item.getSetid() + "");
                    }
                    detail_object.put("take_item", gettakeitemID(category_item.getTakeAway()));
                    detail_object.put("order_detail_id", VOUNCHER_ID + order_detail_id);
                    detail_object.put("discount_amount", category_item.getDiscount() + "");
                    detail_object.put("promotion_id", category_item.getPromotion_id() + "");
                    detail_object.put("price", category_item.getPrice());
                    detail_object.put("quantity", category_item.getQuantity());
                    detail_object.put("amount", category_item.getTotalAmount());
                    detail_object.put("order_type_id", orderType);
                    detail_object.put("status", category_item.getStatusid());
                    detail_object.put("exception", category_item.getUserRemark() + "");
                    detail_object.put("extra", orderExtraJsonArray);
                    detail_object.put("state", TotalitemArraylist.get(i).getState());
                    Log.i("state", TotalitemArraylist.get(i).getState());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                orderDetailJsonArray.put(detail_object);

            }
        }
        JSONArray jsonArray = new JSONArray();
        try {
            double totalcharge = 0;
            double netcharge;
            orderjsonObject.put("user_id", WAITER_ID);
            if (TAKE_AWAY.equals("take")) {
                orderjsonObject.put("take_id", "1");
            } else {
                orderjsonObject.put("take_id", "null");
            }
            orderjsonObject.put("order_id", VOUNCHER_ID);
            orderjsonObject.put("total_price", tvalue);
            orderjsonObject.put("extra_price", totalExtraAmt);
            orderjsonObject.put("discount_amount", totalDisAmt);

            if (ROOM_ID != null /*|| !ROOM_ID.equals("")*/) {
                totalcharge = (serviceamont + roomchargeAmt);
                orderjsonObject.put("service_amount", serviceamont);
                Log.i("totalcharge111", totalcharge + "");


            } else {
                totalcharge = serviceamont;
                orderjsonObject.put("service_amount", serviceamont);
            } //  NEED TO CHECK LATER

//            if (!check_check.equals("null")) {
//                totalcharge = (serviceamont + roomchargeAmt);
//                orderjsonObject.put("service_amount", serviceamont);
//                Log.i("totalcharge111", totalcharge + "");
//            } else {
//                totalcharge = serviceamont;
//                orderjsonObject.put("service_amount", serviceamont);
//            }

            orderjsonObject.put("tax_amount", taxamount);
            netcharge = (tvalue + totalcharge + taxamount);

            orderjsonObject.put("net_price", netcharge);
            orderjsonObject.put("order_detail", orderDetailJsonArray);

            if (check_check.equals("room")) {

                orderjsonObject.put("room_charge", roomchargeAmt);

            } else {

                orderjsonObject.put("room_charge", 0);

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(orderjsonObject);
        Log.i("OrderJson2", jsonArray + "");
        callDialog("Uploading order add data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Success> call = request.postOrderAddInvoice(jsonArray + "");
        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                try {
                    Success jsonResponse = response.body();
                    String message = jsonResponse.getMessage();
                    if (message.equals("Success")) {
                        Log.d("Order", message);

                        Handler handler = new Handler();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                socket.emit("order_edit", VOUNCHER_ID);
                            }
                        });

                        mProgressDialog.dismiss();
                        saveUpdateOrderData();
                        startActivity(new Intent(CategoryActivity.this, HomePageActivity.class));
                        finish();

                    } else if (message.equals("Paid")) {

                        final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(
                                CategoryActivity.this, R.style.InvitationDialog)
                                .setPositiveButton(R.string.invitation_ok, null)
                                .create();
                        builder.setTitle(R.string.alert);
                        builder.setMessage("This invoice is already paid");
                        builder.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                final Button btnAccept = builder.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                                btnAccept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        builder.dismiss();

                                        startActivity(new Intent(CategoryActivity.this, HomePageActivity.class));
                                        finish();

                                    }
                                });
                            }
                        });
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (activity.isFinishing()) {
                                    return;
                                } else {
                                    builder.show();
                                }
                            }
                        });

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Vouncher add reply null.");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                Log.d("Login", t.getMessage());
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
            }
        });
    }

    private void saveUpdateOrderData() {
        database.beginTransaction();
        Calendar todayCal = Calendar.getInstance();
        String todayDate = orderTime.format(todayCal.getTime());
        String arg[] = {VOUNCHER_ID};
        ContentValues cv = new ContentValues();
        cv.put("total_amount", Double.parseDouble(tPriceTxt.getText().toString().trim().replaceAll(",", "")));
        cv.put("discount_amount", totalDisAmt);
        cv.put("net_amount", Double.parseDouble(tnetPriceTxt.getText().toString().trim().replaceAll(",", "")));
        cv.put("total_extra", totalExtraAmt);
        cv.put("order_time", todayDate);
        for (int i = 0; i < TotalitemArraylist.size(); i++) {
            Category_Item category_item = TotalitemArraylist.get(i);
            if (category_item.getItem_check().equals("1")) {
                String order_detail_id = String.valueOf(i + 1);
                String orderType = null;
                final ContentValues content = new ContentValues();
                content.put("id", VOUNCHER_ID + order_detail_id);
                Log.e("UpdateDetailID", VOUNCHER_ID + order_detail_id + "");
                content.put("order_id", VOUNCHER_ID);
                content.put("item_id", category_item.getId());
                content.put("item_name", category_item.getItemName());
                content.put("quantity", category_item.getQuantity());
                content.put("item_price", category_item.getPrice());
                content.put("discount", category_item.getDiscount());
                content.put("item_extra", category_item.getExtraPrice());
                content.put("category_id", category_item.getCategoryId());
                if (category_item.isTakeAway() == true) {
                    orderType = "2";
                } else {
                    orderType = "1";
                }
                content.put("order_type_id", orderType);
                if (category_item.getAddOnArrayList() != null && category_item.getAddOnArrayList().size() != 0) {
                    for (AddOn addOn : category_item.getAddOnArrayList()) {
                        String order_extra_id = VOUNCHER_ID + order_detail_id;
                        if (addOn.isSelected() == true) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("order_detail_id", order_extra_id);
                            contentValues.put("extra_id", addOn.getId());
                            contentValues.put("quantity", "1");
                            contentValues.put("amount", addOn.getPrice());
                        }
                    }
                }
                content.put("exception", category_item.getUserRemark());
                content.put("discount_id", category_item.getDiscount_id());
                content.put("amount", category_item.getAmount());
                content.put("status_id", "1");
                content.put("remark", category_item.getUserRemark());
            }
        }
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void deleteTableVersion(String tableName) {
        database.beginTransaction();
        database.execSQL("DELETE FROM " + tableName);
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private String getActivateKeyFromDB() {
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

    private void loadCategoryJson() {
        callDialog("Updating category data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseCategory> call = request.getJSON(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseCategory>() {
            @Override
            public void onResponse(Call<JSONResponseCategory> call, Response<JSONResponseCategory> response) {
                try {
                    Resources r = getResources();
                    Bitmap bm = BitmapFactory.decodeResource(r, R.drawable.img1);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.PNG, 100, baos); //bm is the bitmap object
                    byte[] b = baos.toByteArray();
                    String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);


                    JSONResponseCategory jsonResponse = response.body();
                    mProgressDialog.dismiss();
                    deleteTableVersion("category");
                    download_categoryArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getCategory()));
                    Log.i("DownloadCategorylist", download_categoryArrayList.size() + "");
                    database.beginTransaction();

                    if (download_setMenuArrayList.size() > 0) {
                        ContentValues setMenuCV = new ContentValues();
                        setMenuCV.put("id", "set_menu");
                        setMenuCV.put("name", "SetMenu");
                        setMenuCV.put("status", "");
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
                    database.setTransactionSuccessful();
                    database.endTransaction();

                    CategoryArrayAdapter adapter = (CategoryArrayAdapter) recyclerView.getAdapter();
                    adapter.albumList = categoryDataFromDB("0");
                    adapter.notifyDataSetChanged();

                    loadItemJson();
                    //loadSetMenuJson();
                    //loadSetItemJson();
                    //loadAddONJson();
                    //loadDiscountJson();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Category data reply null!");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseCategory> call, Throwable t) {
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
                Log.d("ErrorCategory", t.getMessage());
            }
        });
    }

    private void loadItemJson() {
        callDialog("Updating item data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseItem> call = request.getItem(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseItem>() {
            @Override
            public void onResponse(Call<JSONResponseItem> call, Response<JSONResponseItem> response) {
                try {
                    JSONResponseItem jsonResponse = response.body();
                    mProgressDialog.dismiss();
                    deleteTableVersion("item");
                    download_itemArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getItem()));
                    database.beginTransaction();
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
                    database.setTransactionSuccessful();
                    database.endTransaction();
                    loadSetItemJson();
                    //loadSetMenuJson();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Item data is null!");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseItem> call, Throwable t) {
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
                Log.d("ErrorItem", t.getMessage());
            }
        });
    }

    private void loadContimentJson() {

        callDialog("Updating contiment data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseContiment> call = request.getContiment(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseContiment>() {
            @Override
            public void onResponse(Call<JSONResponseContiment> call, Response<JSONResponseContiment> response) {

                try {

                    JSONResponseContiment jsonResponse = response.body();
                    mProgressDialog.dismiss();
                    deleteTableVersion("contiment");
                    download_contimentArrayList = new ArrayList<>(jsonResponse.getContiments());
                    database.beginTransaction();
                    for (Download_Contiment download_contiment : download_contimentArrayList) {

                        ContentValues cv = new ContentValues();
                        cv.put("id", download_contiment.getId());
                        cv.put("name", download_contiment.getName());
                        database.insert("contiment", null, cv);

                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();

                } catch (Exception e) {

                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Contiment data is null!");

                }

            }

            @Override
            public void onFailure(Call<JSONResponseContiment> call, Throwable t) {

            }
        });

    }

    private void loadSetMenuJson() {
        callDialog("Updating setmenu data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseSetMenu> call = request.getSet_Menu(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseSetMenu>() {
            @Override
            public void onResponse(Call<JSONResponseSetMenu> call, Response<JSONResponseSetMenu> response) {
                try {
                    JSONResponseSetMenu jsonResponse = response.body();
                    mProgressDialog.dismiss();
                    deleteTableVersion("setMenu");
                    download_setMenuArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getSet_menu()));
                    database.beginTransaction();
                    for (Download_SetMenu download_setMenu : download_setMenuArrayList) {
                        ContentValues cv = new ContentValues();
                        cv.put("id", download_setMenu.getId());
                        cv.put("set_menu_name", download_setMenu.getSet_menus_name());
                        cv.put("set_menu_price", download_setMenu.getSet_menus_price());
                        cv.put("image", download_setMenu.getImage());
                        cv.put("status", download_setMenu.getStatus());
                        database.insert("setMenu", null, cv);
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();
                    loadCategoryJson();
                    //loadSetItemJson();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Setmenu is null.");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseSetMenu> call, Throwable t) {
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
                Log.d("ErrorSetMenu", t.getMessage());
            }
        });
    }

    private void loadSetItemJson() {
        callDialog("Updating setitem data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseSetItem> call = request.getSet_Item(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseSetItem>() {
            @Override
            public void onResponse(Call<JSONResponseSetItem> call, Response<JSONResponseSetItem> response) {
                try {
                    JSONResponseSetItem jsonResponse = response.body();
                    mProgressDialog.dismiss();
                    deleteTableVersion("setItem");
                    download_setItemArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getSet_item()));
                    database.beginTransaction();
                    for (Download_SetItem download_setItem : download_setItemArrayList) {
                        ContentValues cv = new ContentValues();
                        cv.put("id", download_setItem.getId());
                        cv.put("set_menu_id", download_setItem.getSet_menu_id());
                        cv.put("item_id", download_setItem.getItem_id());
                        database.insert("setItem", null, cv);
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();
                    loadAddONJson();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Setmenu is null.");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseSetItem> call, Throwable t) {
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
                Log.d("ErrorSetMenu", t.getMessage());
            }
        });
    }

    private void loadAddONJson() {
        callDialog("Updating addon data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseAddOn> call = request.getAddon(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseAddOn>() {
            @Override
            public void onResponse(Call<JSONResponseAddOn> call, Response<JSONResponseAddOn> response) {
                try {
                    JSONResponseAddOn jsonResponse = response.body();
                    mProgressDialog.dismiss();
                    deleteTableVersion("addOn");
                    download_addOnArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getAddon()));
                    database.beginTransaction();
                    for (Download_AddOn download_addOn : download_addOnArrayList) {
                        ContentValues cv = new ContentValues();
                        cv.put("id", download_addOn.getId());
                        cv.put("food_name", download_addOn.getFood_name());
                        cv.put("category_id", download_addOn.getCategory_id());
                        cv.put("image", download_addOn.getMobile_image());
                        cv.put("price", download_addOn.getPrice());
                        database.insert("addOn", null, cv);
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();
                    loadDiscountJson();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Addon data is null.");
                }

            }

            @Override
            public void onFailure(Call<JSONResponseAddOn> call, Throwable t) {
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
                Log.d("ErrorSetMenu", t.getMessage());
            }
        });
    }

    private void loadDiscountJson() {
        callDialog("Updating discount data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseDiscount> call = request.getDiscount(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseDiscount>() {
            @Override
            public void onResponse(Call<JSONResponseDiscount> call, Response<JSONResponseDiscount> response) {
                try {
                    JSONResponseDiscount jsonResponse = response.body();
                    mProgressDialog.dismiss();
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
                    loadContimentJson();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Discount data is null.");
                }

            }

            @Override
            public void onFailure(Call<JSONResponseDiscount> call, Throwable t) {
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
                Log.d("ErrorDiscount", t.getMessage());
            }
        });
    }

    private void catchEvents() {
        searchTotallist.clear();
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        Log.e("VoucherID", VOUNCHER_ID + "");
        if (VOUNCHER_ID != null) {
            getVouncherDetailData();
        }
        getConfigData();
        getItemForAuotSearch();
        setMenuDataFromDB();
        for (Category item : searchTotallist) {
            searchItemList.add(item.getName());
        }
        searchItemAuto.setAdapter(new ArrayAdapter<String>(
                this, android.R.layout.simple_list_item_1, searchItemList));
        searchItemAuto.setThreshold(1);
        searchItemAuto.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position,
                                    long id) {
                for (Category searchList : searchTotallist) {
                    String itemName = searchList.getName();
                    if (itemName.equals(parent.getItemAtPosition(position).toString())) {
                        Category_Item category_item = new Category_Item();
                        category_item.setId(searchList.getId());
                        if (searchList.getCategory_id().equals("set_menu")) {
                            category_item.setSetid(searchList.getId());
                        }
                        category_item.setItemName(searchList.getName());
                        category_item.setQuantity(searchList.getQuantity());
                        category_item.setPrice(searchList.getPrice());
                        category_item.setStatusid("1");
                        if (TAKE_AWAY == "take") {
                            category_item.setTakeid("1");
                        } else {
                            category_item.setTakeid("0");
                        }
                        category_item.setDiscount(searchList.getDiscount());
                        String discountType = searchList.getDiscount_type();
                        if (discountType == null) {
                            Log.e("DiscountType", "null");
                        } else if (discountType.equals("%")) {
                            double price = searchList.getPrice();
                            double qty = searchList.getQuantity();
                            double discount = searchList.getDiscount();
                            double totalAmt = price * qty;
                            double totalDiscount = totalAmt * discount / 100;
                            category_item.setDiscount(totalDiscount);
                        }
                        category_item.setDiscount_type(searchList.getDiscount_type());
                        category_item.setDiscount_id(searchList.getDiscount_id());
                        category_item.setExtraPrice(searchList.getExtraPrice());
                        category_item.setAmount(searchList.getAmount());
                        if (TAKE_AWAY == "take") {
                            category_item.setTakeAway(true);
                            category_item.setOrder_type_id("2");
                        } else {
                            category_item.setTakeAway(false);
                            category_item.setOrder_type_id("1");
                        }
                        category_item.setCategoryId(searchList.getCategory_id());
                        String value = getAddOnID(searchList.getCategory_id());
                        category_item.setAddOnArrayList(getAddonData(value));
                        TotalitemArraylist.add(category_item);
                        categoryItemAdapter.notifyDataSetChanged();
                    }
                }
                searchItemAuto.setText("");
            }
        });


        CategoryArrayAdapter adapter = new CategoryArrayAdapter(this, categoryDataFromDB("0"));
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(CategoryActivity.this, 2);
        recycleritemView.setLayoutManager(layoutManager);
        recycleritemView.addItemDecoration(new GridSpacingItemDecoration(2, dpToPx(10), true));
        recycleritemView.setItemAnimator(new DefaultItemAnimator());

        categoryItemAdapter = new CategoryItemAdapter(CategoryActivity.this);
        listView.setAdapter(categoryItemAdapter);
        categoryItemAdapter = new CategoryItemAdapter(this);
        layoutManager = new LinearLayoutManager(this);
        listView.setLayoutManager(layoutManager);
        listView.setItemAnimator(new DefaultItemAnimator());
        listView.setAdapter(categoryItemAdapter);
        categoryItemAdapter.notifyDataSetChanged();
        /*listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(CategoryActivity.this, "", Toast.LENGTH_SHORT).show();
                return false;
            }
        });*/
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerState) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recycleritemView.setVisibility(View.GONE);
                    if (count2 == 0) {
                        Log.e("Parent", parent_ID);
                        CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(CategoryActivity.this, categoryDataFromDB(parent_ID));
                        recyclerView.setAdapter(categoryAdapter);
                        categoryAdapter.notifyDataSetChanged();
                        count2++;
                    } else {
                        String cate_id = null;
                        for (Category cat : cateDataFromDB(parent_ID)) {
                            cate_id = cat.getParent_id();
                            parent_ID = cat.getParent_id();
                            Log.e("Parent", parent_ID);
                        }
                        if (categoryDataFromDB(cate_id).size() > 0) {
                            CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(CategoryActivity.this, categoryDataFromDB(cate_id));
                            recyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                            Log.e("Parent", parent_ID);
                        } else {
                            CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(CategoryActivity.this, categoryDataFromDB("0"));
                            recyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    if (count == 0) {
                        CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(CategoryActivity.this, categoryDataFromDB(parent_ID));
                        recyclerView.setAdapter(categoryAdapter);
                        categoryAdapter.notifyDataSetChanged();
                        count++;
                    } else {
                        String cate_id = null;
                        for (Category cat : cateDataFromDB(parent_ID)) {
                            cate_id = cat.getParent_id();
                            parent_ID = cat.getParent_id();
                        }
                        if (categoryDataFromDB(cate_id).size() > 0) {
                            CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(CategoryActivity.this, categoryDataFromDB(cate_id));
                            recyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        } else {
                            CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(CategoryActivity.this, categoryDataFromDB("0"));
                            recyclerView.setAdapter(categoryAdapter);
                            categoryAdapter.notifyDataSetChanged();
                        }
                    }
                }
            }
        });
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (recyclerState) {
                    recyclerView.setVisibility(View.VISIBLE);
                    recycleritemView.setVisibility(View.GONE);
                }
                CategoryArrayAdapter categoryAdapter = new CategoryArrayAdapter(CategoryActivity.this, categoryDataFromDB("0"));
                recyclerView.setAdapter(categoryAdapter);
                categoryAdapter.notifyDataSetChanged();
            }
        });
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadSetMenuJson();
                //loadCategoryJson();
                getItemForAuotSearch();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TotalitemArraylist.size() > 0) {
                    if (VOUNCHER_ID == null || VOUNCHER_ID.equals("NULL")) {
                        uploadOrderData();
                    } else {
                        CompareItemListsUploadUpdate(categoryItemList, TotalitemArraylist);
                        uploadUpdateOrderData();

                    }
                } else {
                    final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                            .setPositiveButton(R.string.invitation_ok, null)
                            .create();
                    builder.setTitle(R.string.alert);
                    builder.setMessage("You must specify at least one product.");
                    builder.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                            btnAccept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    builder.dismiss();
                                }
                            });
                        }
                    });
                    Handler handler = new Handler();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (activity.isFinishing()) {
                                return;
                            } else {
                                builder.show();
                            }
                        }
                    });
                }
                Log.i("InvoiceID", "" + VOUNCHER_ID);
            }
        });
    }

    public class GridSpacingItemDecoration extends RecyclerView.ItemDecoration {
        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public GridSpacingItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)
                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }

    private int dpToPx(int dp) {
        Resources r = getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.take_away_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    private class CategoryItemAdapter extends RecyclerView.Adapter<CategoryItemAdapter.ViewHolder> {


        LayoutInflater inflater;
        Context context;

        public CategoryItemAdapter(Context context) {

            inflater = LayoutInflater.from(context);
            this.context = context;

        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_list_item, parent, false);
            ViewHolder viewHolder = new ViewHolder(view);
            return viewHolder;
        }

        @Override
        public int getItemViewType(int position) {
            return position;
        }

        @Override
        public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
//            viewHolder.setIsRecyclable(false);

            /*if (convertView == null) {
                LayoutInflater layoutInflater = context.getLayoutInflater();
                convertView = layoutInflater.inflate(R.layout.category_list_item, null, true);
                viewHolder = new CategoryItemViewHolder(convertView);
                convertView.setTag(viewHolder);

            } else {
                viewHolder = (CategoryItemViewHolder) convertView.getTag();
            }*/

            categoryTxt.setText("Item");
            final Category_Item categoryItem = TotalitemArraylist.get(position);
            String takeiddd = categoryItem.getTakeid();
            //String statusiddd = categoryItem.getStatusid();

            Log.i("Current position => ", position + "");

            /***
             * PhoneLinAung 11.9.17 Start
             */
            viewHolder.itemNameTxt.setTypeface(font);
            viewHolder.itemNameTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    contimentforItemSelectList.clear();

                    String item_id = TotalitemArraylist.get(position).getId();

                    Cursor cursor = database.rawQuery("SELECT * FROM item where id='" + item_id + "'", null);

                    while (cursor.moveToNext()) {

                        int cookingtime=cursor.getInt(cursor.getColumnIndex("standard_cooking_time"));
                        int has_contiment = cursor.getInt(cursor.getColumnIndex("has_contiment"));

                        if (has_contiment == 1) {

                            group_id = cursor.getString(cursor.getColumnIndex("group_id"));

                            Cursor cursor1 = database.rawQuery("SELECT * FROM item WHERE group_id='" + group_id + "'", null);

                            while (cursor1.moveToNext()) {
                                contimet = new ContimentforItemSelect();
                                String name = cursor1.getString(cursor1.getColumnIndex("name"));
                                contiment_id = cursor1.getInt(cursor1.getColumnIndex("contiment_id"));

                                Cursor cursor2 = database.rawQuery("SELECT * FROM contiment where id=" + contiment_id, null);

                                while (cursor2.moveToNext()) {

                                    contiment_name = cursor2.getString(cursor2.getColumnIndex("name"));

                                }
                                contimet.setContiment_id(contiment_id);
                                contimet.setContiment_name(contiment_name);
                                contimentforItemSelectList.add(contimet);
                            }

                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                    .setPositiveButton(R.string.invitation_ok, null)
                                    .setNegativeButton(R.string.invitation_cancel, null)
                                    .create();
                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View view1 = layoutInflater.inflate(R.layout.category_contiment_dialog, null);

                            TextView cookingTimetxt= (TextView) view1.findViewById(R.id.cookingTime);
                            cookingTimetxt.setText(cookingtime+" mins");

                            contimentnameList.clear();
                            for (ContimentforItemSelect c : contimentforItemSelectList) {

                                contimentnameList.add(c.getContiment_name());

                            }
                            final Spinner contimentSpinner = (Spinner) view1.findViewById(R.id.contimentSpinner);
                            ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(CategoryActivity.this, android.R.layout.simple_spinner_item, contimentnameList);
                            stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            contimentSpinner.setAdapter(stringAdapter);
                            builder.setView(view1);
                            builder.setTitle(categoryItem.getItemName());
                            builder.setMessage("Choose ContimentforItemSelect");

//                            Handler handler = new Handler();
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (activity.isFinishing()) {
//                                        return;
//                                    } else {
//                                        builder.show();
//                                    }
//                                }
//                            });

                            contimentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

//                                    String con_disamount = "";
//                                    String con_distype = "";

                                     selected_ContimentName = contimentnameList.get(i);

                                    for (i = 0; i < contimentnameList.size(); i++) {

                                        if (selected_ContimentName.equals(contimentforItemSelectList.get(i).getContiment_name())) {

                                            selected_Contiment_id = contimentforItemSelectList.get(i).getContiment_id();

//                                            Cursor cursor = database.rawQuery("SELECT * FROM item where contiment_id='" + selected_Contiment_id + "' and group_id='" + group_id + "'", null);
//
//                                            while (cursor.moveToNext()) {
//
//                                                String selected_item_id = cursor.getString(cursor.getColumnIndex("id"));
//                                                double selected_item_price = cursor.getDouble(cursor.getColumnIndex("price"));
//
//                                                Cursor cursorContimentDiscount = database.rawQuery("SELECT * FROM discount WHERE item_id=\"" + selected_item_id + "\"", null);
//
//
//                                                while (cursorContimentDiscount.moveToNext()) {
//
//                                                    con_disamount = cursorContimentDiscount.getString(cursorContimentDiscount.getColumnIndex("amount"));
//                                                    con_distype = cursorContimentDiscount.getString(cursorContimentDiscount.getColumnIndex("type"));
////
//
//
//                                                }
//
//                                                if (con_distype != null && !con_distype.equals("")) {
//
//                                                    if (con_distype.equalsIgnoreCase("%")) {
//
//                                                        double total = selected_item_price * Double.parseDouble(con_disamount);
//                                                        TotalitemArraylist.get(position).setDiscount(total / 100);
//                                                        Log.i("Price", TotalitemArraylist.get(position).getPrice() + "," + Integer.parseInt(con_disamount) + "," + total);
//                                                    } else {
//                                                        TotalitemArraylist.get(position).setDiscount(Double.parseDouble(con_disamount));
//                                                    }
//                                                } else {
//                                                    TotalitemArraylist.get(position).setDiscount(0);
//                                                }
//
//
//                                                Log.i("Select_Item_Price", selected_item_price + "");
//                                                categoryItem.setId(selected_item_id);
//                                                String selectd_itemname = cursor.getString(cursor.getColumnIndex("name"));
//                                                viewHolder.itemNameTxt.setText(selected_ContimentName + " " + selectd_itemname);
//                                                viewHolder.priceTxt.setText(selected_item_price + "");
//                                                viewHolder.amountTxt.setText(selected_item_price + "");
//                                                TotalitemArraylist.get(position).setItemName(selected_ContimentName + " " + selectd_itemname);
//                                                TotalitemArraylist.get(position).setPrice(selected_item_price);
//                                                TotalitemArraylist.get(position).setTotalAmount(selected_item_price);
//                                                double tamt = 0.0;
//                                                for (Category_Item category_item : TotalitemArraylist) {
//                                                    tamt += category_item.getTotalAmount();
//                                                }
//
//                                                tPriceTxt.setText(commaSepFormat.format(tamt));
//                                                double taxValue = tamt * (taxAmt / 100);
//                                                taxAmtTxt.setText(commaSepFormat.format(taxValue));
//                                                taxPercentTxt.setText(taxAmt + "%");
//                                                double serviceValue = tamt * (serviceAmt / 100);
//                                                serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
//                                                servicePercentTxt.setText(serviceAmt + "%");
//                                                double netAmount = (tamt + taxValue + serviceValue);
//                                                tnetPriceTxt.setText(commaSepFormat.format(netAmount));
//
//
//                                            }
//                                            cursor.close();
//                                            categoryItemAdapter.notifyDataSetChanged();
                                        }

                                    }


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });

                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    final Button btnAccept = builder.getButton(DialogInterface.BUTTON_POSITIVE);
                                    btnAccept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Toast.makeText(context, "Toast", Toast.LENGTH_SHORT).show();

                                            Cursor cursor = database.rawQuery("SELECT * FROM item where contiment_id='" + selected_Contiment_id + "' and group_id='" + group_id + "'", null);

                                            while (cursor.moveToNext()) {

                                                String selected_item_id = cursor.getString(cursor.getColumnIndex("id"));
                                                double selected_item_price = cursor.getDouble(cursor.getColumnIndex("price"));

                                                Cursor cursorContimentDiscount = database.rawQuery("SELECT * FROM discount WHERE item_id=\"" + selected_item_id + "\"", null);


                                                while (cursorContimentDiscount.moveToNext()) {

                                                    con_disamount = cursorContimentDiscount.getString(cursorContimentDiscount.getColumnIndex("amount"));
                                                    con_distype = cursorContimentDiscount.getString(cursorContimentDiscount.getColumnIndex("type"));
//


                                                }

                                                if (con_distype != null && !con_distype.equals("")) {

                                                    if (con_distype.equalsIgnoreCase("%")) {

                                                        double total = selected_item_price * Double.parseDouble(con_disamount);
                                                        TotalitemArraylist.get(position).setDiscount(total / 100);
                                                        Log.i("Price", TotalitemArraylist.get(position).getPrice() + "," + Integer.parseInt(con_disamount) + "," + total);
                                                    } else {
                                                        TotalitemArraylist.get(position).setDiscount(Double.parseDouble(con_disamount));
                                                    }
                                                } else {
                                                    TotalitemArraylist.get(position).setDiscount(0);
                                                }


                                                Log.i("Select_Item_Price", selected_item_price + "");
                                                categoryItem.setId(selected_item_id);
                                                String selectd_itemname = cursor.getString(cursor.getColumnIndex("name"));
                                                viewHolder.itemNameTxt.setText(selected_ContimentName + " " + selectd_itemname);
                                                viewHolder.priceTxt.setText(selected_item_price + "");
                                                viewHolder.amountTxt.setText(selected_item_price + "");
                                                TotalitemArraylist.get(position).setItemName(selected_ContimentName + " " + selectd_itemname);
                                                TotalitemArraylist.get(position).setPrice(selected_item_price);
                                                TotalitemArraylist.get(position).setTotalAmount(selected_item_price);
                                                double tamt = 0.0;
                                                for (Category_Item category_item : TotalitemArraylist) {
                                                    tamt += category_item.getTotalAmount();
                                                }

                                                tPriceTxt.setText(commaSepFormat.format(tamt));
                                                double taxValue = tamt * (taxAmt / 100);
                                                taxAmtTxt.setText(commaSepFormat.format(taxValue));
                                                taxPercentTxt.setText(taxAmt + "%");
                                                double serviceValue = tamt * (serviceAmt / 100);
                                                serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
                                                servicePercentTxt.setText(serviceAmt + "%");
                                                double netAmount = (tamt + taxValue + serviceValue);
                                                tnetPriceTxt.setText(commaSepFormat.format(netAmount));


                                            }
                                            cursor.close();
                                            categoryItemAdapter.notifyDataSetChanged();



                                            builder.dismiss();
                                        }
                                    });
                                }
                            });

                            builder.show();


                        } else {

                            Toast.makeText(CategoryActivity.this, "This item has no contiment", Toast.LENGTH_SHORT).show();

                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                    .setPositiveButton(R.string.invitation_ok, null)
                                    .create();
                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View view1 = layoutInflater.inflate(R.layout.category_contiment_dialog, null);

                            Spinner spinner= (Spinner) view1.findViewById(R.id.contimentSpinner);
                            spinner.setVisibility(View.GONE);

                            TextView cookingTimetxt= (TextView) view1.findViewById(R.id.cookingTime);
                            cookingTimetxt.setText(cookingtime+" mins");

                            builder.setView(view1);
                            builder.setTitle(categoryItem.getItemName());
                            builder.setMessage("Item Info");

                            builder.show();

                        }

                    }

                }
            });

            /***
             * PhoneLinAung 12.9.17 End
             */
            String statusiddd = TotalitemArraylist.get(position).getStatusid();
            Log.i("Status", statusiddd);
            if (TAKE_AWAY == "table" || TAKE_AWAY == "room") {     // for from room and table including new invoice and exiting invoice
                if ((statusiddd == "6" || statusiddd.equals("6")) || (statusiddd == "7" || statusiddd.equals("7"))) {
                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
                    viewHolder.itemNameTxt.setPaintFlags(viewHolder.itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
                    viewHolder.quantityBtn.setPaintFlags(viewHolder.quantityBtn.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.quantityBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlight));
                    viewHolder.extraBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlight));
                    viewHolder.priceTxt.setText(commaSepFormat.format(0));
                    viewHolder.priceTxt.setPaintFlags(viewHolder.priceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
                    viewHolder.discountTxt.setPaintFlags(viewHolder.discountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.extraPriceTxt.setText(commaSepFormat.format(0));
                    viewHolder.extraPriceTxt.setPaintFlags(viewHolder.extraPriceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.amountTxt.setText(commaSepFormat.format(0));
                    viewHolder.amountTxt.setPaintFlags(viewHolder.amountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.takeAwayCheck.setEnabled(false);

                } else {
                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
                    viewHolder.priceTxt.setText(commaSepFormat.format(categoryItem.getPrice()));
                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
                }
                if (ADD_INVOICE.equals("NULL") || ADD_INVOICE == null) {
                    viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                    .setPositiveButton(R.string.invitation_ok, null)
                                    .setNegativeButton(R.string.invitation_cancel, null)
                                    .create();
                            builder.setTitle(R.string.clear);
                            builder.setMessage("Do you want to clear this item?");
                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                    btnAccept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            TotalitemArraylist.remove(position);
                                            categoryItemList.remove(position);
                                            categoryItemAdapter.notifyDataSetChanged();
                                            double totalValue = 0;
                                            double teValue = 0;
                                            double tdValue = 0;
                                            double servicecharges = 0;
                                            for (Category_Item catItem : TotalitemArraylist) {
                                                totalValue += catItem.getTotalAmount();
                                                teValue += catItem.getTotalExtraPrice();
                                                tdValue += catItem.getTotalDiscount();
                                            }
                                            if (TotalitemArraylist.size() > 0) {
                                                taxValue = totalValue * taxAmt / 100;
                                                serviceValue = totalValue * serviceAmt / 100;
                                                servicecharges = taxValue + serviceValue;
                                            } else {
                                                taxValue = 0;
                                                serviceValue = 0;
                                                servicecharges = 0;
                                            }
                                            totalAmt = totalValue;
                                            totalDisAmt = tdValue;
                                            totalExtraAmt = teValue;
                                            totalTaxAmt = taxValue;
                                            totalServiceAmt = serviceValue;
                                            tPriceTxt.setText(commaSepFormat.format(totalValue));
                                            taxAmtTxt.setText(commaSepFormat.format(taxValue));
                                            taxPercentTxt.setText(taxAmt + "%");
                                            serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
                                            servicePercentTxt.setText(serviceAmt + "%");

                                            if (check_check.equals("room")) {

                                                roomchargesAmtTxt.setText(commaSepFormat.format(roomchargeAmt));
                                                tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges + roomchargeAmt));

                                            } else {

                                                roomchargesTxt.setVisibility(View.GONE);
                                                roomchargesAmtTxt.setVisibility(View.GONE);
                                                tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));

                                            }


                                            builder.dismiss();
                                        }
                                    });
                                    final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                                    btnDecline.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.d("Clear", "Item");
                                            builder.dismiss();
                                        }
                                    });
                                }
                            });
                            Handler handler = new Handler();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (activity.isFinishing()) {
                                        return;
                                    } else {
                                        builder.show();
                                    }
                                }
                            });
                        }

                    });


                } else if (ADD_INVOICE == "EDITING_INVOICE" || ADD_INVOICE.equals("EDITING_INVOICE") || ADD_INVOICE.equals("status1")) {
                    viewHolder.clearBtn.setEnabled(false);
                    viewHolder.clearBtn.setColorFilter(Color.argb(220, 220, 220, 220));
                    viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(CategoryActivity.this, "Can't Cancel NOW !", Toast.LENGTH_SHORT).show();
                            Log.e("Can't Cancel NOW !", "Can't Cancel NOW !Can't Cancel NOW !");
                        }
                    });
                }
                double extraVlaue = 0;
                for (AddOn addOn : categoryItem.getAddOnArrayList()) {

                    if (addOn.isSelected() == true) {
                        extraVlaue += addOn.getPrice();
                    }
                }
                viewHolder.extraPriceTxt.setText(commaSepFormat.format(extraVlaue));
                categoryItem.setExtraPrice(extraVlaue);
//                categoryItemAdapter.notifyDataSetChanged();
                viewHolder.amountTxt.setText(commaSepFormat.format(categoryItem.getTotalAmount()));
                Log.e("TakeAway", TAKE_AWAY + "");
                viewHolder.takeAwayCheck.setChecked(categoryItem.getTakeAway());
                viewHolder.takeAwayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                        @Override
                                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                            if (categoryItem.getOrder_type_id().equals("2")) {
                                                                                Log.e("TakeAway", "true");
                                                                                Log.e("TakeAwayCheckBox ", isChecked + "");
                                                                            }

                                                                            Log.e("TakeAwayCheckBox ", isChecked + "");
                                                                            categoryItem.setTakeAway(isChecked);
                                                                            TotalitemArraylist.set(position, categoryItem);
                                                                            //categoryItemAdapter.notifyDataSetChanged();
                                                                        }
                                                                    }
                );
                double totalValue = 0;
                double teValue = 0;
                double tdValue = 0;
                for (Category_Item catItem : TotalitemArraylist) {
                    Log.i("ggggggggg>>>>>>>>", catItem.getStatusid() + "");
                    if ((catItem.getStatusid() == "6" || catItem.getStatusid().equals("6")) || (catItem.getStatusid() == "7" || catItem.getStatusid().equals("7"))) {
                        Log.i("ggwp", "ggggggggg>>>>>>>>");
                    } else if (catItem.getStatusid().equals("1") || catItem.getStatusid().equals("2") || catItem.getStatusid().equals("3") || catItem.getStatusid().equals("4") || catItem.getStatusid().equals("5")) {
                        totalValue += catItem.getTotalAmount();
                        teValue += catItem.getTotalExtraPrice();
                        tdValue += catItem.getTotalDiscount();
                    }
                }
                double taxValue = totalValue * taxAmt / 100;
                double serviceValue = totalValue * serviceAmt / 100;
                double servicecharges = taxValue + serviceValue;
                totalDisAmt = tdValue;
                totalExtraAmt = teValue;
                tPriceTxt.setText(commaSepFormat.format(totalValue));
                serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
                servicePercentTxt.setText(serviceAmt + "%");
                taxAmtTxt.setText(commaSepFormat.format(taxValue));
                taxPercentTxt.setText(taxAmt + "%");

                if (check_check.equals("room")) {

                    roomchargesAmtTxt.setText(commaSepFormat.format(roomchargeAmt));
                    tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges + roomchargeAmt));

                } else {

                    roomchargesTxt.setVisibility(View.GONE);
                    roomchargesAmtTxt.setVisibility(View.GONE);
                    tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));

                }
                viewHolder.quantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                .setPositiveButton(R.string.invitation_ok, null)
                                .setNegativeButton(R.string.invitation_cancel, null)
                                .create();
                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View view = layoutInflater.inflate(R.layout.category_quantity_dialog, null);
                        final EditText qtyEdit = (EditText) view.findViewById(R.id.qty_edit);
                        builder.setView(view);
                        builder.setTitle(R.string.quantity_title);
                        builder.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                btnAccept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (qtyEdit.getText().toString().isEmpty()) {
                                            qtyEdit.setError("Quantity is required.");
                                            qtyEdit.requestFocus();
                                        } else {
                                            int qty = Integer.parseInt(qtyEdit.getText().toString());
                                            String idid;
                                            if (categoryItem.getId().equals(null)) {
                                                idid = categoryItem.getSetid();
                                                getPromotionDataInDBforsetmenu(idid);
                                            } else {
                                                idid = categoryItem.getId();
                                                getPromotionDataInDB(/*categoryItem.getId()*/ idid);
                                            }

                                            if (from_date == null && to_date == null && from_time == null && to_time == null) {
                                                Log.e("PromotionItem", "This item is not promotion.");
                                            } else {
                                                try {
                                                    Date CurrentDate = date_format.parse(date_format.format(new Date()));
                                                    if (CurrentDate.equals(from_date) || CurrentDate.after(from_date) && CurrentDate.before(to_date)) {
                                                        Log.e("CurrentDate", CurrentDate + ",From" + from_date + ",To" + to_date);
                                                        Date currentTime = time_format.parse(time_format.format(new Date()));
                                                        Log.e("CurrentTime", currentTime + ",from" + from_time + ",to" + to_time);
                                                        if (currentTime.equals(from_time) || currentTime.equals(to_time) || currentTime.after(from_time) && currentTime.before(to_time)) {// && currentTime.before(to_time)
                                                            Log.e("Promotion", promotion_id);
                                                            if (qty >= sell_quantity) {
                                                                Log.e("SellQuantity", sell_quantity + "");
                                                                categoryItem.setPromotion_id(promotion_id);
                                                            }
                                                        } else {
                                                            Log.e("Promotions", promotion_id);
                                                        }
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Log.e("ItemID", categoryItem.getId());
                                            if (qty == 0) {
                                                qtyEdit.setError("Quantity is required.");
                                            } else {
                                                categoryItem.setQuantity(qty);
                                            }

                                            Log.d("Quantity", "You have entered: " + qty);
                                            builder.dismiss();
                                            categoryItemAdapter.notifyDataSetChanged();
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
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (activity.isFinishing()) {
                                    return;
                                } else {
                                    builder.show();
                                }
                            }
                        });
                    }
                });
                viewHolder.extraBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                .setPositiveButton(R.string.invitation_ok, null)
                                .setNegativeButton(R.string.invitation_cancel, null)
                                .create();
                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View view = layoutInflater.inflate(R.layout.category_extra_dialog, null);
                        ListView addListView = (ListView) view.findViewById(R.id.list_view);
                        final EditText remarkEdit = (EditText) view.findViewById(R.id.remark_edit);
                        AddOnAdapter addOnAdapter = new AddOnAdapter(CategoryActivity.this, categoryItem.getAddOnArrayList());
                        addListView.setAdapter(addOnAdapter);
                        addOnAdapter.notifyDataSetChanged();
                        remarkEdit.setText(categoryItem.getUserRemark());
                        builder.setView(view);
                        builder.setTitle(R.string.extra_title);
                        builder.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                btnAccept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        categoryItem.setUserRemark(remarkEdit.getText().toString());
                                        Log.d("Quantity", "You have entered: " + remarkEdit.getText().toString());
                                        builder.dismiss();
                                        categoryItemAdapter.notifyDataSetChanged();
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
                        Handler handler = new Handler();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (activity.isFinishing()) {
                                    return;
                                } else {
                                    builder.show();
                                }
                            }
                        });
                    }
                });
                //ADD_INVOICE = null;
            } else {
                Log.i("takeaway!!!!!", TAKE_AWAY + "");      // for from  take away including new invoice and exiting invoice
                if ((statusiddd == "6" || statusiddd.equals("6")) || (statusiddd == "7" || statusiddd.equals("7"))) {
                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
                    viewHolder.itemNameTxt.setPaintFlags(viewHolder.itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
                    viewHolder.quantityBtn.setPaintFlags(viewHolder.quantityBtn.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.quantityBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlight));
                    viewHolder.extraBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlight));
                    viewHolder.priceTxt.setText(commaSepFormat.format(0));
                    viewHolder.priceTxt.setPaintFlags(viewHolder.priceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
                    viewHolder.discountTxt.setPaintFlags(viewHolder.discountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.extraPriceTxt.setText(commaSepFormat.format(0));
                    viewHolder.extraPriceTxt.setPaintFlags(viewHolder.extraPriceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.amountTxt.setText(commaSepFormat.format(0));
                    viewHolder.amountTxt.setPaintFlags(viewHolder.amountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.clearBtn.setEnabled(false);
                    viewHolder.clearBtn.setColorFilter(Color.argb(220, 220, 220, 220));
                    viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(CategoryActivity.this, "Can't Cancel NOW !", Toast.LENGTH_SHORT).show();
                            Log.e("Can't Cancel NOW !", "Can't Cancel NOW !Can't Cancel NOW !");
                        }
                    });
                    viewHolder.takeAwayCheck.setEnabled(false);
                } else {
                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
                    viewHolder.priceTxt.setText(commaSepFormat.format(categoryItem.getPrice()));
                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
                    if (ADD_INVOICE == "EDITING_INVOICE") {
                        viewHolder.clearBtn.setEnabled(false);
                        viewHolder.clearBtn.setColorFilter(Color.argb(220, 220, 220, 220));
                        viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(CategoryActivity.this, "Can't Cancel NOW !", Toast.LENGTH_SHORT).show();
                                Log.e("Can't Cancel NOW !", "Can't Cancel NOW !Can't Cancel NOW !");
                            }
                        });
                    } else if (ADD_INVOICE == "NULL" || ADD_INVOICE == null) {
                        viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                        .setPositiveButton(R.string.invitation_ok, null)
                                        .setNegativeButton(R.string.invitation_cancel, null)
                                        .create();
                                builder.setTitle(R.string.clear);
                                builder.setMessage("Do you want to clear this item?");
                                builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                        btnAccept.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                TotalitemArraylist.remove(position);
                                                categoryItemList.remove(position);
                                                categoryItemAdapter.notifyDataSetChanged();
                                                double totalValue = 0;
                                                double teValue = 0;
                                                double tdValue = 0;
                                                double servicecharges = 0;
                                                for (Category_Item catItem : TotalitemArraylist) {
                                                    totalValue += catItem.getTotalAmount();
                                                    teValue += catItem.getTotalExtraPrice();
                                                    tdValue += catItem.getTotalDiscount();
                                                }
                                                if (TotalitemArraylist.size() > 0) {
                                                    taxValue = totalValue * taxAmt / 100;
                                                    serviceValue = totalValue * serviceAmt / 100;
                                                    servicecharges = taxValue + serviceValue;
                                                } else {
                                                    taxValue = 0;
                                                    serviceValue = 0;
                                                    servicecharges = 0;
                                                }
                                                totalAmt = totalValue;
                                                totalDisAmt = tdValue;
                                                totalExtraAmt = teValue;
                                                totalTaxAmt = taxValue;
                                                totalServiceAmt = serviceValue;
                                                tPriceTxt.setText(commaSepFormat.format(totalValue));
                                                taxAmtTxt.setText(commaSepFormat.format(taxValue));
                                                taxPercentTxt.setText(taxAmt + "%");
                                                serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
                                                servicePercentTxt.setText(serviceAmt + "%");

                                                if (check_check.equals("room")) {

                                                    roomchargesAmtTxt.setText(commaSepFormat.format(roomchargeAmt));
                                                    tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges + roomchargeAmt));

                                                } else {

                                                    roomchargesTxt.setVisibility(View.GONE);
                                                    roomchargesAmtTxt.setVisibility(View.GONE);
                                                    tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));

                                                }

                                                builder.dismiss();
                                            }
                                        });
                                        final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                                        btnDecline.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Log.d("Clear", "Item");
                                                builder.dismiss();
                                            }
                                        });
                                    }
                                });
                                Handler handler = new Handler();
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (activity.isFinishing()) {
                                            return;
                                        } else {
                                            builder.show();
                                        }
                                    }
                                });
                            }
                        });
                    }
                    double extraVlaue = 0;

                    if (categoryItem.getAddOnArrayList() != null) {

                        for (AddOn addOn : categoryItem.getAddOnArrayList()) {
                            if (addOn.isSelected() == true) {
                                extraVlaue += addOn.getPrice();
                            }
                        }
                    }
                    viewHolder.extraPriceTxt.setText(commaSepFormat.format(extraVlaue));
                    categoryItem.setExtraPrice(extraVlaue);
//                    categoryItemAdapter.notifyDataSetChanged();
                    viewHolder.amountTxt.setText(commaSepFormat.format(categoryItem.getTotalAmount()));
                    Log.e("TakeAway", TAKE_AWAY + "");
                    viewHolder.takeAwayCheck.setChecked(categoryItem.getTakeAway());
                    if (takeiddd.equals("1")) {
                        viewHolder.takeAwayCheck.setEnabled(false);
                    }

                    Log.e("TakeAway", takeiddd);
                    viewHolder.takeAwayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                                            @Override
                                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                                if (categoryItem.getOrder_type_id().equals("2")) {
                                                                                    Log.e("TakeAway", "true");
                                                                                    Log.e("TakeAwayCheckBox ", isChecked + "");
                                                                                }
                                                                                categoryItem.setTakeAway(isChecked);
                                                                                Log.e("TakeAwayCheckBox ", isChecked + "");
                                                                                TotalitemArraylist.set(position, categoryItem);
                                                                                //categoryItemAdapter.notifyDataSetChanged();
                                                                            }
                                                                        }
                    );
                    double totalValue = 0;
                    double teValue = 0;
                    double tdValue = 0;
                    for (Category_Item catItem : TotalitemArraylist) {
                        Log.i("ggggggggg>>>>>>>>", catItem.getStatusid() + "");
                        if ((catItem.getStatusid() == "6" || catItem.getStatusid().equals("6")) || (catItem.getStatusid() == "7" || catItem.getStatusid().equals("7"))) {
                            Log.i("ggwp", "ggggggggg>>>>>>>>");
                        } else if (catItem.getStatusid().equals("1") || catItem.getStatusid().equals("2") || catItem.getStatusid().equals("3") || catItem.getStatusid().equals("4") || catItem.getStatusid().equals("5")) {
                            totalValue += catItem.getTotalAmount();
                            teValue += catItem.getTotalExtraPrice();
                            tdValue += catItem.getTotalDiscount();
                        }
                    }
                    double taxValue = totalValue * taxAmt / 100;
                    double serviceValue = totalValue * serviceAmt / 100;
                    double servicecharges = taxValue + serviceValue;
                    totalDisAmt = tdValue;
                    totalExtraAmt = teValue;
                    tPriceTxt.setText(commaSepFormat.format(totalValue));
                    serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
                    servicePercentTxt.setText(serviceAmt + "%");
                    taxAmtTxt.setText(commaSepFormat.format(taxValue));
                    taxPercentTxt.setText(taxAmt + "%");

                    if (check_check.equals("room")) {

                        roomchargesAmtTxt.setText(commaSepFormat.format(roomchargeAmt));
                        tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges + roomchargeAmt));

                    } else {

                        roomchargesTxt.setVisibility(View.GONE);
                        roomchargesAmtTxt.setVisibility(View.GONE);
                        tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));

                    }

                    viewHolder.quantityBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                    .setPositiveButton(R.string.invitation_ok, null)
                                    .setNegativeButton(R.string.invitation_cancel, null)
                                    .create();
                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View view = layoutInflater.inflate(R.layout.category_quantity_dialog, null);
                            final EditText qtyEdit = (EditText) view.findViewById(R.id.qty_edit);
                            builder.setView(view);
                            builder.setTitle(R.string.quantity_title);
                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                    btnAccept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (qtyEdit.getText().toString().isEmpty()) {
                                                qtyEdit.setError("Quantity is required.");
                                                qtyEdit.requestFocus();
                                            } else {
                                                int qty = Integer.valueOf(qtyEdit.getText().toString());
                                                getPromotionDataInDB(categoryItem.getId());
                                                if (from_date == null && to_date == null && from_time == null && to_time == null) {
                                                    Log.e("PromotionItem", "This item is not promotion.");
                                                } else {
                                                    try {
                                                        Date CurrentDate = date_format.parse(date_format.format(new Date()));
                                                        if (CurrentDate.equals(from_date) || CurrentDate.after(from_date) && CurrentDate.before(to_date)) {
                                                            Log.e("CurrentDate", CurrentDate + ",From" + from_date + ",To" + to_date);
                                                            Date currentTime = time_format.parse(time_format.format(new Date()));
                                                            Log.e("CurrentTime", currentTime + ",from" + from_time + ",to" + to_time);
                                                            if (currentTime.equals(from_time) || currentTime.equals(to_time) || currentTime.after(from_time) && currentTime.before(to_time)) {// && currentTime.before(to_time)
                                                                Log.e("Promotion", promotion_id);
                                                                if (qty >= sell_quantity) {
                                                                    Log.e("SellQuantity", sell_quantity + "");
                                                                    categoryItem.setPromotion_id(promotion_id);
                                                                }
                                                            } else {
                                                                Log.e("Promotions", promotion_id);
                                                            }
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                //Log.e("ItemID", categoryItem.getId());
                                                if (qty == 0) {
                                                    qtyEdit.setError("Quantity is required.");
                                                } else {
                                                    categoryItem.setQuantity(qty);
                                                    //adapterList.get(position).setQuantity(qty);
                                                }
                                                Log.d("Quantity", "You have entered: " + qty);
                                                builder.dismiss();
                                                categoryItemAdapter.notifyDataSetChanged();
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
                            Handler handler = new Handler();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (activity.isFinishing()) {
                                        return;
                                    } else {
                                        builder.show();
                                    }
                                }
                            });
                        }
                    });
                    viewHolder.extraBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                    .setPositiveButton(R.string.invitation_ok, null)
                                    .setNegativeButton(R.string.invitation_cancel, null)
                                    .create();
                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View view = layoutInflater.inflate(R.layout.category_extra_dialog, null);
                            ListView addListView = (ListView) view.findViewById(R.id.list_view);
                            final EditText remarkEdit = (EditText) view.findViewById(R.id.remark_edit);
                            AddOnAdapter addOnAdapter = new AddOnAdapter(CategoryActivity.this, categoryItem.getAddOnArrayList());
                            addListView.setAdapter(addOnAdapter);
                            addOnAdapter.notifyDataSetChanged();
                            remarkEdit.setText(categoryItem.getUserRemark());
                            builder.setView(view);
                            builder.setTitle(R.string.extra_title);
                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                    btnAccept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            categoryItem.setUserRemark(remarkEdit.getText().toString());
                                            Log.d("Quantity", "You have entered: " + remarkEdit.getText().toString());
                                            builder.dismiss();
                                            categoryItemAdapter.notifyDataSetChanged();
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
                            Handler handler = new Handler();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    if (activity.isFinishing()) {
                                        return;
                                    } else {
                                        builder.show();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        }

        @Override
        public int getItemCount() {
            return TotalitemArraylist.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {


            public TextView itemNameTxt;
            Button quantityBtn;
            TextView priceTxt;
            TextView discountTxt;
            Button extraBtn;
            TextView extraPriceTxt;
            TextView amountTxt;
            CheckBox takeAwayCheck;
            ImageView clearBtn;

            public ViewHolder(View view) {
                super(view);

                itemNameTxt = (TextView) view.findViewById(R.id.item_name_txt);
                quantityBtn = (Button) view.findViewById(R.id.quantity_btn);
                priceTxt = (TextView) view.findViewById(R.id.price_txt);
                discountTxt = (TextView) view.findViewById(R.id.discount_txt);
                extraBtn = (Button) view.findViewById(R.id.extra_btn);
                extraPriceTxt = (TextView) view.findViewById(R.id.extra_price_txt);
                amountTxt = (TextView) view.findViewById(R.id.amount_txt);
                takeAwayCheck = (CheckBox) view.findViewById(R.id.take_away_check);
                clearBtn = (ImageView) view.findViewById(R.id.clear_btn);

            }
        }
    }

//    private class CategoryItemAdapter extends ArrayAdapter<Category_Item> {
//        public final Activity context;
//
//        public CategoryItemAdapter(Activity context) {
//            super(context, R.layout.category_list_item, TotalitemArraylist);
//            // Log.i("categoryItemList", categoryItemList.size() + "");
//            this.context = context;
//        }
//
//        @Override
//        public View getView(final int position, View convertView, ViewGroup parent) {
//            final CategoryItemViewHolder viewHolder;
//            if (convertView == null) {
//                LayoutInflater layoutInflater = context.getLayoutInflater();
//                convertView = layoutInflater.inflate(R.layout.category_list_item, null, true);
//                viewHolder = new CategoryItemViewHolder(convertView);
//                convertView.setTag(viewHolder);
//
//            } else {
//                viewHolder = (CategoryItemViewHolder) convertView.getTag();
//            }
//
//            categoryTxt.setText("Item");
//            final Category_Item categoryItem = TotalitemArraylist.get(position);
//            String takeiddd = categoryItem.getTakeid();
//            //String statusiddd = categoryItem.getStatusid();
//
//            Log.i("Current position => ",position+"");
//
//            /***
//             * PhoneLinAung 11.9.17 Start
//             */
//            viewHolder.itemNameTxt.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    contimentforItemSelectList.clear();
//
//                    String item_id = TotalitemArraylist.get(position).getId();
//
//                    Cursor cursor = database.rawQuery("SELECT * FROM item where id='" + item_id + "'", null);
//
//                    while (cursor.moveToNext()) {
//
//                        int has_contiment = cursor.getInt(cursor.getColumnIndex("has_contiment"));
//
//                        if (has_contiment == 1) {
//
//                            group_id = cursor.getString(cursor.getColumnIndex("group_id"));
//
//                            Cursor cursor1 = database.rawQuery("SELECT * FROM item WHERE group_id='" + group_id + "'", null);
//
//                            while (cursor1.moveToNext()) {
//                                contimet = new ContimentforItemSelect();
//                                String name = cursor1.getString(cursor1.getColumnIndex("name"));
//                                contiment_id = cursor1.getInt(cursor1.getColumnIndex("contiment_id"));
//
//                                Cursor cursor2 = database.rawQuery("SELECT * FROM contiment where id=" + contiment_id, null);
//
//                                while (cursor2.moveToNext()) {
//
//                                    contiment_name = cursor2.getString(cursor2.getColumnIndex("name"));
//
//                                }
//                                contimet.setContiment_id(contiment_id);
//                                contimet.setContiment_name(contiment_name);
//                                contimentforItemSelectList.add(contimet);
//                            }
//
//                            Toast.makeText(CategoryActivity.this, categoryItem.getItemName(), Toast.LENGTH_SHORT).show();
//                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
//                                    .setPositiveButton(R.string.invitation_ok, null)
//                                    .setNegativeButton(R.string.invitation_cancel, null)
//                                    .create();
//                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            final View view1 = layoutInflater.inflate(R.layout.category_contiment_dialog, null);
//
//                            contimentnameList.clear();
//                            for (ContimentforItemSelect c : contimentforItemSelectList) {
//
//                                contimentnameList.add(c.getContiment_name());
//
//                            }
//                            final Spinner contimentSpinner = (Spinner) view1.findViewById(R.id.contimentSpinner);
//                            ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(CategoryActivity.this, android.R.layout.simple_spinner_item, contimentnameList);
//                            stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//                            contimentSpinner.setAdapter(stringAdapter);
//                            builder.setView(view1);
//                            builder.setTitle(categoryItem.getItemName());
//                            builder.setMessage("Choose ContimentforItemSelect");
//                            builder.show();
//
//                            contimentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                                @Override
//                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//
//                                    String selected_ContimentName = contimentnameList.get(i);
//                                    Toast.makeText(CategoryActivity.this, selected_ContimentName, Toast.LENGTH_SHORT).show();
//
//                                    // itemNameTxt.setText(selected_ContimentName + categoryItem.getItemName());
//
//                                    for (i = 0; i < contimentnameList.size(); i++) {
//
//                                        if (selected_ContimentName.equals(contimentforItemSelectList.get(i).getContiment_name())) {
//
//                                            selected_Contiment_id = contimentforItemSelectList.get(i).getContiment_id();
//
//                                            Cursor cursor = database.rawQuery("SELECT * FROM item where contiment_id='" + selected_Contiment_id + "' and group_id='" + group_id + "'", null);
//
//                                            while (cursor.moveToNext()) {
//
//                                                String selected_item_id = cursor.getString(cursor.getColumnIndex("id"));
//                                                double selected_item_price = cursor.getDouble(cursor.getColumnIndex("price"));
//                                                categoryItem.setId(selected_item_id);
//                                                String selectd_itemname = cursor.getString(cursor.getColumnIndex("name"));
//                                                viewHolder.itemNameTxt.setText(selected_ContimentName + " " + selectd_itemname);
//                                                viewHolder.priceTxt.setText(selected_item_price + "");
//                                                viewHolder.amountTxt.setText(selected_item_price + "");
//                                                TotalitemArraylist.get(position).setItemName(selected_ContimentName + " " + selectd_itemname);
//                                                TotalitemArraylist.get(position).setPrice(selected_item_price);
//
//                                            }
//
//                                        }
//
//                                    }
//
//
//                                }
//
//                                @Override
//                                public void onNothingSelected(AdapterView<?> adapterView) {
//
//                                }
//                            });
//
//
//                        } else {
//
//                            Toast.makeText(CategoryActivity.this, "This item has no contiment", Toast.LENGTH_SHORT).show();
//
//                        }
//
//                    }
//
//                }
//            });
//
//            /***
//             * PhoneLinAung 12.9.17 End
//             */
//            String statusiddd = TotalitemArraylist.get(position).getStatusid();
//            if (TAKE_AWAY == "table" || TAKE_AWAY == "room") {     // for from room and table including new invoice and exiting invoice
//                if ((statusiddd == "6" || statusiddd.equals("6")) || (statusiddd == "7" || statusiddd.equals("7"))) {
//                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
//                    viewHolder.itemNameTxt.setPaintFlags(viewHolder.itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
//                    viewHolder.quantityBtn.setPaintFlags(viewHolder.quantityBtn.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.quantityBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlight));
//                    viewHolder.extraBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlight));
//                    viewHolder.priceTxt.setText(commaSepFormat.format(0));
//                    viewHolder.priceTxt.setPaintFlags(viewHolder.priceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
//                    viewHolder.discountTxt.setPaintFlags(viewHolder.discountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.extraPriceTxt.setText(commaSepFormat.format(0));
//                    viewHolder.extraPriceTxt.setPaintFlags(viewHolder.extraPriceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.amountTxt.setText(commaSepFormat.format(0));
//                    viewHolder.amountTxt.setPaintFlags(viewHolder.amountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.takeAwayCheck.setEnabled(false);
//
//                } else {
//                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
//                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
//                    viewHolder.priceTxt.setText(commaSepFormat.format(categoryItem.getPrice()));
//                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
//                }
//                if (ADD_INVOICE.equals("NULL") || ADD_INVOICE == null) {
//                    viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
//                                    .setPositiveButton(R.string.invitation_ok, null)
//                                    .setNegativeButton(R.string.invitation_cancel, null)
//                                    .create();
//                            builder.setTitle(R.string.clear);
//                            builder.setMessage("Do you want to clear this item?");
//                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
//                                @Override
//                                public void onShow(DialogInterface dialog) {
//                                    final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
//                                    btnAccept.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            TotalitemArraylist.remove(position);
//                                            categoryItemList.remove(position);
//                                            categoryItemAdapter.notifyDataSetChanged();
//                                            double totalValue = 0;
//                                            double teValue = 0;
//                                            double tdValue = 0;
//                                            double servicecharges = 0;
//                                            for (Category_Item catItem : TotalitemArraylist) {
//                                                totalValue += catItem.getTotalAmount();
//                                                teValue += catItem.getTotalExtraPrice();
//                                                tdValue += catItem.getTotalDiscount();
//                                            }
//                                            if (TotalitemArraylist.size() > 0) {
//                                                taxValue = totalValue * taxAmt / 100;
//                                                serviceValue = totalValue * serviceAmt / 100;
//                                                servicecharges = taxValue + serviceValue;
//                                            } else {
//                                                servicecharges = 0;
//                                            }
//                                            totalAmt = totalValue;
//                                            totalDisAmt = tdValue;
//                                            totalExtraAmt = teValue;
//                                            totalTaxAmt = taxValue;
//                                            totalServiceAmt = serviceValue;
//                                            tPriceTxt.setText(commaSepFormat.format(totalValue));
//                                            taxAmtTxt.setText(commaSepFormat.format(taxValue));
//                                            taxPercentTxt.setText(taxAmt + "%");
//                                            serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
//                                            servicePercentTxt.setText(serviceAmt + "%");
//
//                                            if (check_check.equals("room")) {
//
//                                                roomchargesAmtTxt.setText(commaSepFormat.format(roomchargeAmt));
//                                                tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges + roomchargeAmt));
//
//                                            } else {
//
//                                                roomchargesTxt.setVisibility(View.GONE);
//                                                roomchargesAmtTxt.setVisibility(View.GONE);
//                                                tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));
//
//                                            }
//
//
//                                            builder.dismiss();
//                                        }
//                                    });
//                                    final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
//                                    btnDecline.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Log.d("Clear", "Item");
//                                            builder.dismiss();
//                                        }
//                                    });
//                                }
//                            });
//                            builder.show();
//                        }
//
//                    });
//
//
//                } else if (ADD_INVOICE == "EDITING_INVOICE" || ADD_INVOICE.equals("EDITING_INVOICE") || ADD_INVOICE.equals("status1")) {
//                    viewHolder.clearBtn.setEnabled(false);
//                    viewHolder.clearBtn.setColorFilter(Color.argb(220, 220, 220, 220));
//                    viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(CategoryActivity.this, "Can't Cancel NOW !", Toast.LENGTH_SHORT).show();
//                            Log.e("Can't Cancel NOW !", "Can't Cancel NOW !Can't Cancel NOW !");
//                        }
//                    });
//                }
//                double extraVlaue = 0;
//                for (AddOn addOn : categoryItem.getAddOnArrayList()) {
//
//                    if (addOn.isSelected() == true) {
//                        extraVlaue += addOn.getPrice();
//                    }
//                }
//                viewHolder.extraPriceTxt.setText(commaSepFormat.format(extraVlaue));
//                categoryItem.setExtraPrice(extraVlaue);
//                categoryItemAdapter.notifyDataSetChanged();
//                viewHolder.amountTxt.setText(commaSepFormat.format(categoryItem.getTotalAmount()));
//                Log.e("TakeAway", TAKE_AWAY + "");
//                viewHolder.takeAwayCheck.setChecked(categoryItem.getTakeAway());
//                viewHolder.takeAwayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//                                                                        @Override
//                                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                                                            if (categoryItem.getOrder_type_id().equals("2")) {
//                                                                                Log.e("TakeAway", "true");
//                                                                                Log.e("TakeAwayCheckBox ", isChecked + "");
//                                                                            }
//
//                                                                            Log.e("TakeAwayCheckBox ", isChecked + "");
//                                                                            categoryItem.setTakeAway(isChecked);
//                                                                            TotalitemArraylist.set(position, categoryItem);
//                                                                            //categoryItemAdapter.notifyDataSetChanged();
//                                                                        }
//                                                                    }
//                );
//                double totalValue = 0;
//                double teValue = 0;
//                double tdValue = 0;
//                for (Category_Item catItem : TotalitemArraylist) {
//                    Log.i("ggggggggg>>>>>>>>", catItem.getStatusid() + "");
//                    if ((catItem.getStatusid() == "6" || catItem.getStatusid().equals("6")) || (catItem.getStatusid() == "7" || catItem.getStatusid().equals("7"))) {
//                        Log.i("ggwp", "ggggggggg>>>>>>>>");
//                    } else if (catItem.getStatusid().equals("1") || catItem.getStatusid().equals("2") || catItem.getStatusid().equals("3") || catItem.getStatusid().equals("4") || catItem.getStatusid().equals("5")) {
//                        totalValue += catItem.getTotalAmount();
//                        teValue += catItem.getTotalExtraPrice();
//                        tdValue += catItem.getTotalDiscount();
//                    }
//                }
//                double taxValue = totalValue * taxAmt / 100;
//                double serviceValue = totalValue * serviceAmt / 100;
//                double servicecharges = taxValue + serviceValue;
//                totalDisAmt = tdValue;
//                totalExtraAmt = teValue;
//                tPriceTxt.setText(commaSepFormat.format(totalValue));
//                serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
//                servicePercentTxt.setText(serviceAmt + "%");
//                taxAmtTxt.setText(commaSepFormat.format(taxValue));
//                taxPercentTxt.setText(taxAmt + "%");
//
//                if (check_check.equals("room")) {
//
//                    roomchargesAmtTxt.setText(commaSepFormat.format(roomchargeAmt));
//                    tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges + roomchargeAmt));
//
//                } else {
//
//                    roomchargesTxt.setVisibility(View.GONE);
//                    roomchargesAmtTxt.setVisibility(View.GONE);
//                    tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));
//
//                }
//                viewHolder.quantityBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
//                                .setPositiveButton(R.string.invitation_ok, null)
//                                .setNegativeButton(R.string.invitation_cancel, null)
//                                .create();
//                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        final View view = layoutInflater.inflate(R.layout.category_quantity_dialog, null);
//                        final EditText qtyEdit = (EditText) view.findViewById(R.id.qty_edit);
//                        builder.setView(view);
//                        builder.setTitle(R.string.quantity_title);
//                        builder.setOnShowListener(new DialogInterface.OnShowListener() {
//                            @Override
//                            public void onShow(DialogInterface dialog) {
//                                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
//                                btnAccept.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        if (qtyEdit.getText().toString().isEmpty()) {
//                                            qtyEdit.setError("Quantity is required.");
//                                            qtyEdit.requestFocus();
//                                        } else {
//                                            int qty = Integer.parseInt(qtyEdit.getText().toString());
//                                            String idid;
//                                            if (categoryItem.getId().equals(null)) {
//                                                idid = categoryItem.getSetid();
//                                                getPromotionDataInDBforsetmenu(idid);
//                                            } else {
//                                                idid = categoryItem.getId();
//                                                getPromotionDataInDB(/*categoryItem.getId()*/ idid);
//                                            }
//
//                                            if (from_date == null && to_date == null && from_time == null && to_time == null) {
//                                                Log.e("PromotionItem", "This item is not promotion.");
//                                            } else {
//                                                try {
//                                                    Date CurrentDate = date_format.parse(date_format.format(new Date()));
//                                                    if (CurrentDate.equals(from_date) || CurrentDate.after(from_date) && CurrentDate.before(to_date)) {
//                                                        Log.e("CurrentDate", CurrentDate + ",From" + from_date + ",To" + to_date);
//                                                        Date currentTime = time_format.parse(time_format.format(new Date()));
//                                                        Log.e("CurrentTime", currentTime + ",from" + from_time + ",to" + to_time);
//                                                        if (currentTime.equals(from_time) || currentTime.equals(to_time) || currentTime.after(from_time) && currentTime.before(to_time)) {// && currentTime.before(to_time)
//                                                            Log.e("Promotion", promotion_id);
//                                                            if (qty >= sell_quantity) {
//                                                                Log.e("SellQuantity", sell_quantity + "");
//                                                                categoryItem.setPromotion_id(promotion_id);
//                                                            }
//                                                        } else {
//                                                            Log.e("Promotions", promotion_id);
//                                                        }
//                                                    }
//                                                } catch (ParseException e) {
//                                                    e.printStackTrace();
//                                                }
//                                            }
//                                            Log.e("ItemID", categoryItem.getId());
//                                            if (qty == 0) {
//                                                qtyEdit.setError("Quantity is required.");
//                                            } else {
//                                                categoryItem.setQuantity(qty);
//                                            }
//
//                                            Log.d("Quantity", "You have entered: " + qty);
//                                            builder.dismiss();
//                                            categoryItemAdapter.notifyDataSetChanged();
//                                        }
//                                    }
//                                });
//                                final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
//                                btnDecline.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Log.d("Quantity", "Invitation declined");
//                                        builder.dismiss();
//                                    }
//                                });
//                            }
//                        });
//                        builder.show();
//                    }
//                });
//                viewHolder.extraBtn.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(final View v) {
//                        final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
//                                .setPositiveButton(R.string.invitation_ok, null)
//                                .setNegativeButton(R.string.invitation_cancel, null)
//                                .create();
//                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                        final View view = layoutInflater.inflate(R.layout.category_extra_dialog, null);
//                        ListView addListView = (ListView) view.findViewById(R.id.list_view);
//                        final EditText remarkEdit = (EditText) view.findViewById(R.id.remark_edit);
//                        AddOnAdapter addOnAdapter = new AddOnAdapter(CategoryActivity.this, categoryItem.getAddOnArrayList());
//                        addListView.setAdapter(addOnAdapter);
//                        addOnAdapter.notifyDataSetChanged();
//                        remarkEdit.setText(categoryItem.getUserRemark());
//                        builder.setView(view);
//                        builder.setTitle(R.string.extra_title);
//                        builder.setOnShowListener(new DialogInterface.OnShowListener() {
//                            @Override
//                            public void onShow(DialogInterface dialog) {
//                                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
//                                btnAccept.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        categoryItem.setUserRemark(remarkEdit.getText().toString());
//                                        Log.d("Quantity", "You have entered: " + remarkEdit.getText().toString());
//                                        builder.dismiss();
//                                        categoryItemAdapter.notifyDataSetChanged();
//                                    }
//                                });
//                                final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
//                                btnDecline.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        Log.d("Quantity", "Invitation declined");
//                                        builder.dismiss();
//                                    }
//                                });
//                            }
//                        });
//                        builder.show();
//                    }
//                });
//                //ADD_INVOICE = null;
//            } else {
//                Log.i("takeaway!!!!!", TAKE_AWAY + "");      // for from  take away including new invoice and exiting invoice
//                if ((statusiddd == "6" || statusiddd.equals("6")) || (statusiddd == "7" || statusiddd.equals("7"))) {
//                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
//                    viewHolder.itemNameTxt.setPaintFlags(viewHolder.itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
//                    viewHolder.quantityBtn.setPaintFlags(viewHolder.quantityBtn.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.quantityBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlight));
//                    viewHolder.extraBtn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorHighlight));
//                    viewHolder.priceTxt.setText(commaSepFormat.format(0));
//                    viewHolder.priceTxt.setPaintFlags(viewHolder.priceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
//                    viewHolder.discountTxt.setPaintFlags(viewHolder.discountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.extraPriceTxt.setText(commaSepFormat.format(0));
//                    viewHolder.extraPriceTxt.setPaintFlags(viewHolder.extraPriceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.amountTxt.setText(commaSepFormat.format(0));
//                    viewHolder.amountTxt.setPaintFlags(viewHolder.amountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
//                    viewHolder.clearBtn.setEnabled(false);
//                    viewHolder.clearBtn.setColorFilter(Color.argb(220, 220, 220, 220));
//                    viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            Toast.makeText(CategoryActivity.this, "Can't Cancel NOW !", Toast.LENGTH_SHORT).show();
//                            Log.e("Can't Cancel NOW !", "Can't Cancel NOW !Can't Cancel NOW !");
//                        }
//                    });
//                    viewHolder.takeAwayCheck.setEnabled(false);
//                } else {
//                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
//                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
//                    viewHolder.priceTxt.setText(commaSepFormat.format(categoryItem.getPrice()));
//                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
//                    if (ADD_INVOICE == "EDITING_INVOICE") {
//                        viewHolder.clearBtn.setEnabled(false);
//                        viewHolder.clearBtn.setColorFilter(Color.argb(220, 220, 220, 220));
//                        viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                Toast.makeText(CategoryActivity.this, "Can't Cancel NOW !", Toast.LENGTH_SHORT).show();
//                                Log.e("Can't Cancel NOW !", "Can't Cancel NOW !Can't Cancel NOW !");
//                            }
//                        });
//                    } else if (ADD_INVOICE == "NULL" || ADD_INVOICE == null) {
//                        viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View v) {
//                                final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
//                                        .setPositiveButton(R.string.invitation_ok, null)
//                                        .setNegativeButton(R.string.invitation_cancel, null)
//                                        .create();
//                                builder.setTitle(R.string.clear);
//                                builder.setMessage("Do you want to clear this item?");
//                                builder.setOnShowListener(new DialogInterface.OnShowListener() {
//                                    @Override
//                                    public void onShow(DialogInterface dialog) {
//                                        final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
//                                        btnAccept.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                TotalitemArraylist.remove(position);
//                                                categoryItemList.remove(position);
//                                                categoryItemAdapter.notifyDataSetChanged();
//                                                double totalValue = 0;
//                                                double teValue = 0;
//                                                double tdValue = 0;
//                                                double servicecharges = 0;
//                                                for (Category_Item catItem : TotalitemArraylist) {
//                                                    totalValue += catItem.getTotalAmount();
//                                                    teValue += catItem.getTotalExtraPrice();
//                                                    tdValue += catItem.getTotalDiscount();
//                                                }
//                                                if (TotalitemArraylist.size() > 0) {
//                                                    taxValue = totalValue * taxAmt / 100;
//                                                    serviceValue = totalValue * serviceAmt / 100;
//                                                    servicecharges = taxValue + serviceValue;
//                                                } else {
//                                                    servicecharges = 0;
//                                                }
//                                                totalAmt = totalValue;
//                                                totalDisAmt = tdValue;
//                                                totalExtraAmt = teValue;
//                                                totalTaxAmt = taxValue;
//                                                totalServiceAmt = serviceValue;
//                                                tPriceTxt.setText(commaSepFormat.format(totalValue));
//                                                taxAmtTxt.setText(commaSepFormat.format(taxValue));
//                                                taxPercentTxt.setText(taxAmt + "%");
//                                                serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
//                                                servicePercentTxt.setText(serviceAmt + "%");
//
//                                                if (check_check.equals("room")) {
//
//                                                    roomchargesAmtTxt.setText(commaSepFormat.format(roomchargeAmt));
//                                                    tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges + roomchargeAmt));
//
//                                                } else {
//
//                                                    roomchargesTxt.setVisibility(View.GONE);
//                                                    roomchargesAmtTxt.setVisibility(View.GONE);
//                                                    tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));
//
//                                                }
//
//                                                builder.dismiss();
//                                            }
//                                        });
//                                        final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
//                                        btnDecline.setOnClickListener(new View.OnClickListener() {
//                                            @Override
//                                            public void onClick(View v) {
//                                                Log.d("Clear", "Item");
//                                                builder.dismiss();
//                                            }
//                                        });
//                                    }
//                                });
//                                builder.show();
//                            }
//                        });
//                    }
//                    double extraVlaue = 0;
//
//                    if (categoryItem.getAddOnArrayList() != null) {
//
//                        for (AddOn addOn : categoryItem.getAddOnArrayList()) {
//                            if (addOn.isSelected() == true) {
//                                extraVlaue += addOn.getPrice();
//                            }
//                        }
//                    }
//                    viewHolder.extraPriceTxt.setText(commaSepFormat.format(extraVlaue));
//                    categoryItem.setExtraPrice(extraVlaue);
//                    categoryItemAdapter.notifyDataSetChanged();
//                    viewHolder.amountTxt.setText(commaSepFormat.format(categoryItem.getTotalAmount()));
//                    Log.e("TakeAway", TAKE_AWAY + "");
//                    viewHolder.takeAwayCheck.setChecked(categoryItem.getTakeAway());
//                    if (takeiddd.equals("1")) {
//                        viewHolder.takeAwayCheck.setEnabled(false);
//                    }
//
//                    Log.e("TakeAway", takeiddd);
//                    viewHolder.takeAwayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//                                                                            @Override
//                                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                                                                                if (categoryItem.getOrder_type_id().equals("2")) {
//                                                                                    Log.e("TakeAway", "true");
//                                                                                    Log.e("TakeAwayCheckBox ", isChecked + "");
//                                                                                }
//                                                                                categoryItem.setTakeAway(isChecked);
//                                                                                Log.e("TakeAwayCheckBox ", isChecked + "");
//                                                                                TotalitemArraylist.set(position, categoryItem);
//                                                                                //categoryItemAdapter.notifyDataSetChanged();
//                                                                            }
//                                                                        }
//                    );
//                    double totalValue = 0;
//                    double teValue = 0;
//                    double tdValue = 0;
//                    for (Category_Item catItem : TotalitemArraylist) {
//                        Log.i("ggggggggg>>>>>>>>", catItem.getStatusid() + "");
//                        if ((catItem.getStatusid() == "6" || catItem.getStatusid().equals("6")) || (catItem.getStatusid() == "7" || catItem.getStatusid().equals("7"))) {
//                            Log.i("ggwp", "ggggggggg>>>>>>>>");
//                        } else if (catItem.getStatusid().equals("1") || catItem.getStatusid().equals("2") || catItem.getStatusid().equals("3") || catItem.getStatusid().equals("4") || catItem.getStatusid().equals("5")) {
//                            totalValue += catItem.getTotalAmount();
//                            teValue += catItem.getTotalExtraPrice();
//                            tdValue += catItem.getTotalDiscount();
//                        }
//                    }
//                    double taxValue = totalValue * taxAmt / 100;
//                    double serviceValue = totalValue * serviceAmt / 100;
//                    double servicecharges = taxValue + serviceValue;
//                    totalDisAmt = tdValue;
//                    totalExtraAmt = teValue;
//                    tPriceTxt.setText(commaSepFormat.format(totalValue));
//                    serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
//                    servicePercentTxt.setText(serviceAmt + "%");
//                    taxAmtTxt.setText(commaSepFormat.format(taxValue));
//                    taxPercentTxt.setText(taxAmt + "%");
//
//                    if (check_check.equals("room")) {
//
//                        roomchargesAmtTxt.setText(commaSepFormat.format(roomchargeAmt));
//                        tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges + roomchargeAmt));
//
//                    } else {
//
//                        roomchargesTxt.setVisibility(View.GONE);
//                        roomchargesAmtTxt.setVisibility(View.GONE);
//                        tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));
//
//                    }
//
//                    viewHolder.quantityBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
//                                    .setPositiveButton(R.string.invitation_ok, null)
//                                    .setNegativeButton(R.string.invitation_cancel, null)
//                                    .create();
//                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            final View view = layoutInflater.inflate(R.layout.category_quantity_dialog, null);
//                            final EditText qtyEdit = (EditText) view.findViewById(R.id.qty_edit);
//                            builder.setView(view);
//                            builder.setTitle(R.string.quantity_title);
//                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
//                                @Override
//                                public void onShow(DialogInterface dialog) {
//                                    final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
//                                    btnAccept.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            if (qtyEdit.getText().toString().isEmpty()) {
//                                                qtyEdit.setError("Quantity is required.");
//                                                qtyEdit.requestFocus();
//                                            } else {
//                                                int qty = Integer.valueOf(qtyEdit.getText().toString());
//                                                getPromotionDataInDB(categoryItem.getId());
//                                                if (from_date == null && to_date == null && from_time == null && to_time == null) {
//                                                    Log.e("PromotionItem", "This item is not promotion.");
//                                                } else {
//                                                    try {
//                                                        Date CurrentDate = date_format.parse(date_format.format(new Date()));
//                                                        if (CurrentDate.equals(from_date) || CurrentDate.after(from_date) && CurrentDate.before(to_date)) {
//                                                            Log.e("CurrentDate", CurrentDate + ",From" + from_date + ",To" + to_date);
//                                                            Date currentTime = time_format.parse(time_format.format(new Date()));
//                                                            Log.e("CurrentTime", currentTime + ",from" + from_time + ",to" + to_time);
//                                                            if (currentTime.equals(from_time) || currentTime.equals(to_time) || currentTime.after(from_time) && currentTime.before(to_time)) {// && currentTime.before(to_time)
//                                                                Log.e("Promotion", promotion_id);
//                                                                if (qty >= sell_quantity) {
//                                                                    Log.e("SellQuantity", sell_quantity + "");
//                                                                    categoryItem.setPromotion_id(promotion_id);
//                                                                }
//                                                            } else {
//                                                                Log.e("Promotions", promotion_id);
//                                                            }
//                                                        }
//                                                    } catch (ParseException e) {
//                                                        e.printStackTrace();
//                                                    }
//                                                }
//                                                //Log.e("ItemID", categoryItem.getId());
//                                                if (qty == 0) {
//                                                    qtyEdit.setError("Quantity is required.");
//                                                } else {
//                                                    categoryItem.setQuantity(qty);
//                                                    //adapterList.get(position).setQuantity(qty);
//                                                }
//                                                Log.d("Quantity", "You have entered: " + qty);
//                                                builder.dismiss();
//                                                categoryItemAdapter.notifyDataSetChanged();
//                                            }
//                                        }
//                                    });
//                                    final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
//                                    btnDecline.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Log.d("Quantity", "Invitation declined");
//                                            builder.dismiss();
//                                        }
//                                    });
//                                }
//                            });
//                            builder.show();
//                        }
//                    });
//                    viewHolder.extraBtn.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(final View v) {
//                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
//                                    .setPositiveButton(R.string.invitation_ok, null)
//                                    .setNegativeButton(R.string.invitation_cancel, null)
//                                    .create();
//                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//                            final View view = layoutInflater.inflate(R.layout.category_extra_dialog, null);
//                            ListView addListView = (ListView) view.findViewById(R.id.list_view);
//                            final EditText remarkEdit = (EditText) view.findViewById(R.id.remark_edit);
//                            AddOnAdapter addOnAdapter = new AddOnAdapter(CategoryActivity.this, categoryItem.getAddOnArrayList());
//                            addListView.setAdapter(addOnAdapter);
//                            addOnAdapter.notifyDataSetChanged();
//                            remarkEdit.setText(categoryItem.getUserRemark());
//                            builder.setView(view);
//                            builder.setTitle(R.string.extra_title);
//                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
//                                @Override
//                                public void onShow(DialogInterface dialog) {
//                                    final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
//                                    btnAccept.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            categoryItem.setUserRemark(remarkEdit.getText().toString());
//                                            Log.d("Quantity", "You have entered: " + remarkEdit.getText().toString());
//                                            builder.dismiss();
//                                            categoryItemAdapter.notifyDataSetChanged();
//                                        }
//                                    });
//                                    final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
//                                    btnDecline.setOnClickListener(new View.OnClickListener() {
//                                        @Override
//                                        public void onClick(View v) {
//                                            Log.d("Quantity", "Invitation declined");
//                                            builder.dismiss();
//                                        }
//                                    });
//                                }
//                            });
//                            builder.show();
//                        }
//                    });
//                }
//            }
//            return convertView;
//        }
//
////        public class CategoryItemViewHolder extends RecyclerView.ViewHolder{
////
////            public TextView itemNameTxt;
////            Button quantityBtn;
////            TextView priceTxt;
////            TextView discountTxt;
////            Button extraBtn;
////            TextView extraPriceTxt;
////            TextView amountTxt;
////            CheckBox takeAwayCheck;
////            ImageView clearBtn;
////
////            /*public CategoryItemViewHolder(View itemView) {
////                super(itemView);
////            }*/
////
////            public CategoryItemViewHolder(View view) {
////                super(view);
////                itemNameTxt = (TextView) view.findViewById(R.id.item_name_txt);
////                quantityBtn = (Button) view.findViewById(R.id.quantity_btn);
////                priceTxt = (TextView) view.findViewById(R.id.price_txt);
////                discountTxt = (TextView) view.findViewById(R.id.discount_txt);
////                extraBtn = (Button) view.findViewById(R.id.extra_btn);
////                extraPriceTxt = (TextView) view.findViewById(R.id.extra_price_txt);
////                amountTxt = (TextView) view.findViewById(R.id.amount_txt);
////                takeAwayCheck = (CheckBox) view.findViewById(R.id.take_away_check);
////                clearBtn = (ImageView) view.findViewById(R.id.clear_btn);
////            }
////        }
//    }

/*    private class CategoryItemAdapter extends ArrayAdapter<Category_Item> {
        public final Activity context;

        public CategoryItemAdapter(Activity context) {
            super(context, R.layout.category_list_item, TotalitemArraylist);
           // Log.i("categoryItemList", categoryItemList.size() + "");
            this.context = context;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            final CategoryItemViewHolder viewHolder;

            if (view == null) {
                LayoutInflater layoutInflater = context.getLayoutInflater();
                view = layoutInflater.inflate(R.layout.category_list_item, null, true);
                viewHolder = new CategoryItemViewHolder(view);
                view.setTag(viewHolder);
            } else {
                viewHolder = (CategoryItemViewHolder) view.getTag();
            }

            categoryTxt.setText("Item");
            final Category_Item categoryItem = TotalitemArraylist.get(position);
            String takeiddd = categoryItem.getTakeid();
            String statusiddd = categoryItem.getStatusid();

            *//***
     * PhoneLinAung 11.9.17 Start
     *//*
            viewHolder.itemNameTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    contimentforItemSelectList.clear();

                    String item_id = TotalitemArraylist.get(position).getId();

                    Cursor cursor = database.rawQuery("SELECT * FROM item where id='" + item_id + "'", null);

                    while (cursor.moveToNext()) {

                        int has_contiment = cursor.getInt(cursor.getColumnIndex("has_contiment"));

                        if (has_contiment == 1) {

                            group_id = cursor.getString(cursor.getColumnIndex("group_id"));

                            Cursor cursor1 = database.rawQuery("SELECT * FROM item WHERE group_id='" + group_id + "'", null);

                            while (cursor1.moveToNext()) {
                                contimet = new ContimentforItemSelect();
                                String name = cursor1.getString(cursor1.getColumnIndex("name"));
                                contiment_id = cursor1.getInt(cursor1.getColumnIndex("contiment_id"));

                                Cursor cursor2 = database.rawQuery("SELECT * FROM contiment where id=" + contiment_id, null);

                                while (cursor2.moveToNext()) {

                                    contiment_name = cursor2.getString(cursor2.getColumnIndex("name"));

                                }
                                contimet.setContiment_id(contiment_id);
                                contimet.setContiment_name(contiment_name);
                                contimentforItemSelectList.add(contimet);
                            }

                            Toast.makeText(CategoryActivity.this, categoryItem.getItemName(), Toast.LENGTH_SHORT).show();
                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                    .setPositiveButton(R.string.invitation_ok, null)
                                    .setNegativeButton(R.string.invitation_cancel, null)
                                    .create();
                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View view1 = layoutInflater.inflate(R.layout.category_contiment_dialog, null);

                            contimentnameList.clear();
                            for (ContimentforItemSelect c : contimentforItemSelectList) {

                                contimentnameList.add(c.getContiment_name());

                            }
                            final Spinner contimentSpinner = (Spinner) view1.findViewById(R.id.contimentSpinner);
                            ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(CategoryActivity.this, android.R.layout.simple_spinner_item, contimentnameList);
                            stringAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            contimentSpinner.setAdapter(stringAdapter);
                            builder.setView(view1);
                            builder.setTitle(categoryItem.getItemName());
                            builder.setMessage("Choose ContimentforItemSelect");
                            builder.show();

                            contimentSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    String selected_ContimentName = contimentnameList.get(i);
                                    Toast.makeText(CategoryActivity.this, selected_ContimentName, Toast.LENGTH_SHORT).show();

                                    // itemNameTxt.setText(selected_ContimentName + categoryItem.getItemName());

                                    for (i = 0; i < contimentnameList.size(); i++) {

                                        if (selected_ContimentName.equals(contimentforItemSelectList.get(i).getContiment_name())) {

                                            selected_Contiment_id = contimentforItemSelectList.get(i).getContiment_id();

                                            Cursor cursor = database.rawQuery("SELECT * FROM item where contiment_id='" + selected_Contiment_id + "' and group_id='" + group_id + "'", null);

                                            while (cursor.moveToNext()) {

                                                String selected_item_id = cursor.getString(cursor.getColumnIndex("id"));
                                                categoryItem.setId(selected_item_id);
                                                String selectd_itemname = cursor.getString(cursor.getColumnIndex("name"));
                                                viewHolder.itemNameTxt.setText(selected_ContimentName + " " + selectd_itemname);
                                                TotalitemArraylist.get(position).setItemName(selected_ContimentName + " " + selectd_itemname);

                                            }

                                        }

                                    }


                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });


                        } else {

                            Toast.makeText(CategoryActivity.this, "This item has no contiment", Toast.LENGTH_SHORT).show();

                        }

                    }

                }
            });

            */

    /***
     * PhoneLinAung 12.9.17 End
     *//*

            if (TAKE_AWAY == "table" || TAKE_AWAY == "room") {     // for from room and table including new invoice and exiting invoice
                if ((statusiddd == "6" || statusiddd.equals("6")) || (statusiddd == "7" || statusiddd.equals("7"))) {
                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
                    viewHolder.itemNameTxt.setPaintFlags(viewHolder.itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
                    viewHolder.quantityBtn.setPaintFlags(viewHolder.quantityBtn.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.priceTxt.setText(commaSepFormat.format(0));
                    viewHolder.priceTxt.setPaintFlags(viewHolder.priceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
                    viewHolder.discountTxt.setPaintFlags(viewHolder.discountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.extraPriceTxt.setText(commaSepFormat.format(0));
                    viewHolder.extraPriceTxt.setPaintFlags(viewHolder.extraPriceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.amountTxt.setText(commaSepFormat.format(0));
                    viewHolder.amountTxt.setPaintFlags(viewHolder.amountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.takeAwayCheck.setEnabled(false);

                } else {
                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
                    viewHolder.priceTxt.setText(commaSepFormat.format(categoryItem.getPrice()));
                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
                }
                if (ADD_INVOICE.equals("NULL") || ADD_INVOICE == null) {
                    viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                    .setPositiveButton(R.string.invitation_ok, null)
                                    .setNegativeButton(R.string.invitation_cancel, null)
                                    .create();
                            builder.setTitle(R.string.clear);
                            builder.setMessage("Do you want to clear this item?");
                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                    btnAccept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            categoryItemList.remove(position);
                                            categoryItemAdapter.notifyDataSetChanged();
                                            double totalValue = 0;
                                            double teValue = 0;
                                            double tdValue = 0;
                                            double servicecharges = 0;
                                            for (Category_Item catItem : categoryItemList) {
                                                totalValue += catItem.getTotalAmount();
                                                teValue += catItem.getTotalExtraPrice();
                                                tdValue += catItem.getTotalDiscount();
                                            }
                                            if (categoryItemList.size() > 0) {
                                                taxValue = totalValue * taxAmt / 100;
                                                serviceValue = totalValue * serviceAmt / 100;
                                                servicecharges = taxValue + serviceValue;
                                            } else {
                                                servicecharges = 0;
                                            }
                                            totalAmt = totalValue;
                                            totalDisAmt = tdValue;
                                            totalExtraAmt = teValue;
                                            totalTaxAmt = taxValue;
                                            totalServiceAmt = serviceValue;
                                            tPriceTxt.setText(commaSepFormat.format(totalValue));
                                            tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));
                                            taxAmtTxt.setText(commaSepFormat.format(taxValue));
                                            serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
                                            builder.dismiss();
                                        }
                                    });
                                    final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                                    btnDecline.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Log.d("Clear", "Item");
                                            builder.dismiss();
                                        }
                                    });
                                }
                            });
                            builder.show();
                        }

                    });


                } else if (ADD_INVOICE == "EDITING_INVOICE" || ADD_INVOICE.equals("EDITING_INVOICE") || ADD_INVOICE.equals("status1")) {
                    viewHolder.clearBtn.setEnabled(false);
                    viewHolder.clearBtn.setColorFilter(Color.argb(220, 220, 220, 220));
                    viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(CategoryActivity.this, "Can't Cancel NOW !", Toast.LENGTH_SHORT).show();
                            Log.e("Can't Cancel NOW !", "Can't Cancel NOW !Can't Cancel NOW !");
                        }
                    });
                }
                double extraVlaue = 0;
                for (AddOn addOn : categoryItem.getAddOnArrayList()) {

                    if (addOn.isSelected() == true) {
                        extraVlaue += addOn.getPrice();
                    }
                }
                viewHolder.extraPriceTxt.setText(commaSepFormat.format(extraVlaue));
                categoryItem.setExtraPrice(extraVlaue);
                categoryItemAdapter.notifyDataSetChanged();
                viewHolder.amountTxt.setText(commaSepFormat.format(categoryItem.getTotalAmount()));
                Log.e("TakeAway", TAKE_AWAY + "");
                viewHolder.takeAwayCheck.setChecked(categoryItem.getTakeAway());
                viewHolder.takeAwayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                                                        @Override
                                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                            if (categoryItem.getOrder_type_id().equals("2")) {
                                                                                Log.e("TakeAway", "true");
                                                                            }
                                                                            categoryItem.setTakeAway(isChecked);
                                                                            categoryItemAdapter.notifyDataSetChanged();
                                                                        }
                                                                    }
                );
                double totalValue = 0;
                double teValue = 0;
                double tdValue = 0;
                for (Category_Item catItem : categoryItemList) {
                    Log.i("ggggggggg>>>>>>>>", catItem.getStatusid() + "");
                    if ((catItem.getStatusid() == "6" || catItem.getStatusid().equals("6")) || (catItem.getStatusid() == "7" || catItem.getStatusid().equals("7"))) {
                        Log.i("ggwp", "ggggggggg>>>>>>>>");
                    } else if (catItem.getStatusid().equals("1") || catItem.getStatusid().equals("2") || catItem.getStatusid().equals("3") || catItem.getStatusid().equals("4") || catItem.getStatusid().equals("5")) {
                        totalValue += catItem.getTotalAmount();
                        teValue += catItem.getTotalExtraPrice();
                        tdValue += catItem.getTotalDiscount();
                    }
                }
                double taxValue = totalValue * taxAmt / 100;
                double serviceValue = totalValue * serviceAmt / 100;
                double servicecharges = taxValue + serviceValue;
                totalDisAmt = tdValue;
                totalExtraAmt = teValue;
                tPriceTxt.setText(commaSepFormat.format(totalValue));
                tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));
                serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
                taxAmtTxt.setText(commaSepFormat.format(taxValue));
                viewHolder.quantityBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                .setPositiveButton(R.string.invitation_ok, null)
                                .setNegativeButton(R.string.invitation_cancel, null)
                                .create();
                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View view = layoutInflater.inflate(R.layout.category_quantity_dialog, null);
                        final EditText qtyEdit = (EditText) view.findViewById(R.id.qty_edit);
                        builder.setView(view);
                        builder.setTitle(R.string.quantity_title);
                        builder.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                btnAccept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        if (qtyEdit.getText().toString().isEmpty()) {
                                            qtyEdit.setError("Quantity is required.");
                                            qtyEdit.requestFocus();
                                        } else {
                                            int qty = Integer.parseInt(qtyEdit.getText().toString());
                                            String idid;
                                            if (categoryItem.getId().equals(null)) {
                                                idid = categoryItem.getSetid();
                                                getPromotionDataInDBforsetmenu(idid);
                                            } else {
                                                idid = categoryItem.getId();
                                                getPromotionDataInDB(*//*categoryItem.getId()*//* idid);
                                            }

                                            if (from_date == null && to_date == null && from_time == null && to_time == null) {
                                                Log.e("PromotionItem", "This item is not promotion.");
                                            } else {
                                                try {
                                                    Date CurrentDate = date_format.parse(date_format.format(new Date()));
                                                    if (CurrentDate.equals(from_date) || CurrentDate.after(from_date) && CurrentDate.before(to_date)) {
                                                        Log.e("CurrentDate", CurrentDate + ",From" + from_date + ",To" + to_date);
                                                        Date currentTime = time_format.parse(time_format.format(new Date()));
                                                        Log.e("CurrentTime", currentTime + ",from" + from_time + ",to" + to_time);
                                                        if (currentTime.equals(from_time) || currentTime.equals(to_time) || currentTime.after(from_time) && currentTime.before(to_time)) {// && currentTime.before(to_time)
                                                            Log.e("Promotion", promotion_id);
                                                            if (qty >= sell_quantity) {
                                                                Log.e("SellQuantity", sell_quantity + "");
                                                                categoryItem.setPromotion_id(promotion_id);
                                                            }
                                                        } else {
                                                            Log.e("Promotions", promotion_id);
                                                        }
                                                    }
                                                } catch (ParseException e) {
                                                    e.printStackTrace();
                                                }
                                            }
                                            Log.e("ItemID", categoryItem.getId());
                                            if (qty == 0) {
                                                qtyEdit.setError("Quantity is required.");
                                            } else {
                                                categoryItem.setQuantity(qty);
                                            }

                                            Log.d("Quantity", "You have entered: " + qty);
                                            builder.dismiss();
                                            categoryItemAdapter.notifyDataSetChanged();
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
                });
                viewHolder.extraBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                .setPositiveButton(R.string.invitation_ok, null)
                                .setNegativeButton(R.string.invitation_cancel, null)
                                .create();
                        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        final View view = layoutInflater.inflate(R.layout.category_extra_dialog, null);
                        ListView addListView = (ListView) view.findViewById(R.id.list_view);
                        final EditText remarkEdit = (EditText) view.findViewById(R.id.remark_edit);
                        addOnAdapter = new AddOnAdapter(CategoryActivity.this, categoryItem.getAddOnArrayList());
                        addListView.setAdapter(addOnAdapter);
                        addOnAdapter.notifyDataSetChanged();
                        remarkEdit.setText(categoryItem.getUserRemark());
                        builder.setView(view);
                        builder.setTitle(R.string.extra_title);
                        builder.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                btnAccept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        categoryItem.setUserRemark(remarkEdit.getText().toString());
                                        Log.d("Quantity", "You have entered: " + remarkEdit.getText().toString());
                                        builder.dismiss();
                                        categoryItemAdapter.notifyDataSetChanged();
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
                });
                //ADD_INVOICE = null;
            } else {
                Log.i("takeaway!!!!!", TAKE_AWAY + "");      // for from  take away including new invoice and exiting invoice
                if ((statusiddd == "6" || statusiddd.equals("6")) || (statusiddd == "7" || statusiddd.equals("7"))) {
                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
                    viewHolder.itemNameTxt.setPaintFlags(viewHolder.itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
                    viewHolder.quantityBtn.setPaintFlags(viewHolder.quantityBtn.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.priceTxt.setText(commaSepFormat.format(0));
                    viewHolder.priceTxt.setPaintFlags(viewHolder.priceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
                    viewHolder.discountTxt.setPaintFlags(viewHolder.discountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.extraPriceTxt.setText(commaSepFormat.format(0));
                    viewHolder.extraPriceTxt.setPaintFlags(viewHolder.extraPriceTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.amountTxt.setText(commaSepFormat.format(0));
                    viewHolder.amountTxt.setPaintFlags(viewHolder.amountTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                    viewHolder.clearBtn.setEnabled(false);
                    viewHolder.clearBtn.setColorFilter(Color.argb(220, 220, 220, 220));
                    viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(CategoryActivity.this, "Can't Cancel NOW !", Toast.LENGTH_SHORT).show();
                            Log.e("Can't Cancel NOW !", "Can't Cancel NOW !Can't Cancel NOW !");
                        }
                    });
                    viewHolder.takeAwayCheck.setEnabled(false);
                } else {
                    viewHolder.itemNameTxt.setText(categoryItem.getItemName());
                    viewHolder.quantityBtn.setText(categoryItem.getQuantity() + "");
                    viewHolder.priceTxt.setText(commaSepFormat.format(categoryItem.getPrice()));
                    viewHolder.discountTxt.setText(commaSepFormat.format(categoryItem.getDiscount()));
                    if (ADD_INVOICE == "EDITING_INVOICE") {
                        viewHolder.clearBtn.setEnabled(false);
                        viewHolder.clearBtn.setColorFilter(Color.argb(220, 220, 220, 220));
                        viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(CategoryActivity.this, "Can't Cancel NOW !", Toast.LENGTH_SHORT).show();
                                Log.e("Can't Cancel NOW !", "Can't Cancel NOW !Can't Cancel NOW !");
                            }
                        });
                    } else if (ADD_INVOICE == "NULL" || ADD_INVOICE == null) {
                        viewHolder.clearBtn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                        .setPositiveButton(R.string.invitation_ok, null)
                                        .setNegativeButton(R.string.invitation_cancel, null)
                                        .create();
                                builder.setTitle(R.string.clear);
                                builder.setMessage("Do you want to clear this item?");
                                builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                    @Override
                                    public void onShow(DialogInterface dialog) {
                                        final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                        btnAccept.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                categoryItemList.remove(position);
                                                categoryItemAdapter.notifyDataSetChanged();
                                                double totalValue = 0;
                                                double teValue = 0;
                                                double tdValue = 0;
                                                double servicecharges = 0;
                                                for (Category_Item catItem : categoryItemList) {
                                                    totalValue += catItem.getTotalAmount();
                                                    teValue += catItem.getTotalExtraPrice();
                                                    tdValue += catItem.getTotalDiscount();
                                                }
                                                if (categoryItemList.size() > 0) {
                                                    taxValue = totalValue * taxAmt / 100;
                                                    serviceValue = totalValue * serviceAmt / 100;
                                                    servicecharges = taxValue + serviceValue;
                                                } else {
                                                    servicecharges = 0;
                                                }
                                                totalAmt = totalValue;
                                                totalDisAmt = tdValue;
                                                totalExtraAmt = teValue;
                                                totalTaxAmt = taxValue;
                                                totalServiceAmt = serviceValue;
                                                tPriceTxt.setText(commaSepFormat.format(totalValue));
                                                tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));
                                                taxAmtTxt.setText(commaSepFormat.format(taxValue));
                                                serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
                                                builder.dismiss();
                                            }
                                        });
                                        final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                                        btnDecline.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                Log.d("Clear", "Item");
                                                builder.dismiss();
                                            }
                                        });
                                    }
                                });
                                builder.show();
                            }
                        });
                    }
                    double extraVlaue = 0;
                    for (AddOn addOn : categoryItem.getAddOnArrayList()) {
                        if (addOn.isSelected() == true) {
                            extraVlaue += addOn.getPrice();
                        }
                    }
                    viewHolder.extraPriceTxt.setText(commaSepFormat.format(extraVlaue));
                    categoryItem.setExtraPrice(extraVlaue);
                    categoryItemAdapter.notifyDataSetChanged();
                    viewHolder.amountTxt.setText(commaSepFormat.format(categoryItem.getTotalAmount()));
                    Log.e("TakeAway", TAKE_AWAY + "");
                    viewHolder.takeAwayCheck.setChecked(categoryItem.getTakeAway());
                    if (takeiddd.equals("1")) {
                        viewHolder.takeAwayCheck.setEnabled(false);
                    }
                    viewHolder.takeAwayCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                                            @Override
                                                                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                                                if (categoryItem.getOrder_type_id().equals("2")) {
                                                                                    Log.e("TakeAway", "true");
                                                                                }
                                                                                categoryItem.setTakeAway(isChecked);
                                                                                categoryItemAdapter.notifyDataSetChanged();
                                                                            }
                                                                        }
                    );
                    double totalValue = 0;
                    double teValue = 0;
                    double tdValue = 0;
                    for (Category_Item catItem : categoryItemList) {
                        Log.i("ggggggggg>>>>>>>>", catItem.getStatusid() + "");
                        if ((catItem.getStatusid() == "6" || catItem.getStatusid().equals("6")) || (catItem.getStatusid() == "7" || catItem.getStatusid().equals("7"))) {
                            Log.i("ggwp", "ggggggggg>>>>>>>>");
                        } else if (catItem.getStatusid().equals("1") || catItem.getStatusid().equals("2") || catItem.getStatusid().equals("3") || catItem.getStatusid().equals("4") || catItem.getStatusid().equals("5")) {
                            totalValue += catItem.getTotalAmount();
                            teValue += catItem.getTotalExtraPrice();
                            tdValue += catItem.getTotalDiscount();
                        }
                    }
                    double taxValue = totalValue * taxAmt / 100;
                    double serviceValue = totalValue * serviceAmt / 100;
                    double servicecharges = taxValue + serviceValue;
                    totalDisAmt = tdValue;
                    totalExtraAmt = teValue;
                    tPriceTxt.setText(commaSepFormat.format(totalValue));
                    tnetPriceTxt.setText(commaSepFormat.format(totalValue + servicecharges));
                    serviceAmtTxt.setText(commaSepFormat.format(serviceValue));
                    taxAmtTxt.setText(commaSepFormat.format(taxValue));
                    viewHolder.quantityBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                    .setPositiveButton(R.string.invitation_ok, null)
                                    .setNegativeButton(R.string.invitation_cancel, null)
                                    .create();
                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View view = layoutInflater.inflate(R.layout.category_quantity_dialog, null);
                            final EditText qtyEdit = (EditText) view.findViewById(R.id.qty_edit);
                            builder.setView(view);
                            builder.setTitle(R.string.quantity_title);
                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                    btnAccept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            if (qtyEdit.getText().toString().isEmpty()) {
                                                qtyEdit.setError("Quantity is required.");
                                                qtyEdit.requestFocus();
                                            } else {
                                                int qty = Integer.valueOf(qtyEdit.getText().toString());
                                                getPromotionDataInDB(categoryItem.getId());
                                                if (from_date == null && to_date == null && from_time == null && to_time == null) {
                                                    Log.e("PromotionItem", "This item is not promotion.");
                                                } else {
                                                    try {
                                                        Date CurrentDate = date_format.parse(date_format.format(new Date()));
                                                        if (CurrentDate.equals(from_date) || CurrentDate.after(from_date) && CurrentDate.before(to_date)) {
                                                            Log.e("CurrentDate", CurrentDate + ",From" + from_date + ",To" + to_date);
                                                            Date currentTime = time_format.parse(time_format.format(new Date()));
                                                            Log.e("CurrentTime", currentTime + ",from" + from_time + ",to" + to_time);
                                                            if (currentTime.equals(from_time) || currentTime.equals(to_time) || currentTime.after(from_time) && currentTime.before(to_time)) {// && currentTime.before(to_time)
                                                                Log.e("Promotion", promotion_id);
                                                                if (qty >= sell_quantity) {
                                                                    Log.e("SellQuantity", sell_quantity + "");
                                                                    categoryItem.setPromotion_id(promotion_id);
                                                                }
                                                            } else {
                                                                Log.e("Promotions", promotion_id);
                                                            }
                                                        }
                                                    } catch (ParseException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                //Log.e("ItemID", categoryItem.getId());
                                                if (qty == 0) {
                                                    qtyEdit.setError("Quantity is required.");
                                                } else {
                                                    categoryItem.setQuantity(qty);
                                                }
                                                Log.d("Quantity", "You have entered: " + qty);
                                                builder.dismiss();
                                                categoryItemAdapter.notifyDataSetChanged();
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
                    });
                    viewHolder.extraBtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(final View v) {
                            final AlertDialog builder = new AlertDialog.Builder(CategoryActivity.this, R.style.InvitationDialog)
                                    .setPositiveButton(R.string.invitation_ok, null)
                                    .setNegativeButton(R.string.invitation_cancel, null)
                                    .create();
                            LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                            final View view = layoutInflater.inflate(R.layout.category_extra_dialog, null);
                            ListView addListView = (ListView) view.findViewById(R.id.list_view);
                            final EditText remarkEdit = (EditText) view.findViewById(R.id.remark_edit);
                            addOnAdapter = new AddOnAdapter(CategoryActivity.this, categoryItem.getAddOnArrayList());
                            addListView.setAdapter(addOnAdapter);
                            addOnAdapter.notifyDataSetChanged();
                            remarkEdit.setText(categoryItem.getUserRemark());
                            builder.setView(view);
                            builder.setTitle(R.string.extra_title);
                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                                    btnAccept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            categoryItem.setUserRemark(remarkEdit.getText().toString());
                                            Log.d("Quantity", "You have entered: " + remarkEdit.getText().toString());
                                            builder.dismiss();
                                            categoryItemAdapter.notifyDataSetChanged();
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
                    });
                }
            }
            return view;
        }

        public class CategoryItemViewHolder extends RecyclerView.ViewHolder {

            public TextView itemNameTxt;
            Button quantityBtn;
            TextView priceTxt;
            TextView discountTxt;
            Button extraBtn;
            TextView extraPriceTxt;
            TextView amountTxt;
            CheckBox takeAwayCheck;
            ImageView clearBtn;

            public CategoryItemViewHolder(View view) {
                super(view);
                itemNameTxt = (TextView) view.findViewById(R.id.item_name_txt);
                quantityBtn = (Button) view.findViewById(R.id.quantity_btn);
                priceTxt = (TextView) view.findViewById(R.id.price_txt);
                discountTxt = (TextView) view.findViewById(R.id.discount_txt);
                extraBtn = (Button) view.findViewById(R.id.extra_btn);
                extraPriceTxt = (TextView) view.findViewById(R.id.extra_price_txt);
                amountTxt = (TextView) view.findViewById(R.id.amount_txt);
                takeAwayCheck = (CheckBox) view.findViewById(R.id.take_away_check);
                clearBtn = (ImageView) view.findViewById(R.id.clear_btn);
            }
        }
    }*/

    private class AddOnAdapter extends ArrayAdapter<AddOn> {
        public final Activity context;
        ArrayList<AddOn> addonarraylist;

        public AddOnAdapter(Activity context, ArrayList<AddOn> addonarraylist) {
            super(context, R.layout.list_view_checkbox, addonarraylist);
            this.context = context;
            this.addonarraylist = addonarraylist;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.list_view_checkbox, null, true);
            final AddOn addOn = addonarraylist.get(position);
            CheckBox add_on_check = (CheckBox) view.findViewById(R.id.check_box);
            TextView name_txt = (TextView) view.findViewById(R.id.name_txt);
            add_on_check.setChecked(addOn.isSelected());
            if (addOn.isSelected()) {
                addOn.setStatus("1");
            } else {
                addOn.setStatus("0");
            }
            name_txt.setText(addOn.getFood_name());
            add_on_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                        @Override
                                                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                            addOn.setSelected(isChecked);
                                                            if (!isChecked) {
                                                                addOn.setStatus("0");
                                                            } else {
                                                                addOn.setStatus("1");
                                                            }
                                                            //addOnAdapter.notifyDataSetChanged();
                                                        }
                                                    }
            );
            return view;
        }
    }

    public class CategoryArrayAdapter extends RecyclerView.Adapter<CategoryArrayAdapter.MyViewHolder> {
        private Context mContext;
        private List<Category> albumList;

        public CategoryArrayAdapter(Context mContext, List<Category> albumList) {
            this.mContext = mContext;
            this.albumList = albumList;
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title;
            public ImageView thumbnail;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                thumbnail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        Toast.makeText(mContext, "Here1", Toast.LENGTH_SHORT).show();

                        ID = albumList.get(getPosition()).getId();
                        parent_ID = albumList.get(getPosition()).getParent_id();
                        next_ID = ID;
                        previous_ID = parent_ID;
                        if (categoryDataFromDB(ID).size() > 0) {
                            CategoryArrayAdapter adapter = new CategoryArrayAdapter(CategoryActivity.this, categoryDataFromDB(ID));
                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        } else {
                            recyclerView.setVisibility(View.GONE);
                            recycleritemView.setVisibility(View.VISIBLE);
                            recyclerState = true;
                            parent_ID = albumList.get(getPosition()).getParent_id();
                            count2 = 0;
                            Log.e("Category_id", ID);
                            if (ID.equals("set_menu")) {
                                ItemArrayAdapter itemAdapter = new ItemArrayAdapter(CategoryActivity.this, setMenuDataFromDB());
                                recycleritemView.setAdapter(itemAdapter);
                                itemAdapter.notifyDataSetChanged();
                            } else {
                                ItemArrayAdapter itemAdapter = new ItemArrayAdapter(CategoryActivity.this, itemDataFromDB(ID));
                                recycleritemView.setAdapter(itemAdapter);
                                itemAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                });
            }
        }


        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {

            categoryTxt.setText("Category");

            Category album = albumList.get(position);
            //byte[] ImageShow = Base64.decode(album.getImage(), Base64.DEFAULT);
            //Bitmap mBitmap = BitmapFactory.decodeByteArray(ImageShow, 0, ImageShow.length);


            Glide.with(mContext)
                    .load(MainActivity.IMG_URL_PREFIX + album.getImage()).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    return false;
                }
            })
                    .error(R.drawable.default_pic)
                    .placeholder(R.drawable.default_pic)
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.thumbnail);


            //holder.thumbnail.setImageBitmap(mBitmap);

            holder.title.setTypeface(font);

            holder.title.setText(album.getName());
        }

        @Override
        public int getItemCount() {
            return albumList.size();
        }
    }

    public class ItemArrayAdapter extends RecyclerView.Adapter<ItemArrayAdapter.MyViewHolder> {
        private Context mContext;
        private List<Category> itemList;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView title, count;
            public ImageView thumbnail, overflow;

            public MyViewHolder(View view) {
                super(view);
                title = (TextView) view.findViewById(R.id.title);
                thumbnail = (ImageView) view.findViewById(R.id.thumbnail);
                thumbnail.setOnClickListener(new View.OnClickListener() {
                    @SuppressLint("LongLogTag")
                    @Override
                    public void onClick(View v) {


//                        Toast.makeText(mContext, "Here2", Toast.LENGTH_SHORT).show();

                        categoryTxt.setText("Item");
                        Category_Item category_item = new Category_Item();
                        category_item.setId(itemList.get(getPosition()).getId());
                        Log.i("category_item.category_item.setId", category_item.getId() + "");
                        category_item.setItemName(itemList.get(getPosition()).getName());
                        Log.i("category_item.setItemName", category_item.getItemName() + "");
                        category_item.setQuantity(itemList.get(getPosition()).getQuantity());
                        category_item.setPrice(itemList.get(getPosition()).getPrice());
                        String discountType = itemList.get(getPosition()).getDiscount_type();
                        category_item.setDiscount(itemList.get(getPosition()).getDiscount());
                        Log.d("DiscountType", discountType + "v");
                        if (discountType == null) {
                            category_item.setDiscount(0);
                        } else if (discountType.equals("%")) {
                            double price = itemList.get(getPosition()).getPrice();
                            double qty = itemList.get(getPosition()).getQuantity();
                            double discount = itemList.get(getPosition()).getDiscount();
                            double totalAmt = price * qty;
                            double totalDiscount = totalAmt * discount / 100;
                            category_item.setDiscount(totalDiscount);
                        }
                        category_item.setDiscount_type(itemList.get(getPosition()).getDiscount_type());
                        category_item.setDiscount_id(itemList.get(getPosition()).getDiscount_id());
                        category_item.setExtraPrice(itemList.get(getPosition()).getExtraPrice());
                        category_item.setAmount(itemList.get(getPosition()).getAmount());
                        if (TAKE_AWAY.equals("take")) {
                            category_item.setOrder_type_id("2");
                            category_item.setTakeAway(true);
                            category_item.setTakeid("1");
                        } else {
                            category_item.setOrder_type_id("1");
                            category_item.setTakeAway(false);
                            category_item.setTakeid("0");
                        }
                        category_item.setCategoryId(itemList.get(getPosition()).getCategory_id());
                        if (category_item.getCategoryId().equals("set_menu")) {
                            category_item.setSetid(itemList.get(getPosition()).getId());
                        } else {
                            category_item.setSetid("null");
                        }
                        category_item.setStatusid("1");
                        category_item.setState("new");
                        String value = getAddOnID(itemList.get(getPosition()).getCategory_id());
                        category_item.setAddOnArrayList(getAddonData(value));


                        if (VOUNCHER_ID != null) {

                            //tempcategoryItemList1.clear();
                            // Toast.makeText(mContext, "add to tempArray", Toast.LENGTH_SHORT).show();
                            // category_item.setState("new");
                            tempcategoryItemList1.add(category_item);
                            //categoryItemList.addAll(tempcategoryItemList1);


                            Log.i("CategoryItemListSize", ":" + categoryItemList.size());
                            AddnewItemCompareList(categoryItemList, TotalitemArraylist);


                        } else {

                            categoryItemList.add(category_item);
                        }


                        TotalitemArraylist.clear();
                        TotalitemArraylist.addAll(categoryItemList);
                        TotalitemArraylist.addAll(tempcategoryItemList1);
                        categoryItemAdapter.notifyDataSetChanged();
                        // Log.i("category_itemcategory_itemfromadapter", category_item + "");
                    }
                });
            }
        }

        public ItemArrayAdapter(Context mContext, List<Category> itemList) {
            this.mContext = mContext;
            this.itemList = itemList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.category_card, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            categoryTxt.setText("Item");
            String category_id = itemList.get(0).getCategory_id();
            Log.e("CategoryID", category_id);
            for (Category cate : cateDataFromDB(category_id)) {
                parent_ID = cate.getParent_id();
                count2 = 0;
            }
            Log.e("ParentID", parent_ID);
            Category item = itemList.get(position);


            holder.title.setTypeface(font);

            holder.title.setText(item.getName());
            //byte[] ImageShow = Base64.decode(item.getImage(), Base64.DEFAULT);
            //Bitmap mBitmap = BitmapFactory.decodeByteArray(ImageShow, 0, ImageShow.length);
            Glide.with(mContext)
                    .load(MainActivity.IMG_URL_PREFIX + item.getImage())
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .into(holder.thumbnail);
            //holder.thumbnail.setImageBitmap(mBitmap);
        }

        @Override
        public int getItemCount() {
            return itemList.size();
        }
    }

    private void uploadOrderData() {  // uploading new order data !!
        Calendar todayCal = Calendar.getInstance();
        String todayDate = orderTime.format(todayCal.getTime());
        SharedPreferences prefs = getSharedPreferences(MainActivity.LOGIN_PREFERENCES, MODE_PRIVATE);
        WAITER_ID = prefs.getString(MainActivity.WAITER_ID, "No name defined");
        double tvalue = Double.parseDouble(tPriceTxt.getText().toString().trim().replaceAll(",", ""));
        double tax_value = tvalue * taxAmt / 100;
        double service_value = tvalue * serviceAmt / 100;
        final String order_id = makeOrderID();
        Log.e("InvoiceOrderID", order_id + "");
        JSONObject orderjsonObject = new JSONObject();
        JSONArray orderDetailJsonArray = new JSONArray();
        JSONArray tableArray = new JSONArray();
        JSONArray roomArray = new JSONArray();

        int invoiceDetailID = 1;
        for (Category_Item category_item : TotalitemArraylist) {

            int quantity = category_item.getQuantity();

            Log.e("ItemQuantity", quantity + "");

            //for (int i = 0; i < quantity; i++) {
            JSONObject detail_object = new JSONObject();
            String orderType = null;

            if (category_item.isTakeAway() == true) {
                orderType = "2";
            } else {
                orderType = "1";
            }

            try {
                if (category_item.getCategoryId().equals("set_menu")) {
                    detail_object.put("set_id", category_item.getId());
                    detail_object.put("item_id", "null");
                    JSONArray setItemJsonArray = new JSONArray();
                    ArrayList<SetItem> setItemArrayList = new ArrayList<>();

                    setItemArrayList = getseitemdata(category_item.getId());
                    Log.i("testttttttttt>>>>>", setItemArrayList.size() + "");
                    for (SetItem setItem : setItemArrayList) {
                        if (setItemArrayList != null) {

                            JSONObject setitemJsonObject = new JSONObject();
                            try {
                                setitemJsonObject.put("id", setItem.getId());
                                setitemJsonObject.put("item_id", setItem.getItem_id());
//                                setitemJsonObject.put("item_id", setItem.getItem_id());
                                setitemJsonObject.put("set_menu_id", setItem.getSet_menu_id());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            setItemJsonArray.put(setitemJsonObject);
                        }
                    }
                    detail_object.put("set_item", setItemJsonArray);
                    Log.i("setItemJS", setItemJsonArray + "");

                    Log.e("SetID", category_item.getId() + "");
                } else {
                    detail_object.put("item_id", category_item.getId());
                    detail_object.put("set_id", "null");
                    JSONArray setItemArray = new JSONArray();
                    detail_object.put("set_item", setItemArray);
                    Log.e("ITEM_ID >>>>", category_item.getId() + "");
                }
                detail_object.put("state", "new");
                detail_object.put("order_detail_id", order_id + String.valueOf(invoiceDetailID));
                double discount = category_item.getDiscount();
                double price = category_item.getPrice();
                double extraPrice = category_item.getExtraPrice();
                double quantityy = category_item.getQuantity();
                double totalAmt = ((price + extraPrice) - discount) * quantityy;

                detail_object.put("take_item", gettakeitemID(category_item.getTakeAway()));
                detail_object.put("discount_amount", discount + "");
                detail_object.put("promotion_id", category_item.getPromotion_id() + "");
                detail_object.put("price", price);

                detail_object.put("quantity", category_item.getQuantity());
                detail_object.put("amount", totalAmt);
                detail_object.put("order_type_id", orderType);
                detail_object.put("status", "1");
                detail_object.put("exception", category_item.getUserRemark() + "");
                Log.e("AddonSize", category_item.getAddOnArrayList().size() + "");
                JSONArray orderExtraJsonArray = new JSONArray();
                for (AddOn addOn : category_item.getAddOnArrayList()) {
                    if (addOn.isSelected() == true) {

                        JSONObject extra_object = new JSONObject();
                        try {
                            extra_object.put("extra_id", addOn.getId());
                            extra_object.put("category_id", addOn.getCategory_id());
                            extra_object.put("quantity", category_item.getQuantity()); //set the same Quantity of OrderItem
                            extra_object.put("amount", addOn.getPrice());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        orderExtraJsonArray.put(extra_object);
                    }
                }
                detail_object.put("extra", orderExtraJsonArray);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            orderDetailJsonArray.put(detail_object);
            invoiceDetailID++;
            //}
        }

        if (groupTableArrayList == null) {
            Log.e("GroupTableArray", "null");
        } else if (groupTableArrayList.size() > 0) {
            for (String tableName : groupTableArrayList) {
                JSONObject table = new JSONObject();
                try {
                    table.put("table_id", tableName);
                    table.put("table_status", "1");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                tableArray.put(table);
            }
        }

        if (ROOM_ID == null) {
            Log.e("Room_id", "null");
        } else {
            JSONObject room = new JSONObject();
            try {
                room.put("room_id", ROOM_ID);
                room.put("room_status", "1");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            roomArray.put(room);
        }


        JSONArray jsonArray = new JSONArray();
        try {
            double totalcharge = 0;
            double netcharge;
            orderjsonObject.put("order_id", order_id);
            orderjsonObject.put("order_status", 1);
            orderjsonObject.put("user_id", WAITER_ID);
            orderjsonObject.put("order_table", tableArray);
            orderjsonObject.put("order_room", roomArray);

            if (TAKE_AWAY.equals("take")) {
                orderjsonObject.put("take_id", "1");
            } else {
                orderjsonObject.put("take_id", "null");
            }
            orderjsonObject.put("total_price", tvalue);
            orderjsonObject.put("extra_price", totalExtraAmt);
            orderjsonObject.put("discount_amount", totalDisAmt);
            if (ROOM_ID != null /*|| !ROOM_ID.equals("")*/) {

                totalcharge = (service_value + roomchargeAmt);
                //totalcharge = service_value;
                orderjsonObject.put("service_amount", service_value);
                Log.i("totalcharge111", totalcharge + "");
            } else {
                totalcharge = service_value;
                orderjsonObject.put("service_amount", totalcharge);
            }
            orderjsonObject.put("tax_amount", tax_value);
            orderjsonObject.put("order_detail", orderDetailJsonArray);
            netcharge = (tvalue + totalcharge + tax_value);
            orderjsonObject.put("net_price", netcharge);

            if (check_check.equals("room")) {

                orderjsonObject.put("room_charge", roomchargeAmt);

            } else {

                orderjsonObject.put("room_charge", 0);

            }
            orderjsonObject.put("tablet_id", MainActivity.tablet_id);


        } catch (JSONException e) {
            e.printStackTrace();
        }
        jsonArray.put(orderjsonObject);
        Log.i("orderjsonObject", orderjsonObject + "");
        Log.e("OrderJson", jsonArray + "");


        callDialog("Uploading order data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<Success> call = request.postOrderInvoice(jsonArray.toString());
        call.enqueue(new Callback<Success>() {
            @Override
            public void onResponse(Call<Success> call, Response<Success> response) {
                try {
                    //Success jsonResponse = response.body();
                    //String message = jsonResponse.getMessage();
                    String message = response.body().getMessage();
                    if (message.equals("Success")) {
                        Log.d("Order", message);

                        Handler handler = new Handler();

                        handler.post(new Runnable() {
                            @Override
                            public void run() {

                                socket.emit("order", "blah blah");

                            }
                        });

                        mProgressDialog.dismiss();
                        saveOrderData(order_id);
                        startActivity(new Intent(CategoryActivity.this, HomePageActivity.class));
                        finish();


                    } else {
                        Log.e("Message", message + "");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Your vouncher is duplicate.Please check it!");
                }
            }

            @Override
            public void onFailure(Call<Success> call, Throwable t) {
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
            }
        });
    }

    private void saveOrderData(String order_id) {
        Log.e("Save", "yes");
        database.beginTransaction();
        Calendar todayCal = Calendar.getInstance();
        String todayDate = orderTime.format(todayCal.getTime());
        SharedPreferences prefs = getSharedPreferences(MainActivity.LOGIN_PREFERENCES, MODE_PRIVATE);
        WAITER_ID = prefs.getString(MainActivity.WAITER_ID, "No name defined");
        //gjhkljh
//        String order_id = makeOrderID();
        ContentValues cv = new ContentValues();
        cv.put("id", order_id);
        cv.put("user_id", WAITER_ID);
        cv.put("table_id", TABLE_ID);
        cv.put("room_id", ROOM_ID);
        cv.put("total_amount", Double.parseDouble(tPriceTxt.getText().toString().trim().replaceAll(",", "")));
        cv.put("discount_amount", totalDisAmt);
        cv.put("net_amount", Double.parseDouble(tnetPriceTxt.getText().toString().trim().replaceAll(",", "")));
        cv.put("total_extra", totalExtraAmt);
        cv.put("order_time", todayDate);
        //database.insert("orderList", null, cv);

        if (groupTableArrayList == null) {
            Log.e("GroupTableList", "null");
        } else {
            if (groupTableArrayList.size() > 0) {
                for (String table_id_str : groupTableArrayList) {
                    ContentValues cvTable = new ContentValues();
                    cvTable.put("order_id", order_id);
                    cvTable.put("table_id", table_id_str);
                    //database.insert("order_table", null, cvTable);
                    Log.e("GroupTable", table_id_str + "");
                }
            } else {
                Log.e("GroupTableArrayList", groupTableArrayList.size() + "");
            }
        }

        int orderDetailID = 1;

        for (Category_Item category_item : categoryItemList) {
            int quantity = category_item.getQuantity();

            Log.e("ItemQuantity", quantity + "");

            for (int i = 0; i < quantity; i++) {
                String order_detail_id = String.valueOf(orderDetailID);
                String orderType = null;
                ContentValues content = new ContentValues();
                content.put("id", order_id + order_detail_id);
                Log.e("DetailID", order_id + order_detail_id + "");
                content.put("order_id", order_id);
                content.put("item_id", category_item.getId());
                content.put("item_name", category_item.getItemName());
                content.put("quantity", "1");
                content.put("item_price", category_item.getPrice());
                content.put("discount", category_item.getDiscount());
                content.put("item_extra", category_item.getExtraPrice());
                content.put("category_id", category_item.getCategoryId());
                if (category_item.isTakeAway() == true) {
                    orderType = "2";
                } else {
                    orderType = "1";
                }
                content.put("order_type_id", orderType);
                for (AddOn addOn : category_item.getAddOnArrayList()) {
                    String order_extra_id = order_id + order_detail_id;
                    if (addOn.isSelected() == true) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put("order_detail_id", order_extra_id);
                        contentValues.put("extra_id", addOn.getId());
                        contentValues.put("quantity", "1");
                        contentValues.put("amount", addOn.getPrice());
                        database.insert("order_extra", null, contentValues);

                    }
                }
                content.put("exception", category_item.getUserRemark());
                content.put("discount_id", category_item.getDiscount_id());
                double totalAmount = category_item.getTotalAmount() / quantity;
                content.put("amount", totalAmount + "");
                content.put("status_id", "1");
                content.put("remark", category_item.getUserRemark());


                orderDetailID++;
            }
        }
        ContentValues invCountCV = new ContentValues();
        if (invoicecount == 10000000) {
            invoicecount = 1;
        }
        String arg[] = {"1"};
        invCountCV.put("voucher_count", invoicecount + 1);
        database.update("voucher", invCountCV, "id LIKE ?", arg);
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private Integer gettakeitemID(Boolean takeID) {
        Integer TakeItemID;
        if (takeID == true) {
            TakeItemID = 1;
        } else {
            TakeItemID = 0;
        }
        return TakeItemID;
    }

    private Boolean settakeitemID(Integer takeID) {
        Boolean TakeItemID;
        if (takeID == 1) {
            TakeItemID = true;
        } else {
            TakeItemID = false;
        }
        return TakeItemID;
    }

    @Override
    public void onBackPressed() {
        //  if (check_check.equals("table") ) {

//            Toast.makeText(activity, "Here", Toast.LENGTH_SHORT).show();
//            final JSONArray tableListJsonArray = new JSONArray();
//            JSONObject product = new JSONObject();
//            try {
//                product.put("booking_id", "");
//                product.put("status", "0");
//                product.put("table_id", TABLE_ID + "");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            tableListJsonArray.put(product);
//            Log.e("TableList", tableListJsonArray.toString());
//            RequestInterface request = retrofit.create(RequestInterface.class);
//            Call<Success> call = request.postTableStatus(tableListJsonArray.toString());
//            call.enqueue(new Callback<Success>() {
//                @Override
//                public void onResponse(Call<Success> call, Response<Success> response) {
//                    try {
//                        Success jsonResponse = response.body();
//                        String message = jsonResponse.getMessage();
//                        if (message.equals("Success")) {
//                            Log.d("TableStatus", message);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.d("fail!!", "");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Success> call, Throwable t) {
//                    Log.d("TableStatuscatt", t.getMessage());
//                }
//            });
        //    startActivity(new Intent(CategoryActivity.this, HomePageActivity.class));
        //    finish();
        // } else if (check_check.equals("room")) {
//            JSONObject jsonObject = new JSONObject();
//            try {
//                jsonObject.put("room_id", ROOM_ID + "");
//                jsonObject.put("status", "0");
//                jsonObject.put("booking_id", "null");
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            Log.e("RoomStatusJson", jsonObject + "");
//            RequestInterface request = retrofit.create(RequestInterface.class);
//            Call<Success> call = request.postRoomStatus(jsonObject + "");
//            call.enqueue(new Callback<Success>() {
//                @Override
//                public void onResponse(Call<Success> call, Response<Success> response) {
//                    try {
//                        Success jsonResponse = response.body();
//                        String message = jsonResponse.getMessage();
//                        if (message.equals("Success")) {
//                            Log.d("TableStatus", message);
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                        Log.d("fail!!", "");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<Success> call, Throwable t) {
//                    Log.d("TableStatuscatt", t.getMessage());
//                }
//            });
        //    startActivity(new Intent(CategoryActivity.this, HomePageActivity.class));
        //     finish();
        //  } else {
        //     check_check = "null";

        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) {
                    return;
                } else {

                    if (TotalitemArraylist.size() > 0) {

                        final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(
                                CategoryActivity.this, R.style.InvitationDialog)
                                .setPositiveButton(R.string.invitation_ok, null)
                                .setNegativeButton(R.string.invitation_cancel, null)
                                .create();
                        builder.setTitle(R.string.alert);
                        builder.setMessage("Do You Want to create this Order?");
                        builder.setOnShowListener(new DialogInterface.OnShowListener() {
                            @Override
                            public void onShow(DialogInterface dialog) {
                                final Button btnAccept = builder.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                                btnAccept.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        builder.dismiss();
                                        Log.e("VoucherID", VOUNCHER_ID + "");
                                        if (VOUNCHER_ID == null || VOUNCHER_ID.equals("NULL")) {
                                            uploadOrderData();
                                        } else {
                                            CompareItemListsUploadUpdate(categoryItemList, TotalitemArraylist);
                                            uploadUpdateOrderData();

                                        }

                                    }
                                });

                                final Button btnDecline = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                                btnDecline.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(CategoryActivity.this, HomePageActivity.class));
                                        finish();
                                    }
                                });
                            }
                        });
                        builder.show();
                    } else {

                        startActivity(new Intent(CategoryActivity.this, HomePageActivity.class));
                        finish();

                    }
                }
            }
        });


//            startActivity(new Intent(CategoryActivity.this, HomePageActivity.class));
//            finish();
        // }


    }


    public void CompareItemListsUploadUpdate(ArrayList<Category_Item> oldList1, ArrayList<Category_Item> oldList2) {

        for (int i = 0; i < oldList1.size(); i++) {

            if (oldList1.get(i).getId() != oldList2.get(i).getId() || oldList1.get(i).getSetid() != oldList2.get(i).getSetid() || oldList1.get(i).getQuantity() != oldList2.get(i).getQuantity()) {


                oldList2.get(i).setState("edit");

            }

//            if (oldList1.get(i).getAddOnArrayList().size() > oldList2.get(i).getAddOnArrayList().size()) {
//
//                for (int j = 0; j < oldList2.get(i).getAddOnArrayList().size(); j++) {
//
//
//                    if (oldList2.get(j).getAddOnArrayList().get(j).getFood_name() != oldList1.get(j).getAddOnArrayList().get(j).getFood_name()) {
//
//                        oldList2.get(i).setState("edit");
//
//                    }
//
//                }
//
//            }
//            else {

            if (oldList1.get(i).getAddOnArrayList() != null && oldList1.get(i).getAddOnArrayList().size() != 0 && oldList2.get(i).getAddOnArrayList() != null && oldList2.get(i).getAddOnArrayList().size() != 0) {

                for (int j = 0; j < oldList1.get(i).getAddOnArrayList().size(); j++) {

                    if (oldList1.get(i).getAddOnArrayList() != null && oldList1.get(i).getAddOnArrayList().size() != 0 && oldList2.get(i).getAddOnArrayList() != null && oldList2.get(i).getAddOnArrayList().size() != 0) {

                        boolean a = oldList1.get(i).getAddOnArrayList().get(j).isSelected();

                        boolean b = oldList2.get(i).getAddOnArrayList().get(j).isSelected();

                        if (oldList2.get(i).getAddOnArrayList().get(j).getFood_name() != oldList1.get(i).getAddOnArrayList().get(j).getFood_name() || a != b) {

                            oldList2.get(i).setState("edit");

                        }
                    }
                }

            }
            //           }

        }

    }

    public void AddnewItemCompareList(ArrayList<Category_Item> oldList1, ArrayList<Category_Item> oldList2) {

        for (int i = 0; i < oldList1.size(); i++) {

            if (oldList1.get(i).getId() != oldList2.get(i).getId() || oldList1.get(i).getSetid() != oldList2.get(i).getSetid() || oldList1.get(i).getQuantity() != oldList2.get(i).getQuantity()) {


                Category_Item item = new Category_Item();
                item.setId(oldList2.get(i).getId());
                item.setItemName(oldList2.get(i).getItemName());
                item.setPrice(oldList2.get(i).getPrice());
                item.setQuantity(oldList2.get(i).getQuantity());
                item.setDiscount_id(oldList2.get(i).getDiscount_id());
                item.setPromotion_id(oldList2.get(i).getPromotion_id());
                item.setDiscount(oldList2.get(i).getDiscount());
                item.setTotalDiscount(oldList2.get(i).getTotalDiscount());
                item.setDiscount_type(oldList2.get(i).getDiscount_type());
                item.setExtra(oldList2.get(i).getExtra());
                item.setExtraPrice(oldList2.get(i).getExtraPrice());
                item.setTotalExtraPrice(oldList2.get(i).getTotalExtraPrice());
                item.setAmount(oldList2.get(i).getAmount());
                item.setTotalAmount(oldList2.get(i).getTotalAmount());
                item.setTakeAway(oldList2.get(i).getTakeAway());
                item.setCategoryId(oldList2.get(i).getCategoryId());
                item.setUserRemark(oldList2.get(i).getUserRemark());
                item.setItem_check(oldList2.get(i).getItem_check());
                item.setOrder_type_id(oldList2.get(i).getOrder_type_id());
                item.setSetid(oldList2.get(i).getSetid());
                item.setSet_menu_name(oldList2.get(i).getSet_menu_name());
                item.setSet_item_id(oldList2.get(i).getSet_item_id());
                item.setTakeid(oldList2.get(i).getTakeid());
                item.setStatusid(oldList2.get(i).getStatusid());
                item.setOrderIDD(oldList2.get(i).getOrderIDD());
                item.setOrderDetailIDD(oldList2.get(i).getOrderDetailIDD());
                item.setState("edit");

                ArrayList<AddOn> tempAddonList = new ArrayList<>();

                for (AddOn addon : oldList2.get(i).getAddOnArrayList()) {

                    AddOn addondata = new AddOn();
                    addondata.setId(addon.getId());
                    addondata.setCategory_id(addon.getCategory_id());
                    addondata.setFood_name(addon.getFood_name());
                    addondata.setImage(addon.getImage());
                    addondata.setPrice(addon.getPrice());
                    addondata.setSelected(addon.isSelected());
                    addondata.setStatus(addon.getStatus());

                    tempAddonList.add(addondata);

                    item.setAddOnArrayList(tempAddonList);

                }
                item.setSetItemArrayList(oldList2.get(i).getSetItemArrayList());

                oldList1.set(i, item);


            }

            if (oldList1.get(i).getAddOnArrayList() != null && oldList1.get(i).getAddOnArrayList().size() != 0 && oldList2.get(i).getAddOnArrayList() != null && oldList2.get(i).getAddOnArrayList().size() != 0) {

                for (int j = 0; j < oldList1.get(i).getAddOnArrayList().size(); j++) {

                    if (oldList1.get(i).getAddOnArrayList() != null && oldList1.get(i).getAddOnArrayList().size() != 0 && oldList2.get(i).getAddOnArrayList() != null && oldList2.get(i).getAddOnArrayList().size() != 0) {
                        boolean a = oldList1.get(i).getAddOnArrayList().get(j).isSelected();
                        boolean b = oldList2.get(i).getAddOnArrayList().get(j).isSelected();

                        if (a != b) {

                            oldList1.get(i).getAddOnArrayList().get(j).setSelected(b);
                            oldList1.get(i).setState("edit");

                        }
                    }
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (socket.connected() || socket.hasListeners("order_remove")) {
            socket.disconnect();
            socket.off("order_remove", onNewMessage);
        }
    }
}

