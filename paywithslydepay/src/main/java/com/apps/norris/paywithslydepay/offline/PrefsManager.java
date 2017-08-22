package com.apps.norris.paywithslydepay.offline;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import com.apps.norris.paywithslydepay.utils.Constants;

public class PrefsManager {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;

    private static final String MERCHANT_EMAIL_OR_PHONE_NUMBER = "email_or_phone";
    private static final String MERCHANT_KEY = "key";

    @SuppressLint("CommitPrefEdits")
    public PrefsManager(Context context) {
        int PRIVATE_MODE = 0;
        pref = context.getSharedPreferences(Constants.LOG_TAG, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void initCredentials(String merchantEmail,String merchantKey){
        editor.putString(MERCHANT_EMAIL_OR_PHONE_NUMBER,merchantEmail);
        editor.putString(MERCHANT_KEY,merchantKey);
        editor.apply();
    }

    public String getMerchantEmailOrPhoneNumber(){
        return pref.getString(MERCHANT_EMAIL_OR_PHONE_NUMBER,"");
    }

    public String getMerchantKey(){
        return pref.getString(MERCHANT_KEY,"");
    }
}
