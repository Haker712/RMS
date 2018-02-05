package com.aceplus.rmsproject.rmsproject.object;


import com.aceplus.rmsproject.rmsproject.utils.Utils;

/**
 * Created by macmininew on 12/7/16.
 */

public final class CsvLogModel {
    public String dateAndTime;
    public String apiName;
    public String jsonData;
    public String errorMessage;

    public CsvLogModel() {
        dateAndTime = Utils.getCurrentDate(true);
    }

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getDateAndTime() {
        return dateAndTime;
    }
}
