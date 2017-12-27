package com.aceplus.rmsproject.rmsproject.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aceplus.rmsproject.rmsproject.CategoryActivity;
import com.aceplus.rmsproject.rmsproject.MainActivity;
import com.aceplus.rmsproject.rmsproject.R;
import com.aceplus.rmsproject.rmsproject.object.Download_OrderStatus;
import com.aceplus.rmsproject.rmsproject.object.Download_OrderStatusDetail;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseOrderStatus;
import com.aceplus.rmsproject.rmsproject.object.JsonTest;
import com.aceplus.rmsproject.rmsproject.object.Order_Complete;
import com.aceplus.rmsproject.rmsproject.object.Order_Item;
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
import java.util.ArrayList;
import java.util.Arrays;
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

/**
 * Created by kyawminlwin on 8/8/16.
 */
public class FragmentMessageComplete extends Fragment {
    public FragmentMessageComplete() {
        // Required empty public constructor
    }

    String WAITER_ID;
    //AtomicBoolean ContinueThread;
    private Retrofit retrofit;
    private ArrayList<Download_OrderStatus> download_orderStatusArrayList;
    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    SQLiteDatabase database;
    OrderItemAdapter orderItemAdapter;
    private ArrayList<Order_Complete> completeArrayList = new ArrayList<>();
    private ArrayList<Order_Complete> finalCompleteList = new ArrayList<>();
    private ArrayList<Order_Item> checkstatusArrayList = new ArrayList<>();
    Socket socket;
    Activity activity;

    Typeface font;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String mainurl = MainActivity.URL;
        String supmainturl = "";
        activity = this.getActivity();
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
        socket.on("cooking_done", onMessageCompete);
        socket.on("take", onMessageCompete);
        socket.connect();

        font = Typeface.createFromAsset(
                getActivity().getAssets(),
                "fonts/zawgyi.ttf");

    }

    private Emitter.Listener onMessageCompete = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    loadOrderStatusJson();
                }
            });
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.message_complete_fragment, container, false);
        try {
            database = new Database(getActivity()).getDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        //ContinueThread = new AtomicBoolean(false);
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
        loadOrderStatusJson();
        SharedPreferences prefs = getActivity().getSharedPreferences(MainActivity.LOGIN_PREFERENCES, getActivity().MODE_PRIVATE);
        WAITER_ID = prefs.getString(MainActivity.WAITER_ID, "No name defined");
        recyclerView = (RecyclerView) view.findViewById(R.id.card_recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        /*view.findViewById(R.id.buttonRefresh).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadOrderStatusJson();
            }
        });*/

        return view;
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

    @SuppressLint("LongLogTag")
    private void setDataInRecycler() {   // for view !!
        boolean canAdd = false;
        ArrayList<Order_Complete> ocompleteList = new ArrayList<>();
        for (Order_Complete ocomplete : completeArrayList) {
            Order_Complete com = new Order_Complete();
            String order_id = ocomplete.getOrder_id();
            com.setOrder_id(order_id);
            com.setTable_no(ocomplete.getTable_no());
            com.setRoom_name(ocomplete.getRoom_name());
            ArrayList<Order_Item> itemList = new ArrayList<>();
            for (Order_Item item : ocomplete.getOrder_item()) {
                Order_Item oitem = new Order_Item();
                String itemOrderID = item.getOrder_id();
                String status = item.getStatus();
                Log.i("statusss>>>>>>>>>>>", item.getStatus() + "");
                if (itemOrderID.equals(order_id) && status.equals("3")) {
                    oitem.setOrder_id(item.getOrder_id());
                    oitem.setOrder_detail_id(item.getOrder_detail_id());
                    oitem.setItem_name(item.getItem_name());
                    oitem.setSub_menu(item.getSub_menu());
                    oitem.setMessage(item.getMessage());
                    oitem.setOrder_type(item.getOrder_type());
                    oitem.setCooking_time(item.getCooking_time());
                    itemList.add(oitem);
                    canAdd = true;
                } else {
                    canAdd = false;
                }
            }
            com.setOrder_item(itemList);
            if (canAdd) {
                ocompleteList.add(com);
            }
        }
        Log.i("ocompleteList>>>>>>", ocompleteList.size() + "");
        finalCompleteList.clear();
        for (Order_Complete order_complete : ocompleteList) {
            Order_Complete complete = new Order_Complete();
            if (order_complete.getOrder_item().size() > 0) {
                Log.e("OrderItemLength", order_complete.getOrder_item().size() + "");
                complete.setOrder_id(order_complete.getOrder_id());
                complete.setTable_no(order_complete.getTable_no());
                complete.setRoom_name(order_complete.getRoom_name());
                complete.setOrder_item(order_complete.getOrder_item());
                finalCompleteList.add(complete);
                Log.i("for_showing_data.............", complete.getOrder_item() + "");
                Log.i("for_showing_data.............", complete.getOrder_id() + "");
                Log.i("for_showing_data.............", complete.getTable_no() + "");
                Log.i("for_showing_data.............", complete.getRoom_name() + "");
            }
        }
        adapter = new DataAdapter(completeArrayList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    public class DataAdapter extends RecyclerView.Adapter<DataAdapter.ViewHolder> {
        private ArrayList<Order_Complete> orderCompleteList;

        public DataAdapter(ArrayList<Order_Complete> orderCompleteList) {
            this.orderCompleteList = orderCompleteList;
        }

        @Override
        public DataAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_order_complete_cardview, viewGroup, false);
            return new ViewHolder(view);
        }

        private String getSetMenuId(String setMenuName) {
            String returnMenuID = "";
            Cursor cursor = database.rawQuery("SELECT * FROM setMenu WHERE set_menu_name = '" + setMenuName + "'", null);
            while (cursor.moveToNext()) {
                returnMenuID = cursor.getString(cursor.getColumnIndex("id"));
                Log.i("gettingSetID_for_take", returnMenuID + "");
            }
            return returnMenuID;
        }

        @Override
        public void onBindViewHolder(final DataAdapter.ViewHolder viewHolder, final int position) {
            final Order_Complete order_complete = orderCompleteList.get(position);
            viewHolder.vouncherTxt.setText(order_complete.getOrder_id());
//            if (order_complete.getTable_no() == null) {
//                Log.e("OComTableID", order_complete.getTable_no() + "");
//            } else {
//                viewHolder.tableTxt.setText(order_complete.getTable_no());
//            }
//            if (order_complete.getRoom_name() == null) {
//                Log.e("OComRoomID", order_complete.getRoom_name() + "");
//            } else {
//                viewHolder.tableTxt.setText(order_complete.getRoom_name());
//            }

            if (order_complete.getTable_no() != null) {

                viewHolder.tableTxt.setText(order_complete.getTable_no());

            } else if (order_complete.getRoom_name() != null) {

                viewHolder.tableTxt.setText(order_complete.getRoom_name());
                viewHolder.txtRoomTable.setText("Room No : ");

            } else {

                viewHolder.tableTxt.setText("Take Away");
                viewHolder.txtRoomTable.setText("");

            }

              viewHolder.listView.getLayoutParams().height = 66 * completeArrayList.get(position).getOrder_item().size();//52
            orderItemAdapter = new OrderItemAdapter(getActivity(), completeArrayList.get(position).getOrder_item());
            viewHolder.listView.setAdapter(orderItemAdapter);
            orderItemAdapter.notifyDataSetChanged();
            viewHolder.takeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int count = 0;
                    Log.e("OrderItemSize", completeArrayList.get(position).getOrder_item().size() + "");
                    JSONObject jsonObject = new JSONObject();
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonObject.put("waiter_id", WAITER_ID);
                        for (Order_Item order_item : completeArrayList.get(position).getOrder_item()) {
                            if (order_item.isCheck() == true) {
                                JSONObject orderDetailID = new JSONObject();
                                orderDetailID.put("detail_id", order_item.getId() + "");
                                if (order_item.getSub_menu() != null) {
                                    String gettingsetID = order_item.getSub_menu();
                                    String setMenuID = getSetMenuId(gettingsetID);
                                    orderDetailID.put("set_id", setMenuID);
                                    orderDetailID.put("set_item_id", order_item.getSet_item_id());
                                } else {
                                    orderDetailID.put("set_id", "null");
                                    orderDetailID.put("set_item_id", "null");
                                }
                                Log.i("Set_menu_Name_.........", order_item.getItem_name() + "");
                                Log.i("Set_menu_id_.........", order_item.getSet_item_id() + "");
                                jsonArray.put(orderDetailID);
                                count++;
                            }
                        }
                        jsonObject.put("order_detail_id", jsonArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.e("UploadingTake", jsonObject + "");
                    if (count > 0) {
                        final ProgressDialog mProgressDialog = new ProgressDialog(getActivity(), ProgressDialog.THEME_HOLO_LIGHT);
                        mProgressDialog.setIndeterminate(false);
                        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        mProgressDialog.setMessage("Uploading take data....");
                        mProgressDialog.show();
                        RequestInterface request = retrofit.create(RequestInterface.class);
                        Log.i("PostTake",jsonObject+"");
                        Call<Success> call = request.postTake(jsonObject + "");
                        call.enqueue(new Callback<Success>() {
                            @Override
                            public void onResponse(Call<Success> call, Response<Success> response) {
                                try {
                                    Success jsonResponse = response.body();
                                    String message = jsonResponse.getMessage();
                                    Log.d("TakeStatus", message + "size" + order_complete.getOrder_item().size());
                                    mProgressDialog.dismiss();
                                    if (message.equals("Success")) {

                                        Handler handler = new Handler();

                                        handler.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                socket.emit("taken_by", "Item Taken");
                                                Toast.makeText(activity, "SocketFire", Toast.LENGTH_SHORT).show();
                                            }
                                        });


                                        for (int i = 0; i < completeArrayList.get(position).getOrder_item().size(); i++) {
                                            Order_Item order_item = completeArrayList.get(position).getOrder_item().get(i);
                                            if (order_item.isCheck() == true) {
                                                String arg[] = {order_item.getOrder_detail_id()};
                                                Log.e("UpdateID", order_item.getOrder_detail_id() + "");
                                            }
                                        }
                                        loadOrderStatusJson();
                                        if (completeArrayList.get(position).getOrder_item().size() >= 0) {
                                            orderCompleteList.remove(position);
                                            notifyItemRemoved(position);
                                            adapter.notifyDataSetChanged();
                                        }
                                        mProgressDialog.dismiss();
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    mProgressDialog.dismiss();
                                }
                            }

                            @Override
                            public void onFailure(Call<Success> call, Throwable t) {
                                Log.d("RoomStatus", t.getMessage());
                                callUploadDialog("Please upload again!");
                            }
                        });
                    } else {
                        callUploadDialog("You haven't any carry!");
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return orderCompleteList.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            private TextView vouncherTxt;
            private TextView tableTxt;
            private Button takeBtn;
            private ListView listView;
            private TextView txtRoomTable;

            public ViewHolder(View view) {
                super(view);
                vouncherTxt = (TextView) view.findViewById(R.id.vouncher_txt);
                tableTxt = (TextView) view.findViewById(R.id.table_txt);
                takeBtn = (Button) view.findViewById(R.id.take_btn);
                listView = (ListView) view.findViewById(R.id.complete_list_view);
                txtRoomTable = (TextView) view.findViewById(R.id.table_view);
            }
        }
    }

    private class OrderItemAdapter extends ArrayAdapter<Order_Item> {
        public final Activity context;
        ArrayList<Order_Item> orderItemArrayList;

        public OrderItemAdapter(Activity context, ArrayList<Order_Item> orderItemArrayList) {
            super(context, R.layout.message_complete_list_view, orderItemArrayList);
            this.context = context;
            this.orderItemArrayList = orderItemArrayList;
        }

        private String getSetMenuItemName(String Set_Item_Name) {
            String SetItemReturn = "";
            Cursor cursor = database.rawQuery("SELECT * FROM Item WHERE id = '" + Set_Item_Name + "'", null);
            while (cursor.moveToNext()) {
                SetItemReturn = cursor.getString(cursor.getColumnIndex("name"));
            }
            return SetItemReturn;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            LayoutInflater layoutInflater = context.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.message_complete_list_view, null, true);
            final Order_Item order_item = orderItemArrayList.get(position);
            TextView productNameTxt = (TextView) view.findViewById(R.id.product_txt);
            TextView orderTypeTxt = (TextView) view.findViewById(R.id.order_type_txt);
            CheckBox statusCheck = (CheckBox) view.findViewById(R.id.status_check);

            productNameTxt.setTypeface(font);

            if (order_item.getItem_name() == null) {
                productNameTxt.setText(getSetMenuItemName(order_item.getSet_item_id()));
            } else {
                productNameTxt.setText(order_item.getItem_name());
            }
            Log.i("OrderType", order_item.getOrder_type());
            orderTypeTxt.setText(order_item.getOrder_type());
            statusCheck.setChecked(order_item.isCheck());
            statusCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                                                       @Override
                                                       public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                                           order_item.setCheck(isChecked);
                                                           orderItemAdapter.notifyDataSetChanged();
                                                       }
                                                   }
            );
            return view;
        }
    }

    private void loadOrderStatusJson() {   // geting data from back end !!
        final ProgressDialog mProgressDialog = new ProgressDialog(activity, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
        mProgressDialog.setMessage("Download order status data....");
        mProgressDialog.show();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseOrderStatus> call = request.getOrderStatus();
        call.enqueue(new Callback<JSONResponseOrderStatus>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JSONResponseOrderStatus> call, Response<JSONResponseOrderStatus> response) {
                try {
                    JSONResponseOrderStatus jsonResponse = response.body();
                    download_orderStatusArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getOrder_status()));
                    mProgressDialog.dismiss();
                    completeArrayList.clear();
                    Log.i("download_orderStatusArrayListdownload_orderStatusArrayList", download_orderStatusArrayList.size() + "");
                    for (Download_OrderStatus download_orderStatus : download_orderStatusArrayList) {
                        Order_Complete complete = new Order_Complete();
                        complete.setOrder_id(download_orderStatus.getVoucher_no());
                        complete.setRoom_name(download_orderStatus.getRoom_name());
                        complete.setTable_no(download_orderStatus.getTable_name());
                        ArrayList<Order_Item> order_itemArrayList = new ArrayList<Order_Item>();


                        for (Download_OrderStatusDetail orderDetail : download_orderStatus.getProduct_list()) {
                            Order_Item item = new Order_Item();
                            String canAddForComplete = null;
                            item.setOrder_id(orderDetail.getOrder_id());
                            item.setOrder_detail_id(orderDetail.getOrder_detail_id());
                            item.setItem_name(orderDetail.getItem_name());
                            item.setSub_menu(orderDetail.getSet_menus_name());
                            Log.i("Submen_name>>>hak>>>", orderDetail.getSet_menus_name() + "Whyyyy");
                            item.setMessage(orderDetail.getMessage());
                            item.setStatus(orderDetail.getStatus());
                            item.setCooking_time(orderDetail.getCooking_time());
                            item.setOrder_type(orderDetail.getOrder_type());
                            item.setId(orderDetail.getId());
                            item.setSet_item_id(orderDetail.getSet_item_id());

                            if (item.getStatus().equals("3") || item.getStatus() == "3") {
                                order_itemArrayList.add(item);
                            }
                            Log.i("item.getStatus()newwwww", canAddForComplete + "");
                            if (item.getStatus().equals("3") || item.getStatus() == "3") {
                                complete.setOrder_item(order_itemArrayList);
                                Log.i("completeArraylist>>>>>>>>>>>>", completeArrayList.size() + "");
                            }
                        }
                        if (complete.getOrder_item() != null) {
                            completeArrayList.add(complete);
                        }
                    }
                    setDataInRecycler();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("Exceptionex",e.getMessage()+"");
                    mProgressDialog.dismiss();
                    callUploadDialog("Kitchen complete is null.");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseOrderStatus> call, Throwable t) {
                Log.d("ErrorCategory", t.getMessage());
                mProgressDialog.dismiss();
                callUploadDialog("Please upload again!");
            }
        });
    }

    private void refreshOrderStatusJson() {   // refreashing data from back end between a time interval !!
        adapter.notifyDataSetChanged();
        RequestInterface request = retrofit.create(RequestInterface.class);
        Call<JSONResponseOrderStatus> call = request.getOrderStatus();
        call.enqueue(new Callback<JSONResponseOrderStatus>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(Call<JSONResponseOrderStatus> call, Response<JSONResponseOrderStatus> response) {
                try {
                    JSONResponseOrderStatus jsonResponse = response.body();
                    download_orderStatusArrayList = new ArrayList<>(Arrays.asList(jsonResponse.getOrder_status()));
                    Log.i("download_orderStatusArrayListdownload_orderStatusArrayList", download_orderStatusArrayList.size() + "");
                    completeArrayList.clear();
                    for (Download_OrderStatus download_orderStatus : download_orderStatusArrayList) {
                        Order_Complete complete = new Order_Complete();
                        complete.setOrder_id(download_orderStatus.getVoucher_no());
                        complete.setRoom_name(download_orderStatus.getRoom_name());
                        complete.setTable_no(download_orderStatus.getTable_name());
                        ArrayList<Order_Item> order_itemArrayList = new ArrayList<Order_Item>();
                        for (Download_OrderStatusDetail orderDetail : download_orderStatus.getProduct_list()) {
                            Order_Item item = new Order_Item();
                            String canAddForComplete = null;
                            item.setOrder_id(orderDetail.getOrder_id());
                            item.setOrder_detail_id(orderDetail.getOrder_detail_id());
                            item.setItem_name(orderDetail.getItem_name());
                            item.setSub_menu(orderDetail.getSet_menus_name());
                            Log.i("Submen_name>>>hak>>>", orderDetail.getSet_menus_name() + "Whyyyy");
                            item.setMessage(orderDetail.getMessage());
                            item.setStatus(orderDetail.getStatus());
                            item.setCooking_time(orderDetail.getCooking_time());
                            item.setOrder_type(orderDetail.getOrder_type());
                            if (item.getStatus().equals("3") || item.getStatus() == "3") {
                                order_itemArrayList.add(item);
                            }
                            canAddForComplete = item.getStatus();
                            Log.i("item.getStatus()refreash", canAddForComplete + "");
                            if (item.getStatus().equals("3") || item.getStatus() == "3") {
                                complete.setOrder_item(order_itemArrayList);
                            }
                        }
                        if (complete.getOrder_item() != null) {
                            completeArrayList.add(complete);
                        }
                        Log.i("completeArraylist2>>>>>>>>>>>>", completeArrayList.size() + "");
                    }
                    setDataInRecycler();
                } catch (Exception e) {
                    e.printStackTrace();
                    callUploadDialog("Kitchen complete is null.");
                }
            }

            @Override
            public void onFailure(Call<JSONResponseOrderStatus> call, Throwable t) {
                Log.d("ErrorCategory", t.getMessage());
                callUploadDialog("Please upload again!");
            }
        });
    }

    private void callUploadDialog(String message) {
        try {
            final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.InvitationDialog)
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {  // the tim interval !!!!
        super.onStart();
        /*Thread background = new Thread(new Runnable() {

            public void run() {
                try {
                    while (ContinueThread.get()) {
                        Thread.sleep(40000);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                loadOrderStatusJson();
                            }
                        });
                    }
                } catch (Throwable t) {
                }
            }

        });

        ContinueThread.set(true);
        background.start();*/
    }

    @Override
    public void onStop() {
        super.onStop();
        /*ContinueThread.set(false);*/
    }
}
