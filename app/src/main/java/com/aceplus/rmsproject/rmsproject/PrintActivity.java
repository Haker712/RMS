package com.aceplus.rmsproject.rmsproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.aceplus.rmsproject.rmsproject.object.InvoiceDetailProduct;

import java.util.ArrayList;

/**
 * Created by PhyoKyawSwar on 4/3/17.
 */

public class PrintActivity extends ActionBarActivity {

    private Toolbar toolbar;
    private ProgressDialog mProgressDialog;
    private Button onprintBtn;
    /*public static String print_datetime = null;
    public static String print_voucherID = null;
    public static String print_tableNo = null;
    public static String print_staffName = null;
    public static String print_tax = null;
    public static String print_service = null;
    public static String print_totalAmount = null;
    public static String print_netAmount = null;*/

    private TextView datetimetxt;
    private TextView voucgerIDtxt;
    private TextView tablenotxt;
    private TextView staffnametxt;
    private TextView taxtxt;
    private TextView servicetxt;
    private TextView totalamttxt;
    private TextView netamttxt;
    private ListView productListView;

    printDetailAdapter PrintDetailAdapter;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_print);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        registerIDs();
        setAdapter();
        catchEvents();
    }

    private void callDialog(String messageTxt) {
        mProgressDialog = new ProgressDialog(PrintActivity.this, ProgressDialog.THEME_HOLO_LIGHT);
        mProgressDialog.setIndeterminate(false);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage(messageTxt);
        mProgressDialog.show();
    }

    private void registerIDs() {
        onprintBtn = (Button) findViewById(R.id.onprintBtn);
        datetimetxt  = (TextView) findViewById(R.id.datetimetxt);
        voucgerIDtxt  =  (TextView) findViewById(R.id.voucherIDtxt);
        tablenotxt    = (TextView) findViewById(R.id.tablenotxt);
        staffnametxt   = (TextView) findViewById(R.id.staffnametxt);
        taxtxt         = (TextView) findViewById(R.id.taxtxt);
        servicetxt    = (TextView) findViewById(R.id.servicetxt);
        totalamttxt   = (TextView) findViewById(R.id.totalamttxt);
        netamttxt     = (TextView) findViewById(R.id.netamttxt);
    }

    private void catchEvents() {

        Intent i = getIntent();

        Bundle extras = i.getExtras();

        datetimetxt.setText((String) extras.get("datetime"));
        voucgerIDtxt.setText((String) extras.get("voucherID"));
        tablenotxt.setText((String) extras.get("tableNo"));
        staffnametxt.setText((String) extras.get("staffName"));
        taxtxt.setText((String) extras.get("taxAmt"));
        servicetxt.setText((String) extras.get("serviceAmt"));
        totalamttxt.setText((String) extras.get("totalAmt"));
        netamttxt.setText((String) extras.get("netAmt"));
    }

    private void setAdapter() {
        PrintDetailAdapter = new PrintActivity.printDetailAdapter(PrintActivity.this, InvoiceActivity.detailProductArrayList);
        productListView = (ListView) findViewById(R.id.print_list_view);
        productListView.setAdapter(PrintDetailAdapter);
        PrintDetailAdapter.notifyDataSetChanged();

    }

    private class printDetailAdapter extends ArrayAdapter<InvoiceDetailProduct>{
        public Activity contextt;
        ArrayList<InvoiceDetailProduct> detailproductaerraylist;
        public printDetailAdapter(PrintActivity printActivity, ArrayList<InvoiceDetailProduct> detailProductArrayList) {
            super(printActivity, R.layout.print_product_view, detailProductArrayList);
            this.contextt = printActivity;
            this.detailproductaerraylist = detailProductArrayList;
        }
        public View getView (final int position, View convertView, ViewGroup parent){
            final LayoutInflater layoutInflater = contextt.getLayoutInflater();
            View view = layoutInflater.inflate(R.layout.print_product_view, null, true);
            TextView itemNameTxt = (TextView) view.findViewById(R.id.print_item_name_txt);
            TextView priceTxt = (TextView) view.findViewById(R.id.print_price_txt);
            TextView quantityTxt = (TextView) view.findViewById(R.id.print_qty_txt);
            TextView extraPriceTxt = (TextView) view.findViewById(R.id.print_extra_price_txt);
            TextView discountTxt = (TextView) view.findViewById(R.id.print_discount_txt);
            TextView amountTxt = (TextView) view.findViewById(R.id.print_amount_txt);
            final InvoiceDetailProduct detailProduct = detailproductaerraylist.get(position);
            itemNameTxt.setText(detailProduct.getItemName());
            priceTxt.setText(detailProduct.getPrice());
            quantityTxt.setText(detailProduct.getQuantity());
            extraPriceTxt.setText(detailProduct.getExtraPrice());
            discountTxt.setText(detailProduct.getDiscount());
            amountTxt.setText(detailProduct.getAmount());
            return view;
        }
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
        final AlertDialog builder = new AlertDialog.Builder(PrintActivity.this, R.style.InvitationDialog)
                .setPositiveButton("Yes", null)
                .setNegativeButton("No", null)
                .create();
        builder.setTitle("");
        builder.setMessage("Are you finish printing ?");
        builder.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                final Button btnaccept = builder.getButton(AlertDialog.BUTTON_POSITIVE);
                btnaccept.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(PrintActivity.this, HomePageActivity.class));
                    }
                });
                final Button btncancel = builder.getButton(AlertDialog.BUTTON_NEGATIVE);
                btncancel.setOnClickListener(new View.OnClickListener() {
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
