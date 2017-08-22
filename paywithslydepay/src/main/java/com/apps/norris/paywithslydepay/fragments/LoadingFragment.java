package com.apps.norris.paywithslydepay.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.apps.norris.paywithslydepay.R;
import com.apps.norris.paywithslydepay.views.CustomTextView;

public class LoadingFragment extends Fragment {

    private static final String ARG_MESSAGE = "message";
    private String message;

    public static LoadingFragment newInstance(String message) {
        Bundle args = new Bundle();
        args.putString(ARG_MESSAGE,message);
        LoadingFragment fragment = new LoadingFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public LoadingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_MESSAGE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_loading, container, false);

        CustomTextView msg = (CustomTextView) view.findViewById(R.id.message);
        msg.setText(message);

        return view;
    }

}
