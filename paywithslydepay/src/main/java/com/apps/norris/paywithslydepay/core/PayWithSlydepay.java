package com.apps.norris.paywithslydepay.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.customtabs.CustomTabsIntent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.apps.norris.paywithslydepay.R;
import com.apps.norris.paywithslydepay.fragments.ConfirmPaymentFragment;
import com.apps.norris.paywithslydepay.fragments.ConfirmingTransactionFragment;
import com.apps.norris.paywithslydepay.fragments.LoadingFragment;
import com.apps.norris.paywithslydepay.fragments.MobileMoneyFragment;
import com.apps.norris.paywithslydepay.fragments.PaymentChoiceFragment;
import com.apps.norris.paywithslydepay.fragments.TransactionResponseFragment;
import com.apps.norris.paywithslydepay.offline.PrefsManager;
import com.apps.norris.paywithslydepay.utils.Constants;
import com.apps.norris.paywithslydepay.utils.PaymentUtils;
import com.apps.norris.paywithslydepay.views.CustomTextView;
import com.apps.norris.paywithslydepay.views.CustomViewPager;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class PayWithSlydepay extends AppCompatActivity {

    public CustomTextView next;
    private ImageView close;
    public CustomViewPager viewpager;
    private int count = 0;
    private String token="";
    private String paymentCode="";
    public String paymentOption="";
    public String transactionResponse="";
    private final int CHROME_CUSTOM_TAB_REQUEST_CODE = 100;
    private PrefsManager prefsManager;
    private static  int RESULT_CODE;

    private boolean successful;

    static String getCustomerName;
    static double getAmount;
    static String getDescription;
    static String itemCode;
    static String getEmail;
    public String getPhoneNumber;
    static String getItemName;

    private final int MOBILE_MONEY_PAYMENT = 1;
    private final int CONFIRM_PAYMENT = 3;
    private final int LOADING = 2;
    private final int CHECKING_INVOICE = 4;
    private final int TRANSACTION_RESPONSE = 6;

    public static void Pay(Activity context,String itemName,double amount,String description ,String customerName,String customerEmail,String orderCode,String phoneNumber,int setRequestCode){
        Bundle bundle = new Bundle();
        bundle.putDouble(Constants.AMOUNT,amount);
        bundle.putString(Constants.DESCRIPTION,description);
        bundle.putString(Constants.CUSTOMER_NAME,customerName);
        bundle.putString(Constants.CUSTOMER_EMAIL,customerEmail);
        bundle.putString(Constants.CUSTOMER_PHONE_NUMBER,phoneNumber);
        bundle.putString(Constants.ORDER_CODE,orderCode);
        bundle.putString(Constants.ITEM_NAME,itemName);
        bundle.putInt(Constants.REQUEST_CODE,setRequestCode);

        PayWithSlydepay.start(context,bundle);
    }

    public static void start(Activity context, Bundle bundle) {
        Intent intent = new Intent(context, PayWithSlydepay.class);
        intent.putExtras(bundle);
        context.startActivityForResult(intent,bundle.getInt(Constants.REQUEST_CODE));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_with_slydepay);
        initView();
        setupTabs();
        initListeners();
    }

    private void initView() {
        next = (CustomTextView) findViewById(R.id.next);
        next.setTextColor(getResources().getColor(android.R.color.white));
        viewpager = (CustomViewPager) findViewById(R.id.viewpager);
        close = (ImageView) findViewById(R.id.close);

        prefsManager = new PrefsManager(PayWithSlydepay.this);

        Intent intent = getIntent();
        getDescription = intent.getStringExtra(Constants.DESCRIPTION);
        getCustomerName = intent.getStringExtra(Constants.CUSTOMER_NAME);
        getEmail = intent.getStringExtra(Constants.CUSTOMER_EMAIL);
        itemCode = intent.getStringExtra(Constants.ORDER_CODE);
        getPhoneNumber = intent.getStringExtra(Constants.CUSTOMER_PHONE_NUMBER);
        getItemName = intent.getStringExtra(Constants.ITEM_NAME);
        getAmount = intent.getDoubleExtra(Constants.AMOUNT,0);

        if (TextUtils.isEmpty(getPhoneNumber))
            getPhoneNumber = "233";

        if (TextUtils.isEmpty(itemCode))
            itemCode = PaymentUtils.generateOrderCode();

    }

    private void initListeners() {
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getTextViewValue().equalsIgnoreCase("DONE"))
                    setResults(RESULT_CODE);
                else if (getTextViewValue().equalsIgnoreCase("CONFIRM")){
                    next.setEnabled(true);
                    viewpager.setCurrentItem(CHECKING_INVOICE);
                    new CheckInvoiceStatus().execute();
                }
                else if (getTextViewValue().equalsIgnoreCase("MAKE PURCHASE")){
                    viewpager.setCurrentItem(LOADING);
                    hideKeyboard();
                    new CreateInvoice().execute();
                }
            }
        });

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setResults(RESULT_FIRST_USER);
            }
        });
    }

    private String getTextViewValue(){
        return next.getText().toString();
    }

    private void setupTabs() {
        paymentTabAdapter adapter = new paymentTabAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(5);
        viewpager.setPagingEnabled(false);
        viewpager.setOffscreenPageLimit(0);

        viewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == MOBILE_MONEY_PAYMENT){

                    if (next.getVisibility() == View.GONE)
                        next.setVisibility(View.VISIBLE);

                    next.setText("MAKE PURCHASE");

                    next.setEnabled(getPhoneNumber.length() == 12);

                }else if (position == LOADING){
                    next.setVisibility(View.GONE);
                }else if (position == CHECKING_INVOICE){
                    next.setVisibility(View.GONE);
                }else if (position == TRANSACTION_RESPONSE){
                    if (next.getVisibility() == View.GONE)
                        next.setVisibility(View.VISIBLE);

                    next.setText("DONE");
                }else if (position == CONFIRM_PAYMENT){
                    if (next.getVisibility() == View.GONE)
                        next.setVisibility(View.VISIBLE);

                    next.setText("CONFIRM");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void startTransaction(){
        if (paymentOption.equalsIgnoreCase(Constants.VISA)){
            new CreateInvoice().execute();
            viewpager.setCurrentItem(LOADING);
        }else {
            viewpager.setCurrentItem(MOBILE_MONEY_PAYMENT);
        }
    }

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private class paymentTabAdapter extends FragmentPagerAdapter {

        paymentTabAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

        @Override
        public int getCount() {
            return 7;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == LOADING){
                String msg;
                if (paymentOption.equalsIgnoreCase(Constants.VISA))
                    msg = getResources().getString(R.string.visa_loading_message);
                else
                    msg = getResources().getString(R.string.mm_loading_message);
                return LoadingFragment.newInstance(msg);
            } else if (position == MOBILE_MONEY_PAYMENT)
                return MobileMoneyFragment.newInstance();
            else if (position == CONFIRM_PAYMENT){
                String msg,title;
                title = "CONFIRM PAYMENT";
                if (paymentOption.equalsIgnoreCase(Constants.VISA))
                    msg = getResources().getString(R.string.visa_confirm_message);
                else
                    msg = getResources().getString(R.string.mm_confirm_message);
                return ConfirmPaymentFragment.newInstance(title,msg);
            }
            else if (position == CHECKING_INVOICE)
                return ConfirmingTransactionFragment.newInstance("Confirming Transaction...");
            else if (position == TRANSACTION_RESPONSE)
                return TransactionResponseFragment.newInstance(successful,transactionResponse);
            else
                return PaymentChoiceFragment.newInstance();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHROME_CUSTOM_TAB_REQUEST_CODE) {
            viewpager.setCurrentItem(CONFIRM_PAYMENT);
        }
    }

    public void setResults(int resultCodeHere){
        Intent intent = new Intent();
        //intent.putExtra(PayWithUiUtils.ORDER_ID,orderId);
        setResult(resultCodeHere, intent);
        this.finish();
    }

    private class CreateInvoice extends AsyncTask<String, String, String> {

        private int success;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            URL httpbinEndpoint;
            try {

                httpbinEndpoint = new URL("https://app.slydepay.com/api/merchant/invoice/create");
                HttpsURLConnection myConnection= (HttpsURLConnection) httpbinEndpoint.openConnection();
                myConnection.setRequestProperty("Content-Type","application/json");

                myConnection.setRequestMethod("POST");

                JSONObject invoice = new JSONObject();
                invoice.put("emailOrMobileNumber",prefsManager.getMerchantEmailOrPhoneNumber());
                invoice.put("merchantKey",prefsManager.getMerchantKey());
                invoice.put("amount", getAmount);
                invoice.put("description",getDescription);
                invoice.put("orderCode", PaymentUtils.generateOrderCode());
                invoice.put("sendInvoice",true);
                invoice.put("payOption",paymentOption);
                invoice.put("customerName",getCustomerName);

                if (paymentOption.equalsIgnoreCase(Constants.VISA))
                    invoice.put("customerEmail", getEmail);
                else{
                    invoice.put("customerMobileNumber",getPhoneNumber);
                }

                String myData = invoice.toString();

                // Enable writing
                myConnection.setDoOutput(true);

                // Write the data
                myConnection.getOutputStream().write(myData.getBytes());

                success = 1;
                InputStream responseBody = myConnection.getInputStream();

                BufferedReader bR = new BufferedReader(  new InputStreamReader(responseBody));
                String line;

                StringBuilder responseStrBuilder = new StringBuilder();
                while((line =  bR.readLine()) != null){

                    responseStrBuilder.append(line);
                }
                responseBody.close();

                JSONObject result= new JSONObject(responseStrBuilder.toString());

                if (result.getBoolean("success")){
                    success = 1;
                    JSONObject result1 = new JSONObject(result.getString("result"));
                    token = result1.getString("payToken");
                    paymentCode = result1.getString("orderCode");

                }else {
                    success = 0;
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (success == 1){
                if (paymentOption.equalsIgnoreCase(Constants.VISA)){
                    String url = "https://app.slydepay.com/paylive/detailsnew.aspx?pay_token="+token;
                    CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
                    builder.setToolbarColor(getResources().getColor(R.color.slydeGreen));
                    CustomTabsIntent customTabsIntent = builder.build();
                    customTabsIntent.intent.setData(Uri.parse(url));
                    startActivityForResult(customTabsIntent.intent, CHROME_CUSTOM_TAB_REQUEST_CODE);
                }else {
                    viewpager.setCurrentItem(CONFIRM_PAYMENT);
                }
            }else {
                RESULT_CODE = RESULT_CANCELED;
            }
        }
    }

    private class CheckInvoiceStatus  extends AsyncTask<String, String, String> {

        private int success;
        private String status="";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            URL httpbinEndpoint;
            try {

                httpbinEndpoint = new URL("https://app.slydepay.com/api/merchant/invoice/checkstatus");
                HttpsURLConnection myConnection= (HttpsURLConnection) httpbinEndpoint.openConnection();
                myConnection.setRequestProperty("Content-Type","application/json");

                myConnection.setRequestMethod("POST");

                JSONObject invoice = new JSONObject();
                invoice.put("emailOrMobileNumber",prefsManager.getMerchantEmailOrPhoneNumber());
                invoice.put("merchantKey",prefsManager.getMerchantKey());
                invoice.put("payToken",token);
                invoice.put("orderCode", paymentCode);
                invoice.put("confirmTransaction",true);

                String myData = invoice.toString();

                // Enable writing
                myConnection.setDoOutput(true);

                // Write the data
                myConnection.getOutputStream().write(myData.getBytes());

                success = 1;
                InputStream responseBody = myConnection.getInputStream();

                BufferedReader bR = new BufferedReader(  new InputStreamReader(responseBody));
                String line;

                StringBuilder responseStrBuilder = new StringBuilder();
                while((line =  bR.readLine()) != null){

                    responseStrBuilder.append(line);
                }
                responseBody.close();

                JSONObject result= new JSONObject(responseStrBuilder.toString());
                Log.v("norristest","payment result "+result.toString());

                if (result.getBoolean("success")){
                    success = 1;
                    status = result.getString("result");
                    return result.getString("errorMessage");
                }else {
                    success = 0;
                    return result.getString("errorMessage");
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (success == 1){
                if (status.equalsIgnoreCase(Constants.CONFIRMED)){
                    successful = true;
                    RESULT_CODE = RESULT_OK;
                    transactionResponse = String.format(getResources().getString(R.string.successful_purchase),getItemName,getAmount);
                    viewpager.setCurrentItem(TRANSACTION_RESPONSE);

                }else {
                    if (count != 7){
                        new CheckInvoiceStatus().execute();
                        count++;
                    }else{
                        successful = false;
                        transactionResponse = "Transaction failed";
                        RESULT_CODE = RESULT_CANCELED;
                        viewpager.setCurrentItem(TRANSACTION_RESPONSE);
                    }
                }
            }else
                setResults(RESULT_CANCELED);
        }
    }

}
