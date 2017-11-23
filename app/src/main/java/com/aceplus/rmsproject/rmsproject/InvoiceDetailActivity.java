package com.aceplus.rmsproject.rmsproject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.aceplus.rmsproject.rmsproject.object.InvoiceDetailProduct;
import com.aceplus.rmsproject.rmsproject.object.InvoiceDetailProductSetItem;
import com.aceplus.rmsproject.rmsproject.object.JsonTest;
import com.aceplus.rmsproject.rmsproject.object.SetMenu_Item_for_dialog;
import com.aceplus.rmsproject.rmsproject.object.Success;
import com.aceplus.rmsproject.rmsproject.utils.Database;
import com.aceplus.rmsproject.rmsproject.utils.Download_forShow_roomID;
import com.aceplus.rmsproject.rmsproject.utils.Download_forShow_tableID;
import com.aceplus.rmsproject.rmsproject.utils.JsonForShowRoomId;
import com.aceplus.rmsproject.rmsproject.utils.JsonForShowTableId;
import com.aceplus.rmsproject.rmsproject.utils.RequestInterface;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URISyntaxException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.ButterKnife;
import butterknife.InjectView;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.graphics.Paint.STRIKE_THRU_TEXT_FLAG;

/**
 * Created by kyawminlwin on 8/4/16.
 */
public class InvoiceDetailActivity extends ActionBarActivity {

    private Toolbar toolbar;
    SQLiteDatabase database;
    public /*static*/ String vouncherID = null;
    public /*static*/ String userID = null;
    public String userName=null;
    public /*static*/ String date = null;
    public /*static*/ String tableNo = null;
    public /*static*/ String RommCharge = null;
    public /*static*/ String TABLE_ID = null;
    public /*static*/ String ROOM_ID = null;
    public /*static*/ String totalAmount = null;
    public /*static*/ String totalExtra = null;
    public /*static*/ String totalDiscount = null;
    public /*static*/ String netAmount = null;
    private double taxAmt = 0.0;
    private double serviceAmt = 0.0;
    private double roomchargeAmt = 0.0;
    private double totalroomchargeAmt = 0.0;
    private double discount = 0.0;
    private String memberID = null;
    DecimalFormat commaSepFormat = new DecimalFormat("###,##0");
    private ProgressDialog mProgressDialog;
    InvoiceDetailAdapter invoiceDetailAdapter;
    private Retrofit retrofit;
    private TextView waiterIDTxt;
    private TextView waiterNameTxt;
    private TextView dateTxt;
    private TextView vouncherTxt;
    private TextView tableNoTxt;
    private EditText memberCardEdit;
    private TextView totalAmtTxt;
    private TextView totalExtraTxt;
    private TextView totalDisTxt;
    private TextView totalMemberDisTxt;
    private TextView taxTxt;
    private TextView taxpercentTxt;
    private TextView serviceTxt;
    private TextView servicepercentTxt;
    private TextView netAmtTxt;
    private EditText payAmtEdit;
    @InjectView(R.id.FOC_amount_edit)
    EditText focAmtEdit;
    private EditText focdescription;
    private TextView totaldiscamounttxt;
    private TextView refundTxt;
    private ListView productListView;
    private Button addBtn;
    private Button paidBtn;
    private Button printBtn;
    double FOC_Amount = 0.0;
    double old_totalAmt = 0.0;
    double totalAmt_calc = 0.0;
    double FOC_toatalamount = 0.0;
    double FOC_tax;
    double FOC_Service;
    String set_MenuID = "";
    private /*static*/ ArrayList<SetMenu_Item_for_dialog> setMenu_item_name = new ArrayList<>();
    private ArrayList<Download_forShow_tableID> download_forShow_tableIDs;
    private ListView SetItemListView;
    SetItemDetailAdapter setItemdetailadapter;
    ArrayList<InvoiceDetailProduct> arrayList = new ArrayList<>();
    private ArrayList<Download_forShow_tableID> download_orderTableArrayList = new ArrayList<>();
    private ArrayList<Download_forShow_roomID> download_orderRoomArrayList = new ArrayList<>();
    Boolean paidavailable = true;

    /***
     * PhoneLinAung 19.9.17 Start
     */

    ArrayList<InvoiceDetailProduct> invoiceDetailProductArrayList = new ArrayList<>();
    Map<String, String> detailDataMap = new HashMap<>();

    Socket socket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_detail_test);
        ButterKnife.inject(this);
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        Interceptor interceptor = new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request newRequest = chain.request().newBuilder().addHeader("X-Authorization", getActivateKeyFromDB()).build();
                return chain.proceed(newRequest);

            }
        };
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
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            database = new Database(this).getDataBase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        registerIDs();
        mProgressDialog = new ProgressDialog(InvoiceDetailActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        getArrayIntent();
        setDetailDataFromMap();
        getConfigData();
        setAdapter();
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

        socket.connect();

    }

    /***
     * PhoneLinAung 19.9.17 Start
     */

    private void getArrayIntent() {

        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        invoiceDetailProductArrayList = (ArrayList<InvoiceDetailProduct>) args.getSerializable("ARRAYLIST");
        detailDataMap = (Map<String, String>) args.getSerializable("Map");
    }

    private void setDetailDataFromMap() {


        vouncherID = detailDataMap.get("vouncherID");
        userID = detailDataMap.get("userId");
        userName=detailDataMap.get("userName");
        date = detailDataMap.get("date");
        tableNo = detailDataMap.get("tableNo");
        if(tableNo.equals("")) {
            tableNo = "TAKE AWAY";
        }
        RommCharge = detailDataMap.get("RommCharge");
        TABLE_ID = detailDataMap.get("TABLE_ID");
        ROOM_ID = detailDataMap.get("ROOM_ID");
        totalAmount = detailDataMap.get("totalAmount");
        totalExtra = detailDataMap.get("totalExtra");
        totalDiscount = detailDataMap.get("totalDiscount");
        netAmount = detailDataMap.get("netAmount");


    }


    // This is where get data from invoice.activity
    @SuppressLint("LongLogTag")
    private void setAdapter() {
        invoiceDetailAdapter = new InvoiceDetailAdapter(InvoiceDetailActivity.this, invoiceDetailProductArrayList);
        productListView = (ListView) findViewById(R.id.list_view);
        productListView.setAdapter(invoiceDetailAdapter);
        invoiceDetailAdapter.notifyDataSetChanged();

    }

    private void registerIDs() {
        waiterIDTxt = (TextView) findViewById(R.id.staffid_txt);
        waiterNameTxt= (TextView) findViewById(R.id.staffname_txt);
        dateTxt = (TextView) findViewById(R.id.date_txt);
        vouncherTxt = (TextView) findViewById(R.id.vouncher_txt);
        tableNoTxt = (TextView) findViewById(R.id.table_txt);
        memberCardEdit = (EditText) findViewById(R.id.member_card_edit);
        payAmtEdit = (EditText) findViewById(R.id.pay_amount_edit);
        payAmtEdit.requestFocus();
        focdescription = (EditText) findViewById(R.id.remark_of_FOC_edit);
        totaldiscamounttxt = (TextView) findViewById(R.id.total_discount_amount_txt);
        totalAmtTxt = (TextView) findViewById(R.id.total_amount_txt);
        //totalExtraTxt = (TextView) findViewById(R.id.total_extra_amount_txt);
        //totalDisTxt = (TextView) findViewById(R.id.total_discount_amount_txt);
        totalMemberDisTxt = (TextView) findViewById(R.id.member_discount_txt);
        taxTxt = (TextView) findViewById(R.id.tax_txt);
        taxpercentTxt= (TextView) findViewById(R.id.tax_percent_txt);
        serviceTxt = (TextView) findViewById(R.id.service_txt);
        servicepercentTxt= (TextView) findViewById(R.id.service_percent_txt);
        netAmtTxt = (TextView) findViewById(R.id.net_amount_txt);
        refundTxt = (TextView) findViewById(R.id.refund_txt);
        addBtn = (Button) findViewById(R.id.add_btn);
        paidBtn = (Button) findViewById(R.id.paid_btn);
        //  printBtn = (Button) findViewById(R.id.print_btn);
    }

    private void callUploadDialog(String message) {
        try {
            final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(InvoiceDetailActivity.this, R.style.InvitationDialog)
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

    public void getConfigData() {
        try {

            Cursor cur = database.rawQuery("SELECT * FROM config", null);
            while (cur.moveToNext()) {
                taxAmt = cur.getDouble(cur.getColumnIndex("tax"));
                serviceAmt = cur.getDouble(cur.getColumnIndex("service"));
                roomchargeAmt = cur.getDouble(cur.getColumnIndex("room_charge"));
            }
            cur.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Double getMemberDiscountInDB(String member_card_no) {
        Log.e("MemberCardNo", member_card_no);
        Cursor cur = database.rawQuery("SELECT * FROM member WHERE member_card_no = \"" + member_card_no + "\"", null);
        double discountAmt = 0.0;
        while (cur.moveToNext()) {
            discountAmt = cur.getDouble(cur.getColumnIndex("discount"));
            memberID = cur.getString(cur.getColumnIndex("id"));
        }
        return discountAmt;
    }

    private void catchEvents() {

        vouncherTxt.setText(vouncherID);
        waiterIDTxt.setText(userID);
        waiterNameTxt.setText(userName);
        dateTxt.setText(date);
        tableNoTxt.setText(tableNo);
        totalAmtTxt.setText(totalAmount);
        //totalExtraTxt.setText(totalExtra);
        //Log.i("ExtraPrice>>>>", totalExtraTxt.getText() + "");
        //totalDisTxt.setText(totalDiscount);
        netAmtTxt.setText(netAmount);
        double total = Double.parseDouble(totalAmount);
        double taxValue = total * taxAmt / 100;
        taxTxt.setText(commaSepFormat.format(taxValue));
        taxpercentTxt.setText(taxAmt+"%");
        Log.d("TaxValue", taxAmt + "%");
        double serviceValue = 0;
        if (!RommCharge.equals("")) {
            serviceValue = (total * serviceAmt / 100) + roomchargeAmt;
        } else {
            serviceValue = total * serviceAmt / 100;
        }
        serviceTxt.setText(commaSepFormat.format(serviceValue));
        servicepercentTxt.setText(serviceAmt+"%");
        Log.d("Service", serviceValue + "%");
        focAmtEdit.setEnabled(false);
        payAmtEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                double refund;
                String payAmount = payAmtEdit.getText().toString().trim();
                if (!payAmount.equals("")) {
                    double pay_Amount = Double.parseDouble(payAmount);
                    double netAmt = Double.parseDouble(netAmtTxt.getText().toString().trim().replaceAll(",", ""));
                    if (pay_Amount > netAmt) {
                        refund = pay_Amount - netAmt;
                        refundTxt.setText(commaSepFormat.format(refund));
                    } else {
                        refundTxt.setText("0");
                    }
                }
                focAmtEdit.setEnabled(true);
            }
        });
        memberCardEdit.addTextChangedListener(new TextWatcher() {    // for  when enter member ID !!
            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                String member_card_no = memberCardEdit.getText().toString().trim();
                if (!member_card_no.equals("")) {
                    double discountAmt = getMemberDiscountInDB(member_card_no);
                    Log.e("MemberCard", member_card_no);
                    double totalAmt = Double.parseDouble(totalAmtTxt.getText().toString().trim().replaceAll(",", ""));
                    double mDiscount = totalAmt * discountAmt / 100;
                    Log.d("Discount", discountAmt + "%");
                    totalMemberDisTxt.setText(commaSepFormat.format(mDiscount));
                    double memberDiscount = Double.parseDouble(totalMemberDisTxt.getText().toString().trim().replaceAll(",", ""));
                    Log.d("MemberDiscount", memberDiscount + "%");
                    double netAmt = Double.parseDouble(netAmtTxt.getText().toString().trim().replaceAll(",", ""));
                    Log.d("NetAmount", netAmt + "");
                    double totalValue = netAmt - memberDiscount;
                    netAmtTxt.setText(commaSepFormat.format(totalValue));
                } else {
                    totalMemberDisTxt.setText("0");
                    netAmtTxt.setText(netAmount);
                }
            }
        });
        focAmtEdit.addTextChangedListener(new TextWatcher() {   // when  enter foc amount !
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String focAmount = String.valueOf(s);
                old_totalAmt = Double.parseDouble(totalAmount.trim().replaceAll(",", ""));
                totalAmt_calc = Double.parseDouble(totalAmtTxt.getText().toString().trim().replaceAll(",", ""));
                double pay_amount = Double.parseDouble(payAmtEdit.getText().toString().trim().replaceAll(",", ""));
                if (!focAmtEdit.getText().toString().equals("")) {
                    FOC_Amount = Double.parseDouble(focAmtEdit.getText().toString());
                    totalAmt_calc = old_totalAmt;
                    FOC_toatalamount = totalAmt_calc - FOC_Amount;
                    setTotalAmt(FOC_toatalamount);
                    double FOC_tax_amount = FOC_toatalamount * 0.1;
                    taxTxt.setText(commaSepFormat.format(FOC_tax_amount));
                    double FOC_Service_amount = FOC_toatalamount * 0.1;
                    serviceTxt.setText(commaSepFormat.format(FOC_Service_amount));
                    double FOC_Net_amount = FOC_toatalamount + (FOC_tax_amount + FOC_Service_amount);
                    netAmtTxt.setText(commaSepFormat.format(FOC_Net_amount));
                    double foc_refund = pay_amount - FOC_Net_amount;
                    refundTxt.setText(commaSepFormat.format(foc_refund));
                } else {
                    FOC_Amount = 0.0;
                    totalAmtTxt.setText(totalAmount);
                    netAmtTxt.setText(netAmount);
                    double no_foc_netamount = Double.parseDouble(netAmount.trim().replaceAll(",", ""));
                    double no_foc_refund = pay_amount - no_foc_netamount;
                    refundTxt.setText(commaSepFormat.format(no_foc_refund));
                }
                Log.i("FOC_Amount>>>", FOC_Amount + "");
                Log.i("FOC_toatalamount>>>", FOC_toatalamount + "");
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

            addBtn.setOnClickListener(new View.OnClickListener()

                                      {
                                          @SuppressLint("LongLogTag")
                                          @Override
                                          public void onClick(View v) {   // add btn which lead to category activity !
                                              CategoryActivity.VOUNCHER_ID = vouncherID;
                                              CategoryActivity.TAKE_AWAY = "add_invoice";
                                              CategoryActivity.ADD_INVOICE = "EDITING_INVOICE";
                                              Log.i("CategoryActivity.VOUNCHER_ID", CategoryActivity.VOUNCHER_ID + "");
                                              startActivity(new Intent(InvoiceDetailActivity.this, CategoryActivity.class));
                                              finish();
                                          }
                                      }
            );

//        printBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                       /* PrintActivity.print_datetime = null;
//                        PrintActivity.print_voucherID = null;
//                        PrintActivity.print_tableNo = null;
//                        PrintActivity.print_staffName = null;
//                        PrintActivity.print_tax = null;
//                        PrintActivity.print_service = null;
//                        PrintActivity.print_totalAmount = null;
//                        PrintActivity.print_netAmount = null;*/
////here!!
//
//
//                Intent intent = new Intent(InvoiceDetailActivity.this, PrintActivity.class);
//                intent.putExtra("voucherID", vouncherID);
//                intent.putExtra("datetime", date);
//                intent.putExtra("tableNo", tableNo);
//                intent.putExtra("staffName", userID);
//                intent.putExtra("totalAmt", totalAmount);
//                intent.putExtra("netAmt", netAmount);
//                intent.putExtra("taxAmt", taxTxt.getText().toString().trim().replace(",", ""));
//                intent.putExtra("serviceAmt", serviceTxt.getText().toString().trim().replace(",", ""));
//                //intent.putCharSequenceArrayListExtra("json", );
//
//                startActivity(intent);
//            }
//        });

        paidBtn.setOnClickListener(new View.OnClickListener()

                                   {
                                       @Override
                                       public void onClick(View v) {   // paid button ! uploading data if get paid !

                                           if (paidavailable == false) {
                                               Toast.makeText(InvoiceDetailActivity.this, "Can't pay right now!", Toast.LENGTH_SHORT).show();
                                           } else if (paidavailable == true) {
                                               if (netAmtTxt.getText().toString().length() == 0) {
                                                   netAmtTxt.setText("0");
                                               }
                                               double total = Double.parseDouble(netAmtTxt.getText().toString().trim().replaceAll(",", ""));
                                               double payAmt = 0.0;
                                               String payAmount = payAmtEdit.getText().toString();
                                               if (payAmount.equals("")) {
                                                   payAmt = 0;
                                               } else {
                                                   payAmt = Double.parseDouble(payAmount);
                                               }
                                               if (payAmt >= total) {
                                                   TABLE_ID = gettableIDD(vouncherID);
                                                   if (TABLE_ID == null) {
                                                       Log.e("TableID", TABLE_ID + "");
                                                   } else {
                                                       JSONArray tableListJsonArray = new JSONArray();
                                                       for (String tableName : getGroupTableListInDB(vouncherID)) {
                                                           JSONObject jsonObject = new JSONObject();
                                                           try {
                                                               jsonObject.put("table_id", tableName);
                                                               jsonObject.put("status", "0");
                                                               jsonObject.put("booking_id", "null");
                                                           } catch (JSONException e) {
                                                               e.printStackTrace();
                                                           }
                                                           tableListJsonArray.put(jsonObject);
                                                       }
                                                       Log.e("InvoiceTableStatus", tableListJsonArray.toString());
                                                       RequestInterface request = retrofit.create(RequestInterface.class);
                                                       Call<Success> call = request.postTableStatus(tableListJsonArray + "");
                                                       call.enqueue(new Callback<Success>() {
                                                           @Override
                                                           public void onResponse(Call<Success> call, Response<Success> response) {
                                                               try {
                                                                   Success jsonResponse = response.body();
                                                                   String message = jsonResponse.getMessage();
                                                                   if (message.equals("Success")) {
                                                                       Log.d("TableStatus", message);
                                                                       String arg[] = {vouncherID};
                                                                   }
                                                               } catch (Exception e) {
                                                                   e.printStackTrace();
                                                                   //callUploadDialog("Problem in table status!");
                                                                   Log.i("Problem in table status!", "Problem in table status!");
                                                               }
                                                           }

                                                           @Override
                                                           public void onFailure(Call<Success> call, Throwable t) {
                                                               Log.d("TableStatus", t.getMessage());
                                                               callUploadDialog("Please upload again!");
                                                           }
                                                       });
                                                       Log.e("TableID", TABLE_ID + "");
                                                   }
                                                   ROOM_ID = getroomIDD(vouncherID);
                                                   if (ROOM_ID == null) {
                                                       Log.e("RoomID", ROOM_ID + "");
                                                   } else {
                                                       JSONObject jsonObject = new JSONObject();
                                                       try {
                                                           jsonObject.put("room_id", ROOM_ID + "");
                                                           jsonObject.put("status", "0");
                                                           jsonObject.put("booking_id", "null");
                                                       } catch (JSONException e) {
                                                           e.printStackTrace();
                                                       }
                                                       Log.e("CategoryRoomStatus", jsonObject.toString());
                                                       RequestInterface request = retrofit.create(RequestInterface.class);
                                                       Call<Success> call = request.postRoomStatus(jsonObject + "");
                                                       call.enqueue(new Callback<Success>() {
                                                           @Override
                                                           public void onResponse(Call<Success> call, Response<Success> response) {
                                                               try {
                                                                   Success jsonResponse = response.body();
                                                                   String message = jsonResponse.getMessage();
                                                                   if (message.equals("Success")) {
                                                                       Log.d("RoomStatus", message);
                                                                   }
                                                               } catch (Exception e) {
                                                                   e.printStackTrace();
                                                                   //callUploadDialog("Problem in room status.");
                                                                   Log.i("Problem in room status.", "Problem in room status.");
                                                               }
                                                           }

                                                           @Override
                                                           public void onFailure(Call<Success> call, Throwable t) {
                                                               Log.d("RoomStatus", t.getMessage());
                                                               mProgressDialog.dismiss();
                                                               callUploadDialog("Please upload again!");
                                                           }
                                                       });
                                                       Log.e("RoomID", ROOM_ID + "");
                                                   }
                                                   Log.e("PayAmt", payAmt + "");
                                                   mProgressDialog.setMessage("Uploading paid data....");
                                                   mProgressDialog.show();
                                                   Log.e("UploadVoucherID", vouncherID);
                                                   Log.e("UploadMemberID", memberID + "");
                                                   RequestInterface request = retrofit.create(RequestInterface.class);
                                                   Call<Success> call;
                                                   double pay_amount = Double.parseDouble(payAmtEdit.getText().toString().trim().replaceAll(",", ""));
                                                   double refund = Double.parseDouble(refundTxt.getText().toString().trim().replaceAll(",", ""));
                                                   double tax_amount = Double.parseDouble(taxTxt.getText().toString().trim().replace(",", ""));
                                                   double service_amount = Double.parseDouble(serviceTxt.getText().toString().trim().replace(",", ""));
                                                   double total_amount = Double.parseDouble(totalAmtTxt.getText().toString().trim().replace(",", ""));
                                                   double all_total_amount = Double.parseDouble(netAmtTxt.getText().toString().trim().replace(",", ""));
                                                   double dicount_amount = Double.parseDouble(totaldiscamounttxt.getText().toString().trim().replace(",", ""));
                                                   double foc_amount = 0;
                                                   if (focAmtEdit.getText().toString().trim().length() != 0) {
                                                       foc_amount = Double.parseDouble(focAmtEdit.getText().toString().trim().replace(",", ""));
                                                   }
                                                   String foc_description = null;
                                                   if (focdescription.getText().toString().trim().replace(",", "") != null) {
                                                       foc_description = focdescription.getText().toString().trim().replace(",", "");
                                                   }
                                                   if (memberID != null && foc_amount != 0) {
                                                       call = request.createMember(vouncherID, memberID, total_amount, tax_amount, service_amount, foc_amount, all_total_amount, foc_description, pay_amount, refund, dicount_amount);
                                                   } else if (memberID == null && foc_amount == 0) {
                                                       call = request.createMember(vouncherID, "null", total_amount, tax_amount, service_amount, 0, all_total_amount, "null", pay_amount, refund, 0);
                                                   } else if (memberID != null && foc_amount == 0) {
                                                       call = request.createMember(vouncherID, memberID, total_amount, tax_amount, service_amount, 0, all_total_amount, "null", pay_amount, refund, dicount_amount);
                                                       Log.d("param", "v id=" + vouncherID + "\n" + "m id=" + String.valueOf(memberID) + "\n" + "total amt=" + String.valueOf(total_amount) + "ser amt" + String.valueOf(service_amount) + "\n" + "all t amt=" + String.valueOf(all_total_amount) + "\n" + "refund=" + String.valueOf(refund) + "\n" + "dis amnt=" + String.valueOf(dicount_amount));
                                                   } else {
                                                       call = request.createMember(vouncherID, "null", total_amount, tax_amount, service_amount, foc_amount, all_total_amount, foc_description, pay_amount, refund, 0);
                                                   }
                                                   call.enqueue(new Callback<Success>() {
                                                       @Override
                                                       public void onResponse(Call<Success> call, Response<Success> response) {
                                                           try {
                                                               Success jsonResponse = response.body();
                                                               String message = jsonResponse.getMessage();
                                                               if (message.equals("Success")) {
                                                                   Log.d("Order", message);
                                                                   mProgressDialog.dismiss();
                                                                   String arg[] = {vouncherID};
                                                                   startActivity(new Intent(InvoiceDetailActivity.this, HomePageActivity.class));
                                                                   finish();
                                                               }
                                                           } catch (Exception e) {
                                                               e.printStackTrace();
                                                               mProgressDialog.dismiss();
                                                               callUploadDialog("Order data is null.");
                                                           }
                                                       }

                                                       @Override
                                                       public void onFailure(Call<Success> call, Throwable t) {
                                                           Log.d("Member", t.getMessage());
                                                           mProgressDialog.dismiss();
                                                           callUploadDialog("Please upload again!");
                                                       }
                                                   });
                                               } else {
                                                   callUploadDialog("Please check you payment amount!");
                                               }
                                           }
                                       }
                                   }
        );
    }

    private void setTotalAmt(double amt) {
        totalAmtTxt.setText(commaSepFormat.format(amt));
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

    private ArrayList<String> getGroupTableListInDB(String order_id) {
        final ArrayList<String> tableNameList = new ArrayList<>();
        final String tableid = null;
        RequestInterface requestInterfacefortable = retrofit.create(RequestInterface.class);
        Call<JsonForShowTableId> callfortable = requestInterfacefortable.getforshowOrderTable(getActivateKeyFromDB(), order_id);
        callfortable.enqueue(new Callback<JsonForShowTableId>() {
            @Override
            public void onResponse(Call<JsonForShowTableId> call, Response<JsonForShowTableId> response) {
                JsonForShowTableId jsonForShowTableId = response.body();
                download_forShow_tableIDs = jsonForShowTableId.getForShow_tableID();
                for (Download_forShow_tableID download_forShow_tableID : download_forShow_tableIDs) {
                    tableNameList.add(download_forShow_tableID.getTable_id());
                }
            }

            @SuppressLint("LongLogTag")
            @Override
            public void onFailure(Call<JsonForShowTableId> call, Throwable t) {
                Log.i("Error At Inserting TableID", "?!?!?!?!?!?!?");
            }
        });
        return tableNameList;
    }

    private String get_setmenu_id(String setmenuid) {
        String setidreturn = "";
        Cursor cursor = database.rawQuery("SELECT * FROM setMenu WHERE set_menu_name = '" + setmenuid + "'", null);
        while (cursor.moveToNext()) {
            setidreturn = cursor.getString(cursor.getColumnIndex("id"));
            Log.i("setidretrunnnnnnnn", setidreturn + "Whyyy");
        }
        return setidreturn;
    }


    private ArrayList<SetMenu_Item_for_dialog> get_setMeu_item_name(String setMenu_id) {
        ArrayList<SetMenu_Item_for_dialog> arrayList = new ArrayList<>();
        SetMenu_Item_for_dialog S_I_F_D = null;
        Cursor cursor = database.rawQuery("SELECT * FROM SetItem WHERE set_menu_id = '" + setMenu_id + "'", null);
        while (cursor.moveToNext()) {
            S_I_F_D = new SetMenu_Item_for_dialog();
            S_I_F_D.setSetMenu_Item_ID(cursor.getString(cursor.getColumnIndex("item_id")));
            Log.i("item_id......", S_I_F_D.getSetMenu_Item_ID() + ".......");
            S_I_F_D.setSetMenu_Item_Name(getSetMenuItemName(S_I_F_D.getSetMenu_Item_ID()));
            Log.i("item_name......", S_I_F_D.getSetMenu_Item_Name() + ".......");
            arrayList.add(S_I_F_D);
        }
        Log.i("Arr sixe>>>", arrayList.size() + "");
        return arrayList;
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
        for (Download_forShow_tableID download_forShow_tableID : download_orderTableArrayList) {
            tableeeID = (download_forShow_tableID.getTable_id());
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
            roommmID = (download_forShow_roomID.getRoom_id());
        }
        return roommmID;
    }

    private String getSetMenuItemName(String Set_Item_Name) {
        String SetItemReturn = "";
        Cursor cursor = database.rawQuery("SELECT * FROM Item WHERE id = '" + Set_Item_Name + "'", null);
        while (cursor.moveToNext()) {
            SetItemReturn = cursor.getString(cursor.getColumnIndex("name"));
        }
        return SetItemReturn;
    }

    // these are for view !!
    private class SetItemDetailAdapter extends ArrayAdapter<SetMenu_Item_for_dialog> {
        public Activity contxt;
        ArrayList<SetMenu_Item_for_dialog> setMenu_item_name = new ArrayList<>();

        public SetItemDetailAdapter(Activity contxt, ArrayList<SetMenu_Item_for_dialog> setmenudetailArrayList) {
            super(contxt, R.layout.invoice_detail_product_setitem_view_inner, setmenudetailArrayList);
            this.contxt = contxt;
            this.setMenu_item_name = setmenudetailArrayList;
            Log.i("Arr size in adapter>>>", setMenu_item_name.size() + "");
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final LayoutInflater layoutInflater = contxt.getLayoutInflater();
            View setview = layoutInflater.inflate(R.layout.invoice_detail_product_setitem_view_inner, null, true);
            TextView SetItemNameTxt = (TextView) setview.findViewById(R.id.set_item_name_txt);
            Button SetStatus = (Button) setview.findViewById(R.id.setitem_status_btn);
            SetMenu_Item_for_dialog setMenu_item_for_dialog = setMenu_item_name.get(position);
            SetItemNameTxt.setText(setMenu_item_for_dialog.getSetMenu_Item_Name());
            if (setMenu_item_for_dialog.getSetMenu_Item_Status().equals("6")) {
                SetItemNameTxt.setPaintFlags(SetItemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                SetStatus.setText("Disable");
                SetStatus.setBackgroundColor(Color.TRANSPARENT);
                SetStatus.setTextColor(getResources().getColor(R.color.order_disable));
                SetStatus.setEnabled(false);
                SetStatus.setEnabled(false);
            } else if (setMenu_item_for_dialog.getSetMenu_Item_Status().equals("2")) {
                SetStatus.setText("Cooking");
                SetStatus.setBackgroundColor(Color.TRANSPARENT);
                SetStatus.setTextColor(getResources().getColor(R.color.order_cooking));
                SetStatus.setEnabled(false);

            } else if (setMenu_item_for_dialog.getSetMenu_Item_Status().equals("3")) {
                SetStatus.setText("Cooked");
                SetStatus.setBackgroundColor(Color.TRANSPARENT);
                SetStatus.setTextColor(getResources().getColor(R.color.order_cooked));
                SetStatus.setEnabled(false);

            } else if (setMenu_item_for_dialog.getSetMenu_Item_Status().equals("4")) {
                SetStatus.setText("Serve");
                SetStatus.setBackgroundColor(Color.TRANSPARENT);
                SetStatus.setTextColor(getResources().getColor(R.color.order_serve));
                SetStatus.setEnabled(false);

            } else {
                SetStatus.setText("Cancel");
                SetStatus.setEnabled(false);

            }
            return setview;
        }
    }

    private class InvoiceDetailAdapter extends ArrayAdapter<InvoiceDetailProduct> {
        public Activity context;
        ArrayList<InvoiceDetailProduct> detailProductArrayList;

        public InvoiceDetailAdapter(Activity context, ArrayList<InvoiceDetailProduct> detailProductArrayList) {
            super(context, R.layout.invoice_detail_product_view, detailProductArrayList);
            this.context = context;
            this.detailProductArrayList = detailProductArrayList;
        }

        @SuppressLint("LongLogTag")
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final LayoutInflater layoutInflater = context.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.invoice_detail_product_view, null, true);
            TextView itemNameTxt = (TextView) view.findViewById(R.id.item_name_txt);
            TextView priceTxt = (TextView) view.findViewById(R.id.price_txt);
            TextView quantityTxt = (TextView) view.findViewById(R.id.qty_txt);
            TextView extraPriceTxt = (TextView) view.findViewById(R.id.extra_price_txt);
            TextView discountTxt = (TextView) view.findViewById(R.id.discount_txt);
            TextView amountTxt = (TextView) view.findViewById(R.id.amount_txt);
            Button statusBtn = (Button) view.findViewById(R.id.status_btn);
            final InvoiceDetailProduct detailProduct = detailProductArrayList.get(position);
            Log.i("detailProductArrayList>>>>hak>>>>", detailProductArrayList.size() + "");
            itemNameTxt.setText(detailProduct.getItemName());
            priceTxt.setText(detailProduct.getPrice());
            quantityTxt.setText(detailProduct.getQuantity());
            extraPriceTxt.setText(detailProduct.getExtraPrice());
            discountTxt.setText(detailProduct.getDiscount());
            amountTxt.setText(detailProduct.getAmount());
            Log.i("status>>>>h>>>>", detailProduct.getStatus().toString());
            Log.i("ID>>>>h>>>>", detailProduct.getId().toString());
            Log.i("NAME>>>>h>>>>", detailProduct.getItemName().toString());
            Log.i("PRICE>>>>h>>>>", detailProduct.getPrice().toString());
            Log.i("AMOUNT>>>>h>>>>", detailProduct.getAmount().toString());
            Log.i("EXTRA>>>>h>>>>", detailProduct.getExtraPrice().toString());
            final ArrayList<InvoiceDetailProductSetItem> invoiceDetailProductSetItemArrayList = detailProduct.getInvoiceDetailProductSetItemArrayList();
            final ArrayList<SetMenu_Item_for_dialog> setMenu_item_for_dialogArrayList = new ArrayList<>();
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (invoiceDetailProductSetItemArrayList == null) {
                        Toast.makeText(context, "This is not SetMenu !", Toast.LENGTH_SHORT).show();
                    } else {
                        setMenu_item_for_dialogArrayList.clear();
                        for (InvoiceDetailProductSetItem invoiceDetailProductSetItem : invoiceDetailProductSetItemArrayList) {
                            SetMenu_Item_for_dialog setMenu_item_for_dialog = new SetMenu_Item_for_dialog();
                            setMenu_item_for_dialog.setSetMenu_Item_ID(invoiceDetailProductSetItem.getItemId());
                            setMenu_item_for_dialog.setSetMenu_Item_Name(getSetMenuItemName(invoiceDetailProductSetItem.getItemId()));
                            setMenu_item_for_dialog.setSetMenu_Item_Status(invoiceDetailProductSetItem.getStatusId());
                            setMenu_item_for_dialogArrayList.add(setMenu_item_for_dialog);
                        }
                        set_MenuID = get_setmenu_id(detailProduct.getItemName());

                        ArrayList<SetMenu_Item_for_dialog> arrayList = get_setMeu_item_name(set_MenuID);

                        final LayoutInflater layoutInflater = context.getLayoutInflater();
                        View setview = layoutInflater.inflate(R.layout.invoice_detail_product_setitem_view, null, true);
                        ListView listView = (ListView) setview.findViewById(R.id.listViewSetItemName);
                        SetItemDetailAdapter setItemDetailAdapter = new SetItemDetailAdapter(InvoiceDetailActivity.this, setMenu_item_for_dialogArrayList);
                        listView.setAdapter(setItemDetailAdapter);
                        setItemDetailAdapter.notifyDataSetChanged();
                        if (arrayList.size() != 0) {
                            new AlertDialog.Builder(InvoiceDetailActivity.this)
                                    .setView(setview)
                                    .setPositiveButton("Ok", null)
                                    .show();
                        }

                    }


                }
            });
            if (detailProduct.getStatus().equals("6")) {
                itemNameTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                priceTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                quantityTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                extraPriceTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                discountTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                amountTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                statusBtn.setText("Disable");
                statusBtn.setBackgroundColor(Color.TRANSPARENT);
                statusBtn.setTextColor(getResources().getColor(R.color.order_disable));
                statusBtn.setEnabled(false);
                statusBtn.setEnabled(false);

            } else if (detailProduct.getStatus().equals("2")) {
                statusBtn.setText("Cooking");
                statusBtn.setBackgroundColor(Color.TRANSPARENT);
                statusBtn.setTextColor(getResources().getColor(R.color.order_cooking));
                statusBtn.setEnabled(false);
                paidavailable = false;
            } else if (detailProduct.getStatus().equals("3")) {
                statusBtn.setText("Cooked");
                statusBtn.setBackgroundColor(Color.TRANSPARENT);
                statusBtn.setTextColor(getResources().getColor(R.color.order_cooked));
                statusBtn.setEnabled(false);
                paidavailable = false;
            } else if (detailProduct.getStatus().equals("4")) {
                statusBtn.setText("Serve");
                statusBtn.setBackgroundColor(Color.TRANSPARENT);
                statusBtn.setTextColor(getResources().getColor(R.color.order_serve));
                statusBtn.setEnabled(false);

            } else if (detailProduct.getStatus().equals("7")) {
                itemNameTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                priceTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                quantityTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                extraPriceTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                discountTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                amountTxt.setPaintFlags(itemNameTxt.getPaintFlags() | STRIKE_THRU_TEXT_FLAG);
                statusBtn.setText("Cancel");
                statusBtn.setBackgroundColor(Color.TRANSPARENT);
                statusBtn.setTextColor(getResources().getColor(R.color.order_disable));
                statusBtn.setEnabled(false);
                statusBtn.setEnabled(false);

            } else {
                statusBtn.setText("Cancel");
                statusBtn.setEnabled(true);
                paidavailable = false;
            }
            Log.i("testing >>>>>>>>>>", detailProduct.getStatus() + "");
            final String order_detail_id = detailProduct.getId();
            Log.e("order_detail_id", order_detail_id + "");
            statusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final AlertDialog builder = new AlertDialog.Builder(InvoiceDetailActivity.this, R.style.InvitationDialog)
                            .setPositiveButton(R.string.invitation_ok, null)
                            .setNegativeButton(R.string.invitation_cancel, null)
                            .create();
                    builder.setTitle(R.string.clear);
                    builder.setMessage("Do you want to cancel this item?");
                    builder.setOnShowListener(new DialogInterface.OnShowListener() {
                        @Override
                        public void onShow(DialogInterface dialog) {
                            final Button btnAccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                            btnAccept.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    int a=0;

                                  for (int i=0;i<detailProductArrayList.size();i++){

                                      if (!"7".equals(detailProductArrayList.get(i).getStatus())){

                                         a++;

                                      }

                                  }


                                    if (a > 1 ) {
                                        double amt = Double.parseDouble(detailProductArrayList.get(position).getAmount().trim().replaceAll(",", ""));
                                        Log.e("IAmount", amt + "");
                                        double exa = Double.parseDouble(detailProductArrayList.get(position).getExtraPrice().trim().replaceAll(",", ""));
                                        Log.e("IExtra", exa + "");
                                        int quantity = Integer.parseInt(detailProductArrayList.get(position).getQuantity().trim().replaceAll(",", ""));
                                        double dis = Double.parseDouble(detailProductArrayList.get(position).getDiscount().trim().replaceAll(",", ""));
                                        Log.e("IDiscount", dis + "");
                                        double totalAmt = Double.parseDouble(totalAmtTxt.getText().toString().trim().replaceAll(",", ""));
                                        //double totalExa = Double.parseDouble(totalExtraTxt.getText().toString().trim().replaceAll(",", ""));
                                        //double totalDis = Double.parseDouble(totalDisTxt.getText().toString().trim().replaceAll(",", ""));
                                        //double memberAmt = Double.parseDouble(totalMemberDisTxt.getText().toString().trim().replaceAll(",", ""));
                                        final double totalAmtValue = totalAmt - amt;
                                        final double totalExaValue = Double.parseDouble(totalExtra) - (exa * quantity);
                                        final double totalDisValue = Double.parseDouble(totalDiscount) - (dis * quantity);
                                        final double taxValue = totalAmtValue * taxAmt / 100;
                                        final double serviceValue = totalAmtValue * serviceAmt / 100;
                                        final double totalNetValue = (totalAmtValue + taxValue + serviceValue) /*- (totalDisValue *//*+ memberAmt*//*)*/;
                                        final JSONObject jsonObject = new JSONObject();
                                        try {
                                            jsonObject.put("order_id", vouncherID);
                                            jsonObject.put("order_detail_id", order_detail_id + "");
                                            jsonObject.put("service_amount", serviceValue);
                                            jsonObject.put("tax_amount", taxValue);
                                            jsonObject.put("total_amount", totalAmtValue);
                                            jsonObject.put("net_amount", totalNetValue);
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                        Log.e("CustomerCancel", jsonObject + "");
                                        mProgressDialog.setMessage("Uploading customer cancel item....");
                                        mProgressDialog.show();
                                        RequestInterface request = retrofit.create(RequestInterface.class);
                                        Call<Success> call = request.postCustomerCancle(jsonObject + "");
                                        call.enqueue(new Callback<Success>() {
                                            @Override
                                            public void onResponse(Call<Success> call, Response<Success> response) {
                                                try {
                                                    Success jsonResponse = response.body();
                                                    String message = jsonResponse.getMessage();
                                                    if (message.equals("Success")) {
                                                        Log.d("CustomerCancel", message);
                                                        mProgressDialog.dismiss();
                                                        String arg[] = {order_detail_id};

                                                        detailProductArrayList.remove(position);
                                                        invoiceDetailAdapter.notifyDataSetChanged();
                                                        taxTxt.setText(commaSepFormat.format(taxValue));
                                                        serviceTxt.setText(commaSepFormat.format(serviceValue));
                                                        totalAmtTxt.setText(commaSepFormat.format(totalAmtValue));
                                                        //totalExtraTxt.setText(commaSepFormat.format(totalExaValue));
                                                        //totalDisTxt.setText(commaSepFormat.format(totalDisValue));
                                                        netAmtTxt.setText(commaSepFormat.format(totalNetValue));
                                                        String arg2[] = {vouncherID};

                                                        Handler handler = new Handler();

                                                        handler.post(new Runnable() {
                                                            @Override
                                                            public void run() {
                                                                socket.emit("order_cancel", vouncherID);
                                                              //  Toast.makeText(InvoiceDetailActivity.this, "SocketFire", Toast.LENGTH_SHORT).show();
                                                            }
                                                        });

                                                    } else {
                                                        mProgressDialog.dismiss();
                                                        callUploadDialog("This item is cooking now.", InvoiceDetailActivity.this);
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                    mProgressDialog.dismiss();
                                                    callUploadDialog("Item status data is null.", InvoiceDetailActivity.this);
                                                }
                                            }

                                            @Override
                                            public void onFailure(Call<Success> call, Throwable t) {
                                                Log.d("CustomerCancel", t.getMessage());
                                                mProgressDialog.dismiss();
                                                callUploadDialog("Please upload again!", InvoiceDetailActivity.this);
                                            }
                                        });
                                    } else {
                                        final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(InvoiceDetailActivity.this, R.style.InvitationDialog)
                                                .setPositiveButton(R.string.invitation_ok, null)
                                                .create();
                                        builder.setTitle(R.string.alert);
                                        builder.setMessage("You can't delete any more!");
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
            return view;
        }
    }


    private void callUploadDialog(final String message, final Activity activity) {

        Handler handler = new Handler();

        handler.post(new Runnable() {
            @Override
            public void run() {
                if (activity.isFinishing()) {
                    return;
                } else {
                    try {
                        final android.support.v7.app.AlertDialog builder = new android.support.v7.app.AlertDialog.Builder(InvoiceDetailActivity.this, R.style.InvitationDialog)
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
            }
        });
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
            startActivity(new Intent(InvoiceDetailActivity.this, InvoiceActivity.class));
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(InvoiceDetailActivity.this, InvoiceActivity.class));
        finish();
    }
}
