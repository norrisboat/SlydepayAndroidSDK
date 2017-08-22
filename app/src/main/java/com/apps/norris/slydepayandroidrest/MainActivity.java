package com.apps.norris.slydepayandroidrest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.apps.norris.paywithslydepay.core.PayWithSlydepay;
import com.apps.norris.paywithslydepay.core.SlydepayPayment;

public class MainActivity extends AppCompatActivity {

    private int PAY_WITH_SLYDEPAY = 90;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PayWithSlydepay.Pay(MainActivity.this,"Iphone",1.00,"64 gb gold","Norris Aboagye","norrisboat@gmail.com","","233553311070",PAY_WITH_SLYDEPAY);
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==PAY_WITH_SLYDEPAY){
            switch (resultCode){
                case RESULT_OK:
                    //Payment was successful
                    Toast.makeText(this, "Was successful", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_CANCELED:
                    //Payment failed
                    Toast.makeText(this, "Payment Failed", Toast.LENGTH_SHORT).show();
                    break;
                case RESULT_FIRST_USER:
                    //Payment was cancelled by user
                    Toast.makeText(this, "User Canceled", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
}
