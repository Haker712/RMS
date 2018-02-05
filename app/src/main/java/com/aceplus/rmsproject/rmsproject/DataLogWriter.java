package com.aceplus.rmsproject.rmsproject;

import android.os.Environment;
import android.util.Log;


import com.aceplus.rmsproject.rmsproject.object.CsvLogModel;
import com.aceplus.rmsproject.rmsproject.utils.Utils;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by macmininew on 12/7/16.
 */

public class DataLogWriter {
    public static DataLogWriter INSTANCE;

    DataLogWriter() {
        //empty constructor
    }

    public static DataLogWriter newInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DataLogWriter();

        }
        return INSTANCE;
    }


    public static void writeLog(CsvLogModel data) {
        try {
            CSVWriter csvWriter = new CSVWriter(new FileWriter(getPath(), false));
            List<String[]> dataString = new ArrayList<String[]>();
            //title for first line or file
            dataString.add(new String[]{"Date", "Api Name", "Data", "Error Message"});
            dataString.add(new String[]{data.getDateAndTime(), data.getApiName(), data.getJsonData(), data.getErrorMessage()});
            csvWriter.writeAll(dataString);
            csvWriter.close();
            Log.d("csvpath", getPath().getPath());
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public static File getPath() {
        String fileName = Utils.getCurrentDate(false);
        File sdCard = Environment.getExternalStorageDirectory();
//        File dir = new File(sdCard.getAbsolutePath() + "/" + Constant.LOG_DIRECTORY + "/");
        File dir = new File(sdCard.getAbsolutePath() + "/Constant/");
        if (!dir.isDirectory()) {
            dir.mkdirs();
        }
        File file = new File(dir, "/" + fileName + ".csv");
        return file;
    }
}
