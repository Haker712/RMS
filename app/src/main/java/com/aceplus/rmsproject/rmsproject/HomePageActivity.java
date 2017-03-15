package com.aceplus.rmsproject.rmsproject;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.aceplus.rmsproject.rmsproject.object.Download_OrderStatus;
import com.aceplus.rmsproject.rmsproject.object.JSONResponseOrderStatus;
import com.aceplus.rmsproject.rmsproject.utils.Database;
import com.aceplus.rmsproject.rmsproject.utils.RequestInterface;

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
    private Retrofit retrofit;
    private ArrayList<Download_OrderStatus> download_orderStatusArrayList;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        try {
            database = new Database(HomePageActivity.this).getDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    private void registerIDs() {
        takeawayBtn = (Button) findViewById(R.id.take_away_btn);
        tableBtn = (Button) findViewById(R.id.table_btn);
        roomBtn = (Button) findViewById(R.id.room_btn);
        invoiceBtn = (Button) findViewById(R.id.invoice_btn);
        messageBtn = (Button) findViewById(R.id.message_btn);
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
            builder.show();
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
}
