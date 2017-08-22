package com.apps.norris.paywithslydepay.fragments;

import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.apps.norris.paywithslydepay.R;
import com.apps.norris.paywithslydepay.core.PayWithSlydepay;

public class MobileMoneyFragment extends Fragment {

    public MobileMoneyFragment() {
        // Required empty public constructor
    }

    public static MobileMoneyFragment newInstance() {
        return new MobileMoneyFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_mobile_money, container, false);

        final PayWithSlydepay context = (PayWithSlydepay)getContext();

        final EditText phoneNumber = (EditText) view.findViewById(R.id.phone);
        final TextInputLayout textInputEditText = (TextInputLayout) view.findViewById(R.id.text_input);

        phoneNumber.setText(context.getPhoneNumber);

        if (!(context.getPhoneNumber.length() == 12))
            textInputEditText.setError("Phone number invalid");

        phoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                boolean isValid = s.toString().trim().length() == 12;
                context.next.setEnabled(isValid);
                if (!isValid)
                    textInputEditText.setError("Phone number invalid");
                else{
                    textInputEditText.setError(null);
                    context.getPhoneNumber = phoneNumber.getText().toString();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        return view;
    }

}
