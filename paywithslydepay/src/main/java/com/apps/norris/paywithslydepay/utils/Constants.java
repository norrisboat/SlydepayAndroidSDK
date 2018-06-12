package com.apps.norris.paywithslydepay.utils;

/**
 * This class contains various error codes for easy error handling
 */

public class Constants {

    public static final String LOG_TAG="SlydepayREST";

    //error codes
    public static final String TRANSACTION_SUCCESSFUL="100";

    //Payment variables
    public static final String MTN_MOBILE_MONEY="MTN_MONEY";
    public static final String VISA="VISA";

    public static final String NEW="NEW";
    //public static final String PENDING="PENDING";
    public static final String CONFIRMED="CONFIRMED";
    public static final String CANCELLED="CANCELLED";
    public static final String DISPUTED="DISPUTED";

    //bundles
    public static final String AMOUNT="amount";
    public static final String DESCRIPTION="description";
    public static final String CUSTOMER_NAME="name";
    public static final String CUSTOMER_EMAIL="email";
    public static final String CUSTOMER_PHONE_NUMBER="phone";
    public static final String ORDER_CODE="order_code";
    public static final String REQUEST_CODE="request_code";
    public static final String ITEM_NAME="name";
}
