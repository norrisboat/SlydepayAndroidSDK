package com.apps.norris.paywithslydepay.core;

import android.content.Context;

import com.apps.norris.paywithslydepay.offline.PrefsManager;

/**
 * The Slydepay Android REST sdk simplifies slydepay payments
 * for android developers.
 * @author  Sancrop Ltd.
 * @version 1.0
 * @since   2017-06-03
 */

public class SlydepayPayment {

    private Context context;

    public SlydepayPayment(Context context){
        this.context = context;
    }

    /**
     * This method is used to initialize the mobile money sdk with
     * the credentials provided by MTN that verifies you to make
     * sure you have the authorization to use this adk
     * @param merchantEmail This is the merchant email or phone number
     * @param merchantKey  This is the merchant key provided by Slydepay
     * @see String#toLowerCase()
     */
    public void initCredentials(String merchantEmail,String merchantKey){
        new PrefsManager(context).initCredentials(merchantEmail,merchantKey);
    }
}
