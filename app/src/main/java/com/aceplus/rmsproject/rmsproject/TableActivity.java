package com.aceplus.rmsproject.rmsproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aceplus.rmsproject.rmsproject.object.BRoom;
import com.aceplus.rmsproject.rmsproject.object.BTable;
import com.aceplus.rmsproject.rmsproject.object.BookingTable;
import com.aceplus.rmsproject.rmsproject.object.Category;
import com.aceplus.rmsproject.rmsproject.object.Download_Booking;
import com.aceplus.rmsproject.rmsproject.object.Download_Table;
import com.aceplus.rmsproject.rmsproject.object.Download_ordertable;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseBooking;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseTable;
import com.aceplus.rmsproject.rmsproject.object.JsonTest;
import com.aceplus.rmsproject.rmsproject.object.Success;
import com.aceplus.rmsproject.rmsproject.utils.Database;
import com.aceplus.rmsproject.rmsproject.utils.RequestInterface;
import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TableActivity extends ActionBarActivity {

    Call<Success> callc;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button transferBtn;
    private Button groupBtn;
    SQLiteDatabase database;
    private TableAdapter adapter;
    ArrayList<String> tableName = new ArrayList<String>();
    ArrayList<String> fortransfertableName = new ArrayList<String>();
    ArrayList<BookingTable> bookingTableArrayList = new ArrayList<>();
    ArrayList<BookingTable> getBookingArrayList = new ArrayList<>();
    ArrayList<BookingTable> get4transfertableArrayList = new ArrayList<>();
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:SS");
    private ProgressDialog mProgressDialog;
    private Retrofit retrofit;
    private ArrayList<Download_Table> download_tableArrayList;
    private ArrayList<Download_Booking> download_bookingArrayList;
    private String fromTable = null;
    private String toTable = null;
    private String transferTableName = null;
    private String groupTableID = null;
    private ArrayList<String> groupTableArrayList = new ArrayList<>();
    private int fromPos = 0;
    private int toPos = 0;
    Date fromTime;
    Date toTime;
    String invoiceee_id = null;
    String group_invoiceee_id = null;
    AtomicBoolean ContinueThread;
    Activity activity;

    Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_table);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        activity = this;
        database = new Database(this).getDataBase();
        ContinueThread = new AtomicBoolean(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mProgressDialog = new ProgressDialog(TableActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

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


        socket.on("tableChange", onNewMessage);
        socket.on("invoice_update", onNewMessage);
        socket.connect();


        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("X-Authorization", getActivateKeyFromDB()).build();
                return chain.proceed(newRequest);
            }
        };
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.interceptors().add(interceptor);
        builder.addInterceptor(loggingInterceptor);
        builder.readTimeout(120, TimeUnit.SECONDS);
        builder.connectTimeout(120, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        retrofit = new Retrofit.Builder()
                .baseUrl(MainActivity.URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        loadTableJson();
        registerIDs();
        catchEvents();
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    JSONObject data = (JSONObject) args[0];
//                    String username;
//                    //String message;
//                    try {
//                        username = data.getString("description");
//                        //message = data.getString("message");
//                    } catch (JSONException e) {
//                        return;
//                    }
                    loadTableJson();
                    mProgressDialog.dismiss();
                }
            });
        }
    };

    private void getTableData() {  // data from database !!
        //database.beginTransaction();
        getBookingArrayList.clear();
        get4transfertableArrayList.clear();
        Cursor cur;
        Cursor curBooking = null;
        Cursor curBTable = null;
        Cursor curConfig = null;
        cur = database.rawQuery("SELECT * FROM tableList", null);

        try {
            while (cur.moveToNext()) {
                BookingTable bookingTable = new BookingTable();
                String table_id = cur.getString(cur.getColumnIndex("id"));
                bookingTable.setTableID(table_id);
                bookingTable.setTable_no(cur.getString(cur.getColumnIndex("table_no")));
                bookingTable.setTableStatus(cur.getString(cur.getColumnIndex("status")));
                bookingTable.setTable_check(false);
                curBTable = database.rawQuery("SELECT * FROM booking_table WHERE table_id = \"" + table_id + "\"", null);
                while (curBTable.moveToNext()) {
                    bookingTable.setTable_id(curBTable.getString(curBTable.getColumnIndex("table_id")));
                    String booking_id = curBTable.getString(curBTable.getColumnIndex("booking_id"));
                    String booking_time = null;
                    curBooking = database.rawQuery("SELECT * FROM booking WHERE id = \"" + booking_id + "\"", null);
                    while (curBooking.moveToNext()) {
                        bookingTable.setBookingID(curBooking.getString(curBooking.getColumnIndex("id")));
                        bookingTable.setBooking_time(curBooking.getString(curBooking.getColumnIndex("from_time")));
                        booking_time = curBooking.getString(curBooking.getColumnIndex("from_time"));
                    }
                    curBooking.close();
                    Log.e("TableIDBooking", table_id + "," + booking_id + "," + booking_time + "");
                }
                curBTable.close();

                curConfig = database.rawQuery("SELECT * FROM config", null);
                while (curConfig.moveToNext()) {
                    bookingTable.setBooking_waiting(curConfig.getString(curConfig.getColumnIndex("booking_waiting_time")));
                    bookingTable.setBooking_service(curConfig.getString(curConfig.getColumnIndex("booking_service_time")));
                    bookingTable.setBooking_warning(curConfig.getString(curConfig.getColumnIndex("booking_warning_time")));
                }
                curConfig.close();
                getBookingArrayList.add(bookingTable);
            }

            cur.close();
        } catch (Exception e) {
            Log.e("Get table err", e.getMessage());
        }/* finally {
            cur.close();
            curBooking.close();
            curBTable.close();
            curConfig.close();
        }*/

        //database.setTransactionSuccessful();
        //database.endTransaction();
        getTransferTableData();
    }

    private void getTransferTableData() {
        //database.beginTransaction();
        get4transfertableArrayList.clear();
        Cursor cur;
        Cursor curBooking = null;
        Cursor curBTable = null;
        Cursor curConfig = null;
        cur = database.rawQuery("SELECT * FROM tableList WHERE status = '0'", null);
        try {
            while (cur.moveToNext()) {
                BookingTable bookingTable = new BookingTable();
                String table_id = cur.getString(cur.getColumnIndex("id"));
                bookingTable.setTableID(table_id);
                bookingTable.setTable_no(cur.getString(cur.getColumnIndex("table_no")));
                bookingTable.setTableStatus(cur.getString(cur.getColumnIndex("status")));
                bookingTable.setTable_check(false);
                curBTable = database.rawQuery("SELECT * FROM booking_table WHERE table_id = \"" + table_id + "\"", null);
                while (curBTable.moveToNext()) {
                    bookingTable.setTable_id(curBTable.getString(curBTable.getColumnIndex("table_id")));
                    String booking_id = curBTable.getString(curBTable.getColumnIndex("booking_id"));
                    String booking_time = null;
                    curBooking = database.rawQuery("SELECT * FROM booking WHERE id = \"" + booking_id + "\"", null);
                    while (curBooking.moveToNext()) {
                        bookingTable.setBookingID(curBooking.getString(curBooking.getColumnIndex("id")));
                        bookingTable.setBooking_time(curBooking.getString(curBooking.getColumnIndex("from_time")));
                        booking_time = curBooking.getString(curBooking.getColumnIndex("from_time"));
                    }
                    curBooking.close();
                    Log.e("TableIDBooking", table_id + "," + booking_id + "," + booking_time + "");
                }
                curBTable.close();

                curConfig = database.rawQuery("SELECT * FROM config", null);
                while (curConfig.moveToNext()) {
                    bookingTable.setBooking_waiting(curConfig.getString(curConfig.getColumnIndex("booking_waiting_time")));
                    bookingTable.setBooking_service(curConfig.getString(curConfig.getColumnIndex("booking_service_time")));
                    bookingTable.setBooking_warning(curConfig.getString(curConfig.getColumnIndex("booking_warning_time")));
                }
                curConfig.close();
                get4transfertableArrayList.add(bookingTable);
            }
            cur.close();
        } catch (Exception e) {

        }/* finally {
            cur.close();
            curBooking.close();
            curBTable.close();
            curConfig.close();
        }*/

        //database.setTransactionSuccessful();
        //database.endTransaction();
    }

    private void registerIDs() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        transferBtn = (Button) findViewById(R.id.transfer_btn);
        groupBtn = (Button) findViewById(R.id.group_btn);
    }

    private String getTableInvoiceDataInDB(String table_id) {  // get invoice data fromback end !
        String callinvoiceid = "";
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<Download_ordertable> call = requestInterface.getOrderTable(getActivateKeyFromDB(), table_id);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            Download_ordertable download_ordertable = call.execute().body();
            callinvoiceid = download_ordertable.getOrder_id();
            Log.i("jflskdfjjslkdfj", download_ordertable.getOrder_id() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return callinvoiceid;
    }

    private String getGroupTableInvoiceDataInDB(String table_id) {  // for group table
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<Download_ordertable> call = requestInterface.getOrderTable(getActivateKeyFromDB(), table_id);
        call.enqueue(new Callback<Download_ordertable>() {
            @Override
            public void onResponse(Call<Download_ordertable> call, Response<Download_ordertable> response) {
                Download_ordertable download_ordertable = response.body();
                group_invoiceee_id = download_ordertable.getOrder_id();

            }

            @Override
            public void onFailure(Call<Download_ordertable> call, Throwable t) {
            }
        });
        return group_invoiceee_id;
    }

    private void callDialog(String messageTxt)  {
        try {
            mProgressDialog = new ProgressDialog(TableActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(messageTxt);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void tableView() {  // for view !
        getTableData();
        bookingTableArrayList.clear();
        for (BookingTable bookingTable : getBookingArrayList) {
            BookingTable bookTable = new BookingTable();
            bookTable.setTableID(bookingTable.getTableID());
            bookTable.setTable_no(bookingTable.getTable_no());
            bookTable.setTable_check(bookingTable.isTable_check());
            bookTable.setTable_check(bookingTable.isTable_check());
            bookTable.setBookingID(bookingTable.getBookingID());
            bookTable.setTable_id(bookingTable.getTable_id());
            bookTable.setRoom_id(bookingTable.getRoom_id());
            bookTable.setBooking_time(bookingTable.getBooking_time());
            Log.e("TestBookingTime", bookingTable.getBooking_time() + "");
            bookTable.setBooking_service(bookingTable.getBooking_service());
            bookTable.setBooking_waiting(bookingTable.getBooking_waiting());
            bookTable.setBooking_warning(bookingTable.getBooking_warning());
            if (bookingTable.getTableStatus().equals("1")) {
                Log.e("ServiceTableID", bookingTable.getTableID() + "");
                bookTable.setTableService("1");
            } else if (bookingTable.getTableStatus().equals("0")) {
                if (bookingTable.getBooking_time().equals("00:00:00")) {
                    bookTable.setBackgroundColor("");
                } else {
                    try {
                        Log.e("Booking_TableID", bookingTable.getTableID() + "");
                        Date booking_time = timeFormat.parse(bookingTable.getBooking_time());
                        int booking_hour = booking_time.getHours();
                        int booking_minute = booking_time.getMinutes();
                        int booking_second = booking_time.getSeconds();
                        Date warning_time = timeFormat.parse(bookingTable.getBooking_warning());
                        int warning_hour = warning_time.getHours();
                        int warning_minute = warning_time.getMinutes();
                        int warning_second = warning_time.getSeconds();
                        Date waiting_time = timeFormat.parse(bookingTable.getBooking_waiting());
                        int waiting_hour = waiting_time.getHours();
                        int waiting_minute = waiting_time.getMinutes();
                        int waiting_second = waiting_time.getSeconds();
                        int to_hour = booking_hour + waiting_hour;
                        int to_minute = booking_minute + waiting_minute;
                        int to_second = booking_second + waiting_second;
                        toTime = timeFormat.parse(to_hour + ":" + to_minute + ":" + to_second);
                        int from_hour = booking_hour - warning_hour;
                        int from_minute = booking_minute - warning_minute;
                        int from_second = booking_second - warning_second;
                        fromTime = timeFormat.parse(from_hour + ":" + from_minute + ":" + from_second);
                        Date CurrentTime = timeFormat.parse(timeFormat.format(new Date()));
                        Log.e("booking_time3", CurrentTime + "" + fromTime + "," + booking_time + "," + toTime);
                        if (CurrentTime.equals(fromTime) || CurrentTime.after(fromTime) && CurrentTime.before(booking_time)) {
                            bookTable.setTableService("2");
                            Log.e("TableService", "2");
                        } else if (CurrentTime.equals(booking_time) || CurrentTime.after(booking_time) && CurrentTime.before(toTime)) {
                            bookTable.setTableService("3");
                            Log.e("TableService", "3");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            bookingTableArrayList.add(bookTable);
        }
        adapter = new TableAdapter(this, bookingTableArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    private void catchEvents() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(5, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog builder = new AlertDialog.Builder(TableActivity.this, R.style.InvitationDialog)
                        .setPositiveButton(R.string.invitation_ok, null)
                        .setNegativeButton(R.string.invitation_cancel, null)
                        .create();
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = layoutInflater.inflate(R.layout.activity_table_transfer_dialog, null);
                tableName.clear();
                fortransfertableName.clear();
                for (BookingTable table : getBookingArrayList) {
                    if (table.getTableStatus().equals("1") || table.getTableStatus().equals(1)) {
                        tableName.add(table.getTable_no());
                    }
                }
                for (BookingTable table : getBookingArrayList) {
                    if (table.getTableStatus().equals("0") || table.getTableStatus().equals(0)) {
                        fortransfertableName.add(table.getTable_no());
                    }
                }
                final Spinner from_spinner = (Spinner) view.findViewById(R.id.from_spinner);
                final Spinner to_spinner = (Spinner) view.findViewById(R.id.to_spinner);
                ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(TableActivity.this, R.layout.spinner_text, tableName);
                stringAdapter.setDropDownViewResource(R.layout.spinner_dropdown_textview);
                from_spinner.setAdapter(stringAdapter);

                if (tableName != null && tableName.size() > 0) {
                    from_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {
                            String fromTableName = tableName.get(position);
                            Toast.makeText(TableActivity.this, fromTableName, Toast.LENGTH_SHORT).show();
                            for (int i = 0; i < bookingTableArrayList.size(); i++) {

                                Log.i("TableNames", bookingTableArrayList.get(i).getTable_no());
                                if (fromTableName.equals(bookingTableArrayList.get(i).getTable_no())) {
                                    Toast.makeText(TableActivity.this, bookingTableArrayList.get(i).getTableID(), Toast.LENGTH_SHORT).show();
                                    fromTable = bookingTableArrayList.get(i).getTableID();
                                }

                            }

                            fromPos = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub


                        }
                    });
                }

                ArrayAdapter<String> toArrayAdapter = new ArrayAdapter<String>(TableActivity.this, R.layout.spinner_text, fortransfertableName);
                toArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_textview);
                to_spinner.setAdapter(toArrayAdapter);

                if (fortransfertableName != null && fortransfertableName.size() > 0 && get4transfertableArrayList != null && get4transfertableArrayList.size() > 0) {
                    to_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                            //getavailable table

                            toTable = get4transfertableArrayList.get(position).getTableID();
                            Log.i("toTable", toTable);
                            toPos = position;
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                            // TODO Auto-generated method stub

                        }
                    });
                }
                builder.setView(view);
                builder.setTitle(R.string.table_transfer);
                builder.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                        if (tableName.size() == 0) {
                            btnAccept.setEnabled(false);
                        } else {
                            btnAccept.setOnClickListener(new View.OnClickListener() {
                                @SuppressLint("LongLogTag")
                                @Override
                                public void onClick(View v) {


                                    Handler handler = new Handler();

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            socket.emit("table_transfer", "transfer_table");
                                            Toast.makeText(TableActivity.this, "SocketFire", Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                    if (fromTable == null && toTable == null) {
                                        Log.e("TableTransfer", "null");
                                    } else {
                                        String arg[] = {fromTable};
                                        ContentValues cv = new ContentValues();
                                        cv.put("table_id", toTable);
//                                        bookingTableArrayList.get(fromPos).setTableService("0");
//                                        Log.i("TableServiceStatus",bookingTableArrayList.get(fromPos).getTableService());
//                                        bookingTableArrayList.get(Integer.parseInt(toTable) - 1).setTableService("1");
//                                        Log.i("TableServiceStatus",bookingTableArrayList.get(Integer.parseInt(toTable) - 1).getTableService());
//                                        adapter.notifyDataSetChanged();
                                        JSONObject tableTransferJson = new JSONObject();
                                        try {
                                            tableTransferJson.put("transfer_from_table_id", fromTable);
                                            tableTransferJson.put("transfer_to_table_id", toTable);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.e("TransferTable", tableTransferJson.toString());
                                        String invoiceIDDD;
                                        invoiceIDDD = getTableInvoiceDataInDB(fromTable);
                                        Log.i("invoiceIDDDinvoiceIDDDinvoiceIDDD", invoiceIDDD + "");
                                        callDialog("Upload Transfer Table data...");   // transfer table method
                                        RequestInterface request = retrofit.create(RequestInterface.class);
                                        callc = request.postTableTransfer(invoiceIDDD, fromTable, toTable);
                                        Log.i("fromTable", fromTable + "");
                                        Log.i("toTable", toTable + "");
                                        if (android.os.Build.VERSION.SDK_INT > 9) {
                                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                            StrictMode.setThreadPolicy(policy);
                                        }
                                        try {
                                            Success jsonResponse = callc.execute().body();
                                            String message = jsonResponse.getMessage();
                                            if (message.equals("Success")) {
                                                Log.d("TableTransfer", message);
                                                mProgressDialog.dismiss();
                                                builder.dismiss();
                                            } else {
                                                Toast.makeText(TableActivity.this, "U can't transfer", Toast.LENGTH_SHORT).show();
                                                builder.dismiss();
                                                mProgressDialog.dismiss();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            builder.dismiss();
                                            mProgressDialog.dismiss();
                                            callUploadDialog("Table transfer is null.");
                                            callUploadDialog("Please upload again!");
                                        }
                                    }
                                    // refreshTableJson();
                                    //loadTableJson();
                                }
                            });
                            final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                            btnDecline.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Toast.makeText(TableActivity.this, "Cancel", Toast.LENGTH_LONG).show();
                                    builder.dismiss();
                                }
                            });
                        }
                    }
                });
                builder.show();
            }
        });
        groupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {   // group table method
                final ArrayList<String> groupTableList = new ArrayList<String>();
                groupTableList.clear();
                for (BookingTable book : bookingTableArrayList) {
                    if (book.isTable_check() == true) {
                        groupTableList.add(book.getTable_no());
                    }
                }
                if (groupTableList.size() >= 2) {
                    getGroupTable();
                } else {
                    callUploadDialog("You need to choose more than one table!");
                }
            }
        });
    }

    private void getGroupTable() {
        final ArrayList<String> groupTableList = new ArrayList<String>();
        groupTableList.clear();
        for (BookingTable book : bookingTableArrayList) {
            if (book.isTable_check() == true) {
                groupTableList.add(book.getTable_no());
            }
        }
        final AlertDialog builder = new AlertDialog.Builder(TableActivity.this, R.style.InvitationDialog)
                .setPositiveButton(R.string.invitation_order, null)
                .setNeutralButton(R.string.invitation_cancel, null)
                .create();

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.table_group_dialog, null);
        ListView tableList = (ListView) view.findViewById(R.id.group_list);
        ArrayAdapter<String> groupTableAdapter = new ArrayAdapter<String>(TableActivity.this, R.layout.group_dialog_list_textview, R.id.group_txt, groupTableList);
        tableList.setAdapter(groupTableAdapter);
        builder.setView(view);
        builder.setTitle(R.string.table_group);
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnOrder = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnOrder.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        JSONArray tableListJsonArray = new JSONArray();
                        groupTableList.clear();

                        for (BookingTable book : bookingTableArrayList) {
                            if (book.isTable_check() == true) {
                                groupTableID = book.getTableID();
                                Log.e("Booking_ID", groupTableID);
                                groupTableArrayList.add(book.getTableID());
                                JSONObject product = new JSONObject();
                                try {
                                    product.put("table_id", book.getTableID());
                                    product.put("booking_id", book.getBookingID() + "");
                                    product.put("status", "1");
                                    product.put("old",0);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                tableListJsonArray.put(product);
                                //book.setTableService("1");
                                book.setTable_check(false);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        Log.e("TableList1", tableListJsonArray.toString());
                        callDialog("Upload Goup Tables Data...");
                        RequestInterface request = retrofit.create(RequestInterface.class);
                        Call<Success> call = request.postTableStatus(tableListJsonArray + "");
                        if (android.os.Build.VERSION.SDK_INT > 9) {
                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);
                        }
                        try {
                            Success jsonResponse = call.execute().body();
                            String message = jsonResponse.getMessage();
                            if (message.equals("Success")) {

                                Handler handler = new Handler();

                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        socket.emit("table_message", "TakeTable");
                                        Toast.makeText(TableActivity.this, "SocketFire", Toast.LENGTH_SHORT).show();
                                    }
                                });

                                Log.d("TableStatus", message);
                                mProgressDialog.dismiss();
                                CategoryActivity.TABLE_ID = groupTableID;
                                Log.e("GroupTableID", groupTableID);
                                CategoryActivity.groupTableArrayList = groupTableArrayList;
                                CategoryActivity.TAKE_AWAY = "table";
                                CategoryActivity.ROOM_ID = null;

                                CategoryActivity.VOUNCHER_ID = null;
                                CategoryActivity.ADD_INVOICE = "NULL";
                                builder.dismiss();
                                startActivity(new Intent(TableActivity.this, CategoryActivity.class));
                                finish();
                            }else {
                                Toast.makeText(activity, "These tables can't be grouped", Toast.LENGTH_SHORT).show();
                                mProgressDialog.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            mProgressDialog.dismiss();
                            callUploadDialog("Group Table is null.");
                        }
                    }
                });
                final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEUTRAL);
                btnDecline.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        for (BookingTable book : bookingTableArrayList) {
                            if (book.isTable_check() == true) {
                                book.setTable_check(false);
                                adapter.notifyDataSetChanged();
                            }
                        }
                        builder.dismiss();
                    }
                });
            }
        });
        builder.show();
    }

    public class TableAdapter extends RecyclerView.Adapter<TableAdapter.MyViewHolder> {   // for view !
        private Context mContext;
        private List<BookingTable> tableList;
        private int PosCheck = 0;
        private boolean onBind;
        String Checking;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tableTxt;
            public CheckBox groupCheck;
            public RelativeLayout backgroundLayout;

            public MyViewHolder(View view) {
                super(view);
                tableTxt = (TextView) view.findViewById(R.id.table_txt);
                groupCheck = (CheckBox) view.findViewById(R.id.group_check);
                backgroundLayout = (RelativeLayout) view.findViewById(R.id.background_layout);
            }
        }

        public TableAdapter(Context mContext, List<BookingTable> tableList) {
            this.mContext = mContext;
            this.tableList = tableList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.table_room_card, parent, false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {
            final BookingTable table = tableList.get(position);
            Log.e("transferTableName", transferTableName + "");
            holder.tableTxt.setText(table.getTable_no());
            if (table.getTableService().equals("2")) {
                holder.backgroundLayout.setBackgroundColor(Color.parseColor("#f7941d"));
                holder.groupCheck.setVisibility(View.VISIBLE);//Change Gone to Visible
            } else if (table.getTableService().equals("3")) {
                holder.backgroundLayout.setBackgroundColor(Color.parseColor("#ed1c24"));
                holder.groupCheck.setVisibility(View.VISIBLE);//Change Gone to Visible
            } else if (table.getTableService().equals("1")) {
                holder.groupCheck.setVisibility(View.VISIBLE);
                holder.backgroundLayout.setBackgroundColor(Color.parseColor("#00bff3"));
            } else {
                holder.groupCheck.setVisibility(View.VISIBLE);
                holder.backgroundLayout.setBackgroundColor(Color.parseColor("#8dc63f"));
            }
            Log.e("TableService", table.getTableService());
            Checking = table.getTableService();
            Log.d("BackgroundColor", table.getBackgroundColor());
            holder.groupCheck.setOnClickListener(new CompoundButton.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (table.getTableService() == "1") {
                        try {
                            final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(TableActivity.this, R.style.InvitationDialog)
                                    .setPositiveButton(R.string.invitation_ok, null)
                                    .create();
                            builder.setTitle(R.string.alert);
                            builder.setMessage("Can't group this table");
                            builder.setOnShowListener(new DialogInterface.OnShowListener() {
                                @Override
                                public void onShow(DialogInterface dialog) {
                                    final Button btnAccept = builder.getButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE);
                                    btnAccept.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            builder.dismiss();
                                            holder.groupCheck.setChecked(false);
                                        }
                                    });
                                }
                            });
                            builder.show();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
            holder.groupCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    table.setTable_check(isChecked);
                    try {
                        adapter.notifyItemChanged(position);
                    } catch (Exception e) {
                        Log.e("onCheckChanged", e.getMessage());
                    }


                    if (table.getTableService() == "1") {
                        onBind = false;
                    } else {
                        table.setTable_check(isChecked);
                        try {
                            adapter.notifyItemChanged(position);
                        } catch (Exception e) {
                            Log.e("onCheckChanged", e.getMessage());
                        }
                    }
                }

            });
            holder.groupCheck.setChecked(table.isTable_check());
            holder.backgroundLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {  // when you enter the table !

                    if (table.getTableService().equals("1")) {
                        final JSONArray tableListJsonArray = new JSONArray();
                        JSONObject product = new JSONObject();
                        try {
                            product.put("booking_id", table.getBookingID() + "");
                            product.put("status", "1");
                            product.put("table_id", table.getTableID() + "");
                            product.put("old", "1");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tableListJsonArray.put(product);
                        Log.e("TableList", tableListJsonArray.toString());
                        callDialog("Upload table data...");

                        RequestInterface request = retrofit.create(RequestInterface.class);
                        Call<Success> call = request.postTableStatus(tableListJsonArray.toString());
                        call.enqueue(new Callback<Success>() {
                            @Override
                            public void onResponse(Call<Success> call, Response<Success> response) {
                                Success jsonResponse = response.body();
                                String message = jsonResponse.getMessage();
                                if (message.equals("Success")) {
                                    Log.d("TableStatus", message);
                                    mProgressDialog.dismiss();
                                    CategoryActivity.TABLE_ID = table.getTableID();
                                    ArrayList<String> tableNameList = new ArrayList<String>();
                                    tableNameList.add(table.getTableID());
                                    CategoryActivity.groupTableArrayList = tableNameList;
                                    CategoryActivity.TAKE_AWAY = "table";
                                    CategoryActivity.ROOM_ID = null;
                                    RequestInterface requestInterface = retrofit.create(RequestInterface.class);
                                    Call<Download_ordertable> calltable3 = requestInterface.getOrderTable(getActivateKeyFromDB(), table.getTableID());
                                    if (android.os.Build.VERSION.SDK_INT > 9) {
                                        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                        StrictMode.setThreadPolicy(policy);
                                    }
                                    try {
                                        Download_ordertable download_ordertable = calltable3.execute().body();
                                        invoiceee_id = download_ordertable.getOrder_id();
                                        Log.i("jflskdfjjslkdfj", download_ordertable.getOrder_id() + "");
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                    String invoice_id = invoiceee_id;
                                    Log.i("invoiceee_id", invoiceee_id + "");
                                    if (invoiceee_id.equals(null)) {
                                        CategoryActivity.ADD_INVOICE = "NULL";
//                                        CategoryActivity.VOUNCHER_ID = "NULL";
                                        CategoryActivity.VOUNCHER_ID = null;
                                    } else {
                                       // CategoryActivity.VOUNCHER_ID = "NULL";
                                        CategoryActivity.VOUNCHER_ID = null;
                                        if (invoice_id == null || invoice_id.equals("null")) {
                                            String group_invoice_id = getGroupTableInvoiceDataInDB(table.getTableID());
                                            //CategoryActivity.VOUNCHER_ID = "NULL";
                                            CategoryActivity.VOUNCHER_ID = null;
                                            if (group_invoice_id == null) {
                                                Log.e("GroupInvoice", group_invoice_id + "");
                                               // CategoryActivity.VOUNCHER_ID = "NULL";
                                                CategoryActivity.VOUNCHER_ID = null;
                                            } else {
                                                CategoryActivity.VOUNCHER_ID = group_invoice_id;
                                            }
                                        } else {
                                            CategoryActivity.VOUNCHER_ID = invoice_id;
                                            Log.i("CategoryActivity.VOUNCHER_IDfromtable", CategoryActivity.VOUNCHER_ID + "");
                                        }
                                        CategoryActivity.ADD_INVOICE = "EDITING_INVOICE";
                                    }
                                    CategoryActivity.check_check = "null";
                                    startActivity(new Intent(TableActivity.this, CategoryActivity.class));
                                    finish();
                                }
                            }

                            @Override
                            public void onFailure(Call<Success> call, Throwable t) {
                                Log.d("TableStatus", t.getMessage());
                                mProgressDialog.dismiss();
                                callUploadDialog("Please upload again!");
                            }
                        });
                    } else {
                        final JSONArray tableListJsonArray = new JSONArray();
                        JSONObject product = new JSONObject();
                        try {
                            product.put("booking_id", table.getBookingID() + "");
                            product.put("status", "1");
                            product.put("table_id", table.getTableID() + "");
                            product.put("old", "0");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        tableListJsonArray.put(product);
                        Log.e("TableList", tableListJsonArray.toString());
                        callDialog("Upload table data...");
                        mProgressDialog.dismiss();

                        RequestInterface request = retrofit.create(RequestInterface.class);
                        Call<Success> call = request.postTableStatus(tableListJsonArray.toString());
                        call.enqueue(new Callback<Success>() {
                            @Override
                            public void onResponse(Call<Success> call, Response<Success> response) {
                                try {
                                    Success jsonResponse = response.body();
                                    String message = jsonResponse.getMessage();
                                    if (message.equals("Success")) {

                                        Handler handler = new Handler();

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                socket.emit("table_message", "TakeTable");
                                                Toast.makeText(mContext, "SocketFire", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                        Log.d("TableStatus", message);
                                        mProgressDialog.dismiss();
                                        CategoryActivity.TABLE_ID = table.getTableID();
                                        ArrayList<String> tableNameList = new ArrayList<String>();
                                        tableNameList.add(table.getTableID());
                                        CategoryActivity.groupTableArrayList = tableNameList;
                                        CategoryActivity.TAKE_AWAY = "table";
                                        CategoryActivity.ROOM_ID = null;
                                        CategoryActivity.ADD_INVOICE = "NULL";
                                       // CategoryActivity.VOUNCHER_ID = "NULL";
                                        CategoryActivity.VOUNCHER_ID=null;
                                        startActivity(new Intent(TableActivity.this, CategoryActivity.class));
                                        finish();
                                    } else {

                                        Toast.makeText(TableActivity.this, "This TABLE is unavailable", Toast.LENGTH_SHORT).show();

                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    mProgressDialog.dismiss();
                                    callUploadDialog("Table status is null.");
                                }
                            }

                            @Override
                            public void onFailure(Call<Success> call, Throwable t) {
                                Log.d("TableStatus", t.getMessage());
                                mProgressDialog.dismiss();
                                callUploadDialog("Please upload again!");
                            }
                        });
                    }
                }
            });
            holder.backgroundLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Toast.makeText(TableActivity.this, "Onlong", Toast.LENGTH_LONG).show();
                    return false;
                }
            });
        }

        @Override
        public int getItemCount() {
            return tableList.size();
        }
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
            startActivity(new Intent(TableActivity.this, HomePageActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void deleteTableVersion(String tableName) {
        database.beginTransaction();
        database.execSQL("DELETE FROM " + tableName);
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void loadTableJson() {  // for table data from back end !
        callDialog("Download table data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseTable> call = request.getTable(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseTable>() {
            @Override
            public void onResponse(Call<JSONResponseTable> call, Response<JSONResponseTable> response) {
                try {
                    JSONResponseTable jsonResponse = response.body();
                    download_tableArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getTable()));
                    mProgressDialog.dismiss();
                    deleteTableVersion("tableList");
                    database.beginTransaction();
                    for (Download_Table download_table : download_tableArrayList) {
                        ContentValues cv = new ContentValues();
                        cv.put("id", download_table.getId());
                        cv.put("table_no", download_table.getTable_no());
                        cv.put("status", download_table.getStatus());
                        database.insert("tableList", null, cv);
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();

                    loadBookingJson();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Table data is null.");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseTable> call, Throwable t) {
                Log.d("ErrorTable", t.getMessage());
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
            }
        });
    }

    private void refreshTableJson() {
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseTable> call = request.getTable(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseTable>() {
            @Override
            public void onResponse(Call<JSONResponseTable> call, Response<JSONResponseTable> response) {
                try {
                    JSONResponseTable jsonResponse = response.body();
                    download_tableArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getTable()));
                    deleteTableVersion("tableList");
                    database.beginTransaction();
                    for (Download_Table download_table : download_tableArrayList) {
                        ContentValues cv = new ContentValues();
                        cv.put("id", download_table.getId());
                        cv.put("table_no", download_table.getTable_no());
                        cv.put("status", download_table.getStatus());
                        database.insert("tableList", null, cv);
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();

                    loadBookingJson();
                } catch (Exception e) {
                    e.printStackTrace();
                    callUploadDialog("Table data is null.");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseTable> call, Throwable t) {
                Log.d("ErrorTable", t.getMessage());
                callUploadDialog("Please upload again!");
            }
        });
    }

    private String getActivateKeyFromDB() {
        Cursor cur = null;
        String backend_activate_key = null;
        try {
            database.beginTransaction();
            cur = database.rawQuery("SELECT * FROM activate_key", null);
            while (cur.moveToNext()) {
                backend_activate_key = cur.getString(cur.getColumnIndex("backend_activation_key"));
            }
            cur.close();
            database.setTransactionSuccessful();
            database.endTransaction();
        } catch (Exception e) {
            callUploadDialog("DB error");
        } finally {
            cur.close();
        }
        return backend_activate_key;
    }

    private void loadBookingJson() {
        callDialog("Download booking data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseBooking> call = request.getBooking(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseBooking>() {
            @Override
            public void onResponse(Call<JSONResponseBooking> call, Response<JSONResponseBooking> response) {
                try {
                    JSONResponseBooking jsonResponse = response.body();
                    download_bookingArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getBooking()));
                    mProgressDialog.dismiss();
                    deleteTableVersion("booking");
                    deleteTableVersion("booking_table");
                    deleteTableVersion("booking_room");
                    database.beginTransaction();
                    for (Download_Booking download_booking : download_bookingArrayList) {
                        ContentValues cv = new ContentValues();
                        cv.put("id", download_booking.getId());
                        cv.put("customer_name", download_booking.getCustomer_name());
                        cv.put("from_time", download_booking.getFrom_time());
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
                    database.setTransactionSuccessful();
                    database.endTransaction();
                    tableView();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Booking data is null.");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseBooking> call, Throwable t) {
                Log.d("ErrorTable", t.getMessage());
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
            }
        });
    }

    private void callUploadDialog(final String message) {
        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) {
                    return;
                } else {
                    final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(TableActivity.this, R.style.InvitationDialog)
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
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(TableActivity.this, HomePageActivity.class));
        finish();
    }

    public void onStart() {
        super.onStart();
//        Thread background = new Thread(new Runnable() {
//
//            public void run() {
//                try {
//                    while (ContinueThread.get()) {
//                        Thread.sleep(30000);
//                        runOnUiThread(new Runnable() {
//                            @Override
//                            public void run() {
//                                refreshTableJson();
//                            }
//                        });
//                    }
//                } catch (InterruptedException e) {
//                    Thread.currentThread().interrupt();
//                }
//            }
//
//        });
//
//        ContinueThread.set(true);
//        background.start();
    }

    public void onStop() {
        super.onStop();
        ContinueThread.set(false);
    }
}
