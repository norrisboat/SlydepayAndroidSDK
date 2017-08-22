package com.apps.norris.paywithslydepay.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.apps.norris.paywithslydepay.R;
import com.apps.norris.paywithslydepay.views.CustomTextView;

public class TransactionResponseFragment extends Fragment {

    private static final String ARG_MESSAGE = "message";
    private static final String ARG_SUCCESS = "success";
    private boolean success;
    private String message;

    public static TransactionResponseFragment newInstance(boolean success,String message) {

        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE,message);
        args.putBoolean(ARG_SUCCESS,success);
        TransactionResponseFragment fragment = new TransactionResponseFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public TransactionResponseFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            success = getArguments().getBoolean(ARG_SUCCESS);
            message = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_transaction_response, container, false);

        CustomTextView titleTV = (CustomTextView) view.findViewById(R.id.title);
        CustomTextView messageTV = (CustomTextView) view.findViewById(R.id.message);
        ImageView status = (ImageView) view.findViewById(R.id.status);

        String title;
        if (success){
            title = "Success";
            status.setImageResource(R.drawable.success);
        } else{
            title = "Error!!";
            status.setImageResource(R.drawable.error);
        }

        titleTV.setText(title);
        messageTV.setText(message);

        return view;
    }

}
