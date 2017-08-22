package com.apps.norris.paywithslydepay.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.norris.paywithslydepay.R;
import com.apps.norris.paywithslydepay.adapters.PaymentOptionsAdapter;
import com.apps.norris.paywithslydepay.core.PayWithSlydepay;
import com.apps.norris.paywithslydepay.models.PaymentOption;
import com.apps.norris.paywithslydepay.offline.PrefsManager;
import com.apps.norris.paywithslydepay.utils.ConnectivityUtils;
import com.apps.norris.paywithslydepay.utils.JSONUtils;
import com.apps.norris.paywithslydepay.views.MaterialProgressBar;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class PaymentChoiceFragment extends Fragment {

    private ArrayList<PaymentOption> paymentOptions;
    private RecyclerView recyclerView;
    private MaterialProgressBar progressBar;

    public PaymentChoiceFragment() {
        // Required empty public constructor
    }

    public static PaymentChoiceFragment newInstance() {
        return new PaymentChoiceFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_choice, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.paymentOptions);
        progressBar = (MaterialProgressBar) view.findViewById(R.id.progress_bar);

        paymentOptions = new ArrayList<>();

        if (new ConnectivityUtils(getContext()).isNetworkConnected())
            new GetPaymentOptions().execute();

        return view;
    }

    private void setUpAdapter() {
        PaymentOptionsAdapter adapter = new PaymentOptionsAdapter((PayWithSlydepay) getContext(),paymentOptions);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);
    }


    private class GetPaymentOptions extends AsyncTask<String, String, String> {

        private PrefsManager prefsManager = new PrefsManager(getContext());

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            URL httpbinEndpoint;
            try {

                httpbinEndpoint = new URL("https://app.slydepay.com.gh/api/merchant/invoice/payoptions");
                HttpsURLConnection myConnection= (HttpsURLConnection) httpbinEndpoint.openConnection();
                myConnection.setRequestProperty("Content-Type","application/json");

                myConnection.setRequestMethod("POST");

                JSONObject invoice = new JSONObject();
                invoice.put("emailOrMobileNumber",prefsManager.getMerchantEmailOrPhoneNumber());
                invoice.put("merchantKey", prefsManager.getMerchantKey());

                String myData = invoice.toString();

                // Enable writing
                myConnection.setDoOutput(true);

                // Write the data
                myConnection.getOutputStream().write(myData.getBytes());

                InputStream responseBody = myConnection.getInputStream();

                BufferedReader bR = new BufferedReader(  new InputStreamReader(responseBody));
                String line;

                StringBuilder responseStrBuilder = new StringBuilder();
                while((line =  bR.readLine()) != null){

                    responseStrBuilder.append(line);
                }
                responseBody.close();

                JSONObject result= new JSONObject(responseStrBuilder.toString());
                Log.v("norristest",result.toString());

                if (result.getBoolean("success")){
                    paymentOptions = JSONUtils.getPaymentOptions(result);
                }

            }catch (Exception e){
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            progressBar.setVisibility(View.GONE);
            setUpAdapter();
        }
    }

}
