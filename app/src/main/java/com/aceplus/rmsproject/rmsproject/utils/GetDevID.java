package com.aceplus.rmsproject.rmsproject.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.telephony.TelephonyManager;

public class GetDevID 
{
	public static String getDevId(Context context) 
	{		
		TelephonyManager mTelephonyMgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);		
		return mTelephonyMgr.getDeviceId(); 
		
	//	simid =( mTelephonyMgr.getSimSerialNumber()==null) ? "" :mTelephonyMgr.getSimSerialNumber();		
	}

	public static String getActivateKeyFromDB(Context context) { // for activation key
		SQLiteDatabase database = new Database(context).getDataBase();
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
