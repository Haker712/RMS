package com.aceplus.rmsproject.rmsproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.aceplus.rmsproject.rmsproject.object.CsvLogModel;
import com.aceplus.rmsproject.rmsproject.object.LogUploadReturn;
import com.aceplus.rmsproject.rmsproject.utils.Database;
import com.aceplus.rmsproject.rmsproject.utils.JsonForShowTableId;
import com.aceplus.rmsproject.rmsproject.utils.RequestInterface;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendExceptionActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private String tabletId;
    SQLiteDatabase database;

    SharedPreferences prefs;
    String BACKEND_URL="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_exception);


        database = new Database(this).getDataBase();

        prefs = getSharedPreferences("MYPRE", MODE_PRIVATE);

        if (prefs.getString("BACKEND_URL", "") != null) {
            BACKEND_URL = prefs.getString("BACKEND_URL", "");
        }


        Intent intent = getIntent();
        final String Exp = intent.getStringExtra("ConstantKey");

        TextView LogTxtView = (TextView) findViewById(R.id.LogTxtView);
        LogTxtView.setText(Exp);

        Log.i("Exp", Exp);


        tabletId = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);

        Log.i("TBID", tabletId);

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
        try {
            Log.i("URL", BACKEND_URL + "[space]");

            retrofit = new Retrofit.Builder()
                    .baseUrl(BACKEND_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(client)
                    .build();


            RequestInterface requestInterfacefortable = retrofit.create(RequestInterface.class);
            Call<LogUploadReturn> call = requestInterfacefortable.LOG_UPLOAD_CALL(tabletId, Exp);
            call.enqueue(new Callback<LogUploadReturn>() {
                @Override
                public void onResponse(Call<LogUploadReturn> call, Response<LogUploadReturn> response) {
                    if (response.code() == 200) {

                        if (response.body().getMessage().equals("Success")) {

                            Toast.makeText(SendExceptionActivity.this, "Success", Toast.LENGTH_SHORT).show();

                            CsvLogModel logModel = new CsvLogModel();
                            logModel.setApiName(call.request().url().toString());
                            logModel.setErrorMessage("successfull upload data");
                            logModel.setJsonData("{tabletId:"+tabletId+", exp:"+Exp+"}");
                            DataLogWriter.newInstance().writeLog(logModel);

                        }
                    }
                }

                @Override
                public void onFailure(Call<LogUploadReturn> call, Throwable t) {

                    CsvLogModel logModel = new CsvLogModel();
                    logModel.setApiName(call.request().url().toString());
                    logModel.setErrorMessage("successfull upload data");
                    logModel.setJsonData("{tabletId:"+tabletId+", exp:"+Exp+"}");
                    DataLogWriter.newInstance().writeLog(logModel);




                }
            });
        } catch (Exception e) {
            Log.i("exception", e.getMessage());
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
}
