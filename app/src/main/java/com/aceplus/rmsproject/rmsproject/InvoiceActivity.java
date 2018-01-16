package com.aceplus.rmsproject.rmsproject;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.aceplus.rmsproject.rmsproject.object.JsonTest;
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
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    // public static ArrayList<InvoiceDetailProduct> detailProductArrayList = new ArrayList<>();
    public ArrayList<InvoiceDetailProduct> detailProductArrayList = new ArrayList<>();
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
    HashMap<String, String> detailDataMap;
    String con_name;

    Socket socket;
    Activity activity = this;

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

//         handler = new Handler();
//        handler.post(new Runnable() {
//            @Override
//            public void run() {
//                if (activity.isFinishing()) {
//                    return;
//                } else {
//                    socket.on("invoice_update", onNewMessage);
//                    socket.connect();
//                }
//            }
//        });

        socket.on("invoice_update", onNewMessage);
        socket.connect();


        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("X-Authorization", getActivateKeyFromDB()).build();
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
        detailDataMap = new HashMap<>();
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

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    try {
//                        Thread.sleep(500);
                    getInvoiceData();
                    adapter.notifyDataSetChanged();
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                }
            });
        }
    };

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
                tableeeID += getTableNoFromDB(download_forShow_tableID.getTable_id());
            } else {
                if (i == download_orderTableArrayList.size() - 1) {
                    tableeeID += getTableNoFromDB(download_forShow_tableID.getTable_id());
                } else {
                    tableeeID += getTableNoFromDB(download_forShow_tableID.getTable_id());
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
            roommmID = getRoomNoFromDB(download_forShow_roomID.getRoom_id());
        }

        detailDataMap.put("RommCharge", roommmID);
        //InvoiceDetailActivity.RommCharge = "";
        //InvoiceDetailActivity.RommCharge = roommmID;
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

    private String getTableNoFromDB(String tableId) {
        String tableNo = null;
        Cursor cur = database.rawQuery("SELECT * FROM tableList WHERE id = '" + tableId + "'", null);
        while (cur.moveToNext()) {
            tableNo = cur.getString(cur.getColumnIndex("table_no"));
        }
        cur.close();
        return tableNo;
    }

    private String getRoomNoFromDB(String roomId) {
        String tableNo = null;
        Cursor cur = database.rawQuery("SELECT * FROM room WHERE id = '" + roomId + "'", null);
        while (cur.moveToNext()) {
            tableNo = cur.getString(cur.getColumnIndex("room_name"));
        }
        cur.close();
        return tableNo;
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

                    detailDataMap.put("vouncherID", invoice.getVouncherID());
                    detailDataMap.put("date", invoice.getDate());
                    //detailDataMap.put("tableNo", "TAKE AWAY");
                    detailDataMap.put("tableNo", invoice.getRoonOrTable());

//                    InvoiceDetailActivity.vouncherID = invoice.getVouncherID();
//                    InvoiceDetailActivity.userID = invoice.getUser_id();
//                    InvoiceDetailActivity.date = invoice.getDate();
//                    InvoiceDetailActivity.tableNo = "TAKE AWAY";
                    if (order_table.getTable_id() == null) {
                        Log.d("InvTableID", "no" + tableID);
                    } else {
                        detailDataMap.put("tableNo", order_table.getTable_id());
                        detailDataMap.put("TABLE_ID", order_table.getTable_id());
//                        InvoiceDetailActivity.tableNo = order_table.getTable_id();
//                        InvoiceDetailActivity.TABLE_ID = order_table.getTable_id();
                    }
                    if (order_room.getRoom_id() == null) {
                        Log.d("InvTableID", "no" + tableID);
                    } else {
                        detailDataMap.put("tableNo", order_room.getRoom_id());
                        detailDataMap.put("ROOM_ID", order_room.getRoom_id());
//                        InvoiceDetailActivity.tableNo = order_room.getRoom_id();
//                        InvoiceDetailActivity.ROOM_ID = order_room.getRoom_id();
                    }
                    detailDataMap.put("totalAmount", invoice.getTotalAmount());
                    detailDataMap.put("totalDiscount", invoice.getDiscountAmount());
                    detailDataMap.put("totalExtra", invoice.getExtraAmount());
                    detailDataMap.put("netAmount", invoice.getNetAmount());

//                    InvoiceDetailActivity.totalAmount = invoice.getTotalAmount();
//                    InvoiceDetailActivity.totalDiscount = invoice.getDiscountAmount();
//                    InvoiceDetailActivity.totalExtra = invoice.getExtraAmount();
//                    InvoiceDetailActivity.netAmount = invoice.getNetAmount();

                    Log.i("detailProductArrayList>CallIntent>>>", String.valueOf(detailProductArrayList.size()));
                    Intent intent = new Intent(InvoiceActivity.this, InvoiceDetailActivity.class);
                    Bundle args = new Bundle();
                    //args.putSerializable("ARRAYLIST", detailProductArrayList);
                    args.putSerializable("invoice", invoice);
                    args.putSerializable("Map", detailDataMap);
                    intent.putExtra("BUNDLE", args);
                    startActivity(intent);
                    finish();
                    //detailProductArrayList = getInvDetailProduct(invoice);
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
        if (activity.isFinishing()) {
            return;
        } else {
            builder.show();
        }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        socket.disconnect();
        socket.off("invoice_update", onNewMessage);
    }
}