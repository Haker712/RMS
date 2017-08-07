package com.aceplus.rmsproject.rmsproject;

import android.annotation.SuppressLint;
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
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aceplus.rmsproject.rmsproject.object.BRoom;
import com.aceplus.rmsproject.rmsproject.object.BTable;
import com.aceplus.rmsproject.rmsproject.object.BookingTable;
import com.aceplus.rmsproject.rmsproject.object.Download_Booking;
import com.aceplus.rmsproject.rmsproject.object.Download_Room;
import com.aceplus.rmsproject.rmsproject.object.Download_orderroom;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseBooking;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseRoom;
import com.aceplus.rmsproject.rmsproject.object.Room;
import com.aceplus.rmsproject.rmsproject.object.Success;
import com.aceplus.rmsproject.rmsproject.utils.Database;
import com.aceplus.rmsproject.rmsproject.utils.RequestInterface;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
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
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RoomActivity extends AppCompatActivity {


    // most of all are similar from table ! different only group methods

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private Button transferBtn;
    SQLiteDatabase database;
    private RoomAdapter adapter;
    ArrayList<String> roomName = new ArrayList<String>();
    ArrayList<String> fortransferroomName = new ArrayList<String>();
    ArrayList<BookingTable> bookingTableArrayList = new ArrayList<>();
    ArrayList<BookingTable> getRoomArrayList = new ArrayList<>();
    private ArrayList<Download_Room> download_roomArrayList;
    private ArrayList<Download_Booking> download_bookingArrayList;
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:SS");
    String fromRoom = null;
    String toRoom = null;
    Date fromTime;
    Date toTime;
    private int fromPos = 0;
    private int toPos = 0;
    private ProgressDialog mProgressDialog;
    private Retrofit retrofit;
    AtomicBoolean ContinueThread;
    String invoice_id = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        try {
            database = new Database(this).getDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        ContinueThread = new AtomicBoolean(false);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("X-Authorization", "25c512a9b6b76c778e321e35606016f10e95e74b").build();
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
        loadRoomJson();
        registerIDs();
        catchEvents();
    }

    private void callDialog(String message) {
        try {
            mProgressDialog = new ProgressDialog(RoomActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ArrayList<Room> getRoomData() {
        database.beginTransaction();
        ArrayList<Room> roomArrayList = new ArrayList<>();
        Cursor cur = database.rawQuery("SELECT * FROM room", null);
        while (cur.moveToNext()) {
            Room room = new Room();
            room.setId(cur.getString(cur.getColumnIndex("id")));
            room.setRoom_name(cur.getString(cur.getColumnIndex("room_name")));
            room.setStatus(cur.getString(cur.getColumnIndex("status")));
            roomArrayList.add(room);
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return roomArrayList;
    }

    private ArrayList<Room> gettransferRoomData() {
        database.beginTransaction();
        ArrayList<Room> roomArrayList = new ArrayList<>();
        Cursor cur = database.rawQuery("SELECT * FROM room WHERE status = '0'", null);
        while (cur.moveToNext()) {
            Room room = new Room();
            room.setId(cur.getString(cur.getColumnIndex("id")));
            room.setRoom_name(cur.getString(cur.getColumnIndex("room_name")));
            room.setStatus(cur.getString(cur.getColumnIndex("status")));
            roomArrayList.add(room);
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
        return roomArrayList;
    }

    private void deleteTableVersion(String tableName) {
        database.beginTransaction();
        database.execSQL("DELETE FROM " + tableName);
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void getRoomDataInDB() {
        database.beginTransaction();
        getRoomArrayList.clear();
        Cursor cur;
        Cursor curBooking = null;
        Cursor curBRoom = null;
        Cursor curConfig = null;
        cur = database.rawQuery("SELECT * FROM room", null);
        while (cur.moveToNext()) {
            BookingTable bookingTable = new BookingTable();
            String room_id = cur.getString(cur.getColumnIndex("id"));
            bookingTable.setTableID(room_id);
            bookingTable.setTable_no(cur.getString(cur.getColumnIndex("room_name")));
            bookingTable.setTableService(cur.getString(cur.getColumnIndex("status")));
            bookingTable.setTable_check(false);
            curBRoom = database.rawQuery("SELECT * FROM booking_room WHERE room_id = \"" + room_id + "\"", null);
            while (curBRoom.moveToNext()) {
                bookingTable.setTable_id(curBRoom.getString(curBRoom.getColumnIndex("room_id")));
                String booking_id = curBRoom.getString(curBRoom.getColumnIndex("booking_id"));
                Log.e("RoomBookingID", room_id + "" + booking_id);
                curBooking = database.rawQuery("SELECT * FROM booking WHERE id = \"" + booking_id + "\"", null);
                while (curBooking.moveToNext()) {
                    bookingTable.setBookingID(curBooking.getString(curBooking.getColumnIndex("id")));
                    bookingTable.setBooking_time(curBooking.getString(curBooking.getColumnIndex("from_time")));
                    Log.e("RoomBookingTime", curBooking.getString(curBooking.getColumnIndex("from_time")) + "");
                }
            }
            curConfig = database.rawQuery("SELECT * FROM config", null);
            while (curConfig.moveToNext()) {
                bookingTable.setBooking_waiting(curConfig.getString(curConfig.getColumnIndex("booking_waiting_time")));
                bookingTable.setBooking_service(curConfig.getString(curConfig.getColumnIndex("booking_service_time")));
                bookingTable.setBooking_warning(curConfig.getString(curConfig.getColumnIndex("booking_warning_time")));
            }
            getRoomArrayList.add(bookingTable);
        }
        cur.close();
        database.setTransactionSuccessful();
        database.endTransaction();
    }

    private void registerIDs() {
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        transferBtn = (Button) findViewById(R.id.room_transfer_btn);
    }

    private void callUploadDialog(String message) {
        final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(RoomActivity.this, R.style.InvitationDialog)
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

    private String getRoomInvoiceDataInDB(String room_id) {
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<Download_orderroom> call = requestInterface.getOrderRoom(getActivateKeyFromDB(), room_id);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            Download_orderroom download_orderroom = call.execute().body();
            invoice_id = download_orderroom.getOrder_id();
            Log.i("invoice_idinvoice_idinvoice_id", download_orderroom.getOrder_id() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoice_id;
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

    private void loadRoomJson() {
        callDialog("Download room data....");
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseRoom> call = request.getRoom(getActivateKeyFromDB());
        call.enqueue(new Callback<JSONResponseRoom>() {
            @Override
            public void onResponse(Call<JSONResponseRoom> call, Response<JSONResponseRoom> response) {
                try {
                    JSONResponseRoom jsonResponse = response.body();
                    download_roomArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getRoom()));
                    mProgressDialog.dismiss();
                    deleteTableVersion("room");
                    database.beginTransaction();
                    for (Download_Room download_room : download_roomArrayList) {
                        ContentValues cv = new ContentValues();
                        cv.put("id", download_room.getId());
                        cv.put("room_name", download_room.getRoom_name());
                        cv.put("status", download_room.getStatus());
                        database.insert("room", null, cv);
                    }
                    database.setTransactionSuccessful();
                    database.endTransaction();
                    loadBookingJson();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    callUploadDialog("Room data is null.");
                }
            }
            @Override
            public void onFailure(Call<JSONResponseRoom> call, Throwable t) {
                Log.d("ErrorRoom", t.getMessage());
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
            }
        });
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
                    roomView();
                } catch (Exception e) {
                    e.printStackTrace();
                    mProgressDialog.dismiss();
                    roomView();
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

    private void roomView() {
        getRoomDataInDB();
        bookingTableArrayList.clear();
        for (BookingTable bookingTable : getRoomArrayList) {
            BookingTable bookTable = new BookingTable();
            bookTable.setTableID(bookingTable.getTableID());
            bookTable.setTable_no(bookingTable.getTable_no());
            bookTable.setTable_check(bookingTable.isTable_check());
            bookTable.setTable_check(bookingTable.isTable_check());
            bookTable.setBookingID(bookingTable.getBookingID());
            bookTable.setTable_id(bookingTable.getTable_id());
            bookTable.setRoom_id(bookingTable.getRoom_id());
            bookTable.setBooking_time(bookingTable.getBooking_time());
            Log.e("RoomBookinTime", bookingTable.getBooking_time());
            bookTable.setBooking_service(bookingTable.getBooking_service());
            bookTable.setBooking_waiting(bookingTable.getBooking_waiting());
            bookTable.setBooking_warning(bookingTable.getBooking_warning());
            if (bookingTable.getTableService().equals("1")) {
                Log.e("Table_Service", "true");
                bookTable.setTableService(bookingTable.getTableService());
            } else if (bookingTable.getTableService().equals("0")) {
                Log.e("Table_Service", "false");
                if (bookingTable.getBooking_time().equals("00:00:00")) {
                    bookTable.setBackgroundColor("");
                    Log.e("booking_time", bookingTable.getBooking_time() + "");
                } else {
                    try {
                        Date booking_time = timeFormat.parse(bookingTable.getBooking_time());
                        Log.e("room_booking_time1", booking_time + "");
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
                        Log.e("room_booking_time2", booking_time + " " + fromTime + " " + toTime);
                        Date CurrentTime = timeFormat.parse(timeFormat.format(new Date()));
                        if (CurrentTime.equals(fromTime) || CurrentTime.after(fromTime) && CurrentTime.before(booking_time)) {
                            bookTable.setTableService("2");
                        } else if (CurrentTime.equals(booking_time) || CurrentTime.after(booking_time) && CurrentTime.before(toTime)) {
                            bookTable.setTableService("3");
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
            }
            bookingTableArrayList.add(bookTable);
        }
        adapter = new RoomAdapter(this, bookingTableArrayList);
        recyclerView.setAdapter(adapter);
    }

    private void catchEvents() {
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 5);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(5, dpToPx(10), true));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        transferBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog builder = new AlertDialog.Builder(RoomActivity.this, R.style.InvitationDialog)
                        .setPositiveButton(R.string.invitation_ok, null)
                        .setNegativeButton(R.string.invitation_cancel, null)
                        .create();
                LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View view = layoutInflater.inflate(R.layout.activity_table_transfer_dialog, null);
                roomName.clear();
                fortransferroomName.clear();
                for (Room room : getRoomData()) {
                    roomName.add(room.getRoom_name());
                }
                for (Room room : getRoomData()) {
                    if(room.getStatus().equals("0") || room.getStatus().equals(0)){
                        fortransferroomName.add(room.getRoom_name());
                    }

                }
                final Spinner from_spinner = (Spinner) view.findViewById(R.id.from_spinner);
                final Spinner to_spinner = (Spinner) view.findViewById(R.id.to_spinner);
                ArrayAdapter<String> stringAdapter = new ArrayAdapter<String>(RoomActivity.this, R.layout.spinner_text, roomName);
                stringAdapter.setDropDownViewResource(R.layout.spinner_dropdown_textview);
                from_spinner.setAdapter(stringAdapter);
                from_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        fromRoom = getRoomData().get(position).getId();
                        fromPos = position;
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });
                ArrayAdapter<String> toArrayAdapter = new ArrayAdapter<String>(RoomActivity.this, R.layout.spinner_text, fortransferroomName);
                toArrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown_textview);
                to_spinner.setAdapter(toArrayAdapter);
                to_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        toRoom = gettransferRoomData().get(position).getId();
                        toPos = Integer.parseInt(toRoom);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // TODO Auto-generated method stub
                    }
                });
                builder.setView(view);
                builder.setTitle(R.string.room_transfer);
                builder.setOnShowListener(new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface dialog) {
                        final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                        btnAccept.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String arg[] = {fromRoom};
                                ContentValues cv = new ContentValues();
                                cv.put("room_id", toRoom);
                                bookingTableArrayList.get(fromPos).setTableService("0");
                                bookingTableArrayList.get(toPos-1).setTableService("1");
                                adapter.notifyDataSetChanged();
                                JSONObject jsonObject = new JSONObject();
                                try {
                                    jsonObject.put("transfer_from_room_id", fromRoom);
                                    jsonObject.put("transfer_to_room_id", toRoom);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                Log.e("TransferRoom", jsonObject.toString());
                                String transferInvoice;
                                transferInvoice = getRoomInvoiceDetail(fromRoom);
                                callDialog("Uploading room transfer....");
                                RequestInterface request = retrofit.create(RequestInterface.class);
                                Call<Success> call = request.postRoomTransfer(transferInvoice, fromRoom, toRoom);
                                Log.i("transferInvoice", transferInvoice + "");
                                Log.i("fromRoom", fromRoom + "");
                                Log.i("toRoom", toRoom + "");
                                call.enqueue(new Callback<Success>() {
                                    @Override
                                    public void onResponse(Call<Success> call, Response<Success> response) {
                                        try {
                                            Success jsonResponse = response.body();
                                            String message = jsonResponse.getMessage();
                                            if (message.equals("Success")) {
                                                Log.d("RoomTransfer", message);
                                                mProgressDialog.dismiss();
                                                builder.dismiss();
                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                            mProgressDialog.dismiss();
                                            callUploadDialog("Room transfer is null.");
                                        }
                                    }
                                    @Override
                                    public void onFailure(Call<Success> call, Throwable t) {
                                        Log.d("RoomTransfer", t.getMessage());
                                        mProgressDialog.dismiss();
                                        callUploadDialog("Please upload again!");
                                    }
                                });
                            }
                        });
                        final Button btnDecline = builder.getButton(DialogInterface.BUTTON_NEGATIVE);
                        btnDecline.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(RoomActivity.this, "Cancel", Toast.LENGTH_LONG).show();
                                builder.dismiss();
                            }
                        });
                    }
                });
                builder.show();
            }
        });

    }

    private String getRoomInvoiceDetail(String room_id) {
        String invoiceeec_id = null;
        RequestInterface requestInterface = retrofit.create(RequestInterface.class);
        Call<Download_orderroom> calltable3 = requestInterface.getOrderRoom(getActivateKeyFromDB(), room_id);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            Download_orderroom download_orderroom = calltable3.execute().body();
            invoiceeec_id = download_orderroom.getOrder_id();
            Log.i("jflskdfjjslkdfj22222", download_orderroom.getOrder_id() + "");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return invoiceeec_id;
    }

    public class RoomAdapter extends RecyclerView.Adapter<RoomAdapter.MyViewHolder> {
        private Context mContext;
        private List<BookingTable> roomList;
        String checkinvoiceid;
        public RoomAdapter(Context mContext, List<BookingTable> roomList) {
            this.mContext = mContext;
            this.roomList = roomList;
        }
        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.room_cardview, parent, false);
            return new MyViewHolder(itemView);
        }
        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView tableTxt;
            public RelativeLayout backgroundLayout;
            public MyViewHolder(View view) {
                super(view);
                tableTxt = (TextView) view.findViewById(R.id.table_txt);
                backgroundLayout = (RelativeLayout) view.findViewById(R.id.background_layout);
            }
        }
        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position) {
            final BookingTable table = roomList.get(position);
            holder.tableTxt.setText(table.getTable_no());
            if (table.getTableService().equals("2")) {
                holder.backgroundLayout.setBackgroundColor(Color.parseColor("#f7941d"));
            } else if (table.getTableService().equals("3")) {
                holder.backgroundLayout.setBackgroundColor(Color.parseColor("#ed1c24"));
            } else if (table.getTableService().equals("1")) {
                holder.backgroundLayout.setBackgroundColor(Color.parseColor("#00bff3"));
            } else {
                holder.backgroundLayout.setBackgroundColor(Color.parseColor("#8dc63f"));
            }
            checkinvoiceid = table.getTableService();
            holder.backgroundLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //if (table.getTableService().equals("0")) {
                        final String room_id = table.getTableID();
                        final String status = table.getTableService();
                        JSONObject jsonObject = new JSONObject();
                        try {
                            jsonObject.put("room_id", room_id + "");
                            jsonObject.put("status", "1");
                            String booking_id = table.getBookingID();
                            if (booking_id == null) {
                                jsonObject.put("booking_id", "null");
                            } else {
                                jsonObject.put("booking_id", table.getBookingID());
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("RoomStatusJson", jsonObject + "");
                        callDialog("Uploading room status....");
                        RequestInterface request = retrofit.create(RequestInterface.class);
                        Call<Success> call = request.postRoomStatus(jsonObject + "");
                        call.enqueue(new Callback<Success>() {
                            @SuppressLint("LongLogTag")
                            @Override
                            public void onResponse(Call<Success> call, Response<Success> response) {
                                try {
                                    Success jsonResponse = response.body();
                                    String message = jsonResponse.getMessage();
                                    if (message.equals("Success")) {
                                        Log.d("RoomStatus", message);
                                        mProgressDialog.dismiss();
                                        CategoryActivity.ROOM_ID = room_id;
                                        CategoryActivity.TAKE_AWAY = "room";
                                        CategoryActivity.groupTableArrayList = null;
                                        CategoryActivity.TABLE_ID = null;
                                        String vouncherid = getRoomInvoiceDataInDB(room_id);
                                        CategoryActivity.VOUNCHER_ID = vouncherid;
                                        Log.e("RoomVouncherID", vouncherid + "");
                                        String invoiceeece_id;
                                        invoiceeece_id = getRoomInvoiceDetail(room_id);
                                        String invoice_id = null;
                                        invoice_id = invoiceeece_id;
                                        CategoryActivity.VOUNCHER_ID = invoice_id;
                                        Log.i("CategoryActivity.vouncherIDfromRoom", CategoryActivity.VOUNCHER_ID + "");

                                        if (invoice_id.equals("NULL") || invoice_id.equals(null)) {
                                            if (table.getTableService().equals("1")) {
                                                CategoryActivity.ADD_INVOICE = "status1";
                                            } else {
                                                CategoryActivity.ADD_INVOICE = "NULL";
                                            }

                                            //
                                            CategoryActivity.VOUNCHER_ID = "NULL";
                                        } else {
                                            CategoryActivity.ADD_INVOICE = "EDITING_INVOICE";
                                        }
                                        startActivity(new Intent(RoomActivity.this, CategoryActivity.class));
                                        finish();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    mProgressDialog.dismiss();
                                    callUploadDialog("Room status is null.");
                                }
                            }

                            @Override
                            public void onFailure(Call<Success> call, Throwable t) {
                                Log.d("RoomStatus", t.getMessage());
                                mProgressDialog.dismiss();
                                callUploadDialog("Please upload again!");
                            }
                        });
                    }
                //}

            });
        }
        @Override
        public int getItemCount() {
            return roomList.size();
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
            startActivity(new Intent(RoomActivity.this, HomePageActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    public void onStart() {
        super.onStart();
        Thread background = new Thread(new Runnable() {

            public void run() {
                try {
                    while (ContinueThread.get()) {
                        Thread.sleep(30000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadRoomJson();
                            }
                        });
                    }
                } catch (Throwable t) {
                }
            }

        });
        ContinueThread.set(true);
        background.start();
    }

    public void onStop() {
        super.onStop();
        ContinueThread.set(false);
    }
}
