package com.aceplus.rmsproject.rmsproject;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aceplus.rmsproject.rmsproject.object.Download_ForInvoiceDetail;
import com.aceplus.rmsproject.rmsproject.object.Download_ForInvoiceExtraDetail;
import com.aceplus.rmsproject.rmsproject.object.Download_ForInvoiceSetItemDetail;
import com.aceplus.rmsproject.rmsproject.object.Download_ForInvoiveItemDetail;
import com.aceplus.rmsproject.rmsproject.object.Download_OrderStatus;
import com.aceplus.rmsproject.rmsproject.object.Download_forInvoice;
import com.aceplus.rmsproject.rmsproject.object.Invoice;
import com.aceplus.rmsproject.rmsproject.object.InvoiceDetailProduct;
import com.aceplus.rmsproject.rmsproject.object.InvoiceDetailProductSetItem;
import com.aceplus.rmsproject.rmsproject.object.JsonResponseforInvoice;
import com.aceplus.rmsproject.rmsproject.object.JsonResponseforInvoiceDetail;
import com.aceplus.rmsproject.rmsproject.object.Order_Complete;
import com.aceplus.rmsproject.rmsproject.object.order_room;
import com.aceplus.rmsproject.rmsproject.object.order_table;
import com.aceplus.rmsproject.rmsproject.utils.Database;
import com.aceplus.rmsproject.rmsproject.utils.Download_forShow_roomID;
import com.aceplus.rmsproject.rmsproject.utils.Download_forShow_tableID;
import com.aceplus.rmsproject.rmsproject.utils.GetDevID;
import com.aceplus.rmsproject.rmsproject.utils.JsonForShowRoomId;
import com.aceplus.rmsproject.rmsproject.utils.JsonForShowTableId;
import com.aceplus.rmsproject.rmsproject.utils.RequestInterface;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class InvoiceActivity extends AppCompatActivity {

    private Toolbar toolbar;
    SQLiteDatabase database;
    private Retrofit retrofit;
    String tableID = "";
    String roomID = "";
    double taxAmt = 0.0;
    double serviceAmt = 0.0;
    ArrayList<Invoice> invoiceArrayList = new ArrayList<>();
    private ArrayList<Download_forShow_tableID> download_orderTableArrayList = new ArrayList<>();
    private ArrayList<Download_forShow_roomID> download_orderRoomArrayList = new ArrayList<>();
    public static ArrayList<InvoiceDetailProduct> detailProductArrayList = new ArrayList<>();
    private ArrayList<Download_OrderStatus> download_orderStatusArrayList;
    private ArrayList<Order_Complete> completeArrayList = new ArrayList<>();
    private ArrayList<Download_forInvoice> download_forInvoiceArrayList;
    private ArrayList<Invoice> forInvoice = new ArrayList<>();
    DecimalFormat commaSepFormat = new DecimalFormat("###,##0");
    ArrayList<Invoice> temp = new ArrayList<>();
    private RecyclerView.Adapter adapter;
    private ProgressDialog mProgressDialog;
    RecyclerView recyclerView;
    String orderIDforBindView;
    String DataForRoomTable;
    Handler handler = null;

    String con_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        try {
            database = new Database(this).getDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new InvoiceAdapter(forInvoice);
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("X-Authorization", "25c512a9b6b76c778e321e35606016f10e95e74b").build();
                return chain.proceed(newRequest);
            }
        };
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC));
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        Log.i("temp>>>", forInvoice.size() + "");
        mProgressDialog = new ProgressDialog(InvoiceActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mProgressDialog.setMessage("Download order status data....");
        mProgressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {   //start a new thread to process job
                try {  //heavy job here   //send message to main thread
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(0);
            }
        }).start();
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                mProgressDialog.dismiss();
            }
        };
        getInvoiceData();
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            GestureDetector gestureDetector = new GestureDetector(getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

            });

            @SuppressLint("LongLogTag")
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());
                if (child != null && gestureDetector.onTouchEvent(e)) {
                    int position = rv.getChildAdapterPosition(child);
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {
            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
            }
        });
    }

    private ArrayList<Invoice> getInvoiceData() {  // getting all invoice data for invoice activity
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JsonResponseforInvoice> call = request.getforInvoice(getActivateKeyFromDB());
        call.enqueue(new Callback<JsonResponseforInvoice>() {
            @Override
            public void onResponse(Call<JsonResponseforInvoice> call, Response<JsonResponseforInvoice> response) {
                try {
                    Log.i("respons_code>>>>>", response.code() + "");
                    JsonResponseforInvoice jsonResponseforInvoice = response.body();
                    download_forInvoiceArrayList = jsonResponseforInvoice.getForInvoices();
                    Log.i("<<<<<<<<body>>>>>", download_forInvoiceArrayList + "");
                    forInvoice.clear();
                    for (Download_forInvoice download_forInvoice : download_forInvoiceArrayList) {
                        Invoice invoice = new Invoice();
                        invoice.setVouncherID(download_forInvoice.getId());
                        DataForRoomTable = getRoomOrTable(invoice.getVouncherID());
                        Log.i("DataForRoomTable", DataForRoomTable + "ffff");
                        invoice.setRoonOrTable(DataForRoomTable);
                        Log.i("invoicesize>>>>>>>", invoice.getVouncherID() + "");
                        invoice.setTake_id(download_forInvoice.getTakeId());
                        invoice.setDate(download_forInvoice.getOrderTime());
                        invoice.setDiscountAmount(download_forInvoice.getTotal_discount_amount());
                        invoice.setExtraAmount(download_forInvoice.getTotal_extra_price());
                        invoice.setTotalAmount(download_forInvoice.getTotalPrice());
                        invoice.setNetAmount(download_forInvoice.getAllTotalAmount());
                        invoice.setStatus(Integer.parseInt(download_forInvoice.getStatus()));
                        forInvoice.add(invoice);
                    }
                    Log.i("invoicesize>>>>>>>", forInvoice.size() + "");
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog(" Data Are Null! ");
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonResponseforInvoice> call, Throwable t) {
                mProgressDialog.dismiss();
                callUploadDialog("Please download again!");
            }
        });
        Log.i("temp>2222>>", forInvoice.size() + "");
        return forInvoice;
    }

    private String getRoomOrTable(String VoucherID) {       //checking  order from room or table or take away !
        String RoomOrTable = null;
        String Room = null;
        String Table = null;
        Table = gettableIDD(VoucherID);
        Log.i("table?!?!?!?!?!", Table + "");
        Room = getroomIDD(VoucherID);
        Log.i("Room?!?!?!?!?!", Room + "");
        if (Room.equals(null) || Room == null || Room.equals("")) {
            RoomOrTable = Table;
        } else {
            RoomOrTable = Room;
        }
        return RoomOrTable;
    }

    private String gettableIDD(String orderID) {
        String tableeeID = "";
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<JsonForShowTableId> call = requestInterface.getforshowOrderTable(getActivateKeyFromDB(), orderID);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        JsonForShowTableId jsonForShowTableId = null;
        try {
            jsonForShowTableId = call.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        download_orderTableArrayList = jsonForShowTableId.getForShow_tableID();
        for (int i = 0; i < download_orderTableArrayList.size(); i++) {
            Download_forShow_tableID download_forShow_tableID = download_orderTableArrayList.get(i);
            if (download_orderTableArrayList.size() == 1) {
                tableeeID += "TABLE " + download_forShow_tableID.getTable_id();
            } else {
                if (i == download_orderTableArrayList.size() - 1) {
                    tableeeID += "TABLE " + download_forShow_tableID.getTable_id();
                } else {
                    tableeeID += "TABLE " + download_forShow_tableID.getTable_id();
                    tableeeID += ",";
                }
            }
        }
        return tableeeID;
    }

    private String getroomIDD(String orderID) {
        String roommmID = "";
        RequestInterface requestInterfaces = retrofit.create(RequestInterface.class);
        Call<JsonForShowRoomId> calls = requestInterfaces.getforshowOrderRoom(getActivateKeyFromDB(), orderID);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        JsonForShowRoomId jsonForShowRoomId = null;
        try {
            jsonForShowRoomId = calls.execute().body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        download_orderRoomArrayList = jsonForShowRoomId.getForShow_roomID();
        for (Download_forShow_roomID download_forShow_roomID : download_orderRoomArrayList) {
            roommmID = "ROOM " + (download_forShow_roomID.getRoom_id());
        }
        InvoiceDetailActivity.RommCharge = "";
        InvoiceDetailActivity.RommCharge = roommmID;
        return roommmID;
    }

    private String getActivateKeyFromDB() {
        String backend_activate_key = null;
        Cursor cur = database.rawQuery("SELECT * FROM activate_key", null);
        while (cur.moveToNext()) {
            backend_activate_key = cur.getString(cur.getColumnIndex("backend_activation_key"));
        }
        cur.close();
        return backend_activate_key;
    }

    //for InvoiceDetail data and pass to invoicedetail.activity!
    @SuppressLint("LongLogTag")
    private ArrayList<InvoiceDetailProduct> getInvDetailProduct(final Invoice invoice) {
        String vouncherID = invoice.getVouncherID();
        Log.i("voucherID>>>>hak>>>", vouncherID);
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<JsonResponseforInvoiceDetail> call = requestInterface.getforInvoiceDetail(GetDevID.getActivateKeyFromDB(this), vouncherID);
        call.enqueue(new Callback<JsonResponseforInvoiceDetail>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JsonResponseforInvoiceDetail> call, Response<JsonResponseforInvoiceDetail> response) {
                detailProductArrayList.clear();
                JsonResponseforInvoiceDetail jsonResponseforInvoiceDetail = response.body();
                ArrayList<Download_ForInvoiceDetail> Download_ForInvoiveDetailArrayList = jsonResponseforInvoiceDetail.getDownload_forInvoiceDetailArrayList();
                for (Download_ForInvoiceDetail download_forInvoiceDetail : Download_ForInvoiveDetailArrayList) {
                    InvoiceDetailActivity.userID = download_forInvoiceDetail.getUserId();
                    ArrayList<Download_ForInvoiveItemDetail> Download_ForInoviceItemDetailArrayList = download_forInvoiceDetail.getForInvoiveItemDetail();
                    for (Download_ForInvoiveItemDetail download_forInvoiveItemDetail : Download_ForInoviceItemDetailArrayList) {
                        InvoiceDetailProduct invDetail = new InvoiceDetailProduct();
                        invDetail.setId(download_forInvoiveItemDetail.getOrderDetailId());
                        final String Name;
                        if (Integer.parseInt(download_forInvoiveItemDetail.getSetmenuId()) == 0) {
                            String Itemid = download_forInvoiveItemDetail.getItemId();

                            Cursor cursor=database.rawQuery("SELECT * FROM item where id='"+Itemid+"' and has_contiment ="+1,null);

                            if (cursor.getCount()>0) {

                                while (cursor.moveToNext()) {

                                    int con_id = cursor.getInt(cursor.getColumnIndex("contiment_id"));

                                    Cursor cursor1 = database.rawQuery("SELECT * FROM contiment where id=" + con_id, null);

                                    while (cursor1.moveToNext()) {

                                        con_name = cursor1.getString(cursor1.getColumnIndex("name"));

                                    }

                                }
                                String item_name = getItemName(Itemid);
                                Name = con_name + " " + item_name;

                            }else {

                                Name=getItemName(Itemid);

                            }

                        } else {
                            String SetMenuid = download_forInvoiveItemDetail.getSetmenuId();
                            Name = getSetMenuName(SetMenuid);
                        }
                        invDetail.setItemName(Name);

                        ArrayList<Download_ForInvoiceSetItemDetail> download_forInvoiceSetItemDetailArrayList = download_forInvoiveItemDetail.getOrderSetMenus();
                        if (download_forInvoiceSetItemDetailArrayList.size() == 0) {
                            invDetail.setInvoiceDetailProductSetItemArrayList(null);
                        } else {
                            ArrayList<InvoiceDetailProductSetItem> invoiceDetailProductSetItemArrayList = new ArrayList<InvoiceDetailProductSetItem>();

                            for (Download_ForInvoiceSetItemDetail download_forInvoiceSetItemDetail : download_forInvoiceSetItemDetailArrayList) {
                                InvoiceDetailProductSetItem invoiceDetailProductSetItem = new InvoiceDetailProductSetItem();
                                invoiceDetailProductSetItem.setItemId(download_forInvoiceSetItemDetail.getItemId());
                                invoiceDetailProductSetItem.setSetMenuId(download_forInvoiceSetItemDetail.getSetmenuId());
                                invoiceDetailProductSetItem.setStatusId(download_forInvoiceSetItemDetail.getStatusId());
                                invoiceDetailProductSetItemArrayList.add(invoiceDetailProductSetItem);
                            }

                            invDetail.setInvoiceDetailProductSetItemArrayList(invoiceDetailProductSetItemArrayList);

                        }


                        invDetail.setPrice(String.valueOf(commaSepFormat.format(download_forInvoiveItemDetail.getAmount())));
                        invDetail.setQuantity(commaSepFormat.format(download_forInvoiveItemDetail.getQuantity()));
                        invDetail.setDiscount(commaSepFormat.format(download_forInvoiveItemDetail.getDiscountAmount()));
                        invDetail.setAmount(commaSepFormat.format(download_forInvoiveItemDetail.getAmountWithDiscount()));
                        invDetail.setStatus(download_forInvoiveItemDetail.getStatusId());
                        Double ExtraAmount = 0.0;
                        int i = 0;
                        ArrayList<Download_ForInvoiceExtraDetail> download_forInvoiceExtraDetailsArrayList = download_forInvoiveItemDetail.getOrderExtras();
                        if (download_forInvoiceExtraDetailsArrayList.size() == 0) {
                            ExtraAmount = Double.valueOf(0);
                            invDetail.setExtraPrice(String.valueOf(Double.valueOf(0)));
                        } else {
                            for (Download_ForInvoiceExtraDetail download_forInvoiceExtraDetail : download_forInvoiceExtraDetailsArrayList) {
                                Log.i("ExtraAmount_size>>>>>>>>>>>>", download_forInvoiceExtraDetailsArrayList.size() + "");
                                if (download_forInvoiceExtraDetailsArrayList.size() > 1) {
                                    Log.i("extraamount>-------", download_forInvoiceExtraDetail.getAmount() + "");
                                    ExtraAmount += download_forInvoiceExtraDetail.getAmount();
                                } else {
                                    ExtraAmount = download_forInvoiceExtraDetail.getAmount();
                                }
                                Log.i("ExtraAmount>>>>>>>>>>>>", download_forInvoiceExtraDetail.getAmount() + "");
                            }
                        }
                        invDetail.setExtraPrice(String.valueOf(ExtraAmount));

                        Log.i("status>>>>hak>>>>", invDetail.getStatus().toString());
                        Log.i("ID>>>>hak>>>>", invDetail.getId().toString());
                        Log.i("NAME>>>>hak>>>>", invDetail.getItemName().toString());
                        Log.i("PRICE>>>>hak>>>>", invDetail.getPrice().toString());
                        Log.i("AMOUNT>>>>hak>>>>", invDetail.getAmount().toString());
                        Log.i("DISCOUNT>>>>hak>>>>", invDetail.getDiscount().toString());
                        Log.i("EXTRA>>>>hak>>>>", invDetail.getExtraPrice().toString());
                        detailProductArrayList.add(invDetail);
                    }
                }
                Log.i("detailProductArrayList>>Response>>", String.valueOf(detailProductArrayList.size()));
                InvoiceDetailActivity.vouncherID = invoice.getVouncherID();

                InvoiceDetailActivity.date = invoice.getDate();
                String invoiceIDDD = invoice.getVouncherID();
                String TableID = null;
                String RoomID = null;
                String RoomOrTable22 = null;
                TableID = gettableIDD(invoiceIDDD);
                RoomID = getroomIDD(invoiceIDDD);
                if (RoomID.equals(null) || RoomID == null || RoomID.equals("")) {
                    RoomOrTable22 = TableID;
                } else {
                    RoomOrTable22 = RoomID;
                }
                if (RoomOrTable22 != null) {
                    InvoiceDetailActivity.tableNo = RoomOrTable22;
                } else {
                    InvoiceDetailActivity.tableNo = "TAKE AWAY";
                }
                InvoiceDetailActivity.totalAmount = invoice.getTotalAmount();
                InvoiceDetailActivity.totalDiscount = invoice.getDiscountAmount();
                InvoiceDetailActivity.totalExtra = invoice.getExtraAmount();
                Log.i("extrapriceamount", invoice.getExtraAmount() + "");
                InvoiceDetailActivity.netAmount = invoice.getNetAmount();
                startActivity(new Intent(InvoiceActivity.this, InvoiceDetailActivity.class));
            }

            @Override
            public void onFailure(Call<JsonResponseforInvoiceDetail> call, Throwable t) {
                Toast.makeText(InvoiceActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        return detailProductArrayList;
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
        Cursor cursor = database.rawQuery("SELECT * FROM item WHERE id = '" + item_id_forName + "' ", null);
        while (cursor.moveToNext()) {
            NameStr = cursor.getString(cursor.getColumnIndex("name"));
        }
        return NameStr;
    }

    public class InvoiceAdapter extends RecyclerView.Adapter<InvoiceAdapter.ViewHolder> {   // view methods
        private ArrayList<Invoice> invoiceList;

        public InvoiceAdapter(ArrayList<Invoice> invoiceList) {
            this.invoiceList = invoiceList;
            Log.i("onbindviewinvoice", invoiceList + "");
        }

        @Override
        public InvoiceAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            final View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.invoice_detail_layout, viewGroup, false);
            view.setOnClickListener(new View.OnClickListener() {
                @SuppressLint("LongLogTag")
                @Override
                public void onClick(View v) {
                    Toast.makeText(InvoiceActivity.this, "Please Wait !", Toast.LENGTH_SHORT).show();
                    int position = recyclerView.getChildAdapterPosition(view);
                    Invoice invoice = forInvoice.get(position);
                    detailProductArrayList = getInvDetailProduct(invoice);
                    InvoiceDetailActivity.vouncherID = invoice.getVouncherID();
                    InvoiceDetailActivity.userID = invoice.getUser_id();
                    InvoiceDetailActivity.date = invoice.getDate();
                    InvoiceDetailActivity.tableNo = "TAKE AWAY";
                    if (order_table.getTable_id() == null) {
                        Log.d("InvTableID", "no" + tableID);
                    } else {
                        InvoiceDetailActivity.tableNo = order_table.getTable_id();
                        InvoiceDetailActivity.TABLE_ID = order_table.getTable_id();
                    }
                    if (order_room.getRoom_id() == null) {
                        Log.d("InvTableID", "no" + tableID);
                    } else {
                        InvoiceDetailActivity.tableNo = order_room.getRoom_id();
                        InvoiceDetailActivity.ROOM_ID = order_room.getRoom_id();
                    }
                    InvoiceDetailActivity.totalAmount = invoice.getTotalAmount();
                    InvoiceDetailActivity.totalDiscount = invoice.getDiscountAmount();
                    InvoiceDetailActivity.totalExtra = invoice.getExtraAmount();
                    InvoiceDetailActivity.netAmount = invoice.getNetAmount();
                    //startActivity(new Intent(InvoiceActivity.this, InvoiceDetailActivity.class));



                    Log.i("detailProductArrayList>CallIntent>>>", String.valueOf(detailProductArrayList.size()));
                }
            });
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final InvoiceAdapter.ViewHolder viewHolder, int position) {
            Invoice invoice = invoiceList.get(position);
            viewHolder.vouncherTxt.setText(invoice.getVouncherID());

//            Log.i("Status",invoice.getStatus()+"");
            if (invoice.getStatus() == 1) {

                if (invoice.getRoonOrTable() == "") {
                    viewHolder.tableTxt.setText("TAKE AWAY");
                } else {
                    viewHolder.tableTxt.setText(invoice.getRoonOrTable());
                }
                viewHolder.dateTxt.setText(invoice.getDate());
                viewHolder.totalTxt.setText(invoice.getTotalAmount());
                viewHolder.discountTxt.setText(invoice.getDiscountAmount());
                viewHolder.netTxt.setText(invoice.getNetAmount());

            }


        }

        @Override
        public int getItemCount() {
            return invoiceList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView vouncherTxt;
            private TextView tableTxt;
            private TextView dateTxt;
            private TextView totalTxt;
            private TextView discountTxt;
            private TextView netTxt;

            public ViewHolder(View view) {
                super(view);
                vouncherTxt = (TextView) view.findViewById(R.id.vouncher_id_txt);
                tableTxt = (TextView) view.findViewById(R.id.table_id_txt);
                dateTxt = (TextView) view.findViewById(R.id.date_txt);
                totalTxt = (TextView) view.findViewById(R.id.total_amt_txt);
                discountTxt = (TextView) view.findViewById(R.id.discount_amt_txt);
                netTxt = (TextView) view.findViewById(R.id.net_amt_txt);
            }
        }
    }

    private void callUploadDialog(String message) {
        final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(InvoiceActivity.this, R.style.InvitationDialog)
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
        builder.show();
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(InvoiceActivity.this, HomePageActivity.class));
        finish();
    }
}